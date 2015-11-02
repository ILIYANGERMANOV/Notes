package com.gcode.notes.activities.helpers.compose;

import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.gcode.notes.R;

public class ComposeToolbarHelper {

    public static void setupToolbar(AppCompatActivity appCompatActivity, Toolbar mToolbar) {
        if (mToolbar != null) {
            appCompatActivity.setSupportActionBar(mToolbar);
            ActionBar actionBar = appCompatActivity.getSupportActionBar();
            if (actionBar != null) {
                actionBar.setHomeButtonEnabled(true);
                actionBar.setDisplayHomeAsUpEnabled(true);
                actionBar.setHomeAsUpIndicator(ContextCompat.getDrawable(appCompatActivity, R.drawable.ic_done_black_24dp));
            }
        }
    }
}
