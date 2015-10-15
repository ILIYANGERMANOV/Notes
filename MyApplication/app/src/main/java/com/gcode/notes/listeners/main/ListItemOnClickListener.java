package com.gcode.notes.listeners.main;


import android.app.Activity;
import android.content.Intent;
import android.view.View;

import com.gcode.notes.activities.display.DisplayListActivity;
import com.gcode.notes.data.ListData;
import com.gcode.notes.extras.Constants;
import com.gcode.notes.serialization.Serializer;

public class ListItemOnClickListener implements View.OnClickListener {
    Activity mActivity;
    ListData mListData;

    public ListItemOnClickListener(Activity activity, ListData listData) {
        mActivity = activity;
        mListData = listData;
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(mActivity, DisplayListActivity.class);
        intent.putExtra(Constants.EXTRA_LIST_DATA, Serializer.serializeListData(mListData));
        mActivity.startActivityForResult(intent, Constants.LIST_FROM_DISPLAY_RES_CODE);
    }
}
