package com.gcode.notes.listeners;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.gcode.notes.adapters.NotesAdapter;
import com.gcode.notes.data.ContentBase;


public class UndoOnClickListener implements View.OnClickListener {
    boolean undo;

    RecyclerView mRecyclerView;
    NotesAdapter mAdapter;
    int mPosition;
    ContentBase mNote;

    public UndoOnClickListener(RecyclerView recyclerView, NotesAdapter adapter, int position, ContentBase note) {
        mRecyclerView = recyclerView;
        mAdapter = adapter;
        mPosition = position;
        mNote = note;
    }

    @Override
    public void onClick(View v) {
        undo = true;
        mAdapter.addItem(mPosition, mNote);
        mRecyclerView.smoothScrollToPosition(mPosition);
    }

    public boolean undoTriggered() {
        return undo;
    }
}
