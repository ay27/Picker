package org.bitman.ay27.view.feed;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.ButterKnife;
import butterknife.InjectView;
import com.android.volley.toolbox.ImageLoader;
import com.hb.views.PinnedSectionListView;
import org.bitman.ay27.R;
import org.bitman.ay27.cache.ImageCacheManager;
import org.bitman.ay27.module.Section;
import org.bitman.ay27.module.dp.FeedDp;
import org.bitman.ay27.request.UrlGenerator;
import org.bitman.ay27.view.user.UserInfoActivity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Proudly to use Intellij IDEA.
 * Created by ay27 on 14/12/24.
 */
public class QuestionAdapterWithPinnedSection extends BaseAdapter implements PinnedSectionListView.PinnedSectionListAdapter {

    private ArrayList<Item> itemList;
    private List<Section> sectionList;
    private Context context;
    private int startPage, endPage;

    public QuestionAdapterWithPinnedSection(Context context, List<FeedDp> questionList, List<Section> sections) {
        this.context = context;
        this.sectionList = sections;
        Collections.sort(questionList);


        itemList = new ArrayList<Item>();
        Item item;
        itemList.add(item = new Item(Item.SECTION, questionList.get(0)));
        item.sectionStr = concatSectionStr(sectionList, questionList.get(0).getPage());
        item.startPage = sectionList.get(0).getStartPage();
        item.endPage = sectionList.get(0).getEndPage();

        itemList.add(new Item(Item.ITEM, questionList.get(0)));
        item.sectionPosition = 0;
        item.listPosition = 0;

        int listPosition = 0;

        for (int i = 1; i < questionList.size(); i++) {
            if (questionList.get(i - 1).getPage() != questionList.get(i).getPage()) {
                if (!isSameSection(sectionList, questionList.get(i - 1).getPage(), questionList.get(i).getPage())) {
                    itemList.add(item = new Item(Item.SECTION, questionList.get(i)));
                    item.sectionStr = concatSectionStr(sectionList, questionList.get(i).getPage());
                    item.sectionPosition = i;
                    item.startPage = startPage;
                    item.endPage = endPage;
                    item.listPosition = listPosition++;
                }
            }
            itemList.add(item = new Item(Item.ITEM, questionList.get(i)));
            item.sectionPosition = i;
            item.listPosition = listPosition++;
        }

    }

    private String concatSectionStr(List<Section> sections, int page) {
        for (int i = 0; i < sections.size(); i++) {
            Section tmp = sections.get(i);
            if (!tmp.hasSubSections()) {
                if (page >= tmp.getStartPage() && page <= tmp.getEndPage()) {
                    startPage = tmp.getStartPage();
                    endPage = tmp.getEndPage();
                    return tmp.getNum();
                }
            } else {
                if (page >= tmp.getStartPage() && page <= tmp.getEndPage()) {
                    return tmp.getNum() + " " + concatSectionStr(tmp.getSubSections(), page);
                }
            }
        }
        return null;
    }

    private boolean isSameSection(List<Section> sections, int page, int page1) {
        for (int i = 0; i < sections.size(); i++) {
            Section tmp = sections.get(i);
            if (!tmp.hasSubSections()) {
                if (page >= tmp.getStartPage() && page <= tmp.getEndPage()
                        &&
                        page1 >= tmp.getStartPage() && page1 <= tmp.getEndPage())
                    return true;
            } else if (page >= tmp.getStartPage() && page <= tmp.getEndPage()
                    &&
                    page1 >= tmp.getStartPage() && page1 <= tmp.getEndPage()) {
                boolean result = isSameSection(tmp.getSubSections(), page, page1);
                if (result)
                    return true;
            }
        }
        return false;
    }

    @Override
    public int getCount() {
        return itemList.size();
    }

    @Override
    public Item getItem(int position) {
        return itemList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public int getItemViewType(int position) {
        return getItem(position).type;
    }

    @Override
    public boolean isItemViewTypePinned(int viewType) {
        return viewType == Item.SECTION;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Item item = itemList.get(position);
        ViewHolder holder;

        if (item.type == Item.SECTION) {
            convertView = LayoutInflater.from(context).inflate(R.layout.section_layout, null);
            TextView textView = (TextView) convertView.findViewById(R.id.section_text);
            textView.setText(item.toString());
            return convertView;
        }

        convertView = LayoutInflater.from(context).inflate(R.layout.feed_listview_item, null);
        holder = new ViewHolder(convertView);
        convertView.setTag(holder);

        final FeedDp questionDP = itemList.get(position).feedDp;

        holder.questionID = questionDP.getId();
        holder.pageNum = questionDP.getPage();
        holder.header.setText(questionDP.getTitle());
        holder.content.setText(questionDP.getBrief());
//        holder.content.setText(questionDP.getContent());
        holder.page.setText("" + questionDP.getPage());
        if (questionDP.getUserAvatarUrl() != null)
            holder.container = ImageCacheManager.loadImage(UrlGenerator.getResourcesUrl(questionDP.getUserAvatarUrl()),
                    ImageCacheManager.getImageListener(holder.avatar, null, null));

        holder.avatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, UserInfoActivity.class);
                intent.putExtra("TargetUserID", questionDP.getUserId());
                context.startActivity(intent);
            }
        });

        return convertView;
    }


    public static class Item {

        public static final int ITEM = 0;
        public static final int SECTION = 1;

        public final int type;
        public final FeedDp feedDp;

        public String sectionStr;
        public int sectionPosition;
        public int listPosition;

        public int startPage, endPage;


        public Item(int type, FeedDp feedDp) {
            this.type = type;
            this.feedDp = feedDp;
        }

        @Override
        public String toString() {
            return sectionStr == null ? feedDp.getDate().toString() : sectionStr;
        }

    }

    public static class ViewHolder {
        public long questionID;
        public int pageNum;


        @InjectView(R.id.feed_list_item_avatar)
        ImageView avatar;
        @InjectView(R.id.feed_list_item_content)
        TextView content;
        @InjectView(R.id.feed_list_item_header)
        TextView header;
        @InjectView(R.id.feed_list_item_page)
        TextView page;
        ImageLoader.ImageContainer container;


        public ViewHolder(View v) {
            ButterKnife.inject(this, v);
        }
    }

}
