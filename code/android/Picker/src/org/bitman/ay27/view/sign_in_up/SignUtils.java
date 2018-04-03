package org.bitman.ay27.view.sign_in_up;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.bitman.ay27.PickerApplication;
import org.bitman.ay27.module.dp.UserDP;
import org.bitman.ay27.module.form.LoginForm;
import org.bitman.ay27.module.form.RegisterForm;
import org.bitman.ay27.request.PickerRequest;
import org.bitman.ay27.request.Status;
import org.bitman.ay27.request.UrlGenerator;
import org.bitman.ay27.request.Urls;
import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.CookieHandler;
import java.net.CookieManager;

/**
 * Proudly to use Intellij IDEA.
 * Created by ay27 on 14-9-5.
 */
public class SignUtils {

    public static void signIn(@NotNull String userName, @NotNull final String password, @NotNull final SignCallback callback) {
        String url = UrlGenerator.signIn();
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(new Gson().toJson(new LoginForm(userName, password), LoginForm.class));
        } catch (JSONException e) {
            e.printStackTrace();
            callback.onFailed();
            return;
        }

        Response.Listener<JSONObject> successListener = new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                try {
                    int status = jsonObject.getInt(Urls.KEY_STATUS);
                    if (status == Status.SUCCESS) {
                        doQueryUserInfo(callback, password);
                    } else {
                        callback.onFailed();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    callback.onFailed();
                }
            }
        };

        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                callback.onFailed();
            }
        };


        CookieManager cookieManager = new CookieManager();
        CookieHandler.setDefault(cookieManager);
        new PickerRequest(Request.Method.POST, url, jsonObject, successListener, errorListener);

    }

    private static void doQueryUserInfo(@NotNull final SignCallback callback, @NotNull final String password) {
        String url = UrlGenerator.queryMyUserInfo();
        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                callback.onFailed();
            }
        };
        Response.Listener<JSONObject> successListener = new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                UserDP userDP;
                try {
                    String json = jsonObject.getString(Urls.KEY_USER);
                    if (json != null) {
                        Gson gson = new GsonBuilder().setDateFormat(
                                "yyyy-MM-dd HH:mm:ss.m").create();
                        userDP = gson.fromJson(json, UserDP.class);
                        userDP.setPassword(password);
                        PickerApplication.setMyUser(userDP);
                        callback.onSuccess(userDP);
                    } else {
                        callback.onFailed();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    callback.onFailed();
                }
            }
        };
        new PickerRequest(url, null, successListener, errorListener);
    }

    public static void signUp(@NotNull String userName, @NotNull final String password, @NotNull String email, @NotNull final SignCallback callback) {
        String url = UrlGenerator.signUp();
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(new Gson().toJson(new RegisterForm(email, userName, password), RegisterForm.class));
        } catch (JSONException e) {
            e.printStackTrace();
            callback.onFailed();
            return;
        }

        Response.Listener<JSONObject> successListener = new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                UserDP userDP;
                try {
                    String json = jsonObject.getString(Urls.KEY_USER);
                    if (json != null) {
                        Gson gson = new GsonBuilder().setDateFormat(
                                "yyyy-MM-dd HH:mm:ss.m").create();
                        userDP = gson.fromJson(json, UserDP.class);
                        userDP.setPassword(password);
                        PickerApplication.setMyUser(userDP);
                        callback.onSuccess(userDP);
                    } else {
                        callback.onFailed();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    callback.onFailed();
                }
            }
        };
        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                callback.onFailed();
            }
        };

        CookieManager cookieManager = new CookieManager();
        CookieHandler.setDefault(cookieManager);

        new PickerRequest(Request.Method.POST, url, jsonObject, successListener, errorListener);
    }


    public static interface SignCallback {
        public void onSuccess(UserDP userDP);

        public void onFailed();
    }
}
