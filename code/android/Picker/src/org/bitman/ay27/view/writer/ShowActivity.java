package org.bitman.ay27.view.writer;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.*;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import butterknife.ButterKnife;
import butterknife.InjectView;
import org.bitman.ay27.PickerWidget.BookPageIndicator;
import org.bitman.ay27.PickerWidget.markdown_support.MarkdownView;
import org.bitman.ay27.R;
import org.bitman.ay27.common.ContentType;
import org.bitman.ay27.common.ToastUtils;
import org.bitman.ay27.common.Utils;
import org.bitman.ay27.swipe_back.app.SwipeBackActivity;
import org.bitman.ay27.view.writer.upload.UploadCallback;
import org.bitman.ay27.view.writer.upload.Uploader;

import java.io.File;
import java.util.ArrayList;


/**
 * Proudly to use Intellij IDEA.
 * Created by ay27 on 14-8-2.
 */
public class ShowActivity extends SwipeBackActivity {

    @InjectView(R.id.show_book_indicator)
    BookPageIndicator indicator;
    @InjectView(R.id.show_title)
    TextView title;
    @InjectView(R.id.show_markdown_view)
    MarkdownView markdownView;
    @InjectView(R.id.show_attachment_list)
    ListView attachmentList;
    @InjectView(R.id.show_attachment_hint)
    View attachmentHint;
    @InjectView(R.id.show_activity_toolbar)
    Toolbar toolbar;

    private String header;
    private String bookName;
    private int bookPage;
    private ContentType type;
    private String data;
    private long bookID, questionID;
    private ArrayList<Uri> files;
    private UploadCallback uploadCallback = new UploadCallback() {

        private ProgressDialog pd;

        @Override
        public void onPreExecute() {
            pd = new ProgressDialog(ShowActivity.this);
            pd.setProgress(0);
            pd.show();
        }

        @Override
        public void onProgressUpdate(double value) {
            pd.setProgress((int) value);
        }

        @Override
        public void onPostExecute() {
            pd.dismiss();
            ToastUtils.showSuccess(ShowActivity.this, getString(R.string.commit_succeed));
        }

        @Override
        public void onError() {
            ToastUtils.showError(ShowActivity.this, getString(R.string.commit_failed));
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_activity);
        ButterKnife.inject(this);

        toolbar.setTitle(getString(R.string.writer_preview));
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        bookName = intent.getStringExtra("BookName");
        bookPage = intent.getIntExtra("BookPage", -1);

        header = intent.getStringExtra("Header");
        data = intent.getStringExtra("Content");
        bookID = intent.getLongExtra("BookID", -1);
        questionID = intent.getLongExtra("QuestionID", -1);
        type = (ContentType) intent.getSerializableExtra("Type");

        files = intent.getParcelableArrayListExtra("AttachmentFileList");
        if (files == null || files.isEmpty()) {
            attachmentHint.setVisibility(View.GONE);
            attachmentList.setVisibility(View.GONE);
        } else {
            attachmentList.setAdapter(new _Adapter(this, files));
        }

        indicator.setText(bookName, bookPage);
        title.setText(header);

        markdownView.loadMarkdown(data);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(false);
        getSupportActionBar().setHomeButtonEnabled(true);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.show, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.show_commit_button) {
            if (type == ContentType.Answer)
                new Uploader(ContentType.Answer, questionID, data, uploadCallback);
            else if (type == ContentType.Question || type == ContentType.Note)
                new Uploader(type, bookID, bookPage, header, data, uploadCallback);
            else if (type == ContentType.Attachment)
                new Uploader(bookID, bookPage, header, data, files, uploadCallback);
            return true;
        } else if (item.getItemId() == android.R.id.home) {
            super.onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    static class _Adapter extends BaseAdapter {
        private ArrayList<Uri> files;
        private Context context;

        public _Adapter(Context context, ArrayList<Uri> files) {
            this.context = context;
            this.files = files;
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
            holder.fileName.setText(new File(Utils.getPathFromUri(context, files.get(position))).getName());

            return convertView;
        }

        static class ViewHolder {
            @InjectView(R.id.attachment_file_item_text)
            TextView fileName;

            public ViewHolder(View view) {
                ButterKnife.inject(this, view);
            }
        }
    }
}
