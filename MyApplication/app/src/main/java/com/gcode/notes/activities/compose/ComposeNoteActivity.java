package com.gcode.notes.activities.compose;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.gcode.notes.R;
import com.gcode.notes.activities.helpers.compose.ComposeNoteResultHandler;
import com.gcode.notes.activities.helpers.compose.ComposeToolbarHelper;
import com.gcode.notes.adapters.note.ComposeNoteImagesAdapter;
import com.gcode.notes.controllers.BaseController;
import com.gcode.notes.data.ContentDetails;
import com.gcode.notes.data.NoteData;
import com.gcode.notes.extras.MyDebugger;
import com.gcode.notes.extras.utils.DateUtils;
import com.gcode.notes.extras.values.Constants;
import com.gcode.notes.notes.MyApplication;
import com.gcode.notes.serialization.Serializer;
import com.gcode.notes.ui.ActionExecutor;
import com.linearlistview.LinearListView;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ComposeNoteActivity extends AppCompatActivity {
    //TODO: REFACTOR AND OPTIMIZE
    @Bind(R.id.compose_note_toolbar)
    Toolbar mToolbar;

    @Bind(R.id.compose_note_title_edit_text)
    EditText mTitleEditText;

    @Bind(R.id.compose_note_linear_list_view)
    LinearListView mLinearListView;

    @Bind(R.id.compose_note_description_edit_text)
    EditText mDescriptionEditText;

    @Bind(R.id.compose_star_image_button)
    ImageButton mStarImageButton;

    @Bind(R.id.compose_note_reminder_text_view)
    TextView mReminderTextView;

    boolean mIsOpenedInEditMode;
    int mEditNoteId;
    int mEditNoteTargetId;

    boolean mIsStarred;
    boolean mNoteModeChanged;
    ContentDetails mContentDetails;

    public static boolean mOpenInGalleryLaunched;

    ComposeNoteImagesAdapter mImagesAdapter;

    public ComposeNoteImagesAdapter getAdapter() {
        return mImagesAdapter;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compose_note);
        ButterKnife.bind(this);
        ComposeToolbarHelper.setupToolbar(this, mToolbar);
        setupStartState(savedInstanceState);
    }

    private void setupStartState(Bundle savedInstanceState) {
        Intent intent = getIntent();
        setupLayout();
        if (savedInstanceState == null) {
            if (intent.getStringExtra(Constants.EXTRA_NOTE_DATA) != null) {
                //Note opened in edit mode
                mIsOpenedInEditMode = true;
                setupFromEditMode(intent.getStringExtra(Constants.EXTRA_NOTE_DATA));
            } else if (intent.getStringExtra(Constants.EXTRA_PHOTO_URI) != null) {
                setupFromPhoto(intent.getStringExtra(Constants.EXTRA_PHOTO_URI));
            } else {
                //New note
                mIsOpenedInEditMode = false;
                setupFromZero();
            }
        } else {
            //Saved instance state
            handlerScreenRotation(savedInstanceState);
        }
    }

    private void setupFromPhoto(String photoUriString) {
        mImagesAdapter.add(photoUriString);
    }

    private void setupLayout() {
        mTitleEditText.setHorizontallyScrolling(false);
        mTitleEditText.setMaxLines(3);

        mImagesAdapter = new ComposeNoteImagesAdapter(this, new ArrayList<String>(), mLinearListView);
        mLinearListView.setAdapter(mImagesAdapter);
        mLinearListView.setVisibility(View.GONE);
    }

    private void setupFromZero() {
        switch (BaseController.getInstance().getControllerId()) {
            case Constants.CONTROLLER_ALL_NOTES:

                break;
            case Constants.CONTROLLER_IMPORTANT:
                setStarredState();
                break;
            case Constants.CONTROLLER_PRIVATE:
                //TODO: PRIVATE: set mStarImageButton to sth
                break;
            default:
                break;
        }
    }

    private void setupFromEditMode(String serializedNoteData) {
        NoteData noteData = Serializer.parseNoteData(serializedNoteData);
        if (noteData != null) {
            mEditNoteId = noteData.getId();
            mEditNoteTargetId = noteData.getTargetId();
            mTitleEditText.setText(noteData.getTitle());
            if (noteData.isImportant()) {
                setStarredState();
            }
            if (noteData.hasReminder()) {
                mReminderTextView.setText(noteData.getReminder());
            }
            mContentDetails = noteData.getContentDetails();
            //TODO: set audio
            if (noteData.hasAttachedImage()) {
                //adapter's list is still empty, no need to clear
                mImagesAdapter.addAll(noteData.getAttachedImagesPaths());
            }
            mDescriptionEditText.setText(noteData.getDescription());
        }
    }

    private void handlerScreenRotation(Bundle savedInstanceState) {
        //TODO: handle screen rotation for audio
        mIsOpenedInEditMode = savedInstanceState.getBoolean(Constants.EXTRA_IS_OPENED_IN_EDIT_MODE);
        if (mIsOpenedInEditMode) {
            mEditNoteId = savedInstanceState.getInt(Constants.EXTRA_EDIT_NOTE_ID);
            mEditNoteTargetId = savedInstanceState.getInt(Constants.EXTRA_EDIT_NOTE_TARGET_ID);
        }
        if (savedInstanceState.getBoolean(Constants.EXTRA_IS_STARRED)) {
            setStarredState();
        }
        mNoteModeChanged = savedInstanceState.getBoolean(Constants.EXTRA_NOTE_MODE_CHANGED);
        mContentDetails = Serializer.parseContentDetails(savedInstanceState.getString(Constants.EXTRA_CONTENT_DETAILS));
        ArrayList<String> attachedImagesList = Serializer.parseStringPathsList(
                savedInstanceState.getString(Constants.EXTRA_ATTACHED_IMAGES_LIST));
        if (attachedImagesList != null) {
            //adapter's list is still empty, no need to clear
            mImagesAdapter.addAll(attachedImagesList);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //TODO: add audio
        outState.putBoolean(Constants.EXTRA_IS_OPENED_IN_EDIT_MODE, mIsOpenedInEditMode);
        if (mIsOpenedInEditMode) {
            outState.putInt(Constants.EXTRA_EDIT_NOTE_ID, mEditNoteId);
            outState.putInt(Constants.EXTRA_EDIT_NOTE_TARGET_ID, mEditNoteTargetId);
        }
        outState.putBoolean(Constants.EXTRA_IS_STARRED, mIsStarred);
        outState.putBoolean(Constants.EXTRA_NOTE_MODE_CHANGED, mNoteModeChanged);
        outState.putString(Constants.EXTRA_CONTENT_DETAILS, Serializer.serializeContentDetails(mContentDetails));
        outState.putString(Constants.EXTRA_ATTACHED_IMAGES_LIST, Serializer.serializePathsList(mImagesAdapter.getData()));
    }

    @OnClick(R.id.compose_star_image_button)
    public void starImageButtonClicked() {
        if (mIsStarred) {
            setNotStarredState();
        } else {
            setStarredState();
        }
        mNoteModeChanged = !mNoteModeChanged;
    }

    private void setStarredState() {
        mIsStarred = true;
        mStarImageButton.setImageResource(R.drawable.ic_star_orange_36dp);
    }

    private void setNotStarredState() {
        mIsStarred = false;
        mStarImageButton.setImageResource(R.drawable.ic_star_border_black_36dp);
    }

    private void saveNote() {
        Intent resultIntent = new Intent();

        String title = mTitleEditText.getText().toString();
        String description = mDescriptionEditText.getText().toString();
        ArrayList<String> attachedImagesList = mImagesAdapter.getData();
        if (isValidNote(title, description, attachedImagesList)) {
            int mode = mIsStarred ? Constants.MODE_IMPORTANT : Constants.MODE_NORMAL;
            String reminderString = mReminderTextView.getText().toString();
            if (reminderString.equals(getResources().getString(R.string.compose_note_set_reminder_text))) {
                reminderString = Constants.NO_REMINDER;
            }

            //TODO: optmize passing empty array lists
            NoteData noteData = new NoteData(title, mode,
                    hasAttributes(description, attachedImagesList),
                    description, attachedImagesList,
                    new ArrayList<String>(), reminderString);

            if (!mIsOpenedInEditMode) {
                //new note
                if (MyApplication.getWritableDatabase().insertNote(noteData) != Constants.DATABASE_ERROR) {
                    resultIntent.putExtra(Constants.NOTE_ADDED_SUCCESSFULLY, true);
                    resultIntent.putExtra(Constants.COMPOSE_NOTE_MODE, mode);
                } else {
                    MyDebugger.log("Failed to save note.");
                }
            } else {
                //update existing note
                noteData.setId(mEditNoteId);
                noteData.setTargetId(mEditNoteTargetId);
                if (mContentDetails != null) {
                    mContentDetails.setLastModifiedDate(DateUtils.getCurrentTimeSQLiteFormatted());
                    noteData.setContentDetails(mContentDetails);
                } else {
                    MyDebugger.log("ContentDetails are null");
                }
                if (MyApplication.getWritableDatabase().updateNote(noteData)) {
                    resultIntent.putExtra(Constants.NOTE_UPDATED_SUCCESSFULLY, true);
                    resultIntent.putExtra(Constants.EXTRA_NOTE_DATA, Serializer.serializeNoteData(noteData));
                    resultIntent.putExtra(Constants.EXTRA_NOTE_MODE_CHANGED, mNoteModeChanged);
                } else {
                    MyDebugger.log("Failed to update note.");
                }
            }
        } else {
            MyDebugger.toast(this, "Cannot save empty notes.");
        }
        setResult(Activity.RESULT_OK, resultIntent);
    }

    private boolean hasAttributes(String description, ArrayList<String> attachedImagesList) {
        return description.trim().length() > 0 || attachedImagesList.size() > 0;
    }

    private boolean isValidNote(String title, String description, ArrayList<String> attachedImagesList) {
        //TODO: add sound in the validation
        return title.trim().length() > 0 || description.trim().length() > 0 || attachedImagesList.size() > 0;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_compose_note, menu);
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
                saveNote();
                finish();
                break;
            case R.id.action_add_image:
                ActionExecutor.addPhotoToNote(this);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        ComposeNoteResultHandler.handleResult(this, requestCode, resultCode, data);
        if(requestCode == Constants.OPEN_PHOTO_IN_GALLERY_REQ_CODE) {
            mOpenInGalleryLaunched = false;
        }
    }
}
