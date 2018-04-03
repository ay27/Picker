package org.bitman.ay27.view.templete;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.CursorAdapter;
import android.widget.ListView;
import org.bitman.ay27.PickerWidget.CardsAnimationAdapter;
import org.bitman.ay27.R;
import org.bitman.ay27.data.DataTable;
import org.bitman.ay27.module.BaseModule;
import org.bitman.ay27.request.ListModuleQueryRequest;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Type;
import java.util.List;

/**
 * Proudly to use Intellij IDEA.
 * Created by ay27 on 14/12/3.
 */
public abstract class ListPartialLoadFragment extends Fragment implements AdapterView.OnItemClickListener {

    private Parameters parameters;
    private ListView listView;
    private CursorAdapter adapter;
    private CursorSupport cursorSupport;
    private View footer, footerEnd;
    private boolean loadEnd = false;

    private SwipeRefreshLayout swipeLayout;
    private SwipeRefreshLayout.OnRefreshListener onRefreshListener = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            onStep4ListQuery();
        }
    };
    private AbsListView.OnScrollListener onScrollListener = new AbsListView.OnScrollListener() {

        private int lastItemIndex;

        @Override
        public void onScrollStateChanged(AbsListView view, int scrollState) {
            if (scrollState == SCROLL_STATE_IDLE && lastItemIndex == adapter.getCount() - 1 && !loadEnd) {
                listView.addFooterView(footer);
                loadMore();
            }
        }

        @Override
        public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
            lastItemIndex = firstVisibleItem + visibleItemCount - 1;
        }
    };

    private void loadMore() {
        String url = parameters.getListQueryUrl();
        ListModuleQueryRequest.ListRequestCallback<? extends BaseModule> loadFinished = new ListModuleQueryRequest.ListRequestCallback<BaseModule>() {
            @Override
            public void onFinished(List<BaseModule> result) {
                onStep5ListQueryFinished(result);
            }
        };
        new ListModuleQueryRequest(getActivity(), url, null, parameters.getListJsonKey(), parameters.getTypeOfListModule(), loadFinished);
    }


    @Override
    public final View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = onStep1SetUpListView(inflater, container);
        onStep2SetUpAdapter();
        onStep3SetUpDBSupport();
        onStep4ListQuery();
        return view;
    }


    public Context getContext() {
        return getActivity();
    }

    public ListView getListView() {
        return listView;
    }

    public void setListView(ListView listView) {
        this.listView = listView;
    }

    public void setAdapter(CursorAdapter adapter) {
        this.adapter = adapter;
    }

    public View onStep1SetUpListView(LayoutInflater inflater, ViewGroup container) {
        parameters = getChildParameters();

        View view = (parameters.getView() == null) ? inflater.inflate(R.layout.swipe_list, container, false) : parameters.getView();
        setListView(parameters.getListView() == null ? (ListView) view.findViewById(R.id.swipe_list_list) : parameters.getListView());

        footer = inflater.inflate(R.layout.footer, null);
        footerEnd = inflater.inflate(R.layout.list_end, null);

        swipeLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_list_swipe);
        swipeLayout.setColorScheme(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light, android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
        swipeLayout.setOnRefreshListener(onRefreshListener);
        return view;

    }

    public void onStep2SetUpAdapter() {
        adapter = parameters.getAdapter();
        CardsAnimationAdapter cardsAnimationAdapter = new CardsAnimationAdapter(adapter, listView);
        listView.setAdapter(cardsAnimationAdapter);
        listView.setOnItemClickListener(this);
        listView.setOnScrollListener(onScrollListener);
    }

    public void onStep3SetUpDBSupport() {
        cursorSupport = new CursorSupport(getActivity(), getLoaderManager(), parameters.getTable(), adapter);
    }

    public void onStep4ListQuery() {

        cursorSupport.cleanTable();
        parameters.clearPage();
        loadEnd = false;
        if (listView.getFooterViewsCount() > 0) {
            listView.removeFooterView(footer);
            listView.removeFooterView(footerEnd);
        }

        ListModuleQueryRequest.ListRequestCallback<? extends BaseModule> onNetQueryFinished = new ListModuleQueryRequest.ListRequestCallback<BaseModule>() {
            @Override
            public void onFinished(List<BaseModule> result) {
                onStep5ListQueryFinished(result);
            }
        };
        new ListModuleQueryRequest(getActivity(), parameters.getListQueryUrl(), null, parameters.getListJsonKey(), parameters.getTypeOfListModule(), onNetQueryFinished);
    }

    public void onStep5ListQueryFinished(List result) {
        parameters.addPage();
        cursorSupport.insert(result);

        if (listView.getFooterViewsCount() > 0)
            listView.removeFooterView(footer);
        if (result.isEmpty() || result.size()<Parameters.EVERY_PAGE) {
            loadEnd = true;
            listView.addFooterView(footerEnd);
        }

        onStep6BindViews();

        if (swipeLayout.isRefreshing())
            swipeLayout.setRefreshing(false);
    }

    public void onStep6BindViews() {

    }


    protected abstract Parameters getChildParameters();


    public class Parameters {
        public static final int EVERY_PAGE = 10;
        private ListView listView;
        private CursorAdapter adapter;
        private View view;
        private Type typeOfListModule;
        private String listQueryUrl, listJsonKey;
        private DataTable table;
        private int currentPage;

        public Parameters(View view, ListView listView, @NotNull Type typeOfListModule, @NotNull CursorAdapter adapter,
                          @NotNull DataTable table, @NotNull String listQueryUrl, @NotNull String listJsonKey) {
            this.listView = listView;
            this.adapter = adapter;
            this.view = view;
            this.typeOfListModule = typeOfListModule;
            this.listQueryUrl = listQueryUrl;
            this.listJsonKey = listJsonKey;
            this.table = table;
            currentPage = 0;
        }

        public int getCurrentPage() {
            return currentPage;
        }

        public void clearPage() {
            currentPage = 0;
        }

        public void addPage() {
            currentPage++;
        }

        public String getListQueryUrl() {
            return listQueryUrl + currentPage;
        }

        public String getListJsonKey() {
            return listJsonKey;
        }

        public DataTable getTable() {
            return table;
        }

        public ListView getListView() {
            return listView;
        }

        public CursorAdapter getAdapter() {
            return adapter;
        }

        public View getView() {
            return view;
        }

        public Type getTypeOfListModule() {
            return typeOfListModule;
        }
    }


}
