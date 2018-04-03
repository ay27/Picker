package org.bitman.ay27.view.private_letter;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.view.*;
import android.view.inputmethod.InputMethodManager;
import android.widget.*;
import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import com.android.volley.toolbox.ImageLoader;
import com.google.gson.reflect.TypeToken;
import org.bitman.ay27.PickerApplication;
import org.bitman.ay27.R;
import org.bitman.ay27.cache.ImageCacheManager;
import org.bitman.ay27.common.TaskUtils;
import org.bitman.ay27.module.dp.PrivateLetterDP;
import org.bitman.ay27.request.ListModuleQueryRequest;
import org.bitman.ay27.request.PostRequest;
import org.bitman.ay27.request.UrlGenerator;
import org.bitman.ay27.request.Urls;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Proudly to use Intellij IDEA.
 * Created by ay27 on 14/12/30.
 */
public class PrivateLetterFragment extends Fragment  implements AdapterView.OnItemClickListener {

    @InjectView(R.id.private_letter_activity_message_list)
    ListView letterListView;
    @InjectView(R.id.private_letter_activity_editText)
    EditText editText;
    @InjectView(R.id.private_letter_activity_send_button)
    Button sendButton;

    private ProgressDialog pd;
    private long dialogID;
    private long targetUserId;
    private String targetUserName;
    private String message;

    public PrivateLetterFragment(long dialogID, long targetUserId, String targetUserName) {
        this.dialogID = dialogID;
        this.targetUserId = targetUserId;
        this.targetUserName = targetUserName;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.private_letter_activity, null);
        ButterKnife.inject(this, view);



        letterListView.setOnItemClickListener(this);
        letterListView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                // when the list is scrolling, the soft-keyboard will hide automatically.
                if (scrollState == SCROLL_STATE_TOUCH_SCROLL) {
                    InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                // do nothing
            }
        });

        pd = new ProgressDialog(getActivity());

        doQueryDialog();


        return view;
    }


    private void doQueryDialog() {
        ListModuleQueryRequest.ListRequestCallback<PrivateLetterDP> _onListQueryFinished = new ListModuleQueryRequest.ListRequestCallback<PrivateLetterDP>() {
            @Override
            public void onFinished(List<PrivateLetterDP> result) {
                letterListView.setAdapter(new _Adapter(result));
            }
        };
        String url = (dialogID != -1) ? UrlGenerator.queryDialogByDialogId(dialogID) : UrlGenerator.queryDialogByUserId(targetUserId);
        new ListModuleQueryRequest(getActivity(), url, null, Urls.KEY_PRIVATEMESSAGE_LIST, new TypeToken<List<PrivateLetterDP>>() {
        }.getType(), _onListQueryFinished);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.private_letter, menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.private_letter_menu_clear) {
            // TODO ...
            return true;
        } else
            return super.onOptionsItemSelected(item);
    }

    @OnClick(R.id.private_letter_activity_send_button)
    public void sendClick(View v) {
        Editable text = editText.getText();
        if (text == null || text.length() == 0)
            return;

        message = editText.getText().toString();
        pd.show();


        final PostRequest.PostFinishCallback postFinished = new PostRequest.PostFinishCallback() {
            @Override
            public void onFinished(Boolean result) {
                if (result) {
                    TaskUtils.executeAsyncTask(new AsyncTask<Object, Object, Object>() {
                        @Override
                        protected Object doInBackground(Object... params) {
                            // reQuery
                            doQueryDialog();
                            return null;
                        }

                        @Override
                        protected void onPostExecute(Object o) {
                            pd.dismiss();
                            editText.setText("");
                        }
                    });
                }
            }
        };

        TaskUtils.executeAsyncTask(new AsyncTask<Object, Object, Object>() {
            @Override
            protected Object doInBackground(Object... params) {
                String url = UrlGenerator.sendPrivateLetter();
                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("content", message);
                    jsonObject.put("receiverId", targetUserId);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                new PostRequest(getActivity(), url, jsonObject, postFinished);
                return null;
            }
        });
    }

    private class _Adapter extends BaseAdapter {
        private List<PrivateLetterDP> data;

        public _Adapter(List<PrivateLetterDP> data) {
            this.data = data;
        }

        @Override
        public int getCount() {
            return data.size();
        }

        @Override
        public Object getItem(int position) {
            return data.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            PrivateLetterDP current = data.get(position);
            ViewHolder holder = new ViewHolder();
            // send by me
            if (current.getSenderId() == PickerApplication.getMyUserId()) {
                convertView = LayoutInflater.from(getActivity()).inflate(R.layout.private_letter_item_right, null);
                holder.avatar = (ImageView) convertView.findViewById(R.id.private_letter_item_logo_right);
                holder.content = (TextView) convertView.findViewById(R.id.private_letter_item_message_content_right);
                holder.time = (TextView) convertView.findViewById(R.id.private_letter_item_time_right);
                holder.userName = (TextView)convertView.findViewById(R.id.private_letter_item_user_name_right);
            }
            // send by other
            else {
                convertView = LayoutInflater.from(getActivity()).inflate(R.layout.private_letter_item_left, null);
                holder.avatar = (ImageView) convertView.findViewById(R.id.private_letter_item_logo_left);
                holder.content = (TextView) convertView.findViewById(R.id.private_letter_item_message_content_left);
                holder.time = (TextView) convertView.findViewById(R.id.private_letter_item_time_left);
                holder.userName = (TextView) convertView.findViewById(R.id.private_letter_item_user_name_left);
            }

            if (holder.container != null)
                holder.container.cancelRequest();

            holder.container = ImageCacheManager.loadImage(UrlGenerator.getResourcesUrl(current.getSenderAvatarUrl()),
                    ImageCacheManager.getImageListener(holder.avatar, null, null));

            holder.userName.setText(current.getSenderName());
            holder.content.setText(current.getContent());
            holder.time.setText("" + current.getDate().toString());

            return convertView;
        }

        class ViewHolder {
            ImageView avatar;
            TextView content;
            TextView time;
            TextView userName;
            ImageLoader.ImageContainer container;
        }
    }
}
