package com.gcode.notes.activities.helpers.main.ui;

import android.view.View;

import com.gcode.notes.activities.MainActivity;
import com.gcode.notes.activities.helpers.main.ui.listeners.FabMenuOnMenuToggleListener;
import com.gcode.notes.activities.helpers.main.ui.listeners.FabMenuOnTouchListener;
import com.gcode.notes.extras.values.Constants;
import com.github.clans.fab.FloatingActionMenu;

public class FabMenuHelper {
    //TODO: REFACTOR AND OPTIMIZE
    //TODO: fix FAB animations
    //TODO: fix bug when recycler view scroll called programmatically hides the FAB

    public static boolean isOpenStarted = false;
    public static boolean isOpenedConsumed = false;

    public static void setupFabMenu(final MainActivity mainActivity) {
        final FloatingActionMenu fabMenu = mainActivity.getFabMenu(); //reference for easier access

        fabMenu.setOnTouchListener(new FabMenuOnTouchListener(mainActivity)); //closes fab menu on touch

        fabMenu.setOnMenuButtonClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //!NOTE: this check must be before fabMenu#toggle()
                if (!fabMenu.isOpened()) {
                    //menu is closed atm, so after toggle it will start opening, disable listeners
                    setTouchListenerFlagsUp();
                    mainActivity.mMainAdapter.setListenersDisabled(true);
                    mainActivity.setContentAlpha(Constants.VIEWS_FADED_ALPHA, true);
                } else {
                    //menu is opened atm, so after click will be closed,
                    // set touch listeners flags so it won't consume the event
                    setTouchListenerFlagsDown();
                    mainActivity.setContentAlpha(1f, true);
                }
                fabMenu.toggle(true); //handle fab button click (toggle fab menu)
            }
        });

        if (mainActivity.mIsFabMenuOpened) {
            //fab menu was open, open it
            fabMenu.open(false); //without animation cuz it should look like it was never closed
        }
        fabMenu.setOnMenuToggleListener(new FabMenuOnMenuToggleListener(mainActivity)); //toggle mIsFabMenuOpened
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
