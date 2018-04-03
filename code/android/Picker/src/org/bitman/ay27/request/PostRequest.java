package org.bitman.ay27.request;

import android.app.Activity;
import android.util.Log;
import com.android.volley.Response;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Proudly to use Intellij IDEA.
 * Created by ay27 on 14-8-29.
 */
public class PostRequest extends PickerRequest {

    public PostRequest(String url, JSONObject jsonPost, Response.Listener<JSONObject> listener, Response.ErrorListener errorListener) {
        super(Method.POST, url, jsonPost, listener, errorListener);
    }

    public PostRequest(Activity activity, String url, JSONObject jsonPost, Response.Listener<JSONObject> listener) {
        super(Method.POST, url, jsonPost, listener, defaultErrorListener(activity));
    }

    public PostRequest(Activity activity, String url, JSONObject jsonObject, PostFinishCallback postFinishCallback) {
        super(Method.POST, url, jsonObject, defaultPostSuccessCallback(postFinishCallback), defaultErrorListener(activity));
    }

    public static interface PostFinishCallback {
        public void onFinished(Boolean result);
    }

    private static Response.Listener<JSONObject> defaultPostSuccessCallback(final PostFinishCallback postFinishCallback) {
        return new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.i("json", response.toString());
                try {
                    int status = response.getInt(Urls.KEY_STATUS);
                    if (status == Status.SUCCESS)
                        postFinishCallback.onFinished(true);
                    else {
                        postFinishCallback.onFinished(false);
                        Log.e("post request", "status = "+status);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
    }
}
