package org.bitman.ay27.upload_download;

import android.app.DownloadManager;
import android.content.Context;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;
import org.bitman.ay27.PickerApplication;
import org.bitman.ay27.data.DataHelper;

import java.io.File;
import java.util.ArrayList;

/**
 * Proudly to use Intellij IDEA.
 * Created by ay27 on 14/11/8.
 */
public class MyDownloadManager {

    public static final String APP_FILE_DIR = "picker/download_file";
    public static final Uri CONTENT_URI = Uri.parse("content://downloads/my_downloads");

    private static ArrayList<DownloadTask> tasks;
    private static MyDownloadManager instance;
    private final Handler handleQuery = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            for (DownloadTask task : tasks) {
                task.update();
            }
        }
    };
    private DownloadManager manager;
    private Context context;
    private ContentObserver observer;
    private DataHelper helper;

    private MyDownloadManager() {
        this.context = PickerApplication.getContext();
        manager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
        context.getContentResolver().registerContentObserver(CONTENT_URI, true, observer = new DownloadChangeObserver(handleQuery));
        tasks = new ArrayList<DownloadTask>();

        helper = new DataHelper(context, context.getContentResolver());
    }

    public static MyDownloadManager getInstance() {
        if (instance == null)
            instance = new MyDownloadManager();
        return instance;
    }

    public void destroy() {
        tasks = null;
        instance = null;
    }


    public long addTask(String url, S_P_Callback callback) {
//        for (DownloadTask task : tasks) {
//            if (task.url.equals(url)) {
//                Toast.makeText(context, "任务已存在", Toast.LENGTH_SHORT).show();
//                return -1;
//            }
//        }

        final DownloadTask task = new DownloadTask(manager, url, callback);
        tasks.add(task);
        return task.id;
    }

    public void remove(String url) {
        for (int i = 0; i < tasks.size(); i++) {
            DownloadTask task = tasks.get(i);
            if (task.isMatch(url)) {
                manager.remove(task.id);
                task = null;
                tasks.remove(i);
                return;
            }
        }
    }

    public void onDestroy() {
        for (int i = 0; i < tasks.size(); i++) {
            manager.remove(tasks.get(i).id);
        }
        tasks = null;
        context.getContentResolver().unregisterContentObserver(observer);
    }

    public static class DownloadTask {
        public DownloadManager.Request request;
        public Uri targetUri;
        public long id;
        public DownloadManager.Query query;
        public S_P_Callback callback;
        public DownloadManager manager;
        public String url;
        public String fileName;

        public DownloadTask(DownloadManager manager, String url, S_P_Callback callback) {
            this.targetUri = Uri.parse(url);
            this.url = url;
            this.manager = manager;
            this.callback = callback;

            fileName = getFileName(url);
            try {
                request = new DownloadManager.Request(targetUri);
            } catch (Exception e) {
                e.printStackTrace();
                callback.onError(e);
                return;
            }
            request.setVisibleInDownloadsUi(true);
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
            request.setDestinationInExternalPublicDir(APP_FILE_DIR, fileName);
            request.setTitle(getFileNameWithoutExtension(fileName));
            request.setDescription("正在下载");
            id = manager.enqueue(request);

            callback.onStart(id, fileName);

            query = new DownloadManager.Query();
            query.setFilterById(id);
        }

        private static String getFileName(String url) {
            return url.substring(url.lastIndexOf('/') + 1);
        }

        private static String getFileNameWithoutExtension(String fileName) {
            return fileName.substring(0, fileName.lastIndexOf('.') - 1);
        }

        public void update() {
            Cursor cursor = manager.query(query);
            if (cursor != null && cursor.moveToFirst()) {
                int status = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS));
                int fileSize = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_TOTAL_SIZE_BYTES));
                int downloadedSize = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR));
                switch (status) {
                    case DownloadManager.STATUS_PAUSED:
                        callback.onPause(downloadedSize, fileSize);
                        break;
                    case DownloadManager.STATUS_FAILED:
                        callback.onError(new Exception("error"));
                        break;
                    case DownloadManager.STATUS_RUNNING:
                        callback.onProgress((int) ((double) downloadedSize / (double) fileSize * 100));
                        break;
                    case DownloadManager.STATUS_SUCCESSFUL:
                        callback.onFinished(null, new File(Environment.getExternalStorageDirectory()+"/"+APP_FILE_DIR+"/"+fileName));
                        break;
                    default:
                        break;
                }
            }
        }

        public boolean isMatch(String url) {
            return targetUri.toString().equals(url);
        }
    }

    public static class DownloadChangeObserver extends ContentObserver {

        private Handler handler;

        public DownloadChangeObserver(Handler handler) {
            super(handler);
            this.handler = handler;
        }

        @Override
        public void onChange(boolean selfChange) {
            super.onChange(selfChange);
            handler.dispatchMessage(new Message());
        }

    }
}
