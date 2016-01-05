package com.gcode.notes.activities.helpers.main.ui.listeners;

import android.support.v7.widget.RecyclerView;

import com.github.clans.fab.FloatingActionMenu;

public class RecyclerViewOnScrollListener extends RecyclerView.OnScrollListener {
    //TODO: REFACTOR AND OPTIMIZE (has serious performance issues)
    FloatingActionMenu mFabMenu;

    public RecyclerViewOnScrollListener(FloatingActionMenu fabMenu) {
        mFabMenu = fabMenu;
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);

        if (dy > 0) {
            //MyDebugger.log("scrolling up");
            //scrolling up and button is hidden, show fab menu button
            mFabMenu.hideMenuButton(true);
        } else if (dy < 0) {
            //MyDebugger.log("scrolling down");
            //scrolling down and button isn't hidden, hide fab menu button
            mFabMenu.showMenuButton(true);
        }
    }
}
