package org.bitman.ay27.view.feed;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import butterknife.ButterKnife;
import butterknife.InjectView;
import org.bitman.ay27.R;
import org.bitman.ay27.common.ContentType;
import org.bitman.ay27.swipe_back.app.SwipeBackActivity;
import org.bitman.ay27.view.writer.ChooseBookPageFragment;

/**
 * Proudly to use Intellij IDEA.
 * Created by ay27 on 14/12/1.
 */
public class SearchActivity extends SwipeBackActivity {

    private static final int FRAGMENT_INIT = 0x0;
    private static final int FRAGMENT_CHOOSE_BOOK = 0x1;
    @InjectView(R.id.fragment_toolbar)
    Toolbar toolbar;
    private long bookId;
    private String bookName;
    private int bookPage;
    private ChooseBookPageFragment.ChooseBookPageCallback chooseBookPageCallback = new ChooseBookPageFragment.ChooseBookPageCallback() {
        @Override
        public void onResult(long chooseBookId, String chooseBookName, int chooseBookPage) {
            bookId = chooseBookId;
            bookName = chooseBookName;
            bookPage = chooseBookPage;
            doNext(FRAGMENT_CHOOSE_BOOK);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_container);
        ButterKnife.inject(this);

        toolbar.setTitle(R.string.search);
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        bookId = intent.getLongExtra("BookID", -1);
        bookName = intent.getStringExtra("BookName");
        doNext(FRAGMENT_INIT);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(false);
        getSupportActionBar().setHomeButtonEnabled(true);
    }

    private void doNext(int currentFragment) {

        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment mContentFragment = null;

        switch (currentFragment) {
            case FRAGMENT_INIT:
                mContentFragment = new ChooseBookPageFragment(bookId, ContentType.Search, chooseBookPageCallback);
                break;
            case FRAGMENT_CHOOSE_BOOK:
                mContentFragment = new SearchResultFragment(bookId, bookName, bookPage);
                break;
            default:
                break;
        }

        fragmentManager.beginTransaction().replace(R.id.content_frame, mContentFragment).commit();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            super.onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
