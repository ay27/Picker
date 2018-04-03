package org.bitman.ay27.view.user;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import butterknife.ButterKnife;
import butterknife.InjectView;
import com.google.gson.reflect.TypeToken;
import org.bitman.ay27.R;
import org.bitman.ay27.data.DataTable;
import org.bitman.ay27.module.dp.UserDP;
import org.bitman.ay27.request.UrlGenerator;
import org.bitman.ay27.request.Urls;
import org.bitman.ay27.view.adapter.UserItemAdapter;
import org.bitman.ay27.view.templete.ListSupportActivity;

import java.util.List;

/**
 * Proudly to use Intellij IDEA.
 * Created by ay27 on 14-10-1.
 */
public class UserFollowOtherListActivity extends ListSupportActivity {

    @InjectView(R.id.list_with_divider_activity_list)
    ListView listView;
    @InjectView(R.id.list_with_divider_activity_toolbar)
    Toolbar toolbar;

    private long targetUserID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_with_divider_activity);
        ButterKnife.inject(this);

        targetUserID = getIntent().getLongExtra("TargetUserID", -1);

        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setTitle(getString(R.string.follow_other_title));
        setSupportActionBar(toolbar);


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(false);
        getSupportActionBar().setHomeButtonEnabled(true);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        UserItemAdapter.ViewHolder holder = (UserItemAdapter.ViewHolder) view.getTag();
        if (holder == null)
            return;
        Intent intent = new Intent(this, UserInfoActivity.class);
        intent.putExtra("TargetUserID", holder.userID);
        startActivity(intent);
    }

    @Override
    protected Parameters getChildrenParameters() {
        return new Parameters(null, null, null,
                listView, new TypeToken<List<UserDP>>() {
        }.getType(), new UserItemAdapter(this, null, false),
                DataTable.followOther, UrlGenerator.queryUserFollowOtherList(targetUserID), Urls.KEY_USER_LIST);
    }
}
