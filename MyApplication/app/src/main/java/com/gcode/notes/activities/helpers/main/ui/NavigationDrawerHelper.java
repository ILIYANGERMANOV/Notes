package com.gcode.notes.activities.helpers.main.ui;

import android.support.design.widget.NavigationView;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.MenuItem;
import android.view.View;

import com.gcode.notes.R;
import com.gcode.notes.activities.MainActivity;
import com.gcode.notes.activities.helpers.main.actions.DrawerOptionExecutor;
import com.gcode.notes.ui.helpers.NavDrawerHelper;

public class NavigationDrawerHelper implements NavigationView.OnNavigationItemSelectedListener {
    MainActivity mMainActivity;
    DrawerOptionExecutor mDrawerOptionExecutor;

    public NavigationDrawerHelper(MainActivity mainActivity, DrawerOptionExecutor drawerOptionExecutor) {
        mMainActivity = mainActivity;
        mDrawerOptionExecutor = drawerOptionExecutor;
    }

    public void setupNavigationDrawer() {
        mMainActivity.getDrawer().setNavigationItemSelectedListener(this);
        mMainActivity.mDrawerToggle = new ActionBarDrawerToggle(mMainActivity,
                mMainActivity.getDrawerLayout(),
                mMainActivity.getToolbar(),
                R.string.drawer_open,
                R.string.drawer_close
        ) {

            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                super.onDrawerSlide(drawerView, slideOffset);
                if (mMainActivity.getFabMenu().isOpened()) {
                    mMainActivity.getFabMenu().close(true);
                }
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                if (mMainActivity.getFabMenu().isOpened()) {
                    mMainActivity.getFabMenu().close(false);
                }
            }
        };


        mMainActivity.getDrawerLayout().setDrawerListener(mMainActivity.mDrawerToggle);
        mMainActivity.mDrawerToggle.syncState();
        if (!NavDrawerHelper.didUserLearnedDrawer()) {
            NavDrawerHelper.openDrawer(mMainActivity.getDrawerLayout());
            NavDrawerHelper.markDrawerSeen();
        }
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem menuItem) {
        if (menuItem.getGroupId() == R.id.navigation_group_1) {
            menuItem.setChecked(true);
            NavDrawerHelper.closeDrawer(mMainActivity.getDrawerLayout());
        }
        mDrawerOptionExecutor.applySelectedOption(menuItem.getItemId(), true);
        return true;
    }
}
