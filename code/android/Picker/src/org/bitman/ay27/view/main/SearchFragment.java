package org.bitman.ay27.view.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import com.google.gson.reflect.TypeToken;
import org.bitman.ay27.PickerWidget.CardsAnimationAdapter;
import org.bitman.ay27.R;
import org.bitman.ay27.common.ContentType;
import org.bitman.ay27.common.ToastUtils;
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
 * Created by ay27 on 14-10-1.
 */
public class SearchFragment extends Fragment implements AdapterView.OnItemClickListener {

    @InjectView(R.id.search_spinner)
    Spinner spinner;
    @InjectView(R.id.search_input)
    EditText searchInput;
    @InjectView(R.id.search_button)
    Button searchButton;
    @InjectView(R.id.search_list)
    ListView resultList;

    private SearchResultAdapter adapter;
    private String[] spinnerStrings;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.search_fragment, null);
        ButterKnife.inject(this, v);

        spinnerStrings = getActivity().getResources().getStringArray(R.array.search_spinner);
        spinner.setAdapter(new ArrayAdapter<String>(getActivity(), R.layout.spinner_item, R.id.spinner_item_text, spinnerStrings));
        spinner.setSelection(0);

        View view = LayoutInflater.from(getActivity()).inflate(R.layout.list_end, null);
        resultList.addFooterView(view);

        resultList.setOnItemClickListener(this);

        return v;
    }


    @OnClick(R.id.search_button)
    public void onSearchClick(View v) {
        String searchText = searchInput.getText().toString();
        if (searchText == null || searchText.equals("")) {
            ToastUtils.showError(getActivity(), getActivity().getString(R.string.search_failed));
            return;
        }

        String url = UrlGenerator.search(getType(), searchText);

        ListModuleQueryRequest.ListRequestCallback<SearchResult> requestFinished = new ListModuleQueryRequest.ListRequestCallback<SearchResult>() {
            @Override
            public void onFinished(final List<SearchResult> result) {
                adapter = new SearchResultAdapter(getActivity(), result);
                CardsAnimationAdapter cardsAnimationAdapter = new CardsAnimationAdapter(adapter, resultList);
                resultList.setAdapter(cardsAnimationAdapter);
            }
        };

        new ListModuleQueryRequest(getActivity(), url, null, Urls.KEY_SEARCH_RESULT, new TypeToken<List<SearchResult>>() {
        }.getType(),
                requestFinished);
    }


    //    <item>用户</item>
//    <item>圈子</item>
//    <item>书本</item>
//    <item>问题</item>
//    <item>笔记</item>
    private ContentType getType() {
        switch (spinner.getSelectedItemPosition()) {
            case 0:
                return ContentType.User;
            case 1:
                return ContentType.Circle;
            case 2:
                return ContentType.Book;
            case 3:
                return ContentType.Question;
            case 4:
                return ContentType.Note;
            default:
                return null;
        }
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
