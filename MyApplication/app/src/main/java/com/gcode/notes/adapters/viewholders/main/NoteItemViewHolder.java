package com.gcode.notes.adapters.viewholders.main;


import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.gcode.notes.R;
import com.gcode.notes.data.ContentBase;
import com.gcode.notes.data.NoteData;
import com.gcode.notes.listeners.main.NoteItemOnClickListener;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

public class NoteItemViewHolder extends BaseItemViewHolder {

    @Bind(R.id.note_description_text_view)
    TextView mContentTextView;

    @Bind(R.id.note_item_image_view)
    ImageView mAttachedImageView;

    @Bind(R.id.voice_image_view)
    ImageView mVoiceImageView;

    @Bind(R.id.note_item_card_view)
    CardView mCardView;

    Handler mHandler;

    public NoteItemViewHolder(Activity activity, final View itemView, ArrayList<ContentBase> data) {
        super(activity, itemView, data);
        ButterKnife.bind(this, itemView);
        mHandler = new Handler();
        startRepeatingTask();
    }

    Runnable mSetOnClickListener = new Runnable() {
        @Override
        public void run() {
            int itemPosition = getAdapterPosition();
            if (itemPosition != RecyclerView.NO_POSITION) {
                NoteData noteData = (NoteData) mData.get(itemPosition);
                itemView.setOnClickListener(new NoteItemOnClickListener(mActivity, noteData));
                stopRepeatingTask();
                return;
            }
            mHandler.postDelayed(mSetOnClickListener, 50);
        }
    };

    void startRepeatingTask() {
        mSetOnClickListener.run();
    }

    void stopRepeatingTask() {
        mHandler.removeCallbacks(mSetOnClickListener);
    }

    @Override
    public void setStartState() {
        mCardView.setCardBackgroundColor(Color.WHITE);
        startRepeatingTask();
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
}
