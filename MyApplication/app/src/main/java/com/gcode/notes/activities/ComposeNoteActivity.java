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
import android.widget.ImageView;
import android.widget.TextView;

import com.gcode.notes.R;
import com.gcode.notes.data.NoteData;
import com.gcode.notes.extras.Constants;
import com.gcode.notes.extras.MyDebugger;
import com.gcode.notes.notes.MyApplication;

import butterknife.Bind;
import butterknife.ButterKnife;

public class ComposeNoteActivity extends AppCompatActivity {
    @Bind(R.id.compose_note_toolbar)
    Toolbar mToolbar;

    @Bind(R.id.compose_note_title_edit_text)
    EditText mTitleEditText;

    @Bind(R.id.compose_note_attached_image_view)
    ImageView mAttachedImageView;

    @Bind(R.id.compose_note_description_edit_text)
    EditText mDescriptionEditText;

    @Bind(R.id.compose_note_priority_switch)
    SwitchCompat mPrioritySwitch;

    @Bind(R.id.compose_note_set_reminder_text_view)
    TextView mSetReminderTextView;

    int mFromController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compose_note);

        ButterKnife.bind(this);

        setupToolbar();
        setupMode();
    }

    private void setupMode() {
        Intent mIntent = getIntent();
        if (mIntent != null) {
            mFromController = mIntent.getExtras().getInt(Constants.CONTROLLER_ID);
        }
        switch (mFromController) {
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

    private void saveNote() {
        Intent mResultIntent = new Intent();

        String title = mTitleEditText.getText().toString();
        String description = mDescriptionEditText.getText().toString();
        if (isValidNote(title, description)) {
            int mode = mPrioritySwitch.isChecked() ? Constants.MODE_IMPORTANT : Constants.MODE_NORMAL;
            String reminderString = mSetReminderTextView.getText().toString();
            if (reminderString.equals(getResources().getString(R.string.compose_note_set_reminder_text))) {
                reminderString = Constants.NO_REMINDER;
            }


            NoteData noteData = new NoteData(title, mode,
                    hasAttributes(description),
                    description, null, null, reminderString);

            if (MyApplication.getWritableDatabase().insertNote(noteData) != Constants.DATABASE_ERROR) {
                mResultIntent.putExtra(Constants.NOTE_ADDED_SUCCESSFULLY, true);
                mResultIntent.putExtra(Constants.COMPOSE_NOTE_MODE, mode);
            }
        } else {
            //TODO: handle not valid note
            MyDebugger.toast(this, "Note is not valid!");
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
