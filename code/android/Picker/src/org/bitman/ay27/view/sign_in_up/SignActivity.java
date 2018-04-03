package org.bitman.ay27.view.sign_in_up;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.Window;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import butterknife.ButterKnife;
import butterknife.InjectView;
import org.bitman.ay27.R;
import org.bitman.ay27.view.feed.FragmentIndicator;

public class SignActivity extends android.support.v4.app.FragmentActivity {

    public static final int TAB_NUM = 2;

    @InjectView(R.id.sign_pager)
    ViewPager pager;
    @InjectView(R.id.sign_view_indicator)
    FragmentIndicator indicator;
    @InjectView(R.id.sign_radio_group)
    RadioGroup radioGroup;
    @InjectView(R.id.sign_radio_1)
    RadioButton button1;
    @InjectView(R.id.sign_radio_2)
    RadioButton button2;

    private PagerAdapter adapter;
    private boolean stateFlag = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.sign_activity);
        ButterKnife.inject(this);

        initIndicator();
        initRadioGroup();
        initViewPager();
    }

    private void initIndicator() {
        indicator.setPageNum(TAB_NUM);
    }

    private void initRadioGroup() {


        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pager.setCurrentItem(0);
            }
        });
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pager.setCurrentItem(1);
            }
        });

        radioGroup.check(button1.getId());
    }

    private void initViewPager() {
        adapter = new SignAdapter(getSupportFragmentManager());
        pager.setAdapter(adapter);
        pager.setOffscreenPageLimit(TAB_NUM);
        pager.setCurrentItem(0);
        ViewPager.OnPageChangeListener onPageChangeListener = new ViewPager.OnPageChangeListener() {

            private int paged = 0;

            @Override
            public void onPageScrolled(int i, float v, int i1) {
                indicator.draw(i, i1);
            }

            @Override
            public void onPageSelected(int i) {
                paged = i;
            }

            @Override
            public void onPageScrollStateChanged(int i) {
                if (i == 1)
                    stateFlag = true;

                if (i == 0 && stateFlag) {
                    stateFlag = false;
                    switch (paged) {
                        case 0:
                            radioGroup.check(button1.getId());
                            break;
                        case 1:
                            radioGroup.check(button2.getId());
                            break;
                    }
                }
            }
        };
        pager.setOnPageChangeListener(onPageChangeListener);
    }

    private class SignAdapter extends FragmentPagerAdapter {

        private Fragment[] fragments;

        public SignAdapter(FragmentManager fm) {
            super(fm);
            fragments = new Fragment[TAB_NUM];
        }

        @Override
        public Fragment getItem(int arg0) {
            if (fragments[arg0] == null) {
                if (arg0 == 0)
                    fragments[arg0] = new SignInPage();
                else if (arg0 == 1)
                    fragments[arg0] = new SignUpPage();
            }
            return fragments[arg0];
        }

        @Override
        public int getCount() {
            return TAB_NUM;
        }
    }
}
