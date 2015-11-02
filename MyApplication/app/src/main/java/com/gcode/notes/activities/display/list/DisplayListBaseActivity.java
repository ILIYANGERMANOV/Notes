package com.gcode.notes.activities.display.list;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ScrollView;
import android.widget.TextView;

import com.gcode.notes.R;
import com.gcode.notes.adapters.list.display.ListDisplayAdapter;
import com.gcode.notes.adapters.list.display.ListDisplayTickedAdapter;
import com.gcode.notes.data.ListData;
import com.gcode.notes.data.ListDataItem;
import com.gcode.notes.extras.values.Constants;
import com.gcode.notes.serialization.Serializer;
import com.linearlistview.LinearListView;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DisplayListBaseActivity extends AppCompatActivity {
    @Bind(R.id.display_list_scroll_view)
    ScrollView mRootScrollView;

    @Bind(R.id.display_action_image_button)
    ImageButton mActionImageButton;

    @Bind(R.id.display_list_dates_text_view)
    TextView mDatesTextView;

    @Bind(R.id.display_list_toolbar)
    Toolbar mToolbar;

    @Bind(R.id.display_title_text_view)
    TextView mTitleTextView;

    @Bind(R.id.display_list_done_button)
    Button mDoneButton;

    @Bind(R.id.display_list_linear_list_view)
    LinearListView mLinearListView;

    @Bind(R.id.display_list_ticked_linear_list_view)
    LinearListView mTickedLinearListView;

    ListData mListData;

    ListDisplayAdapter mDisplayAdapter;
    ListDisplayTickedAdapter mDisplayTickedAdapter;

    ArrayList<ListDataItem> mListDataItems;
    ArrayList<ListDataItem> mTickedListDataItems;

    boolean mIsDoneTasksHidden;
    boolean mNoteModeChanged;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_list);
        ButterKnife.bind(this);
        setupToolbar();
        setupStartState(savedInstanceState);
    }

    private void setupStartState(Bundle savedInstanceState) {
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null && savedInstanceState == null) {
            //first time started
            setupFromBundle(extras);
        } else {
            //from saved instance state
            setupFromBundle(savedInstanceState);
            handleScreenRotation(savedInstanceState);
        }
    }

    protected void handleScreenRotation(Bundle savedInstanceState) {
        if (savedInstanceState.getBoolean(Constants.EXTRA_IS_DONE_HIDDEN)) {
            //apply hide
            hideDoneTasks();
        }
        mNoteModeChanged = savedInstanceState.getBoolean(Constants.EXTRA_NOTE_MODE_CHANGED);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(Constants.EXTRA_LIST_DATA, Serializer.serializeListData(mListData));
        outState.putBoolean(Constants.EXTRA_IS_DONE_HIDDEN, mIsDoneTasksHidden);
        outState.putBoolean(Constants.EXTRA_NOTE_MODE_CHANGED, mNoteModeChanged);
    }

    @OnClick(R.id.display_list_done_button)
    public void doneClicked() {
        if (mIsDoneTasksHidden) {
            showDoneTasks();
        } else {
            hideDoneTasks();
        }
    }

    public void hideDoneTasks() {
        mTickedLinearListView.setVisibility(View.GONE);
        mDoneButton.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_expand_less_black_24dp, 0, 0, 0);
        mIsDoneTasksHidden = true;
        mDisplayTickedAdapter.setDoneHidden(true);
    }

    public void showDoneTasks() {
        mTickedLinearListView.setVisibility(View.VISIBLE);
        mDoneButton.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_expand_more_black_24dp, 0, 0, 0);
        mIsDoneTasksHidden = false;
        mDisplayTickedAdapter.setDoneHidden(false);
    }

    private void setupFromBundle(Bundle bundle) {
        String serializedListData = bundle.getString(Constants.EXTRA_LIST_DATA);
        if (serializedListData != null) {
            mListData = Serializer.parseListData(serializedListData);
            if (mListData != null) {
                displayListData();
            }
        }
    }

    protected void displayListData() {
        mListData.displayBase(mTitleTextView);
        mDatesTextView.setText(mListData.getDateDetails());
        if (mListDataItems == null || mTickedListDataItems == null) {
            //displayListData for first time
            setupLinearListViews(false);
        } else {
            //displayExisting existing
            mListDataItems.clear();
            mTickedListDataItems.clear();

            fillListDataItemLists();

            mDisplayAdapter.notifyDataSetChanged();
            mDisplayTickedAdapter.notifyDataSetChanged();
        }
    }

    protected void setupLinearListViews(boolean isDeactivated) {
        mListDataItems = new ArrayList<>();
        mTickedListDataItems = new ArrayList<>();

        fillListDataItemLists();

        mDisplayAdapter = new ListDisplayAdapter(this, mListDataItems, isDeactivated);
        mDisplayTickedAdapter = new ListDisplayTickedAdapter(this, mTickedListDataItems, isDeactivated,
                mDoneButton, mRootScrollView, mDatesTextView, mTickedLinearListView);

        mLinearListView.setAdapter(mDisplayAdapter);
        mTickedLinearListView.setAdapter(mDisplayTickedAdapter);

        mLinearListView.setOnItemClickListener(new LinearListView.OnItemClickListener() {
            @Override
            public void onItemClick(LinearListView parent, View view, int position, long id) {
                ListDataItem item = mDisplayAdapter.getItem(position);
                mDisplayAdapter.remove(item);
                mDisplayTickedAdapter.add(item);
            }
        });

        mTickedLinearListView.setOnItemClickListener(new LinearListView.OnItemClickListener() {
            @Override
            public void onItemClick(LinearListView parent, View view, int position, long id) {
                ListDataItem item = mDisplayTickedAdapter.getItem(position);
                mDisplayTickedAdapter.remove(item);
                mDisplayAdapter.add(item);
            }
        });
    }

    private void fillListDataItemLists() {
        for (ListDataItem listDataItem : mListData.getList()) {
            if (!listDataItem.isChecked()) {
                mListDataItems.add(listDataItem);
            } else {
                mTickedListDataItems.add(listDataItem);
            }
        }

        if (mTickedListDataItems.isEmpty()) {
            mDoneButton.setVisibility(View.GONE);
        } else {
            mDoneButton.setVisibility(View.VISIBLE);
        }
    }

    private void setupToolbar() {
        if (mToolbar != null) {
            setSupportActionBar(mToolbar);
            ActionBar mActionBar = getSupportActionBar();
            if (mActionBar != null) {
                mActionBar.setHomeButtonEnabled(true);
                mActionBar.setDisplayHomeAsUpEnabled(true);
            }
        }
    }

    @Override
    public void onBackPressed() {
        setResult();
        super.onBackPressed();
    }

    protected void setResult() {
        //collect data from activity current state
        mListData.getList().clear();
        mListData.getList().addAll(mListDataItems);
        mListData.getList().addAll(mTickedListDataItems);
        //create result intent
        Intent resultIntent = new Intent();
        resultIntent.putExtra(Constants.EXTRA_LIST_DATA, Serializer.serializeListData(mListData));
        resultIntent.putExtra(Constants.EXTRA_NOTE_MODE_CHANGED, mNoteModeChanged);
        setResult(Activity.RESULT_OK, resultIntent);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        switch (item.getItemId()) {
            case R.id.action_settings:
                return true;
            case android.R.id.home:
                setResult();
                finish();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
