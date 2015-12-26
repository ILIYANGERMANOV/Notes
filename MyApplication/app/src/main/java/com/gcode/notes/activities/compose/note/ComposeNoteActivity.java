package com.gcode.notes.activities.compose.note;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.gcode.notes.R;
import com.gcode.notes.activities.compose.ComposeBaseActivity;
import com.gcode.notes.activities.helpers.compose.note.ComposeNoteMenuOptionsHelper;
import com.gcode.notes.activities.helpers.compose.note.ComposeNoteResultHandler;
import com.gcode.notes.activities.helpers.compose.note.ComposeNoteRotationHandler;
import com.gcode.notes.activities.helpers.compose.note.ComposeNoteStartStateHelper;
import com.gcode.notes.adapters.note.compose.ComposeNoteImagesAdapter;
import com.gcode.notes.data.note.NoteData;
import com.gcode.notes.extras.utils.AudioUtils;
import com.gcode.notes.ui.ActionExecutor;
import com.linearlistview.LinearListView;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ComposeNoteActivity extends ComposeBaseActivity {
    //TODO: delete audio on not saving note
    @Bind(R.id.compose_note_description_edit_text)
    EditText mDescriptionEditText;

    @Bind(R.id.compose_note_images_linear_list_view)
    LinearListView mImagesLinearListView;

    @Bind(R.id.compose_note_audio_layout)
    LinearLayout mAudioLayout;

    @Bind(R.id.compose_audio_play_pause_button)
    ImageButton mAudioPlayPauseButton;

    @Bind(R.id.compose_audio_progress_bar)
    ProgressBar mAudioProgressBar;

    @Bind(R.id.compose_audio_duration_text_view)
    TextView mAudioDurationTextView;

    //getters for layout components------------------------------------------------------------------------------------------
    public EditText getDescriptionEditText() {
        return mDescriptionEditText;
    }

    public LinearListView getImagesLinearListView() {
        return mImagesLinearListView;
    }

    public LinearLayout getAudioLayout() {
        return mAudioLayout;
    }

    public ImageButton getAudioPlayPauseButton() {
        return mAudioPlayPauseButton;
    }

    public ProgressBar getAudioProgressBar() {
        return mAudioProgressBar;
    }

    public TextView getAudioDurationTextView() {
        return mAudioDurationTextView;
    }
    //getters for layout components------------------------------------------------------------------------------------------

    public NoteData mNoteData;

    public AudioUtils mAudioUtils;
    public ComposeNoteImagesAdapter mImagesAdapter;
    public MaterialDialog mOpenImageInGalleryProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compose_note);
        ButterKnife.bind(this);
        setup(savedInstanceState);
    }

    private void setup(Bundle savedInstanceState) {
        super.setup(); //setups base in ComposeBaseActivity
        new ComposeNoteStartStateHelper(this).setupStartState(savedInstanceState);
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        ComposeNoteRotationHandler.saveInstanceState(this, outState);
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
        mAudioUtils.toggle();
    }

    @OnClick(R.id.compose_audio_delete_button)
    public void deleteAudio() {
        ActionExecutor.deleteAudioFromNote(this);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_compose_note, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item) || ComposeNoteMenuOptionsHelper.optionsItemSelected(this, item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        ComposeNoteResultHandler.handleResult(this, requestCode, resultCode, data);
    }
}