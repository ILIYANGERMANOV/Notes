package com.gcode.notes.adapters.viewholders.main;

import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.gcode.notes.R;
import com.gcode.notes.data.ContentBase;
import com.gcode.notes.extras.MyDebugger;
import com.gcode.notes.helper.ItemTouchHelperViewHolder;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;


public abstract class BaseItemViewHolder extends RecyclerView.ViewHolder implements ItemTouchHelperViewHolder {
    @Bind(R.id.note_title_text_view)
    TextView mTitleTextView;

    @Bind(R.id.title_image_button)
    ImageButton mTitleImageButton;

    @Bind(R.id.reminder_text_view)
    TextView mReminderTextView;

    @Bind(R.id.attributes_divider)
    View mAttributesDivider;

    Context mContext;
    ArrayList<ContentBase> mData;

    public BaseItemViewHolder(Context context, View itemView, ArrayList<ContentBase> data) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        mContext = context;
        mData = data;
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

    public abstract void setStartState();
}
