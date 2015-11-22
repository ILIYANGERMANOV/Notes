package com.gcode.notes.activities.display.note;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.gcode.notes.R;
import com.gcode.notes.adapters.note.display.DisplayNoteImagesAdapter;
import com.gcode.notes.data.NoteData;
import com.gcode.notes.extras.utils.AudioUtils;
import com.gcode.notes.extras.utils.PhotoUtils;
import com.gcode.notes.extras.values.Constants;
import com.gcode.notes.serialization.Serializer;
import com.linearlistview.LinearListView;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DisplayNoteBaseActivity extends AppCompatActivity {
    //TODO: REFACTOR
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

    @Bind(R.id.display_note_images_Linear_list_view)
    LinearListView mImagesLinearListView;

    @Bind(R.id.display_note_audio_layout)
    LinearLayout mAudioLayout;

    @Bind(R.id.display_audio_play_pause_button)
    ImageButton mAudioPlayPauseButton;

    @Bind(R.id.display_audio_progress_bar)
    ProgressBar mAudioProgressBar;

    @Bind(R.id.display_audio_duration_text_view)
    TextView mAudioDurationTextView;

    NoteData mNoteData;
    boolean mNoteModeChanged;

    //used to prevent opening the image in gallery many times on spamming
    boolean mOpenInGalleryLaunched;

    AudioUtils mAudioUtils;

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
        mNoteData.displayNote(mTitleTextView, mDescriptionTextView);
        mDatesTextView.setText(mNoteData.getDateDetails());
        if (mNoteData.hasAttachedImage()) {
            final DisplayNoteImagesAdapter adapter = new DisplayNoteImagesAdapter(this, mNoteData.getAttachedImagesPaths());
            mImagesLinearListView.setAdapter(adapter);
            mImagesLinearListView.setOnItemClickListener(new LinearListView.OnItemClickListener() {
                @Override
                public void onItemClick(LinearListView parent, View view, int position, long id) {
                    if (!mOpenInGalleryLaunched) {
                        mOpenInGalleryLaunched = true;
                        //TODO: add on click effect on image
                        PhotoUtils.openPhotoInGallery(DisplayNoteBaseActivity.this, adapter.getItem(position));
                    }
                }
            });
        }
        if (mNoteData.hasAttachedAudio()) {
            mAudioLayout.setVisibility(View.VISIBLE);
            mAudioUtils = new AudioUtils(this, mNoteData.getAttachedAudioPath(),
                    mAudioDurationTextView, mAudioProgressBar, mAudioPlayPauseButton);
            mAudioLayout.setVisibility(View.VISIBLE);
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

    @OnClick(R.id.display_audio_play_pause_button)
    public void playPauseAudio() {
        if (!mAudioUtils.isPlaying()) {
            //audio is not playing, start it and set pause icon
            mAudioUtils.playAudio();
        } else {
            //audio is playing, stop it and set play icon
            mAudioUtils.pauseAudio();
        }
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constants.OPEN_PHOTO_IN_GALLERY_REQ_CODE) {
            mOpenInGalleryLaunched = false;
        }
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
