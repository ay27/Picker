package org.bitman.ay27.upload_download;

import java.io.File;

/**
 * Proudly to use Intellij IDEA.
 * Created by ay27 on 14/11/14.
 */

public interface S_P_Callback {
    public void onProgress(int progress);

    public void onError(Exception e);

    public void onFinished(String serverResponseMessage, File file);

    public void onStart(long id, String fileName);

    public void onPause(int currentFileSize, int fileSize);
}
