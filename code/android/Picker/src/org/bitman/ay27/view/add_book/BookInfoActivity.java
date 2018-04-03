package org.bitman.ay27.view.add_book;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import com.android.volley.Response;
import com.android.volley.toolbox.ImageLoader;
import org.bitman.ay27.R;
import org.bitman.ay27.cache.ImageCacheManager;
import org.bitman.ay27.common.ToastUtils;
import org.bitman.ay27.module.Book;
import org.bitman.ay27.request.*;
import org.bitman.ay27.swipe_back.app.SwipeBackActivity;
import org.bitman.ay27.view.main.MainActivity;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Proudly to use Intellij IDEA.
 * Created by ay27 on 14-8-2.
 */
public class BookInfoActivity extends SwipeBackActivity {

    @InjectView(R.id.book_result_header)
    TextView header;
    @InjectView(R.id.book_result_cover)
    ImageView cover;

    @InjectView(R.id.book_result_isbn)
    TextView isbnView;
    @InjectView(R.id.book_result_writer)
    TextView writerView;
    @InjectView(R.id.book_result_press)
    TextView pressView;
    @InjectView(R.id.book_result_follow)
    TextView followView;
    @InjectView(R.id.book_result_question)
    TextView questionView;
    @InjectView(R.id.book_result_note)
    TextView noteView;
    @InjectView(R.id.book_result_commit)
    Button commitButton;
    @InjectView(R.id.book_result_cancel)
    Button cancelButton;
    @InjectView(R.id.add_book_result_toolbar)
    Toolbar toolbar;

    private ImageLoader.ImageContainer container;

    private String ISBN;
    private Book book;
    private long bookId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_book_result);
        ButterKnife.inject(this);

        toolbar.setTitle(R.string.add_book_title);
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);

        ISBN = getIntent().getStringExtra("ISBN");
        bookId = getIntent().getLongExtra("BookID", -1);

        doQueryBookInfo();
    }

    @OnClick(R.id.book_result_commit)
    public void commit(View view) {
        doAddBook();
    }

    @OnClick(R.id.book_result_cancel)
    public void cancel(View view) {
        startBack2MainActivity();
    }

    private void doQueryBookInfo() {

        ModuleQueryRequest.RequestFinishedCallback<Book> requestFinished = new ModuleQueryRequest.RequestFinishedCallback<Book>() {
            @Override
            public void onFinished(String json, Book result) {
                book = result;
                if (book == null) {
                    ToastUtils.showError(BookInfoActivity.this, getString(R.string.add_book_result_no_book));
                    startBack2MainActivity();
                    return;
                }
                setViewAfterBookReady();
            }
        };

        String url = (bookId==-1) ? UrlGenerator.queryBookByISBN(ISBN) : UrlGenerator.queryBookById(bookId);
        new ModuleQueryRequest(this, url, null, Urls.KEY_BOOK, Book.class, requestFinished);
    }

    private void setViewAfterBookReady() {
        toolbar.setTitle(book.getName());

        header.setText(book.getName());
        isbnView.setText("" + book.getIsbn());
        writerView.setText(book.getWriter());
        pressView.setText(book.getPress());
        followView.setText("" + book.getFollowNum());
        questionView.setText("" + book.getQuestionNum());
        noteView.setText("" + book.getNoteNum());

        if (container != null)
            container.cancelRequest();
        container = ImageCacheManager.loadImage(UrlGenerator.getResourcesUrl(book.getCoverUrl()),
                ImageCacheManager.getImageListener(cover, null, null));
    }


    private void doAddBook() {
        assert book != null;

        Response.Listener<JSONObject> successListener = new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                try {
                    int status = jsonObject.getInt(Urls.KEY_STATUS);
                    if (status == Status.SUCCESS) {
                        ToastUtils.showSuccess(BookInfoActivity.this, getString(R.string.add_book_success));
                    } else {
                        ToastUtils.showError(BookInfoActivity.this, getString(R.string.add_book_failed));
                    }
                    startBack2MainActivity();
                } catch (JSONException e) {
                    e.printStackTrace();
                    ToastUtils.showError(BookInfoActivity.this, getString(R.string.add_book_failed));
                }
            }
        };
        String url = UrlGenerator.addBook(book.getId());
        new PickerRequest(url, null, successListener, PickerRequest.defaultErrorListener(this));
    }

    private void startBack2MainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

}
