package com.gcode.notes.adapters.custom;


import android.graphics.Paint;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import com.gcode.notes.R;

public class ListInputTickedContainerAdapter extends BaseInputContainerAdapter {
    public ListInputTickedContainerAdapter(LinearLayout container, ScrollView scrollView) {
        super(container, scrollView);
    }

    @Override
    protected View createView() {
        return mInflater.inflate(R.layout.list_input_item_ticked, mContainer, false);
    }

    @Override
    protected void setupInputItemLayout(View inputItem, String inputItemContent) {
        super.setupInputItemLayout(inputItem, inputItemContent);
        EditText mEditText = getEditTextFromView(inputItem);
        mEditText.setPaintFlags(mEditText.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
    }

    @Override
    protected void onUnchecked(View parent) {
        removeInputItem(parent);
        EditText mEditText = getEditTextFromView(parent);
        mOtherContainerAdapter.addInputItem(mEditText.getText().toString(), true);
    }

    @Override
    protected boolean isListDataItemChecked() {
        return true;
    }
}
