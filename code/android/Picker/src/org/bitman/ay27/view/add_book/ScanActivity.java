package org.bitman.ay27.view.add_book;

import android.app.Activity;
import android.content.Intent;
import android.hardware.Camera;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.*;
import android.widget.Toast;
import butterknife.ButterKnife;
import butterknife.InjectView;
import org.bitman.ay27.ConcreteImageTool;
import org.bitman.ay27.R;
import org.bitman.picker.imageproc_module.ImageProc;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Proudly to use Intellij IDEA.
 * Created by ay27 on 14-7-25.
 */

public class ScanActivity extends Activity implements SurfaceHolder.Callback, Camera.PreviewCallback {

    private static final String TAG = "Scan ISBN";
    private static int picWidth, picHeight, left, top, width, height;
    @InjectView(R.id.isbn_scaner_preview)
    SurfaceView preview;
    ExecutorService executorService;
    private Camera camera;
    private ImageProc imageProc;
    private boolean isInited = false;
    private int sum = 0;
    private boolean getInfo = false;
    private Handler start2OtherActivityHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {

            executorService.shutdown();
            String str = (String) msg.getData().get("ISBN");
            Toast.makeText(ScanActivity.this, str, Toast.LENGTH_SHORT).show();
            closeCamera();
            Intent intent = new Intent(ScanActivity.this, BookInfoActivity.class);
            intent.putExtra("ISBN", str);
            startActivity(intent);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 要求全屏化，横屏在AndroidManifest.xml中定义
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.isbnscaner);
        ButterKnife.inject(this);

        imageProc = ConcreteImageTool.getImageProc();

        preview.getHolder().setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        preview.getHolder().addCallback(this);

        preview.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                camera.autoFocus(new Camera.AutoFocusCallback() {
                    @Override
                    public void onAutoFocus(boolean b, Camera camera) {
                        Log.v("camera", "auto focus");
//                        camera.setOneShotPreviewCallback(ScanActivity.this);
                    }
                });
                return true;
            }
        });


    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i(TAG, "resume  time = " + System.currentTimeMillis());
    }

    @Override
    protected void onPause() {
        super.onPause();

        Log.i(TAG, "pause time = " + System.currentTimeMillis());

        closeCamera();

        Log.i(TAG, "close camera time = " + System.currentTimeMillis());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ConcreteImageTool.recycle();
    }

    private synchronized void closeCamera() {
        if (camera != null) {
            camera.setPreviewCallback(null);
            camera.stopPreview();
            camera.release();
            camera = null;
            isInited = false;
        }
    }

    public synchronized void initCamera(SurfaceHolder holder) {
        if (isInited)
            return;

        Log.i(TAG, "before init camera time = " + System.currentTimeMillis());

        try {
            camera = Camera.open(Camera.CameraInfo.CAMERA_FACING_BACK);
            camera.setPreviewDisplay(holder);
            Camera.Parameters params = camera.getParameters();
            params.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
            List<Camera.Size> sizes = params.getSupportedPreviewSizes();
            List<Camera.Size> pictureSize = params.getSupportedPictureSizes();
            for (Camera.Size size : pictureSize)
                if (Math.abs(size.height - 1080) < 15 || Math.abs(size.width - 1920) < 15) {
                    params.setPictureSize(size.width, size.height);
                    break;
                }
            params.setPreviewSize(sizes.get(0).width, sizes.get(0).height);
//            params.setRotation(90);
            camera.setParameters(params);
            camera.setDisplayOrientation(90);
            camera.setPreviewCallback(this);
//            camera.setOneShotPreviewCallback(this);

            camera.startPreview();
            camera.autoFocus(new Camera.AutoFocusCallback() {
                @Override
                public void onAutoFocus(boolean b, Camera camera) {
//                    camera.setOneShotPreviewCallback(ScanActivity.this);
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }

        Log.i(TAG, "after init camera time = " + System.currentTimeMillis());

        isInited = true;
    }

    @Override
    public void onPreviewFrame(byte[] bytes, Camera camera) {

        Log.i(TAG, "on preview time = " + System.currentTimeMillis());

        sum++;
        if (sum % 5 != 0)
            return;

        if (!getInfo) {
            View finderView = findViewById(R.id.isbn_scaner_region);
            Camera.Size picSize = camera.getParameters().getPictureSize();
            Camera.Size previewSize = camera.getParameters().getPreviewSize();
            picWidth = picSize.width;
            picHeight = picSize.height;
            double scale = (double) picWidth / (double) previewSize.width;

            Log.i(TAG, "scale = " + scale);

            left = (int) (finderView.getLeft() * scale);
            top = (int) (finderView.getTop() * scale);
            width = (int) (finderView.getWidth() * scale);
            height = (int) (finderView.getHeight() * scale);

            executorService = Executors.newCachedThreadPool();

            getInfo = true;
        }

//        new HandleImageTask(bytes).execute();
        executorService.execute(new ImageThread(bytes.clone()));

    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        Log.i(TAG, "surface create time = " + System.currentTimeMillis());
        initCamera(preview.getHolder());
        Log.i(TAG, "after surface create time = " + System.currentTimeMillis());
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i2, int i3) {
        Log.i(TAG, "surface change time = " + System.currentTimeMillis());
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        Log.i(TAG, "surface destroy time = " + System.currentTimeMillis());
        closeCamera();
        Log.i(TAG, "after surface destroy time = " + System.currentTimeMillis());
    }

    private class ImageThread implements Runnable {

        private byte[] data;

        private ImageThread(byte[] data) {
            this.data = data;
        }

        private byte[] reverse(int width, int height, byte[] yuvData) {
            byte[] result = new byte[yuvData.length];
            int delta1, delta2;
            for (int x = 0; x < width; x++) {
                delta1 = x * height;
                delta2 = 0;
                for (int y = 0; y < height; y++) {
                    result[delta1 + y] = yuvData[delta2 + x];
                    delta2 += width;
                }
            }
            return result;
        }

        @Override
        public void run() {
            String str = imageProc.DecodeBarCodeISBN_Fast(reverse(picWidth, picHeight, data), picHeight, picWidth, left, top, width, height, false);
            if (str != null && !str.isEmpty()) {
                Message msg = new Message();
                msg.getData().putString("ISBN", str);
                start2OtherActivityHandler.sendMessage(msg);
            }
        }
    }

}