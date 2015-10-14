package com.gcode.notes.activities.display;

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
import com.gcode.notes.data.ListData;
import com.gcode.notes.extras.Constants;
import com.gcode.notes.serialization.Serializer;

import butterknife.Bind;
import butterknife.ButterKnife;

public class DisplayListActivity extends AppCompatActivity {
    @Bind(R.id.display_list_toolbar)
    Toolbar mToolbar;

    @Bind(R.id.display_list_title_text_view)
    TextView mTitleTextView;

    @Bind(R.id.display_list_recycler_view)
    RecyclerView mRecyclerView;

    @Bind(R.id.reminder_text_view)
    TextView mReminderTextView;

    @Bind(R.id.attributes_divider)
    View mAttributesDividerView;

    ListData mListData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_list);
        ButterKnife.bind(this);
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
                display(mListData);
            }
        }
    }

    private void display(ListData listData) {
        if (listData != null) {
            listData.displayList(this, mTitleTextView, mReminderTextView,
                    mAttributesDividerView, mRecyclerView);
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
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id) {
            case R.id.action_settings:
                return true;
            case android.R.id.home:
                finish();
                return true;
            case R.id.action_edit:
                Intent intent = new Intent(this, ComposeListActivity.class);
                intent.putExtra(Constants.EXTRA_LIST_DATA, Serializer.serializeListData(mListData));
                startActivity(intent);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
