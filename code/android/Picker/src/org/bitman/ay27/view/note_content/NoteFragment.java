package org.bitman.ay27.view.note_content;

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
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import butterknife.ButterKnife;
import butterknife.InjectView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.bitman.ay27.PickerWidget.AdjustableListView;
import org.bitman.ay27.PickerWidget.BookPageIndicator;
import org.bitman.ay27.PickerWidget.CardsAnimationAdapter;
import org.bitman.ay27.PickerWidget.markdown_support.MarkdownView;
import org.bitman.ay27.R;
import org.bitman.ay27.common.ContentType;
import org.bitman.ay27.common.TaskUtils;
import org.bitman.ay27.common.ToastUtils;
import org.bitman.ay27.data.DataHelper;
import org.bitman.ay27.data.DataTable;
import org.bitman.ay27.module.dp.CommentDP;
import org.bitman.ay27.module.dp.FeedDp;
import org.bitman.ay27.request.ListModuleQueryRequest;
import org.bitman.ay27.request.ModuleQueryRequest;
import org.bitman.ay27.request.UrlGenerator;
import org.bitman.ay27.request.Urls;
import org.bitman.ay27.view.adapter.CommentAdapter;
import org.bitman.ay27.view.widget.FavoriteButton;
import org.bitman.ay27.view.widget.FollowButton;

import java.util.List;
import java.util.concurrent.Semaphore;

/**
 * Proudly to use Intellij IDEA.
 * Created by ay27 on 15/1/2.
 */
public class NoteFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>, AdapterView.OnItemClickListener {

    @InjectView(R.id.note_content_activity_book_indicator)
    BookPageIndicator indicator;
    @InjectView(R.id.note_content_activity_header)
    TextView header;
    @InjectView(R.id.note_content_activity_content)
    MarkdownView noteContent;
    @InjectView(R.id.note_content_activity_favorite)
    FavoriteButton favoriteButton;
    @InjectView(R.id.follow_button)
    FollowButton followButton;
    @InjectView(R.id.note_content_activity_comment_list)
    AdjustableListView commentList;
    @InjectView(R.id.nothing)
    View nothingView;
    @InjectView(R.id.note_content_activity_editText)
    EditText commentEdit;
    @InjectView(R.id.note_content_activity_send_button)
    Button sendButton;
//    @InjectView(R.id.note_content_activity_toolbar)
//    Toolbar toolbar;


    private CommentAdapter adapter;
    private FeedDp note;
    private long noteID;
    private DataHelper helper;
    private String bookName;

    private Semaphore commentDPTableReady = new Semaphore(0);

    public NoteFragment(long noteID, String bookName) {
        this.noteID = noteID;
        this.bookName = bookName;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.note_content_activity, null);
        ButterKnife.inject(this, view);

        adapter = new CommentAdapter(getActivity(), null, false);

        helper = new DataHelper(getActivity(), getActivity().getContentResolver());
        getLoaderManager().initLoader(0, null, this);

        initViews();

        doQueryNote();
        doQueryCommentList();

        return view;
    }

    private void initViews() {
        CardsAnimationAdapter cardsAnimationAdapter = new CardsAnimationAdapter(adapter, commentList);
        commentList.setAdapter(cardsAnimationAdapter);
        commentList.setOnItemClickListener(this);
        nothingView.setVisibility(View.GONE);
    }

    private void doQueryNote() {
        Cursor cursor = helper.queryOne(DataTable.getContentUri(DataTable.note), DataTable.KEY_ID, noteID);
        if (cursor == null || cursor.getCount() == 0) {
            String url = UrlGenerator.queryNote(noteID);
            ModuleQueryRequest.RequestFinishedCallback<FeedDp> requestFinished = new ModuleQueryRequest.RequestFinishedCallback<FeedDp>() {
                @Override
                public void onFinished(final String json, final FeedDp result) {
                    TaskUtils.executeAsyncTask(new AsyncTask<Object, Object, Object>() {
                        @Override
                        protected Object doInBackground(Object... params) {
                            helper.insert(DataTable.getContentUri(DataTable.note), result);
                            return null;
                        }
                    });
                    note = result;
                    setViewsAfterNoteReady();
                }
            };
            new ModuleQueryRequest(getActivity(), url, null, Urls.KEY_NOTE, FeedDp.class, requestFinished);
        } else {
            cursor.moveToNext();
            note = new Gson().fromJson(cursor.getString(cursor.getColumnIndex(DataTable.KEY_JSON)), FeedDp.class);
            setViewsAfterNoteReady();
        }
    }

    private void setViewsAfterNoteReady() {

        indicator.setText(bookName, note.getPage());
        favoriteButton.setFavoriteModule(note, ContentType.Note);
//        followButton.setFollowModule(note, ContentType.Note);
        header.setText(note.getTitle());
        noteContent.loadMarkdown(note.getContent());

        if (note.getCommentNum() <= 0) {
            commentList.setVisibility(View.GONE);
            nothingView.setVisibility(View.VISIBLE);
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

        String url = UrlGenerator.queryCommentDPList_Note(noteID);
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

    // comment list
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        ToastUtils.showSuccess(getActivity(), " click on " + position);
    }
}

