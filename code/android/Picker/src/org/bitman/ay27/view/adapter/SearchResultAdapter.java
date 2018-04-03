package org.bitman.ay27.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.ButterKnife;
import butterknife.InjectView;
import com.android.volley.toolbox.ImageLoader;
import org.bitman.ay27.R;
import org.bitman.ay27.cache.ImageCacheManager;
import org.bitman.ay27.common.Utils;
import org.bitman.ay27.module.SearchResult;
import org.bitman.ay27.request.UrlGenerator;

import java.util.List;

/**
 * Proudly to use Intellij IDEA.
 * Created by ay27 on 14/12/3.
 */
public class SearchResultAdapter extends BaseAdapter {

    List<SearchResult> results;
    Context context;

    public SearchResultAdapter(Context context, List<SearchResult> results) {
        this.results = results;
        this.context = context;
    }

    public void setResults(List<SearchResult> results) {
        this.results = results;
        notifyDataSetInvalidated();
    }

    @Override
    public int getCount() {
        return results.size();
    }

    @Override
    public SearchResult getItem(int position) {
        return results.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.search_result_item, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        SearchResult result = results.get(position);

        if (holder.container != null)
            holder.container.cancelRequest();

        if (Utils.isNull(result.getImageUrl()))
            holder.imageView.setVisibility(View.GONE);
        else
            holder.container = ImageCacheManager.loadImage(UrlGenerator.getResourcesUrl(result.getImageUrl()),
                    ImageCacheManager.getImageListener(holder.imageView, null, null));

        if (Utils.isNull(result.getName()))
            holder.largeText.setVisibility(View.GONE);
        else
            holder.largeText.setText(result.getName());

        if (Utils.isNull(result.getContent()))
            holder.smallText.setVisibility(View.GONE);
        else
            holder.smallText.setText(result.getContent());

        String typeStr = null;
        switch (result.getType()) {
            case SearchResult.RESULT_NOTE:
                typeStr = context.getString(R.string.note);
                break;
            case SearchResult.RESULT_QUESTION:
                typeStr = context.getString(R.string.question);
                break;
            case SearchResult.RESULT_BOOK:
                typeStr = context.getString(R.string.book);
                break;
            case SearchResult.RESULT_CIRCLE:
                typeStr = context.getString(R.string.circle);
                break;
            case SearchResult.RESULT_USER:
                typeStr = context.getString(R.string.user);
                break;
            case SearchResult.RESULT_AFEED:
                typeStr = context.getString(R.string.afeed);
                break;
            default:
                typeStr = null;
        }
        holder.typeText.setText(typeStr);


        return convertView;
    }

    public class ViewHolder {
        @InjectView(R.id.search_result_item_image)
        ImageView imageView;
        @InjectView(R.id.search_result_item_text_large)
        TextView largeText;
        @InjectView(R.id.search_result_item_text_small)
        TextView smallText;
        @InjectView(R.id.search_result_item_type)
        TextView typeText;
        ImageLoader.ImageContainer container;

        public ViewHolder(View v) {
            ButterKnife.inject(this, v);
        }
    }
}
