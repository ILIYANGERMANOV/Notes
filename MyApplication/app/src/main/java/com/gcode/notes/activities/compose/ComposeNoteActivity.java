package com.gcode.notes.activities.compose;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.gcode.notes.R;
import com.gcode.notes.activities.helpers.compose.ComposeNoteResultHandler;
import com.gcode.notes.activities.helpers.compose.ComposeToolbarHelper;
import com.gcode.notes.adapters.note.compose.ComposeNoteImagesAdapter;
import com.gcode.notes.controllers.BaseController;
import com.gcode.notes.data.ContentDetails;
import com.gcode.notes.data.NoteData;
import com.gcode.notes.extras.MyDebugger;
import com.gcode.notes.extras.utils.AudioUtils;
import com.gcode.notes.extras.utils.DateUtils;
import com.gcode.notes.extras.values.Constants;
import com.gcode.notes.notes.MyApplication;
import com.gcode.notes.serialization.Serializer;
import com.gcode.notes.ui.ActionExecutor;
import com.gcode.notes.ui.helpers.DialogHelper;
import com.linearlistview.LinearListView;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ComposeNoteActivity extends AppCompatActivity {
    //TODO: REFACTOR AND OPTIMIZE
    //TODO: delete audio on not saving note
    @Bind(R.id.compose_note_toolbar)
    Toolbar mToolbar;

    @Bind(R.id.compose_note_title_edit_text)
    EditText mTitleEditText;

    @Bind(R.id.compose_note_images_linear_list_view)
    LinearListView mImagesLinearListView;

    @Bind(R.id.compose_note_audio_layout)
    LinearLayout mAudioLayout;

    @Bind(R.id.compose_audio_progress_bar)
    ProgressBar mAudioProgressBar;

    @Bind(R.id.compose_audio_play_pause_button)
    ImageButton mAudioPlayPauseButton;

    @Bind(R.id.compose_audio_duration_text_view)
    TextView mAudioDurationTextView;

    @Bind(R.id.compose_note_description_edit_text)
    EditText mDescriptionEditText;

    @Bind(R.id.compose_star_image_button)
    ImageButton mStarImageButton;

    @Bind(R.id.compose_note_reminder_text_view)
    TextView mReminderTextView;

    boolean mIsOpenedInEditMode;
    int mEditNoteId;
    int mEditNoteTargetId = Constants.ERROR;

    boolean mIsStarred;
    boolean mNoteModeChanged;
    ContentDetails mContentDetails;


    ComposeNoteImagesAdapter mImagesAdapter;
    String mAudioPath = Constants.NO_AUDIO;

    AudioUtils mAudioUtils;

    MaterialDialog mOpenImageInGalleryProgressDialog;

    public void showAndInitOpenImageProgressDialog() {
        mOpenImageInGalleryProgressDialog = DialogHelper.buildOpenImageProgressDialog(this);
    }

    public ComposeNoteImagesAdapter getImagesAdapter() {
        return mImagesAdapter;
    }

    public LinearLayout getAudioLayout() {
        return mAudioLayout;
    }

    public AudioUtils getAudioUtils() {
        return mAudioUtils;
    }

    public String getAudioPath() {
        return mAudioPath;
    }

    public void setAudioPath(String audioPath) {
        this.mAudioPath = audioPath;
    }

    public int getEditNoteTargetId() {
        return mEditNoteTargetId;
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
        //TODO: consider adding EXTRA_SETUP_FROM
        if (savedInstanceState == null) {
            if (intent.getStringExtra(Constants.EXTRA_NOTE_DATA) != null) {
                //Note opened in edit mode
                mIsOpenedInEditMode = true;
                setupFromEditMode(intent.getStringExtra(Constants.EXTRA_NOTE_DATA));
            } else if (intent.getStringExtra(Constants.EXTRA_PHOTO_URI) != null) {
                setupFromPhoto(intent.getStringExtra(Constants.EXTRA_PHOTO_URI));
            } else if (intent.getStringExtra(Constants.EXTRA_AUDIO_PATH) != null) {
                setupFromAudio(intent.getStringExtra(Constants.EXTRA_AUDIO_PATH),
                        intent.getStringExtra(Constants.EXTRA_RECOGNIZED_SPEECH_TEXT));
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

    private void setupFromAudio(String audioPath, String recognizedSpeechText) {
        mDescriptionEditText.setText(recognizedSpeechText);
        setupAudio(audioPath);
    }

    private void setupFromPhoto(String photoUriString) {
        mImagesAdapter.add(photoUriString);
    }

    private void setupLayout() {
        mTitleEditText.setHorizontallyScrolling(false);
        mTitleEditText.setMaxLines(3);

        mImagesAdapter = new ComposeNoteImagesAdapter(this, new ArrayList<String>(), mImagesLinearListView);
        mImagesLinearListView.setAdapter(mImagesAdapter);
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
            if (noteData.hasAttachedAudio()) {
                setupAudio(noteData.getAttachedAudioPath());
            }
            if (noteData.hasAttachedImage()) {
                //adapter's list is still empty, no need to clear
                mImagesAdapter.addAll(noteData.getAttachedImagesPaths());
            }
            mDescriptionEditText.setText(noteData.getDescription());
        }
    }

    private void handlerScreenRotation(Bundle savedInstanceState) {
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
        ArrayList<String> pathsList = Serializer.parseStringPathsList(
                savedInstanceState.getString(Constants.EXTRA_ATTACHED_IMAGES_LIST));

        if (pathsList != null) {
            //adapter's list is still empty, no need to clear
            mImagesAdapter.addAll(pathsList);
        }
        String audioPath = savedInstanceState.getString(Constants.EXTRA_ATTACHED_AUDIO_PATH);
        if (audioPath != null && !audioPath.equals(Constants.NO_AUDIO)) {
            setupAudio(audioPath);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(Constants.EXTRA_IS_OPENED_IN_EDIT_MODE, mIsOpenedInEditMode);
        if (mIsOpenedInEditMode) {
            outState.putInt(Constants.EXTRA_EDIT_NOTE_ID, mEditNoteId);
            outState.putInt(Constants.EXTRA_EDIT_NOTE_TARGET_ID, mEditNoteTargetId);
        }
        outState.putBoolean(Constants.EXTRA_IS_STARRED, mIsStarred);
        outState.putBoolean(Constants.EXTRA_NOTE_MODE_CHANGED, mNoteModeChanged);
        outState.putString(Constants.EXTRA_CONTENT_DETAILS, Serializer.serializeContentDetails(mContentDetails));
        //TODO: optimize always passing images list
        outState.putString(Constants.EXTRA_ATTACHED_IMAGES_LIST, Serializer.serializePathsList(mImagesAdapter.getData()));
        outState.putString(Constants.EXTRA_ATTACHED_AUDIO_PATH, mAudioPath);
    }

    private void setupAudio(String audioPath) {
        mAudioUtils = new AudioUtils(this, audioPath, mAudioDurationTextView,
                mAudioProgressBar, mAudioPlayPauseButton, mAudioLayout);
        mAudioPath = audioPath;
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

    @Override
    protected void onPause() {
        if (mAudioUtils != null) {
            mAudioUtils.pauseAudio();
        }
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        if (mAudioUtils != null) {
            mAudioUtils.clearResources();
        }
        super.onDestroy();
    }

    @OnClick(R.id.compose_audio_play_pause_button)
    public void playPauseAudio() {
        if (!mAudioUtils.isPlaying()) {
            //audio is not playing, start it and set pause icon
            mAudioUtils.playAudio();
        } else {
            //audio is playing, stop it and set play icon
            mAudioUtils.pauseAudio();
        }
    }

    @OnClick(R.id.compose_audio_delete_button)
    public void deleteAudio() {
        ActionExecutor.deleteAudioFromNote(this);
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

            //TODO: optimize passing by not passing empty array lists
            NoteData noteData = new NoteData(title, mode,
                    hasAttributes(description, attachedImagesList),
                    description, attachedImagesList, mAudioPath, reminderString);

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
        return title.trim().length() > 0 || description.trim().length() > 0 ||
                attachedImagesList.size() > 0 || !mAudioPath.equals(Constants.NO_AUDIO);
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
        if (requestCode == Constants.OPEN_PHOTO_IN_GALLERY_REQ_CODE) {
            if (mOpenImageInGalleryProgressDialog != null) {
                mOpenImageInGalleryProgressDialog.dismiss();
            }
        }
    }
}
