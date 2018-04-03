package org.bitman.ay27.view.templete;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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
import java.util.Arrays;
import java.util.List;

/**
 * Created by ay27 on 14-10-20.
 */
public abstract class ListSupportFragment extends Fragment implements AdapterView.OnItemClickListener, ListSupport {

    private Parameters parameters;
    private ListView listView;
    private CursorAdapter adapter;

    private CursorSupport cursorSupport;


    @Override
    public final View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = onStep1SetUpListView(inflater, container);
        onStep2SetUpAdapter();
        onStep3SetUpDBSupport();
        onStep4ListQuery();
        return view;
    }

//    @Override
//    public final void onViewCreated(View view, Bundle savedInstanceState) {
//        super.onViewCreated(view, savedInstanceState);
//
//    }
//
//    @Override
//    public final void onStart() {
//        super.onStart();
//
//    }

    public Context getContext() {
        return getActivity();
    }

    public void setListView(ListView listView) {
        this.listView = listView;
    }

    public ListView getListView() {
        return listView;
    }

    public void setAdapter(CursorAdapter adapter) {
        this.adapter = adapter;
    }

    @Override
    public View onStep1SetUpListView(LayoutInflater inflater, ViewGroup container) {
        parameters = getChildParameters();

        View view = (parameters.getView() == null) ? inflater.inflate(R.layout.list_with_divider, container, false) : parameters.getView();
        listView = (parameters.getListView() == null) ? (ListView) view.findViewById(R.id.list_with_divider_list) : parameters.getListView();
        return view;

    }

    @Override
    public void onStep2SetUpAdapter() {
        adapter = parameters.getAdapter();
        View foot = LayoutInflater.from(getContext()).inflate(R.layout.list_end, null);
        foot.setEnabled(false);
        foot.setClickable(false);
        listView.addFooterView(foot);
        CardsAnimationAdapter cardsAnimationAdapter = new CardsAnimationAdapter(adapter, listView);
        listView.setAdapter(cardsAnimationAdapter);
        listView.setOnItemClickListener(this);
    }

    @Override
    public void onStep3SetUpDBSupport() {
        cursorSupport = new CursorSupport(getActivity(), getLoaderManager(), parameters.getTable(), adapter);
    }

    @Override
    public void onStep4ListQuery() {

        cursorSupport.cleanTable();

        ListModuleQueryRequest.ListRequestCallback<? extends BaseModule> onNetQueryFinished = new ListModuleQueryRequest.ListRequestCallback<BaseModule>() {
            @Override
            public void onFinished(List<BaseModule> result) {
                onStep5ListQueryFinished(result);
            }
        };
        new ListModuleQueryRequest(getActivity(), parameters.getListQueryUrl(), null, parameters.getListJsonKey(), parameters.getTypeOfListModule(), onNetQueryFinished);
    }

    @Override
    public void onStep5ListQueryFinished(List result) {
        cursorSupport.updateTable(result);
        onStep6BindViews();
    }

    @Override
    public void onStep6BindViews() {

    }


    protected abstract Parameters getChildParameters();


    public class Parameters {
        private ListView listView;
        private CursorAdapter adapter;
        private View view;
        private Type typeOfListModule;
        private String listQueryUrl, listJsonKey;
        private DataTable table;

        public Parameters(View view, ListView listView, @NotNull Type typeOfListModule, @NotNull CursorAdapter adapter,
                          @NotNull DataTable table, @NotNull String listQueryUrl, @NotNull String listJsonKey) {
            this.listView = listView;
            this.adapter = adapter;
            this.view = view;
            this.typeOfListModule = typeOfListModule;
            this.listQueryUrl = listQueryUrl;
            this.listJsonKey = listJsonKey;
            this.table = table;
        }

        public String getListQueryUrl() {
            return listQueryUrl;
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
