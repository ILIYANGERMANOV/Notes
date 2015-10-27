package com.gcode.notes.activities.display.note;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.gcode.notes.R;
import com.gcode.notes.data.NoteData;
import com.gcode.notes.extras.values.Constants;
import com.gcode.notes.serialization.Serializer;

import butterknife.Bind;
import butterknife.ButterKnife;

public class DisplayNoteBaseActivity extends AppCompatActivity {
    @Bind(R.id.display_note_toolbar)
    Toolbar mToolbar;

    @Bind(R.id.display_title_text_view)
    TextView mTitleTextView;

    @Bind(R.id.display_note_dates_text_view)
    TextView mDatesTextView;

    @Bind(R.id.display_action_image_button)
    ImageButton mActionImageButton;

    @Bind(R.id.display_note_description_text_view)
    TextView mDescriptionTextView;

    @Bind(R.id.display_note_image_view)
    ImageView mAttachedImageView;

    NoteData mNoteData;
    boolean mNoteModeChanged;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_note);
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
            handleScreenRotation(savedInstanceState);
        }
    }

    private void handleScreenRotation(Bundle savedInstanceState) {
        mNoteModeChanged = savedInstanceState.getBoolean(Constants.EXTRA_NOTE_MODE_CHANGED);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(Constants.EXTRA_NOTE_DATA, Serializer.serializeNoteData(mNoteData));
        outState.putBoolean(Constants.EXTRA_NOTE_MODE_CHANGED, mNoteModeChanged);
    }

    private void setupFromBundle(Bundle bundle) {
        if (bundle != null) {
            String serializedNoteData = bundle.getString(Constants.EXTRA_NOTE_DATA);
            if (serializedNoteData != null) {
                mNoteData = Serializer.parseNoteData(serializedNoteData);
                if (mNoteData != null) {
                    displayNoteData();
                }
            }
        }
    }


    protected void displayNoteData() {
        mNoteData.displayNote(mTitleTextView, mDescriptionTextView, mAttachedImageView);
        mDatesTextView.setText(mNoteData.getDateDetails());
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
    public void onBackPressed() {
        setResult();
        super.onBackPressed();
    }

    private void setResult() {
        Intent resultIntent = new Intent();
        resultIntent.putExtra(Constants.EXTRA_NOTE_DATA, Serializer.serializeNoteData(mNoteData));
        resultIntent.putExtra(Constants.EXTRA_NOTE_MODE_CHANGED, mNoteModeChanged);
        setResult(Activity.RESULT_OK, resultIntent);
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
                setResult();
                finish();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
