package com.gcode.notes.adapters;


import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gcode.notes.R;
import com.gcode.notes.adapters.viewholders.main.BaseItemViewHolder;
import com.gcode.notes.adapters.viewholders.main.ListItemViewHolder;
import com.gcode.notes.adapters.viewholders.main.NoteItemViewHolder;
import com.gcode.notes.data.ContentBase;
import com.gcode.notes.data.ListData;
import com.gcode.notes.data.NoteData;
import com.gcode.notes.extras.MyDebugger;
import com.gcode.notes.extras.values.Constants;
import com.gcode.notes.helper.ItemTouchHelperAdapter;
import com.gcode.notes.notes.MyApplication;
import com.gcode.notes.ui.ActionExecutor;

import java.util.ArrayList;
import java.util.Collections;

public class MainAdapter extends RecyclerView.Adapter<BaseItemViewHolder> implements ItemTouchHelperAdapter {
    ArrayList<ContentBase> mData;
    View mRootView;
    Activity mActivity;
    RecyclerView mRecyclerView;
    LayoutInflater mInflater;

    public MainAdapter(Activity activity, RecyclerView recyclerView, ArrayList<ContentBase> data, View rooView) {

        mRecyclerView = recyclerView;
        mActivity = activity;
        mData = data;
        mRootView = rooView;
        mInflater = LayoutInflater.from(activity);
    }

    @Override
    public int getItemViewType(int position) {
        return mData.get(position).getType();
    }

    @Override
    public BaseItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == Constants.TYPE_NOTE) {
            return new NoteItemViewHolder(mActivity, mInflater.inflate(R.layout.note_item, parent, false), mData);
        } else {
            return new ListItemViewHolder(mActivity, mInflater.inflate(R.layout.list_item, parent, false), mData);
        }
    }

    @Override
    public void onBindViewHolder(final BaseItemViewHolder holder, int position) {
        holder.setStartState();
        ContentBase currentItem = mData.get(position);
        if (currentItem != null) {
            if (currentItem.getType() == Constants.TYPE_NOTE) {
                ((NoteData) currentItem).displayNote((NoteItemViewHolder) holder);
            } else {
                ((ListData) currentItem).displayList(mActivity, (ListItemViewHolder) holder);
            }
        }
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public RecyclerView getRecyclerView() {
        return mRecyclerView;
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
            MyDebugger.log("Invalid position in UpdateItem");
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

    public void removeItem(int position) {
        mData.remove(position);
        notifyItemRemoved(position);
    }

    public boolean addItem(int position, ContentBase item) {
        try {
            mData.add(position, item);
            notifyItemInserted(position);
            return true;
        } catch (IndexOutOfBoundsException exception) {
            MyDebugger.log("MainAdapter(): indexOutOfBoundsException caught.");
            mData.add(item);
            notifyItemInserted(getItemCount() - 1);
            return false;
        }
    }

    @Override
    public void onItemDismissFromBin(int position) {
        ActionExecutor.deleteNoteFromBin(mActivity, this, mData.get(position), position);
        dismissItem(position);
    }

    @Override
    public void onItemDismiss(int position) {
        ActionExecutor.popNoteDeletedSnackbar(mRootView, this, position, mData.get(position));
        dismissItem(position);
    }

    public void dismissItem(int position) {
        mData.remove(position);
        notifyItemRemoved(position);
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
    }

}
