package com.gcode.notes.adapters.viewholders.list;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import com.gcode.notes.R;

import butterknife.Bind;
import butterknife.ButterKnife;

public class ListItemBaseViewHolder extends RecyclerView.ViewHolder {
    @Bind(R.id.list_data_item_check_box)
    CheckBox mCheckBox;

    @Bind(R.id.list_data_item_text_view)
    TextView mTextView;

    public ListItemBaseViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public CheckBox getCheckBox() {
        return mCheckBox;
    }

    public TextView getTextView() {
        return mTextView;
    }

}
