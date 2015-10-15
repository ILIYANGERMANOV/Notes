package com.gcode.notes.activities.compose;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
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
import com.gcode.notes.adapters.custom.ListInputContainerAdapter;
import com.gcode.notes.adapters.custom.ListInputTickedContainerAdapter;
import com.gcode.notes.controllers.BaseController;
import com.gcode.notes.data.ListData;
import com.gcode.notes.data.ListDataItem;
import com.gcode.notes.extras.Constants;
import com.gcode.notes.extras.MyDebugger;
import com.gcode.notes.notes.MyApplication;
import com.gcode.notes.serialization.Serializer;

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
    TextView mReminderTextView;

    @Bind(R.id.compose_list_container_layout)
    LinearLayout mContainer;

    @Bind(R.id.compose_list_container_ticked_layout)
    LinearLayout mTickedContainer;

    ListInputContainerAdapter mContainerAdapter;
    ListInputTickedContainerAdapter mTickedContainerAdapter;

    ArrayList<ListDataItem> mListDataItems;
    ArrayList<ListDataItem> mTickedListDataItems;

    boolean mIsOpenedInEditMode;
    int mEditNoteId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compose_list);
        ButterKnife.bind(this);

        setupToolbar();
        setupStartState(savedInstanceState);
    }

    private void setupStartState(final Bundle savedInstanceState) {
        setupContainers();
        mTitleEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                //if title is focused drop focus from containers
                mContainerAdapter.setLastFocused(Constants.NO_FOCUS);
                mTickedContainerAdapter.setLastFocused(Constants.NO_FOCUS);
            }
        });
        Bundle extras = getIntent().getExtras();
        if (savedInstanceState == null) {
            if (extras != null) {
                //List opened in edit mode
                mIsOpenedInEditMode = true;
                setupFromEditMode(extras);
            } else {
                //New list add empty item and focus it
                mIsOpenedInEditMode = false;
                setupFromZero();
            }
        } else {
            //Saved instance state
            handlerScreenRotation(savedInstanceState);
        }
    }

    private void setupFromZero() {
        mContainerAdapter.addInputItem(null, false);
        mContainerAdapter.setFocusOnChild(0);
        setupMode();
    }

    private void setupFromEditMode(Bundle extras) {
        ListData listData = Serializer.parseListData(extras.getString(Constants.EXTRA_LIST_DATA));
        if (listData != null) {
            mEditNoteId = listData.getId();
            mTitleEditText.setText(listData.getTitle());
            mPrioritySwitch.setChecked(listData.isImportant());
            String reminderString = listData.getReminderString();
            if (!reminderString.equals(Constants.NO_REMINDER)) {
                mReminderTextView.setText(reminderString);
            }
            ArrayList<ListDataItem> listDataItems = listData.getList();
            if (listDataItems != null) {
                addListDataItems(listDataItems);
            }
        }
    }

    private void handlerScreenRotation(final Bundle savedInstanceState) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                String serializedObject = savedInstanceState.getString(Constants.EXTRA_LIST_DATA_ITEMS);
                if (serializedObject != null) {
                    mListDataItems = Serializer.parseListDataItems(serializedObject);
                    if (mListDataItems != null) {
                        addListDataItems(mListDataItems);
                    }
                }

                serializedObject = savedInstanceState.getString(Constants.EXTRA_TICKED_LIST_DATA_ITEMS);
                if (serializedObject != null) {
                    mTickedListDataItems = Serializer.parseListDataItems(serializedObject);
                    if (mTickedListDataItems != null) {
                        addListDataItems(mTickedListDataItems);
                    }
                }
                //WARNING: lastFocused MUST BE lastFocused -= 1
                int lastFocused = savedInstanceState.getInt(Constants.EXTRA_LAST_FOCUSED, Constants.NO_FOCUS);
                if (Math.abs(lastFocused) - 1 != Constants.NO_FOCUS) {
                    if (lastFocused > 0) {
                        //focused from mContainerAdapter
                        mContainerAdapter.setFocusOnChild(lastFocused - 1);
                    } else {
                        //focused from mTickedContainerAdapter (lastFocused is passed negated)
                        mTickedContainerAdapter.setFocusOnChild(lastFocused * -1 - 1);
                    }
                } else {
                    //request focus on title
                    mTitleEditText.requestFocus();
                }
            }
        }, 20);
        mIsOpenedInEditMode = savedInstanceState.getBoolean(Constants.EXTRA_IS_OPENED_IN_EDIT_MODE);
        if (mIsOpenedInEditMode) {
            mEditNoteId = savedInstanceState.getInt(Constants.EXTRA_EDIT_NOTE_ID);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        mListDataItems = mContainerAdapter.getListDataItems(false);
        mTickedListDataItems = mTickedContainerAdapter.getListDataItems(false);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mListDataItems != null) {
            outState.putString(Constants.EXTRA_LIST_DATA_ITEMS,
                    Serializer.serializeListDataItems(mListDataItems));
        } else {
            outState.putString(Constants.EXTRA_LIST_DATA_ITEMS,
                    Serializer.serializeListDataItems(mContainerAdapter.getListDataItems(false)));
        }
        if (mTickedListDataItems != null) {
            outState.putString(Constants.EXTRA_TICKED_LIST_DATA_ITEMS,
                    Serializer.serializeListDataItems(mTickedListDataItems));
        } else {
            outState.putString(Constants.EXTRA_TICKED_LIST_DATA_ITEMS,
                    Serializer.serializeListDataItems(mTickedContainerAdapter.getListDataItems(false)));
        }
        int lastFocused = mContainerAdapter.getLastFocused();
        if (lastFocused != Constants.NO_FOCUS) {
            //adding 1 in order to escape problems with id 0
            outState.putInt(Constants.EXTRA_LAST_FOCUSED, lastFocused + 1);
        } else {
            lastFocused = mTickedContainerAdapter.getLastFocused();
            if (lastFocused != Constants.NO_FOCUS) {
                //if last focused is from ticked items its passed negated
                //adding 1 in order to escape problems with id 0
                outState.putInt(Constants.EXTRA_LAST_FOCUSED, (lastFocused + 1) * -1);
            }
        }
        outState.putBoolean(Constants.EXTRA_IS_OPENED_IN_EDIT_MODE, mIsOpenedInEditMode);
        if (mIsOpenedInEditMode) {
            outState.putInt(Constants.EXTRA_EDIT_NOTE_ID, mEditNoteId);
        }
    }

    private void addListDataItems(ArrayList<ListDataItem> listDataItems) {
        for (ListDataItem item : listDataItems) {
            if (!item.isChecked()) {
                mContainerAdapter.addInputItem(item.getContent(), false);
            } else {
                mTickedContainerAdapter.addInputItem(item.getContent(), false);
            }
        }
    }

    private void setupContainers() {
        if (mContainerAdapter == null || mTickedContainerAdapter == null) {
            mContainerAdapter = new ListInputContainerAdapter(mContainer, mScrollView);

            mTickedContainerAdapter = new ListInputTickedContainerAdapter(mTickedContainer, mScrollView);

            mContainerAdapter.setOtherContainerAdapter(mTickedContainerAdapter);
            mTickedContainerAdapter.setOtherContainerAdapter(mContainerAdapter);
        }
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
        mContainerAdapter.addInputItem(null, true);
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
        ArrayList<ListDataItem> listDataItems = mContainerAdapter.getListDataItems(true);
        listDataItems.addAll(mTickedContainerAdapter.getListDataItems(true));

        if (isValidList(title, listDataItems)) {
            int mode = mPrioritySwitch.isChecked() ? Constants.MODE_IMPORTANT : Constants.MODE_NORMAL;
            String reminderString = mReminderTextView.getText().toString();
            if (reminderString.equals(getResources().getString(R.string.compose_note_set_reminder_text))) {
                reminderString = Constants.NO_REMINDER;
            }

            ListData listData = new ListData(title, mode, listDataItems.size() > 0, listDataItems, reminderString);

            if (!mIsOpenedInEditMode) {
                //save new list
                if (MyApplication.getWritableDatabase().insertNote(listData) != Constants.DATABASE_ERROR) {
                    mResultIntent.putExtra(Constants.NOTE_ADDED_SUCCESSFULLY, true);
                    mResultIntent.putExtra(Constants.COMPOSE_NOTE_MODE, mode);
                }
            } else {
                //update existing list
                listData.setId(mEditNoteId);
                if (MyApplication.getWritableDatabase().updateNote(listData)) {
                    mResultIntent.putExtra(Constants.NOTE_UPDATED_SUCCESSFULLY, true);
                    mResultIntent.putExtra(Constants.EXTRA_LIST_DATA, Serializer.serializeListData(listData));
                    mResultIntent.putExtra(Constants.COMPOSE_NOTE_MODE, mode);
                } else {
                    MyDebugger.log("Failed to update note.");
                }
            }
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
