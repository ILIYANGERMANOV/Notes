package com.gcode.notes.activities;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.NavigationView;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.gcode.notes.R;
import com.gcode.notes.activities.helpers.main.actions.DrawerOptionExecutor;
import com.gcode.notes.activities.helpers.main.actions.FabMenuActionHandler;
import com.gcode.notes.activities.helpers.main.actions.MainActivityBackPressHelper;
import com.gcode.notes.activities.helpers.main.actions.MainActivityMenuOptionsHelper;
import com.gcode.notes.activities.helpers.main.actions.MainSearchHandler;
import com.gcode.notes.activities.helpers.main.state.MainActivityResultHandler;
import com.gcode.notes.activities.helpers.main.state.MainActivityRotationHandler;
import com.gcode.notes.activities.helpers.main.state.ReminderNotificationStartHelper;
import com.gcode.notes.activities.helpers.main.ui.FabMenuHelper;
import com.gcode.notes.activities.helpers.main.ui.MainRecyclerViewHelper;
import com.gcode.notes.activities.helpers.main.ui.MainToolbarHelper;
import com.gcode.notes.activities.helpers.main.ui.NavigationDrawerHelper;
import com.gcode.notes.adapters.main.MainAdapter;
import com.gcode.notes.extras.utils.PermissionsUtils;
import com.gcode.notes.extras.values.Constants;
import com.gcode.notes.touch.SimpleItemTouchHelperCallback;
import com.github.clans.fab.FloatingActionMenu;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {
    public SimpleItemTouchHelperCallback mSimpleItemTouchHelperCallback = null;
    public ActionBarDrawerToggle mDrawerToggle;
    public int mSelectedId = R.id.navigation_item_all_notes;
    /**
     * The id of nav drawer menu item.
     * WARNING: On first creation equals all_notes id;
     * After bin screen rotation is bin id;
     * In all other cases is the id of the previous different item selected id.
     */
    public int mPreviousSelectedId = R.id.navigation_item_all_notes;
    public boolean mIsFabMenuOpened;
    public Menu mMenu;
    public DrawerOptionExecutor mDrawerOptionExecutor;
    public MainAdapter mMainAdapter;
    public SearchView mSearchView;
    public MainSearchHandler mSearchHandler;
    public PermissionsUtils mPermissionsUtils;
    /**
     * Flag indicating whether the controller should load new content.
     * Used to prevent loading new content when exiting from private label.
     */
    public boolean mLoadNewContentPrivate;
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
    @Bind(R.id.fab_menu)
    FloatingActionMenu mFabMenu;
    @Bind(R.id.main_content_recycler_view)
    RecyclerView mRecyclerView;
    @Bind(R.id.main_notes_recycler_view_empty_text_view)
    TextView mRecyclerViewEmptyView;
    private ReminderNotificationStartHelper mReminderNotificationStartHelper;
    private boolean mFabMenuClickEnabled;

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

    public FloatingActionMenu getFabMenu() {
        return mFabMenu;
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
        mReminderNotificationStartHelper = new ReminderNotificationStartHelper(this);
        mReminderNotificationStartHelper.handleIfStartedFromReminderNotification(getIntent());
    }

    @Override
    protected void onResume() {
        super.onResume();
        mPermissionsUtils.verifyPermissionsChanges();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (mReminderNotificationStartHelper == null) {
            //secure
            mReminderNotificationStartHelper = new ReminderNotificationStartHelper(this);
        }
        mReminderNotificationStartHelper.handleIfStartedFromReminderNotification(intent);
    }

    private void setup() {
        mFabMenuClickEnabled = true; //enable here, so it will be activated after screen rotation
        mDrawerOptionExecutor = new DrawerOptionExecutor(this);

        mPermissionsUtils = new PermissionsUtils(this) {
            @Override
            protected View getRootView() {
                return mCoordinatorLayout;
            }
        };

        new MainToolbarHelper(this).setupToolbar();
        FabMenuHelper.setupFabMenu(this);
        new NavigationDrawerHelper(this, mDrawerOptionExecutor).setupNavigationDrawer();
        new MainRecyclerViewHelper(this).setupRecyclerView();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //delay it, cuz otherwise will result in crash (layout is not ready)
                mDrawerOptionExecutor.applySelectedOption(mSelectedId, false, true);
            }
        }, Constants.MINIMUM_DELAY);
    }

    @Override
    public void startActivityForResult(Intent intent, int requestCode, Bundle options) {
        super.startActivityForResult(intent, requestCode, options);
        overridePendingTransition(R.anim.slide_in_right, 0);
        setListenersForRequestDisabled(requestCode, true);
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
        if (MainActivityBackPressHelper.shouldMoveTaskToBack(this)) {
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
        return super.onOptionsItemSelected(item) ||
                MainActivityMenuOptionsHelper.optionsItemSelected(this, item);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        mPermissionsUtils.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        MainActivityResultHandler.handleResult(this, requestCode, resultCode, data);
        setListenersForRequestDisabled(requestCode, false);
    }

    public void fabMenuItemClicked(View view) {
        if (mFabMenuClickEnabled)
            FabMenuActionHandler.handleItemClick(this, view);
    }

    public void setContentAlpha(float alpha, boolean animate) {
        if (animate) {
            ViewCompat.animate(mRecyclerView).alpha(alpha);
            ViewCompat.animate(mToolbar).alpha(alpha);
        } else {
            mRecyclerView.setAlpha(alpha);
            mToolbar.setAlpha(alpha);
        }
    }

    private void setListenersForRequestDisabled(int requestCode, boolean disabled) {
        switch (requestCode) {
            case Constants.DISPLAY_NOTE_REQUEST_CODE:
            case Constants.DISPLAY_LIST_REQUEST_CODE:
                mMainAdapter.setListenersDisabled(disabled);
                break;
            default:
                mFabMenuClickEnabled = !disabled;
        }
    }
}