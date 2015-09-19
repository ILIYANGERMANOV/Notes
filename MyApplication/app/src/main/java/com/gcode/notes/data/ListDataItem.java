package com.gcode.notes.data;


import android.graphics.Paint;
import android.widget.CheckedTextView;

import com.gcode.notes.adapters.viewholders.SingleListItemViewHolder;

import java.io.Serializable;

public class ListDataItem implements Serializable {
    String content;
    boolean checked;

    public ListDataItem(String content, boolean checked) {
        this.content = content;
        this.checked = checked;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void display(SingleListItemViewHolder holder) {
        CheckedTextView mCheckedTextView = holder.getCheckedTextView();
        mCheckedTextView.setText(content);
        if (checked) {
            mCheckedTextView.setChecked(true);
            mCheckedTextView.setPaintFlags(mCheckedTextView.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        } else {
            mCheckedTextView.setChecked(false);
            mCheckedTextView.setPaintFlags(mCheckedTextView.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
        }
    }
}
