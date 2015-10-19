package com.gcode.notes.adapters.custom;

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
import com.gcode.notes.data.ListDataItem;
import com.gcode.notes.extras.Constants;
import com.gcode.notes.extras.MyDebugger;
import com.gcode.notes.listeners.list.ListInputOnKeyListener;
import com.gcode.notes.listeners.list.MyFocusListener;
import com.gcode.notes.listeners.list.RemoveListInputOnClickListener;

import java.util.ArrayList;

public abstract class BaseInputContainerAdapter {
    LinearLayout mContainer;
    ScrollView mScrollView;
    LayoutInflater mInflater;
    BaseInputContainerAdapter mOtherContainerAdapter;

    int mLastFocused;

    public BaseInputContainerAdapter(LinearLayout container, ScrollView scrollView) {
        mContainer = container;
        mScrollView = scrollView;
        mInflater = (LayoutInflater) container.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mLastFocused = Constants.NO_FOCUS;
    }

    public BaseInputContainerAdapter getOtherContainerAdapter() {
        return mOtherContainerAdapter;
    }

    public void setOtherContainerAdapter(BaseInputContainerAdapter mOtherContainerAdapter) {
        this.mOtherContainerAdapter = mOtherContainerAdapter;
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
            child.requestFocus();
        } else {
            MyDebugger.log("Child to focus is null");
        }
    }

    public ArrayList<ListDataItem> getListDataItems(boolean filterEmpty) {
        return getListDataItemsFromContainer(mContainer, filterEmpty);
    }

    public void addInputItem(@Nullable String inputItemContent, boolean requestFocus) {
        if (mContainer.getChildCount() != 0) {
            addInputItemAfterView(mContainer.getChildAt(mContainer.getChildCount() - 1), inputItemContent, requestFocus);
        } else {
            setupContainer(inputItemContent, requestFocus);
        }
    }

    public void addInputItemAfterView(View view, @Nullable String inputItemContent, boolean requestFocus) {
        int viewId = getViewId(view);
        if (viewId == Constants.ERROR) return;

        View inputItem = createView();
        int inputItemPosition = viewId + 1;
        inputItem.setTag(inputItemPosition);
        updateItemsIdAfterAdd(inputItemPosition);

        setupInputItemLayout(inputItem, inputItemContent);

        mContainer.addView(inputItem, inputItemPosition);
        if (requestFocus) {
            onAddItemRequestFocus(inputItem);
        }
    }

    public void removeInputItem(View inputItem) {
        int position = getViewId(inputItem);
        if (position == Constants.ERROR) return;
        boolean wasItemFocused = getEditTextFromView(inputItem).isFocused();
        updateItemsIdAfterRemove(position);
        mContainer.removeView(inputItem);
        View previousItem = mContainer.getChildAt(position - 1);
        if (previousItem != null) {
            onRemoveItemRequestFocus(previousItem, wasItemFocused);
        }
    }

    protected abstract View createView();

    protected abstract boolean isListDataItemChecked();

    protected void onRemoveItemRequestFocus(View previousItem, boolean wasItemFocused) {

    }

    protected void onAddItemRequestFocus(View inputItem) {
        final EditText mEditText = getEditTextFromView(inputItem);
        mScrollView.post(new Runnable() {
            @Override
            public void run() {
                mScrollView.smoothScrollBy(0, mEditText.getHeight());

            }
        });
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
            onAddItemRequestFocus(inputItem);
        }
    }

    protected void setupInputItemLayout(View inputItem, String inputItemContent) {
        EditText mEditText = getEditTextFromView(inputItem);
        mEditText.setOnKeyListener(new ListInputOnKeyListener(this));
        mEditText.setOnFocusChangeListener(new MyFocusListener(this));

        ImageButton mRemoveImageButton = (ImageButton) inputItem.findViewById(R.id.list_input_item_remove_button);
        mRemoveImageButton.setOnClickListener(new RemoveListInputOnClickListener(this));

        final CheckBox mCheckBox = (CheckBox) inputItem.findViewById(R.id.list_input_item_check_box);
        mCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
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

    protected EditText getEditTextFromView(View view) {
        return (EditText) view.findViewById(R.id.list_input_item_edit_text);
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

    private void updateItemsIdAfterAdd(int inputItemPosition) {
        for (int i = inputItemPosition; i < mContainer.getChildCount(); ++i) {
            moveItemOnePositionForward(mContainer.getChildAt(i));
        }
    }

    private void moveItemOnePositionForward(View view) {
        int position = getViewId(view);
        if (position == Constants.ERROR) return;
        view.setTag(position + 1);
    }

    private void updateItemsIdAfterRemove(int removePosition) {
        for (int i = removePosition + 1; i < mContainer.getChildCount(); ++i) {
            moveItemOnePositionBackward(mContainer.getChildAt(i));
        }
    }

    private void moveItemOnePositionBackward(View view) {
        int position = getViewId(view);
        if (position == Constants.ERROR) return;
        view.setTag(position - 1);
    }

    private int getViewId(View view) {
        Object idObject = view.getTag();
        if (idObject != null) {
            return (int) idObject;
        } else {
            MyDebugger.log("viewId not found error");
            return Constants.ERROR;
        }
    }
}
