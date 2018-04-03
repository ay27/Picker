package org.bitman.ay27.view.feed;

/**
 * Proudly to use Intellij IDEA.
 * Created by ay27 on 14/12/23.
 */

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import butterknife.ButterKnife;
import butterknife.InjectView;
import com.google.gson.reflect.TypeToken;
import com.hb.views.PinnedSectionListView;
import org.bitman.ay27.PickerWidget.FloatingButton;
import org.bitman.ay27.R;
import org.bitman.ay27.common.ContentType;
import org.bitman.ay27.module.Section;
import org.bitman.ay27.module.dp.FeedDp;
import org.bitman.ay27.request.ListModuleQueryRequest;
import org.bitman.ay27.request.UrlGenerator;
import org.bitman.ay27.request.Urls;
import org.bitman.ay27.view.question_content.QuestionContentActivity;
import org.bitman.ay27.view.writer.WriteActivity;

import java.util.List;

public class QuestionFragmentInChapterLine extends Fragment implements SwipeRefreshLayout.OnRefreshListener, AdapterView.OnItemClickListener, ProvideList {

    private final long bookID;
    private final String bookName;
    @InjectView(R.id.question_fragment_list)
    PinnedSectionListView listView;
    @InjectView(R.id.question_fragment_swipe)
    SwipeRefreshLayout swipeLayout;

    FloatingButton floatingButton;


    private QuestionAdapterWithPinnedSection adapter;
    private View.OnClickListener onFloatCLickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(getActivity(), WriteActivity.class);
            intent.putExtra("Type", ContentType.Question);
            intent.putExtra("BookID", bookID);
            startActivity(intent);
        }
    };
    private List<Section> sectionList;

    public QuestionFragmentInChapterLine(final long bookID, final String bookName, final FloatingButton button) {
        super();
        this.bookID = bookID;
        this.bookName = bookName;
        this.floatingButton = button;
    }

    //    @OnClick(R.id.question_fragment_float_button)
    public void addClick(View view) {

        Intent intent = new Intent(getActivity(), WriteActivity.class);
        intent.putExtra("Type", ContentType.Question);
        intent.putExtra("BookID", bookID);
        startActivity(intent);

    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.question_fragment, null);
        ButterKnife.inject(this, root);

        swipeLayout.setColorScheme(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light, android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
        swipeLayout.setOnRefreshListener(this);

        floatingButton.attachToListView(listView);

        listView.setOnItemClickListener(QuestionFragmentInChapterLine.this);
        listView.addFooterView(inflater.inflate(R.layout.list_end, null));


        doQuerySectionList();

        return root;
    }

    private void doQuerySectionList() {

        String url = UrlGenerator.queryBookSectionList(bookID);
        ListModuleQueryRequest.ListRequestCallback<Section> requestFinish = new ListModuleQueryRequest.ListRequestCallback<Section>() {
            @Override
            public void onFinished(List<Section> result) {
                sectionList = result;
                doQueryQuestionList();
            }
        };
        new ListModuleQueryRequest(getActivity(), url, null, Urls.KEY_SECTION_LIST, new TypeToken<List<Section>>() {
        }.getType(), requestFinish);

    }

    private void doQueryQuestionList() {
        ListModuleQueryRequest.ListRequestCallback<FeedDp> requestFinished = new ListModuleQueryRequest.ListRequestCallback<FeedDp>() {
            @Override
            public void onFinished(final List<FeedDp> result) {
                adapter = new QuestionAdapterWithPinnedSection(QuestionFragmentInChapterLine.this.getActivity(), result, sectionList);
                listView.setAdapter(adapter);
                if (swipeLayout.isRefreshing())
                    swipeLayout.setRefreshing(false);

            }
        };


        String url = UrlGenerator.queryQuestionListInPage(bookID);
        new ListModuleQueryRequest(getActivity(), url, null, Urls.KEY_QUESTION_LIST, new TypeToken<List<FeedDp>>() {
        }.getType(), requestFinished);
    }


    @Override
    public void onRefresh() {
        doQueryQuestionList();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        QuestionAdapterWithPinnedSection.ViewHolder holder = (QuestionAdapterWithPinnedSection.ViewHolder) view.getTag();
        if (holder == null)
            return;
        Intent intent = new Intent(getActivity(), QuestionContentActivity.class);
        intent.putExtra("QuestionID", holder.questionID);
        intent.putExtra("BookName", bookName);
        intent.putExtra("BookID", bookID);
        startActivity(intent);
    }

    @Override
    public AbsListView getListView() {
        return listView;
    }

    public void fastScroll(int startPage, int endPage) {
        for (int i = 0; i < adapter.getCount(); i++) {
            QuestionAdapterWithPinnedSection.Item tmp = adapter.getItem(i);
            if (tmp.type == QuestionAdapterWithPinnedSection.Item.SECTION && tmp.startPage == startPage && tmp.endPage == endPage) {
                listView.setSelection(i);
                return;
            }
        }
    }
}
