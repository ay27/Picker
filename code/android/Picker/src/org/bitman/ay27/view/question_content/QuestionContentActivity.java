package org.bitman.ay27.view.question_content;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;
import butterknife.ButterKnife;
import butterknife.InjectView;
import com.google.gson.reflect.TypeToken;
import org.bitman.ay27.PickerWidget.AdjustableListView;
import org.bitman.ay27.PickerWidget.BookPageIndicator;
import org.bitman.ay27.PickerWidget.markdown_support.MarkdownView;
import org.bitman.ay27.R;
import org.bitman.ay27.common.ContentType;
import org.bitman.ay27.data.DataTable;
import org.bitman.ay27.module.dp.AnswerDP;
import org.bitman.ay27.module.dp.FeedDp;
import org.bitman.ay27.request.UrlGenerator;
import org.bitman.ay27.request.Urls;
import org.bitman.ay27.view.adapter.AnswerAdapter;
import org.bitman.ay27.view.templete.ListSupportActivity;
import org.bitman.ay27.view.widget.FavoriteButton;
import org.bitman.ay27.view.widget.FollowButton;
import org.bitman.ay27.view.writer.WriteActivity;

import java.util.List;

/**
 * Proudly to use Intellij IDEA.
 * Created by ay27 on 14-10-27.
 */
public class QuestionContentActivity extends ListSupportActivity<FeedDp> {

    @InjectView(R.id.question_content_activity_book_indicator)
    BookPageIndicator indicator;
    @InjectView(R.id.question_content_activity_header)
    TextView header;
    @InjectView(R.id.question_content_activity_content)
    MarkdownView mdView;
    @InjectView(R.id.question_content_activity_favorite)
    FavoriteButton favoriteButton;
    @InjectView(R.id.follow_button)
    FollowButton followButton;
    @InjectView(R.id.question_content_activity_answer_list)
    AdjustableListView answerList;
    @InjectView(R.id.nothing)
    View nothingView;
    @InjectView(R.id.question_content_activity_toolbar)
    Toolbar toolbar;


    private long questionID;
    private FeedDp questionDP;
    private String bookName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.question_content_activity);
        ButterKnife.inject(this);

        toolbar.setTitle(R.string.question_content_title);
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        questionID = intent.getLongExtra("QuestionID", -1);
        bookName = intent.getStringExtra("BookName");

        nothingView.setVisibility(View.GONE);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(false);
        getSupportActionBar().setHomeButtonEnabled(true);

    }

    @Override
    protected Parameters getChildrenParameters() {
        return new Parameters(UrlGenerator.queryQuestion(questionID), Urls.KEY_QUESTION, FeedDp.class, answerList, new TypeToken<List<AnswerDP>>() {
        }.getType(),
                new AnswerAdapter(this, null, false), DataTable.answerDP, UrlGenerator.queryAnswerList(questionID), Urls.KEY_ANSWER_LIST);
    }

    @Override
    protected void step5BindViews(FeedDp result) {
        questionDP = result;
        indicator.setText(bookName, questionDP.getPage());
        favoriteButton.setFavoriteModule(questionDP, ContentType.Question);
        followButton.setFollowModule(questionDP, ContentType.Question);

        header.setText(questionDP.getTitle());
        mdView.loadMarkdown(questionDP.getContent());

//        aboveView.setVisibility(View.GONE);
//        bottom.setVisibility(View.VISIBLE);

        if (questionDP.getAnswerNum() <= 0) {
            answerList.setVisibility(View.GONE);
            nothingView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        AnswerAdapter.ViewHolder holder = (AnswerAdapter.ViewHolder) view.getTag();
        if (holder == null)
            return;
        Intent intent = new Intent(this, AnswerContentActivity.class);
        intent.putExtra("AnswerID", holder.answerID);
        intent.putExtra("QuestionTitle", questionDP.getTitle());
        intent.putExtra("QuestionID", questionID);
        intent.putExtra("BookName", bookName);
        intent.putExtra("BookPage", questionDP.getPage());

        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.question_content, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_question_content_add_answer_button) {
            Intent intent = new Intent(this, WriteActivity.class);
            intent.putExtra("QuestionID", questionID);
            intent.putExtra("BookID", questionDP.getBookId());
            intent.putExtra("BookName", bookName);
            intent.putExtra("BookPage", questionDP.getPage());
            intent.putExtra("QuestionTitle", questionDP.getTitle());
            intent.putExtra("Type", ContentType.Answer);
            startActivity(intent);
            return true;
        } else if (item.getItemId() == android.R.id.home) {
            super.onBackPressed();
            return true;
        } else
            return super.onOptionsItemSelected(item);
    }
}
