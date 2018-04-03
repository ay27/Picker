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
import org.bitman.ay27.module.dp.CommentDP;
import org.bitman.ay27.request.UrlGenerator;
import org.bitman.ay27.view.user.UserInfoActivity;

/**
 * Proudly to use Intellij IDEA.
 * Created by ay27 on 14-9-5.
 */
public class CommentAdapter extends CursorAdapter {
    public CommentAdapter(Context context, Cursor c, boolean autoRequery) {
        super(context, c, autoRequery);
    }

    public CommentAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.comment_item, null);
        ViewHolder holder = new ViewHolder(view);
        view.setTag(holder);

        return view;
    }

    @Override
    public void bindView(View view, final Context context, Cursor cursor) {
        ViewHolder holder = (ViewHolder) view.getTag();

        if (holder.container != null)
            holder.container.cancelRequest();

        final CommentDP commentDP = new Gson().fromJson(cursor.getString(cursor.getColumnIndex(DataTable.KEY_JSON)), CommentDP.class);
        holder.commentID = commentDP.getId();
        if (commentDP.getUserAvatarUrl() != null)
            holder.container = ImageCacheManager.loadImage(UrlGenerator.getResourcesUrl(commentDP.getUserAvatarUrl()),
                    ImageCacheManager.getImageListener(holder.userAvatar, null, null));
        holder.textView.setText(commentDP.getContent());
        holder.timeView.setText(""+commentDP.getDate());
        holder.userName.setText(commentDP.getProducerName());

        holder.userAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, UserInfoActivity.class);
                intent.putExtra("TargetUserID", commentDP.getProducerId());
                context.startActivity(intent);
            }
        });
//        holder.favoriteButton.setFavoriteModule(commentDP, ContentType.Comment);
    }

    public static class ViewHolder {
        public long commentID;
        @InjectView(R.id.comment_list_item_avatar)
        ImageView userAvatar;
        @InjectView(R.id.comment_list_item_content)
        TextView textView;
        @InjectView(R.id.comment_list_item_time)
        TextView timeView;
        @InjectView(R.id.comment_list_item_user_name)
                TextView userName;
//        @InjectView(R.id.comment_list_item_favorite)
//        FavoriteButton favoriteButton;

        ImageLoader.ImageContainer container;

        public ViewHolder(View view) {
            ButterKnife.inject(this, view);
        }
    }
}
