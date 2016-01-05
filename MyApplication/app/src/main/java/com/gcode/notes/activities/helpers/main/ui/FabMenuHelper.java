package com.gcode.notes.activities.helpers.main.ui;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.gcode.notes.activities.MainActivity;
import com.gcode.notes.activities.helpers.main.ui.listeners.FabMenuOnMenuToggleListener;
import com.gcode.notes.activities.helpers.main.ui.listeners.FabMenuOnTouchListener;
import com.gcode.notes.adapters.viewholders.main.BaseItemViewHolder;
import com.gcode.notes.adapters.viewholders.main.listeners.BaseItemListener;
import com.github.clans.fab.FloatingActionMenu;

public class FabMenuHelper {
    //TODO: REFACTOR
    //TODO: push up FAB when a wild snackbar appears, fix FAB animations
    //TODO: fix bug when recycler view scroll called programmatically hides the FAB

    public static boolean isOpenStarted = false;

    public static void setupFabMenu(final MainActivity mainActivity) {
        final FloatingActionMenu fabMenu = mainActivity.getFabMenu(); //reference for easier access

        fabMenu.setOnTouchListener(new FabMenuOnTouchListener(fabMenu)); //closes fab menu on touch

        fabMenu.setOnMenuButtonClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!fabMenu.isOpened()) {
                    //menu is closed, disable listeners
                    //MyDebugger.log("LISTENERS DISABLED");
                    isOpenStarted = true;
                    setRecyclerViewListenersDisabled(mainActivity.getRecyclerView(), true);
                }
                fabMenu.toggle(true);
            }
        });

        if (mainActivity.mFabMenuOpened) {
            //fab menu was open, open it
            fabMenu.open(false); //w/o animation cuz it should look like it was never closed
        }
        fabMenu.setOnMenuToggleListener(new FabMenuOnMenuToggleListener(mainActivity)); //toggle mFabMenuOpened
    }

    public static void setRecyclerViewListenersDisabled(RecyclerView recyclerView, boolean disabled) {
        for (int i = 0; i < recyclerView.getChildCount(); ++i) {
            View childView = recyclerView.getChildAt(i);
            BaseItemViewHolder baseItemViewHolder = (BaseItemViewHolder) recyclerView.getChildViewHolder(childView);
            BaseItemListener baseItemListener = baseItemViewHolder.getItemBaseListener();
            if (baseItemListener != null) {
                baseItemListener.setDisabled(disabled);
            }
        }
    }
}
