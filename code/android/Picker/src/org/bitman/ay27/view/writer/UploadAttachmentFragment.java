package org.bitman.ay27.view.writer;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.*;
import android.widget.Button;
import android.widget.ListView;
import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import org.bitman.ay27.R;
import org.bitman.ay27.common.ContentType;
import org.bitman.ay27.view.adapter.AttachmentFileWithSizeAdapter;

import java.util.ArrayList;

/**
 * Created by ay27 on 14/11/17.
 */
public class UploadAttachmentFragment extends Fragment {
    private static final int GET_FILE = 0x61;
    private static final int RESULT_OK = Activity.RESULT_OK;
    @InjectView(R.id.write_attachment_list)
    ListView listView;
    @InjectView(R.id.write_attachment_add_btn)
    Button addBtn;
    @InjectView(R.id.write_attachment_ok)
    Button okBtn;
    UploadAttachmentCallback callback;
    private long bookId;
    private ContentType type;
    private String chooseBookName;
    private int chooseBookPage;
    private AttachmentFileWithSizeAdapter adapter;

    public UploadAttachmentFragment(long bookId, ContentType type, String chooseBookName, int chooseBookPage, UploadAttachmentCallback callback) {
        this.bookId = bookId;
        this.type = type;
        this.chooseBookName = chooseBookName;
        this.chooseBookPage = chooseBookPage;
        this.callback = callback;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.write_attachment, null);
        ButterKnife.inject(this, view);

        initViews();

        setHasOptionsMenu(true);

        return view;
    }

    private void initViews() {
        listView.setAdapter(adapter = new AttachmentFileWithSizeAdapter(getActivity()));
    }

    @OnClick(R.id.write_attachment_add_btn)
    public void addFile(View v) {
        Intent intent = new Intent();
        intent.setType("multipart/form-data");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, GET_FILE);
    }

    @OnClick(R.id.write_attachment_ok)
    public void next(View view) {
        callback.onResult(adapter.getFilesInUri());
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == GET_FILE && resultCode == RESULT_OK) {
            adapter.addFile(data.getData());
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.write, menu);
        menu.getItem(0).setTitle("next");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.write_commit) {
            next(null);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public static interface UploadAttachmentCallback {
        public void onResult(ArrayList<Uri> files);
    }
}
