package com.gcode.notes.adapters.viewholders.main;


import android.app.Activity;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;

import com.gcode.notes.R;
import com.gcode.notes.adapters.viewholders.main.listeners.BaseItemListener;
import com.gcode.notes.adapters.viewholders.main.listeners.ListItemOnClickListener;
import com.gcode.notes.data.note.base.ContentBase;
import com.gcode.notes.data.note.list.ListData;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

public class ListItemViewHolder extends BaseItemViewHolder {
    @Bind(R.id.list_item_container_layout)
    LinearLayout mContainerLayout;

    Handler mHandler;

    ListItemOnClickListener mListItemOnClickListener;

    @Override
    public BaseItemListener getItemBaseListener() {
        return mListItemOnClickListener;
    }

    public ListItemViewHolder(Activity activity, View itemView, ArrayList<ContentBase> data) {
        super(activity, itemView, data);
        ButterKnife.bind(this, itemView);
        mHandler = new Handler();
        startRepeatingTask();
    }

    Runnable mSetOnClickListenerRunnable = new Runnable() {
        @Override
        public void run() {
            int itemPosition = getAdapterPosition();
            if (itemPosition != RecyclerView.NO_POSITION) {
                ListData listData = (ListData) mData.get(itemPosition);

                mListItemOnClickListener = new ListItemOnClickListener(mActivity, listData);
                itemView.setOnClickListener(mListItemOnClickListener);
                stopRepeatingTask();
                return;
            }
            mHandler.postDelayed(mSetOnClickListenerRunnable, 50);
        }
    };

    void startRepeatingTask() {
        mSetOnClickListenerRunnable.run();
    }

    void stopRepeatingTask() {
        mHandler.removeCallbacks(mSetOnClickListenerRunnable);
    }

    @Override
    public void setStartState() {
        startRepeatingTask();
    }

    public LinearLayout getContainerLayout() {
        return mContainerLayout;
    }
}
