package org.bitman.ay27.PickerWidget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import com.beardedhen.androidbootstrap.FontAwesomeText;

/**
 * Proudly to use Intellij IDEA.
 * Created by ay27 on 14-9-17.
 */
public class SuperFavoriteButton extends FrameLayout {

    private static final int DEFAULT_ICON_SIZE = 10;
    private static final int DEFAULT_TEXT_SIZE = 14;

    public SuperFavoriteButton(Context context) {
        super(context);
        initial(null);
    }

    public SuperFavoriteButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        initial(attrs);
    }

    public SuperFavoriteButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initial(attrs);
    }

    @Override
    public void setOnClickListener(OnClickListener l) {
        super.setOnClickListener(l);
        mOnClickListener = l;
    }

    // here, default will return false, and the click action will be banded.
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return mOnClickListener != null;
    }

    private OnClickListener mOnClickListener;
    private FontAwesomeText icon;
    private TextView text;
    private boolean enable;

    private void initial(AttributeSet attrs) {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.SuperFavoriteButton);

        float scale = getResources().getDisplayMetrics().density;
        int iconSize = (int) (DEFAULT_ICON_SIZE * scale);
        int textSize = (int) (DEFAULT_TEXT_SIZE * scale);
        enable = true;

        if (typedArray.getString(R.styleable.SuperFavoriteButton_icon_size) != null) {
            iconSize = (int) typedArray.getDimension(R.styleable.SuperFavoriteButton_icon_size, 0);
        }
        if (typedArray.getString(R.styleable.SuperFavoriteButton_text_size) != null) {
            textSize = (int) typedArray.getDimension(R.styleable.SuperFavoriteButton_text_size, 0);
        }
        if (typedArray.getString(R.styleable.SuperFavoriteButton_enable) != null) {
            enable = typedArray.getBoolean(R.styleable.SuperFavoriteButton_enable, true);
        }

        iconSize /= scale;
        textSize /= scale;

        typedArray.recycle();

        View v = inflater.inflate(R.layout.favorite_button, null);

        icon = (FontAwesomeText) v.findViewById(R.id._favorite_icon);
        text = (TextView)v.findViewById(R.id._favorite_text);

        icon.setTextSize(TypedValue.COMPLEX_UNIT_SP, iconSize);
        text.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize);

        this.setEnabled(enable);
        // here, default is false.
        this.setClickable(true);
        v.setEnabled(enable);

        addView(v);
    }


    public void setIconSize(int unit, float size) {
        icon.setTextSize(unit, size);
    }
    public void setIconColor(int color) {
        icon.setTextColor(color);
    }
    public void setTextSize(int unit, float size) {
        text.setTextSize(unit, size);
    }
    public void setTextColor(int color) {
        text.setTextColor(color);
    }

    public void setFavorite(boolean favorite) {
        if (favorite) {
            icon.setTextColor(getResources().getColor(R.color.favorite_checked));
            text.setTextColor(getResources().getColor(R.color.favorite_checked));
        } else {
            icon.setTextColor(getResources().getColor(R.color.icon_default));
            text.setTextColor(getResources().getColor(R.color.favorite_button_text_selector));
        }
    }
    public void setFavoriteNum(int num) {
        text.setText(""+num);
    }

}
