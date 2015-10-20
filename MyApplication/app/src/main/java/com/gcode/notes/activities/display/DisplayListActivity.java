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
import com.gcode.notes.serialization.Serializer;
import com.gcode.notes.tasks.UpdateListAttributesTask;

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

    ListData mListData;

    ListItemAdapter mAdapter;
    ListItemAdapter mTickedAdapter;

    ArrayList<ListDataItem> mListDataItems;
    ArrayList<ListDataItem> mTickedListDataItems;

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
        }
    }

    private void setupFromBundle(Bundle bundle) {
        String serializedListData = bundle.getString(Constants.EXTRA_LIST_DATA);
        if (serializedListData != null) {
            mListData = Serializer.parseListData(serializedListData);
            displayListData();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(Constants.EXTRA_LIST_DATA, Serializer.serializeListData(mListData));
    }

    private void displayListData() {
        if (mListData != null) {
            mListData.displayBase(mTitleTextView);
            if (mListDataItems == null || mTickedListDataItems == null) {
                //displayListData for first time
                setupRecyclerViews();
            } else {
                //displayExisting existing
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

        mAdapter = new ListItemAdapter(this, mListDataItems, null,
                mRecyclerView, Constants.CALLED_FROM_DISPLAY);

        mTickedAdapter = new ListItemAdapter(this, mTickedListDataItems, null,
                mTickedRecyclerView, Constants.CALLED_FROM_DISPLAY);
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
                            String serializedListData = data.getStringExtra(Constants.EXTRA_LIST_DATA);
                            if (serializedListData != null) {
                                ListData listData = Serializer.parseListData(serializedListData);
                                if (listData != null) {
                                    mListData = listData;
                                    displayListData();
                                }
                            }
                        }
                    }
                }
                break;

        }
    }

    @Override
    public void onBackPressed() {
        saveChanges();
        super.onBackPressed();
    }

    private void saveChanges() {
        //create result intent
        Intent resultIntent = new Intent();
        mListData.getList().clear();
        mListData.getList().addAll(mListDataItems);
        mListData.getList().addAll(mTickedListDataItems);
        resultIntent.putExtra(Constants.EXTRA_LIST_DATA, Serializer.serializeListData(mListData));
        setResult(Activity.RESULT_OK, resultIntent);

        if (mListData.hasAttributes()) {
            //save done/undone changes to database
            new UpdateListAttributesTask().execute(mListData);
        }
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
                saveChanges();
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
