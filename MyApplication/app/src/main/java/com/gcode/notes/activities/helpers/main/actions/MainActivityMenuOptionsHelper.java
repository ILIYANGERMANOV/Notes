package com.gcode.notes.activities.helpers.main.actions;

import android.view.Menu;
import android.view.MenuItem;

import com.gcode.notes.R;
import com.gcode.notes.activities.MainActivity;
import com.gcode.notes.controllers.BaseController;
import com.gcode.notes.extras.values.Constants;
import com.gcode.notes.ui.ActionExecutor;

public class MainActivityMenuOptionsHelper {
    public static void createOptionsMenu(MainActivity mainActivity, Menu menu) {
        mainActivity.getMenuInflater().inflate(R.menu.menu_main, menu); //adds settings and search action to menu
        mainActivity.mMenu = menu; //get reference for the last created menu, so onPrepareMenuOptions() can be called
        //it needs as argument the last created menu and there is no other way to pass it
    }

    public static void prepareOptionsMenu(Menu menu) {
        if (BaseController.getInstance().getControllerId() == Constants.CONTROLLER_BIN) {
            //we are in bin, add empty bin action
            if (menu.findItem(Constants.MENU_EMPTY_BIN) == null) {
                //empty bin menu action, isn't added, add it (if it is exist it shouldn't be added cuz will duplicate)
                menu.add(0, Constants.MENU_EMPTY_BIN, Menu.NONE, R.string.action_empty_bin).
                        setIcon(R.drawable.ic_empty_bin_24dp).setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
            }
        } else {
            //we are not in bin, remove empty bin action
            menu.removeItem(Constants.MENU_EMPTY_BIN); //no need to check if empty bin items exists,
            //cuz menu#removeItem() handles it
        }
    }

    public static boolean optionsItemSelected(MainActivity mainActivity, MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                return true;
            case R.id.action_search:
                return true;
            case Constants.MENU_EMPTY_BIN:
                ActionExecutor.emptyRecyclerBin(mainActivity);
                return true;
        }
        return false;
    }
}
