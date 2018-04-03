package org.bitman.ay27.upload_download;

import android.os.AsyncTask;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Random;
import java.util.UUID;

/**
 * Proudly to use Intellij IDEA.
 * Created by ay27 on 14/11/14.
 */
class UploadTask extends AsyncTask<Void, Integer, String> {

    public static final String BOUNDARY = UUID.randomUUID().toString();
    public static final String PREFIX = "--", LINE_END = "\r\n";
    public static final String CONTENT_TYPE = "multipart/form-data";
    public static final String ERROR_PREFIX = "ErrOr:";
    public static final int TIME_OUT = 6 * 1000;
    private static final String CHARSET = "iso-8859-1";
    private File file;
    private String serverUrl;
    private S_P_Callback callback;
    private MyUploadManager.Key key;

    public UploadTask(long id, File file, MyUploadManager.Key key, String serverUrl, S_P_Callback callback) {
        this.file = file;
        this.key = key;
        this.serverUrl = serverUrl;
        this.callback = callback;

        callback.onStart(id, file.getName());
    }

    @Override
    protected String doInBackground(Void... params) {
        if (file == null || !file.exists())
            return null;

        try {
            URL url = new URL(serverUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(TIME_OUT);
            conn.setReadTimeout(TIME_OUT);
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Charset", CHARSET);
            conn.setRequestProperty("connection", "keep-alive");
            conn.setRequestProperty("Content-Type", CONTENT_TYPE + ";boundary=" + BOUNDARY);
            // to calculate the progress more accurately.
            conn.setUseCaches(false);

            DataOutputStream dos = new DataOutputStream(conn.getOutputStream());
            StringBuilder sb = new StringBuilder();
            sb.append(PREFIX);
            sb.append(BOUNDARY);
            sb.append(LINE_END);
            String filename = new String(file.getName().getBytes(), CHARSET);
            sb.append("Content-Disposition: form-data; name=\"" + key.name() + "\"; filename=\"" + filename + "\"" + LINE_END);
            sb.append("Content-Type: application/octet-stream; charset=" + CHARSET + LINE_END);
            sb.append(LINE_END);
            dos.write(sb.toString().getBytes());


            //*********************************************************************
            // read file and send
            InputStream is = new FileInputStream(file);

            final int fileSize = is.available();
            int currentSize = 0;

            byte[] buffer = new byte[2 * 1024];
            int len = 0;
            while ((len = is.read(buffer)) != -1) {
                dos.write(buffer, 0, len);

                currentSize += len;
                publishProgress((int) (((double) currentSize / (double) fileSize) * 100));
            }
            is.close();
            dos.write(LINE_END.getBytes());
            dos.write((PREFIX + BOUNDARY + PREFIX + LINE_END).getBytes());
            dos.flush();
            dos.close();

            //*****************************************************************
            //   server response
            InputStream responseStream = conn.getInputStream();
            StringBuilder responseBuilder = new StringBuilder();
            int resultLen = 0;
            while ((resultLen = responseStream.read(buffer)) != -1) {
                responseBuilder.append(new String(buffer, 0, resultLen));
            }
            responseStream.close();
            return responseBuilder.toString();

        } catch (Exception e) {
            e.printStackTrace();
            return ERROR_PREFIX + e.toString();
        }
    }

    @Override
    protected void onPostExecute(String s) {
        if (s.startsWith(ERROR_PREFIX))
            callback.onError(new Exception(s.substring(ERROR_PREFIX.length(), s.length())));
        else
            callback.onFinished(s, null);
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        callback.onProgress(values[0]);
    }

}
