package com.gcode.notes.activities.compose;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.gcode.notes.R;
import com.gcode.notes.controllers.BaseController;
import com.gcode.notes.data.NoteData;
import com.gcode.notes.extras.MyDebugger;
import com.gcode.notes.extras.values.Constants;
import com.gcode.notes.notes.MyApplication;
import com.gcode.notes.serialization.Serializer;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ComposeNoteActivity extends AppCompatActivity {
    @Bind(R.id.compose_note_toolbar)
    Toolbar mToolbar;

    @Bind(R.id.compose_note_title_edit_text)
    EditText mTitleEditText;

    @Bind(R.id.compose_note_attached_image_view)
    ImageView mAttachedImageView;

    @Bind(R.id.compose_note_description_edit_text)
    EditText mDescriptionEditText;

    @Bind(R.id.compose_star_image_button)
    ImageButton mStarImageButton;

    @Bind(R.id.compose_note_reminder_text_view)
    TextView mReminderTextView;

    boolean mIsOpenedInEditMode;
    int mEditNoteId;

    boolean mIsStarred;
    boolean mNoteModeChanged;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compose_note);
        ButterKnife.bind(this);
        setupToolbar();
        setupStartState(savedInstanceState);
    }

    private void setupStartState(Bundle savedInstanceState) {
        Bundle extras = getIntent().getExtras();
        if (savedInstanceState == null) {
            if (extras != null) {
                //Note opened in edit mode
                mIsOpenedInEditMode = true;
                setupFromEditMode(extras);
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

    private void setupFromEditMode(Bundle extras) {
        NoteData noteData = Serializer.parseNoteData(extras.getString(Constants.EXTRA_NOTE_DATA));
        if (noteData != null) {
            mEditNoteId = noteData.getId();
            mTitleEditText.setText(noteData.getTitle());
            if (noteData.isImportant()) {
                setStarredState();
            }
            String reminderString = noteData.getReminderString();
            if (!reminderString.equals(Constants.NO_REMINDER)) {
                mReminderTextView.setText(reminderString);
            }
            //TODO: set picture and audio
            mDescriptionEditText.setText(noteData.getDescription());
        }
    }

    private void handlerScreenRotation(Bundle savedInstanceState) {
        //TODO: handle screen rotation (picture and audio)
        mIsOpenedInEditMode = savedInstanceState.getBoolean(Constants.EXTRA_IS_OPENED_IN_EDIT_MODE);
        if (mIsOpenedInEditMode) {
            mEditNoteId = savedInstanceState.getInt(Constants.EXTRA_EDIT_NOTE_ID);
        }
        if (savedInstanceState.getBoolean(Constants.EXTRA_IS_STARRED)) {
            setStarredState();
        }
        mNoteModeChanged = savedInstanceState.getBoolean(Constants.EXTRA_NOTE_MODE_CHANGED);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //TODO: add picture and audio
        outState.putBoolean(Constants.EXTRA_IS_OPENED_IN_EDIT_MODE, mIsOpenedInEditMode);
        if (mIsOpenedInEditMode) {
            outState.putInt(Constants.EXTRA_EDIT_NOTE_ID, mEditNoteId);
        }
        outState.putBoolean(Constants.EXTRA_IS_STARRED, mIsStarred);
        outState.putBoolean(Constants.EXTRA_NOTE_MODE_CHANGED, mNoteModeChanged);
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
        Intent mResultIntent = new Intent();

        String title = mTitleEditText.getText().toString();
        String description = mDescriptionEditText.getText().toString();
        if (isValidNote(title, description)) {
            int mode = mIsStarred ? Constants.MODE_IMPORTANT : Constants.MODE_NORMAL;
            String reminderString = mReminderTextView.getText().toString();
            if (reminderString.equals(getResources().getString(R.string.compose_note_set_reminder_text))) {
                reminderString = Constants.NO_REMINDER;
            }


            NoteData noteData = new NoteData(title, mode,
                    hasAttributes(description),
                    description, null, null, reminderString);

            if (!mIsOpenedInEditMode) {
                //new note
                if (MyApplication.getWritableDatabase().insertNote(noteData) != Constants.DATABASE_ERROR) {
                    mResultIntent.putExtra(Constants.NOTE_ADDED_SUCCESSFULLY, true);
                    mResultIntent.putExtra(Constants.COMPOSE_NOTE_MODE, mode);
                } else {
                    MyDebugger.log("Failed to save note.");
                }
            } else {
                //update existing note
                noteData.setId(mEditNoteId);
                if (MyApplication.getWritableDatabase().updateNote(noteData)) {
                    mResultIntent.putExtra(Constants.NOTE_UPDATED_SUCCESSFULLY, true);
                    mResultIntent.putExtra(Constants.EXTRA_NOTE_DATA, Serializer.serializeNoteData(noteData));
                    mResultIntent.putExtra(Constants.EXTRA_NOTE_MODE_CHANGED, mNoteModeChanged);
                } else {
                    MyDebugger.log("Failed to update note.");
                }
            }
        }
        setResult(Activity.RESULT_OK, mResultIntent);
    }

    private boolean hasAttributes(String description) {
        return description.trim().length() > 0;
    }

    private boolean isValidNote(String title, String description) {
        //TODO: add image and sound in the validation
        return title.trim().length() > 0 || description.trim().length() > 0;
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
        }

        return super.onOptionsItemSelected(item);
    }

}
