package org.bitman.ay27.view.writer;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.*;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.*;
import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import com.beardedhen.androidbootstrap.BootstrapButton;
import com.google.gson.Gson;
import org.bitman.ay27.R;
import org.bitman.ay27.common.ContentType;
import org.bitman.ay27.common.ToastUtils;
import org.bitman.ay27.common.Utils;
import org.bitman.ay27.data.DataTable;
import org.bitman.ay27.module.Book;
import org.bitman.ay27.view.writer.page_detector.PageDetectorActivity;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by ay27 on 14/11/17.
 */
public class ChooseBookPageFragment extends Fragment implements TextView.OnEditorActionListener {

    private static final int ACTION_CAPTURE_PAGE = 0x89;
    @InjectView(R.id.choose_book_page_spinner)
    Spinner spinner;
    @InjectView(R.id.choose_book_page_pageNum)
    EditText pageNum;
    @InjectView(R.id.choose_book_page_capture_page)
    BootstrapButton capturePageButton;
    @InjectView(R.id.choose_book_page_next)
    Button nextButton;

    private ChooseBookPageCallback callback;
    private long bookId;
    private ContentType type;
    private ArrayList<Book> books;

    public ChooseBookPageFragment(long bookId, ContentType type, ChooseBookPageCallback callback) {
        this.bookId = bookId;
        this.type = type;
        this.callback = callback;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.write_choose_book_page, null);
        ButterKnife.inject(this, view);

        initViews();

        setHasOptionsMenu(true);

        return view;
    }

    private void initViews() {

        doQueryBooks();
        int targetBookIndex = 0;
        String[] strings = new String[books.size()];
        for (int i = 0; i < books.size(); i++) {
            strings[i] = books.get(i).getName();
            if (books.get(i).getId() == bookId)
                targetBookIndex = i;
        }

        spinner.setAdapter(new ArrayAdapter<String>(getActivity(), R.layout.spinner_item, R.id.spinner_item_text, strings));
        spinner.setSelection(targetBookIndex);

        pageNum.setOnEditorActionListener(this);
    }

    private void doQueryBooks() {
        ContentResolver cr = getActivity().getContentResolver();
        Cursor cursor = cr.query(DataTable.getContentUri(DataTable.book), null, null, null, null);
        books = new ArrayList<Book>();
        if (!cursor.isFirst())
            cursor.moveToFirst();
        while (!cursor.isLast()) {
            books.add(new Gson().fromJson(cursor.getString(cursor.getColumnIndex(DataTable.KEY_JSON)), Book.class));
            cursor.moveToNext();
        }
        books.add(new Gson().fromJson(cursor.getString(cursor.getColumnIndex(DataTable.KEY_JSON)), Book.class));
        cursor.close();
    }

    @OnClick(R.id.choose_book_page_capture_page)
    public void capturePage(View view) {
        Intent intent = new Intent(getActivity(), PageDetectorActivity.class);
        startActivityForResult(intent, ACTION_CAPTURE_PAGE);
    }

    @OnClick(R.id.choose_book_page_next)
    public void next(View view) {
        int page = (pageNum.getText() == null || Utils.isNull(pageNum.getText().toString())) ? 0 : Integer.parseInt(pageNum.getText().toString());
        if (page < 0) {
            ToastUtils.showError(getActivity(), getActivity().getString(R.string.choose_book_page_error));
        }
        String bookName = books.get(spinner.getSelectedItemPosition()).getName();
        long chooseBookId = books.get(spinner.getSelectedItemPosition()).getId();

        callback.onResult(chooseBookId, bookName, page);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == ACTION_CAPTURE_PAGE && resultCode == Activity.RESULT_OK) {
            String page = data.getStringExtra("PageNum");
            pageNum.setText(page);
            next(nextButton);
        }
    }


    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if(actionId == EditorInfo.IME_ACTION_NEXT){
				/*隐藏软键盘*/
            InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            if(inputMethodManager.isActive()){
                inputMethodManager.hideSoftInputFromWindow(v.getApplicationWindowToken(), 0);
            }
            next(nextButton);
            return true;
        }
        return false;
    }

    public static interface ChooseBookPageCallback {
        public void onResult(long bookId, String bookName, int bookPage);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.write, menu);
        menu.getItem(0).setTitle("next");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.write_commit) {
            next(null);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
