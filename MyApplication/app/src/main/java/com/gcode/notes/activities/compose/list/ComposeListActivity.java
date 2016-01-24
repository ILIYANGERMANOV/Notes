package com.gcode.notes.activities.compose.list;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ScrollView;

import com.gcode.notes.R;
import com.gcode.notes.activities.compose.ComposeBaseActivity;
import com.gcode.notes.activities.compose.list.listeners.TitleEditTextOnFocusChangeListener;
import com.gcode.notes.activities.helpers.compose.ComposeLocationHelper;
import com.gcode.notes.activities.helpers.compose.list.ComposeListMenuOptionsHelper;
import com.gcode.notes.activities.helpers.compose.list.ComposeListRotationHandler;
import com.gcode.notes.activities.helpers.compose.list.ComposeListStartStateHelper;
import com.gcode.notes.adapters.list.compose.ListComposeContainerAdapter;
import com.gcode.notes.adapters.list.compose.TickedListComposeContainerAdapter;
import com.gcode.notes.data.list.ListData;
import com.jmedeisis.draglinearlayout.DragLinearLayout;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ComposeListActivity extends ComposeBaseActivity {
    public ListComposeContainerAdapter mContainerAdapter;
    public TickedListComposeContainerAdapter mTickedContainerAdapter;
    public ListData mListData;
    @Bind(R.id.compose_list_scroll_view)
    ScrollView mScrollView;
    @Bind(R.id.compose_list_last_divider_view)
    View mLastDividerView;
    @Bind(R.id.compose_list_container_layout)
    DragLinearLayout mContainer;
    @Bind(R.id.compose_list_container_ticked_layout)
    DragLinearLayout mTickedContainer;

    public ScrollView getScrollView() {
        return mScrollView;
    }

    public View getLastDividerView() {
        return mLastDividerView;
    }

    public DragLinearLayout getContainer() {
        return mContainer;
    }

    public DragLinearLayout getTickedContainer() {
        return mTickedContainer;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compose_list);
        ButterKnife.bind(this);
        setup(savedInstanceState);
    }

    private void setup(Bundle savedInstanceState) {
        super.setup();
        getTitleEditText().setOnFocusChangeListener(new TitleEditTextOnFocusChangeListener(this));
        new ComposeListStartStateHelper(this).setupStartState(savedInstanceState);
        if (!mIsOpenedInEditMode) {
            //its new note, obtain creation location if possible
            ComposeLocationHelper.getLocation(this);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        ComposeListRotationHandler.saveInstanceState(this, outState);
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @OnClick(R.id.compose_list_add_list_item_text_view)
    public void addListInputItem() {
        //cast it to String in order to not be ambiguous
        mContainerAdapter.addInputItem((String) null, true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_compose_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item) || ComposeListMenuOptionsHelper.optionsItemSelected(this, item);
    }
}