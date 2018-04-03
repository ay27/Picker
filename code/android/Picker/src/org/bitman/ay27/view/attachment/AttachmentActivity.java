package org.bitman.ay27.view.attachment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.TextView;
import butterknife.ButterKnife;
import butterknife.InjectView;
import org.bitman.ay27.PickerWidget.AdjustableListView;
import org.bitman.ay27.PickerWidget.BookPageIndicator;
import org.bitman.ay27.PickerWidget.markdown_support.MarkdownView;
import org.bitman.ay27.R;
import org.bitman.ay27.common.OpenFileUtils;
import org.bitman.ay27.common.TaskUtils;
import org.bitman.ay27.common.ToastUtils;
import org.bitman.ay27.module.AttachmentFile;
import org.bitman.ay27.module.dp.AttachmentFeedDp;
import org.bitman.ay27.request.ModuleQueryRequest;
import org.bitman.ay27.request.UrlGenerator;
import org.bitman.ay27.request.Urls;
import org.bitman.ay27.swipe_back.app.SwipeBackActivity;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;
import java.util.List;

/**
 * Proudly to use Intellij IDEA.
 * Created by ay27 on 14-11-5.
 */
public class AttachmentActivity extends SwipeBackActivity implements AdapterView.OnItemClickListener {

    @InjectView(R.id.attachment_activity_book_indicator)
    BookPageIndicator indicator;
    @InjectView(R.id.attachment_activity_header)
    TextView header;
    @InjectView(R.id.attachment_activity_content)
    MarkdownView contentView;
    @InjectView(R.id.attachment_activity_file_list)
    AdjustableListView fileList;
    @InjectView(R.id.nothing)
    View nothingView;
    @InjectView(R.id.attachment_activity_toolbar)
    Toolbar toolbar;

