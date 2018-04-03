package org.bitman.ay27.view.question_content;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import butterknife.ButterKnife;
import butterknife.InjectView;
import org.bitman.ay27.R;
import org.bitman.ay27.swipe_back.app.SwipeBackActivity;

/**
 * Proudly to use Intellij IDEA.
 * Created by ay27 on 14-10-28.
 */
public class AnswerContentActivity extends SwipeBackActivity {
    @InjectView(R.id.fragment_toolbar)
    Toolbar toolbar;
    private long answerId;
    private int bookPage;
    private String bookName;
    private Fragment fragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_container);
        ButterKnife.inject(this);


        answerId = getIntent().getLongExtra("AnswerID", -1);
        bookName = getIntent().getStringExtra("BookName");
        bookPage = getIntent().getIntExtra("BookPage", -1);

        toolbar.setTitle(R.string.note_title);
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);

        fragment = new AnswerFragment(answerId, bookName, bookPage);
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();

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


}
