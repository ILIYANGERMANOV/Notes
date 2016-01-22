package com.gcode.notes.adapters.main;


import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gcode.notes.R;
import com.gcode.notes.activities.MainActivity;
import com.gcode.notes.adapters.main.viewholders.BaseItemViewHolder;
import com.gcode.notes.adapters.main.viewholders.ListItemViewHolder;
import com.gcode.notes.adapters.main.viewholders.NoteItemViewHolder;
import com.gcode.notes.data.NoteData;
import com.gcode.notes.data.base.ContentBase;
import com.gcode.notes.data.list.ListData;
import com.gcode.notes.extras.MyDebugger;
import com.gcode.notes.extras.values.Constants;
import com.gcode.notes.helper.ItemTouchHelperAdapter;
import com.gcode.notes.notes.MyApplication;
import com.gcode.notes.ui.ActionExecutor;

import java.util.ArrayList;
import java.util.Collections;

public class MainAdapter extends RecyclerView.Adapter<BaseItemViewHolder> implements ItemTouchHelperAdapter {
    //TODO: REFACTOR AND OPTIMIZE
    private ArrayList<ContentBase> mData;
    private View mRootView;
    private MainActivity mMainActivity;
    private RecyclerView mRecyclerView;
    private LayoutInflater mInflater;

    private View mEmptyView;
    private boolean mEmptyViewVisible;

    public MainAdapter(MainActivity mainActivity, ArrayList<ContentBase> data) {
        mMainActivity = mainActivity;
        mRecyclerView = mainActivity.getRecyclerView();
        mRootView = mainActivity.getCoordinatorLayout();
        mEmptyView = mainActivity.getRecyclerViewEmptyView();
        mData = data;

        mInflater = LayoutInflater.from(mainActivity);
        mEmptyViewVisible = false;
    }

    //getters----------------------------------------------------------------------------------------
    public RecyclerView getRecyclerView() {
        return mRecyclerView;
    }

    public View getRootView() {
        return mRootView;
    }

    public MainActivity getMainActivity() {
        return mMainActivity;
    }

    public ArrayList<ContentBase> getData() {
        return mData;
    }

    //getters----------------------------------------------------------------------------------------

    @Override
    public int getItemViewType(int position) {
        return mData.get(position).getType();
    }

