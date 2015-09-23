package com.gcode.notes.listeners.main;


import android.content.Context;
import android.content.Intent;
import android.view.View;

import com.gcode.notes.activities.display.DisplayNoteActivity;
import com.gcode.notes.data.NoteData;

public class NoteItemOnClickListener implements View.OnClickListener {
    Context mContext;
    NoteData mNoteData;

    public NoteItemOnClickListener(Context context, NoteData noteData) {
        mContext = context;
        mNoteData = noteData;
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(mContext, DisplayNoteActivity.class);
        mContext.startActivity(intent);
    }
}