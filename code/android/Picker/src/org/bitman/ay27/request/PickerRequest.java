package org.bitman.ay27.request;

import android.app.Activity;
import android.util.Log;
import com.android.volley.*;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonObjectRequest;
import org.bitman.ay27.common.ToastUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.Map;

/**
 * Proudly to use Intellij IDEA.
 * Created by ay27 on 14-8-27.
 */
public class PickerRequest extends JsonObjectRequest {

    private static final String TAG = "PickerRequest";

    public PickerRequest(int method, String url, JSONObject jsonRequest, Response.Listener<JSONObject> listener, Response.ErrorListener errorListener) {
        super(method, url, jsonRequest, listener, errorListener);

        RequestManager.addRequest(this, null);
    }

    public PickerRequest(String url, JSONObject jsonRequest, Response.Listener<JSONObject> listener, Response.ErrorListener errorListener) {
        super(url, jsonRequest, listener, errorListener);
        RequestManager.addRequest(this, null);
    }

    private Map<String, String> mParams;

    public PickerRequest(String url, Map<String, String> params, JSONObject jsonObject, Response.Listener<JSONObject> listener, Response.ErrorListener errorListener) {
        super(url, jsonObject, listener, errorListener);
        mParams = params;

        RequestManager.addRequest(this, null);
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return mParams;
    }

    @Override
    public RetryPolicy getRetryPolicy() {
        RetryPolicy retryPolicy = new DefaultRetryPolicy(1000, 1, 1.0f);
//        this.setRetryPolicy(retryPolicy);
        return retryPolicy;
    }

    public static Response.ErrorListener defaultErrorListener(final Activity activity) {
        return new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                if (activity == null) {
                    Log.e(TAG, volleyError.toString());
                    return;
                }
                Log.e(TAG, volleyError.toString());
                ToastUtils.showError(activity, volleyError.toString());
            }
        };
    }

    @Override
    protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
        try {
            String str = new String(response.data, "UTF-8");
            try {
                return Response.success(new JSONObject(str),
                        HttpHeaderParser.parseCacheHeaders(response));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return super.parseNetworkResponse(response);
    }
}
