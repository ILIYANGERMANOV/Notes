package com.gcode.notes.activities.display.note;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.gcode.notes.R;
import com.gcode.notes.activities.display.DisplayBaseActivity;
import com.gcode.notes.activities.helpers.display.DisplayBaseMenuOptionsHelper;
import com.gcode.notes.activities.helpers.display.note.DisplayNoteBaseDisplayHelper;
import com.gcode.notes.activities.helpers.display.note.DisplayNoteBaseResultHandler;
import com.gcode.notes.activities.helpers.display.note.DisplayNoteBaseStartStateHelper;
import com.gcode.notes.data.note.NoteData;
import com.gcode.notes.extras.utils.AudioUtils;
import com.gcode.notes.extras.values.Constants;
import com.gcode.notes.serialization.Serializer;
import com.linearlistview.LinearListView;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DisplayNoteBaseActivity extends DisplayBaseActivity {
    public NoteData mNoteData;
    public MaterialDialog mOpenInGalleryProgressDialog;
    public AudioUtils mAudioUtils;

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

    //getters for layout components----------------------------------------------------------------------------------------
    public LinearLayout getAudioLayout() {
        return mAudioLayout;
    }

    public TextView getAudioDurationTextView() {
        return mAudioDurationTextView;
    }
    //getters for layout components----------------------------------------------------------------------------------------

    public ProgressBar getAudioProgressBar() {
        return mAudioProgressBar;
    }

    public ImageButton getAudioPlayPauseButton() {
        return mAudioPlayPauseButton;
    }

    public TextView getDescriptionTextView() {
        return mDescriptionTextView;
    }

    public LinearListView getImagesLinearListView() {
        return mImagesLinearListView;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_note);
        ButterKnife.bind(this);
        setup(savedInstanceState);
    }

    private void setup(Bundle savedInstanceState) {
        setup(); //setups activity's toolbar in DisplayBaseActivity
        new DisplayNoteBaseStartStateHelper(this).setupStartState(savedInstanceState);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(Constants.EXTRA_NOTE_DATA, Serializer.serializeNoteData(mNoteData));
        outState.putBoolean(Constants.EXTRA_NOTE_MODE_CHANGED, mNoteModeChanged);
    }

    public void displayNoteData() {
        displayBase(mNoteData);
        DisplayNoteBaseDisplayHelper.displayNoteData(this);
    }

    @SuppressWarnings("unused")
    @OnClick(R.id.display_audio_play_pause_button)
    public void playPauseAudio() {
        mAudioUtils.toggle();
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
    public void finish() {
        DisplayNoteBaseResultHandler.setResult(this); //set activity's result before closing
        super.finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        DisplayNoteBaseResultHandler.handleResult(this, requestCode);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item) || DisplayBaseMenuOptionsHelper.optionItemSelected(this, item);
    }
}
