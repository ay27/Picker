package org.bitman.ay27.view.templete;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.CursorAdapter;
import android.widget.ListView;
import org.bitman.ay27.PickerWidget.CardsAnimationAdapter;
import org.bitman.ay27.R;
import org.bitman.ay27.data.DataTable;
import org.bitman.ay27.module.BaseModule;
import org.bitman.ay27.request.ListModuleQueryRequest;
import org.bitman.ay27.request.ModuleQueryRequest;
import org.bitman.ay27.swipe_back.app.SwipeBackActivity;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Type;
import java.util.List;

/**
 * Proudly to use Intellij IDEA.
 * Created by ay27 on 14-10-23.
 */
public abstract class   ListSupportActivity<Module extends BaseModule> extends SwipeBackActivity implements AdapterView.OnItemClickListener {

    private CursorSupport cursorSupport;
    private CursorAdapter adapter;
    private Parameters parameters;
    private ListView listView;

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        if (parameters == null) {
            parameters = getChildrenParameters();
            step1SetUpAdapter();
            step2SetUpDB();
        }

        step3QueryModule();
        step4QueryList();
    }

//    @Override
//    protected void onStart() {
//        super.onStart();
//
//
//    }


    private void step1SetUpAdapter() {
        adapter = parameters.getAdapter();
        listView = parameters.getListView();

        View foot = LayoutInflater.from(this).inflate(R.layout.list_end, null);
        listView.addFooterView(foot);

        CardsAnimationAdapter cardsAnimationAdapter = new CardsAnimationAdapter(adapter, listView);
        listView.setAdapter(cardsAnimationAdapter);
        listView.setOnItemClickListener(this);
    }

    protected abstract Parameters getChildrenParameters();

    private void step2SetUpDB() {
        cursorSupport = new CursorSupport(this, this.getLoaderManager(), parameters.getListTable(), adapter);
    }

    protected void step3QueryModule() {

        if (parameters.getModuleQueryUrl() == null)
            return;

        ModuleQueryRequest.RequestFinishedCallback<Module> onModuleQueryFinished = new ModuleQueryRequest.RequestFinishedCallback<Module>() {
            @Override
            public void onFinished(String json, Module result) {
                step5BindViews(result);
            }
        };
        new ModuleQueryRequest(this, parameters.getModuleQueryUrl(), null, parameters.getModuleJsonKey(), parameters.getModuleClass(), onModuleQueryFinished);
    }

    protected void step5BindViews(Module result){

    }

    protected void step4QueryList() {

        cursorSupport.cleanTable();

        ListModuleQueryRequest.ListRequestCallback<? extends BaseModule> _onListQueryFinished = new ListModuleQueryRequest.ListRequestCallback<BaseModule>() {
            @Override
            public void onFinished(List<BaseModule> result) {
                onListQueryFinished(result);
            }
        };
        new ListModuleQueryRequest(this, parameters.getListQueryUrl(), null, parameters.getListJsonKey(), parameters.getTypeOfListModule(), _onListQueryFinished);
    }

    protected void onListQueryFinished(List result) {
        cursorSupport.updateTable(result);
    }

    protected void refresh() {
        step3QueryModule();
        step4QueryList();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            super.onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public class Parameters {
        private ListView listView;
        private CursorAdapter adapter;
        private Type typeOfListModule;
        private String listQueryUrl, listJsonKey;
        private DataTable listTable;

        private String moduleQueryUrl, moduleJsonKey;
        private Class moduleClass;

        public <T extends BaseModule> Parameters(String moduleQueryUrl, String moduleJsonKey, Class<T> moduleClass,
                @NotNull ListView listView, @NotNull Type typeOfListModule, @NotNull CursorAdapter adapter,
                          @NotNull DataTable table, @NotNull String listQueryUrl, @NotNull String listJsonKey) {
            this.moduleQueryUrl = moduleQueryUrl;
            this.moduleJsonKey = moduleJsonKey;
            this.moduleClass = moduleClass;
            this.listView = listView;
            this.adapter = adapter;
            this.typeOfListModule = typeOfListModule;
            this.listQueryUrl = listQueryUrl;
            this.listJsonKey = listJsonKey;
            this.listTable = table;
        }

        public Class getModuleClass() {
            return moduleClass;
        }

        public String getModuleQueryUrl() {
            return moduleQueryUrl;
        }

        public String getModuleJsonKey() {
            return moduleJsonKey;
        }

        public String getListQueryUrl() {
            return listQueryUrl;
        }

        public String getListJsonKey() {
            return listJsonKey;
        }

        public DataTable getListTable() {
            return listTable;
        }

        public ListView getListView() {
            return listView;
        }

        public CursorAdapter getAdapter() {
            return adapter;
        }

        public Type getTypeOfListModule() {
            return typeOfListModule;
        }
    }
}
