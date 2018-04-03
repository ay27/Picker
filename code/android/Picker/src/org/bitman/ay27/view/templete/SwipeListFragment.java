package org.bitman.ay27.view.templete;

import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import org.bitman.ay27.R;

import java.util.List;

/**
 * Proudly to use Intellij IDEA.
 * Created by ay27 on 14-10-6.
 */
public abstract class SwipeListFragment extends ListSupportFragment {

    private SwipeRefreshLayout swipeLayout;
    private SwipeRefreshLayout.OnRefreshListener onRefreshListener = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            onStep4ListQuery();
        }
    };

    @Override
    public View onStep1SetUpListView(LayoutInflater inflater, ViewGroup container) {
        super.onStep1SetUpListView(inflater, container);

        Parameters parameters = getChildParameters();

        View view = (parameters.getView() == null) ? inflater.inflate(R.layout.swipe_list, container, false) : parameters.getView();
        setListView(parameters.getListView() == null ? (ListView) view.findViewById(R.id.swipe_list_list) : parameters.getListView());

        swipeLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_list_swipe);
        swipeLayout.setColorScheme(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light, android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
        swipeLayout.setOnRefreshListener(onRefreshListener);

        return view;
    }

    @Override
    public void onStep5ListQueryFinished(List result) {
        super.onStep5ListQueryFinished(result);
        if (swipeLayout.isRefreshing())
            swipeLayout.setRefreshing(false);
    }


}
