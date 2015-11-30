package com.gcode.notes.activities.helpers.compose.list;

import android.os.Bundle;
import android.os.Handler;

import com.gcode.notes.activities.compose.ComposeListActivity;
import com.gcode.notes.activities.helpers.compose.base.ComposeBaseRotationHandler;
import com.gcode.notes.data.main.ListData;
import com.gcode.notes.extras.MyDebugger;
import com.gcode.notes.extras.values.Constants;
import com.gcode.notes.serialization.Serializer;

public class ComposeListRotationHandler extends ComposeBaseRotationHandler {
    //TODO: REFACTOR AND OPTIMIZE

    public static void saveInstanceState(ComposeListActivity composeListActivity, Bundle outState) {
        ComposeBaseRotationHandler.saveInstanceState(composeListActivity, outState); //save base
        ListData listData = composeListActivity.mListData;
        if (composeListActivity.mListDataItems != null) {
            listData.setList(composeListActivity.mListDataItems);
        } else {
            listData.setList(composeListActivity.mContainerAdapter.getListDataItems(false));
        }
        if (composeListActivity.mTickedListDataItems != null) {
            listData.addToList(composeListActivity.mTickedListDataItems);
        } else {
            listData.addToList(composeListActivity.mTickedContainerAdapter.getListDataItems(false));
        }
        outState.putString(Constants.EXTRA_LIST_DATA, Serializer.serializeListData(listData));

        int lastFocused = composeListActivity.mContainerAdapter.getLastFocused();
        if (lastFocused != Constants.NO_FOCUS) {
            //adding 1 in order to escape problems with id 0
            outState.putInt(Constants.EXTRA_LAST_FOCUSED, lastFocused + 1);
        } else {
            lastFocused = composeListActivity.mTickedContainerAdapter.getLastFocused();
            if (lastFocused != Constants.NO_FOCUS) {
                //if last focused is from ticked items its passed negated
                //adding 1 in order to escape problems with id 0
                outState.putInt(Constants.EXTRA_LAST_FOCUSED, (lastFocused + 1) * -1);
            }
        }

    }

    public static void handlerScreenRotation(final ComposeListActivity composeListActivity, final Bundle savedInstanceState) {
        ComposeBaseRotationHandler.handlerScreenRotation(composeListActivity, savedInstanceState); //handle base
        final ListData listData = Serializer.parseListData(savedInstanceState.getString(Constants.EXTRA_LIST_DATA));
        if (listData != null) {
            composeListActivity.mListData = listData;//postDelayed because layout isn't loaded and leads to crash
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    composeListActivity.addListDataItems(listData.getList());

                    //WARNING: lastFocused MUST BE lastFocused -= 1
                    int lastFocused = savedInstanceState.getInt(Constants.EXTRA_LAST_FOCUSED, Constants.NO_FOCUS);
                    if (Math.abs(lastFocused) - 1 != Constants.NO_FOCUS) {
                        if (lastFocused > 0) {
                            //focused from mContainerAdapter
                            composeListActivity.mContainerAdapter.setFocusOnChild(lastFocused - 1);
                        } else {
                            //focused from mTickedContainerAdapter (lastFocused is passed negated)
                            composeListActivity.mTickedContainerAdapter.setFocusOnChild(lastFocused * -1 - 1);
                        }
                    } else {
                        //request focus on title
                        composeListActivity.getTitleEditText().requestFocus();
                    }
                }
            }, 20);
        } else {
            MyDebugger.log("handleScreenRotation", "listData is null");
        }
    }
}
