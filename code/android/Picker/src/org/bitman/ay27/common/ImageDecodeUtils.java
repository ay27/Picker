package org.bitman.ay27.common;

import android.content.ContentResolver;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;

import java.io.FileNotFoundException;

/**
 * Proudly to use Intellij IDEA.
 * Created by ay27 on 14-9-7.
 */
public class ImageDecodeUtils {


    public static Bitmap decodeSource(Context context, Uri picUri, int targetWidth) throws FileNotFoundException {
        ContentResolver cr = context.getContentResolver();
        BitmapFactory.Options o1 = new BitmapFactory.Options();
        o1.inJustDecodeBounds = true;

        if (targetWidth <= 0)
            return BitmapFactory.decodeStream(cr.openInputStream(picUri));

        BitmapFactory.decodeStream(cr.openInputStream(picUri), null, o1);

        BitmapFactory.Options o2 = new BitmapFactory.Options();
        o2.inSampleSize = (int) ((double) o1.outWidth / (double) targetWidth);
        return BitmapFactory.decodeStream(cr.openInputStream(picUri), null, o2);
    }


    public static final int ThumbnailsWidth = 250;
    public static final int ThumbnailsHeight = 250;
    public static Bitmap decode2Thumbnails(Context context, Uri picUri) throws FileNotFoundException {
        ContentResolver cr = context.getContentResolver();
        BitmapFactory.Options o1 = new BitmapFactory.Options();
        o1.inJustDecodeBounds = true;
        Bitmap bitmap = BitmapFactory.decodeStream(cr.openInputStream(picUri), null, o1);

        return Bitmap.createScaledBitmap(bitmap, ThumbnailsWidth, ThumbnailsHeight, false);
    }
}