    private long attachmentId, bookID;
    private String bookName;
    private AttachmentFeedDp attachmentDp;
    private boolean openFileAfterDownload = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.attachment_activity);
        ButterKnife.inject(this);

        toolbar.setTitle(R.string.attachment);
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        attachmentId = intent.getLongExtra("AttachmentID", -1);
        bookName = intent.getStringExtra("BookName");
        bookID = intent.getLongExtra("BookID", -1);

        nothingView.setVisibility(View.GONE);

    }

    @Override
    protected void onStart() {
        super.onStart();

        queryModule();
    }

    private void queryModule() {
        String url = UrlGenerator.queryAttachment(attachmentId);
        ModuleQueryRequest.RequestFinishedCallback<AttachmentFeedDp> queryFinished = new ModuleQueryRequest.RequestFinishedCallback<AttachmentFeedDp>() {
            @Override
            public void onFinished(String json, AttachmentFeedDp result) {
                attachmentDp = result;
                bindViews();
            }
        };
        new ModuleQueryRequest(this, url, null, Urls.KEY_ATTACHMENTFEED, AttachmentFeedDp.class, queryFinished);
    }

    private void bindViews() {
        indicator.setText(bookName, attachmentDp.getPage());
        header.setText(attachmentDp.getTitle());
        contentView.loadMarkdown(attachmentDp.getContent());

        fileList.setAdapter(new _AttachmentFileAdapter(this, attachmentDp));
        fileList.setOnItemClickListener(this);

        if (fileList.getCount() <= 0) {
            fileList.setVisibility(View.GONE);
            nothingView.setVisibility(View.VISIBLE);
        }
    }

    private static String getFileNameWithEx(AttachmentFile attachment)
    {
        String name=attachment.getFileName();
        //如果没有后缀名，从path中获取并加上
        if(name.lastIndexOf('.')==-1)
        {
            name=name+getExtension(attachment.getFilePath());
        }
        return name;
    }

    private static String getExtension(String filename) {
        int i = filename.lastIndexOf('.');
        return filename.substring(i);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {

        new AlertDialog.Builder(this)
                .setTitle(getString(R.string.choose))
                .setItems(getResources().getStringArray(R.array.downloadMode), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == 0)
                            onlyDownload(position);
                        else
                            downloadAndOpen(position);
                    }
                })
                .setNegativeButton(getString(R.string.cancel), null)
                .show();

    }

    private void downloadAndOpen(int position) {
        openFileAfterDownload = true;
        download(position);
    }

    private void onlyDownload(int position) {
        openFileAfterDownload = false;
        download(position);
    }

    private void download(int position) {
        String filePath = attachmentDp.getAttachments().get(position).getFilePath();
        String url = UrlGenerator.getResourcesUrl(filePath);

        TaskUtils.executeAsyncTask(new FileDownLoader(url,
                Environment.getExternalStorageDirectory() + "/Picker/download_file/" + getFileNameWithEx(attachmentDp.getAttachments().get(position))));

//        String filePath = attachmentDp.getAttachments().get(position).getFilePath();
//        String url = UrlGenerator.getResourcesUrl(filePath.substring(1, filePath.length()));
//
//        MyDownloadManager manager = MyDownloadManager.getInstance();
//        long downloadId = manager.addTask(url, new S_P_Callback() {
//            @Override
//            public void onProgress(int progress) {
//
//            }
//
//            @Override
//            public void onError(Exception e) {
//                ToastUtils.showError(AttachmentActivity.this, getString(R.string.attachment_download_error));
//            }
//
//            @Override
//            public void onFinished(String serverResponseMessage, File file) {
//                ToastUtils.showSuccess(AttachmentActivity.this, getString(R.string.attachment_download_success));
//
//                if (openFileAfterDownload) {
//                    OpenFileUtils.open(AttachmentActivity.this, file);
//                }
//            }
//
//            @Override
//            public void onStart(long id, String fileName) {
//                ToastUtils.showWaiting(AttachmentActivity.this, getString(R.string.attachment_begin_download));
//            }
//
//            @Override
//            public void onPause(int currentFileSize, int fileSize) {
//
//            }
//        });
    }

    class FileDownLoader extends AsyncTask {

        String url;
        String filePath;
        File file;

        public FileDownLoader(String url, String filePath) {
            this.url = url;
            this.filePath = filePath;
        }

        @Override
        protected Object doInBackground(Object[] params) {
            HttpURLConnection connection = null;
            try {
                URL url = new URL(this.url);
                System.out.println(url.toString());
                connection = (HttpURLConnection) url.openConnection();
                connection.setUseCaches(false);
                connection.setReadTimeout(20000);
                connection.setConnectTimeout(20000);

                connection.setRequestMethod("GET");
                connection.setRequestProperty("Connection", "Keep-Alive");
                connection.setRequestProperty("Cache-Control", "no-cache");
                file = new File(filePath);
                RandomAccessFile raFile = new RandomAccessFile(file, "rwd");
                InputStream input = connection.getInputStream();
                byte[] buffer = new byte[1024];
                int bytes = -1;
                while ((bytes = input.read(buffer)) != -1) {
                    raFile.write(buffer, 0, bytes);
                }
            } catch (ProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
            }
            return true;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);

            ToastUtils.showSuccess(AttachmentActivity.this, getString(R.string.attachment_download_success));

            if (openFileAfterDownload) {
                OpenFileUtils.open(AttachmentActivity.this, file);
            }
        }
    }

    class _AttachmentFileAdapter extends BaseAdapter {

        List<AttachmentFile> files;
        Context context;

        public _AttachmentFileAdapter(Context context, AttachmentFeedDp attachmentDp) {
            this.context = context;
            this.files = attachmentDp.getAttachments();
        }

        @Override
        public int getCount() {
            return files.size();
        }

        @Override
        public Object getItem(int position) {
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
                convertView = LayoutInflater.from(context).inflate(R.layout.attachment_file_item, null);
                holder = new ViewHolder(convertView);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            holder.fileName.setText(files.get(position).getFileName());

            return convertView;
        }

        class ViewHolder {
            @InjectView(R.id.attachment_file_item_text)
            TextView fileName;

            public ViewHolder(View view) {
                ButterKnife.inject(this, view);
            }
        }
    }
}
