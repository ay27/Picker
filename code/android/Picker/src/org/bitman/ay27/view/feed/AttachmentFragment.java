package org.bitman.ay27.view.feed;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import com.google.gson.reflect.TypeToken;
import org.bitman.ay27.data.DataTable;
import org.bitman.ay27.module.dp.AttachmentFeedDp;
import org.bitman.ay27.request.UrlGenerator;
import org.bitman.ay27.request.Urls;
import org.bitman.ay27.view.adapter.AttachmentAdapter;
import org.bitman.ay27.view.attachment.AttachmentActivity;
import org.bitman.ay27.view.templete.ListPartialLoadFragment;

import java.util.List;

/**
 * Created by ay27 on 14-11-5.
 */
public class AttachmentFragment extends ListPartialLoadFragment implements ProvideList {

    private final long bookID;
    private final String bookName;

    public AttachmentFragment(String bookName, long bookID) {
        this.bookName = bookName;
        this.bookID = bookID;
    }

    @Override
    protected Parameters getChildParameters() {
        return new Parameters(null, null, new TypeToken<List<AttachmentFeedDp>>() {
        }.getType(),
                new AttachmentAdapter(getActivity(), null, false), DataTable.attachmentDp,
                UrlGenerator.queryAttachmentListPart(bookID), Urls.KEY_ATTACHMENT_LIST);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        AttachmentAdapter.ViewHolder holder = (AttachmentAdapter.ViewHolder) view.getTag();
        if (holder == null)
            return;
        Intent intent = new Intent(getActivity(), AttachmentActivity.class);
        intent.putExtra("AttachmentID", holder.attachmentId);
        intent.putExtra("BookName", bookName);
        intent.putExtra("BookID", bookID);
        getActivity().startActivity(intent);
    }
}
