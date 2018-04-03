package org.bitman.ay27.PickerWidget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

import java.text.DecimalFormat;

/**
 * Proudly to use Intellij IDEA.
 * Created by ay27 on 14-9-17.
 */
public class NumberAdjustableTextView extends TextView {
    public NumberAdjustableTextView(Context context) {
        super(context);
    }

    public NumberAdjustableTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public NumberAdjustableTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public void setText(CharSequence text, BufferType type) {
        String str = text.toString();
        if (text.length()>=4) {
            DecimalFormat format = new DecimalFormat("##.#");
            int data = Integer.parseInt(text.toString());
            if (data>=1000000) {
                str = format.format((double)data/1000000.0) +"m";
            }
            else if (data>=10000) {
                str = format.format((double)data/10000.0) +"w";
            } else if (data>=1000) {
                str = format.format((double)data/1000.0) +"k";
            }
        }
        super.setText(str, type);
    }
}
