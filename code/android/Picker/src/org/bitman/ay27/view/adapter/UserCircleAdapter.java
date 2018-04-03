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
import org.bitman.ay27.module.Circle;
import org.bitman.ay27.module.dp.CircleDP;

/**
 * Proudly to use Intellij IDEA.
 * Created by ay27 on 14-9-9.
 */
public class UserCircleAdapter extends CursorAdapter {
    public UserCircleAdapter(Context context, Cursor c, boolean autoRequery) {
        super(context, c, autoRequery);
    }


    public UserCircleAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.circle_item, null);
        ViewHolder holder = new ViewHolder(view);
        view.setTag(holder);

        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        ViewHolder holder = (ViewHolder) view.getTag();
        CircleDP item = new Gson().fromJson(cursor.getString(cursor.getColumnIndex(DataTable.KEY_JSON)), CircleDP.class);
        holder.textView.setText(item.getName());
        holder.circleID = item.getId();

    }

    public static class ViewHolder {

        public long circleID;
        @InjectView(R.id.circle_item_textView)
        TextView textView;

        public ViewHolder(View view) {
            ButterKnife.inject(this, view);
        }
    }
}
