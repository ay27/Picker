package org.bitman.ay27.PickerWidget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

/**
 * Proudly to use Intellij IDEA.
 * Created by ay27 on 14-9-18.
 */
public class SeparatorBar extends FrameLayout {
    public SeparatorBar(Context context) {
        super(context);
        initial(null);
    }

    public SeparatorBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        initial(attrs);
    }

    public SeparatorBar(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initial(attrs);
    }

    private void initial(AttributeSet attrs) {
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.SeparatorBar);
        String text = null;
        if (typedArray.getString(R.styleable.SeparatorBar_text) !=null) {
            text = typedArray.getString(R.styleable.SeparatorBar_text);
        }
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View view;
        if (text==null||text.equals("")) {
            view = inflater.inflate(R.layout.without_text, null);
        }
        else {
            view = inflater.inflate(R.layout.withtext, null);
            ((TextView)view.findViewById(R.id._separator_text)).setText(text);
        }

        addView(view);
    }
}
