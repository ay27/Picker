package org.bitman.ay27.view.main;

import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import butterknife.ButterKnife;
import butterknife.InjectView;
import org.bitman.ay27.R;
import org.bitman.ay27.common.OpenFileUtils;
import org.bitman.ay27.common.Utils;
import org.bitman.ay27.upload_download.MyDownloadManager;

import java.io.*;
import java.util.Arrays;
import java.util.List;

/**
 * Proudly to use Intellij IDEA.
 * Created by ay27 on 14/12/3.
 */
public class FileFragment extends Fragment {

    @InjectView(R.id.list_without_divider_list)
    ListView listView;
    private _AttachmentFileWithSizeAdapter adapter;

    private AdapterView.OnItemClickListener onFileItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            OpenFileUtils.open(getActivity(), adapter.getItem(position));
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.list_without_divider, null);
        ButterKnife.inject(this, view);

        File file = new File(Environment.getExternalStorageDirectory() + "/" + MyDownloadManager.APP_FILE_DIR);

        if (!file.exists()) {
            file.mkdir();
        }

        List<File> files = Arrays.asList(file.listFiles());
        listView.setAdapter(adapter = new _AttachmentFileWithSizeAdapter(getActivity(), files));
        listView.setOnItemClickListener(onFileItemClickListener);
        return view;
    }

    class _AttachmentFileWithSizeAdapter extends BaseAdapter {

        private List<File> files;
        private Context context;

        public _AttachmentFileWithSizeAdapter(Context context, List<File> files) {
            this.context = context;
            this.files = files;
        }

        @Override
        public int getCount() {
            return files.size();
        }

        @Override
        public File getItem(int position) {
            return files.get(position);
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

            File file = files.get(position);
            if (!file.exists()) {
                Toast.makeText(context, "fatal error in adding file", Toast.LENGTH_SHORT).show();
                return null;
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

        class ViewHolder {
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
}
