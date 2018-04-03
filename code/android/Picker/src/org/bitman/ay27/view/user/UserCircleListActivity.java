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
import org.bitman.ay27.PickerApplication;
import org.bitman.ay27.R;
import org.bitman.ay27.data.DataTable;
import org.bitman.ay27.module.Circle;
import org.bitman.ay27.request.UrlGenerator;
import org.bitman.ay27.request.Urls;
import org.bitman.ay27.view.adapter.UserCircleAdapter;
import org.bitman.ay27.view.circle.CircleActivity;
import org.bitman.ay27.view.templete.ListSupportActivity;

import java.util.List;

/**
 * Proudly to use Intellij IDEA.
 * Created by ay27 on 14-9-3.
 */
public class UserCircleListActivity extends ListSupportActivity {

    @InjectView(R.id.list_without_divider_activity_list)
    ListView listView;
    @InjectView(R.id.list_without_divider_activity_toolbar)
    Toolbar toolbar;
    private long targetUserID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_without_divider_activity);
        ButterKnife.inject(this);

        toolbar.setTitleTextColor(Color.WHITE);


        targetUserID = getIntent().getLongExtra("TargetUserID", -1);

        if (targetUserID == PickerApplication.getMyUserId())
            toolbar.setTitle(getString(R.string.my_circle_title));
        else
            toolbar.setTitle(getString(R.string.other_circle_title));

        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(false);
        getSupportActionBar().setHomeButtonEnabled(true);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        UserCircleAdapter.ViewHolder holder = (UserCircleAdapter.ViewHolder) view.getTag();
        if (holder == null)
            return;
        Intent intent = new Intent(this, CircleActivity.class);
        intent.putExtra("TargetCircleID", holder.circleID);
        startActivity(intent);
    }

    @Override
    protected Parameters getChildrenParameters() {
        return new Parameters(null, null, null,
                listView, new TypeToken<List<Circle>>() {
        }.getType(), new UserCircleAdapter(this, null, false),
                DataTable.circle, UrlGenerator.queryCircleList(targetUserID), Urls.KEY_CIRCLE_LIST);
    }
}
