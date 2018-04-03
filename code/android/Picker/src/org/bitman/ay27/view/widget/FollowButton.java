package org.bitman.ay27.view.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import com.android.volley.Response;
import org.bitman.ay27.R;
import org.bitman.ay27.common.ContentType;
import org.bitman.ay27.module.BaseModule;
import org.bitman.ay27.module.interfaces.IsFollow;
import org.bitman.ay27.request.PickerRequest;
import org.bitman.ay27.request.Status;
import org.bitman.ay27.request.UrlGenerator;
import org.bitman.ay27.request.Urls;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Proudly to use Intellij IDEA.
 * Created by ay27 on 14-9-3.
 */
public class FollowButton extends Button implements View.OnClickListener {
    private IsFollow followModule;
    private ContentType targetType;

    public FollowButton(Context context) {
        super(context);
    }

    public FollowButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FollowButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void setFollowModule(IsFollow followModule, ContentType type) {
        this.followModule = followModule;
        this.targetType = type;
        setStatusText();
        this.setOnClickListener(this);
    }


    public void setStatusText() {
        if (followModule.isFollow()) {
            this.setText(R.string.cancel_follow);
            this.setBackground(getResources().getDrawable(R.drawable.bbuton_danger));
        }
        else {
            this.setText(R.string.follow);
            this.setBackground(getResources().getDrawable(R.drawable.bbuton_success));
        }
    }


    @Override
    public void onClick(View v) {
        String url;
        if (followModule.isFollow()) {
            url = UrlGenerator.withdrawFollow(((BaseModule) followModule).getId(), targetType);
        } else {
            url = UrlGenerator.follow(((BaseModule) followModule).getId(), targetType);
        }

        Response.Listener<JSONObject> successListener = new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                try {
                    int status = jsonObject.getInt(Urls.KEY_STATUS);
                    if (status == Status.SUCCESS) {
                        followModule.setFollow(!followModule.isFollow());
                        setStatusText();
                    } else {
                        Toast.makeText(getContext(), R.string.action_failed, Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
        new PickerRequest(url, null, successListener, PickerRequest.defaultErrorListener(null));
    }
}
