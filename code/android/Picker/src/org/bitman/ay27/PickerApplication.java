package org.bitman.ay27;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Environment;
import android.util.DisplayMetrics;
import org.bitman.ay27.module.dp.UserDP;
import org.bitman.ay27.view.sign_in_up.SignUtils;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

/**
 * Proudly to use Intellij IDEA.
 * Created by ay27 on 14-7-25.
 */
public class PickerApplication extends Application {

    private static final String USER_PREFERENCES = "user_info";
    private static UserDP myUser = null;
    // 只能获取，不能更改的数据
    private static int SCREEN_WIDTH, SCREEN_HEIGHT;
    private static int scale_dp2pixel;

    private static Application instance;

    private static void write2Preference(UserDP userDP) {
        assert instance != null;
        SharedPreferences preferences = instance.getSharedPreferences(USER_PREFERENCES, MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("UserName", userDP.getName());
        editor.putString("UserEmail", userDP.getEmail());
        editor.putString("Password", userDP.getPassword());
        editor.putLong("UserID", userDP.getId());
        editor.commit();

    }

    public static long getMyUserId() {
        assert myUser != null;
        return myUser.getId();
    }

    public static UserDP getMyUser() {
        return myUser;
    }

    public static void setMyUser(@NotNull UserDP userDP) {
        myUser = new UserDP(userDP);
        write2Preference(myUser);
    }

    public static int getSCREEN_WIDTH() {
        return SCREEN_WIDTH;
    }

    public static int getSCREEN_HEIGHT() {
        return SCREEN_HEIGHT;
    }

    public static int getScale_dp2pixel() {
        return scale_dp2pixel;
    }

    public static Context getContext() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        instance = this;

        DisplayMetrics dm = getResources().getDisplayMetrics();
        SCREEN_HEIGHT = dm.heightPixels;
        SCREEN_WIDTH = dm.widthPixels;
        scale_dp2pixel = (int) dm.density;
//        scale_dp2pixel = SCREEN_WIDTH / 360;

        readPreferences();

        try {
            checkImageModuleFile();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void checkImageModuleFile() throws Exception {
        File file = new File(Environment.getExternalStorageDirectory() + "/Picker/tesseract/tessdata/eng.traineddata");
        if (file.exists())
            return;

        new File(Environment.getExternalStorageDirectory() + "/Picker").mkdir();
        new File(Environment.getExternalStorageDirectory() + "/Picker/tesseract/").mkdir();
        new File(Environment.getExternalStorageDirectory() + "/Picker/tesseract/tessdata/").mkdir();
        if (!file.createNewFile())
            throw new Exception("can't create the initial file");
        FileOutputStream fos = new FileOutputStream(file);
        InputStream is = this.getAssets().open("eng.traineddata");
        byte[] bytes = new byte[1024];
        int len;
        while ((len = (is.read(bytes))) != -1)
            fos.write(bytes, 0, len);

        is.close();
        fos.close();
    }

    private void readPreferences() {
        SharedPreferences preferences = getSharedPreferences(USER_PREFERENCES, MODE_PRIVATE);
        String userName = preferences.getString("UserName", "");
        final String password = preferences.getString("Password", "");
        long userId = preferences.getLong("UserID", -1);
        if (!userName.equals("") && !password.equals("")) {
            SignUtils.SignCallback callback = new SignUtils.SignCallback() {
                @Override
                public void onSuccess(UserDP userDP) {
                    userDP.setPassword(password);
                    setMyUser(userDP);
                }

                @Override
                public void onFailed() {
                    // do nothing
                }
            };
            SignUtils.signIn(userName, password, callback);
        }

    }


}
