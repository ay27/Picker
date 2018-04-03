package org.bitman.ay27.view.adapter;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.ButterKnife;
import butterknife.InjectView;
import com.android.volley.toolbox.ImageLoader;
import com.google.gson.Gson;
import org.bitman.ay27.PickerApplication;
import org.bitman.ay27.R;
import org.bitman.ay27.cache.ImageCacheManager;
import org.bitman.ay27.data.DataTable;
import org.bitman.ay27.module.PrivateMessageSum;
import org.bitman.ay27.request.UrlGenerator;

/**
 * Proudly to use Intellij IDEA.
 * Created by ay27 on 14-9-28.
 */
public class UserDialogAdapter extends CursorAdapter {
    public UserDialogAdapter(Context context, Cursor c, boolean autoRequery) {
        super(context, c, autoRequery);
    }

    public UserDialogAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View v = LayoutInflater.from(context).inflate(R.layout.user_dialog_item, null);
        ViewHolder holder = new ViewHolder(v);

        v.setTag(holder);
        return v;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        ViewHolder holder = (ViewHolder) view.getTag();

        if (holder.container != null)
            holder.container.cancelRequest();

        final long MyUserId = PickerApplication.getMyUserId();

        PrivateMessageSum dialog = new Gson().fromJson(cursor.getString(cursor.getColumnIndex(DataTable.KEY_JSON)), PrivateMessageSum.class);
        if (dialog.getSenderId() != MyUserId) {
            holder.dialogID = dialog.getDialogID();
            holder.targetUserId = dialog.getSenderId();
            if (dialog.getSenderAvatarUrl() != null)
                holder.container = ImageCacheManager.loadImage(UrlGenerator.getResourcesUrl(dialog.getSenderAvatarUrl()),
                        ImageCacheManager.getImageListener(holder.avatar, null, null));
            holder.name.setText(dialog.getSenderName());
            holder.preview.setText(dialog.getContent());
            holder.targetUserName = new String(dialog.getReceiverName());
        }
        else {
            holder.dialogID = dialog.getDialogID();
            holder.targetUserId = dialog.getReceiverId();
            if (dialog.getReceiverAvatarUrl() != null)
                holder.container = ImageCacheManager.loadImage(UrlGenerator.getResourcesUrl(dialog.getReceiverAvatarUrl()),
                        ImageCacheManager.getImageListener(holder.avatar, null, null));
            holder.name.setText(dialog.getReceiverName());
            holder.preview.setText(dialog.getContent());
            holder.targetUserName = new String(dialog.getReceiverName());
        }
    }


    public static class ViewHolder {
        @InjectView(R.id.user_dialog_item_avatar)
        ImageView avatar;
        @InjectView(R.id.user_dialog_item_name)
        TextView name;
        @InjectView(R.id.user_dialog_item_dialog_preview)
        TextView preview;

        ImageLoader.ImageContainer container;

        public long targetUserId;
        public long dialogID;
        public String targetUserName;

        public ViewHolder(View view) {
            ButterKnife.inject(this, view);
        }
    }
}
