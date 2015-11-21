package com.gcode.notes.adapters.note.compose;

import android.content.Context;
import android.view.View;
import android.widget.ArrayAdapter;

import com.linearlistview.LinearListView;

import java.util.ArrayList;

public class ComposeNoteAudioAdapter extends ArrayAdapter<String> {
    //TODO: optimize mData and data and REFACTOR
    //TODO: implement adapter
    ArrayList<String> mData;
    LinearListView mLinearListView;

    public ComposeNoteAudioAdapter(Context context, ArrayList<String> data, LinearListView linearListView) {
        super(context, 0, data);
        mData = data;
        mLinearListView = linearListView;
    }

    public ArrayList<String> getData() {
        return mData;
    }

    public void showListView() {
        mLinearListView.setVisibility(View.VISIBLE);
    }

    public void hideListView() {
        mLinearListView.setVisibility(View.GONE);
    }
}
