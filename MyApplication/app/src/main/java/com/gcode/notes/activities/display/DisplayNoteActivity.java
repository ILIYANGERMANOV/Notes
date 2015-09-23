package com.gcode.notes.activities.display;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.gcode.notes.R;
import com.gcode.notes.data.NoteData;
import com.gcode.notes.extras.Constants;
import com.gcode.notes.extras.Serializer;

import butterknife.Bind;
import butterknife.ButterKnife;

public class DisplayNoteActivity extends AppCompatActivity {
    @Bind(R.id.display_note_toolbar)
    Toolbar mToolbar;

    @Bind(R.id.display_note_title_text_view)
    TextView mTitleTextView;

    @Bind(R.id.display_note_description_text_view)
    TextView mDescriptionTextView;

    @Bind(R.id.display_note_image_view)
    ImageView mAttachedImageView;

    @Bind(R.id.voice_image_view)
    ImageView mVoiceImageView;

    @Bind(R.id.reminder_text_view)
    TextView mReminderTextView;

    @Bind(R.id.attributes_divider)
    View mAttributesDividerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_note);
        ButterKnife.bind(this);
        setupToolbar();
        setupStartState();
    }

    private void setupStartState() {
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {
            String serializedNoteData = extras.getString(Constants.EXTRA_NOTE_DATA);
            if (serializedNoteData != null) {
                NoteData noteData = Serializer.parseNoteData(serializedNoteData);
                display(noteData);
            }
        }
    }

    private void display(NoteData noteData) {
        if (noteData != null) {
            noteData.displayNote(mTitleTextView, mReminderTextView, mDescriptionTextView,
                    mAttachedImageView, mVoiceImageView, mAttributesDividerView);
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
                finish();
        }

        return super.onOptionsItemSelected(item);
    }
}
