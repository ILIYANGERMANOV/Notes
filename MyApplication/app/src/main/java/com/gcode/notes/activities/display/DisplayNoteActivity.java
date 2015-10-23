package com.gcode.notes.activities.display;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.gcode.notes.R;
import com.gcode.notes.activities.compose.ComposeNoteActivity;
import com.gcode.notes.data.NoteData;
import com.gcode.notes.extras.values.Constants;
import com.gcode.notes.notes.MyApplication;
import com.gcode.notes.serialization.Serializer;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DisplayNoteActivity extends AppCompatActivity {
    @Bind(R.id.display_note_toolbar)
    Toolbar mToolbar;

    @Bind(R.id.display_title_text_view)
    TextView mTitleTextView;

    @Bind(R.id.display_star_image_button)
    ImageButton mStarImageButton;

    @Bind(R.id.display_note_description_text_view)
    TextView mDescriptionTextView;

    @Bind(R.id.display_note_image_view)
    ImageView mAttachedImageView;

    NoteData mNoteData;
    boolean mIsStarred;
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
                displayNoteData();
            }
        }
    }


    private void displayNoteData() {
        if (mNoteData != null) {
            mNoteData.displayNote(mTitleTextView, mDescriptionTextView, mAttachedImageView);
            if (mNoteData.isImportant()) {
                setStarredState();
            } else {
                setNotStarredState();
            }
        }
    }

    @OnClick(R.id.display_star_image_button)
    public void starImageButtonClicked() {
        if (mIsStarred) {
            setNotStarredState();
        } else {
            setStarredState();
        }
        mNoteData.setImportant(mIsStarred);
        mNoteModeChanged = !mNoteModeChanged;
        MyApplication.getWritableDatabase().updateNoteMode(mNoteData);
    }

    private void setStarredState() {
        mIsStarred = true;
        mStarImageButton.setImageResource(R.drawable.ic_star_orange_36dp);
    }

    private void setNotStarredState() {
        mIsStarred = false;
        mStarImageButton.setImageResource(R.drawable.ic_star_border_black_36dp);
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case Constants.COMPOSE_NOTE_REQUEST_CODE:
                if (resultCode == Activity.RESULT_OK) {
                    if (data != null) {
                        if (data.getBooleanExtra(Constants.NOTE_UPDATED_SUCCESSFULLY, false)) {
                            String serializedNoteData = data.getStringExtra(Constants.EXTRA_NOTE_DATA);
                            if (serializedNoteData != null) {
                                NoteData noteData = Serializer.parseNoteData(serializedNoteData);
                                if (noteData != null) {
                                    mNoteModeChanged = data.getBooleanExtra(Constants.EXTRA_NOTE_MODE_CHANGED, false);
                                    mNoteData = noteData;
                                    displayNoteData();
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
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_display_note, menu);
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
                setResult();
                finish();
                return true;
            case R.id.action_edit:
                Intent intent = new Intent(this, ComposeNoteActivity.class);
                intent.putExtra(Constants.EXTRA_NOTE_DATA, Serializer.serializeNoteData(mNoteData));
                startActivityForResult(intent, Constants.COMPOSE_NOTE_REQUEST_CODE);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
