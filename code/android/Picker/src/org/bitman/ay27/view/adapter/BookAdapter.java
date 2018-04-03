package org.bitman.ay27.view.adapter;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;
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
 * Created by ay27 on 14-9-8.
 */
public class BookAdapter extends CursorAdapter {

    public BookAdapter(Context context, Cursor c, boolean autoRequery) {
        super(context, c, autoRequery);
    }

    public BookAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.book_item, null);
        ViewHolder holder = new ViewHolder(view);
        view.setTag(holder);

        return view;
    }

//    @Override
//    public Object getItem(int position) {
//        Cursor cursor = getCursor();
//        cursor.moveToPosition(position);
//        return new Gson().fromJson(cursor.getString(cursor.getColumnIndex(DataTable.KEY_JSON)), Book.class);
//    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        ViewHolder holder = (ViewHolder) view.getTag();

        if (holder.imageRequest != null)
            holder.imageRequest.cancelRequest();

        Book item = new Gson().fromJson(cursor.getString(cursor.getColumnIndex(DataTable.KEY_JSON)), Book.class);
        holder.bookName.setText(item.getName());
        holder.bookNameStr = item.getName();
        holder.imageRequest = ImageCacheManager.loadImage(UrlGenerator.getResourcesUrl(item.getCoverUrl()),
                ImageCacheManager.getImageListener(holder.bookCover, null, null));
        holder.bookID = item.getId();
        Log.i("book", "bookId = " + item.getId() + " imageUrl=" + item.getCoverUrl() + " name=" + item.getName());
    }

    public static class ViewHolder {
        public ImageLoader.ImageContainer imageRequest;
        public long bookID;
        public String bookNameStr;
        @InjectView(R.id.book_item_imageView)
        ImageView bookCover;
        @InjectView(R.id.book_item_textView)
        TextView bookName;

        ViewHolder(View view) {
            ButterKnife.inject(this, view);
        }
    }

}

