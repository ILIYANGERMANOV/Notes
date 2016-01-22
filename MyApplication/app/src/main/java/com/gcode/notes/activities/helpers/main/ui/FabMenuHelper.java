package com.gcode.notes.activities.helpers.main.ui;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.gcode.notes.activities.MainActivity;
import com.gcode.notes.activities.helpers.main.ui.listeners.FabMenuOnMenuToggleListener;
import com.gcode.notes.activities.helpers.main.ui.listeners.FabMenuOnTouchListener;
import com.gcode.notes.adapters.main.viewholders.BaseItemViewHolder;
import com.gcode.notes.adapters.main.viewholders.listeners.BaseItemListener;
import com.github.clans.fab.FloatingActionMenu;

public class FabMenuHelper {
    //TODO: REFACTOR AND OPTIMIZE
    //TODO: fix FAB animations
    //TODO: fix bug when recycler view scroll called programmatically hides the FAB

    public static boolean isOpenStarted = false;
    public static boolean isOpenedConsumed = false;

    public static void setupFabMenu(final MainActivity mainActivity) {
        final FloatingActionMenu fabMenu = mainActivity.getFabMenu(); //reference for easier access

        fabMenu.setOnTouchListener(new FabMenuOnTouchListener(fabMenu)); //closes fab menu on touch

        fabMenu.setOnMenuButtonClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //!NOTE: this check must be before fabMenu#toggle()
                if (!fabMenu.isOpened()) {
                    //menu is closed atm, so after toggle it will start opening, disable listeners
                    setTouchListenerFlagsUp();
                    setRecyclerViewListenersDisabled(mainActivity.getRecyclerView(), true);
                } else {
                    //menu is opened atm, so after click will be closed,
                    // set touch listeners flags so it won't consume the event
                    setTouchListenerFlagsDown();
                }
                fabMenu.toggle(true); //handle fab button click (toggle fab menu)
            }
        });

        if (mainActivity.mFabMenuOpened) {
            //fab menu was open, open it
            fabMenu.open(false); //without animation cuz it should look like it was never closed
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

    /**
     * Sets flags so touch listener will consume the event
     */
    public static void setTouchListenerFlagsUp() {
        isOpenStarted = true;   //sets flag so touch listener will consume the event
        isOpenedConsumed = false; //sets flag so touch listener will consume the event
    }

    /**
     * Sets flags so touch listener won't consume the event
     */
    public static void setTouchListenerFlagsDown() {
        isOpenStarted = false; //sets flag so event in touch listener won't be consumed
        isOpenedConsumed = true; //sets flag so event in touch listener won't be consumed
    }
}
