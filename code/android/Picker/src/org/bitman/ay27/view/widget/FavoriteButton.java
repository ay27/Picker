package org.bitman.ay27.view.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Toast;
import com.android.volley.Response;
import org.bitman.ay27.PickerWidget.SuperFavoriteButton;
import org.bitman.ay27.R;
import org.bitman.ay27.common.ContentType;
import org.bitman.ay27.module.BaseModule;
import org.bitman.ay27.module.interfaces.IsFavorite;
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
public class FavoriteButton extends SuperFavoriteButton implements View.OnClickListener {
    private IsFavorite favoriteModule;
    private ContentType targetType;


    public FavoriteButton(Context context) {
        super(context);
    }

    public FavoriteButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FavoriteButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }


    public void setFavoriteModule(IsFavorite favoriteModule, ContentType type) {
        this.favoriteModule = favoriteModule;
        this.targetType = type;
        setStatusText();
        this.setOnClickListener(this);
    }

    public void setStatusText() {
        this.setFavorite(favoriteModule.isFavorite());
        this.setFavoriteNum(favoriteModule.getFavoriteNum());
    }


    @Override
    public void onClick(View v) {
        String url;
        if (favoriteModule.isFavorite()) {
            url = UrlGenerator.withdrawFavorite(((BaseModule) favoriteModule).getId(), targetType);
        } else {
            url = UrlGenerator.favorite(((BaseModule) favoriteModule).getId(), targetType);
        }

        Response.Listener<JSONObject> successListener = new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                try {
                    int favoriteNum = jsonObject.getInt(Urls.KEY_FAVORITENUM);
                    if (favoriteNum >= 0) {
                        favoriteModule.setFavorite(!favoriteModule.isFavorite());
                        favoriteModule.setFavoriteNum(favoriteNum);
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
