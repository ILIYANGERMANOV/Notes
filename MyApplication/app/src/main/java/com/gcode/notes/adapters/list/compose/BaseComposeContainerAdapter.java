package com.gcode.notes.adapters.list.compose;

import android.content.Context;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import com.gcode.notes.R;
import com.gcode.notes.data.extras.ListDataItem;
import com.gcode.notes.extras.MyDebugger;
import com.gcode.notes.extras.values.Constants;
import com.gcode.notes.listeners.list.ListInputOnEditorActionListener;
import com.gcode.notes.listeners.list.MyFocusListener;
import com.gcode.notes.listeners.list.RemoveListInputOnClickListener;

import java.util.ArrayList;

public abstract class BaseComposeContainerAdapter {
    LinearLayout mContainer;
    ScrollView mScrollView;
    LayoutInflater mInflater;
    BaseComposeContainerAdapter mOtherContainerAdapter;

    int mLastFocused;

    public BaseComposeContainerAdapter(LinearLayout container, ScrollView scrollView) {
        mContainer = container;
        mScrollView = scrollView;
        mInflater = (LayoutInflater) container.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mLastFocused = Constants.NO_FOCUS;
    }

    public void setOtherContainerAdapter(BaseComposeContainerAdapter otherContainerAdapter) {
        mOtherContainerAdapter = otherContainerAdapter;
    }

    public ScrollView getRootScrollView() {
        return mScrollView;
    }

    public void setLastFocused(int value) {
        mLastFocused = value;
    }

    public int getLastFocused() {
        return mLastFocused;
    }

    public void setLastFocused(View v) {
        int lastFocused = getViewId(v);
        if (lastFocused != Constants.ERROR) {
            mLastFocused = lastFocused;
            mOtherContainerAdapter.setLastFocused(Constants.NO_FOCUS);
        }
    }

    public void setFocusOnChild(int childId) {
        View child = mContainer.getChildAt(childId);
        if (child != null) {
            smoothScrollToView(child);
            child.requestFocus();
        } else {
            MyDebugger.log("Child to focus is null");
        }
    }

    public ArrayList<ListDataItem> getListDataItems(boolean filterEmpty) {
        return getListDataItemsFromContainer(mContainer, filterEmpty);
    }

    public void addInputItemAsFirst(View inputItem, boolean requestFocus) {
        mContainer.addView(inputItem, 0);
        inputItem.setTag(0);
        //inputItem is already added to the container at 0, so update from position 1 (previous first item has position 1)
        incrementItemsIdFromPosition(1);
        if (requestFocus) {
            onItemAddedRequestFocus(inputItem);
        }
    }

    public void addInputItem(View inputItem, boolean requestFocus) {
        addInputItem(getEditTextFromView(inputItem).getText().toString(), requestFocus);
    }

    public void addInputItem(@Nullable String inputItemContent, boolean requestFocus) {
        if (mContainer.getChildCount() != 0) {
            addInputItemAfterView(mContainer.getChildAt(mContainer.getChildCount() - 1), inputItemContent, requestFocus);
        } else {
            setupContainer(inputItemContent, requestFocus);
        }
    }

    public void addInputItemAfterView(View view, View inputItem, boolean requestFocus) {
        addInputItemAfterView(view, getEditTextFromView(inputItem).getText().toString(), requestFocus);
    }

    public void addInputItemAfterView(View view, @Nullable String inputItemContent, boolean requestFocus) {
        int previousItemId = getViewId(view);
        if (previousItemId == Constants.ERROR) return;

        View inputItem = createView();
        int inputItemPosition = previousItemId + 1;
        inputItem.setTag(inputItemPosition);
        //input item isn't added to container yet, so update items id from its position
        incrementItemsIdFromPosition(inputItemPosition);

        setupInputItemLayout(inputItem, inputItemContent);

        mContainer.addView(inputItem, inputItemPosition);
        if (requestFocus) {
            onItemAddedRequestFocus(inputItem);
        }
    }

    public void removeInputItem(View inputItem, boolean requestFocus) {
        int itemPosition = getViewId(inputItem);
        if (itemPosition == Constants.ERROR) return;
        boolean wasItemFocused = getEditTextFromView(inputItem).isFocused();
        //item isn't removed yet, so decrement items ids after its position
        decrementItemsIdAfterPosition(itemPosition);
        mContainer.removeView(inputItem);
        View previousItem = mContainer.getChildAt(itemPosition - 1);
        if (requestFocus) {
            onRemoveItemRequestFocus(previousItem, wasItemFocused);
        }
    }

