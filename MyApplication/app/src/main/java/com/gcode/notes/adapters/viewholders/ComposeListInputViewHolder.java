package com.gcode.notes.adapters.viewholders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;

import com.gcode.notes.R;

import butterknife.Bind;
import butterknife.ButterKnife;


public class ComposeListInputViewHolder extends RecyclerView.ViewHolder {
    @Bind(R.id.list_input_item_edit_text)
    EditText mEdiText;

    @Bind(R.id.list_input_item_check_box)
    CheckBox mCheckBox;

    @Bind(R.id.list_input_item_remove_button)
    ImageButton mRemoveButton;

    @Bind(R.id.list_input_item_reorder_button)
    ImageButton mReorderButton;


    public ComposeListInputViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public EditText getEdiText() {
        return mEdiText;
    }

    public CheckBox getCheckBox() {
        return mCheckBox;
    }

    public ImageButton getRemoveButton() {
        return mRemoveButton;
    }

    public ImageButton getReorderButton() {
        return mReorderButton;
    }
}
