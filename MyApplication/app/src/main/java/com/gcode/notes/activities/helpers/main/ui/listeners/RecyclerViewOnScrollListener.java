package com.gcode.notes.activities.helpers.main.ui.listeners;

import android.support.v7.widget.RecyclerView;

import com.gcode.notes.controllers.BaseController;
import com.gcode.notes.extras.values.Constants;
import com.github.clans.fab.FloatingActionMenu;

public class RecyclerViewOnScrollListener extends RecyclerView.OnScrollListener {
    FloatingActionMenu mFabMenu;

    public RecyclerViewOnScrollListener(FloatingActionMenu fabMenu) {
        mFabMenu = fabMenu;
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);
        //prevent fab from showing in bin
        if (BaseController.getInstance().getControllerId() == Constants.CONTROLLER_BIN) return;

        if (dy > 0 && !mFabMenu.isMenuButtonHidden()) {
            //scrolling up and button is hidden, show fab menu button
            mFabMenu.hideMenuButton(true);
        } else if (dy < 0 && mFabMenu.isMenuButtonHidden()) {
            //scrolling down and button isn't hidden, hide fab menu button
            mFabMenu.showMenuButton(true);
        }
    }
}
