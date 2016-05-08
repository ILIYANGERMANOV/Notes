package com.gcode.notes.adapters.list.display;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.ScrollView;

import com.gcode.notes.R;
import com.gcode.notes.data.list.ListDataItem;
import com.gcode.notes.ui.helpers.SnackbarHelper;
import com.gcode.notes.ui.helpers.VisibilityHelper;
import com.linearlistview.LinearListView;

import java.util.ArrayList;


public class ListDisplayTickedAdapter extends ListDisplayBaseAdapter {
    Button mDoneButton;
    ScrollView mRootScrollView;
    View mLimitView;
    boolean mIsDoneHidden;
    LinearListView mLinearListView;

    public ListDisplayTickedAdapter(Context context, ArrayList<ListDataItem> data, boolean isDeactivated,
                                    Button doneButton, ScrollView rootScrollView, View limitView,
                                    LinearListView linearListView) {
        super(context, data, isDeactivated);
        mDoneButton = doneButton;
        mRootScrollView = rootScrollView;
        mLimitView = limitView;
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
        if (!VisibilityHelper.isViewVisibleInScrollView(mLimitView, mRootScrollView) || mIsDoneHidden) {
            //dates text view is not visible show snackbar for item added
            SnackbarHelper.showShortSnackbar(mRootScrollView, R.string.item_added_to_done_snackbar_message);
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
