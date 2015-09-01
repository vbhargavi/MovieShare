package com.bhargavi.laxmi.movieshare;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;

import com.bhargavi.laxmi.movieshare.adapter.SearchAdapter;
import com.bhargavi.laxmi.movieshare.service.data.Media;


public class HomeActivity extends AppCompatActivity {
    private static final String EXTRA_TYPE = "type";

    private static final int SCREEN_HOME = 1;
    private static final int SCREEN_MOVIES = 2;
    private static final int SCREEN_TV = 3;
    private static final int SCREEN_USER_SHARED = 4;

    private DrawerLayout drawerLayout;
    private TabLayout tabLayout;
    private MenuItem searchMenuItem;
    private ViewPager viewPager;

    private PagerAdapter viewPagerAdapter;

    private int mType = SCREEN_HOME;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        if (savedInstanceState != null) {
            mType = savedInstanceState.getInt(EXTRA_TYPE);
        }
        initToolbar();
        setupDrawerLayout();
        setupTablayout();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt(EXTRA_TYPE, mType);
        super.onSaveInstanceState(outState);
    }

    private void initToolbar() {
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        final ActionBar actionBar = getSupportActionBar();

        if (actionBar != null) {
            actionBar.setHomeAsUpIndicator(R.drawable.ic_action_navigation_menu);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        setTitle(getString(R.string.app_name));
    }

    private void setTitle(String title) {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(title);
        }
    }


    private void setupDrawerLayout() {
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        NavigationView view = (NavigationView) findViewById(R.id.navigation_view);
        view.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                String title = getString(R.string.app_name);
                switch (menuItem.getItemId()) {
                    case R.id.navigation_home:
                        mType = SCREEN_HOME;
                        break;
                    case R.id.navigation_movies:
                        mType = SCREEN_MOVIES;
                        title = getString(R.string.movies);
                        break;
                    case R.id.navigation_tvshows:
                        mType = SCREEN_TV;
                        title = getString(R.string.tvshows);
                        break;
                    case R.id.navigation_myshares:
                        mType = SCREEN_USER_SHARED;
                        title = getString(R.string.favorites);
                        break;
                }
                setTitle(title);
                menuItemSelected();
                menuItem.setChecked(true);
                drawerLayout.closeDrawers();
                return true;
            }
        });
    }

    private void setupTablayout() {
        viewPager = (ViewPager) findViewById(R.id.pager);

        tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        viewPagerAdapter = new PagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(viewPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                MovieShareApplication.eventBus.post(new TabChangedEvent(viewPagerAdapter.getType(position), position));
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        menuItemSelected();
    }

    private void menuItemSelected() {
        tabLayout.removeAllTabs();
        tabLayout.setVisibility(View.VISIBLE);

        switch (mType) {
            case SCREEN_HOME:
                tabLayout.addTab(tabLayout.newTab().setText(R.string.movies));
                tabLayout.addTab(tabLayout.newTab().setText(R.string.tvshows));
                break;
            case SCREEN_MOVIES:
                tabLayout.addTab(tabLayout.newTab().setText(R.string.nowplaying));
                tabLayout.addTab(tabLayout.newTab().setText(R.string.popular));
                tabLayout.addTab(tabLayout.newTab().setText(R.string.toprated));
                break;
            case SCREEN_TV:
                tabLayout.addTab(tabLayout.newTab().setText(R.string.nowaired));
                tabLayout.addTab(tabLayout.newTab().setText(R.string.popular));
                tabLayout.addTab(tabLayout.newTab().setText(R.string.toprated));
                break;
            case SCREEN_USER_SHARED:
                tabLayout.addTab(tabLayout.newTab().setText(R.string.movies));
                tabLayout.addTab(tabLayout.newTab().setText(R.string.tvshows));
                break;
        }

        MovieShareApplication.eventBus.post(new TabChangedEvent(viewPagerAdapter.getType(0), 0));

        viewPagerAdapter.updateCount(tabLayout.getTabCount());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home, menu);
        searchMenuItem = menu.findItem(R.id.search);

        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchMenuItem);
        SearchView.SearchAutoComplete searchAutoComplete = (SearchView.SearchAutoComplete) searchView.findViewById(android.support.v7.appcompat.R.id.search_src_text);

        final SearchAdapter searchAdapter = new SearchAdapter(HomeActivity.this);
        searchAutoComplete.setAdapter(searchAdapter);

        searchAutoComplete.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Media media = searchAdapter.getItem(position);
                Intent intent = new Intent(HomeActivity.this, MediaDetailActivity.class);
                intent.putExtra(MediaDetailActivity.EXTRA_ID, media.getId());
                intent.putExtra(MediaDetailActivity.EXTRA_ISMOVIE, media.isMovie());
                intent.putExtra(MediaDetailActivity.EXTRA_TITLE, media.getMediaName());
                startActivity(intent);
                MenuItemCompat.collapseActionView(searchMenuItem);
            }
        });

        return true;
    }

    @Override
    public void onBackPressed() {
        if (searchMenuItem != null && MenuItemCompat.isActionViewExpanded(searchMenuItem)) {
            MenuItemCompat.collapseActionView(searchMenuItem);
            return;
        }

        super.onBackPressed();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == android.R.id.home) {
            drawerLayout.openDrawer(GravityCompat.START);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private class PagerAdapter extends FragmentStatePagerAdapter {
        private int count;

        public PagerAdapter(FragmentManager fm) {
            super(fm);
            count = tabLayout.getTabCount();
        }

        @Override
        public Fragment getItem(int position) {
            return MediaListFragment.newInstance(getType(position), position, null);
        }

        @Override
        public int getCount() {
            return count;
        }

        public void updateCount(int cnt) {
            count = cnt;
            notifyDataSetChanged();
        }

        public int getType(int position) {
            int type = MediaListFragment.TYPE_NOWPLAYING_TVSHOWS;
            switch (mType) {
                case SCREEN_HOME:
                    if (position == 0) {
                        type = MediaListFragment.TYPE_NOWPLAYING_MOVIES;
                    } else {
                        type = MediaListFragment.TYPE_NOWPLAYING_TVSHOWS;
                    }
                    break;
                case SCREEN_MOVIES:
                    if (position == 0) {
                        type = MediaListFragment.TYPE_NOWPLAYING_MOVIES;
                    } else if (position == 1) {
                        type = MediaListFragment.TYPE_POPULAR_MOVIES;
                    } else if (position == 2) {
                        type = MediaListFragment.TYPE_TOPRATED_MOVIES;
                    }
                    break;
                case SCREEN_TV:
                    if (position == 0) {
                        type = MediaListFragment.TYPE_NOWPLAYING_TVSHOWS;
                    } else if (position == 1) {
                        type = MediaListFragment.TYPE_POPULAR_TVSHOWS;
                    } else if (position == 2) {
                        type = MediaListFragment.TYPE_TOPRATED_TVSHOWS;
                    }
                    break;
                case SCREEN_USER_SHARED:
                    if (position == 0) {
                        type = MediaListFragment.TYPE_USERSHARED_MOVIES;
                    } else if (position == 1) {
                        type = MediaListFragment.TYPE_USERSHARED_TV_SHOWS;
                    }
                    break;
            }
            return type;
        }
    }

    public static class TabChangedEvent {
        public int type;
        public int position;

        public TabChangedEvent(int type, int position) {
            this.type = type;
            this.position = position;
        }
    }
}
