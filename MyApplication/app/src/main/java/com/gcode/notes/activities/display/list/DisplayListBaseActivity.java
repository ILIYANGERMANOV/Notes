package com.gcode.notes.activities.display.list;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ScrollView;

import com.gcode.notes.R;
import com.gcode.notes.activities.display.DisplayBaseActivity;
import com.gcode.notes.activities.helpers.display.DisplayBaseMenuOptionsHelper;
import com.gcode.notes.activities.helpers.display.list.DisplayListBaseContainersHelper;
import com.gcode.notes.activities.helpers.display.list.DisplayListBaseDisplayHelper;
import com.gcode.notes.activities.helpers.display.list.DisplayListBaseRotationHandler;
import com.gcode.notes.activities.helpers.display.list.DisplayListBaseStartStateHelper;
import com.gcode.notes.activities.helpers.display.list.DisplayListBaseTasksHelper;
import com.gcode.notes.adapters.list.display.ListDisplayAdapter;
import com.gcode.notes.adapters.list.display.ListDisplayTickedAdapter;
import com.gcode.notes.data.list.ListData;
import com.gcode.notes.data.list.ListDataItem;
import com.linearlistview.LinearListView;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DisplayListBaseActivity extends DisplayBaseActivity {
    public ListData mListData;
    public Intent mResultIntent = new Intent();
    public ListDisplayAdapter mDisplayAdapter;
    public ListDisplayTickedAdapter mDisplayTickedAdapter;
    public ArrayList<ListDataItem> mListDataItems;
    public ArrayList<ListDataItem> mTickedListDataItems;
    public boolean mIsDoneTasksHidden;

    @Bind(R.id.display_list_scroll_view)
    ScrollView mRootScrollView;
    @Bind(R.id.display_list_linear_list_view)
    LinearListView mLinearListView;
    @Bind(R.id.display_list_ticked_linear_list_view)
    LinearListView mTickedLinearListView;

    @Bind(R.id.display_list_done_button)
    Button mDoneButton;

    //getters for layout components-----------------------------------------------------------------------------------------
    public ScrollView getRootScrollView() {
        return mRootScrollView;
    }

    public LinearListView getLinearListView() {
        return mLinearListView;
    }

    public LinearListView getTickedLinearListView() {
        return mTickedLinearListView;
    }

    public Button getDoneButton() {
        return mDoneButton;
    }

    public View getLimitView() {
        if(mListData.hasLocation()) {
            return mLocationLayout;
        } else {
            return mLastModifiedTextView;
        }
    }
    //getters for layout components-----------------------------------------------------------------------------------------

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_list);
        ButterKnife.bind(this);
        setup(savedInstanceState);
    }

    private void setup(Bundle savedInstanceState) {
        setup(); //setups activity's toolbar in DisplayBaseActivity
        new DisplayListBaseStartStateHelper(this).setupStartState(savedInstanceState);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        DisplayListBaseRotationHandler.saveInstanceState(this, outState);
    }

    @SuppressWarnings("unused")
    @OnClick(R.id.display_list_done_button)
    public void doneClicked() {
        if (mIsDoneTasksHidden) {
            //tasks are hidden, show them
            DisplayListBaseTasksHelper.showDoneTasks(this);
        } else {
            //tasks are not hidden, hide them
            DisplayListBaseTasksHelper.hideDoneTasks(this);
        }
    }

    public void displayListData() {
        displayBase(mListData);
        DisplayListBaseDisplayHelper.displayListData(this);
    }

    public void setupLinearListViews(boolean isDeactivated) {
        DisplayListBaseContainersHelper.setupLinearListViews(this, isDeactivated);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item) || DisplayBaseMenuOptionsHelper.optionItemSelected(this, item);
    }
}
