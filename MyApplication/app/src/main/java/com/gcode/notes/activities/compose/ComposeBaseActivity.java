package com.gcode.notes.activities.compose;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.gcode.notes.R;
import com.gcode.notes.activities.helpers.compose.base.ComposeBaseToolbarHelper;

import butterknife.Bind;
import butterknife.OnClick;

public class ComposeBaseActivity extends AppCompatActivity {
    @Bind(R.id.compose_toolbar)
    Toolbar mToolbar;

    @Bind(R.id.compose_title_edit_text)
    EditText mTitleEditText;

    @Bind(R.id.compose_star_image_button)
    ImageButton mStarImageButton;

    @Bind(R.id.compose_reminder_text_view)
    TextView mReminderTextView;

    public EditText getTitleEditText() {
        return mTitleEditText;
    }

    public TextView getReminderTextView() {
        return mReminderTextView;
    }

    public boolean mIsOpenedInEditMode;
    public boolean mIsStarred;
    public boolean mNoteModeChanged;
    public Intent mResultIntent = new Intent();

    /**
     * Setups title edit text to not has horizontal scrolling and activity's toolbar
     */
    protected void setup() {
        mTitleEditText.setHorizontallyScrolling(false);
        mTitleEditText.setMaxLines(3);
        ComposeBaseToolbarHelper.setupToolbar(this, mToolbar);
    }

    @OnClick(R.id.compose_star_image_button)
    public void starImageButtonClicked() {
        if (mIsStarred) {
            //activity is in starredState, change it to notStarredState
            setNotStarredState();
        } else {
            //activity is in notStarredState, change it to starredState
            setStarredState();
        }
        mNoteModeChanged = !mNoteModeChanged;
    }

    public void setStarredState() {
        mIsStarred = true;
        mStarImageButton.setImageResource(R.drawable.ic_star_orange_36dp);
    }

    public void setNotStarredState() {
        mIsStarred = false;
        mStarImageButton.setImageResource(R.drawable.ic_star_border_black_36dp);
    }
}
