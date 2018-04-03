package org.bitman.ay27.view.adapter;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;
import butterknife.ButterKnife;
import butterknife.InjectView;
import com.google.gson.Gson;
import org.bitman.ay27.R;
import org.bitman.ay27.data.DataTable;
import org.bitman.ay27.module.AttachmentFile;
import org.bitman.ay27.module.dp.CommentDP;

/**
 * Created by ay27 on 14-11-5.
 */
public class AttachmentFileAdapter extends CursorAdapter {
    public AttachmentFileAdapter(Context context, Cursor c, boolean autoRequery) {
        super(context, c, autoRequery);
    }

    public AttachmentFileAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.attachment_file_item, null);
        ViewHolder holder = new ViewHolder(view);
        view.setTag(holder);

        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        ViewHolder holder = (ViewHolder) view.getTag();

        AttachmentFile file = new Gson().fromJson(cursor.getString(cursor.getColumnIndex(DataTable.KEY_JSON)), AttachmentFile.class);
        holder.textView.setText(file.getFileName());
        holder.attachmentId = file.getId();
        holder.filePath = file.getFilePath();
    }

    public static class ViewHolder {
        public long attachmentId;
        public String filePath;
        @InjectView(R.id.attachment_file_item_text)
        TextView textView;

        public ViewHolder(View view) {
            ButterKnife.inject(this, view);
        }
    }
}
