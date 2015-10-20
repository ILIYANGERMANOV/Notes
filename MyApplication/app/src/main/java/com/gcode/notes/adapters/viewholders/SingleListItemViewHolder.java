package com.gcode.notes.adapters.viewholders;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import com.gcode.notes.R;
import com.gcode.notes.adapters.ListItemAdapter;
import com.gcode.notes.data.ListData;
import com.gcode.notes.extras.Constants;
import com.gcode.notes.extras.MyDebugger;
import com.gcode.notes.listeners.main.ListItemOnClickListener;

import butterknife.Bind;
import butterknife.ButterKnife;

public class SingleListItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    @Bind(R.id.single_list_data_item_check_box)
    CheckBox mCheckBox;

    @Bind(R.id.single_list_data_item_text_view)
    TextView mTextView;

    ListItemAdapter mAdapter;

    public SingleListItemViewHolder(Activity activity, View itemView, ListData listData, ListItemAdapter adapter, int calledFrom) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        if (calledFrom == Constants.CALLED_FROM_MAIN) {
            itemView.setOnClickListener(new ListItemOnClickListener(activity, listData));
            mCheckBox.setClickable(false);
        } else {
            //called from display
            mCheckBox.setOnClickListener(this);
            mAdapter = adapter;
        }
    }

    public CheckBox getCheckBox() {
        return mCheckBox;
    }

    public TextView getTextView() {
        return mTextView;
    }

    @Override
    public void onClick(View v) {
        int itemPosition = getAdapterPosition();
        if (itemPosition != RecyclerView.NO_POSITION) {
            ListItemAdapter otherAdapter = mAdapter.getOtherAdapter();
            otherAdapter.add(mAdapter.getItemAtPosition(itemPosition));
            mAdapter.remove(itemPosition);
        } else {
            MyDebugger.log("onClick() SingleListItemViewHolder NO_POSITION");
        }
    }
}
