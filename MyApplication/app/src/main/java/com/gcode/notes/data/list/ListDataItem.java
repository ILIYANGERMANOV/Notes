package com.gcode.notes.data.list;


public class ListDataItem {
    String content;
    boolean checked;

    public ListDataItem(String content, boolean checked) {
        this.content = content;
        this.checked = checked;
    }

    public ListDataItem(ListDataItem other) {
        content = other.content;
        checked = other.checked;
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
