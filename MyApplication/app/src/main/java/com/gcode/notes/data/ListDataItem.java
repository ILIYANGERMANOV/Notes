package com.gcode.notes.data;


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
}
