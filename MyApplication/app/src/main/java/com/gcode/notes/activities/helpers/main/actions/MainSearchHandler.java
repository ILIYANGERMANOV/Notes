package com.gcode.notes.activities.helpers.main.actions;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.View;

import com.gcode.notes.activities.MainActivity;
import com.gcode.notes.activities.helpers.main.actions.callbacks.SearchHandlerCallbacks;
import com.gcode.notes.adapters.main.MainAdapter;
import com.gcode.notes.data.NoteData;
import com.gcode.notes.data.base.ContentBase;
import com.gcode.notes.data.list.ListData;
import com.gcode.notes.data.list.ListDataItem;
import com.gcode.notes.extras.MyDebugger;

import java.util.ArrayList;

public class MainSearchHandler implements SearchView.OnQueryTextListener,
        SearchView.OnCloseListener, SearchView.OnClickListener, SearchHandlerCallbacks {
    //TODO: REFACTOR AND OPTIMIZE (skipping frames)

    private MainActivity mMainActivity;
    private RecyclerView mRecyclerView;
    private MainAdapter mMainAdapter;

    private ArrayList<ContentBase> mNotesListCopy;
    private String mLastQuery;

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
        mLastQuery = query;
        final ArrayList<ContentBase> filteredNotesList = filter(mNotesListCopy, query);
        mMainAdapter.updateContent(filteredNotesList, false);
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

    @Override
    public void onNewItemAdded(ContentBase contentBase) {
        mNotesListCopy.add(0, contentBase);
        makeSearchQuery();
    }

    @Override
    public void onItemAdded(ContentBase contentBase) {
        if (mNotesListCopy == null) return;
        int itemId = contentBase.getId();
        int itemOrderId = contentBase.getOrderId();
        for (int i = 0; i < mNotesListCopy.size(); ++i) {
            ContentBase currentItem = mNotesListCopy.get(i);
            if (itemOrderId > currentItem.getOrderId()
                    || itemId == currentItem.getId()) {
                //!WARNING: all the checks if item already exist in list are done,
                //because mNotesListCopy is used in filter, which use animateTo()
                //where IndexOutOfBoundsException is thrown when there is no
                //consistency between adapter list and list copy.

                if (itemId == currentItem.getId()) {
                    //item already exists in that list no need to add it, return;
                    MyDebugger.log("MainSearchHandler: item already exists in list.");
                    return;
                }

                //item position found and it not exists, make search query and return;
                mNotesListCopy.add(i, contentBase);
                makeSearchQuery();
                return;
            }
        }
        //item is last and not exists, add it and make search query
        mNotesListCopy.add(contentBase);
        makeSearchQuery();
    }

    @Override
    public void onItemChanged(ContentBase contentBase) {
        if (mNotesListCopy == null) return;
        int itemId = contentBase.getId();
        for (int i = 0; i < mNotesListCopy.size(); ++i) {
            if (itemId == mNotesListCopy.get(i).getId()) {
                //item that changed found; update it in list, make search query and return
                mNotesListCopy.set(i, contentBase);
                makeSearchQuery();
                return;
            }
        }
        MyDebugger.log("MainSearchHandler onItemChanged(): item not found");
    }

    @Override
    public void onItemRemoved(ContentBase contentBase) {
        if (mNotesListCopy == null) return;
        int itemId = contentBase.getId();
        for (int i = 0; i < mNotesListCopy.size(); ++i) {
            if (itemId == mNotesListCopy.get(i).getId()) {
                //item that changed found, remove it and return
                mNotesListCopy.remove(i);
                return;
            }
        }
        MyDebugger.log("MainSearchHandler onItemRemoved(): item not found");
    }

    private void makeSearchQuery() {
        if (mLastQuery != null) {
            onQueryTextChange(mLastQuery);
        }
    }
}
