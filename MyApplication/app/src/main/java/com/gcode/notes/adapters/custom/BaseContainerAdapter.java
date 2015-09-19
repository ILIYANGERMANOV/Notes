package com.gcode.notes.adapters.custom;

import android.content.Context;
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

public class BaseContainerAdapter {
    //TODO: scroll after many added items
    LinearLayout mContainer;
    ScrollView mScrollView;
    LayoutInflater mInflater;

    public BaseContainerAdapter(LinearLayout container, ScrollView scrollView) {
        mContainer = container;
        mScrollView = scrollView;
        mInflater = (LayoutInflater) container.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void setupContainer() {
        View inputItem = mInflater.inflate(R.layout.list_input_item, mContainer, false);
        inputItem.setTag(0);

        EditText mEditText = (EditText) inputItem.findViewById(R.id.list_input_item_edit_text);
        mEditText.setOnKeyListener(new ListInputOnKeyListener(this));

        ImageButton mRemoveImageButton = (ImageButton) inputItem.findViewById(R.id.list_input_item_remove_button);
        mRemoveImageButton.setOnClickListener(new RemoveListInputOnClickListener(this));

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
        EditText mEditText = (EditText) child.findViewById(R.id.list_input_item_edit_text);
        String content = mEditText.getText().toString();
        if (content.trim().length() == 0) return null;
        CheckBox mCheckBox = (CheckBox) child.findViewById(R.id.list_input_item_check_box);
        return new ListDataItem(content, mCheckBox.isChecked());
    }

    public void addInputItem() {
        if (mContainer.getChildCount() != 0) {
            addInputItemAfterView(mContainer.getChildAt(mContainer.getChildCount() - 1));
        } else {
            setupContainer();
            //set focus to first item
            EditText mEditText = (EditText) mContainer.getChildAt(0).findViewById(R.id.list_input_item_edit_text);
            mEditText.requestFocus();
        }
    }

    public void addInputItemAfterView(View view) {
        int viewId = getViewId(view);
        if (viewId == Constants.ERROR) return;

        View inputItem = mInflater.inflate(R.layout.list_input_item, mContainer, false);
        int inputItemPosition = viewId + 1;
        inputItem.setTag(inputItemPosition);
        updateItemsIdAfterAdd(inputItemPosition);

        EditText mEditText = (EditText) inputItem.findViewById(R.id.list_input_item_edit_text);
        mEditText.setOnKeyListener(new ListInputOnKeyListener(this));

        ImageButton mRemoveImageButton = (ImageButton) inputItem.findViewById(R.id.list_input_item_remove_button);
        mRemoveImageButton.setOnClickListener(new RemoveListInputOnClickListener(this));

        final CheckBox mCheckBox = (CheckBox) inputItem.findViewById(R.id.list_input_item_check_box);
        mCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mCheckBox.setChecked(isChecked);
            }
        });

        mContainer.addView(inputItem, inputItemPosition);
        mScrollView.fullScroll(ScrollView.FOCUS_DOWN);
        mEditText.requestFocus();
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

    public void removeInputItem(View inputItem) {
        int position = getViewId(inputItem);
        if (position == Constants.ERROR) return;
        updateItemsIdAfterRemove(position);
        mContainer.removeView(inputItem);
        View previousItem = mContainer.getChildAt(position - 1);
        if (previousItem != null) {
            EditText mEditText = (EditText) previousItem.findViewById(R.id.list_input_item_edit_text);
            mEditText.requestFocus();
        }
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
