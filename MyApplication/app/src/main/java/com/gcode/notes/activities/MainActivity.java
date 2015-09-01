package com.gcode.notes.activities;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.Toast;

import com.gcode.notes.R;
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

    private ActionBarDrawerToggle mDrawerToggle;
    private int mSelectedId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        mSelectedId = savedInstanceState == null ? R.id.navigation_item_1 : savedInstanceState.getInt(Keys.STATE_SELECTED_POSITION);

        setupToolbar();
        setupNavigationDrawer();
        setupFloatingActionButtonMenu();
    }

    private void setupFloatingActionButtonMenu() {
        SubActionButton.Builder itemBuilder = new SubActionButton.Builder(this);

        ImageView textNoteIcon = new ImageView(this);
        textNoteIcon.setImageResource(R.drawable.ic_menu_black_24dp);
        SubActionButton textNoteButton = itemBuilder.setContentView(textNoteIcon).build();
        ImageView reminderIcon = new ImageView(this);
        reminderIcon.setImageResource(R.drawable.ic_menu_black_24dp);
        SubActionButton reminderButton = itemBuilder.setContentView(reminderIcon).build();
        ImageView voiceIcon = new ImageView(this);
        voiceIcon.setImageResource(R.drawable.ic_menu_black_24dp);
        SubActionButton voiceButton = itemBuilder.setContentView(voiceIcon).build();
        ImageView cameraIcon = new ImageView(this);
        cameraIcon.setImageResource(R.drawable.ic_menu_black_24dp);
        SubActionButton cameraButton = itemBuilder.setContentView(cameraIcon).build();

        android.support.design.widget.FloatingActionButton fab = (android.support.design.widget.FloatingActionButton)
                findViewById(R.id.fab);

        FloatingActionMenu actionMenu = new FloatingActionMenu.Builder(this)
                .addSubActionView(textNoteButton)
                .addSubActionView(reminderButton)
                .addSubActionView(voiceButton)
                .addSubActionView(cameraButton)
                .attachTo(fab)
                .build();
    }

    private void setupNavigationDrawer() {
        mDrawer.setNavigationItemSelectedListener(this);
        mDrawerToggle = new ActionBarDrawerToggle(this,
                mDrawerLayout,
                mToolbar,
                R.string.drawer_open,
                R.string.drawer_close);
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
