package com.gcode.notes.adapters.viewholders.main;

import android.app.Activity;
import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.gcode.notes.R;
import com.gcode.notes.data.main.ContentBase;
import com.gcode.notes.helper.ItemTouchHelperViewHolder;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;


public abstract class BaseItemViewHolder extends RecyclerView.ViewHolder implements ItemTouchHelperViewHolder {
    //TODO: OPTIMIZE
    @Bind(R.id.note_title_text_view)
    TextView mTitleTextView;

    @Bind(R.id.note_more_image_view)
    ImageView mMoreImageView;

    @Bind(R.id.reminder_text_view)
    TextView mReminderTextView;

    @Bind(R.id.attributes_divider)
    View mAttributesDivider;

    Activity mActivity;
    ArrayList<ContentBase> mData;

    public BaseItemViewHolder(Activity activity, View itemView, ArrayList<ContentBase> data) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        mActivity = activity;
        mData = data;
    }

    public TextView getTitleTextView() {
        return mTitleTextView;
    }

    public TextView getReminderTextView() {
        return mReminderTextView;
    }

    public View getAttributesDivider() {
        return mAttributesDivider;
    }

    public ImageView getMoreImageView() {
        return mMoreImageView;
    }

    @Override
    public void onItemSelected() {
        ((CardView) itemView).setCardBackgroundColor(Color.LTGRAY);
    }

    @Override
    public void onItemClear() {
        ((CardView) itemView).setCardBackgroundColor(Color.WHITE);
    }

    public abstract void setStartState();
}
