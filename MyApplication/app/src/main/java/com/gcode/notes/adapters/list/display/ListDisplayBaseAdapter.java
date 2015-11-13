package com.gcode.notes.adapters.list.display;

import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckedTextView;

import com.gcode.notes.R;
import com.gcode.notes.data.ListDataItem;

import java.util.ArrayList;

public abstract class ListDisplayBaseAdapter extends ArrayAdapter<ListDataItem> {
    //TODO: REFACTOR AND OPTIMIZE (implement viewHolder pattern)
    ListDisplayBaseAdapter mOtherAdapter;
    boolean mIsDeactivated;

    public ListDisplayBaseAdapter(Context context, ArrayList<ListDataItem> data, boolean isDeactivated) {
        super(context, 0, data);
        mIsDeactivated = isDeactivated;
    }

    public ListDisplayBaseAdapter getOtherAdapter() {
        return mOtherAdapter;
    }

    public void setOtherAdapter(ListDisplayBaseAdapter otherAdapter) {
        this.mOtherAdapter = otherAdapter;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ListDataItem item = getItem(position);

        //create view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_data_item_row, parent, false);
        }

        //bind view
        CheckedTextView checkedTextView =
                (CheckedTextView) convertView.findViewById(R.id.list_data_item_row_checked_text_view);

        if (areItemsChecked()) {
            checkedTextView.setChecked(true);
            checkedTextView.setPaintFlags(checkedTextView.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        } else {
            checkedTextView.setChecked(false);
            checkedTextView.setPaintFlags(checkedTextView.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
        }
        if (mIsDeactivated) {
            convertView.setEnabled(false);
            checkedTextView.setEnabled(false);
        }
        checkedTextView.setText(item.getContent());

        return convertView;
    }

    abstract protected boolean areItemsChecked();
}
