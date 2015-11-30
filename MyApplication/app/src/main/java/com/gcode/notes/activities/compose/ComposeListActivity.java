package com.gcode.notes.activities.compose;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import com.gcode.notes.R;
import com.gcode.notes.activities.helpers.compose.list.ComposeListRotationHandler;
import com.gcode.notes.activities.helpers.compose.list.ComposeListSaveHelper;
import com.gcode.notes.activities.helpers.compose.list.ComposeListStartStateHelper;
import com.gcode.notes.adapters.list.compose.ListComposeContainerAdapter;
import com.gcode.notes.adapters.list.compose.TickedListComposeContainerAdapter;
import com.gcode.notes.data.extras.ContentDetails;
import com.gcode.notes.data.extras.ListDataItem;
import com.gcode.notes.data.main.ListData;
import com.gcode.notes.extras.values.Constants;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ComposeListActivity extends ComposeBaseActivity {
    //TODO: REFACTOR AND OPTIMIZE

    @Bind(R.id.compose_list_scroll_view)
    ScrollView mScrollView;

    @Bind(R.id.compose_list_last_divider_view)
    View mLastDividerView;

    @Bind(R.id.compose_list_container_layout)
    LinearLayout mContainer;

    @Bind(R.id.compose_list_container_ticked_layout)
    LinearLayout mTickedContainer;

    public ScrollView getScrollView() {
        return mScrollView;
    }

    public View getLastDividerView() {
        return mLastDividerView;
    }

    public LinearLayout getContainer() {
        return mContainer;
    }

    public LinearLayout getTickedContainer() {
        return mTickedContainer;
    }

    public ListComposeContainerAdapter mContainerAdapter;
    public TickedListComposeContainerAdapter mTickedContainerAdapter;

    public ArrayList<ListDataItem> mListDataItems;
    public ArrayList<ListDataItem> mTickedListDataItems;

    public ListData mListData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compose_list);
        ButterKnife.bind(this);
        setup(savedInstanceState);
    }

    private void setup(Bundle savedInstanceState) {
        super.setup();
        mTitleEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                //if title is focused drop focus from containers
                mContainerAdapter.setLastFocused(Constants.NO_FOCUS);
                mTickedContainerAdapter.setLastFocused(Constants.NO_FOCUS);
            }
        });

        new ComposeListStartStateHelper(this).setupStartState(savedInstanceState);
        //setupStartState(savedInstanceState);
    }


//    private void setupStartState(final Bundle savedInstanceState) {
//        setupContainers();
//        Bundle extras = getIntent().getExtras();
//        if (savedInstanceState == null) {
//            if (extras != null) {
//                //List opened in edit mode
//                mIsOpenedInEditMode = true;
//                setupFromEditMode(extras);
//            } else {
//                //New list add empty item and focus it
//                mIsOpenedInEditMode = false;
//                setupFromZero();
//            }
//        } else {
//            //Saved instance state
//            handlerScreenRotation(savedInstanceState);
//        }
//    }

//    private void setupFromZero() {
//        //cast it to String in order to not be ambiguous
//        mContainerAdapter.addInputItem((String) null, false);
//        mContainerAdapter.setFocusOnChild(0);
//        setupMode();
//    }

//    private void setupFromEditMode(Bundle extras) {
//        ListData listData = Serializer.parseListData(extras.getString(Constants.EXTRA_LIST_DATA));
//        if (listData != null) {
//            mEditNoteId = listData.getId();
//            mEditNoteTargetId = listData.getTargetId();
//            mTitleEditText.setText(listData.getTitle());
//            if (listData.isImportant()) {
//                setStarredState();
//            }
//            if (listData.hasReminder()) {
//                mReminderTextView.setText(listData.getReminder());
//            }
//            mContentDetails = listData.getContentDetails();
//
//
//            ArrayList<ListDataItem> listDataItems = listData.getList();
//            if (listDataItems != null) {
//                addListDataItems(listDataItems);
//            }
//        }
//    }

