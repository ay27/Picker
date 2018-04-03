package org.bitman.ay27.view.private_letter;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import butterknife.ButterKnife;
import butterknife.InjectView;
import org.bitman.ay27.R;
import org.bitman.ay27.swipe_back.app.SwipeBackActivity;

/**
 * Proudly to use Intellij IDEA.
 * Created by ay27 on 14-10-30.
 */
public class PrivateLetterActivity extends SwipeBackActivity {

    @InjectView(R.id.fragment_toolbar)
    Toolbar toolbar;
    private long dialogID;
    private long targetUserId;
    private String targetUserName;
    private Fragment fragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_container);
        ButterKnife.inject(this);


        dialogID = getIntent().getLongExtra("DialogID", -1);
        targetUserId = getIntent().getLongExtra("TargetUserID", -1);
        targetUserName = getIntent().getStringExtra("TargetUserName");

        toolbar.setTitle(targetUserName);
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);

        fragment = new PrivateLetterFragment(dialogID, targetUserId, targetUserName);
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(false);
        getSupportActionBar().setHomeButtonEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            super.onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.private_letter, menu);
        return true;
    }
}
