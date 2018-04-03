package org.bitman.ay27.view.templete;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by ay27 on 14-10-22.
 */
public interface ListSupport {

    public View onStep1SetUpListView(LayoutInflater inflater, ViewGroup container);
    public void onStep2SetUpAdapter();
    public void onStep3SetUpDBSupport();
    public void onStep4ListQuery();
    public void onStep5ListQueryFinished(List result);
    public void onStep6BindViews();

}
