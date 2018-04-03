package org.bitman.ay27.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import butterknife.ButterKnife;
import butterknife.InjectView;
import org.bitman.ay27.R;
import org.bitman.ay27.module.Section;

import java.util.ArrayList;
import java.util.List;

/**
 * Proudly to use Intellij IDEA.
 * Created by ay27 on 15/1/2.
 */
public class SectionAdapter extends BaseAdapter {

    private ArrayList<Item> items;
    private Context context;
    private Holder[] holders;

    public SectionAdapter(Context context, ArrayList<Section> sections) {
        this.context = context;

        items = untie(0, sections);

        holders = new Holder[items.size()];
    }

    private ArrayList<Item> untie(int indent, List<Section> sections) {

        if (sections == null || sections.isEmpty())
            return null;

        ArrayList<Item> result = new ArrayList<Item>();
        for (Section section : sections) {
            result.add(new Item(section, indent));
            if (section.hasSubSections())
                result.addAll(untie(indent + 1, section.getSubSections()));
        }

        return result;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Item item = items.get(position);

        Holder holder;
        if (item.section.hasSubSections()) {
            convertView = LayoutInflater.from(context).inflate(R.layout.chapter_item, null);
            holder = new ChapterViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            convertView = LayoutInflater.from(context).inflate(R.layout.section_item, null);
            holder = new SectionViewHolder(convertView);
            convertView.setTag(convertView);
        }

        holders[position] = holder;

        String indentStr = "";
        for (int i = 0; i < item.indent * 10; i++) {
            indentStr += " ";
        }

        holder.getHeaderTxv().setText(indentStr + item.section.getNum());
        holder.getNameTxv().setText(item.section.getName());
        holder.getPageTxv().setText("" + item.section.getStartPage() + "-" + item.section.getEndPage());
        holder.setStartPage(item.section.getStartPage());
        holder.setEndPage(item.section.getEndPage());

        return convertView;
    }

    public int getEndPage(int position) {
        return holders[position].getEndPage();
    }

    public int getStartPage(int position) {
        return holders[position].getStartPage();
    }

    public static interface Holder {
        public TextView getHeaderTxv();

        public TextView getNameTxv();

        public TextView getPageTxv();

        public int getEndPage();

        public void setEndPage(int endPage);

        public int getStartPage();

        public void setStartPage(int startPage);
    }

    class ChapterViewHolder implements Holder {

        @InjectView(R.id.chapter_item_header)
        TextView header;
        @InjectView(R.id.chapter_item_name)
        TextView name;
        @InjectView(R.id.chapter_item_page)
        TextView page;

        private int startPage, endPage;

        public ChapterViewHolder(View view) {
            ButterKnife.inject(this, view);
        }

        @Override
        public TextView getHeaderTxv() {
            return header;
        }

        @Override
        public TextView getNameTxv() {
            return name;
        }

        @Override
        public TextView getPageTxv() {
            return page;
        }

        public int getEndPage() {
            return endPage;
        }

        public void setEndPage(int endPage) {
            this.endPage = endPage;
        }

        public int getStartPage() {
            return startPage;
        }

        public void setStartPage(int startPage) {
            this.startPage = startPage;
        }
    }

    class SectionViewHolder implements Holder {
        @InjectView(R.id.section_item_header)
        TextView header;
        @InjectView(R.id.section_item_name)
        TextView name;
        @InjectView(R.id.section_item_page)
        TextView page;


        private int startPage, endPage;

        public SectionViewHolder(View view) {
            ButterKnife.inject(this, view);
        }

        @Override
        public TextView getHeaderTxv() {
            return header;
        }

        @Override
        public TextView getNameTxv() {
            return name;
        }

        @Override
        public TextView getPageTxv() {
            return page;
        }

        public int getEndPage() {
            return endPage;
        }

        public void setEndPage(int endPage) {
            this.endPage = endPage;
        }

        public int getStartPage() {
            return startPage;
        }

        public void setStartPage(int startPage) {
            this.startPage = startPage;
        }
    }

    class Item {
        int indent;
        Section section;

        public Item(Section section, int indent) {
            this.section = section;
            this.indent = indent;
        }
    }

}
