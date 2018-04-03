package org.bitman.ay27.view.main;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import com.google.gson.reflect.TypeToken;
import org.bitman.ay27.data.DataTable;
import org.bitman.ay27.module.PrivateMessageSum;
import org.bitman.ay27.request.UrlGenerator;
import org.bitman.ay27.request.Urls;
import org.bitman.ay27.view.adapter.UserDialogAdapter;
import org.bitman.ay27.view.private_letter.PrivateLetterActivity;
import org.bitman.ay27.view.templete.SwipeListFragment;

import java.util.List;

/**
 * Proudly to use Intellij IDEA.
 * Created by ay27 on 14-10-28.
 */
public class DialogFragment extends SwipeListFragment {
    @Override
    protected Parameters getChildParameters() {
        return new Parameters(null, null, new TypeToken<List<PrivateMessageSum>>() {
        }.getType(),
                new UserDialogAdapter(getContext(), null, false), DataTable.dialog,
                UrlGenerator.queryDialog(), Urls.KEY_PRIVATEMESSAGE_LIST);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        UserDialogAdapter.ViewHolder holder = (UserDialogAdapter.ViewHolder) view.getTag();
        if (holder == null)
            return;
        Intent intent = new Intent(getContext(), PrivateLetterActivity.class);
        intent.putExtra("DialogID", holder.dialogID);
        intent.putExtra("TargetUserID", holder.targetUserId);
        intent.putExtra("TargetUserName", holder.targetUserName);
        getContext().startActivity(intent);
    }
}
