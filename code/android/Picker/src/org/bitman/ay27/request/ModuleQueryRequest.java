package org.bitman.ay27.request;

import android.app.Activity;
import android.util.Log;
import com.android.volley.Response;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.bitman.ay27.module.BaseModule;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;

/**
 * Proudly to use Intellij IDEA.
 * Created by ay27 on 14-8-29.
 */
public class ModuleQueryRequest extends PickerRequest {

    public ModuleQueryRequest(String url, JSONObject jsonRequest, Response.Listener<JSONObject> listener, Response.ErrorListener errorListener) {
        super(url, jsonRequest, listener, errorListener);
    }

//    public<T extends BaseModule> ModuleQueryRequest(Activity activity, String url, JSONObject jsonObject, String pattern, Type type, RequestFinishedCallback<T> requestFinished) {
//        super(Method.GET, url, jsonObject, defaultRequestFinished(pattern, type,  requestFinished), defaultErrorListener(activity));
//    }
//
//    private static <T extends BaseModule> Response.Listener<JSONObject> defaultRequestFinished(final String pattern, final Type type, final RequestFinishedCallback<T> requestFinished) {
//        return new Response.Listener<JSONObject>() {
//            @Override
//            public void onResponse(JSONObject response) {
//                try {
//                    String json = response.getString(pattern);
//                    Log.i("json", json);
//                    Gson gson = new GsonBuilder().setDateFormat(
//                            "yyyy-MM-dd HH:mm:ss.m").create();
//                    T result = gson.fromJson(json, type);
//                    requestFinished.onFinished(json, result);
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//        };
//    }

    public static interface RequestFinishedCallback<T extends BaseModule> {
        public void onFinished(String json, T result);
    }

    public <T extends BaseModule> ModuleQueryRequest(Activity activity, String url, JSONObject jsonObject, String pattern, Class<T> module, RequestFinishedCallback<T> requestFinished) {
        super(Method.GET, url, jsonObject, defaultRequestFinished(pattern, module,  requestFinished), defaultErrorListener(activity));
    }

//    private Map<String, String> params;
//    public ModuleQueryRequest(String url, Map<String, String>params, Response.Listener<JSONObject> listener, Response.ErrorListener errorListener) {
//        super(url, null, listener, errorListener);
//        this.params = params;
//    }
//
//    @Override
//    protected Map<String, String> getParams() throws AuthFailureError {
//        return params;
//    }


    private static <T extends BaseModule> Response.Listener<JSONObject> defaultRequestFinished(final String pattern, final Class<T> module, final RequestFinishedCallback<T> requestFinished) {
        return new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    String json = response.getString(pattern);
                    Log.i("json", json);
                    Gson gson = new GsonBuilder().setDateFormat(
                            "yyyy-MM-dd HH:mm:ss.m").create();
                    T result = gson.fromJson(json, module);
                    requestFinished.onFinished(json, result);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
    }
}
