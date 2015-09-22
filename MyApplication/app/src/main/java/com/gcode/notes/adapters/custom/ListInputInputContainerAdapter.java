package com.gcode.notes.adapters.custom;

import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import com.gcode.notes.R;

public class ListInputInputContainerAdapter extends BaseInputContainerAdapter {

    public ListInputInputContainerAdapter(LinearLayout container, ScrollView scrollView) {
        super(container, scrollView);
    }

    @Override
    protected void setupInputItemScrollingAndFocus(View inputItem) {
        final EditText mEditText = getEditTextFromView(inputItem);
        mScrollView.post(new Runnable() {
            @Override
            public void run() {
                mScrollView.smoothScrollBy(0, mEditText.getHeight());

            }
        });
        mEditText.requestFocus();
    }

    @Override
    protected void setupFocusOnItemRemove(View previousItem) {
        EditText mEditText = getEditTextFromView(previousItem);
        mEditText.requestFocus();
    }

    @Override
    protected void onFirstItemAdded() {
        EditText mEditText = getEditTextFromView(mContainer.getChildAt(0));
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
        mOtherContainerAdapter.addInputItem(mEditText.getText().toString());
    }

    @Override
    protected boolean isListDataItemChecked() {
        return false;
    }
}
