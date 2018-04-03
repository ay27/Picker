package org.bitman.ay27.view.feed;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;
import butterknife.ButterKnife;
import butterknife.InjectView;
import com.google.gson.reflect.TypeToken;
import org.bitman.ay27.PickerWidget.FloatingButton;
import org.bitman.ay27.R;
import org.bitman.ay27.common.ContentType;
import org.bitman.ay27.module.Section;
import org.bitman.ay27.request.ListModuleQueryRequest;
import org.bitman.ay27.request.UrlGenerator;
import org.bitman.ay27.request.Urls;
import org.bitman.ay27.swipe_back.app.SwipeBackActivity;
import org.bitman.ay27.view.adapter.SectionAdapter;
import org.bitman.ay27.view.writer.WriteActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Proudly to use Intellij IDEA.
 * Created by ay27 on 14-7-25.
 */
public class FeedActivity extends SwipeBackActivity {


    public static final int TAB_NUM = 3;

    @InjectView(R.id.question_note_activity_pager)
    ViewPager pager;
    @InjectView(R.id.question_note_activity_view_indicator)
    FragmentIndicator indicator;
    @InjectView(R.id.question_note_activity_radio_group)
    RadioGroup radioGroup;
    @InjectView(R.id.question_note_activity_radio_1)
    RadioButton button1;
    @InjectView(R.id.question_note_activity_radio_2)
    RadioButton button2;
    @InjectView(R.id.question_note_activity_radio_3)
    RadioButton button3;
    @InjectView(R.id.question_note_activity_toolbar)
    Toolbar toolbar;
    @InjectView(R.id.question_note_fragment_float_button)
    FloatingButton floatingButton;

    private FeedPageAdapter adapter;
    private boolean stateFlag = false;
    private long bookID;
    private String bookName;
    private int currentPage = 0;
    private PopupMenu.OnMenuItemClickListener tabClickListener = new PopupMenu.OnMenuItemClickListener() {
        @Override
        public boolean onMenuItemClick(MenuItem item) {
            switch (item.getItemId()) {
                case R.id.menu_question_note_sort_time:
                    adapter.replace(new QuestionFragmentInTimeLine(bookID, bookName, floatingButton), 0);
                    break;
                case R.id.menu_question_note_sort_section:
                    showChapterSelector();
                    adapter.replace(new QuestionFragmentInChapterLine(bookID, bookName, floatingButton), 0);
                    break;
            }
            adapter.notifyDataSetChanged();
//            pager.setCurrentItem(0);
            return true;
        }
    };
    private SectionAdapter sectionAdapter;
    private AlertDialog dialog;
    private AdapterView.OnItemClickListener chapterListListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//            SectionAdapter.Holder holder = (SectionAdapter.Holder) view.getTag();

            dialog.dismiss();

