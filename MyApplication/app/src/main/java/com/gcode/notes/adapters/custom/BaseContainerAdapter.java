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
import com.gcode.notes.listeners.ListInputOnKeyListener;
import com.gcode.notes.listeners.RemoveListInputOnClickListener;

import java.util.ArrayList;

public abstract class BaseContainerAdapter {
    LinearLayout mContainer;
    ScrollView mScrollView;
    LayoutInflater mInflater;
    BaseContainerAdapter mOtherContainerAdapter;

    public BaseContainerAdapter(LinearLayout container, ScrollView scrollView) {
        mContainer = container;
        mScrollView = scrollView;
        mInflater = (LayoutInflater) container.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public BaseContainerAdapter getOtherContainerAdapter() {
        return mOtherContainerAdapter;
    }

    public void setOtherContainerAdapter(BaseContainerAdapter mOtherContainerAdapter) {
        this.mOtherContainerAdapter = mOtherContainerAdapter;
    }

    public void setupContainer(@Nullable String itemContent) {
        View inputItem = createView();
        inputItem.setTag(0);

        setupInputItemLayout(inputItem, itemContent);

        mContainer.addView(inputItem);
    }

    public ArrayList<ListDataItem> getListDataItems() {
        ArrayList<ListDataItem> mList = new ArrayList<>();

        int childCount = mContainer.getChildCount();

        for (int i = 0; i < childCount; ++i) {
            View child = mContainer.getChildAt(i);
            if (child != null) {
                ListDataItem mItem = getListDataItemFromChild(child);
                if (mItem != null) {
                    mList.add(mItem);
                }
            }
        }

        return mList;
    }

    private ListDataItem getListDataItemFromChild(View child) {
        EditText mEditText = getEditTextFromView(child);
        String content = mEditText.getText().toString();
        if (content.trim().length() == 0) return null;
        CheckBox mCheckBox = (CheckBox) child.findViewById(R.id.list_input_item_check_box);
        return new ListDataItem(content, isListDataItemChecked());
    }

    public void addInputItem(@Nullable String inputItemContent) {
        if (mContainer.getChildCount() != 0) {
            addInputItemAfterView(mContainer.getChildAt(mContainer.getChildCount() - 1), inputItemContent);
        } else {
            setupContainer(inputItemContent);
            //set focus to first item
            onFirstItemAdded();
        }
    }

    protected void onFirstItemAdded() {

    }

    public void removeInputItem(View inputItem) {
        int position = getViewId(inputItem);
        if (position == Constants.ERROR) return;
        updateItemsIdAfterRemove(position);
        mContainer.removeView(inputItem);
        View previousItem = mContainer.getChildAt(position - 1);
        if (previousItem != null) {
            setupFocusOnItemRemove(previousItem);
        }
    }

    protected void setupFocusOnItemRemove(View previousItem) {

    }

    public void addInputItemAfterView(View view, @Nullable String inputItemContent) {
        int viewId = getViewId(view);
        if (viewId == Constants.ERROR) return;

        View inputItem = createView();
        int inputItemPosition = viewId + 1;
        inputItem.setTag(inputItemPosition);
        updateItemsIdAfterAdd(inputItemPosition);

        setupInputItemLayout(inputItem, inputItemContent);

        mContainer.addView(inputItem, inputItemPosition);
        setupInputItemScrollingAndFocus(inputItem);
    }

    abstract protected View createView();


    protected void setupInputItemScrollingAndFocus(View inputItem) {
    }

    protected void setupInputItemLayout(View inputItem, String inputItemContent) {
        EditText mEditText = getEditTextFromView(inputItem);
        mEditText.setOnKeyListener(new ListInputOnKeyListener(this));

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

    protected void onChecked(View parent) {

    }

    protected void onUnchecked(View parent) {

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


    protected abstract boolean isListDataItemChecked();
}
