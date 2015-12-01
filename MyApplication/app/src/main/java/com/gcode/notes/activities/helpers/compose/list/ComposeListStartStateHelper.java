package com.gcode.notes.activities.helpers.compose.list;

import android.os.Bundle;

import com.gcode.notes.activities.compose.list.ComposeListActivity;
import com.gcode.notes.activities.helpers.compose.base.ComposeBaseStartStateHelper;
import com.gcode.notes.data.main.ListData;
import com.gcode.notes.extras.MyDebugger;
import com.gcode.notes.extras.values.Constants;
import com.gcode.notes.serialization.Serializer;

public class ComposeListStartStateHelper extends ComposeBaseStartStateHelper {
    ComposeListActivity mComposeListActivity;

    public ComposeListStartStateHelper(ComposeListActivity composeListActivity) {
        mComposeListActivity = composeListActivity;
    }

    public void setupStartState(Bundle savedInstanceState) {
        ComposeListContainerHelper.setupContainers(mComposeListActivity);
        Bundle extras = mComposeListActivity.getIntent().getExtras();
        if (savedInstanceState == null) {
            if (extras != null) {
                //List opened in edit mode
                setupFromEditMode(extras.getString(Constants.EXTRA_LIST_DATA));
            } else {
                //New list
                setupFromZero();
            }
        } else {
            //Screen rotation
            ComposeListRotationHandler.handlerScreenRotation(mComposeListActivity, savedInstanceState);
        }
    }

    protected void setupFromZero() {
        super.setupFromZero(mComposeListActivity);
        mComposeListActivity.mListData = new ListData();
        //add empty input item and focus it
        mComposeListActivity.mContainerAdapter.addInputItem((String) null, false);
        mComposeListActivity.mContainerAdapter.setFocusOnChild(0);
    }

    private void setupFromEditMode(String serializedListData) {
        ListData listData = Serializer.parseListData(serializedListData);
        if (listData != null) {
            //passed listData is OK, setup the activity from it
            super.setupFromEditMode(mComposeListActivity, listData); //setup base (title, importance, reminder, mOpenedInEditMode)
            mComposeListActivity.mListData = listData;
            if (listData.hasAttachedList()) {
                ComposeListContainerHelper.addListDataItems(mComposeListActivity, listData.getList());
            }
        } else {
            MyDebugger.log("EditMode launched with null list, finish activity in order to prevent errors");
            mComposeListActivity.finish();
        }
    }
}
