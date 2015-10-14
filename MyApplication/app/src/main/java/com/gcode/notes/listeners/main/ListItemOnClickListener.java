package com.gcode.notes.listeners.main;


import android.content.Context;
import android.content.Intent;
import android.view.View;

import com.gcode.notes.activities.display.DisplayListActivity;
import com.gcode.notes.data.ListData;
import com.gcode.notes.extras.Constants;
import com.gcode.notes.serialization.Serializer;

public class ListItemOnClickListener implements View.OnClickListener {
    Context mContext;
    ListData mListData;

    public ListItemOnClickListener(Context context, ListData listData) {
        mContext = context;
        mListData = listData;
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(mContext, DisplayListActivity.class);
        intent.putExtra(Constants.EXTRA_LIST_DATA, Serializer.serializeListData(mListData));
        mContext.startActivity(intent);
    }
}
