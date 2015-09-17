package com.gcode.notes.adapters.viewholders;

import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.gcode.notes.R;
import com.gcode.notes.helper.ItemTouchHelperViewHolder;

import butterknife.Bind;
import butterknife.ButterKnife;


public class BaseItemViewHolder extends RecyclerView.ViewHolder implements ItemTouchHelperViewHolder {
    @Bind(R.id.note_title_text_view)
    TextView mTitleTextView;

    @Bind(R.id.title_image_button)
    ImageButton mTitleImageButton;

    @Bind(R.id.reminder_text_view)
    TextView mReminderTextView;

    public BaseItemViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public TextView getTitleTextView() {
        return mTitleTextView;
    }

    public ImageButton getTitleImageButton() {
        return mTitleImageButton;
    }

    public TextView getReminderTextView() {
        return mReminderTextView;
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
