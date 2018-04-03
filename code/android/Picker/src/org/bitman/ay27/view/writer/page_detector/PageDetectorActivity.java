package org.bitman.ay27.view.writer.page_detector;

import android.app.Activity;
import android.content.Intent;
import android.hardware.Camera;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.*;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
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

public class PageDetectorActivity extends Activity implements SurfaceHolder.Callback, Camera.PreviewCallback {

    private static final String TAG = "Page Detector";
    private static int left, top, width, height;
    SurfaceView preview;
    ListView resultList;
    ExecutorService executorService;
    private Camera camera;
    private ImageProc imageProc;
    private boolean isInited = false;
    private int sum = 0;
    private boolean initExecutorService = false;
    private _Adapter adapter;
    private Handler updateResultListHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            String str = (String) msg.getData().get("PageNum");
            adapter.addResult(str);
        }
    };

    private AdapterView.OnItemClickListener resultItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            if (adapter.getItem(position) != null) {
                Intent intent = new Intent();
                intent.putExtra("PageNum", (String) adapter.getItem(position));
                PageDetectorActivity.this.setResult(RESULT_OK, intent);
                PageDetectorActivity.this.finish();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 要求全屏化，横屏在AndroidManifest.xml中定义
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.page_detector);
        preview = (SurfaceView) findViewById(R.id.page_detector_preview);
        resultList = (ListView) findViewById(R.id.page_detector_result_list);
        resultList.setAdapter(adapter = new _Adapter());
        resultList.setOnItemClickListener(resultItemClickListener);

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
                    }
                });
                return true;
            }
        });

    }

    @Override
    protected void onPause() {
        super.onPause();
        closeCamera();
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

        try {
            camera = Camera.open(Camera.CameraInfo.CAMERA_FACING_BACK);
            camera.setPreviewDisplay(holder);
            Camera.Parameters params = camera.getParameters();
            params.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
            params.setZoom(params.getMaxZoom() / 3);
            List<Camera.Size> previewSizes = params.getSupportedPreviewSizes();
            DisplayMetrics dm = getResources().getDisplayMetrics();
            final int screen_width = dm.widthPixels, screen_height = dm.heightPixels;
            for (Camera.Size size : previewSizes) {
                if (Math.abs(size.height - screen_width) < 10 && Math.abs(size.width - screen_height) < 10) {
                    params.setPreviewSize(size.width, size.height);
                    break;
                }
            }
            camera.setParameters(params);
            camera.setDisplayOrientation(90);
            camera.setPreviewCallback(this);

            camera.startPreview();
            camera.autoFocus(new Camera.AutoFocusCallback() {
                @Override
                public void onAutoFocus(boolean b, Camera camera) {
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
        if (sum % 50 != 0)
            return;

        View finderView = findViewById(R.id.page_detector_detect_region);
        Camera.Size previewSize = camera.getParameters().getPreviewSize();
        double scaleWidth = (double) previewSize.height / (double) getResources().getDisplayMetrics().widthPixels;
        double scaleHeight = (double) previewSize.width / (double) getResources().getDisplayMetrics().heightPixels;

        left = (int) (finderView.getLeft() * scaleWidth);
        top = (int) ((finderView.getTop() + preview.getTop()) * scaleHeight);
        width = (int) (finderView.getWidth() * scaleWidth);
        height = (int) (finderView.getHeight() * scaleHeight);

        if (!initExecutorService) {
            executorService = Executors.newCachedThreadPool();

            initExecutorService = true;
        }

        executorService.execute(new ImageThread(bytes.clone()));

    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        initCamera(preview.getHolder());
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i2, int i3) {
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        closeCamera();
    }

    private class ImageThread implements Runnable {

        private byte[] data;
        private byte[] dst;

        private ImageThread(byte[] data) {
            this.data = data;
        }

        private void reverse() {
            Camera.Size size = camera.getParameters().getPreviewSize();
            final int pre_width = size.width, pre_height = size.height;
            if (dst == null)
                dst = new byte[pre_width * pre_height];
            int delta1, delta2;
            for (int x = 0; x < pre_width; x++) {
                delta1 = x * pre_height;
                delta2 = 0;
                for (int y = pre_height - 1; y >= 0; y--) {
                    dst[delta1 + y] = data[delta2 + x];
                    delta2 += pre_width;
                }
            }
        }

        @Override
        public void run() {
            reverse();

            Camera.Size size = camera.getParameters().getPreviewSize();
            final int pre_width = size.width, pre_height = size.height;

            String str = imageProc.OCRSingleLine(dst, pre_height, pre_width, 1, pre_height, left, top, width, height);

            if (str != null && !str.isEmpty()) {
                for (int i = 0; i < str.length(); i++) {
                    if (!(str.charAt(i) >= '0' && str.charAt(i) <= '9'))
                        return;
                }

                Message msg = new Message();
                msg.getData().putString("PageNum", str);
                updateResultListHandler.sendMessage(msg);
            }
        }

    }

    private class _Adapter extends BaseAdapter {

        private String[] data;

        private _Adapter() {
            data = new String[4];
        }

        @Override
        public int getCount() {
            return data.length;
        }

        @Override
        public Object getItem(int position) {
            return data[position];
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            convertView = LayoutInflater.from(PageDetectorActivity.this).inflate(R.layout.page_detector_result_list_item, null);
            TextView textView = (TextView) convertView.findViewById(R.id.page_detector_result_list_text);
            textView.setText(data[position]);
            return convertView;
        }

        public void addResult(String str) {
            for (int i = data.length - 1; i > 0; i--) {
                data[i] = data[i - 1];
            }
            data[0] = str;
            this.notifyDataSetChanged();
        }
    }
}