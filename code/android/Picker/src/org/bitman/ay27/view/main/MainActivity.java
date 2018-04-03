package org.bitman.ay27.view.main;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.*;
import android.widget.*;
import butterknife.ButterKnife;
import butterknife.InjectView;
import com.beardedhen.androidbootstrap.FontAwesomeText;
import org.bitman.ay27.PickerApplication;
import org.bitman.ay27.R;
import org.bitman.ay27.cache.ImageCacheManager;
import org.bitman.ay27.common.ContentType;
import org.bitman.ay27.module.dp.UserDP;
import org.bitman.ay27.request.UrlGenerator;
import org.bitman.ay27.swipe_back.app.SwipeBackActivity;
import org.bitman.ay27.view.add_book.ScanActivity;
import org.bitman.ay27.view.user.UserInfoActivity;

/**
 * Proudly to use Intellij IDEA.
 * Created by ay27 on 14-7-25.
 */
public class MainActivity extends SwipeBackActivity {

    public static final String[] title = new String[]{"书本", "关注", "消息", "私信", "下载", "搜索", "设置"};
    public static final String[] icon = new String[]{"fa-book", "fa-dot-circle-o", "fa-bell", "fa-envelope", "fa-download", "fa-search", "fa-cog"};
    public static final int[] iconColor = new int[]{android.R.color.holo_green_light, android.R.color.holo_red_light, android.R.color.holo_purple, R.color.blue, R.color._base_holo_blue, android.R.color.holo_blue_dark, android.R.color.black};
    public static final int DrawerItemCount = title.length;
    private static long back_pressed;
    @InjectView(R.id.drawer_layout)
    DrawerLayout drawerLayout;
    @InjectView(R.id.left_drawer)
    LinearLayout drawerLeft;
    @InjectView(R.id.book_manager_toolbar)
    Toolbar toolbar;
    @InjectView(R.id.drawer_activity_left_drawer)
    ListView drawerListView;
    @InjectView(R.id.drawer_user_area)
    View userArea;

    private int currentFragment = 0;

    private ActionBarDrawerToggle drawerToggle;

    private DrawerListAdapter adapter;
    private AdapterView.OnItemClickListener drawerListItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {

            drawerLayout.closeDrawer(drawerLeft);

            Fragment fragment = null;

            currentFragment = position;

            switch (position) {
                case 0:
                    fragment = new BookFragment();
                    break;
                case 1:
                    fragment = new MessageFragment(ContentType.UserDynamic, PickerApplication.getMyUserId());
                    break;
                case 2:
                    fragment = new MessageFragment(ContentType.RelatedMessage, PickerApplication.getMyUserId());
                    break;
                case 3:
                    fragment = new DialogFragment();
                    break;
                case 4:
                    fragment = new FileFragment();
                    break;
                case 5:
                    fragment = new SearchFragment();
                    break;
                case 6:
                    fragment = new Preferences();
                    break;
                default:
                    break;
            }
            if (fragment != null) {
                FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.drawer_activity_content_frame, fragment).commit();
                drawerListView.setItemChecked(position, true);
                setTitle(title[position]);
                drawerLayout.closeDrawer(drawerLeft);

                // the the color
                adapter.resetColor();
                view.setSelected(true);
                ViewHolder holder = (ViewHolder) view.getTag();
                holder.textView.setTextColor(Color.WHITE);
                holder.icon.setTextColor(Color.WHITE);

                return;
            }

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.book_manager_activity);
        ButterKnife.inject(this);

        getSwipeBackLayout().setEnabled(false);
        getSwipeBackLayout().setEdgeSize(0);

        toolbar.setTitleTextColor(Color.WHITE);//设置标题颜色
        setSupportActionBar(toolbar);

        initViews();
    }

    @Override
    protected void onResumeFragments() {
        super.onResumeFragments();
    }

    private void initViews() {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(false);
        getSupportActionBar().setHomeButtonEnabled(true);

        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close);
        drawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
        drawerLayout.setDrawerListener(drawerToggle);

        adapter = new DrawerListAdapter(this);
        drawerListView.setAdapter(adapter);
        drawerListView.setOnItemClickListener(drawerListItemClickListener);
        drawerListView.performItemClick(adapter.getItem(0), 0, 0);

        userArea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, UserInfoActivity.class);
                intent.putExtra("TargetUserID", PickerApplication.getMyUserId());
                startActivity(intent);
            }
        });
        final UserDP myUser = PickerApplication.getMyUser();
        ((TextView) userArea.findViewById(R.id.userarea_name)).setText(myUser.getName());
        ImageCacheManager.loadImage(UrlGenerator.getResourcesUrl(myUser.getAvatarUrl()),
                ImageCacheManager.getImageListener((ImageView) userArea.findViewById(R.id.userarea_imageView), null, null));
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        drawerToggle.syncState();
    }

    @Override
    public void onBackPressed() {
        if (back_pressed + 2000 > System.currentTimeMillis()) super.onBackPressed();
        else Toast.makeText(getBaseContext(), "Press once again to exit!", Toast.LENGTH_SHORT).show();
        back_pressed = System.currentTimeMillis();
    }

//    @Override
//    public boolean onPrepareOptionsMenu(Menu menu) {
//        menu.clear();
//        if (currentFragment == 0) {
//            toolbar.inflateMenu(R.menu.main);
//            return true;
//        }
//        return super.onPrepareOptionsMenu(menu);
//    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Pass the event to ActionBarDrawerToggle, if it returns
        // true, then it has handled the app icon touch event
        if (drawerToggle.onOptionsItemSelected(item)) {
            return true;
        } else
            return super.onOptionsItemSelected(item);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
    }

    static class ViewHolder {
        @InjectView(R.id.drawer_list_item_icon)
        FontAwesomeText icon;
        @InjectView(R.id.drawer_list_item_text)
        TextView textView;

        public ViewHolder(View view) {
            ButterKnife.inject(this, view);
        }
    }


    private class DrawerListAdapter extends BaseAdapter {

        private Context _context;
        private View[] items;

        private DrawerListAdapter(Context _context) {
            this._context = _context;

            items = new View[DrawerItemCount];
            for (int i = 0; i < DrawerItemCount; i++) {
                items[i] = makeItem(title[i], icon[i], iconColor[i]);
            }
        }

        private View makeItem(String title, String iconId, int iconColorResId) {
            LayoutInflater inflater = (LayoutInflater) _context.getSystemService(LAYOUT_INFLATER_SERVICE);
            View root = inflater.inflate(R.layout.drawer_list_item, null);

            ViewHolder holder = new ViewHolder(root);
            holder.textView.setText(title);
            holder.icon.setIcon(iconId);
            holder.icon.setTextColor(getResources().getColor(iconColorResId));
            if (iconId.equals("fa-cog"))
                holder.icon.startRotate(MainActivity.this, true, FontAwesomeText.AnimationSpeed.SLOW);

            root.setTag(holder);

            return root;
        }

        public void resetColor() {
            for (int i = 0; i < items.length; i++) {
                ViewHolder holder = (ViewHolder) items[i].getTag();
                holder.textView.setTextColor(getResources().getColor(R.color.text_black_default_selector));
                holder.icon.setTextColor(getResources().getColor(iconColor[i]));
            }
        }

        @Override
        public int getCount() {
            return DrawerItemCount;
        }

        @Override
        public View getItem(int i) {
            return items[i];
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            if (view == null)
                return items[i];

            return view;
        }
    }
}
