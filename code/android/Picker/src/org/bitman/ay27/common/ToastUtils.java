package org.bitman.ay27.common;

import android.app.Activity;
import com.github.johnpersano.supertoasts.SuperActivityToast;
import com.github.johnpersano.supertoasts.SuperToast;

/**
 * Proudly to use Intellij IDEA.
 * Created by ay27 on 14-9-1.
 */
public class ToastUtils {

    private static SuperActivityToast toast;
    private ToastUtils() {}

    public static void showError(Activity activity, String message) {
        dismiss();
        toast = new SuperActivityToast(activity);
        toast.setText(message);
        toast.setBackground(SuperToast.Background.RED);
        toast.setDuration(1000);
        toast.show();
    }
    public static void showError(Activity activity, final int resId) {
        showError(activity, activity.getString(resId));
    }

    public static void showSuccess(Activity activity, String message) {
        dismiss();
        toast = new SuperActivityToast(activity);
        toast.setText(message);
        toast.setBackground(SuperToast.Background.GREEN);
        toast.setDuration(1000);
        toast.show();
    }
    public static void showSuccess(Activity activity, final int resId) {
        showSuccess(activity, activity.getString(resId));
    }

    public static void showWaiting(Activity activity, String message) {
        dismiss();
        toast = new SuperActivityToast(activity, SuperToast.Type.PROGRESS);
        toast.setText(message);
        toast.setBackground(SuperToast.Background.BLUE);
        toast.setDuration(2000);
        toast.show();
    }
    public static void showWaiting(Activity activity, final int resId) {
        showWaiting(activity, activity.getString(resId));
    }

    public static void dismiss() {
        if (toast!=null && toast.isShowing())
            toast.dismiss();
    }

}
