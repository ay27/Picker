package org.bitman.ay27.PickerWidget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

/**
 * Proudly to use Intellij IDEA.
 * Created by ay27 on 14-9-5.
 */
public class AdjustableListView extends ListView {
    public AdjustableListView(Context context) {
        super(context);
    }

    public AdjustableListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public AdjustableListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    //    // 重写测量函数，避免在ScrollView中的测量出错
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
                MeasureSpec.AT_MOST);

        super.onMeasure(widthMeasureSpec, expandSpec);
    }
}
