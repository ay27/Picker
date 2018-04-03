package org.bitman.ay27.view.adapter;

import android.content.Context;
import android.content.Intent;
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
import org.bitman.ay27.R;
import org.bitman.ay27.cache.ImageCacheManager;
import org.bitman.ay27.data.DataTable;
import org.bitman.ay27.module.dp.AttachmentFeedDp;
import org.bitman.ay27.request.UrlGenerator;
import org.bitman.ay27.view.user.UserInfoActivity;

/**
 * Created by ay27 on 14-11-5.
 */
public class AttachmentAdapter extends CursorAdapter {
    public AttachmentAdapter(Context context, Cursor c, boolean autoRequery) {
        super(context, c, autoRequery);
    }

    public AttachmentAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.attachment_list_item, null);
        ViewHolder holder = new ViewHolder(view);
        view.setTag(holder);

        return view;
    }

    @Override
    public void bindView(View view, final Context context, Cursor cursor) {
        ViewHolder holder = (ViewHolder) view.getTag();
        if (holder.container != null)
            holder.container.cancelRequest();

        final AttachmentFeedDp attachment = new Gson().fromJson(cursor.getString(cursor.getColumnIndex(DataTable.KEY_JSON)), AttachmentFeedDp.class);
        holder.attachmentId = attachment.getId();
        holder.title.setText(attachment.getTitle());
        holder.userName.setText(attachment.getUserName());
        holder.page.setText(""+attachment.getPage());
        if (attachment.getUserAvatarUrl() != null)
            holder.container = ImageCacheManager.loadImage(UrlGenerator.getResourcesUrl(attachment.getUserAvatarUrl()),
                    ImageCacheManager.getImageListener(holder.avatar, null, null));

        holder.avatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, UserInfoActivity.class);
                intent.putExtra("TargetUserID", attachment.getUserId());
                context.startActivity(intent);
            }
        });
    }

    public static class ViewHolder {
        @InjectView(R.id.attachment_list_item_avatar)
        ImageView avatar;
        @InjectView(R.id.attachment_list_item_title)
        TextView title;
        @InjectView(R.id.attachment_list_item_user_name)
        TextView userName;
        @InjectView(R.id.attachment_list_item_page)
        TextView page;

        public long attachmentId;

        ImageLoader.ImageContainer container;

        public ViewHolder(View view) {
            ButterKnife.inject(this, view);
        }
    }
}
