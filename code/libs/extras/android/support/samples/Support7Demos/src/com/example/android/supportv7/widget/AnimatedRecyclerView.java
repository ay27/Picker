/*
 * Copyright (C) 2014 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.android.supportv7.widget;

import android.support.v4.util.ArrayMap;
import android.widget.CompoundButton;
import com.example.android.supportv7.R;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AnimatedRecyclerView extends Activity {

    private static final int SCROLL_DISTANCE = 80; // dp

    private RecyclerView mRecyclerView;

    private int mNumItemsAdded = 0;
    ArrayList<String> mItems = new ArrayList<String>();
    MyAdapter mAdapter;

    boolean mAnimationsEnabled = true;
    boolean mPredictiveAnimationsEnabled = true;
    RecyclerView.ItemAnimator mCachedAnimator = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.animated_recycler_view);

        ViewGroup container = (ViewGroup) findViewById(R.id.container);
        mRecyclerView = new RecyclerView(this);
        mCachedAnimator = mRecyclerView.getItemAnimator();
        mRecyclerView.setLayoutManager(new MyLayoutManager(this));
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));
        for (int i = 0; i < 6; ++i) {
            mItems.add("Item #" + i);
        }
        mAdapter = new MyAdapter(mItems);
        mRecyclerView.setAdapter(mAdapter);
        container.addView(mRecyclerView);

        CheckBox enableAnimations = (CheckBox) findViewById(R.id.enableAnimations);
        enableAnimations.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked && mRecyclerView.getItemAnimator() == null) {
                    mRecyclerView.setItemAnimator(mCachedAnimator);
                } else if (!isChecked && mRecyclerView.getItemAnimator() != null) {
                    mRecyclerView.setItemAnimator(null);
                }
                mAnimationsEnabled = isChecked;
            }
        });

        CheckBox enablePredictiveAnimations =
                (CheckBox) findViewById(R.id.enablePredictiveAnimations);
        enablePredictiveAnimations.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mPredictiveAnimationsEnabled = isChecked;
            }
        });

        CheckBox enableChangeAnimations =
                (CheckBox) findViewById(R.id.enableChangeAnimations);
        enableChangeAnimations.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mCachedAnimator.setSupportsChangeAnimations(isChecked);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuItemCompat.setShowAsAction(menu.add("Layout"), MenuItemCompat.SHOW_AS_ACTION_IF_ROOM);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        mRecyclerView.requestLayout();
        return super.onOptionsItemSelected(item);
    }

    public void checkboxClicked(View view) {
        ViewGroup parent = (ViewGroup) view.getParent();
        boolean selected = ((CheckBox) view).isChecked();
        MyViewHolder holder = (MyViewHolder) mRecyclerView.getChildViewHolder(parent);
        mAdapter.selectItem(holder, selected);
    }

    public void itemClicked(View view) {
        ViewGroup parent = (ViewGroup) view;
        MyViewHolder holder = (MyViewHolder) mRecyclerView.getChildViewHolder(parent);
        mAdapter.toggleExpanded(holder);
        mAdapter.notifyItemChanged(holder.getPosition());
    }

    public void deleteItem(View view) {
        int numItems = mItems.size();
        if (numItems > 0) {
            for (int i = numItems - 1; i >= 0; --i) {
                final String itemText = mItems.get(i);
                boolean selected = mAdapter.mSelected.get(itemText);
                if (selected) {
                    removeAtPosition(i);
                }
            }
        }
    }

    private String generateNewText() {
        return "Added Item #" + mNumItemsAdded++;
    }

    public void d1a2d3(View view) {
        removeAtPosition(1);
        addAtPosition(2, "Added Item #" + mNumItemsAdded++);
        removeAtPosition(3);
    }

    private void removeAtPosition(int position) {
        mItems.remove(position);
        mAdapter.notifyItemRemoved(position);
    }

    private void addAtPosition(int position, String text) {
        mItems.add(position, text);
        mAdapter.mSelected.put(text, Boolean.FALSE);
        mAdapter.mExpanded.put(text, Boolean.FALSE);
        mAdapter.notifyItemInserted(position);
    }

    public void addDeleteItem(View view) {
        addItem(view);
        deleteItem(view);
    }

    public void deleteAddItem(View view) {
        deleteItem(view);
        addItem(view);
    }

    public void addItem(View view) {
        addAtPosition(3, "Added Item #" + mNumItemsAdded++);
    }

    /**
     * A basic ListView-style LayoutManager.
     */
    class MyLayoutManager extends RecyclerView.LayoutManager {
        private static final String TAG = "MyLayoutManager";
        private int mFirstPosition;
        private final int mScrollDistance;

        public MyLayoutManager(Context c) {
            final DisplayMetrics dm = c.getResources().getDisplayMetrics();
            mScrollDistance = (int) (SCROLL_DISTANCE * dm.density + 0.5f);
        }

        @Override
        public boolean supportsPredictiveItemAnimations() {
            return mPredictiveAnimationsEnabled;
        }

        @Override
        public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
            int parentBottom = getHeight() - getPaddingBottom();

            final View oldTopView = getChildCount() > 0 ? getChildAt(0) : null;
            int oldTop = getPaddingTop();
            if (oldTopView != null) {
                oldTop = Math.min(oldTopView.getTop(), oldTop);
            }

            // Note that we add everything to the scrap, but we do not clean it up;
            // that is handled by the RecyclerView after this method returns
            detachAndScrapAttachedViews(recycler);

            int top = oldTop;
            int bottom = top;
            final int left = getPaddingLeft();
            final int right = getWidth() - getPaddingRight();

            int count = state.getItemCount();
            for (int i = 0; mFirstPosition + i < count && top < parentBottom; i++, top = bottom) {
                View v = recycler.getViewForPosition(mFirstPosition + i);

                RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) v.getLayoutParams();
                addView(v);
                measureChild(v, 0, 0);
                bottom = top + v.getMeasuredHeight();
                v.layout(left, top, right, bottom);
                if (mPredictiveAnimationsEnabled && params.isItemRemoved()) {
                    parentBottom += v.getHeight();
                }
            }

            if (mAnimationsEnabled && mPredictiveAnimationsEnabled && !state.isPreLayout()) {
                // Now that we've run a full layout, figure out which views were not used
                // (cached in previousViews). For each of these views, position it where
                // it would go, according to its position relative to the visible
                // positions in the list. This information will be used by RecyclerView to
                // record post-layout positions of these items for the purposes of animating them
                // out of view

                View lastVisibleView = getChildAt(getChildCount() - 1);
                if (lastVisibleView != null) {
                    RecyclerView.LayoutParams lastParams =
                            (RecyclerView.LayoutParams) lastVisibleView.getLayoutParams();
                    int lastPosition = lastParams.getViewPosition();
                    final List<RecyclerView.ViewHolder> previousViews = recycler.getScrapList();
                    count = previousViews.size();
                    for (int i = 0; i < count; ++i) {
                        View view = previousViews.get(i).itemView;
                        RecyclerView.LayoutParams params =
                                (RecyclerView.LayoutParams) view.getLayoutParams();
                        if (params.isItemRemoved()) {
                            continue;
                        }
                        int position = params.getViewPosition();
                        int newTop;
                        if (position < mFirstPosition) {
                            newTop = view.getHeight() * (position - mFirstPosition);
                        } else {
                            newTop = lastVisibleView.getTop() + view.getHeight() *
                                    (position - lastPosition);
                        }
                        view.offsetTopAndBottom(newTop - view.getTop());
                    }
                }
            }
        }

        @Override
        public RecyclerView.LayoutParams generateDefaultLayoutParams() {
            return new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
        }

        @Override
        public boolean canScrollVertically() {
            return true;
        }

        @Override
        public int scrollVerticallyBy(int dy, RecyclerView.Recycler recycler,
                RecyclerView.State state) {
            if (getChildCount() == 0) {
                return 0;
            }

            int scrolled = 0;
            final int left = getPaddingLeft();
            final int right = getWidth() - getPaddingRight();
            if (dy < 0) {
                while (scrolled > dy) {
                    final View topView = getChildAt(0);
                    final int hangingTop = Math.max(-topView.getTop(), 0);
                    final int scrollBy = Math.min(scrolled - dy, hangingTop);
                    scrolled -= scrollBy;
                    offsetChildrenVertical(scrollBy);
                    if (mFirstPosition > 0 && scrolled > dy) {
                        mFirstPosition--;
                        View v = recycler.getViewForPosition(mFirstPosition);
                        addView(v, 0);
                        measureChild(v, 0, 0);
                        final int bottom = topView.getTop(); // TODO decorated top?
                        final int top = bottom - v.getMeasuredHeight();
                        v.layout(left, top, right, bottom);
                    } else {
                        break;
                    }
                }
            } else if (dy > 0) {
                final int parentHeight = getHeight();
                while (scrolled < dy) {
                    final View bottomView = getChildAt(getChildCount() - 1);
                    final int hangingBottom = Math.max(bottomView.getBottom() - parentHeight, 0);
                    final int scrollBy = -Math.min(dy - scrolled, hangingBottom);
                    scrolled -= scrollBy;
                    offsetChildrenVertical(scrollBy);
                    if (scrolled < dy && state.getItemCount() > mFirstPosition + getChildCount()) {
                        View v = recycler.getViewForPosition(mFirstPosition + getChildCount());
                        final int top = getChildAt(getChildCount() - 1).getBottom();
                        addView(v);
                        measureChild(v, 0, 0);
                        final int bottom = top + v.getMeasuredHeight();
                        v.layout(left, top, right, bottom);
                    } else {
                        break;
                    }
                }
            }
            recycleViewsOutOfBounds(recycler);
            return scrolled;
        }

        @Override
        public View onFocusSearchFailed(View focused, int direction,
                RecyclerView.Recycler recycler, RecyclerView.State state) {
            final int oldCount = getChildCount();

            if (oldCount == 0) {
                return null;
            }

            final int left = getPaddingLeft();
            final int right = getWidth() - getPaddingRight();

            View toFocus = null;
            int newViewsHeight = 0;
            if (direction == View.FOCUS_UP || direction == View.FOCUS_BACKWARD) {
                while (mFirstPosition > 0 && newViewsHeight < mScrollDistance) {
                    mFirstPosition--;
                    View v = recycler.getViewForPosition(mFirstPosition);
                    final int bottom = getChildAt(0).getTop(); // TODO decorated top?
                    addView(v, 0);
                    measureChild(v, 0, 0);
                    final int top = bottom - v.getMeasuredHeight();
                    v.layout(left, top, right, bottom);
                    if (v.isFocusable()) {
                        toFocus = v;
                        break;
                    }
                }
            }
            if (direction == View.FOCUS_DOWN || direction == View.FOCUS_FORWARD) {
                while (mFirstPosition + getChildCount() < state.getItemCount() &&
                        newViewsHeight < mScrollDistance) {
                    View v = recycler.getViewForPosition(mFirstPosition + getChildCount());
                    final int top = getChildAt(getChildCount() - 1).getBottom();
                    addView(v);
                    measureChild(v, 0, 0);
                    final int bottom = top + v.getMeasuredHeight();
                    v.layout(left, top, right, bottom);
                    if (v.isFocusable()) {
                        toFocus = v;
                        break;
                    }
                }
            }

            return toFocus;
        }

        public void recycleViewsOutOfBounds(RecyclerView.Recycler recycler) {
            final int childCount = getChildCount();
            final int parentWidth = getWidth();
            final int parentHeight = getHeight();
            boolean foundFirst = false;
            int first = 0;
            int last = 0;
            for (int i = 0; i < childCount; i++) {
                final View v = getChildAt(i);
                if (v.hasFocus() || (v.getRight() >= 0 && v.getLeft() <= parentWidth &&
                        v.getBottom() >= 0 && v.getTop() <= parentHeight)) {
                    if (!foundFirst) {
                        first = i;
                        foundFirst = true;
                    }
                    last = i;
                }
            }
            for (int i = childCount - 1; i > last; i--) {
                removeAndRecycleViewAt(i, recycler);
            }
            for (int i = first - 1; i >= 0; i--) {
                removeAndRecycleViewAt(i, recycler);
            }
            if (getChildCount() == 0) {
                mFirstPosition = 0;
            } else {
                mFirstPosition += first;
            }
        }

        @Override
        public void onItemsAdded(RecyclerView recyclerView, int positionStart, int itemCount) {
            if (positionStart < mFirstPosition) {
                mFirstPosition += itemCount;
            }
        }

        @Override
        public void onItemsRemoved(RecyclerView recyclerView, int positionStart, int itemCount) {
            if (positionStart < mFirstPosition) {
                mFirstPosition -= itemCount;
            }
        }
    }

    class MyAdapter extends RecyclerView.Adapter {
        private int mBackground;
        List<String> mData;
        ArrayMap<String, Boolean> mSelected = new ArrayMap<String, Boolean>();
        ArrayMap<String, Boolean> mExpanded = new ArrayMap<String, Boolean>();

        public MyAdapter(List<String> data) {
            TypedValue val = new TypedValue();
            AnimatedRecyclerView.this.getTheme().resolveAttribute(
                    R.attr.selectableItemBackground, val, true);
            mBackground = val.resourceId;
            mData = data;
            for (String itemText : mData) {
                mSelected.put(itemText, Boolean.FALSE);
                mExpanded.put(itemText, Boolean.FALSE);
            }
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            MyViewHolder h = new MyViewHolder(getLayoutInflater().inflate(R.layout.selectable_item,
                    null));
            h.textView.setMinimumHeight(128);
            h.textView.setFocusable(true);
            h.textView.setBackgroundResource(mBackground);
            return h;
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            String itemText = mData.get(position);
            ((MyViewHolder) holder).textView.setText(itemText);
            ((MyViewHolder) holder).expandedText.setText("More text for the expanded version");
            boolean selected = false;
            if (mSelected.get(itemText) != null) {
                selected = mSelected.get(itemText);
            }
            ((MyViewHolder) holder).checkBox.setChecked(selected);
            Boolean expanded = mExpanded.get(itemText);
            if (expanded != null && expanded) {
                ((MyViewHolder) holder).expandedText.setVisibility(View.VISIBLE);
                ((MyViewHolder) holder).textView.setVisibility(View.GONE);
            } else {
                ((MyViewHolder) holder).expandedText.setVisibility(View.GONE);
                ((MyViewHolder) holder).textView.setVisibility(View.VISIBLE);
            }
        }

        @Override
        public int getItemCount() {
            return mData.size();
        }

        public void selectItem(String itemText, boolean selected) {
            mSelected.put(itemText, selected);
        }

        public void selectItem(MyViewHolder holder, boolean selected) {
            mSelected.put((String) holder.textView.getText(), selected);
        }

        public void toggleExpanded(MyViewHolder holder) {
            String text = (String) holder.textView.getText();
            mExpanded.put(text, !mExpanded.get(text));
        }
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView expandedText;
        public TextView textView;
        public CheckBox checkBox;

        public MyViewHolder(View v) {
            super(v);
            expandedText = (TextView) v.findViewById(R.id.expandedText);
            textView = (TextView) v.findViewById(R.id.text);
            checkBox = (CheckBox) v.findViewById(R.id.selected);
        }

        @Override
        public String toString() {
            return super.toString() + " \"" + textView.getText() + "\"";
        }
    }
}
