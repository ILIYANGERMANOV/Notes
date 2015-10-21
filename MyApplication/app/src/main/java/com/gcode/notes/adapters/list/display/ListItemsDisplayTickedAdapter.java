package com.gcode.notes.adapters.list.display;


import android.view.View;
import android.widget.Button;

import com.gcode.notes.data.ListDataItem;

import java.util.ArrayList;

public class ListItemsDisplayTickedAdapter extends ListItemsDisplayAdapter {
    Button mDoneButton;

    public ListItemsDisplayTickedAdapter(ArrayList<ListDataItem> data, Button doneButton) {
        super(data);
        mDoneButton = doneButton;
    }

    public void add(ListDataItem item) {
        item.setChecked(true);
        mData.add(item);
        notifyItemInserted(getItemCount() - 1);
        if (mDoneButton.getVisibility() == View.GONE) {
            //show done button if its gone
            mDoneButton.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void remove(int position) {
        super.remove(position);
        if (getItemCount() == 0) {
            //hide doneButton
            mDoneButton.setVisibility(View.GONE);
        }
    }
}
