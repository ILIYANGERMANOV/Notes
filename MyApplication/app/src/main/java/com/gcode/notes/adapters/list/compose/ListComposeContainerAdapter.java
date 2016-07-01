package com.gcode.notes.adapters.list.compose;

import android.app.Activity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;

import com.gcode.notes.R;
import com.gcode.notes.extras.utils.MyUtils;
import com.gcode.notes.ui.helpers.SnackbarHelper;
import com.gcode.notes.ui.helpers.VisibilityHelper;
import com.jmedeisis.draglinearlayout.DragLinearLayout;

public class ListComposeContainerAdapter extends BaseComposeContainerAdapter
        implements DragLinearLayout.OnDragEventListener {

    Activity mActivity;
    View mLastDividerView;

    public ListComposeContainerAdapter(Activity activity, DragLinearLayout container,
                                       ScrollView scrollView, View lastDividerView) {

        super(container, scrollView);
        mActivity = activity;
        mLastDividerView = lastDividerView;
        mContainer.setOnDragEventListener(this);
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
    protected void onItemAddedToContainer(View inputItem) {
        ImageView dragImageVIew = (ImageView) inputItem.findViewById(R.id.list_input_item_drag_image_view);
        mContainer.setViewDraggable(inputItem, dragImageVIew);
    }

    @Override
    protected View createView() {
        return mInflater.inflate(R.layout.list_compose_item, mContainer, false);
    }

    @Override
    protected void onChecked(View parent) {
        removeInputItem(parent, true);
        EditText mEditText = getEditTextFromView(parent);
        BaseComposeContainerAdapter otherContainerAdapter = mOtherContainerReference.get();
        if (otherContainerAdapter == null) return; //reference has been cleared, abort adding
        otherContainerAdapter.addInputItem(mEditText.getText().toString(), false);
        if (!VisibilityHelper.isViewVisibleInScrollView(mLastDividerView, mScrollView)) {
            //show snackbar for item added to ticked
            SnackbarHelper.showShortSnackbar(mScrollView, R.string.item_added_to_done_snackbar_message);
        }
    }

    @Override
    protected boolean isListDataItemChecked() {
        return false;
    }

    @Override
    public void onSwap(View firstView, int firstPosition, View secondView, int secondPosition) {
        firstView.setTag(secondPosition);
        secondView.setTag(firstPosition);
    }

    @Override
    public void onDragStart(View itemView) {
        //drag started, hide keyboard if shown
        //TODO: fix issue with getting bad focus
        MyUtils.hideSoftInput(mActivity);
    }
}
