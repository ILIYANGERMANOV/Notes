package com.gcode.notes.adapters.main.viewholders;

import android.app.Activity;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.gcode.notes.R;
import com.gcode.notes.adapters.main.viewholders.listeners.BaseItemListener;
import com.gcode.notes.data.base.ContentBase;
import com.gcode.notes.helper.ItemTouchHelperViewHolder;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;


public abstract class BaseItemViewHolder extends RecyclerView.ViewHolder implements ItemTouchHelperViewHolder {
    protected Activity mActivity;
    protected ArrayList<ContentBase> mData;
    protected Handler mHandler;
    protected BaseItemListener mBaseItemListener;
    @Bind(R.id.note_title_text_view)
    TextView mTitleTextView;
    @Bind(R.id.note_more_image_view)
    ImageView mMoreImageView;
    @Bind(R.id.reminder_text_view)
    TextView mReminderTextView;
    @Bind(R.id.attributes_divider)
    View mAttributesDivider;

    public BaseItemViewHolder(Activity activity, View itemView, ArrayList<ContentBase> data) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        mActivity = activity;
        mData = data;
    }

    public void clearAnimation() {
        itemView.clearAnimation();
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
        ((CardView) itemView).setCardBackgroundColor(
                ContextCompat.getColor(mActivity, R.color.card_view_background_selected));
    }

    @Override
    public void onItemClear() {
        ((CardView) itemView).setCardBackgroundColor(
                ContextCompat.getColor(mActivity, R.color.card_background));
    }

    public abstract void setStartState();

    public BaseItemListener getItemBaseListener() {
        return mBaseItemListener;
    }
}
