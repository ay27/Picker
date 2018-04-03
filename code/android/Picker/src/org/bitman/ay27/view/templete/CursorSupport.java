package org.bitman.ay27.view.templete;

import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.widget.CursorAdapter;
import org.bitman.ay27.common.TaskUtils;
import org.bitman.ay27.data.DataHelper;
import org.bitman.ay27.data.DataTable;
import org.bitman.ay27.module.BaseModule;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Created by ay27 on 14-10-22.
 */
public class CursorSupport {

    private DataTable table;
    private DataHelper helper;
    private Context context;
    private boolean flag = false;

    public CursorSupport(Context context, LoaderManager loaderManager, DataTable table, CursorAdapter adapter) {
        this.table = table;
        this.context = context;

        helper = new DataHelper(context, context.getContentResolver());
        loaderManager.initLoader(0, null, new LoaderCallback_Version1(context, table, adapter));
    }

    public CursorSupport(Context context, android.app.LoaderManager loaderManager, DataTable table, CursorAdapter adapter) {
        this.table = table;
        this.context = context;

        helper = new DataHelper(context, context.getContentResolver());
        loaderManager.initLoader(0, null, new LoaderCallback_Version2(context, table, adapter));
    }

    public void cleanTable() {
        TaskUtils.executeAsyncTask(new TableCleanTask(table, helper));
    }

    public void insert(List result) {
        TaskUtils.executeAsyncTask(new TableInsertTask(table, result, helper));
    }

    public void updateTable(List newData) {
        TaskUtils.executeAsyncTask(new TableUpdateTask(table, newData, helper));
    }

    private class TableCleanTask extends AsyncTask {
        private DataTable table;
        private DataHelper helper;

        private TableCleanTask(DataTable table, DataHelper helper) {
            this.table = table;
            this.helper = helper;
        }

        @Override
        protected Object doInBackground(Object[] params) {
            helper.deleteAll(DataTable.getContentUri(table));
            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);

            flag = true;
        }
    }

    private class TableInsertTask extends AsyncTask {
        private DataTable table;
        private List newDatas;
        private DataHelper helper;

        public <T extends BaseModule> TableInsertTask(DataTable table, List<T> newDatas, DataHelper helper) {
            this.table = table;
            this.newDatas = newDatas;
            this.helper = helper;
        }

        @Override
        protected Object doInBackground(Object[] params) {
            helper.bulkInsert(DataTable.getContentUri(table), newDatas);
            return null;
        }
    }

    private class TableUpdateTask extends AsyncTask {

        private DataTable table;
        private List newDatas;
        private DataHelper helper;

        public <T extends BaseModule> TableUpdateTask(DataTable table, List<T> newDatas, DataHelper helper) {
            this.table = table;
            this.newDatas = newDatas;
            this.helper = helper;
        }

        @Override
        protected Object doInBackground(Object[] params) {

            if (!flag)
                helper.deleteAll(DataTable.getContentUri(table));

            helper.bulkInsert(DataTable.getContentUri(table), newDatas);
            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);

            flag = false;
        }
    }


    private class LoaderCallback_Version1 implements LoaderManager.LoaderCallbacks<Cursor> {

        private Context context;
        private DataTable table;
        private CursorAdapter adapter;

        public LoaderCallback_Version1(@NotNull Context context, @NotNull DataTable table, @NotNull CursorAdapter adapter) {
            this.context = context;
            this.table = table;
            this.adapter = adapter;
        }

        @Override
        public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
            return new CursorLoader(context, DataTable.getContentUri(table), null, null, null, null);
//            return new CursorLoader(context, DataTable.getContentUri(table), null, null, null, DataTable.KEY_ID+" desc");
        }

        @Override
        public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
            adapter.swapCursor(cursor);
        }

        @Override
        public void onLoaderReset(Loader loader) {
            adapter.changeCursor(null);
        }
    }

    private class LoaderCallback_Version2 implements android.app.LoaderManager.LoaderCallbacks<Cursor> {
        private Context context;
        private DataTable table;
        private CursorAdapter adapter;

        public LoaderCallback_Version2(@NotNull Context context, @NotNull DataTable table, @NotNull CursorAdapter adapter) {
            this.context = context;
            this.table = table;
            this.adapter = adapter;
        }

        @Override
        public android.content.Loader<Cursor> onCreateLoader(int id, Bundle args) {
            return new android.content.CursorLoader(context, DataTable.getContentUri(table), null, null, null, null);
//            return new android.content.CursorLoader(context, DataTable.getContentUri(table), null, null, null, DataTable.KEY_ID+" desc");
        }

        @Override
        public void onLoadFinished(android.content.Loader<Cursor> loader, Cursor data) {
            adapter.swapCursor(data);
        }

        @Override
        public void onLoaderReset(android.content.Loader<Cursor> loader) {
            adapter.changeCursor(null);
        }
    }

}
