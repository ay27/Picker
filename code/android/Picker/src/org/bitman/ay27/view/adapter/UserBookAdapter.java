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
import org.bitman.ay27.module.Book;
import org.bitman.ay27.request.UrlGenerator;

/**
 * Proudly to use Intellij IDEA.
 * Created by ay27 on 14-9-9.
 */
public class UserBookAdapter extends CursorAdapter {

    public UserBookAdapter(Context context, Cursor c, boolean autoRequery) {
        super(context, c, autoRequery);
    }

    public UserBookAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.user_book_list, null);
        ViewHolder holder = new ViewHolder(view);
        view.setTag(holder);
        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        ViewHolder holder = (ViewHolder) view.getTag();
        if (holder.container != null)
            holder.container.cancelRequest();
        Book book = new Gson().fromJson(cursor.getString(cursor.getColumnIndex(DataTable.KEY_JSON)), Book.class);
        holder.bookId = book.getId();
        holder.bookName.setText(book.getName());
        holder.container = ImageCacheManager.loadImage(UrlGenerator.getResourcesUrl(book.getCoverUrl()),
                ImageCacheManager.getImageListener(holder.bookCover, null, null));
    }

    public static class ViewHolder {
        public long bookId;
        @InjectView(R.id.user_book_image)
        ImageView bookCover;
        @InjectView(R.id.user_book_textView)
        TextView bookName;
        ImageLoader.ImageContainer container;

        public ViewHolder(View view) {
            ButterKnife.inject(this, view);
        }
    }
}
