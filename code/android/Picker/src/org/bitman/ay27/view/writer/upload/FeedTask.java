package org.bitman.ay27.view.writer.upload;

import android.os.AsyncTask;
import android.util.Log;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import org.bitman.ay27.PickerApplication;
import org.bitman.ay27.common.ContentType;
import org.bitman.ay27.module.form.AnswerForm;
import org.bitman.ay27.module.form.FeedForm;
import org.bitman.ay27.request.PostRequest;
import org.bitman.ay27.request.UrlGenerator;
import org.bitman.ay27.request.Urls;
import org.bitman.ay27.upload_download.MyUploadManager;
import org.bitman.ay27.upload_download.S_P_Callback;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.concurrent.Semaphore;

/**
 * Proudly to use Intellij IDEA.
 * Created by ay27 on 14/11/23.
 */
class FeedTask extends AsyncTask<Void, Double, String> {

    private String title;
    private String rawData;
    private long bookId = -1, questionId = -1;
    private int bookPage = -1;
    private ContentType type;
    private ArrayList<ImageUploadUtils.DataContainer> containers;
    private UploadCallback callback;
    private Semaphore uploadImageFinish;
    private Semaphore uploadContent;
    private volatile double progressNum = 0;
    private volatile double delta = 0;
    private MyUploadManager uploadManager = MyUploadManager.getInstance();
    private static final long MyUserID = PickerApplication.getMyUserId();

    // for question & note
    public FeedTask(String title, String rawData, long bookId, int bookPage, ContentType type, UploadCallback callback) {
        this.title = title;
        this.bookId = bookId;
        this.bookPage = bookPage;
        this.rawData = rawData;
        this.type = type;
        this.callback = callback;
        containers = new ArrayList<ImageUploadUtils.DataContainer>();
    }

    // for answer
    public FeedTask(String rawData, long questionId, ContentType type, UploadCallback callback) {
        this.rawData = rawData;
        this.questionId = questionId;
        this.type = type;
        this.callback = callback;
        containers = new ArrayList<ImageUploadUtils.DataContainer>();
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        callback.onPreExecute();
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        callback.onPostExecute();
    }

    @Override
    protected void onProgressUpdate(Double... values) {
        super.onProgressUpdate(values);
        callback.onProgressUpdate(values[0]);
    }

    // the main work is here.
    @Override
    protected String doInBackground(Void... params) {
        publishProgress(progressNum = 0);

        containers = ImageUploadUtils.findImagesInMd(rawData);

        // the delta of progress
        delta = 100 / (containers.size() + 1);

        // the initial permit number must be negative.
        // when the release time reach to containers.size(), the permit number must reach to 1.
        uploadImageFinish = new Semaphore(-(containers.size() - 1));
        uploadImages();
        try {
            uploadImageFinish.acquire();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        // finish upload images, then replace the image url
        rawData = ImageUploadUtils.replaceImageUrlsInId(rawData, containers);

        // upload the content
        uploadContent = new Semaphore(0);
        uploadMdData();
        try {
            uploadContent.acquire();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        publishProgress(100.0);

        return null;
    }

    private void uploadImages() {
        for (ImageUploadUtils.DataContainer container : containers) {
            uploadManager.addFile(container.filePath, MyUploadManager.Key.image, UrlGenerator.uploadImage(), _uploadImageCallback(container));
        }
    }

    private S_P_Callback _uploadImageCallback(final ImageUploadUtils.DataContainer container) {
        return new S_P_Callback() {
            @Override
            public void onProgress(int progress) {
                publishProgress(progressNum += delta);
            }

            @Override
            public void onError(Exception e) {
                callback.onError();
            }

            @Override
            public void onFinished(String serverResponseMessage, File file) {
                try {
                    JSONObject object = new JSONObject(serverResponseMessage);
                    container.replacementUrl = object.getString(Urls.KEY_IMAGE_URL);
                    uploadImageFinish.release();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onStart(long id, String fileName) {

            }

            @Override
            public void onPause(int currentFileSize, int fileSize) {

            }
        };
    }


    private void uploadMdData() {

        String sendUrl = null;
        String key = null;
        if (type == ContentType.Question) {
            sendUrl = UrlGenerator.uploadQuestion();
            key = "question";
        } else if (type == ContentType.Note) {
            sendUrl = UrlGenerator.uploadNote();
            key = "note";
        } else if (type == ContentType.Answer) {
            sendUrl = UrlGenerator.uploadAnswer(questionId);
        }

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("content", rawData);
            if (type != ContentType.Answer) {
                jsonObject.put("bookId", bookId);
                jsonObject.put("page", bookPage);
                jsonObject.put("userId", MyUserID);
                jsonObject.put("title", title);
            } else {
                jsonObject.put("questionId", questionId);
            }
            Log.i("question content", rawData);
        } catch (Exception e) {
            e.printStackTrace();
            cancel(true);
            callback.onError();
        }

//        JSONObject jsonObject = null;
//        try {
//            if (type != ContentType.Answer) {
//                jsonObject = new JSONObject(new Gson().toJson(new FeedForm(key, title, bookPage, rawData), FeedForm.class));
//                jsonObject.put("bookId", bookId);
//            } else {
//                jsonObject = new JSONObject(new Gson().toJson(new AnswerForm(rawData, questionId), AnswerForm.class));
//            }
//        } catch (JSONException e) {
//            e.printStackTrace();
//            callback.onError();
//            cancel(true);
//        }

        Response.Listener<JSONObject> postFinish = new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                try {
                    int status = jsonObject.getInt(Urls.KEY_STATUS);
                    if (status == org.bitman.ay27.request.Status.SUCCESS)
                        uploadContent.release();
                    else {
                        callback.onError();
                        cancel(true);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    callback.onError();
                    cancel(true);
                }
            }
        };
        Response.ErrorListener errorCallback = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                callback.onError();
                cancel(true);
            }
        };
        new PostRequest(sendUrl, jsonObject, postFinish, errorCallback);
    }

}
