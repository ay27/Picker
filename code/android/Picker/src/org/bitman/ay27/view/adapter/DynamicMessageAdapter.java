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
import org.bitman.ay27.module.dp.MessageDp;
import org.bitman.ay27.request.UrlGenerator;
import org.bitman.ay27.view.user.UserInfoActivity;

import java.text.SimpleDateFormat;

/**
 * Proudly to use Intellij IDEA.
 * Created by ay27 on 14-10-1.
 */
public class DynamicMessageAdapter extends CursorAdapter {

    private String[] actionTexts;

    public DynamicMessageAdapter(Context context, Cursor c, boolean autoRequery) {
        super(context, c, autoRequery);
        initial(context);
    }

    public DynamicMessageAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
        initial(context);
    }

    private void initial(Context context) {
        actionTexts = context.getResources().getStringArray(R.array.dynamic_message_action);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.dynamic_list_item, null);
        ViewHolder holder = new ViewHolder(view);
        view.setTag(holder);

        return view;
    }

    @Override
    public void bindView(View view, final Context context, Cursor cursor) {
        ViewHolder holder = (ViewHolder) view.getTag();
        final MessageDp message = new Gson().fromJson(cursor.getString(cursor.getColumnIndex(DataTable.KEY_JSON)), MessageDp.class);
        holder.messageId = message.getId();
        holder.type = message.getType();
        holder.targetId = message.getRelatedSourceId();

            holder.userName.setText(message.getProducerName());
        holder.actionText.setText(actionTexts[message.getType()]);
        holder.content.setText(message.getRelatedSourceContent());
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        holder.time.setText(format.format(message.getTime()));

        if (holder.container != null) {
            holder.container.cancelRequest();
        }

        holder.container = ImageCacheManager.loadImage(UrlGenerator.getResourcesUrl(message.getProducerAvatarUrl()),
                ImageCacheManager.getImageListener(holder.avatar, null, null));

        holder.avatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, UserInfoActivity.class);
                intent.putExtra("TargetUserID", message.getProducerId());
                context.startActivity(intent);
            }
        });
    }

    public static class ViewHolder {
        @InjectView(R.id.dynamic_list_item_avatar)
        ImageView avatar;
        @InjectView(R.id.dynamic_list_item_user_name)
        TextView userName;
        @InjectView(R.id.dynamic_list_item_action)
        TextView actionText;
        @InjectView(R.id.dynamic_list_item_content)
        TextView content;
        @InjectView(R.id.dynamic_list_item_time)
        TextView time;

        ImageLoader.ImageContainer container;

        public long messageId;
        public int type;
        public long targetId;

        public ViewHolder(View v) {
            ButterKnife.inject(this, v);
        }
    }
}
