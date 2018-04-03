package org.bitman.ay27.view.writer.upload;

import android.net.Uri;
import android.os.AsyncTask;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import org.bitman.ay27.module.form.AttachmentFeedForm;
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
class AttachmentTask extends AsyncTask<Void, Double, String> {

    private String title;
    private String rawData;
    private long bookId = -1;
    private int bookPage = 0;
    private ArrayList<Uri> files;
    private UploadCallback callback;
    private volatile double delta = 0, progressNum = 0;
    private MyUploadManager uploadManager = MyUploadManager.getInstance();
    private Semaphore uploadAttachments, uploadContent;
    private ArrayList<Integer> attachmentIds;

    public AttachmentTask(String title, String rawData, long bookId, int bookPage, ArrayList<Uri> files, UploadCallback callback) {
        this.title = title;
        this.rawData = rawData;
        this.bookId = bookId;
        this.bookPage = bookPage;
        this.files = files;
        this.callback = callback;
        attachmentIds = new ArrayList<Integer>();
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

    @Override
    protected String doInBackground(Void... params) {
        publishProgress(progressNum = 0);
        delta = 100 / (files.size() + 1);

        // the initial permit number must be negative.
        // when the release time reach to containers.size(), the permit number must reach to 1.
        uploadAttachments = new Semaphore(-(files.size() - 1));
        uploadAttachment();
        try {
            uploadAttachments.acquire();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        uploadContent = new Semaphore(0);
        uploadData();

        try {
            uploadContent.acquire();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        publishProgress(100.0);

        return null;
    }

    private void uploadData() {
        String url = UrlGenerator.uploadAttachment();
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(new Gson().toJson(new AttachmentFeedForm(bookId, bookPage, title, rawData, attachmentIds), AttachmentFeedForm.class));
        } catch (JSONException e) {
            e.printStackTrace();
            callback.onError();
            cancel(true);
        }

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

        new PostRequest(url, jsonObject, postFinish, errorCallback);
    }

    private void uploadAttachment() {

        S_P_Callback uploadAttachmentCallback = new S_P_Callback() {
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
                    attachmentIds.add(object.getInt(Urls.KEY_ATTACHMENT_ID));
                    uploadAttachments.release();
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

        for (Uri uri : files) {
            String url = UrlGenerator.uploadFile();
            uploadManager.addFile(uri, MyUploadManager.Key.attachment, url, uploadAttachmentCallback);
        }
    }
}
