package com.gcode.notes.listeners.main;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;

import com.gcode.notes.activities.display.DisplayNoteActivity;
import com.gcode.notes.data.NoteData;
import com.gcode.notes.extras.Constants;
import com.gcode.notes.serialization.Serializer;

public class NoteItemOnClickListener implements View.OnClickListener {
    Activity mActivity;
    NoteData mNoteData;

    public NoteItemOnClickListener(Activity activity, NoteData noteData) {
        mActivity = activity;
        mNoteData = noteData;
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(mActivity, DisplayNoteActivity.class);
        intent.putExtra(Constants.EXTRA_NOTE_DATA, Serializer.serializeNoteData(mNoteData));
        mActivity.startActivityForResult(intent, Constants.NOTE_FROM_DISPLAY_RES_CODE);
    }
}