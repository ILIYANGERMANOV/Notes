package com.gcode.notes.activities;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.gcode.notes.R;
import com.gcode.notes.activities.helpers.main.MainActivityResultHandler;
import com.gcode.notes.activities.helpers.main.MainActivityRotationHandler;
import com.gcode.notes.activities.helpers.main.ReminderNotificationStartHelper;
import com.gcode.notes.activities.helpers.main.actions.DrawerOptionExecutor;
import com.gcode.notes.activities.helpers.main.actions.MainActivityBackPressHelper;
import com.gcode.notes.activities.helpers.main.actions.MainActivityMenuOptionsHelper;
import com.gcode.notes.activities.helpers.main.ui.fab.FabHelper;
import com.gcode.notes.activities.helpers.main.ui.MainRecyclerViewHelper;
import com.gcode.notes.activities.helpers.main.ui.MainToolbarHelper;
import com.gcode.notes.activities.helpers.main.ui.NavigationDrawerHelper;
import com.gcode.notes.extras.MyDebugger;
import com.gcode.notes.extras.values.Constants;
import com.gcode.notes.helper.SimpleItemTouchHelperCallback;
import com.oguzdev.circularfloatingactionmenu.library.FloatingActionMenu;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {
    public static FloatingActionMenu mActionMenu;
    public SimpleItemTouchHelperCallback mSimpleItemTouchHelperCallback = null;
    public ActionBarDrawerToggle mDrawerToggle;
    public int mSelectedId = R.id.navigation_item_all_notes;
    public boolean mSubMenuOpened;
    public Menu mMenu;

    @Bind(R.id.main_toolbar)
    Toolbar mToolbar;
    @Bind(R.id.main_app_bar_layout)
    AppBarLayout mAppBarLayout;
    @Bind(R.id.main_drawer_layout)
    DrawerLayout mDrawerLayout;
    @Bind(R.id.main_coordinator)
    CoordinatorLayout mCoordinatorLayout;
    @Bind(R.id.main_navigation_drawer)
    NavigationView mDrawer;
    @Bind(R.id.main_fab)
    FloatingActionButton mFab;
    @Bind(R.id.main_content_recycler_view)
    RecyclerView mRecyclerView;
    @Bind(R.id.main_notes_recycler_view_empty_text_view)
    TextView mRecyclerViewEmptyView;

    //getters for layout components------------------------------------------------------------------------------------------
    public AppBarLayout getAppBarLayout() {
        return mAppBarLayout;
    }

    public CoordinatorLayout getCoordinatorLayout() {
        return mCoordinatorLayout;
    }

    public RecyclerView getRecyclerView() {
        return mRecyclerView;
    }

    public Toolbar getToolbar() {
        return mToolbar;
    }

    public NavigationView getDrawer() {
        return mDrawer;
    }

    public DrawerLayout getDrawerLayout() {
        return mDrawerLayout;
    }

    public FloatingActionButton getFab() {
        return mFab;
    }

    public TextView getRecyclerViewEmptyView() {
        return mRecyclerViewEmptyView;
    }
    //getters for layout components------------------------------------------------------------------------------------------

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        MainActivityRotationHandler.handleScreenRotation(this, savedInstanceState); //also deletes expired notes
        // if app is ran for first time

        setup();

        //!NOTE: must be called after handleScreenRotation(), cuz there getIntent() is set correctly,
        //else will result in bug (opening always display activity on screen rotation)
        ReminderNotificationStartHelper.handleIfStartedFromReminderNotifcation(this, getIntent());
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        ReminderNotificationStartHelper.handleIfStartedFromReminderNotifcation(this, intent);
    }

    private void setup() {
        final DrawerOptionExecutor drawerOptionExecutor = new DrawerOptionExecutor(this);

        new MainToolbarHelper(this).setupToolbar();
        new NavigationDrawerHelper(this, drawerOptionExecutor).setupNavigationDrawer();
        new FabHelper(this).setupFloatingActionButtonMenu();
        new MainRecyclerViewHelper(this).setupRecyclerView();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //delay it, cuz otherwise will result in crash (layout is not ready)
                drawerOptionExecutor.applySelectedOption(mSelectedId, false);
            }
        }, Constants.MINIMUM_DELAY);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        MainActivityRotationHandler.saveInstanceState(this, outState);
    }

    @Override
    public void onBackPressed() {
        if (MainActivityBackPressHelper.handleBackPressedWhenDrawerOrFABMenuOpened(this)) {
            //returns true, when moveTaskToBack() should be called
            moveTaskToBack(false);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MainActivityMenuOptionsHelper.createOptionsMenu(this, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MainActivityMenuOptionsHelper.prepareOptionsMenu(menu);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item) || MainActivityMenuOptionsHelper.optionsItemSelected(this, item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        MainActivityResultHandler.handleResult(this, requestCode, resultCode, data);
    }
}