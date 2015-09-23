package com.gcode.notes.adapters.viewholders.main;


import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.gcode.notes.R;
import com.gcode.notes.data.ContentBase;
import com.gcode.notes.data.ListData;
import com.gcode.notes.extras.MyDebugger;
import com.gcode.notes.listeners.main.ListItemOnClickListener;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

public class ListItemViewHolder extends BaseItemViewHolder {
    @Bind(R.id.list_item_recycler_view)
    RecyclerView mRecyclerView;

    @Bind(R.id.list_item_card_view)
    CardView mCardView;

    Handler mHandler;

    public ListItemViewHolder(Context context, View itemView, ArrayList<ContentBase> data) {
        super(context, itemView, data);
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
                itemView.setOnClickListener(new ListItemOnClickListener(mContext, listData));
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
    }

    public RecyclerView getRecyclerView() {
        return mRecyclerView;
    }
}
