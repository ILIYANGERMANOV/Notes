package com.gcode.notes.adapters;


import android.content.Context;
import android.support.v4.view.MotionEventCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.gcode.notes.R;
import com.gcode.notes.adapters.viewholders.main.BaseItemViewHolder;
import com.gcode.notes.adapters.viewholders.main.ListItemViewHolder;
import com.gcode.notes.adapters.viewholders.main.NoteItemViewHolder;
import com.gcode.notes.data.ContentBase;
import com.gcode.notes.data.ListData;
import com.gcode.notes.data.NoteData;
import com.gcode.notes.extras.Constants;
import com.gcode.notes.helper.ItemTouchHelperAdapter;
import com.gcode.notes.helper.OnStartDragListener;
import com.gcode.notes.notes.MyApplication;
import com.gcode.notes.ui.ActionExecutor;

import java.util.ArrayList;
import java.util.Collections;

public class NotesAdapter extends RecyclerView.Adapter<BaseItemViewHolder> implements ItemTouchHelperAdapter {
    private final OnStartDragListener mDragStartListener;
    ArrayList<ContentBase> mData;
    View mRootView;
    Context mContext;
    RecyclerView mRecyclerView;

    public NotesAdapter(Context context, RecyclerView recyclerView, ArrayList<ContentBase> data,
                        OnStartDragListener dragStartListener, View rooView) {

        mRecyclerView = recyclerView;
        mContext = context;
        mData = data;
        mDragStartListener = dragStartListener;
        mRootView = rooView;
    }

    @Override
    public int getItemViewType(int position) {
        return mData.get(position).getType();
    }

    @Override
    public BaseItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        if (viewType == Constants.TYPE_NOTE) {
            return new NoteItemViewHolder(mContext, inflater.inflate(R.layout.note_item, parent, false), mData);
        } else {
            return new ListItemViewHolder(mContext, inflater.inflate(R.layout.list_item, parent, false), mData);
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
                ((ListData) currentItem).displayList(mContext, (ListItemViewHolder) holder);
            }
        }
        //Start a drag whenever the view is touched
        holder.getTitleImageButton().setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (MotionEventCompat.getActionMasked(event) == MotionEvent.ACTION_DOWN) {
                    mDragStartListener.onStartDrag(holder);
                }
                return false;
            }
        });
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public void addItem(int position, ContentBase item) {
        mData.add(position, item);
        notifyItemInserted(position);
    }

    public void addItem(ContentBase item) {
        mData.add(0, item);
        notifyItemInserted(0);
    }

    @Override
    public void onItemDismissFromBin(int position) {
        ActionExecutor.deleteNoteFromBin(mContext, this, mData.get(position), position);
        dismissItem(position);
    }

    @Override
    public void onItemDismiss(int position) {
        ActionExecutor.popUndoSnackbar(mRootView, mRecyclerView, this, position, mData.get(position));
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
