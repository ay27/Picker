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
import org.bitman.ay27.module.dp.CircleMemberDp;
import org.bitman.ay27.request.UrlGenerator;
import org.bitman.ay27.view.user.UserInfoActivity;

/**
 * Proudly to use Intellij IDEA.
 * Created by ay27 on 14-9-5.
 */
public class UserItemAdapter extends CursorAdapter {

    public UserItemAdapter(Context context, Cursor c, boolean autoRequery) {
        super(context, c, autoRequery);
    }

    public UserItemAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.user_list_item, null);
        ViewHolder holder = new ViewHolder(view);
        view.setTag(holder);

        return view;
    }

    @Override
    public void bindView(View view, final Context context, Cursor cursor) {
        ViewHolder holder = (ViewHolder) view.getTag();

        if (holder.container != null)
            holder.container.cancelRequest();

        final CircleMemberDp item = new Gson().fromJson(cursor.getString(cursor.getColumnIndex(DataTable.KEY_JSON)), CircleMemberDp.class);
        holder.textView.setText(item.getName());
        if (item.getAvatarUrl() != null)
            holder.container = ImageCacheManager.loadImage(UrlGenerator.getResourcesUrl(item.getAvatarUrl()),
                    ImageCacheManager.getImageListener(holder.imageView, null, null));
        holder.userID = item.getId();

        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, UserInfoActivity.class);
                intent.putExtra("TargetUserID", item.getId());
                context.startActivity(intent);
            }
        });

    }

    public static class ViewHolder {
        public long userID;
        @InjectView(R.id.user_list_item_avatar)
        ImageView imageView;
        @InjectView(R.id.user_list_item_name)
        TextView textView;
        ImageLoader.ImageContainer container;

        public ViewHolder(View view) {
            ButterKnife.inject(this, view);
        }
    }
}
