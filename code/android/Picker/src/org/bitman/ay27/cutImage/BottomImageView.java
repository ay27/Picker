package org.bitman.ay27.cutImage;

import android.content.Context;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.util.FloatMath;
import android.view.MotionEvent;
import android.widget.ImageView;

/**
 * Proudly to use Intellij IDEA.
 * Created by ay27 on 14-7-5.
 */
public class BottomImageView extends ImageView {

    private static final int DRAG = 1;//拖动
    private static final int ZOOM = 2;//放大
    private Context mContext;
    private ImageView imageView;
    private PointF startPoint = new PointF();
    private Matrix matrix = new Matrix();
    private Matrix currentMaritx = new Matrix();
    private int mode = 0;//用于标记模式
    private float startDis = 0;
    private PointF midPoint;//中心点
    public BottomImageView(Context context) {
        super(context);
        mContext = context;

//        this.setOnTouchListener(this);
    }
    public BottomImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
//        this.setOnTouchListener(this);
    }
    public BottomImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
//        this.setOnTouchListener(this);
    }

    /**
     * 两点之间的距离
     *
     * @param event
     * @return
     */
    private static float distance(MotionEvent event) {
        //两根线的距离
        float dx = event.getX(1) - event.getX(0);
        float dy = event.getY(1) - event.getY(0);
        return FloatMath.sqrt(dx * dx + dy * dy);
    }

    /**
     * 计算两点之间中心点的距离
     *
     * @param event
     * @return
     */
    private static PointF mid(MotionEvent event) {
        float midx = event.getX(1) + event.getX(0);
        float midy = event.getY(1) - event.getY(0);

        return new PointF(midx / 2, midy / 2);
    }

    public void setZoomView(ImageView imageView) {
        this.imageView = imageView;
    }

    public boolean perform(MotionEvent event) {

        if (event.getX() > this.getWidth() || event.getY() > this.getHeight()
                || event.getX() < 0 || event.getY() < 0)
            return false;
//        WidgetController.checkAndCorrect(event, getWidth(), getHeight());

        // 这里的event获取到的x,y已经是基于这个view的偏移了。。。。
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
//            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:

                int[] x0y0 = new int[2];
                getLocationInWindow(x0y0);
                int imageWidth = getDrawable().getBounds().width();
                int imageHeight = getDrawable().getBounds().height();

                int thisWidth = this.getWidth();
                int thisHeight = this.getHeight();

                int imageViewWidth = imageView.getWidth();
                int imageViewHeight = imageView.getHeight();
                int[] imageVIewx0y0 = new int[2];
                imageView.getLocationInWindow(imageVIewx0y0);

//                int[] imageXY = xy2ImageXY((int)x0y0[0]+this.getWidth(), (int)x0y0[1]+this.getHeight(), x0y0[0], x0y0[1], this.getWidth(), this.getHeight(), imageWidth, imageHeight);
                int[] imageXY = xy2ImageXY((int) event.getX(), (int) event.getY(), x0y0[0], x0y0[1], thisWidth, thisHeight, imageWidth, imageHeight);

//                Matrix mm1 = imageXY2ScreenMidXY(imageXY[0], imageXY[1], getResources().getDisplayMetrics().widthPixels, getResources().getDisplayMetrics().heightPixels);
//                float dy = screenXY2ViewXY(getResources().getDisplayMetrics().heightPixels, imageView.getHeight());
//                Matrix mm2 = new Matrix(mm1);
//                mm2.postTranslate(0, -dy);

                Matrix mm1 = imageXY2ViewXY(imageXY[0], imageXY[1], imageViewWidth, imageViewHeight, imageVIewx0y0[0], imageVIewx0y0[1]);
                imageView.setImageMatrix(mm1);
                break;
            default:
                break;
        }

        return true;
    }


    private Matrix imageXY2ViewXY(int xi, int yi, int imageViewWidth, int imageViewHeight, int imageViewX0, int imageVIewY0) {
        Matrix mm = new Matrix();
        float dx = xi - (imageViewWidth / 2);
        float dy = yi - (imageViewHeight / 2);
        mm.postTranslate(-dx, -dy);

        return mm;
    }

    private float screenXY2ViewXY(int screenHeight, int viewHeight) {
        return screenHeight / 2 - viewHeight / 2;
    }

    private Matrix imageXY2ScreenMidXY(int xi, int yi, int screenWidth, int screenHeight) {
        Matrix mm = new Matrix();
        float dx = xi - screenWidth / 2;
        float dy = yi - screenHeight / 2;
        mm.postTranslate(-dx, -dy);

        return mm;
    }

    private int[] xy2ImageXY(int x, int y, int x0, int y0, int viewWidth, int viewHeight, int imageWidth, int imageHeight) {
        int[] xy = new int[2];
        xy[0] = (x) * imageWidth / viewWidth;
        xy[1] = (y) * imageHeight / viewHeight;

        return xy;
    }
}
