package org.bitman.ay27.view.user;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.Toolbar;
import butterknife.ButterKnife;
import butterknife.InjectView;
import org.bitman.ay27.R;
import org.bitman.ay27.swipe_back.app.SwipeBackActivity;
import org.bitman.ay27.view.main.UserInfoFragment;

/**
 * Proudly to use Intellij IDEA.
 * Created by ay27 on 14-9-2.
 */
public class UserInfoActivity extends SwipeBackActivity {

    private UserInfoFragment mContentFragment;
    private long targetUserID;
    @InjectView(R.id.fragment_toolbar)
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_container);
        ButterKnife.inject(this);

        targetUserID = getIntent().getLongExtra("TargetUserID", -1);

        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setTitle(R.string.user_info_title);
        setSupportActionBar(toolbar);

        mContentFragment = new UserInfoFragment(targetUserID);
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.content_frame, mContentFragment).commit();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(false);
        getSupportActionBar().setHomeButtonEnabled(true);
    }
}
