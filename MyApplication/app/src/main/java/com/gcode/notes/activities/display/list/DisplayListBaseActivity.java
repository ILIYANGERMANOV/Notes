package com.gcode.notes.activities.display.list;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ScrollView;
import android.widget.TextView;

import com.gcode.notes.R;
import com.gcode.notes.activities.helpers.display.DisplayBaseMenuOptionsHelper;
import com.gcode.notes.activities.helpers.display.DisplayToolbarHelper;
import com.gcode.notes.activities.helpers.display.list.base.DisplayListBaseContainersHelper;
import com.gcode.notes.activities.helpers.display.list.base.DisplayListBaseDisplayHelper;
import com.gcode.notes.activities.helpers.display.list.base.DisplayListBaseRotationHandler;
import com.gcode.notes.activities.helpers.display.list.base.DisplayListBaseStartStateHelper;
import com.gcode.notes.activities.helpers.display.list.base.DisplayListBaseTasksHelper;
import com.gcode.notes.adapters.list.display.ListDisplayAdapter;
import com.gcode.notes.adapters.list.display.ListDisplayTickedAdapter;
import com.gcode.notes.data.note.list.ListDataItem;
import com.gcode.notes.data.note.list.ListData;
import com.linearlistview.LinearListView;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DisplayListBaseActivity extends AppCompatActivity {
    @Bind(R.id.display_list_toolbar)
    Toolbar mToolbar;

    @Bind(R.id.display_list_scroll_view)
    ScrollView mRootScrollView;

    @Bind(R.id.display_title_text_view)
    TextView mTitleTextView;

    @Bind(R.id.display_action_image_button)
    ImageButton mActionImageButton;

    @Bind(R.id.display_list_linear_list_view)
    LinearListView mLinearListView;

    @Bind(R.id.display_list_ticked_linear_list_view)
    LinearListView mTickedLinearListView;

    @Bind(R.id.display_list_dates_text_view)
    TextView mDatesTextView;

    @Bind(R.id.display_list_done_button)
    Button mDoneButton;

    //getters for layout components-----------------------------------------------------------------------------------------
    public Toolbar getToolbar() {
        return mToolbar;
    }

    public ScrollView getRootScrollView() {
        return mRootScrollView;
    }

    public TextView getTitleTextView() {
        return mTitleTextView;
    }

    public LinearListView getLinearListView() {
        return mLinearListView;
    }

    public LinearListView getTickedLinearListView() {
        return mTickedLinearListView;
    }

    public TextView getDatesTextView() {
        return mDatesTextView;
    }

    public Button getDoneButton() {
        return mDoneButton;
    }
    //getters for layout components-----------------------------------------------------------------------------------------

    public ListData mListData;

    public Intent mResultIntent = new Intent();

    public ListDisplayAdapter mDisplayAdapter;
    public ListDisplayTickedAdapter mDisplayTickedAdapter;

    public ArrayList<ListDataItem> mListDataItems;
    public ArrayList<ListDataItem> mTickedListDataItems;

    public boolean mIsDoneTasksHidden;
    public boolean mNoteModeChanged;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_list);
        ButterKnife.bind(this);
        setup(savedInstanceState);
    }

    private void setup(Bundle savedInstanceState) {
        DisplayToolbarHelper.setupToolbar(this, mToolbar);
        new DisplayListBaseStartStateHelper(this).setupStartState(savedInstanceState);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        DisplayListBaseRotationHandler.saveInstanceState(this, outState);
    }

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
