package org.bitman.ay27.view.writer.upload;

/**
 * Proudly to use Intellij IDEA.
 * Created by ay27 on 14/11/23.
 */
public interface UploadCallback {
    public void onPreExecute();

    public void onProgressUpdate(double value);

    public void onPostExecute();

    public void onError();
}