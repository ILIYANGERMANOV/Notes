package com.gcode.notes.adapters.custom;

import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import com.gcode.notes.R;
import com.gcode.notes.ui.snackbar.SnackbarMessages;
import com.gcode.notes.ui.snackbar.SnackbarHelper;
import com.gcode.notes.ui.VisibilityHelper;

public class ListInputContainerAdapter extends BaseInputContainerAdapter {
    View mLastDividerView;

    public ListInputContainerAdapter(LinearLayout container, ScrollView scrollView, View lastDividerView) {
        super(container, scrollView);
        mLastDividerView = lastDividerView;
    }

    @Override
    protected void onRemoveItemRequestFocus(View previousItem, boolean wasFocused) {
        EditText mEditText = getEditTextFromView(previousItem);
        mEditText.requestFocus();
    }

    @Override
    protected View createView() {
        return mInflater.inflate(R.layout.list_input_item, mContainer, false);
    }

    @Override
    protected void onChecked(View parent) {
        removeInputItem(parent);
        EditText mEditText = getEditTextFromView(parent);
        mOtherContainerAdapter.addInputItem(mEditText.getText().toString(), false);
        if (!VisibilityHelper.isViewVisibleInScrollView(mLastDividerView, mScrollView)) {
            //show snackbar for item added to ticked
            SnackbarHelper.showShortSnackbar(mScrollView, SnackbarMessages.ITEM_ADDED_TO_DONE_MESSAGE);
        }
    }

    @Override
    protected boolean isListDataItemChecked() {
        return false;
    }
}
