package com.gcode.notes.activities.helpers.main.ui.fab;

import android.os.Handler;
import android.view.View;

import com.gcode.notes.activities.MainActivity;
import com.gcode.notes.activities.helpers.main.actions.FabSubMenuActionHandler;
import com.gcode.notes.activities.helpers.main.ui.fab.builders.FloatingActionMenuBuilder;
import com.gcode.notes.activities.helpers.main.ui.fab.listeners.FabOnClickListener;
import com.gcode.notes.extras.values.Constants;

public class FabHelper implements View.OnClickListener {
    MainActivity mMainActivity;
    //TODO: consider using new FAB Menu library or create own

    public FabHelper(MainActivity mainActivity) {
        mMainActivity = mainActivity;
    }

    public void setupFloatingActionButtonMenu() {
        FloatingActionMenuBuilder.buildFabMenu(this, mMainActivity); //builds fab menu with its sub actions buttons
        //sets listeners on them

        mMainActivity.getFab().setOnClickListener(new FabOnClickListener());

        //postDelayed cuz layout isn't ready yet and leads to crash
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //open fab menu if was open in previous state
                if (MainActivity.mActionMenu != null && mMainActivity.mSubMenuOpened && !MainActivity.mActionMenu.isOpen()) {
                    //mActionMenu is up and its menu should be already opened, open it
                    //this case happens after screen rotation when the menu wasn't closed
                    MainActivity.mActionMenu.open(false);
                }
            }
        }, Constants.MEDIUM_DELAY);
    }

    @Override
    public void onClick(View v) {
        //fab action menu item clicked, handle it
        FabSubMenuActionHandler.handleItemClick(mMainActivity, v);
    }
}
