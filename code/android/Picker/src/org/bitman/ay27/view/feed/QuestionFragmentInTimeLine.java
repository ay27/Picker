package org.bitman.ay27.view.feed;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import com.google.gson.reflect.TypeToken;
import org.bitman.ay27.PickerWidget.FloatingButton;
import org.bitman.ay27.data.DataTable;
import org.bitman.ay27.module.dp.FeedDp;
import org.bitman.ay27.request.UrlGenerator;
import org.bitman.ay27.request.Urls;
import org.bitman.ay27.view.adapter.QuestionAdapter;
import org.bitman.ay27.view.question_content.QuestionContentActivity;
import org.bitman.ay27.view.templete.ListPartialLoadFragment;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Proudly to use Intellij IDEA.
 * Created by ay27 on 14-7-26.
 */
public class QuestionFragmentInTimeLine extends ListPartialLoadFragment implements ProvideList {

    private final String bookName;
    private final long bookID;
    private FloatingButton floatingButton;

    public QuestionFragmentInTimeLine(long bookId, @NotNull String bookName, FloatingButton button) {
        bookID = bookId;
        this.bookName = bookName;
        this.floatingButton = button;
    }

    @Override
    public void onStep2SetUpAdapter() {
        super.onStep2SetUpAdapter();
        floatingButton.attachToListView(getListView());
    }

    @Override
    protected Parameters getChildParameters() {
        return new Parameters(null, null, new TypeToken<List<FeedDp>>() {
        }.getType(), new QuestionAdapter(getContext(), null, false),
                DataTable.questionDP, UrlGenerator.queryQuestionListPart(bookID), Urls.KEY_QUESTION_LIST);
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        QuestionAdapter.ViewHolder holder = (QuestionAdapter.ViewHolder) view.getTag();
        if (holder == null)
            return;
        Intent intent = new Intent(getContext(), QuestionContentActivity.class);
        intent.putExtra("QuestionID", holder.questionID);
        intent.putExtra("BookName", bookName);
        intent.putExtra("BookPage", holder.pageNum);
        intent.putExtra("BookID", bookID);
        getContext().startActivity(intent);
    }

}
