package com.gcode.notes.controllers.visible;

import android.widget.TextView;

import com.gcode.notes.R;
import com.gcode.notes.activities.MainActivity;
import com.gcode.notes.extras.values.Constants;

public class AllNotesController extends VisibleController {

    public AllNotesController(MainActivity mainActivity) {
        super(mainActivity);
    }

    @Override
    public void setContent(boolean scrollToTop, boolean loadNewContent) {
        super.setContent(scrollToTop, loadNewContent);
        mToolbar.setTitle(mMainActivity.getString(R.string.all_notes_label));
    }

    @Override
    public boolean shouldHandleMode(int mode) {
        return mode == Constants.MODE_NORMAL || mode == Constants.MODE_IMPORTANT;
    }

    @Override
    public void setupEmptyView(TextView emptyView) {
        emptyView.setText(mMainActivity.getString(R.string.all_notes_empty_view_text));
        emptyView.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.note_empty_drawable, 0, 0);
    }
}
