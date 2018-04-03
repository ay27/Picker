package org.bitman.ay27.view.feed;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import butterknife.ButterKnife;
import butterknife.InjectView;
import com.google.gson.reflect.TypeToken;
import org.bitman.ay27.R;
import org.bitman.ay27.module.SearchResult;
import org.bitman.ay27.request.ListModuleQueryRequest;
import org.bitman.ay27.request.UrlGenerator;
import org.bitman.ay27.request.Urls;
import org.bitman.ay27.view.adapter.SearchResultAdapter;
import org.bitman.ay27.view.add_book.BookInfoActivity;
import org.bitman.ay27.view.attachment.AttachmentActivity;
import org.bitman.ay27.view.circle.CircleActivity;
import org.bitman.ay27.view.note_content.NoteContentActivity;
import org.bitman.ay27.view.question_content.QuestionContentActivity;
import org.bitman.ay27.view.user.UserInfoActivity;

import java.util.List;

/**
 * Proudly to use Intellij IDEA.
 * Created by ay27 on 14/12/1.
 */
public class SearchResultFragment extends Fragment implements AdapterView.OnItemClickListener {

    @InjectView(R.id.list_without_divider_list)
    ListView listView;
    private long bookId;
    private String bookName;
    private int bookPage;
    private SearchResultAdapter adapter;


    public SearchResultFragment(long bookId, String bookName, int bookPage) {
        this.bookId = bookId;
        this.bookName = bookName;
        this.bookPage = bookPage;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.list_without_divider, null);
        ButterKnife.inject(this, view);

        listView.setOnItemClickListener(this);
        doSearch();

        return view;
    }

    private void doSearch() {
        String url = UrlGenerator.searchPage(bookId, bookPage);
        ListModuleQueryRequest.ListRequestCallback<SearchResult> searchCallback = new ListModuleQueryRequest.ListRequestCallback<SearchResult>() {
            @Override
            public void onFinished(List<SearchResult> result) {
                listView.setAdapter(adapter = new SearchResultAdapter(getActivity(), result));
            }
        };
        new ListModuleQueryRequest(getActivity(), url, null, Urls.KEY_SEARCH_RESULT,
                new TypeToken<List<SearchResult>>() {
                }.getType(), searchCallback);
    }

    // for result list
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        if (position >= adapter.getCount())
            return;

        SearchResult result = adapter.getItem(position);

        Intent intent = new Intent();
        Class cls;

        switch (result.getType()) {
            case SearchResult.RESULT_USER:
                cls = UserInfoActivity.class;
                intent.putExtra("TargetUserID", result.getId());
                break;
            case SearchResult.RESULT_CIRCLE:
                cls = CircleActivity.class;
                intent.putExtra("TargetCircleID", result.getId());
                break;
            case SearchResult.RESULT_BOOK:
                cls = BookInfoActivity.class;
                intent.putExtra("BookID", result.getId());
                break;
            case SearchResult.RESULT_QUESTION:
                cls = QuestionContentActivity.class;
                intent.putExtra("QuestionID", result.getId());
                break;
            case SearchResult.RESULT_NOTE:
                cls = NoteContentActivity.class;
                intent.putExtra("NoteID", result.getId());
                break;
            case SearchResult.RESULT_AFEED:
                cls = AttachmentActivity.class;
                intent.putExtra("AttachmentID", result.getId());
                break;
            default:
                cls = null;
        }

        intent.setClass(getActivity(), cls);
        startActivity(intent);
    }


}
