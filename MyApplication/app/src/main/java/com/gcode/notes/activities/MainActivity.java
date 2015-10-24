package com.gcode.notes.activities;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.gcode.notes.R;
import com.gcode.notes.activities.compose.ComposeListActivity;
import com.gcode.notes.activities.compose.ComposeNoteActivity;
import com.gcode.notes.activities.extra.ExploreActivity;
import com.gcode.notes.activities.extra.SettingsActivity;
import com.gcode.notes.adapters.MainAdapter;
import com.gcode.notes.animations.MyAnimator;
import com.gcode.notes.controllers.AllNotesController;
import com.gcode.notes.controllers.BaseController;
import com.gcode.notes.controllers.BinController;
import com.gcode.notes.controllers.ImportantController;
import com.gcode.notes.controllers.PrivateController;
import com.gcode.notes.data.ContentBase;
import com.gcode.notes.data.ListData;
import com.gcode.notes.data.NoteData;
import com.gcode.notes.extras.MyDebugger;
import com.gcode.notes.extras.values.Constants;
import com.gcode.notes.extras.values.Keys;
import com.gcode.notes.extras.values.Tags;
import com.gcode.notes.helper.OnStartDragListener;
import com.gcode.notes.helper.SimpleItemTouchHelperCallback;
import com.gcode.notes.serialization.Serializer;
import com.gcode.notes.ui.ActionExecutor;
import com.gcode.notes.ui.NavDrawerHelper;
import com.oguzdev.circularfloatingactionmenu.library.FloatingActionMenu;
import com.oguzdev.circularfloatingactionmenu.library.SubActionButton;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener,
        View.OnClickListener, OnStartDragListener {

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

    SimpleItemTouchHelperCallback mSimpleItemTouchHelperCallback = null;
    private ActionBarDrawerToggle mDrawerToggle;
    private int mSelectedId = R.id.navigation_item_all_notes;
    private boolean mSubMenuOpened;
    private ItemTouchHelper mItemTouchHelper;
    private Menu mMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        handleScreenRotation(savedInstanceState);

        setupToolbar();
        setupNavigationDrawer();
        setupFloatingActionButtonMenu();
        setupRecyclerView();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                applySelectedOption(mSelectedId, false);
            }
        }, 10);
    }

    private void applySelectedOption(int selectedId, boolean notForFirstTime) {
        if (mSelectedId == selectedId && notForFirstTime) return;

        switch (selectedId) {
            case R.id.navigation_item_all_notes:
                mSelectedId = selectedId;
                BaseController.setInstance(new AllNotesController(this, mToolbar, mRecyclerView, mFab, mAppBarLayout));
                break;
            case R.id.navigation_item_important:
                mSelectedId = selectedId;
                BaseController.setInstance(new ImportantController(this, mToolbar, mRecyclerView, mFab, mAppBarLayout));
                break;
            case R.id.navigation_item_private:
                mSelectedId = selectedId;
                BaseController.setInstance(new PrivateController(this, mToolbar, mRecyclerView, mFab, mAppBarLayout));
                break;
            case R.id.navigation_item_bin:
                BaseController.setInstance(new BinController(this, mToolbar, mRecyclerView, mFab, mAppBarLayout));
                mSelectedId = selectedId;
                break;
            case R.id.navigation_item_explore:
                startActivity(new Intent(this, ExploreActivity.class));
                return;
            case R.id.navigation_item_settings:
                startActivity(new Intent(this, SettingsActivity.class));
                return;
            default:
                return;
        }

        BaseController.getInstance().setContent();
        if (mMenu != null) {
            onPrepareOptionsMenu(mMenu);
        }
    }


    private void setupRecyclerView() {
        ArrayList<ContentBase> mNotesList = new ArrayList<>();

        MainAdapter mAdapter = new MainAdapter(this, mRecyclerView, mNotesList, this, mCoordinatorLayout);
        GridLayoutManager mGridLayoutManager = new GridLayoutManager(this, Constants.GRID_COLUMNS_COUNT);
        RecyclerView.ItemAnimator mItemAnimator = new DefaultItemAnimator();

        mRecyclerView.setLayoutManager(mGridLayoutManager);
        mRecyclerView.setItemAnimator(mItemAnimator);
        mRecyclerView.setAdapter(mAdapter);

        mSimpleItemTouchHelperCallback = new SimpleItemTouchHelperCallback(mAdapter);
        mItemTouchHelper = new ItemTouchHelper(mSimpleItemTouchHelperCallback);
        mItemTouchHelper.attachToRecyclerView(mRecyclerView);
    }

    private void handleScreenRotation(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            mSelectedId = savedInstanceState.getInt(Keys.STATE_SELECTED_POSITION);
            mSubMenuOpened = savedInstanceState.getBoolean(Keys.STATE_SUB_MENU_OPENED);
        }
    }


    private void setupFloatingActionButtonMenu() {

        SubActionButton.Builder itemBuilder = new SubActionButton.Builder(this);

        if (Build.VERSION.SDK_INT >= 21) {
            itemBuilder.setBackgroundDrawable(mFab.getBackground());
        } else {
            itemBuilder.setBackgroundDrawable(ContextCompat.getDrawable(this, R.drawable.sub_menu_background_selector));
        }

        ImageView textNoteIcon = new ImageView(this);
        textNoteIcon.setImageResource(R.drawable.ic_note_add_white_24dp);
        SubActionButton textNoteButton = itemBuilder.setContentView(textNoteIcon).build();

        ImageView listIcon = new ImageView(this);
        listIcon.setImageResource(R.drawable.ic_list_white_24dp);
        SubActionButton listButton = itemBuilder.setContentView(listIcon).build();

        ImageView voiceIcon = new ImageView(this);
        voiceIcon.setImageResource(R.drawable.ic_keyboard_voice_white_24dp);
        SubActionButton voiceButton = itemBuilder.setContentView(voiceIcon).build();

        ImageView cameraIcon = new ImageView(this);
        cameraIcon.setImageResource(R.drawable.ic_photo_camera_white_24dp);
        SubActionButton cameraButton = itemBuilder.setContentView(cameraIcon).build();

        textNoteButton.setOnClickListener(this);
        listButton.setOnClickListener(this);
        voiceButton.setOnClickListener(this);
        cameraButton.setOnClickListener(this);

        textNoteButton.setTag(Tags.TAG_TEXT_NOTE);
        listButton.setTag(Tags.TAG_LIST);
        voiceButton.setTag(Tags.TAG_VOICE_NOTE);
        cameraButton.setTag(Tags.TAG_CAMERA);

        mActionMenu = new FloatingActionMenu.Builder(this)
                .addSubActionView(textNoteButton)
                .addSubActionView(listButton)
                .addSubActionView(voiceButton)
                .addSubActionView(cameraButton)
                .attachTo(mFab)
                .build();

        mActionMenu.setStateChangeListener(new FloatingActionMenu.MenuStateChangeListener() {
            @Override
            public void onMenuOpened(FloatingActionMenu floatingActionMenu) {
                mFab.setImageResource(R.drawable.ic_close_white_24dp);
                MyAnimator.startAnimation(MainActivity.this, mFab, R.anim.open_rotate_anim);
                mSubMenuOpened = true;
            }

            @Override
            public void onMenuClosed(FloatingActionMenu floatingActionMenu) {
                mFab.setImageResource(R.drawable.ic_open_white_24dp);
                MyAnimator.startAnimation(MainActivity.this, mFab, R.anim.close_rotate_anim);
                mSubMenuOpened = false;
            }
        });

        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.getTranslationY() > Constants.FAB_THRESHOLD_TRANSLATION_Y) {
                    if (Build.VERSION.SDK_INT >= 21) {
                        ObjectAnimator anim = ObjectAnimator.ofFloat(v, "translationY", v.getTranslationY(), 0);
                        anim.start();
                    }
                    v.setTranslationY(0);
                }
                mActionMenu.toggle(true);
            }
        });

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (mSubMenuOpened && !mActionMenu.isOpen()) {
                    mFab.setTranslationY(0);
                    mActionMenu.open(false);
                }
            }
        }, 50);
    }

    private void setupNavigationDrawer() {
        mDrawer.setNavigationItemSelectedListener(this);
        mDrawerToggle = new ActionBarDrawerToggle(this,
                mDrawerLayout,
                mToolbar,
                R.string.drawer_open,
                R.string.drawer_close
        ) {

            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                super.onDrawerSlide(drawerView, slideOffset);
                if (mActionMenu.isOpen()) {
                    mActionMenu.close(true);
                }
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                if (mActionMenu.isOpen()) {
                    mActionMenu.close(false);
                }
            }
        };


        mDrawerLayout.setDrawerListener(mDrawerToggle);
        mDrawerToggle.syncState();
        if (!NavDrawerHelper.didUserLearnedDrawer()) {
            NavDrawerHelper.showDrawer(mDrawerLayout);
            NavDrawerHelper.markDrawerSeen();
        }
    }

    private void setupToolbar() {
        if (mToolbar != null) {
            setSupportActionBar(mToolbar);
        }
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem menuItem) {
        if (menuItem.getGroupId() == R.id.navigation_group_1) {
            menuItem.setChecked(true);
            NavDrawerHelper.hideDrawer(mDrawerLayout);
        }
        applySelectedOption(menuItem.getItemId(), true);
        return true;
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
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case Constants.COMPOSE_NOTE_REQUEST_CODE:
                    if (data != null) {
                        if (data.getBooleanExtra(Constants.NOTE_ADDED_SUCCESSFULLY, false)) {
                            int mode = data.getIntExtra(Constants.COMPOSE_NOTE_MODE, Constants.ERROR);
                            if (mode != Constants.ERROR) {
                                BaseController.getInstance().onItemAdded(mode);
                            } else {
                                MyDebugger.log("onActivityResult() mode ERROR!");
                            }
                        }
                    }
                    break;
                case Constants.LIST_FROM_DISPLAY_RES_CODE:
                    if (data != null) {
                        String serializedListData = data.getStringExtra(Constants.EXTRA_LIST_DATA);
                        if (serializedListData != null) {
                            ListData listData = Serializer.parseListData(serializedListData);
                            if (listData != null) {
                                BaseController controller = BaseController.getInstance();
                                controller.onItemChanged(listData);
                                if (data.getBooleanExtra(Constants.EXTRA_NOTE_MODE_CHANGED, false)) {
                                    controller.onItemModeChanged(listData);
                                }
                            } else {
                                MyDebugger.log("LIST_FROM_DISPLAY listData is null!");
                            }
                        } else {
                            MyDebugger.log("LIST_FROM_DISPLAY serializedListData is null!");
                        }
                    } else {
                        MyDebugger.log("Result: LIST_FROM_DISPLAY data is null!");
                    }
                    break;
                case Constants.NOTE_FROM_DISPLAY_RES_CODE:
                    if (data != null) {
                        String serializedNoteData = data.getStringExtra(Constants.EXTRA_NOTE_DATA);
                        if (serializedNoteData != null) {
                            NoteData noteData = Serializer.parseNoteData(serializedNoteData);
                            if (noteData != null) {
                                BaseController controller = BaseController.getInstance();
                                controller.onItemChanged(noteData);
                                if (data.getBooleanExtra(Constants.EXTRA_NOTE_MODE_CHANGED, false)) {
                                    controller.onItemModeChanged(noteData);
                                }
                            } else {
                                MyDebugger.log("NOTE_FROM_DISPLAY noteData is null!");
                            }
                        } else {
                            MyDebugger.log("NOTE_FROM_DISPLAY serializedNoteData is null!");
                        }
                    } else {
                        MyDebugger.log("Result: NOTE_FROM_DISPLAY data is null!");
                    }
                    break;
            }
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getTag() == null) return;

        Intent mIntent;

        String tag = (String) v.getTag();
        switch (tag) {
            case Tags.TAG_TEXT_NOTE:
                mIntent = new Intent(this, ComposeNoteActivity.class);
                startActivityForResult(mIntent, Constants.COMPOSE_NOTE_REQUEST_CODE);
                break;
            case Tags.TAG_LIST:
                mIntent = new Intent(this, ComposeListActivity.class);
                startActivityForResult(mIntent, Constants.COMPOSE_NOTE_REQUEST_CODE);
                break;
            case Tags.TAG_VOICE_NOTE:
                MyDebugger.toast(this, "Voice note");
                break;
            case Tags.TAG_CAMERA:
                MyDebugger.toast(this, "Camera");
                break;
        }

        mActionMenu.toggle(false);   //can cause problems with startActivity() before it
    }

    @Override
    public void onStartDrag(RecyclerView.ViewHolder viewHolder) {
        mItemTouchHelper.startDrag(viewHolder);
    }
}
