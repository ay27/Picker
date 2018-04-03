package org.bitman.ay27.PickerWidget;

import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.CursorAdapter;
import android.widget.ListView;
import com.nhaarman.listviewanimations.swinginadapters.AnimationAdapter;
import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.ObjectAnimator;

/**
 * Created by storm on 14-4-15.
 */
public class CardsAnimationAdapter extends AnimationAdapter {
    private float mTranslationY = 400;

    private float mRotationX = 15;

    private long mDuration = 500;

//    public CardsAnimationAdapter(BaseAdapter baseAdapter) {
//        super(baseAdapter);
//    }

    public CardsAnimationAdapter(BaseAdapter adapter, AbsListView listView) {
        super(adapter);
        setAbsListView(listView);
    }

    @Override
    protected long getAnimationDelayMillis() {
        return 30;
    }

    @Override
    protected long getAnimationDurationMillis() {
        return mDuration;
    }

    @Override
    public Animator[] getAnimators(ViewGroup parent, View view) {
        return new Animator[]{
                ObjectAnimator.ofFloat(view, "translationY", mTranslationY, 0),
                ObjectAnimator.ofFloat(view, "rotationX", mRotationX, 0)
        };
    }
}
