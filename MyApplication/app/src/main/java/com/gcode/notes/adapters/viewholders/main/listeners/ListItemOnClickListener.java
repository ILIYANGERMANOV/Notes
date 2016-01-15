package com.gcode.notes.adapters.viewholders.main.listeners;


import android.app.Activity;
import android.content.Intent;
import android.view.View;

import com.gcode.notes.activities.display.list.bin.DisplayListBinActivity;
import com.gcode.notes.activities.display.list.editable.DisplayListNormalActivity;
import com.gcode.notes.activities.display.list.editable.DisplayListPrivateActivity;
import com.gcode.notes.controllers.BaseController;
import com.gcode.notes.data.list.ListData;
import com.gcode.notes.extras.MyDebugger;
import com.gcode.notes.extras.values.Constants;
import com.gcode.notes.serialization.Serializer;

public class ListItemOnClickListener extends BaseItemListener implements View.OnClickListener {
    ListData mListData;

    public ListItemOnClickListener(Activity activity, ListData listData) {
        super(activity);
        mListData = listData;
    }

    @Override
    public void onClick(View v) {
        if (mDisabled) return; //if disabled prevent further execution (stop click event)

        Intent intent;
        switch (BaseController.getInstance().getControllerId()) {
            case Constants.CONTROLLER_ALL_NOTES:
            case Constants.CONTROLLER_IMPORTANT:
                intent = new Intent(mActivity, DisplayListNormalActivity.class);
                break;
            case Constants.CONTROLLER_PRIVATE:
                intent = new Intent(mActivity, DisplayListPrivateActivity.class);
                break;
            case Constants.CONTROLLER_BIN:
                intent = new Intent(mActivity, DisplayListBinActivity.class);
                break;
            default:
                MyDebugger.log("ListItemOnClickListener", "invalid controller id");
                return;
        }

        intent.putExtra(Constants.EXTRA_LIST_DATA, Serializer.serializeListData(mListData));
        //MyTransitionHelper.startSharedElementTransitionForResult(mActivity, v, intent, Constants.LIST_FROM_DISPLAY_REQUEST_CODE); transitions disabled due to bug
        mActivity.startActivityForResult(intent, Constants.LIST_FROM_DISPLAY_REQUEST_CODE);
    }
}