//    private void handlerScreenRotation(final Bundle savedInstanceState) {
//        //postDelayed because layout isn't loaded and leads to crash
//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                String serializedObject = savedInstanceState.getString(Constants.EXTRA_LIST_DATA_ITEMS);
//                if (serializedObject != null) {
//                    mListDataItems = Serializer.parseListDataItems(serializedObject);
//                    if (mListDataItems != null) {
//                        addListDataItems(mListDataItems);
//                    }
//                }
//
//                serializedObject = savedInstanceState.getString(Constants.EXTRA_TICKED_LIST_DATA_ITEMS);
//                if (serializedObject != null) {
//                    mTickedListDataItems = Serializer.parseListDataItems(serializedObject);
//                    if (mTickedListDataItems != null) {
//                        addListDataItems(mTickedListDataItems);
//                    }
//                }
//                //WARNING: lastFocused MUST BE lastFocused -= 1
//                int lastFocused = savedInstanceState.getInt(Constants.EXTRA_LAST_FOCUSED, Constants.NO_FOCUS);
//                if (Math.abs(lastFocused) - 1 != Constants.NO_FOCUS) {
//                    if (lastFocused > 0) {
//                        //focused from mContainerAdapter
//                        mContainerAdapter.setFocusOnChild(lastFocused - 1);
//                    } else {
//                        //focused from mTickedContainerAdapter (lastFocused is passed negated)
//                        mTickedContainerAdapter.setFocusOnChild(lastFocused * -1 - 1);
//                    }
//                } else {
//                    //request focus on title
//                    mTitleEditText.requestFocus();
//                }
//            }
//        }, 20);
//
//        mIsOpenedInEditMode = savedInstanceState.getBoolean(Constants.EXTRA_IS_OPENED_IN_EDIT_MODE);
//        if (mIsOpenedInEditMode) {
//            mEditNoteId = savedInstanceState.getInt(Constants.EXTRA_EDIT_NOTE_ID);
//            mEditNoteTargetId = savedInstanceState.getInt(Constants.EXTRA_EDIT_NOTE_TARGET_ID);
//        }
//        if (savedInstanceState.getBoolean(Constants.EXTRA_IS_STARRED)) {
//            setStarredState();
//        }
//        mNoteModeChanged = savedInstanceState.getBoolean(Constants.EXTRA_NOTE_MODE_CHANGED);
//        mContentDetails = Serializer.parseContentDetails(savedInstanceState.getString(Constants.EXTRA_CONTENT_DETAILS));
//    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        ComposeListRotationHandler.saveInstanceState(this, outState);
    }

    public void addListDataItems(ArrayList<ListDataItem> listDataItems) {
        for (ListDataItem item : listDataItems) {
            if (!item.isChecked()) {
                mContainerAdapter.addInputItem(item.getContent(), false);
            } else {
                mTickedContainerAdapter.addInputItem(item.getContent(), false);
            }
        }
    }

    public void setupContainers() {
        if (mContainerAdapter == null || mTickedContainerAdapter == null) {
            mContainerAdapter = new ListComposeContainerAdapter(mContainer, mScrollView, mLastDividerView);

            mTickedContainerAdapter = new TickedListComposeContainerAdapter(mTickedContainer, mScrollView);

            mContainerAdapter.setOtherContainerAdapter(mTickedContainerAdapter);
            mTickedContainerAdapter.setOtherContainerAdapter(mContainerAdapter);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        mListDataItems = mContainerAdapter.getListDataItems(false);
        mTickedListDataItems = mTickedContainerAdapter.getListDataItems(false);
    }


//    private void setupMode() {
//        switch (BaseController.getInstance().getControllerId()) {
//            case Constants.CONTROLLER_ALL_NOTES:
//
//                break;
//            case Constants.CONTROLLER_IMPORTANT:
//                setStarredState();
//                break;
//            case Constants.CONTROLLER_PRIVATE:
//                //TODO: set star image button to lock or sth
//                break;
//            default:
//                break;
//        }
//    }

    @OnClick(R.id.compose_list_add_list_item_text_view)
    public void addListInputItem() {
        //cast it to String in order to not be ambiguous
        mContainerAdapter.addInputItem((String) null, true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_compose_list, menu);
        return true;
    }

//    private void saveList() {
//        Intent resultIntent = new Intent();
//
//        String title = mTitleEditText.getText().toString();
//        ArrayList<ListDataItem> listDataItems = mContainerAdapter.getListDataItems(true);
//        listDataItems.addAll(mTickedContainerAdapter.getListDataItems(true));
//
//        if (isValidList(title, listDataItems)) {
//            int mode = mIsStarred ? Constants.MODE_IMPORTANT : Constants.MODE_NORMAL;
//            String reminderString = mReminderTextView.getText().toString();
//            if (reminderString.equals(getResources().getString(R.string.compose_note_set_reminder_text))) {
//                reminderString = Constants.NO_REMINDER;
//            }
//
//            ListData listData = new ListData(title, mode, listDataItems.size() > 0, listDataItems, reminderString);
//
//            if (!mIsOpenedInEditMode) {
//                //save new list
//                if (MyApplication.getWritableDatabase().insertNote(listData) != Constants.DATABASE_ERROR) {
//                    resultIntent.putExtra(Constants.NOTE_ADDED_SUCCESSFULLY, true);
//                    resultIntent.putExtra(Constants.COMPOSE_NOTE_MODE, mode);
//                } else {
//                    MyDebugger.log("Failed to save list.");
//                }
//            } else {
//                //update existing list
//                listData.setId(mEditNoteId);
//                listData.setTargetId(mEditNoteTargetId);
//                if (mContentDetails != null) {
//                    mContentDetails.setLastModifiedDate(DateUtils.getCurrentTimeSQLiteFormatted());
//                    listData.setContentDetails(mContentDetails);
//                } else {
//                    MyDebugger.log("ContentDetails are null");
//                }
//                if (MyApplication.getWritableDatabase().updateNote(listData)) {
//                    resultIntent.putExtra(Constants.NOTE_UPDATED_SUCCESSFULLY, true);
//                    resultIntent.putExtra(Constants.EXTRA_LIST_DATA, Serializer.serializeListData(listData));
//                    resultIntent.putExtra(Constants.EXTRA_NOTE_MODE_CHANGED, mNoteModeChanged);
//                } else {
//                    MyDebugger.log("Failed to update list.");
//                }
//            }
//        } else {
//            MyDebugger.toast(this, "Cannot save empty list.");
//        }
//        setResult(Activity.RESULT_OK, resultIntent);
//    }
//
//    private boolean isValidList(String title, ArrayList<ListDataItem> listDataItems) {
//        return title.trim().length() > 0 || listDataItems.size() > 0;
//    }

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
                ComposeListSaveHelper.saveList(this);
                finish();
        }

        return super.onOptionsItemSelected(item);
    }


}
