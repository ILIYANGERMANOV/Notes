package com.gcode.notes.adapters;


import android.content.Context;
import android.graphics.Color;
import android.support.v4.view.MotionEventCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.gcode.notes.R;
import com.gcode.notes.controllers.BaseController;
import com.gcode.notes.data.ContentBase;
import com.gcode.notes.data.NoteData;
import com.gcode.notes.extras.Constants;
import com.gcode.notes.helper.ItemTouchHelperAdapter;
import com.gcode.notes.helper.ItemTouchHelperViewHolder;
import com.gcode.notes.helper.OnStartDragListener;
import com.gcode.notes.notes.MyApplication;
import com.gcode.notes.ui.ActionExecutor;

import java.util.ArrayList;
import java.util.Collections;

import butterknife.Bind;
import butterknife.ButterKnife;

public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.MyViewHolder> implements ItemTouchHelperAdapter {
    ArrayList<ContentBase> mData;
    View mRootView;

    Context mContext;

    private final OnStartDragListener mDragStartListener;

    public NotesAdapter(Context context, ArrayList<ContentBase> data,
                        OnStartDragListener dragStartListener, View rooView) {

        mContext = context;
        mData = data;
        mDragStartListener = dragStartListener;
        mRootView = rooView;
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
        ActionExecutor.popUndoSnackbar(mRootView, this, position, mData.get(position));
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

    @Override
    public NotesAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        return new MyViewHolder(inflater.inflate(R.layout.note_item, parent, false));
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        ContentBase currentItem = mData.get(position);
        if (currentItem != null) {
            if (currentItem.getType() == Constants.TYPE_NOTE) {
                ((NoteData) currentItem).displayNote(holder);
            } else {
                //TODO: list here
            }
        }
        //Start a drag whenever the view is touched
        holder.mTitleImageButton.setOnTouchListener(new View.OnTouchListener() {
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

    public void updateContent(ArrayList<ContentBase> newData) {
        mData.clear();
        mData.addAll(newData);
        notifyDataSetChanged();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements ItemTouchHelperViewHolder {
        @Bind(R.id.note_title_text_view)
        TextView mTitleTextView;

        @Bind(R.id.note_description_text_view)
        TextView mContentTextView;

        @Bind(R.id.note_item_image_view)
        ImageView mAttachedImageView;

        @Bind(R.id.voice_image_view)
        ImageView mVoiceImageView;

        @Bind(R.id.reminder_text_view)
        TextView mReminderTextView;

        @Bind(R.id.attributes_divider)
        View mAttributesDivider;

        @Bind(R.id.title_image_button)
        ImageButton mTitleImageButton;

        public MyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public ImageView getVoiceImageView() {
            return mVoiceImageView;
        }

        public ImageView getAttachedImageView() {
            return mAttachedImageView;
        }

        public TextView getContentTextView() {
            return mContentTextView;
        }

        public TextView getReminderTextView() {
            return mReminderTextView;
        }

        public TextView getTitleTextView() {
            return mTitleTextView;
        }

        public View getAttributesDivider() {
            return mAttributesDivider;
        }

        @Override
        public void onItemSelected() {
            ((CardView) itemView).setCardBackgroundColor(Color.LTGRAY);
        }

        @Override
        public void onItemClear() {
            ((CardView) itemView).setCardBackgroundColor(Color.WHITE);
        }
    }
}
