package org.bitman.ay27.view.feed;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import com.google.gson.reflect.TypeToken;
import org.bitman.ay27.common.ContentType;
import org.bitman.ay27.data.DataTable;
import org.bitman.ay27.module.dp.FeedDp;
import org.bitman.ay27.request.UrlGenerator;
import org.bitman.ay27.request.Urls;
import org.bitman.ay27.view.adapter.NoteAdapter;
import org.bitman.ay27.view.note_content.NoteContentActivity;
import org.bitman.ay27.view.templete.ListPartialLoadFragment;
import org.bitman.ay27.view.writer.WriteActivity;

import java.util.List;

/**
 * Proudly to use Intellij IDEA.
 * Created by ay27 on 14-7-26.
 */
public class NoteFragment extends ListPartialLoadFragment implements ProvideList {

    private final long bookID;
    private final String bookName;
    private View.OnClickListener onFloatCLickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(getActivity(), WriteActivity.class);
            intent.putExtra("Type", ContentType.Note);
            intent.putExtra("BookID", bookID);
            startActivity(intent);
        }
    };

    public NoteFragment(final long bookID, final String bookName) {
        this.bookID = bookID;
        this.bookName = bookName;
    }

    @Override
    protected Parameters getChildParameters() {
        return new Parameters(null, null, new TypeToken<List<FeedDp>>() {
        }.getType(), new NoteAdapter(getContext(), null, false),
                DataTable.noteDP, UrlGenerator.queryNoteListPart(bookID), Urls.KEY_NOTE_LIST);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        NoteAdapter.ViewHolder holder = (NoteAdapter.ViewHolder) view.getTag();
        if (holder == null)
            return;
        Intent intent = new Intent(getContext(), NoteContentActivity.class);
        intent.putExtra("NoteID", holder.noteID);
        intent.putExtra("BookName", bookName);
        intent.putExtra("BookID", bookID);
        getContext().startActivity(intent);
    }

}
