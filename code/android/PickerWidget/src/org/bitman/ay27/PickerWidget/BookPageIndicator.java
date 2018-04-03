package org.bitman.ay27.PickerWidget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.TextView;

/**
 * Proudly to use Intellij IDEA.
 * Created by ay27 on 14-9-21.
 */
public class BookPageIndicator extends FrameLayout {
    public BookPageIndicator(Context context) {
        super(context);
        initial(null);
    }

    public BookPageIndicator(Context context, AttributeSet attrs) {
        super(context, attrs);
        initial(attrs);
    }

    public BookPageIndicator(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initial(attrs);
    }

    private TextView textView;
    private void initial(AttributeSet attrs) {
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.BookPageIndicator);
        String bookName = null;
        int bookPage = -1;

        if (typedArray.getString(R.styleable.BookPageIndicator_book_name) != null) {
            bookName = typedArray.getString(R.styleable.BookPageIndicator_book_name);
        }
        if (typedArray.getInteger(R.styleable.BookPageIndicator_book_page, -1) != -1) {
            bookPage = typedArray.getInteger(R.styleable.BookPageIndicator_book_page, -1);
        }

        textView = (TextView) LayoutInflater.from(getContext()).inflate(R.layout.book_page_indicator, null);
        setText(bookName, bookPage);

        addView(textView);
    }

    public void setText(String bookName, int bookPage) {
        String PAGE_SEPARATOR = getContext().getString(R.string.page_separator);
        String PAGE_UNIT = getContext().getString(R.string.page_unit);
        if (bookName==null || bookName.equals(""))
            this.setVisibility(GONE);
        else if (bookPage == -1) {
            textView.setText(PAGE_SEPARATOR + bookName);
            this.setVisibility(VISIBLE);
        }
        else {
            textView.setText(PAGE_SEPARATOR + bookName + PAGE_SEPARATOR + bookPage + PAGE_UNIT);
            this.setVisibility(VISIBLE);
        }
    }
}
