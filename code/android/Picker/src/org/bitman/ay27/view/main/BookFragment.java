package org.bitman.ay27.view.main;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.*;
import android.widget.AdapterView;
import android.widget.CursorAdapter;
import android.widget.GridView;
import butterknife.ButterKnife;
import butterknife.InjectView;
import com.google.gson.reflect.TypeToken;
import org.bitman.ay27.PickerApplication;
import org.bitman.ay27.R;
import org.bitman.ay27.common.TaskUtils;
import org.bitman.ay27.common.ToastUtils;
import org.bitman.ay27.data.DataHelper;
import org.bitman.ay27.data.DataTable;
import org.bitman.ay27.module.Book;
import org.bitman.ay27.request.ListModuleQueryRequest;
import org.bitman.ay27.request.PostRequest;
import org.bitman.ay27.request.UrlGenerator;
import org.bitman.ay27.request.Urls;
import org.bitman.ay27.view.adapter.BookAdapter;
import org.bitman.ay27.view.add_book.ScanActivity;
import org.bitman.ay27.view.feed.FeedActivity;

import java.util.List;
import java.util.concurrent.Semaphore;

/**
 * Proudly to use Intellij IDEA.
 * Created by ay27 on 14-7-25.
 */
public class BookFragment extends Fragment implements android.support.v4.app.LoaderManager.LoaderCallbacks<Cursor> {

    @InjectView(R.id.book_manager_gridView)
    GridView gridView;
    private CursorAdapter adapter;
    private DataHelper helper;
    private Semaphore bookTableReady = new Semaphore(0);
    private AdapterView.OnItemClickListener itemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
            Intent intent = new Intent(getActivity(), FeedActivity.class);
            BookAdapter.ViewHolder holder = (BookAdapter.ViewHolder) view.getTag();
            intent.putExtra("BookID", holder.bookID);
            intent.putExtra("BookName", holder.bookNameStr);
            startActivity(intent);
        }
    };

    private AdapterView.OnItemLongClickListener itemLongClickListener = new AdapterView.OnItemLongClickListener() {
        @Override
        public boolean onItemLongClick(AdapterView<?> adapterView, View view, final int position, long id) {

            final long currentBookID = ((BookAdapter.ViewHolder) view.getTag()).bookID;

            // 删除书本的确认按钮
            DialogInterface.OnClickListener removeBookListener = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                    String url = UrlGenerator.removeBook(currentBookID);
                    PostRequest.PostFinishCallback postFinishCallback = new PostRequest.PostFinishCallback() {
                        @Override
                        public void onFinished(Boolean result) {
                            TaskUtils.executeAsyncTask(new AsyncTask<Object, Object, Object>() {
                                @Override
                                protected Object doInBackground(Object... params) {
                                    helper.delete(DataTable.getContentUri(DataTable.book), DataTable.KEY_ID + "=?", new String[]{Long.toString(currentBookID)});
                                    return true;
                                }
                            });
                            ToastUtils.showSuccess(getActivity(), R.string.remove_book_succeed);
                        }
                    };
                    new PostRequest(getActivity(), url, null, postFinishCallback);
                }
            };

            // 一个简单的dialog来展示删除界面
            new AlertDialog.Builder(getActivity())
                    .setTitle(R.string.remove_book_ask)
                    .setPositiveButton(R.string.remove_book_confirm, removeBookListener)
                    .setNegativeButton(R.string.remove_book_cancel, null)
                    .show();
            return true;
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        helper = new DataHelper(getActivity(), getActivity().getContentResolver());
        adapter = new BookAdapter(getActivity(), null, false);
        getLoaderManager().initLoader(0, null, this);

        // 这一句一定不能省，不然menu不能出来
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_book_manager, null);
        ButterKnife.inject(this, root);

//        getActivity().getActionBar().setTitle(getActivity().getString(R.string.book_activity_title));

        gridView.setAdapter(adapter);
        gridView.setOnItemClickListener(itemClickListener);
        gridView.setOnItemLongClickListener(itemLongClickListener);

        doQueryBookList();

        return root;
    }

    private void doQueryBookList() {

        TaskUtils.executeAsyncTask(new AsyncTask<Object, Object, Object>() {
            @Override
            protected Object doInBackground(Object... params) {
                helper.deleteAll(DataTable.getContentUri(DataTable.book));
                bookTableReady.release();
                return true;
            }
        });


        ListModuleQueryRequest.ListRequestCallback<Book> requestFinished = new ListModuleQueryRequest.ListRequestCallback<Book>() {
            @Override
            public void onFinished(final List<Book> result) {
                TaskUtils.executeAsyncTask(new AsyncTask<Object, Object, Object>() {
                    @Override
                    protected Object doInBackground(Object... params) {
                        try {
                            bookTableReady.acquire();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        helper.bulkInsert(DataTable.getContentUri(DataTable.book), result);
                        return true;
                    }
                });
            }
        };
        String url = UrlGenerator.queryBookList(PickerApplication.getMyUserId());
        new ListModuleQueryRequest(getActivity(), url, null, Urls.KEY_BOOK_LIST, new TypeToken<List<Book>>() {
        }.getType(), requestFinished);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return new CursorLoader(getActivity(), DataTable.getContentUri(DataTable.book), null, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> objectLoader, Cursor o) {
        adapter.swapCursor(o);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> objectLoader) {
        adapter.changeCursor(null);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.main, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.add_book_menu_button) {
            Intent intent = new Intent(getActivity(), ScanActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
