package com.gcode.notes.adapters.list.display;


import android.view.View;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;

import com.gcode.notes.data.ListDataItem;
import com.gcode.notes.ui.snackbar.SnackbarMessages;
import com.gcode.notes.ui.snackbar.SnackbarHelper;
import com.gcode.notes.ui.VisibilityHelper;

import java.util.ArrayList;

public class ListItemsDisplayTickedAdapter extends ListItemsDisplayBaseAdapter {
    Button mDoneButton;
    ScrollView mRootScrollView;
    TextView mDatesTextView;

    public ListItemsDisplayTickedAdapter(ArrayList<ListDataItem> data, Button doneButton,
                                         ScrollView rootScrollView, TextView datesTextView) {
        super(data);
        mDoneButton = doneButton;
        mRootScrollView = rootScrollView;
        mDatesTextView = datesTextView;
    }

    public void add(ListDataItem item) {
        item.setChecked(true);
        mData.add(item);
        notifyItemInserted(getItemCount() - 1);
        if (mDoneButton.getVisibility() == View.GONE) {
            //show done button if its gone
            mDoneButton.setVisibility(View.VISIBLE);
        }
        if (!VisibilityHelper.isViewVisibleInScrollView(mDatesTextView, mRootScrollView)) {
            //dates text view is not visible show snackbar for item added
            SnackbarHelper.showShortSnackbar(mRootScrollView, SnackbarMessages.ITEM_ADDED_TO_DONE_MESSAGE);
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
