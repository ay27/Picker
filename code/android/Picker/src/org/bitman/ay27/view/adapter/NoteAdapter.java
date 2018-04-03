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
import org.bitman.ay27.module.dp.FeedDp;
import org.bitman.ay27.request.UrlGenerator;
import org.bitman.ay27.view.user.UserInfoActivity;

/**
 * Proudly to use Intellij IDEA.
 * Created by ay27 on 14-9-22.
 */

public class NoteAdapter extends CursorAdapter {

    public NoteAdapter(Context context, Cursor c, boolean autoRequery) {
        super(context, c, autoRequery);
    }

    public NoteAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View v = LayoutInflater.from(context).inflate(R.layout.feed_listview_item, null);
        ViewHolder holder = new ViewHolder(v);
        v.setTag(holder);

        return v;
    }

    @Override
    public void bindView(View view, final Context context, Cursor cursor) {
        ViewHolder holder = (ViewHolder) view.getTag();

        if (holder.container != null)
            holder.container.cancelRequest();

        final FeedDp noteDP = new Gson().fromJson(cursor.getString(cursor.getColumnIndex(DataTable.KEY_JSON)), FeedDp.class);
        holder.noteID = noteDP.getId();
        holder.header.setText(noteDP.getTitle());
        holder.content.setText(noteDP.getContent());
        holder.page.setText(""+noteDP.getPage());
        if (noteDP.getUserAvatarUrl() != null)
            holder.container = ImageCacheManager.loadImage(UrlGenerator.getResourcesUrl(noteDP.getUserAvatarUrl()),
                    ImageCacheManager.getImageListener(holder.avatar, null, null));

        holder.avatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, UserInfoActivity.class);
                intent.putExtra("TargetUserID", noteDP.getUserId());
                context.startActivity(intent);
            }
        });
    }



    public static class ViewHolder {
        @InjectView(R.id.feed_list_item_header)
        TextView header;
        @InjectView(R.id.feed_list_item_avatar)
        ImageView avatar;
        @InjectView(R.id.feed_list_item_content)
        TextView content;
        @InjectView(R.id.feed_list_item_page)
        TextView page;

        public long noteID;

        ImageLoader.ImageContainer container;

        public ViewHolder(View v) {
            ButterKnife.inject(this, v);
        }

    }
}