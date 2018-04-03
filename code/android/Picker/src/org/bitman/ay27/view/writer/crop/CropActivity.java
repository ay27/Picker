package org.bitman.ay27.view.writer.crop;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import butterknife.ButterKnife;
import butterknife.InjectView;
import com.edmodo.cropper.CropImageView;
import org.bitman.ay27.PickerApplication;
import org.bitman.ay27.R;
import org.bitman.ay27.common.ImageDecodeUtils;
import org.bitman.ay27.common.ToastUtils;
import org.bitman.ay27.common.Utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * Proudly to use Intellij IDEA.
 * Created by ay27 on 14-9-27.
 */
public class CropActivity extends ActionBarActivity {

    @InjectView(R.id.CropImageView)
    CropImageView cropImageView;
    @InjectView(R.id.croppedImageView)
    ImageView previewImage;
    @InjectView(R.id.cropper_activity_toolbar)
    Toolbar toolbar;

    private boolean isCropped = false;
    private Uri imageUri;
    private Bitmap image;
    private Bitmap croppedImage;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cropper_activity);
        ButterKnife.inject(this);

        toolbar.setTitle(R.string.crop_activity_title);
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);


        imageUri = getIntent().getData();
        try {
            image = ImageDecodeUtils.decodeSource(this, imageUri, PickerApplication.getSCREEN_WIDTH());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        assert image != null;

        cropImageView.setImageBitmap(image);
        cropImageView.setAspectRatio(10, 10);
        cropImageView.setGuidelines(2);

        previewImage.setVisibility(View.GONE);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(false);
        getSupportActionBar().setHomeButtonEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.pre_crop_crop) {
            previewImage.setImageBitmap(croppedImage = cropImageView.getCroppedImage());
            cropImageView.setVisibility(View.GONE);
            previewImage.setVisibility(View.VISIBLE);
            isCropped = true;
            getWindow().invalidatePanelMenu(Window.FEATURE_OPTIONS_PANEL);
            return true;
//        } else if (item.getItemId() == R.id.pre_crop_rotate) {
//            cropImageView.rotateImage(90);
//            return true;
        } else if (item.getItemId() == R.id.post_crop_back) {
            cropImageView.setVisibility(View.VISIBLE);
            previewImage.setVisibility(View.GONE);
            isCropped = false;
            getWindow().invalidatePanelMenu(Window.FEATURE_OPTIONS_PANEL);
            return true;
        }

        // return to father activity.
        else if (item.getItemId() == R.id.post_crop_ok) {

            if (!isCropped) {
                ToastUtils.showError(this, getString(R.string.please_crop_before));
                return true;
            }

            Intent data = new Intent();
            try {
                Uri resultUri = Uri.fromFile(new File(Utils.write2cache(this, croppedImage)));
                data.setData(resultUri);
            } catch (IOException e) {
                e.printStackTrace();
            }
            this.setResult(RESULT_OK, data);
            this.finish();
            return true;
        } else if (item.getItemId() == android.R.id.home) {
            super.onBackPressed();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
//        if (!isCropped)
            getMenuInflater().inflate(R.menu.pre_crop, menu);
//        else
//            getMenuInflater().inflate(R.menu.post_crop, menu);
        return true;
    }
}