    @Override
    public BaseItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == Constants.TYPE_NOTE) {
            return new NoteItemViewHolder(mMainActivity, mInflater.inflate(R.layout.note, parent, false), mData);
        } else {
            return new ListItemViewHolder(mMainActivity, mInflater.inflate(R.layout.list, parent, false), mData);
        }
    }

    @Override
    public void onBindViewHolder(final BaseItemViewHolder holder, int position) {
        holder.setStartState();
        ContentBase currentItem = mData.get(position);
        if (currentItem != null) {
            if (currentItem.getType() == Constants.TYPE_NOTE) {
                ((NoteData) currentItem).displayNoteOnMain((NoteItemViewHolder) holder);
            } else {
                ((ListData) currentItem).displayListOnMain(mMainActivity, (ListItemViewHolder) holder);
            }
        }
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public int getIndexOfItem(ContentBase item) {
        int itemIndex = -1;
        for (int i = 0; i < getItemCount(); ++i) {
            if (mData.get(i).getId() == item.getId()) {
                itemIndex = i;
                break;
            }
        }
        return itemIndex;
    }

    public boolean itemExists(ContentBase item) {
        for (ContentBase currentItem : mData) {
            if (currentItem.getId() == item.getId())
                return true;
        }
        return false;
    }

    public void updateItem(ContentBase item) {
        int position = getIndexOfItem(item);
        if (position != -1) {
            mData.set(position, item);
            notifyItemChanged(position);
        } else {
            MyDebugger.log("MainAdapter updateItem() - invalid position.");
        }
    }

    public void updateItemMode(ContentBase item) {
        int position = getIndexOfItem(item);
        if (position != -1) {
            mData.get(position).setMode(item.getMode());
            notifyItemChanged(position);
        } else {
            MyDebugger.log("updateItemMode()", "Item not found");
        }
    }


    public boolean addItem(int position, ContentBase item) {
        try {
            mData.add(position, item);
            notifyItemInserted(position);
            if (mEmptyViewVisible) {
                hideEmptyView();
            }
            return true;
        } catch (IndexOutOfBoundsException exception) {
            MyDebugger.log("MainAdapter(): indexOutOfBoundsException caught.");
            mData.add(item);
            notifyItemInserted(getItemCount() - 1);
            return false;
        }
    }

    public ContentBase removeItem(int position) {
        ContentBase removedItem = mData.remove(position);
        notifyItemRemoved(position);
        checkForEmptyState();
        return removedItem;
    }

    public void moveItem(int fromPosition, int toPosition) {
        ContentBase model = mData.remove(fromPosition);
        mData.add(toPosition, model);
        notifyItemMoved(fromPosition, toPosition);
    }

    public void animateTo(ArrayList<ContentBase> filteredList) {
        applyAndAnimateRemovals(filteredList);
        applyAndAnimateAdditions(filteredList);
        applyAndAnimateMovedItems(filteredList);
    }

    private void applyAndAnimateRemovals(ArrayList<ContentBase> newNotes) {
        for (int i = mData.size() - 1; i >= 0; i--) {
            final ContentBase contentBase = mData.get(i);
            if (!newNotes.contains(contentBase)) {
                removeItem(i);
            }
        }
    }

    private void applyAndAnimateAdditions(ArrayList<ContentBase> newNotes) {
        for (int i = 0, count = newNotes.size(); i < count; i++) {
            final ContentBase contentBase = newNotes.get(i);
            if (!mData.contains(contentBase)) {
                addItem(i, contentBase);
            }
        }
    }

    private void applyAndAnimateMovedItems(ArrayList<ContentBase> newNotes) {
        for (int toPosition = newNotes.size() - 1; toPosition >= 0; toPosition--) {
            final ContentBase contentBase = newNotes.get(toPosition);
            final int fromPosition = mData.indexOf(contentBase);
            if (fromPosition >= 0 && fromPosition != toPosition) {
                moveItem(fromPosition, toPosition);
            }
        }
    }

    @Override
    public void onItemDismissUnrecoverable(int position) {
        ActionExecutor.deleteNotePermanently(mMainActivity, this, mData.get(position), position);
        removeItem(position);
    }

    @Override
    public void onItemDismiss(int position) {
        ActionExecutor.popNoteDeletedSnackbar(this, position, mData.get(position));
        removeItem(position);
    }

    @Override
    public boolean onItemMove(int fromPosition, int toPosition) {
        if (fromPosition < toPosition) {
            for (int i = fromPosition; i < toPosition; i++) {
                Collections.swap(mData, i, i + 1);
                MyApplication.getWritableDatabase().swapNotesPosition(mData.get(i), mData.get(i + 1));
            }
        } else {
            for (int i = fromPosition; i > toPosition; i--) {
                Collections.swap(mData, i, i - 1);
                MyApplication.getWritableDatabase().swapNotesPosition(mData.get(i), mData.get(i - 1));
            }
        }
        notifyItemMoved(fromPosition, toPosition);
        return true;
    }


    public void updateContent(ArrayList<ContentBase> newData) {
        mData.clear();
        mData.addAll(newData);
        notifyDataSetChanged();
        checkForEmptyState();
    }

    private void checkForEmptyState() {
        if (getItemCount() == 0) {
            showEmptyView();
        } else {
            hideEmptyView();
        }
    }

    private void showEmptyView() {
        mEmptyView.setVisibility(View.VISIBLE);
        mEmptyViewVisible = true;
    }

    private void hideEmptyView() {
        mEmptyView.setVisibility(View.GONE);
        mEmptyViewVisible = false;
    }
}