    public EditText getEditTextFromView(View view) {
        return (EditText) view.findViewById(R.id.list_input_item_edit_text);
    }

    public int getViewId(View view) {
        Object idObject = view.getTag();
        if (idObject != null) {
            return (int) idObject;
        } else {
            MyDebugger.log("viewId not found error");
            return Constants.ERROR;
        }
    }

    public View getViewAtPosition(int position) {
        return mContainer.getChildAt(position);
    }

    public void smoothScrollToView(final View view) {
        mScrollView.postDelayed(new Runnable() {
            @Override
            public void run() {
                //getTop() returns relative coordinates from parent
                //that's why mContainer.getTop() is used and -view.getHeight() is in order to see previous item
                mScrollView.smoothScrollTo(0, view.getTop() + mContainer.getTop() - view.getHeight());
            }
        }, 20);
    }

    public int getItemCount() {
        return mContainer.getChildCount();
    }

    protected abstract View createView();

    protected abstract boolean isListDataItemChecked();

    protected void onRemoveItemRequestFocus(View previousItem, boolean wasItemFocused) {

    }

    protected void onItemAddedRequestFocus(final View inputItem) {
        final EditText mEditText = getEditTextFromView(inputItem);
        smoothScrollToView(inputItem);
        mEditText.requestFocus();
    }

    protected void onChecked(View parent) {

    }

    protected void onUnchecked(View parent) {

    }

    private void setupContainer(@Nullable String itemContent, boolean requestFocus) {
        View inputItem = createView();
        inputItem.setTag(0);
        setupInputItemLayout(inputItem, itemContent);
        mContainer.addView(inputItem);
        if (requestFocus) {
            onItemAddedRequestFocus(inputItem);
        }
    }

    protected void setupInputItemLayout(View inputItem, String inputItemContent) {
        EditText mEditText = getEditTextFromView(inputItem);
        mEditText.setOnEditorActionListener(new ListInputOnEditorActionListener(this));
        mEditText.setHorizontallyScrolling(false);
        mEditText.setMaxLines(Constants.MAX_LIST_INPUT_ITEM_LINES);
        mEditText.setOnFocusChangeListener(new MyFocusListener(this));

        ImageButton removeImageButton = (ImageButton) inputItem.findViewById(R.id.list_input_item_remove_button);
        removeImageButton.setOnClickListener(new RemoveListInputOnClickListener(this));

        final CheckBox checkBox = (CheckBox) inputItem.findViewById(R.id.list_input_item_check_box);
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                View parent = (View) buttonView.getParent();
                if (isChecked) {
                    onChecked(parent);
                } else {
                    onUnchecked(parent);
                }
            }
        });

        if (inputItemContent != null) {
            mEditText.setText(inputItemContent);
        }
    }

    private ArrayList<ListDataItem> getListDataItemsFromContainer(LinearLayout container, boolean filterEmpty) {
        ArrayList<ListDataItem> mList = new ArrayList<>();

        int childCount = container.getChildCount();

        for (int i = 0; i < childCount; ++i) {
            View child = container.getChildAt(i);
            if (child != null) {
                ListDataItem mItem = getListDataItemFromChild(child);
                if (mItem != null) {
                    mList.add(mItem);
                } else {
                    if (!filterEmpty) {
                        mList.add(new ListDataItem("", isListDataItemChecked()));
                    }
                }
            }
        }
        return mList;
    }

    private ListDataItem getListDataItemFromChild(View child) {
        EditText mEditText = getEditTextFromView(child);
        String content = mEditText.getText().toString();
        if (content.trim().length() == 0) return null;
        return new ListDataItem(content, isListDataItemChecked());
    }

    private void incrementItemsIdFromPosition(int startPosition) {
        for (int i = startPosition; i < mContainer.getChildCount(); ++i) {
            incrementViewId(mContainer.getChildAt(i));
        }
    }

    private void incrementViewId(View view) {
        int position = getViewId(view);
        if (position == Constants.ERROR) return;
        view.setTag(position + 1);
    }

    private void decrementItemsIdAfterPosition(int position) {
        for (int i = position + 1; i < mContainer.getChildCount(); ++i) {
            decrementViewId(mContainer.getChildAt(i));
        }
    }

    private void decrementViewId(View view) {
        int position = getViewId(view);
        if (position == Constants.ERROR) return;
        view.setTag(position - 1);
    }
}
