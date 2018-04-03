package org.bitman.ay27.common;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import org.bitman.ay27.PickerApplication;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;

/**
 * Proudly to use Intellij IDEA.
 * Created by ay27 on 14-7-24.
 */
public class Utils {

    public static boolean isNull(String str) {
        if (str != null && !str.isEmpty())
            return false;
        return true;
    }

    public static int dp2Pixel(int dp) {
        return dp * PickerApplication.getScale_dp2pixel();
    }

    public static float sp2Pixel(int sp) {
        return sp * PickerApplication.getScale_dp2pixel();
    }


    public static String getDiskCacheDir(Context context) {
        String cachePath;
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())
                || !Environment.isExternalStorageRemovable()) {
            cachePath = context.getExternalCacheDir().getPath();
        } else {
            cachePath = context.getCacheDir().getPath();
        }
        return cachePath;
    }

    public static String write2cache(Context context, Bitmap data) throws IOException {
        String cachePath = getDiskCacheDir(context);
        File file = new File(cachePath, "" + System.currentTimeMillis() + ".png");

        FileOutputStream outputStream = new FileOutputStream(file);
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        data.compress(Bitmap.CompressFormat.PNG, 100, bos);

        outputStream.write(bos.toByteArray());
        return file.getAbsolutePath();
    }


    public static String getPathFromUri(Context context, Uri uri) {
        String fileName = null;
        Uri filePathUri = uri;
        if(uri!= null){
            if (uri.getScheme().toString().compareTo("content")==0)
            {
                Cursor cursor = context.getContentResolver().query(uri, null, null, null, null);
                if (cursor != null && cursor.moveToFirst())
                {
                    int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                    fileName = cursor.getString(column_index);
                    cursor.close();
                }
            }else if (uri.getScheme().compareTo("file")==0)
            {
                fileName = filePathUri.toString().replace("file://", "");
            }
        }
        return Uri.decode(fileName);
    }




    /**
     * 检测网络是否连接
     *
     * @return
     */
    public static boolean isNetConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm != null) {
            NetworkInfo[] infos = cm.getAllNetworkInfo();
            if (infos != null) {
                for (NetworkInfo ni : infos) {
                    if (ni.isConnected()) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * 检测wifi是否连接
     *
     * @return
     */
    public static boolean isWifiConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm != null) {
            NetworkInfo networkInfo = cm.getActiveNetworkInfo();
            if (networkInfo != null
                    && networkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
                return true;
            }
        }
        return false;
    }

    /**
     * 检测3G是否连接
     *
     * @return
     */
    public static boolean is3gConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm != null) {
            NetworkInfo networkInfo = cm.getActiveNetworkInfo();
            if (networkInfo != null
                    && networkInfo.getType() == ConnectivityManager.TYPE_MOBILE) {
                return true;
            }
        }
        return false;
    }


    public static String formatFileSize(long fileS) {
        DecimalFormat df = new DecimalFormat("#.00");
        String fileSizeString;
        if (fileS < 1024) {
            fileSizeString = df.format((double) fileS) + "B";
        } else if (fileS < 1048576) {
            fileSizeString = df.format((double) fileS / 1024) + "K";
        } else if (fileS < 1073741824) {
            fileSizeString = df.format((double) fileS / 1048576) + "M";
        } else {
            fileSizeString = df.format((double) fileS / 1073741824) + "G";
        }
        return fileSizeString;
    }

}
