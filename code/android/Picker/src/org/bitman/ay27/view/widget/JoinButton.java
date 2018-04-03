package org.bitman.ay27.view.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import com.android.volley.Response;
import org.bitman.ay27.R;
import org.bitman.ay27.module.BaseModule;
import org.bitman.ay27.module.interfaces.IsJoinable;
import org.bitman.ay27.request.PickerRequest;
import org.bitman.ay27.request.Status;
import org.bitman.ay27.request.UrlGenerator;
import org.bitman.ay27.request.Urls;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Proudly to use Intellij IDEA.
 * Created by ay27 on 14-10-5.
 */
public class JoinButton extends Button implements View.OnClickListener {
    public JoinButton(Context context) {
        super(context);
    }

    public JoinButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public JoinButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    private IsJoinable joinableModule;

    public void setJoinableModule(IsJoinable joinableModule) {
        this.joinableModule = joinableModule;
        setStatusText();
        this.setOnClickListener(this);
    }

    private void setStatusText() {
        if (joinableModule.isJoined()) {
            this.setText(getContext().getString(R.string.join_button_acquire_quick));
            this.setBackground(getResources().getDrawable(R.drawable.bbuton_danger));
        } else {
            this.setText(getContext().getString(R.string.join_button_acquire_join));
            this.setBackground(getResources().getDrawable(R.drawable.bbuton_success));
        }
    }

    @Override
    public void onClick(View v) {
        String url = null;
        if (joinableModule.isJoined()) {
            url = UrlGenerator.withdrawJoin(((BaseModule) joinableModule).getId());
        } else {
            url = UrlGenerator.joinCircle(((BaseModule) joinableModule).getId());
        }

        Response.Listener<JSONObject> successListener = new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                try {
                    int status = jsonObject.getInt(Urls.KEY_STATUS);
                    if (status == Status.SUCCESS) {
                        joinableModule.setJoinable(!joinableModule.isJoined());
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
