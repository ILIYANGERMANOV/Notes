package com.gcode.notes.adapters.viewholders.main;


import android.app.Activity;
import android.graphics.Color;
import android.os.Handler;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;

import com.gcode.notes.R;
import com.gcode.notes.data.ContentBase;
import com.gcode.notes.data.ListData;
import com.gcode.notes.listeners.main.ListItemOnClickListener;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

public class ListItemViewHolder extends BaseItemViewHolder {
    @Bind(R.id.list_item_container_layout)
    LinearLayout mContainerLayout;

    @Bind(R.id.list_item_card_view)
    CardView mCardView;

    Handler mHandler;

    public ListItemViewHolder(Activity activity, View itemView, ArrayList<ContentBase> data) {
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
                ListData listData = (ListData) mData.get(itemPosition);
                itemView.setOnClickListener(new ListItemOnClickListener(mActivity, listData));
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

    public LinearLayout getContainerLayout() {
        return mContainerLayout;
    }
}
