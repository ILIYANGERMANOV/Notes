package com.gcode.notes.adapters.list.display;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;

import com.gcode.notes.data.extras.ListDataItem;
import com.gcode.notes.ui.helpers.VisibilityHelper;
import com.gcode.notes.ui.snackbar.SnackbarHelper;
import com.gcode.notes.ui.snackbar.SnackbarMessages;
import com.linearlistview.LinearListView;

import java.util.ArrayList;


public class ListDisplayTickedAdapter extends ListDisplayBaseAdapter {
    Button mDoneButton;
    ScrollView mRootScrollView;
    TextView mDatesTextView;
    boolean mIsDoneHidden;
    LinearListView mLinearListView;

    public ListDisplayTickedAdapter(Context context, ArrayList<ListDataItem> data, boolean isDeactivated,
                                    Button doneButton, ScrollView rootScrollView, TextView datesTextView,
                                    LinearListView linearListView) {
        super(context, data, isDeactivated);
        mDoneButton = doneButton;
        mRootScrollView = rootScrollView;
        mDatesTextView = datesTextView;
        mLinearListView = linearListView;
    }

    public void setDoneHidden(boolean isDoneHidden) {
        mIsDoneHidden = isDoneHidden;
    }

    @Override
    public void add(ListDataItem item) {
        item.setChecked(true);
        super.add(item);
        if (mDoneButton.getVisibility() == View.GONE) {
            //show done button if its gone
            mDoneButton.setVisibility(View.VISIBLE);
        }
        if (!VisibilityHelper.isViewVisibleInScrollView(mDatesTextView, mRootScrollView) || mIsDoneHidden) {
            //dates text view is not visible show snackbar for item added
            SnackbarHelper.showShortSnackbar(mRootScrollView, SnackbarMessages.ITEM_ADDED_TO_DONE_MESSAGE);
            if (mIsDoneHidden) {
                //hide linear list view (whenever you add item it becomes visible)
                mLinearListView.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public void remove(ListDataItem object) {
        super.remove(object);
        if (getCount() == 0) {
            //hide doneButton
            mDoneButton.setVisibility(View.GONE);
        }
    }

    @Override
    protected boolean areItemsChecked() {
        return true;
    }
}
