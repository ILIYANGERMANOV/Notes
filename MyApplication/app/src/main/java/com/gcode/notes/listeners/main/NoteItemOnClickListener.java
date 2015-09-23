package com.gcode.notes.listeners.main;


import android.content.Context;
import android.view.View;

import com.gcode.notes.data.NoteData;
import com.gcode.notes.extras.MyDebugger;

public class NoteItemOnClickListener implements View.OnClickListener {
    Context mContext;
    NoteData mNoteData;

    public NoteItemOnClickListener(Context context, NoteData noteData) {
        mContext = context;
        mNoteData = noteData;
    }

    @Override
    public void onClick(View v) {
        MyDebugger.log("onClick()");
    }
}