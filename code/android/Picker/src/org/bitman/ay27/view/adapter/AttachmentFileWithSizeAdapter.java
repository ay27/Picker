package org.bitman.ay27.view.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;
import butterknife.ButterKnife;
import butterknife.InjectView;
import org.bitman.ay27.R;
import org.bitman.ay27.common.Utils;

import java.io.*;
import java.util.ArrayList;

/**
 * Proudly to use Intellij IDEA.
 * Created by ay27 on 14/12/3.
 */
public class AttachmentFileWithSizeAdapter extends BaseAdapter {

    private ArrayList<Uri> filesInUri;
    private Context context;

    public AttachmentFileWithSizeAdapter(Context context) {
        this.context = context;
        filesInUri = new ArrayList<Uri>();
    }

    public AttachmentFileWithSizeAdapter(Context context, ArrayList<Uri> files) {
        this.context = context;
        this.filesInUri = files;
    }

    public void addFile(Uri uri) {
        filesInUri.add(uri);
        this.notifyDataSetInvalidated();
    }

    public ArrayList<Uri> getFilesInUri() {
        return filesInUri;
    }

    @Override
    public int getCount() {
        return filesInUri.size();
    }

    @Override
    public Object getItem(int position) {
        return filesInUri.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.attachment_file_item_with_size, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        String filePath = null;
        filePath = Utils.getPathFromUri(context, filesInUri.get(position));

        File file = new File(filePath);
        if (!file.exists()) {
            Toast.makeText(context, "fatal error in adding file", Toast.LENGTH_SHORT).show();
            return convertView;
        }

        holder.fileName.setText(file.getName());

        try {
            InputStream is = new FileInputStream(file);
            holder.setFileSize(is.available());
            is.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


        return convertView;
    }

    static class ViewHolder {
        @InjectView(R.id.attachment_file_item_with_size_name)
        TextView fileName;
        @InjectView(R.id.attachment_file_item_with_size_size)
        TextView fileSize;

        public ViewHolder(View view) {
            ButterKnife.inject(this, view);
        }

        public void setFileSize(int size) {
            fileSize.setText(Utils.formatFileSize(size));
        }

    }
}