package org.bitman.ay27.cutImage;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Point;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import org.bitman.ay27.R;
import org.bitman.picker.imageproc_module.ConcreteImageProc;
import org.bitman.picker.imageproc_module.ImageProc;

import java.util.UUID;

/**
 * Proudly to use Intellij IDEA.
 * Created by ay27 on 14-9-27.
 */
public class CutImageView extends FrameLayout {
    public CutImageView(Context context) {
        super(context);
        initial(null);
    }

    public CutImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initial(attrs);
    }

    public CutImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initial(attrs);
    }

//    private int imageResource;
    private View views;
    private BottomImageView bottomImageView;
    private ChooseArea chooseArea;
    private ImageView zoomView;
    private ImageProc imageProc;
    private Bitmap image;

    private void initial(AttributeSet attrs) {
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.CutImageView);

//        imageResource = typedArray.getResourceId(R.styleable.CutImageView_imageSrc, -1);

        typedArray.recycle();

        views = LayoutInflater.from(getContext()).inflate(R.layout.cut_image, null);
        bottomImageView = (BottomImageView) views.findViewById(R.id.layout2_bottomView);
        chooseArea = (ChooseArea) views.findViewById(R.id.layout2_topView);
        zoomView = (ImageView) views.findViewById(R.id.layout2_imageAbove);

        bottomImageView.setZoomView(zoomView);
        chooseArea.setBottomView(bottomImageView);

//        if (imageResource != -1) {
//            image = null;
//            zoomView.setImageBitmap(image = getBitmapById(imageResource));
//            bottomImageView.setImageBitmap(image);
//        }

        addView(views);
    }

    private void setDefaultRegion() {
        final int imageWidth = image.getWidth();
        final int imageHeight = image.getHeight();
        int x0 = imageWidth/4, x1 = x0*3;
        int y0 = imageHeight/4, y1 = y0*3;

        x0 = mulScale(x0, imageWidth, bottomImageView.getWidth());
        x1 = mulScale(x1, imageWidth, bottomImageView.getWidth());
        y0 = mulScale(y0, imageHeight, bottomImageView.getHeight());
        y1 = mulScale(y1, imageHeight, bottomImageView.getHeight());
        chooseArea.setRegion(new Point(x0, y0), new Point(x1, y0), new Point(x1, y1), new Point(x0, y1));
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        setDefaultRegion();
    }

    private int mulScale(int xx, int image_size, int view_size) {
        return (int)((double)xx * (double)view_size / (double)image_size);
    }

//    private Bitmap getBitmapById(int imageResource) {
//        return null;
//    }

    public void setBitmap(Bitmap bitmap) {
        zoomView.setImageBitmap(bitmap);
        bottomImageView.setImageBitmap(bitmap);
        image = bitmap;
    }

    public void setImageProc(ImageProc imageProc) {
        this.imageProc = imageProc;
    }

    public Bitmap getCropArea() throws Exception {

        if (imageProc == null)
            throw new Exception("you must set up the image proc.");

        double scale = ((double) image.getHeight()) / ((double) chooseArea.getHeight());
        final Point[] points = chooseArea.getRegion();

        int[] ints = new int[8];
        for (int i = 0; i < 4; i++) {
            ints[2 * i] = (int) (((double) points[i].x) * scale);
            ints[2 * i + 1] = (int) (((double) points[i].y) * scale);
        }
        int temp = ints[4];
        ints[4] = ints[6];
        ints[6] = temp;
        temp = ints[5];
        ints[5] = ints[7];
        ints[7] = temp;

        int dstWidth = (int) (((double) getMax(points[1].x - points[0].x, points[1].x - points[3].x, points[2].x - points[0].x, points[2].x - points[3].x)) * scale);
        int dstHeight = (int) (((double) getMax(points[3].y - points[0].y, points[3].y - points[1].y, points[2].y - points[0].y, points[2].y - points[1].y)) * scale);

        Bitmap result = imageProc.WarpHomography(image, ints, dstHeight, dstWidth);

        return result;
    }


    private static int getMax(int... ints) {
        int maxOne = Integer.MIN_VALUE;
        for (int i = 0; i < ints.length; i++) {
            maxOne = Math.max(maxOne, ints[i]);
        }
        return maxOne;
    }

}
