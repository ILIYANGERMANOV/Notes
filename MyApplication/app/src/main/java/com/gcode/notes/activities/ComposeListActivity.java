package com.gcode.notes.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.gcode.notes.R;
import com.gcode.notes.adapters.custom.BaseInputContainerAdapter;
import com.gcode.notes.adapters.custom.ListInputInputContainerAdapter;
import com.gcode.notes.adapters.custom.ListInputTickedInputContainerAdapter;
import com.gcode.notes.controllers.BaseController;
import com.gcode.notes.data.ListData;
import com.gcode.notes.data.ListDataItem;
import com.gcode.notes.extras.Constants;
import com.gcode.notes.extras.MyDebugger;
import com.gcode.notes.notes.MyApplication;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ComposeListActivity extends AppCompatActivity {

    @Bind(R.id.compose_list_toolbar)
    Toolbar mToolbar;

    @Bind(R.id.compose_list_scroll_view)
    ScrollView mScrollView;

    @Bind(R.id.compose_list_add_list_item_text_view)
    TextView mAddInputItemTextView;

    @Bind(R.id.compose_note_title_edit_text)
    EditText mTitleEditText;

    @Bind(R.id.compose_note_priority_switch)
    SwitchCompat mPrioritySwitch;

    @Bind(R.id.compose_note_set_reminder_text_view)
    TextView mSetReminderTextView;

    @Bind(R.id.compose_list_container_layout)
    LinearLayout mContainer;

    @Bind(R.id.compose_list_container_ticked_layout)
    LinearLayout mTickedContainer;

    BaseInputContainerAdapter mContainerAdapter;
    BaseInputContainerAdapter mTickedContainerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compose_list);
        //TODO: handle screen rotation
        ButterKnife.bind(this);

        setupToolbar();
        setupContainers();
        setupMode();
    }

    private void setupContainers() {
        mContainerAdapter = new ListInputInputContainerAdapter(mContainer, mScrollView);
        mContainerAdapter.setupContainer(null);

        mTickedContainerAdapter = new ListInputTickedInputContainerAdapter(mTickedContainer, mScrollView);

        mContainerAdapter.setOtherContainerAdapter(mTickedContainerAdapter);
        mTickedContainerAdapter.setOtherContainerAdapter(mContainerAdapter);
    }


    private void setupMode() {
        switch (BaseController.getInstance().getControllerId()) {
            case Constants.CONTROLLER_ALL_NOTES:

                break;
            case Constants.CONTROLLER_IMPORTANT:
                mPrioritySwitch.setChecked(true);
                break;
            case Constants.CONTROLLER_PRIVATE:
                mPrioritySwitch.setVisibility(View.GONE);
                break;
            default:
                break;
        }
    }


    private void setupToolbar() {
        if (mToolbar != null) {
            setSupportActionBar(mToolbar);
            ActionBar mActionBar = getSupportActionBar();
            if (mActionBar != null) {
                mActionBar.setHomeButtonEnabled(true);
                mActionBar.setDisplayHomeAsUpEnabled(true);
                mActionBar.setHomeAsUpIndicator(ContextCompat.getDrawable(this, R.drawable.ic_done_black_24dp));
            }
        }
    }

    @OnClick(R.id.compose_list_add_list_item_text_view)
    public void addListInputItem() {
        mContainerAdapter.addInputItem(null);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_compose_list, menu);
        return true;
    }

    private void saveList() {
        Intent mResultIntent = new Intent();

        String title = mTitleEditText.getText().toString();
        ArrayList<ListDataItem> listDataItems = mContainerAdapter.getListDataItems();

        listDataItems.addAll(mTickedContainerAdapter.getListDataItems());

        if (isValidList(title, listDataItems)) {
            int mode = mPrioritySwitch.isChecked() ? Constants.MODE_IMPORTANT : Constants.MODE_NORMAL;
            String reminderString = mSetReminderTextView.getText().toString();
            if (reminderString.equals(getResources().getString(R.string.compose_note_set_reminder_text))) {
                reminderString = Constants.NO_REMINDER;
            }

            ListData listData = new ListData(title, mode, listDataItems.size() > 0, listDataItems, reminderString);

            if (MyApplication.getWritableDatabase().insertNote(listData) != Constants.DATABASE_ERROR) {
                mResultIntent.putExtra(Constants.NOTE_ADDED_SUCCESSFULLY, true);
                mResultIntent.putExtra(Constants.COMPOSE_NOTE_MODE, mode);
            }
        } else {
            //TODO: handle not valid note
            MyDebugger.toast(this, "Note is not valid!");
        }
        setResult(Activity.RESULT_OK, mResultIntent);
    }

    private boolean isValidList(String title, ArrayList<ListDataItem> listDataItems) {
        return title.trim().length() > 0 || listDataItems.size() > 0;
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
                saveList();
                finish();
        }

        return super.onOptionsItemSelected(item);
    }


}