package com.gcode.notes.adapters.main.viewholders;


import android.app.Activity;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;

import com.gcode.notes.R;
import com.gcode.notes.adapters.main.viewholders.listeners.ListItemOnClickListener;
import com.gcode.notes.data.base.ContentBase;
import com.gcode.notes.data.list.ListData;
import com.gcode.notes.extras.values.Constants;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

public class ListItemViewHolder extends BaseItemViewHolder {
    @Bind(R.id.list_item_container_layout)
    LinearLayout mContainerLayout;
    Runnable mSetOnClickListenerRunnable = new Runnable() {
        @Override
        public void run() {
            int itemPosition = getAdapterPosition();
            if (itemPosition != RecyclerView.NO_POSITION) {
                ListData listData = (ListData) mData.get(itemPosition);

                mBaseItemListener = new ListItemOnClickListener(mActivity, listData);
                itemView.setOnClickListener(mBaseItemListener);
                stopRepeatingTask();
                return;
            }
            mHandler.postDelayed(mSetOnClickListenerRunnable, Constants.MINIMUM_DELAY);
        }
    };

    public ListItemViewHolder(Activity activity, View itemView, ArrayList<ContentBase> data) {
        super(activity, itemView, data);
        ButterKnife.bind(this, itemView);
        mHandler = new Handler();
        startRepeatingTask();
    }

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
