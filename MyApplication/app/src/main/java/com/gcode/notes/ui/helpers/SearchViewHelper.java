package com.gcode.notes.ui.helpers;

import com.gcode.notes.activities.MainActivity;

public class SearchViewHelper {
    public static void closeSearchView(MainActivity mainActivity) {
        if (isSearchViewOpened(mainActivity)) {
            //closes search view and resets its query
            mainActivity.mSearchView.setQuery("", false); //false cuz must not be submitted
            mainActivity.mSearchView.setIconified(true);
        }
    }

    public static boolean isSearchViewOpened(MainActivity mainActivity) {
        return mainActivity.mSearchView != null && !mainActivity.mSearchView.isIconified();
    }
}
