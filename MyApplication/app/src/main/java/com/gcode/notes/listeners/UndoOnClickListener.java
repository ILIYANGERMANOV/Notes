package com.gcode.notes.listeners;

import android.view.View;

import com.gcode.notes.adapters.NotesAdapter;
import com.gcode.notes.data.ContentBase;


public class UndoOnClickListener implements View.OnClickListener {
    boolean undo;
    NotesAdapter mAdapter;
    int mPosition;
    ContentBase mNote;

    public UndoOnClickListener(NotesAdapter adapter, int position, ContentBase note) {
        mAdapter = adapter;
        mPosition = position;
        mNote = note;
    }

    @Override
    public void onClick(View v) {
        undo = true;
        mAdapter.addItem(mPosition, mNote);
    }

    public boolean undoTriggered() {
        return undo;
    }
}
