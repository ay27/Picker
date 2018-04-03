package org.bitman.ay27.upload_download;

import android.content.Context;
import android.net.Uri;
import org.bitman.ay27.PickerApplication;
import org.bitman.ay27.common.TaskUtils;
import org.bitman.ay27.common.Utils;
import org.bitman.ay27.data.DataHelper;

import java.io.File;
import java.util.ArrayList;
import java.util.Random;

/**
 * Proudly to use Intellij IDEA.
 * Created by ay27 on 14/11/8.
 */
public class MyUploadManager {

    private static MyUploadManager instance;
    private ArrayList<Request> requestList;
    private Context context;
    private DataHelper helper;

    public MyUploadManager() {
        context = PickerApplication.getContext();
        requestList = new ArrayList<Request>();
        helper = new DataHelper(context, context.getContentResolver());
    }

    public static MyUploadManager getInstance() {
        if (instance == null)
            instance = new MyUploadManager();
        return instance;
    }

    public void addFile(String filePath, Key key, String serverUrl, S_P_Callback callback) {
        _add(new File(filePath), key, serverUrl, callback);
    }

    public void addFile(Uri fileUri, Key key, String serverUrl, S_P_Callback callback) {
        File file = new File(Utils.getPathFromUri(context, fileUri));
        _add(file, key, serverUrl, callback);
    }

    private void _add(File file, Key key, String serverUrl, S_P_Callback callback) {
        if (!file.exists())
            throw new IllegalArgumentException("file path illegal");

        final Request request = new Request(file, key, serverUrl, callback);
        TaskUtils.executeAsyncTask(request.task);
        requestList.add(request);

    }

    public void destroy() {
        for (Request holder : requestList) {
            holder.task.cancel(false);
        }
        requestList = null;
        context = null;
        instance = null;
    }

    public static enum Key {
        attachment, image
    }


    private class Request {
        UploadTask task;
        File file;
        S_P_Callback callback;
        Key key;
        String serverUrl;
        long id;

        public Request(File file, Key key, String serverUrl, S_P_Callback callback) {
            this.file = file;
            this.callback = callback;
            this.key = key;
            this.serverUrl = serverUrl;
            id = Math.abs(new Random().nextLong());
            task = new UploadTask(id, file, key, serverUrl, callback);
        }
    }
}
