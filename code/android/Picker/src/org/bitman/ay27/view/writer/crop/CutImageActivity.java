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
import org.bitman.ay27.ConcreteImageTool;
import org.bitman.ay27.PickerApplication;
import org.bitman.ay27.R;
import org.bitman.ay27.common.ImageDecodeUtils;
import org.bitman.ay27.common.ToastUtils;
import org.bitman.ay27.common.Utils;
import org.bitman.ay27.cutImage.CutImageView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * Proudly to use Intellij IDEA.
 * Created by ay27 on 14-9-28.
 */
public class CutImageActivity extends ActionBarActivity {

    @InjectView(R.id.cut_image_activity_cut_image)
    CutImageView cutImageView;
    @InjectView(R.id.cut_image_activity_show)
    ImageView preview;
    @InjectView(R.id.cut_image_activity_toolbar)
    Toolbar toolbar;

    private Uri imageUri;
    private boolean isCut = false;
    private Bitmap cutBitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cut_image_activity);
        ButterKnife.inject(this);

        toolbar.setTitle(R.string.cut_image_title);
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);

        imageUri = getIntent().getData();
        cutImageView.setImageProc(ConcreteImageTool.getImageProc());
        try {
            cutImageView.setBitmap(ImageDecodeUtils.decodeSource(this, imageUri, PickerApplication.getSCREEN_WIDTH()));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        preview.setVisibility(View.GONE);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(false);
        getSupportActionBar().setHomeButtonEnabled(true);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ConcreteImageTool.recycle();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
//        if (!isCut) {
            getMenuInflater().inflate(R.menu.pre_cut_image, menu);
//        } else {
//            getMenuInflater().inflate(R.menu.post_cut_image, menu);
//        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.pre_cut_image_cut) {
            try {
                cutBitmap = cutImageView.getCropArea();
                isCut = true;
            } catch (Exception e) {
                e.printStackTrace();
            }
            preview.setImageBitmap(cutBitmap);
            cutImageView.setVisibility(View.GONE);
            preview.setVisibility(View.VISIBLE);
            isCut = true;
            getWindow().invalidatePanelMenu(Window.FEATURE_OPTIONS_PANEL);
            return true;
        } else if (item.getItemId() == R.id.post_cut_image_back) {
            cutImageView.setVisibility(View.VISIBLE);
            preview.setVisibility(View.GONE);
            isCut = false;
            getWindow().invalidatePanelMenu(Window.FEATURE_OPTIONS_PANEL);
            return true;
        } else if (item.getItemId() == R.id.post_cut_image_ok) {
            if (!isCut) {
                ToastUtils.showError(this, getString(R.string.please_crop_before));
                return true;
            }
            Intent data = new Intent();
            try {
                Uri resultUri = Uri.fromFile(new File(Utils.write2cache(this, cutBitmap)));
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
}
