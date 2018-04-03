package org.bitman.ay27.view.writer.upload;

import android.net.Uri;
import org.bitman.ay27.common.ContentType;
import org.bitman.ay27.common.TaskUtils;

import java.util.ArrayList;

/**
 * Proudly to use Intellij IDEA.
 * Created by ay27 on 14-9-22.
 */
public class Uploader {

    public Uploader(ContentType type, long bookId, int bookPage, String title, String rawData, UploadCallback callback) {
        TaskUtils.executeAsyncTask(new FeedTask(title, rawData, bookId, bookPage, type, callback));
    }

    public Uploader(ContentType type, long questionId, String rawData, UploadCallback callback) {
        TaskUtils.executeAsyncTask(new FeedTask(rawData, questionId, type, callback));
    }

    public Uploader(long bookId, int bookPage, String title, String rawData, ArrayList<Uri> files, UploadCallback callback) {
        TaskUtils.executeAsyncTask(new AttachmentTask(title, rawData, bookId, bookPage, files, callback));
    }
}
