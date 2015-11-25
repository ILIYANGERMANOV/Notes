package com.gcode.notes.activities.helpers.compose.note;


import com.gcode.notes.R;
import com.gcode.notes.activities.compose.ComposeNoteActivity;

public class ComposeNoteImportanceHelper {
    public static void setStarredState(ComposeNoteActivity composeNoteActivity) {
        composeNoteActivity.mIsStarred = true;
        composeNoteActivity.getStarImageButton().setImageResource(R.drawable.ic_star_orange_36dp);
    }

    public static void setNotStarredState(ComposeNoteActivity composeNoteActivity) {
        composeNoteActivity.mIsStarred = false;
        composeNoteActivity.getStarImageButton().setImageResource(R.drawable.ic_star_border_black_36dp);
    }
}
