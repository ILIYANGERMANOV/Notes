package com.gcode.notes.activities;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.gcode.notes.R;
import com.gcode.notes.activities.helpers.main.FloatingActionButtonHelper;
import com.gcode.notes.activities.helpers.main.MainRecyclerViewHelper;
import com.gcode.notes.activities.helpers.main.MainActivityResultHandler;
import com.gcode.notes.activities.helpers.main.MainToolbarHelper;
import com.gcode.notes.activities.helpers.main.NavigationDrawerHelper;
import com.gcode.notes.activities.helpers.main.DrawerOptionExecutor;
import com.gcode.notes.controllers.BaseController;
import com.gcode.notes.extras.values.Constants;
import com.gcode.notes.extras.values.Keys;
import com.gcode.notes.helper.SimpleItemTouchHelperCallback;
import com.gcode.notes.tasks.DeleteExpiredNotesTask;
import com.gcode.notes.ui.ActionExecutor;
import com.oguzdev.circularfloatingactionmenu.library.FloatingActionMenu;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {
    public static FloatingActionMenu mActionMenu;

    @Bind(R.id.main_toolbar)
    Toolbar mToolbar;
    @Bind(R.id.main_app_bar_layout)
    AppBarLayout mAppBarLayout;
    @Bind(R.id.main_drawer_layout)
    DrawerLayout mDrawerLayout;
    @Bind(R.id.main_root_coordinator)
    CoordinatorLayout mCoordinatorLayout;
    @Bind(R.id.main_navigation_drawer)
    NavigationView mDrawer;
    @Bind(R.id.main_fab)
    FloatingActionButton mFab;
    @Bind(R.id.main_content_recycler_view)
    RecyclerView mRecyclerView;

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

    public SimpleItemTouchHelperCallback mSimpleItemTouchHelperCallback = null;
    public ActionBarDrawerToggle mDrawerToggle;
    public int mSelectedId = R.id.navigation_item_all_notes;
    public boolean mSubMenuOpened;
    public Menu mMenu;

    //TODO: fix list items aren't updated in very rare cases

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        handleScreenRotation(savedInstanceState);

        setupStartState();

        if (savedInstanceState == null) {
            //app is ran for first time delete expired notes
            new DeleteExpiredNotesTask().execute();
        }
    }

    private void setupStartState() {
        final DrawerOptionExecutor drawerOptionExecutor = new DrawerOptionExecutor(this);
        MainToolbarHelper mainToolbarHelper = new MainToolbarHelper(this);
        NavigationDrawerHelper navigationDrawerHelper = new NavigationDrawerHelper(this, drawerOptionExecutor);
        FloatingActionButtonHelper floatingActionButtonHelper = new FloatingActionButtonHelper(this);
        MainRecyclerViewHelper mainRecyclerViewHelper = new MainRecyclerViewHelper(this);

        mainToolbarHelper.setupToolbar();
        navigationDrawerHelper.setupNavigationDrawer();
        floatingActionButtonHelper.setupFloatingActionButtonMenu();
        mainRecyclerViewHelper.setupRecyclerView();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                drawerOptionExecutor.applySelectedOption(mSelectedId, false);
            }
        }, 10);
    }

    private void handleScreenRotation(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            mSelectedId = savedInstanceState.getInt(Keys.STATE_SELECTED_POSITION);
            mSubMenuOpened = savedInstanceState.getBoolean(Keys.STATE_SUB_MENU_OPENED);
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(Keys.STATE_SELECTED_POSITION, mSelectedId);
        outState.putBoolean(Keys.STATE_SUB_MENU_OPENED, mSubMenuOpened);
    }

    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START) || mActionMenu.isOpen()) {
            if (mActionMenu.isOpen()) {
                mActionMenu.close(true);
            } else {
                mDrawerLayout.closeDrawer(GravityCompat.START);
            }
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        mMenu = menu;
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if (BaseController.getInstance().getControllerId() == Constants.CONTROLLER_BIN) {
            if (menu.findItem(Constants.MENU_EMPTY_BIN) == null) {
                menu.add(0, Constants.MENU_EMPTY_BIN, Menu.NONE, R.string.action_empty_bin).
                        setIcon(R.drawable.ic_empty_bin_24dp).setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
            }
        } else {
            menu.removeItem(Constants.MENU_EMPTY_BIN);
        }
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id) {
            case R.id.action_settings:
                return true;
            case R.id.action_search:
                return true;
            case Constants.MENU_EMPTY_BIN:
                ActionExecutor.emptyRecyclerBin(this);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //TODO: handle take photo result & choose photo result
        if (resultCode == Activity.RESULT_OK && data != null) {
            MainActivityResultHandler.handleResult(requestCode, data);
        }
    }
}