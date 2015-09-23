package com.gcode.notes.adapters.viewholders;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CheckedTextView;

import com.gcode.notes.R;
import com.gcode.notes.data.ListData;
import com.gcode.notes.extras.Constants;
import com.gcode.notes.listeners.main.ListItemOnClickListener;

import butterknife.Bind;
import butterknife.ButterKnife;

public class SingleListItemViewHolder extends RecyclerView.ViewHolder {
    @Bind(R.id.single_list_data_checked_text_view)
    CheckedTextView mCheckedTextView;

    public SingleListItemViewHolder(Context context, View itemView, ListData listData, int calledFrom) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        if(calledFrom == Constants.CALLED_FROM_MAIN) {
            itemView.setOnClickListener(new ListItemOnClickListener(context, listData));
        }
    }

    public CheckedTextView getCheckedTextView() {
        return mCheckedTextView;
    }
}
