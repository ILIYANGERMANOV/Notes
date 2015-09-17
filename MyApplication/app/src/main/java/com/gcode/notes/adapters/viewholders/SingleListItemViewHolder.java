package com.gcode.notes.adapters.viewholders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CheckedTextView;

import com.gcode.notes.R;

import butterknife.Bind;
import butterknife.ButterKnife;

public class SingleListItemViewHolder extends RecyclerView.ViewHolder {
    @Bind(R.id.single_list_data_checked_text_view)
    CheckedTextView mCheckedTextView;

    public SingleListItemViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public CheckedTextView getCheckedTextView() {
        return mCheckedTextView;
    }
}
