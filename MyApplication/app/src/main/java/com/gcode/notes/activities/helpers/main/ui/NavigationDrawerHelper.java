package com.gcode.notes.activities.helpers.main.ui;

import android.os.Handler;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.View;

import com.gcode.notes.R;
import com.gcode.notes.activities.MainActivity;
import com.gcode.notes.activities.helpers.main.actions.DrawerOptionExecutor;
import com.gcode.notes.extras.values.Constants;
import com.gcode.notes.ui.helpers.NavDrawerHelper;

public class NavigationDrawerHelper {
    MainActivity mMainActivity;
    DrawerOptionExecutor mDrawerOptionExecutor;

    public NavigationDrawerHelper(MainActivity mainActivity, DrawerOptionExecutor drawerOptionExecutor) {
        mMainActivity = mainActivity;
        mDrawerOptionExecutor = drawerOptionExecutor;
    }

    public void setupNavigationDrawer() {
        mMainActivity.getDrawer().setNavigationItemSelectedListener(mDrawerOptionExecutor);
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

        //!NOTE: delay it mainly because BaseController is not set yet and this result to creating
        //dummy instance and second so you user can see the drawer opening for first time
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (!NavDrawerHelper.didUserLearnedDrawer()) {
                    NavDrawerHelper.openDrawer(mMainActivity.getDrawerLayout());
                    NavDrawerHelper.markDrawerSeen();
                }
            }
        }, Constants.DELAY_SO_USER_CAN_SEE);
    }
}
