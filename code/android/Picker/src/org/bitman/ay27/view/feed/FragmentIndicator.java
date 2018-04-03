package org.bitman.ay27.view.feed;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

/**
 * Proudly to use Intellij IDEA.
 * Created by ay27 on 14/12/18.
 */
public class FragmentIndicator extends View {

    public FragmentIndicator(Context context) {
        super(context);
    }

    public FragmentIndicator(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FragmentIndicator(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    private static Paint paint;
    static  {
        paint = new Paint();
        paint.setStrokeWidth(18);
        paint.setColor(Color.WHITE);
    }

    private int pageNum;

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawLine(start, 0, start+length, 0, paint);
    }

    private int start;
    private int length = -1;

    public void draw(int page, int start) {
        this.start = (page)*length+start/pageNum;
        this.invalidate();
    }

    public void setLength(int length) {
        this.length = length;
    }

    public void setPageNum(int pageNum) {
        this.pageNum = pageNum;
        if (length == -1)
            length = getResources().getDisplayMetrics().widthPixels / pageNum;
    }

}
