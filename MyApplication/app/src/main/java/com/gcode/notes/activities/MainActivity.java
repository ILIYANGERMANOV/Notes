package com.gcode.notes.activities;

import android.animation.ObjectAnimator;
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
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.Toast;

import com.gcode.notes.R;
import com.gcode.notes.extras.Constants;
import com.gcode.notes.extras.Keys;
import com.gcode.notes.extras.Utils;
import com.oguzdev.circularfloatingactionmenu.library.FloatingActionMenu;
import com.oguzdev.circularfloatingactionmenu.library.SubActionButton;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    @Bind(R.id.toolbar)
    Toolbar mToolbar;

    @Bind(R.id.drawer_layout)
    DrawerLayout mDrawerLayout;

    @Bind(R.id.navigation_drawer)
    NavigationView mDrawer;

    public static FloatingActionButton fab;

    private ActionBarDrawerToggle mDrawerToggle;
    private int mSelectedId = R.id.navigation_item_1;

    public static FloatingActionMenu mActionMenu;

    private boolean mSubMenuOpened;

    SubActionButton textNoteButton;
    SubActionButton reminderButton;
    SubActionButton voiceButton;
    SubActionButton cameraButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        handleScreenRotation(savedInstanceState);

        setupToolbar();
        setupNavigationDrawer();
        setupFloatingActionButtonMenu();
    }

    private void handleScreenRotation(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            mSelectedId = savedInstanceState.getInt(Keys.STATE_SELECTED_POSITION);
            mSubMenuOpened = savedInstanceState.getBoolean(Keys.STATE_SUB_MENU_OPENED);
        }
    }

    private void setupFloatingActionButtonMenu() {
        fab = (FloatingActionButton) findViewById(R.id.fab);

        SubActionButton.Builder itemBuilder = new SubActionButton.Builder(this);

        if (Build.VERSION.SDK_INT >= 21) {
            itemBuilder.setBackgroundDrawable(fab.getBackground());
        } else {
            itemBuilder.setBackgroundDrawable(ContextCompat.getDrawable(this, R.drawable.sub_menu_background_selector));
        }

        ImageView textNoteIcon = new ImageView(this);
        textNoteIcon.setImageResource(R.drawable.ic_note_add_white_24dp);
        textNoteButton = itemBuilder.setContentView(textNoteIcon).build();

        ImageView reminderIcon = new ImageView(this);
        reminderIcon.setImageResource(R.drawable.ic_access_alarms_white_24dp);
        reminderButton = itemBuilder.setContentView(reminderIcon).build();

        ImageView voiceIcon = new ImageView(this);
        voiceIcon.setImageResource(R.drawable.ic_keyboard_voice_white_24dp);
        voiceButton = itemBuilder.setContentView(voiceIcon).build();

        ImageView cameraIcon = new ImageView(this);
        cameraIcon.setImageResource(R.drawable.ic_photo_camera_white_24dp);
        cameraButton = itemBuilder.setContentView(cameraIcon).build();

        mActionMenu = new FloatingActionMenu.Builder(this)
                .addSubActionView(textNoteButton)
                .addSubActionView(reminderButton)
                .addSubActionView(voiceButton)
                .addSubActionView(cameraButton)
                .attachTo(fab)
                .build();

        mActionMenu.setStateChangeListener(new FloatingActionMenu.MenuStateChangeListener() {
            @Override
            public void onMenuOpened(FloatingActionMenu floatingActionMenu) {
                Animation openRotateAnimation = AnimationUtils.loadAnimation(MainActivity.this, R.anim.open_rotate_anim);
                fab.setImageResource(R.drawable.ic_close_white_24dp);
                fab.startAnimation(openRotateAnimation);
                mSubMenuOpened = true;
            }

            @Override
            public void onMenuClosed(FloatingActionMenu floatingActionMenu) {
                Animation closeRotateAnimation = AnimationUtils.loadAnimation(MainActivity.this, R.anim.close_rotate_anim);
                fab.setImageResource(R.drawable.ic_open_white_24dp);
                fab.startAnimation(closeRotateAnimation);
                mSubMenuOpened = false;
            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
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
        mSelectedId = menuItem.getItemId();

        switch (mSelectedId) {
            case R.id.navigation_item_1:
                hideDrawer();
                Toast.makeText(this, "item 1 selected", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.navigation_item_2:
                hideDrawer();
                Toast.makeText(this, "item 2 selected", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.navigation_item_3:
                hideDrawer();
                Toast.makeText(this, "item 3 selected", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return true;
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
}