            QuestionFragmentInChapterLine fragment0 = ((QuestionFragmentInChapterLine) adapter.fragments[0]);
            fragment0.fastScroll(sectionAdapter.getStartPage(position), sectionAdapter.getEndPage(position));

        }
    };

    private void showChapterSelector() {

        String url = UrlGenerator.queryBookSectionList(bookID);
        ListModuleQueryRequest.ListRequestCallback<Section> requestFinish = new ListModuleQueryRequest.ListRequestCallback<Section>() {
            @Override
            public void onFinished(List<Section> result) {
                AlertDialog.Builder builder = new AlertDialog.Builder(FeedActivity.this);
                View view = LayoutInflater.from(FeedActivity.this).inflate(R.layout.list, null);
                builder.setTitle("目录");
                builder.setView(view);
                dialog = builder.create();
                ListView listView = (ListView) view.findViewById(R.id.list_list);
                listView.setAdapter(sectionAdapter = new SectionAdapter(FeedActivity.this, new ArrayList<Section>(result)));
                listView.setOnItemClickListener(chapterListListener);
                dialog.show();
            }
        };
        new ListModuleQueryRequest(this, url, null, Urls.KEY_SECTION_LIST, new TypeToken<List<Section>>() {
        }.getType(), requestFinish);


    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.question_note_activity);
        ButterKnife.inject(this);

        getSwipeBackLayout().setEnabled(false);
        getSwipeBackLayout().setEdgeSize(0);

        Intent intent = getIntent();
        bookID = intent.getLongExtra("BookID", -1);
        bookName = intent.getStringExtra("BookName");


        toolbar.setTitleTextColor(Color.WHITE);//设置标题颜色
        toolbar.setTitle(bookName);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(false);
        getSupportActionBar().setHomeButtonEnabled(true);

        initIndicator();
        initRadioGroup();
        initViewPager();

        floatingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(FeedActivity.this, WriteActivity.class);
                intent.putExtra("BookID", bookID);

                switch (currentPage) {
                    case 0:
                        intent.putExtra("Type", ContentType.Question);
                        break;
                    case 1:
                        intent.putExtra("Type", ContentType.Note);
                        break;
                    case 2:
                        intent.putExtra("Type", ContentType.Attachment);
                        break;
                }

                startActivity(intent);
            }
        });
    }


    private void initIndicator() {
        indicator.setPageNum(TAB_NUM);
    }

    private void initRadioGroup() {

        final PopupMenu popupMenu = new PopupMenu(this, button1);
        popupMenu.getMenuInflater().inflate(R.menu.question_note_popup, popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(tabClickListener);

        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pager.setCurrentItem(0);
                popupMenu.show();
            }
        });
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pager.setCurrentItem(1);
            }
        });
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pager.setCurrentItem(2);
            }
        });

        radioGroup.check(R.id.question_note_activity_radio_1);
    }

    private void initViewPager() {
        adapter = new FeedPageAdapter(getSupportFragmentManager());
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
                currentPage = i;

                floatingButton.attachToListView(((ProvideList) (adapter.fragments[i])).getListView());
            }

            @Override
            public void onPageScrollStateChanged(int i) {
                if (i == 1)
                    stateFlag = true;

                if (i == 0 && stateFlag) {
                    stateFlag = false;
                    switch (paged) {
                        case 0:
                            radioGroup.check(R.id.question_note_activity_radio_1);
                            break;
                        case 1:
                            radioGroup.check(R.id.question_note_activity_radio_2);
                            break;
                        case 2:
                            radioGroup.check(R.id.question_note_activity_radio_3);
                            break;
                    }
                }
            }
        };
        pager.setOnPageChangeListener(onPageChangeListener);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.question_note_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent;
        switch (item.getItemId()) {
            case R.id.menu_question_note_search:
                intent = new Intent(this, SearchActivity.class);
                intent.putExtra("BookID", bookID);
                intent.putExtra("BookName", bookName);
                startActivity(intent);
                return true;

            case android.R.id.home:
                super.onBackPressed();
                return true;
        }


        return super.onOptionsItemSelected(item);
    }

    private class FeedPageAdapter extends FragmentStatePagerAdapter {

        private Fragment[] fragments;

        public FeedPageAdapter(FragmentManager fm) {
            super(fm);
            fragments = new Fragment[TAB_NUM];
        }

        @Override
        public Fragment getItem(int arg0) {
            if (fragments[arg0] == null) {
                if (arg0 == 0)
                    fragments[arg0] = new QuestionFragmentInTimeLine(bookID, bookName, floatingButton);
                else if (arg0 == 1)
                    fragments[arg0] = new NoteFragment(bookID, bookName);
                else if (arg0 == 2)
                    fragments[arg0] = new AttachmentFragment(bookName, bookID);
            }
            return fragments[arg0];
        }

        public void replace(Fragment fragment, int position) {
            if (position < 0 || position >= TAB_NUM || fragment == null)
                return;
            fragments[position] = fragment;
        }

        @Override
        public int getCount() {
            return TAB_NUM;
        }

        @Override
        public int getItemPosition(Object object) {
            // TODO Auto-generated method stub
            return PagerAdapter.POSITION_NONE;
        }
    }

}
