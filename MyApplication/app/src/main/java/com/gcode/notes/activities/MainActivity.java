package com.gcode.notes.activities;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.gcode.notes.R;
import com.gcode.notes.controllers.AllNotesController;
import com.gcode.notes.controllers.BaseController;
import com.gcode.notes.controllers.BinController;
import com.gcode.notes.controllers.ImportantController;
import com.gcode.notes.extras.Constants;
import com.gcode.notes.extras.Keys;
import com.gcode.notes.extras.MyDebugger;
import com.gcode.notes.extras.Tags;
import com.gcode.notes.extras.Utils;
import com.oguzdev.circularfloatingactionmenu.library.FloatingActionMenu;
import com.oguzdev.circularfloatingactionmenu.library.SubActionButton;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {

    @Bind(R.id.toolbar)
    Toolbar mToolbar;

    @Bind(R.id.drawer_layout)
    DrawerLayout mDrawerLayout;

    @Bind(R.id.navigation_drawer)
    NavigationView mDrawer;

    @Bind(R.id.fab)
    FloatingActionButton mFab;

    @Bind(R.id.content_recycler_view)
    RecyclerView mRecyclerView;

    private ActionBarDrawerToggle mDrawerToggle;
    private int mSelectedId = R.id.navigation_item_1;

    public static FloatingActionMenu mActionMenu;

    private boolean mSubMenuOpened;

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
                applySelectedOption(mSelectedId);
            }
        }, 10);
    }

    private void applySelectedOption(int selectedId) {
        BaseController controller = null;

        switch (selectedId) {
            case R.id.navigation_item_1:
                mSelectedId = selectedId;
                controller = new AllNotesController(mToolbar, mRecyclerView);
                break;
            case R.id.navigation_item_2:
                mSelectedId = selectedId;
                controller = new ImportantController(mToolbar, mRecyclerView);
                break;
            case R.id.navigation_item_3:
                mSelectedId = selectedId;
                controller = new BinController(mToolbar, mRecyclerView, mFab);
                break;
            case R.id.navigation_item_4:
                startActivity(new Intent(this, ExploreActivity.class));
                break;
            case R.id.navigation_item_5:
                startActivity(new Intent(this, SettingsActivity.class));
                break;
            default:
                break;
        }

        if (controller != null) {
            mFab.setVisibility(View.VISIBLE);
            controller.setContent();
        }
    }


    private void setupRecyclerView() {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
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
                Animation openRotateAnimation = AnimationUtils.loadAnimation(MainActivity.this, R.anim.open_rotate_anim);
                mFab.setImageResource(R.drawable.ic_close_white_24dp);
                mFab.startAnimation(openRotateAnimation);
                mSubMenuOpened = true;
            }

            @Override
            public void onMenuClosed(FloatingActionMenu floatingActionMenu) {
                Animation closeRotateAnimation = AnimationUtils.loadAnimation(MainActivity.this, R.anim.close_rotate_anim);
                mFab.setImageResource(R.drawable.ic_open_white_24dp);
                mFab.startAnimation(closeRotateAnimation);
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
        if (!didUserLearnedDrawer()) {
            showDrawer();
            markDrawerSeen();
        }
    }

    private boolean didUserLearnedDrawer() {
        return Utils.readFromPreferences(this, Keys.PREF_USER_LEARNED_DRAWER, "false").equals("true");

    }

    private void markDrawerSeen() {
        Utils.saveToPreferences(this, Keys.PREF_USER_LEARNED_DRAWER, "true");
    }

    private void showDrawer() {
        mDrawerLayout.openDrawer(GravityCompat.START);
    }

    private void hideDrawer() {
        mDrawerLayout.closeDrawer(GravityCompat.START);
    }

    private void setupToolbar() {
        if (mToolbar != null) {
            setSupportActionBar(mToolbar);
        }
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem menuItem) {
        menuItem.setChecked(true);
        if (menuItem.getGroupId() == R.id.navigation_group_1) {
            hideDrawer();
        }
        applySelectedOption(menuItem.getItemId());
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
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        switch (id) {
            case R.id.action_settings:
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        if (v.getTag() == null) return;

        String tag = (String) v.getTag();
        switch (tag) {
            case Tags.TAG_TEXT_NOTE:
                MyDebugger.toast(this, "Text note");
                break;
            case Tags.TAG_LIST:
                MyDebugger.toast(this, "List");
                break;
            case Tags.TAG_VOICE_NOTE:
                MyDebugger.toast(this, "Voice note");
                break;
            case Tags.TAG_CAMERA:
                MyDebugger.toast(this, "Camera");
                break;
        }
        mActionMenu.toggle(true);   //can cause problems with startActivity() before it
    }
}
