package com.gcode.notes.listeners.main;


import android.content.Context;
import android.view.View;

import com.gcode.notes.data.ListData;
import com.gcode.notes.extras.MyDebugger;

public class ListItemOnClickListener implements View.OnClickListener {
    Context mContext;
    ListData mListData;

    public ListItemOnClickListener(Context context, ListData listData) {
        mContext = context;
        mListData = listData;
    }

    @Override
    public void onClick(View v) {
        MyDebugger.log("onClick() list");
    }
}
