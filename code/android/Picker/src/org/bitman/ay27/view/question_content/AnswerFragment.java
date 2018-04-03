package org.bitman.ay27.view.question_content;

import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import butterknife.ButterKnife;
import butterknife.InjectView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.bitman.ay27.PickerWidget.AdjustableListView;
import org.bitman.ay27.PickerWidget.BookPageIndicator;
import org.bitman.ay27.PickerWidget.CardsAnimationAdapter;
import org.bitman.ay27.PickerWidget.markdown_support.MarkdownView;
import org.bitman.ay27.R;
import org.bitman.ay27.cache.ImageCacheManager;
import org.bitman.ay27.common.ContentType;
import org.bitman.ay27.common.TaskUtils;
import org.bitman.ay27.common.ToastUtils;
import org.bitman.ay27.data.DataHelper;
import org.bitman.ay27.data.DataTable;
import org.bitman.ay27.module.dp.AnswerDP;
import org.bitman.ay27.module.dp.CommentDP;
import org.bitman.ay27.request.ListModuleQueryRequest;
import org.bitman.ay27.request.ModuleQueryRequest;
import org.bitman.ay27.request.UrlGenerator;
import org.bitman.ay27.request.Urls;
import org.bitman.ay27.view.adapter.CommentAdapter;
import org.bitman.ay27.view.widget.FavoriteButton;

import java.util.List;
import java.util.concurrent.Semaphore;

/**
 * Proudly to use Intellij IDEA.
 * Created by ay27 on 15/1/2.
 */
public class AnswerFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>, AdapterView.OnItemClickListener {

    @InjectView(R.id.answer_content_activity_indicator)
    BookPageIndicator indicator;
    @InjectView(R.id.answer_content_activity_title)
    TextView answerTitle;
    @InjectView(R.id.answer_content_activity_avatar)
    ImageView userAvatar;
    @InjectView(R.id.answer_content_activity_user_name)
    TextView userName;
    @InjectView(R.id.answer_content_activity_favorite)
    FavoriteButton favoriteButton;
    @InjectView(R.id.answer_content_activity_content)
    MarkdownView answerContent;
    @InjectView(R.id.answer_content_activity_time)
    TextView editTime;
    @InjectView(R.id.answer_content_activity_comment_list)
    AdjustableListView commentList;
    @InjectView(R.id.answer_content_activity_editText)
    EditText commentEdit;
    @InjectView(R.id.answer_content_activity_send_button)
    Button sendButton;
    @InjectView(R.id.nothing)
    View nothingView;
//    @InjectView(R.id.answer_content_activity_toolbar)
//    android.support.v7.widget.Toolbar toolbar;

    private long answerID;
    private AnswerDP answerDP;
    private DataHelper helper;
    private CommentAdapter adapter;
    private String bookName;
    private int bookPage;
    private Semaphore commentDPTableReady = new Semaphore(0);

    public AnswerFragment(long answerID, String bookName, int bookPage) {
        this.answerID = answerID;
        this.bookName = bookName;
        this.bookPage = bookPage;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.answer_content_activity, null);
        ButterKnife.inject(this, view);

        adapter = new CommentAdapter(getActivity(), null, false);
        helper = new DataHelper(getActivity(), getActivity().getContentResolver());
        getLoaderManager().initLoader(0, null, this);

        initViews();

        doQueryAnswerInfo();
        doQueryCommentList();

        return view;
    }

    private void initViews() {

        CardsAnimationAdapter cardsAnimationAdapter = new CardsAnimationAdapter(adapter, commentList);
        commentList.setAdapter(cardsAnimationAdapter);
        commentList.setOnItemClickListener(this);

        nothingView.setVisibility(View.GONE);
    }

    private void doQueryAnswerInfo() {
        Cursor cursor = helper.queryOne(DataTable.getContentUri(DataTable.answerDP), DataTable.KEY_ID, answerID);
        if (cursor == null || cursor.getCount() == 0) {

            ModuleQueryRequest.RequestFinishedCallback<AnswerDP> requestFinished = new ModuleQueryRequest.RequestFinishedCallback<AnswerDP>() {
                @Override
                public void onFinished(final String json, AnswerDP result) {
                    TaskUtils.executeAsyncTask(new AsyncTask<Object, Object, Object>() {
                        @Override
                        protected Object doInBackground(Object... params) {
                             helper.insert(DataTable.getContentUri(DataTable.answerDP), json, AnswerDP.class);
                            return null;
                        }
                    });
                    answerDP = result;
                    setViewsAfterAnswerDPReady();
                }
            };

            String url = UrlGenerator.queryAnswer(answerID);
            new ModuleQueryRequest(getActivity(), url, null, Urls.KEY_ANSWER, AnswerDP.class, requestFinished);
        } else {
            cursor.moveToNext();
            answerDP = new Gson().fromJson(cursor.getString(cursor.getColumnIndex(DataTable.KEY_JSON)), AnswerDP.class);
            setViewsAfterAnswerDPReady();
        }
    }

    private void setViewsAfterAnswerDPReady() {
        indicator.setText(bookName, bookPage);
        answerTitle.setText(answerDP.getQuestionName());

        ImageCacheManager.loadImage(UrlGenerator.getResourcesUrl(answerDP.getUserAvatarUrl()),
                ImageCacheManager.getImageListener(userAvatar, null, null));

        userName.setText(answerDP.getReplierName());
        favoriteButton.setFavoriteModule(answerDP, ContentType.Answer);
        answerContent.loadMarkdown(answerDP.getContent());
        editTime.setText("" + answerDP.getCreateTime());

        if (commentList.getCount() <= 0) {
            nothingView.setVisibility(View.VISIBLE);
            commentList.setVisibility(View.GONE);
        }
    }

    private void doQueryCommentList() {
        TaskUtils.executeAsyncTask(new AsyncTask<Object, Object, Object>() {
            @Override
            protected Object doInBackground(Object... params) {
                helper.deleteAll(DataTable.getContentUri(DataTable.commentDP));
                commentDPTableReady.release();
                return null;
            }
        });


        ListModuleQueryRequest.ListRequestCallback<CommentDP> requestFinished = new ListModuleQueryRequest.ListRequestCallback<CommentDP>() {
            @Override
            public void onFinished(final List<CommentDP> result) {
                TaskUtils.executeAsyncTask(new AsyncTask<Object, Object, Object>() {
                    @Override
                    protected Object doInBackground(Object... params) {
                        try {
                            commentDPTableReady.acquire();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        helper.bulkInsert(DataTable.getContentUri(DataTable.commentDP), result);
                        return null;
                    }
                });
            }
        };


        String url = UrlGenerator.queryCommentDPList_Answer(answerID);
        new ListModuleQueryRequest(getActivity(), url, null, Urls.KEY_COMMENT_LIST, new TypeToken<List<CommentDP>>() {
        }.getType(), requestFinished);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(getActivity(), DataTable.getContentUri(DataTable.commentDP), null, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        adapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        adapter.changeCursor(null);
    }


    // for comment list
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        ToastUtils.showSuccess(getActivity(), "click on " + position);
    }

}
