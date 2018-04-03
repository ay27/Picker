package org.bitman.ay27.view.writer;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import butterknife.ButterKnife;
import butterknife.InjectView;
import org.bitman.ay27.R;
import org.bitman.ay27.common.ContentType;
import org.bitman.ay27.swipe_back.app.SwipeBackActivity;

import java.util.ArrayList;

/**
 * Proudly to use Intellij IDEA.
 * Created by ay27 on 14/11/17.
 */
public class WriteActivity extends SwipeBackActivity {

    private static final int FRAGMENT_INIT = 0x0;
    private static final int FRAGMENT_CHOOSE_BOOK = 0x1;
    private static final int FRAGMENT_WRITE_CONTENT = 0x2;
    private static final int FRAGMENT_ATTACHMENT = 0x3;


    private long chooseBookId, questionId;
    private ContentType type;
    private String chooseBookName;
    private String questionTitle;
    private int chooseBookPage;
    private ArrayList<Uri> uploadFiles;
    @InjectView(R.id.fragment_toolbar)
    Toolbar toolbar;


    private ChooseBookPageFragment.ChooseBookPageCallback chooseBookPageCallback = new ChooseBookPageFragment.ChooseBookPageCallback() {
        @Override
        public void onResult(long bookId, String bookName, int bookPage) {
            chooseBookName = bookName;
            chooseBookPage = bookPage;
            chooseBookId = bookId;
            doNext(FRAGMENT_CHOOSE_BOOK);
        }
    };

    private UploadAttachmentFragment.UploadAttachmentCallback uploadAttachmentCallback = new UploadAttachmentFragment.UploadAttachmentCallback() {
        @Override
        public void onResult(ArrayList<Uri> files) {
            uploadFiles = files;
            doNext(FRAGMENT_ATTACHMENT);
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_container);
        ButterKnife.inject(this);


        Intent intent = getIntent();
        chooseBookId = intent.getLongExtra("BookID", -1);
        chooseBookName = intent.getStringExtra("BookName");
        questionId = intent.getLongExtra("QuestionID", -1);
        type = (ContentType) intent.getSerializableExtra("Type");
        questionTitle = intent.getStringExtra("QuestionTitle");

        switch (type) {
            case Question:
                toolbar.setTitle(R.string.writer_question);
                break;
            case Note:
                toolbar.setTitle(R.string.writer_note);
                break;
            case Attachment:
                toolbar.setTitle(R.string.writer_attachment);
                break;
            case Answer:
                toolbar.setTitle(R.string.writer_answer);
                break;
        }

        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);

        doNext(FRAGMENT_INIT);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(false);
        getSupportActionBar().setHomeButtonEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            super.onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void doNext(int currentFragment) {
        switch (currentFragment) {
            case FRAGMENT_INIT:
                if (type == ContentType.Question || type == ContentType.Note || type == ContentType.Attachment) {
                    changeContentView(FRAGMENT_CHOOSE_BOOK);
                } else if (type == ContentType.Answer) {
                    changeContentView(FRAGMENT_WRITE_CONTENT);
                }
                break;
            case FRAGMENT_CHOOSE_BOOK:
                if (type == ContentType.Attachment) {
                    changeContentView(FRAGMENT_ATTACHMENT);
                } else
                    changeContentView(FRAGMENT_WRITE_CONTENT);
                break;
            case FRAGMENT_ATTACHMENT:
                changeContentView(FRAGMENT_WRITE_CONTENT);
                break;
            default:
                break;
        }
    }

    private void changeContentView(int nextFragmentId) {

        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment mContentFragment;

        switch (nextFragmentId) {
            case FRAGMENT_CHOOSE_BOOK:
                mContentFragment = new ChooseBookPageFragment(chooseBookId, type, chooseBookPageCallback);
                break;
            case FRAGMENT_ATTACHMENT:
                mContentFragment = new UploadAttachmentFragment(chooseBookId, type, chooseBookName, chooseBookPage, uploadAttachmentCallback);
                break;
            case FRAGMENT_WRITE_CONTENT:
                if (type == ContentType.Answer)
                    mContentFragment = new WriteContentFragment(type, chooseBookName, chooseBookPage, questionId, questionTitle);
                else
                    mContentFragment = new WriteContentFragment(type, chooseBookId, chooseBookName, chooseBookPage, uploadFiles);
                break;
            default:
                mContentFragment = null;
        }
        fragmentManager.beginTransaction().replace(R.id.content_frame, mContentFragment).commit();
    }

}
