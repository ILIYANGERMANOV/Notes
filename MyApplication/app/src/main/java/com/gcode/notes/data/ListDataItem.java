package com.gcode.notes.data;


import android.graphics.Paint;
import android.widget.CheckBox;
import android.widget.TextView;

import com.gcode.notes.adapters.viewholders.SingleListItemViewHolder;

public class ListDataItem {
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
        CheckBox checkBox = holder.getCheckBox();
        TextView textView = holder.getTextView();
        textView.setText(content);
        if (checked) {
            checkBox.setChecked(true);
            textView.setPaintFlags(textView.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        } else {
            checkBox.setChecked(false);
            textView.setPaintFlags(textView.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
        }
    }
}
