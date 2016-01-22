package com.gcode.notes.activities.helpers.main.actions;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.View;

import com.gcode.notes.activities.MainActivity;
import com.gcode.notes.adapters.main.MainAdapter;
import com.gcode.notes.data.NoteData;
import com.gcode.notes.data.base.ContentBase;
import com.gcode.notes.data.list.ListData;
import com.gcode.notes.data.list.ListDataItem;

import java.util.ArrayList;

public class MainSearchHandler implements SearchView.OnQueryTextListener,
        SearchView.OnCloseListener, SearchView.OnClickListener {

    private MainActivity mMainActivity;
    private RecyclerView mRecyclerView;
    private MainAdapter mMainAdapter;

    private ArrayList<ContentBase> mNotesListCopy;


    public MainSearchHandler(MainActivity mainActivity) {
        mMainActivity = mainActivity;
        mMainAdapter = mainActivity.mMainAdapter;
        mRecyclerView = mainActivity.getRecyclerView();
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String query) {
        final ArrayList<ContentBase> filteredNotesList = filter(mNotesListCopy, query);
        mMainAdapter.animateTo(filteredNotesList);
        mRecyclerView.scrollToPosition(0);
        return true;
    }

    private ArrayList<ContentBase> filter(ArrayList<ContentBase> notes, String query) {

        final ArrayList<ContentBase> filteredNotesList = new ArrayList<>();
        for (ContentBase contentBase : notes) {
            //TODO: implement better filter (better use library)
            if (satisfyQuery(contentBase.getTitle(), query) || extraFilter(contentBase, query)) {
                filteredNotesList.add(contentBase);
            }
        }

        return filteredNotesList;
    }

    private boolean extraFilter(ContentBase contentBase, String query) {
        if (contentBase instanceof NoteData) {
            //its note, extra filter description
            return satisfyQuery(((NoteData) contentBase).getDescription(), query);
        } else {
            //its list, extra filter list items
            ListData listData = ((ListData) contentBase);
            if (listData.hasAttachedList()) {
                //list data has attached list, filer it
                for (ListDataItem listDataItem : listData.getList()) {
                    if (satisfyQuery(listDataItem.getContent(), query)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private boolean satisfyQuery(String text, String query) {
        return text.toLowerCase().contains(query.toLowerCase());
    }

    @Override
    public boolean onClose() {
        //search view is closed, enable recycler view's drag, swipe
        mMainActivity.mSimpleItemTouchHelperCallback.setAllEnabled(true);
        return false; //false, cuz we want default behaviour
    }

    @Override
    public void onClick(View v) {
        //search view is opened, disable recycler view's swipe, drag and create notes list copy
        mMainActivity.mSimpleItemTouchHelperCallback.setAllEnabled(false);
        mNotesListCopy = new ArrayList<>(mMainAdapter.getData());
    }
}
