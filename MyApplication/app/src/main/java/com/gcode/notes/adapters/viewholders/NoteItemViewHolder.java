package com.gcode.notes.adapters.viewholders;


import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.gcode.notes.R;

import butterknife.Bind;
import butterknife.ButterKnife;

public class NoteItemViewHolder extends BaseItemViewHolder {

    @Bind(R.id.note_description_text_view)
    TextView mContentTextView;

    @Bind(R.id.note_item_image_view)
    ImageView mAttachedImageView;

    @Bind(R.id.voice_image_view)
    ImageView mVoiceImageView;

    @Bind(R.id.attributes_divider)
    View mAttributesDivider;


    public NoteItemViewHolder(View itemView) {
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

    public View getAttributesDivider() {
        return mAttributesDivider;
    }
}
