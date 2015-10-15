package com.gcode.notes.activities.display;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.gcode.notes.R;
import com.gcode.notes.activities.compose.ComposeListActivity;
import com.gcode.notes.adapters.ListItemAdapter;
import com.gcode.notes.data.ListData;
import com.gcode.notes.data.ListDataItem;
import com.gcode.notes.extras.Constants;
import com.gcode.notes.extras.MyDebugger;
import com.gcode.notes.serialization.Serializer;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

public class DisplayListActivity extends AppCompatActivity {
    @Bind(R.id.display_list_toolbar)
    Toolbar mToolbar;

    @Bind(R.id.display_list_title_text_view)
    TextView mTitleTextView;

    @Bind(R.id.display_list_recycler_view)
    RecyclerView mRecyclerView;

    @Bind(R.id.display_list_ticked_recycler_view)
    RecyclerView mTickedRecyclerView;

    @Bind(R.id.reminder_text_view)
    TextView mReminderTextView;

    @Bind(R.id.attributes_divider)
    View mAttributesDividerView;

    ListData mListData;

    ListItemAdapter mAdapter;
    ListItemAdapter mTickedAdapter;

    ArrayList<ListDataItem> mListDataItems;
    ArrayList<ListDataItem> mTickedListDataItems;

    Intent mResultIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_list);
        ButterKnife.bind(this);
        mResultIntent = new Intent();
        setupToolbar();
        setupStartState();
    }

    private void setupStartState() {
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {
            String serializedListData = extras.getString(Constants.EXTRA_LIST_DATA);
            if (serializedListData != null) {
                mListData = Serializer.parseListData(serializedListData);
                displayListData();
            }
        }
    }

    private void displayListData() {
        if (mListData != null) {
            mListData.displayBase(mTitleTextView, mReminderTextView);
            if (mListDataItems == null || mTickedListDataItems == null) {
                //displayListData for first time
                setupRecyclerViews();
            } else {
                //onItemAdded existing
                mListDataItems.clear();
                mTickedListDataItems.clear();

                fillListDataItemLists();

                mAdapter.notifyDataSetChanged();
                mTickedAdapter.notifyDataSetChanged();
            }
        }
    }

    private void setupRecyclerViews() {
        mListDataItems = new ArrayList<>();
        mTickedListDataItems = new ArrayList<>();

        fillListDataItemLists();

        mRecyclerView.setLayoutManager(new org.solovyev.android.views.llm.LinearLayoutManager(this));
        mTickedRecyclerView.setLayoutManager(new org.solovyev.android.views.llm.LinearLayoutManager(this));

        mAdapter = new ListItemAdapter(this, mListDataItems, null, Constants.CALLED_FROM_DISPLAY);
        mTickedAdapter = new ListItemAdapter(this, mTickedListDataItems, null, Constants.CALLED_FROM_DISPLAY);
        mTickedAdapter.setTickedModeOn();

        mAdapter.setOtherAdapter(mTickedAdapter);
        mTickedAdapter.setOtherAdapter(mAdapter);

        mRecyclerView.setAdapter(mAdapter);
        mTickedRecyclerView.setAdapter(mTickedAdapter);

    }

    private void fillListDataItemLists() {
        for (ListDataItem listDataItem : mListData.getList()) {
            if (!listDataItem.isChecked()) {
                mListDataItems.add(listDataItem);
            } else {
                mTickedListDataItems.add(listDataItem);
            }
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
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_display_list, menu);
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case Constants.COMPOSE_NOTE_REQUEST_CODE:
                if (resultCode == Activity.RESULT_OK) {
                    if (data != null) {
                        if (data.getBooleanExtra(Constants.NOTE_UPDATED_SUCCESSFULLY, false)) {
                            int mode = data.getIntExtra(Constants.COMPOSE_NOTE_MODE, Constants.ERROR);
                            if (mode != Constants.ERROR) {
                                String serializedListData = data.getStringExtra(Constants.EXTRA_LIST_DATA);
                                if (serializedListData != null) {
                                    ListData listData = Serializer.parseListData(serializedListData);
                                    if (listData != null) {
                                        mListData = listData;
                                        displayListData();
                                    }
                                }
                            } else {
                                MyDebugger.log("onActivityResult() DisplayList mode ERROR!");
                            }
                        }
                    }
                }
                break;

        }
    }

    @Override
    public void onBackPressed() {
        buildResultIntent();
        setResult(Activity.RESULT_OK, mResultIntent);
        super.onBackPressed();
    }

    private void buildResultIntent() {
        mListData.getList().clear();
        mListData.getList().addAll(mListDataItems);
        mListData.getList().addAll(mTickedListDataItems);
        mResultIntent.putExtra(Constants.EXTRA_LIST_DATA, Serializer.serializeListData(mListData));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id) {
            case R.id.action_settings:
                return true;
            case android.R.id.home:
                buildResultIntent();
                setResult(Activity.RESULT_OK, mResultIntent);
                finish();
                return true;
            case R.id.action_edit:
                Intent intent = new Intent(this, ComposeListActivity.class);
                intent.putExtra(Constants.EXTRA_LIST_DATA, Serializer.serializeListData(mListData));
                startActivityForResult(intent, Constants.COMPOSE_NOTE_REQUEST_CODE);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
