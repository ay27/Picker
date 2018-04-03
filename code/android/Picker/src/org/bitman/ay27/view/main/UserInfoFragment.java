package org.bitman.ay27.view.main;

import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.*;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import com.android.volley.toolbox.ImageLoader;
import org.bitman.ay27.PickerApplication;
import org.bitman.ay27.PickerWidget.NumberAdjustableTextView;
import org.bitman.ay27.R;
import org.bitman.ay27.cache.ImageCacheManager;
import org.bitman.ay27.common.ContentType;
import org.bitman.ay27.common.TaskUtils;
import org.bitman.ay27.data.DataHelper;
import org.bitman.ay27.data.DataTable;
import org.bitman.ay27.module.dp.UserDP;
import org.bitman.ay27.request.ModuleQueryRequest;
import org.bitman.ay27.request.UrlGenerator;
import org.bitman.ay27.request.Urls;
import org.bitman.ay27.view.private_letter.PrivateLetterActivity;
import org.bitman.ay27.view.user.*;
import org.bitman.ay27.view.widget.FavoriteButton;
import org.bitman.ay27.view.widget.FollowButton;

/**
 * Proudly to use Intellij IDEA.
 * Created by ay27 on 14-8-19.
 */
public class UserInfoFragment extends Fragment implements LoaderCallbacks<Cursor> {

    @InjectView(R.id.user_info_user_image)
    ImageView userImage;
    @InjectView(R.id.user_info_user_name)
    TextView userName;
    @InjectView(R.id.user_info_email)
    TextView userEmail;
    @InjectView(R.id.user_info_follow)
    NumberAdjustableTextView followNumView;
    @InjectView(R.id.user_info_be_follow_num)
    NumberAdjustableTextView beFollowNumView;
    @InjectView(R.id.user_info_favorite)
    FavoriteButton favoriteButton;
    @InjectView(R.id.follow_button)
    FollowButton followButton;
    @InjectView(R.id.user_info_question_num)
    TextView questionNumView;
    @InjectView(R.id.user_info_note_num)
    TextView noteNumView;
    @InjectView(R.id.user_info_answer_num)
    TextView answerNumView;
    @InjectView(R.id.user_info_dynamic_num)
    TextView dynamicNumView;
    @InjectView(R.id.user_info_book_num)
    TextView bookNumView;
    @InjectView(R.id.user_info_circle_num)
    TextView circleNumView;

    private ImageLoader.ImageContainer container;

    private long targetUserID = -1;

    private UserDP userDP;
    private DataHelper helper;

    public UserInfoFragment(long targetUserID) {
        this.targetUserID = targetUserID;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.user_info_fragment, null);
        ButterKnife.inject(this, root);


        if (targetUserID == PickerApplication.getMyUserId()) {
            followButton.setVisibility(View.GONE);
            setHasOptionsMenu(false);
        } else
            setHasOptionsMenu(true);

        helper = new DataHelper(getActivity(), getActivity().getContentResolver());
        getLoaderManager().initLoader(0, null, this);

        doQueryUserInfo();

        return root;
    }

    private void doQueryUserInfo() {
//        Cursor cursor = helper.queryOne(DataTable.getContentUri(DataTable.userDP), DataTable.KEY_ID, targetUserID);
//        if (cursor == null || cursor.getCount() == 0) {
            String url = UrlGenerator.queryUserInfo(targetUserID);
            ModuleQueryRequest.RequestFinishedCallback<UserDP> requestFinished = new ModuleQueryRequest.RequestFinishedCallback<UserDP>() {
                @Override
                public void onFinished(final String json, final UserDP result) {
                    TaskUtils.executeAsyncTask(new AsyncTask<Object, Object, Object>() {
                        @Override
                        protected Object doInBackground(Object... params) {
                            helper.deleteAll(DataTable.getContentUri(DataTable.userDP));
                            helper.insert(DataTable.getContentUri(DataTable.userDP), result);
                            return null;
                        }
                    });
                    userDP = result;
                    setViewsAfterUserReady();
                }
            };
            new ModuleQueryRequest(getActivity(), url, null, Urls.KEY_USER, UserDP.class, requestFinished);
//        } else {
//            cursor.moveToNext();
//            userDP = new Gson().fromJson(cursor.getString(cursor.getColumnIndex(DataTable.KEY_JSON)), UserDP.class);
//            setViewsAfterUserReady();
//        }
    }

    private void setViewsAfterUserReady() {

        followButton.setFollowModule(userDP, ContentType.User);

        userName.setText(userDP.getName());
        followNumView.setText("" + userDP.getFollowNum());
        beFollowNumView.setText("" + userDP.getBeFollowNum());
        questionNumView.setText("" + userDP.getQuestionNum());
        noteNumView.setText("" + userDP.getNoteNum());
        answerNumView.setText("" + userDP.getAnswerNum());
        bookNumView.setText("" + userDP.getBookNum());
        circleNumView.setText("" + userDP.getCircleNum());

        if (container != null)
            container.cancelRequest();
        container = ImageCacheManager.loadImage(UrlGenerator.getResourcesUrl(userDP.getAvatarUrl()),
                ImageCacheManager.getImageListener(userImage, null, null));
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return new CursorLoader(getActivity(), DataTable.getContentUri(DataTable.userDP), null, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> objectLoader, Cursor o) {
    }

    @Override
    public void onLoaderReset(Loader<Cursor> objectLoader) {
    }


    @OnClick(R.id.user_info_question)
    public void questionClick(View v) {
        start2OtherActivity(UserQuestionListActivity.class);
    }

    @OnClick(R.id.user_info_note)
    public void noteClick(View v) {
        start2OtherActivity(UserNoteListActivity.class);
    }

    @OnClick(R.id.user_info_answer)
    public void answerClick(View v) {
        start2OtherActivity(UserAnswerListActivity.class);
    }

    @OnClick(R.id.user_info_dynamic)
    public void dynamicClick(View v) {
        start2OtherActivity(UserDynamicActivity.class);
    }

    @OnClick(R.id.user_info_book)
    public void bookClick(View v) {
        start2OtherActivity(UserBookListActivity.class);
    }

    @OnClick(R.id.user_info_circle)
    public void circleClick(View v) {
        start2OtherActivity(UserCircleListActivity.class);
    }

    @OnClick(R.id.user_info_follow_other)
    public void followOtherClick(View v) {
        start2OtherActivity(UserFollowOtherListActivity.class);
    }

    @OnClick(R.id.user_info_be_follow)
    public void beFollowClick(View v) {
        start2OtherActivity(BeFollowListActivity.class);
    }

    private void start2OtherActivity(Class<?> cls) {
        Intent intent = new Intent(getActivity(), cls);
        intent.putExtra("TargetUserID", targetUserID);
        startActivity(intent);
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.user_info, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.user_info_pmessage) {
            Intent intent = new Intent(getActivity(), PrivateLetterActivity.class);
            intent.putExtra("TargetUserID", userDP.getId());
            intent.putExtra("TargetUserName", userDP.getName());
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
