package com.gcode.notes.data;

import android.widget.CheckBox;
import android.widget.EditText;

public class ListInputItem {
    EditText mContentEditText;
    CheckBox mCheckBox;

    public ListInputItem() {
        mContentEditText = null;
        mCheckBox = null;
    }

    public ListDataItem getListDataItem() {
        String content = mContentEditText.getText().toString();
        if (validate(content)) {
            return new ListDataItem(content, mCheckBox.isChecked());
        } else {
            return null;
        }
    }

    private boolean validate(String content) {
        return content.trim().length() > 0;
    }

    public CheckBox getCheckBox() {
        return mCheckBox;
    }

    public void setCheckBox(CheckBox mCheckBox) {
        this.mCheckBox = mCheckBox;
    }

    public EditText getContentEditText() {
        return mContentEditText;
    }

    public void setContentEditText(EditText mContentEditText) {
        this.mContentEditText = mContentEditText;
    }
}
