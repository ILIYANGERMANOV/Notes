package com.gcode.notes.activities.compose;

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
import com.gcode.notes.activities.helpers.compose.ComposeToolbarHelper;
import com.gcode.notes.activities.helpers.compose.note.ComposeNoteImportanceHelper;
import com.gcode.notes.activities.helpers.compose.note.ComposeNoteResultHandler;
import com.gcode.notes.activities.helpers.compose.note.ComposeNoteRotationHandler;
import com.gcode.notes.activities.helpers.compose.note.ComposeNoteSaveHelper;
import com.gcode.notes.activities.helpers.compose.note.ComposeNoteStartStateHelper;
import com.gcode.notes.adapters.note.compose.ComposeNoteImagesAdapter;
import com.gcode.notes.data.main.NoteData;
import com.gcode.notes.extras.utils.AudioUtils;
import com.gcode.notes.ui.ActionExecutor;
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

    @Bind(R.id.compose_star_image_button)
    ImageButton mStarImageButton;

    @Bind(R.id.compose_note_title_edit_text)
    EditText mTitleEditText;

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

    @Bind(R.id.compose_note_reminder_text_view)
    TextView mReminderTextView;

    //getters for layout components------------------------------------------------------------------------------------------
    public ImageButton getStarImageButton() {
        return mStarImageButton;
    }

    public EditText getTitleEditText() {
        return mTitleEditText;
    }

    public EditText getDescriptionEditText() {
        return mDescriptionEditText;
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

    public TextView getReminderTextView() {
        return mReminderTextView;
    }
    //getters for layout components------------------------------------------------------------------------------------------

    public NoteData mNoteData;

    public boolean mIsOpenedInEditMode;
    public boolean mIsStarred;
    public boolean mNoteModeChanged;

    public AudioUtils mAudioUtils;
    public ComposeNoteImagesAdapter mImagesAdapter;
    public MaterialDialog mOpenImageInGalleryProgressDialog;

    public Intent mResultIntent = new Intent();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compose_note);
        ButterKnife.bind(this);
        setup(savedInstanceState);
    }

    private void setup(Bundle savedInstanceState) {
        mTitleEditText.setHorizontallyScrolling(false);
        mTitleEditText.setMaxLines(3);

        mImagesAdapter = new ComposeNoteImagesAdapter(this, new ArrayList<String>(), mImagesLinearListView);
        mImagesLinearListView.setAdapter(mImagesAdapter);

        ComposeNoteStartStateHelper composeNoteStartStateHelper = new ComposeNoteStartStateHelper(this);

        ComposeToolbarHelper.setupToolbar(this, mToolbar);
        composeNoteStartStateHelper.setupStartState(savedInstanceState);
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

    @OnClick(R.id.compose_star_image_button)
    public void starImageButtonClicked() {
        if (mIsStarred) {
            ComposeNoteImportanceHelper.setNotStarredState(this);
        } else {
            ComposeNoteImportanceHelper.setStarredState(this);
        }
        mNoteModeChanged = !mNoteModeChanged;
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
                ComposeNoteSaveHelper.saveNote(this);
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
    }
}
