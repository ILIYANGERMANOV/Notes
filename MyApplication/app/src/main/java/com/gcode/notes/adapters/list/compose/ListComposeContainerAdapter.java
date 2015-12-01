package com.gcode.notes.adapters.list.compose;

import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import com.gcode.notes.R;
import com.gcode.notes.ui.helpers.SnackbarHelper;
import com.gcode.notes.ui.helpers.VisibilityHelper;

public class ListComposeContainerAdapter extends BaseComposeContainerAdapter {
    View mLastDividerView;

    public ListComposeContainerAdapter(LinearLayout container, ScrollView scrollView, View lastDividerView) {
        super(container, scrollView);
        mLastDividerView = lastDividerView;
    }

    @Override
    protected void onRemoveItemRequestFocus(View previousItem, boolean wasFocused) {
        if (previousItem != null) {
            EditText editText = getEditTextFromView(previousItem);
            smoothScrollToView(previousItem);
            editText.requestFocus();
        }
    }

    @Override
    protected View createView() {
        return mInflater.inflate(R.layout.list_compose_item, mContainer, false);
    }

    @Override
    protected void onChecked(View parent) {
        removeInputItem(parent, true);
        EditText mEditText = getEditTextFromView(parent);
        mOtherContainerAdapter.addInputItem(mEditText.getText().toString(), false);
        if (!VisibilityHelper.isViewVisibleInScrollView(mLastDividerView, mScrollView)) {
            //show snackbar for item added to ticked
            SnackbarHelper.showShortSnackbar(mScrollView, R.string.item_added_to_done_snackbar_message);
        }
    }

    @Override
    protected boolean isListDataItemChecked() {
        return false;
    }
}
