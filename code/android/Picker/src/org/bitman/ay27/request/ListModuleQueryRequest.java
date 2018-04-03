package org.bitman.ay27.request;

import android.app.Activity;
import android.util.Log;
import com.android.volley.Response;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.bitman.ay27.module.BaseModule;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.List;

/**
 * Proudly to use Intellij IDEA.
 * Created by ay27 on 14-8-29.
 */
public class ListModuleQueryRequest extends PickerRequest {

    public ListModuleQueryRequest(String url, JSONObject jsonRequest, Response.Listener<JSONObject> listener, Response.ErrorListener errorListener) {
        super(url, jsonRequest, listener, errorListener);
    }

    public static interface ListRequestCallback<T extends BaseModule> {
        public void onFinished(List<T> result);
    }

    public <T extends BaseModule> ListModuleQueryRequest(Activity activity, String url, JSONObject jsonObject, String pattern, final Type resultType, ListRequestCallback<T> requestFinished) {
        super(url, jsonObject, defaultRequestFinished(pattern, resultType, requestFinished), defaultErrorListener(activity));
    }

    private static <T extends BaseModule> Response.Listener<JSONObject> defaultRequestFinished(final String pattern, final Type type, final ListRequestCallback<T> requestFinished) {
        return new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.i("respnse array ", response.toString());
                try {
                    JSONArray jsonArray = response.getJSONArray(pattern);
                    Gson gson = new GsonBuilder().setDateFormat(
                            "yyyy-MM-dd HH:mm:ss.m").create();
                    Log.i("json array ", jsonArray.toString());
                    List<T> result = gson.fromJson(jsonArray.toString(), type);
                    requestFinished.onFinished(result);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
    }
}
