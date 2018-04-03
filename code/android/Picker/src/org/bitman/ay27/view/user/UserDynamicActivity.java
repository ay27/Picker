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
import org.bitman.ay27.module.Message;
import org.bitman.ay27.module.dp.MessageDp;
import org.bitman.ay27.request.UrlGenerator;
import org.bitman.ay27.request.Urls;
import org.bitman.ay27.view.adapter.DynamicMessageAdapter;
import org.bitman.ay27.view.add_book.BookInfoActivity;
import org.bitman.ay27.view.circle.CircleActivity;
import org.bitman.ay27.view.note_content.NoteContentActivity;
import org.bitman.ay27.view.question_content.AnswerContentActivity;
import org.bitman.ay27.view.question_content.QuestionContentActivity;
import org.bitman.ay27.view.templete.ListSupportActivity;

import java.util.List;

/**
 * Proudly to use Intellij IDEA.
 * Created by ay27 on 14-10-1.
 */
public class UserDynamicActivity extends ListSupportActivity {

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


        if (targetUserID == PickerApplication.getMyUserId())
            toolbar.setTitle(getString(R.string.my_dynamic_title));
        else
            toolbar.setTitle(getString(R.string.other_dynamic_title));

        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(false);
        getSupportActionBar().setHomeButtonEnabled(true);
    }

    @Override
    protected Parameters getChildrenParameters() {
        return new Parameters(null, null, null,
                listView, new TypeToken<List<MessageDp>>(){}.getType(), new DynamicMessageAdapter(this, null, false),
                DataTable.message, UrlGenerator.queryUserFootPrint(targetUserID), Urls.KEY_USER_MESSAGE_LIST);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        DynamicMessageAdapter.ViewHolder holder = (DynamicMessageAdapter.ViewHolder) view.getTag();
        if (holder == null)
            return;
        final int type = holder.type;
        final long targetId = holder.targetId;
        Intent intent = new Intent();
        Class<?> cls = null;
        switch (type) {
            case Message.MESSAGE_FOLLOWED_ASKQUESTION:          // 1
            case Message.MESSAGE_FOLLOWED_FAVORITE_QEUSTION:    // 3
            case Message.MESSAGE_FOLLOWED_FOLLOWQUESTION:       // 7
            case Message.MESSAGE_FOLLOWED_ADD_QUESTIONCOMMENT:  // 9
            case Message.MESSAGE_QUESTION_EDIT:                 // 15
            case Message.MESSAGE_YOUR_QUESTION_UPDATE:          // 18
            case Message.MESSAGE_YOUR_QUESTION_FAVORITED:       // 19
            case Message.MESSAGE_YOUR_QUESTION_COMMENTED:       // 24
            case Message.MESSAGE_USER_ADDQUESTION:              // 25
            case Message.MESSAGE_USER_ADD_QUESTIONCOMMENT:      // 27
            case Message.MESSAGE_USER_FAVORITE_QUESTION:        // 32
                cls = QuestionContentActivity.class;
                intent.putExtra("QuestionID", targetId);
                break;
            case Message.MESSAGE_FOLLOWED_ANSWER_QUESTION:      // 2
            case Message.MESSAGE_FOLLOWED_FAVORITE_ANSWER:      // 4
            case Message.MESSAGE_FOLLOWED_ADD_ANSWERCOMMENT:    // 11
            case Message.MESSAGE_QUESTION_NEWANSWER:            // 16
            case Message.MESSAGE_YOUR_ANSWER_FAVORITED:         // 20
            case Message.MESSAGE_YOUR_ANSWER_COMMENTED:         // 23
            case Message.MESSAGE_USER_ADDANSWER:                // 26
            case Message.MESSAGE_USER_ADD_ANSWERCOMMENT:        // 29
            case Message.MESSAGE_USER_FAVORITE_ANSWER:          // 34
                cls = AnswerContentActivity.class;
                intent.putExtra("AnswerID", targetId);
                break;
            case Message.MESSAGE_FOLLOWED_FAVORITE_NOTE:        // 5
            case Message.MESSAGE_FOLLOWED_ADD_NOTECOMMENT:      // 10
            case Message.MESSAGE_FOLLOWED_ADDNOTE:              // 14
            case Message.MESSAGE_YOUR_NOTE_FAVORITED:           // 22
            case Message.MESSAGE_USER_ADD_NOTECOMMENT:          // 28
            case Message.MESSAGE_USER_ADDNOTE:                  // 30
            case Message.MESSAGE_USER_FAVORITE_NOTE:            // 33
                cls = NoteContentActivity.class;
                intent.putExtra("NoteID", targetId);
                break;
            case Message.MESSAGE_FOLLOWED_ADDBOUGHT:
                cls = BookInfoActivity.class;
                intent.putExtra("BookID", targetId);            // 8
                break;
            case Message.MESSAGE_FOLLOWED_ADDCIRCLE:            // 12
            case Message.MESSAGE_FOLLOWED_JOINCIRCLE:           // 13
            case Message.MESSAGE_USER_ADDCIRCLE:                // 37
            case Message.MESSAGE_USER_JOINCIRCLE:               // 38
                cls = CircleActivity.class;
                intent.putExtra("TargetCircleID", targetId);
                break;
            case Message.MESSAGE_FOLLOWEDUSER_FOLLOW:                 // 17
            case Message.MESSAGE_OTHERS_FOLLOW_YOU:             // 25
            case Message.MESSAGE_USER_FOLLOW_OTHER:             // 31
                cls = UserInfoActivity.class;
                intent.putExtra("TargetUserID", targetId);
                break;

            case Message.MESSAGE_FOLLOWED_FAVORITE_COMMENT:     // 6
            case Message.MESSAGE_YOUR_COMMENT_FAVORITED:        // 21
            case Message.MESSAGE_USER_FAVORITE_COMMENT:         // 35
            default:
                return;
        }
        intent.setClass(this, cls);
        startActivity(intent);
    }
}
