package org.bitman.ay27.view.circle;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;
import butterknife.ButterKnife;
import butterknife.InjectView;
import com.google.gson.reflect.TypeToken;
import org.bitman.ay27.PickerWidget.AdjustableListView;
import org.bitman.ay27.PickerWidget.markdown_support.MarkdownView;
import org.bitman.ay27.R;
import org.bitman.ay27.data.DataTable;
import org.bitman.ay27.module.dp.CircleDP;
import org.bitman.ay27.module.dp.CircleMemberDp;
import org.bitman.ay27.request.UrlGenerator;
import org.bitman.ay27.request.Urls;
import org.bitman.ay27.view.adapter.UserItemAdapter;
import org.bitman.ay27.view.templete.ListSupportActivity;
import org.bitman.ay27.view.user.UserInfoActivity;
import org.bitman.ay27.view.widget.JoinButton;

import java.util.List;

/**
 * Proudly to use Intellij IDEA.
 * Created by ay27 on 14-8-20.
 */
public class CircleActivity extends ListSupportActivity<CircleDP> {

    @InjectView(R.id.circle_activity_title)
    TextView circleTitle;
    @InjectView(R.id.circle_activity_creater)
    TextView creater;
    @InjectView(R.id.circle_activity_describe)
    MarkdownView describe;
    @InjectView(R.id.circle_activity_join)
    JoinButton joinButton;
    @InjectView(R.id.circle_activity_user_list)
    AdjustableListView userListView;
    @InjectView(R.id.circle_activity_toolbar)
    Toolbar toolbar;

    private long circleID;
    private CircleDP circle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.circle_activity);
        ButterKnife.inject(this);

        toolbar.setTitle(R.string.circle_activity_title);
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        circleID = intent.getLongExtra("TargetCircleID", -1);
    }

    @Override
    protected Parameters getChildrenParameters() {
        return new Parameters(UrlGenerator.queryCircleByID(circleID), Urls.KEY_CIRCLE, CircleDP.class,
                userListView, new TypeToken<List<CircleMemberDp>>() {
        }.getType(), new UserItemAdapter(this, null, false),
                DataTable.circleMemberDP, UrlGenerator.queryCircleUserList(circleID), Urls.KEY_USER_LIST);
    }

    @Override
    protected void step5BindViews(CircleDP result) {
        circle = result;
        if (circle == null)
            return;

        joinButton.setJoinableModule(circle);
        if (getActionBar() != null)
            getActionBar().setTitle(circle.getName());
        describe.loadMarkdown(circle.getDescribe());
        circleTitle.setText(circle.getName());
        creater.setText(circle.getEstablisherName());
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(this, UserInfoActivity.class);
        UserItemAdapter.ViewHolder holder = (UserItemAdapter.ViewHolder) view.getTag();
        if (holder == null)
            return;
        intent.putExtra("TargetUserID", holder.userID);
        startActivity(intent);
    }

}
