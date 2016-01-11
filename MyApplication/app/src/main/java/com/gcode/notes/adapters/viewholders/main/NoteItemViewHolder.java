package com.gcode.notes.adapters.viewholders.main;


import android.app.Activity;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gcode.notes.R;
import com.gcode.notes.adapters.viewholders.main.listeners.BaseItemListener;
import com.gcode.notes.adapters.viewholders.main.listeners.NoteItemOnClickListener;
import com.gcode.notes.data.base.ContentBase;
import com.gcode.notes.data.NoteData;
import com.gcode.notes.extras.values.Constants;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

public class NoteItemViewHolder extends BaseItemViewHolder {
    @Bind(R.id.note_description_text_view)
    TextView mContentTextView;

    @Bind(R.id.note_item_images_container)
    LinearLayout mImagesContainer;

    @Bind(R.id.voice_image_view)
    ImageView mVoiceImageView;

    Handler mHandler;

    NoteItemOnClickListener mNoteItemOnClickListener;

    @Override
    public BaseItemListener getItemBaseListener() {
        return mNoteItemOnClickListener;
    }

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
                mNoteItemOnClickListener = new NoteItemOnClickListener(mActivity, noteData);
                itemView.setOnClickListener(mNoteItemOnClickListener);
                stopRepeatingTask();
                return;
            }
            mHandler.postDelayed(mSetOnClickListener, Constants.MINIMUM_DELAY);
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
        startRepeatingTask();
    }

    public ImageView getVoiceImageView() {
        return mVoiceImageView;
    }

    public LinearLayout getImagesContainer() {
        return mImagesContainer;
    }

    public TextView getDescriptionTextView() {
        return mContentTextView;
    }
}
