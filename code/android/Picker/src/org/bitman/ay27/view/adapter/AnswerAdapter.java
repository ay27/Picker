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
import org.bitman.ay27.R;
import org.bitman.ay27.cache.ImageCacheManager;
import org.bitman.ay27.data.DataTable;
import org.bitman.ay27.module.dp.AnswerDP;
import org.bitman.ay27.request.UrlGenerator;

/**
 * Proudly to use Intellij IDEA.
 * Created by ay27 on 14-9-5.
 */
public class AnswerAdapter extends CursorAdapter {
    public AnswerAdapter(Context context, Cursor c, boolean autoRequery) {
        super(context, c, autoRequery);
    }

    public AnswerAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View v = LayoutInflater.from(context).inflate(R.layout.answer_list_item, null);
        ViewHolder holder = new ViewHolder(v);
        v.setTag(holder);

        return v;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        ViewHolder holder = (ViewHolder) view.getTag();

        if (holder.container != null)
            holder.container.cancelRequest();

        AnswerDP answerDP = new Gson().fromJson(cursor.getString(cursor.getColumnIndex(DataTable.KEY_JSON)), AnswerDP.class);
        holder.answerID = answerDP.getId();
        holder.content.setText(answerDP.getContent());
        if (answerDP.getUserAvatarUrl() != null)
            holder.container = ImageCacheManager.loadImage(UrlGenerator.getResourcesUrl(answerDP.getUserAvatarUrl()),
                    ImageCacheManager.getImageListener(holder.userAvatar, null, null));
        holder.favoriteNum.setText("" + answerDP.getFavoriteNum());
        holder.userName.setText(answerDP.getReplierName());
    }

    public static class ViewHolder {
        public long answerID;
        @InjectView(R.id.answer_list_item_avatar)
        ImageView userAvatar;
        @InjectView(R.id.answer_list_item_favoriteNum)
        TextView favoriteNum;
        @InjectView(R.id.answer_list_item_content)
        TextView content;
        @InjectView(R.id.answer_list_item_user_name)
        TextView userName;
        ImageLoader.ImageContainer container;

        public ViewHolder(View view) {
            ButterKnife.inject(this, view);
        }
    }
}
