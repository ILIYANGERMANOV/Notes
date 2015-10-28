package com.gcode.notes.adapters.list.display;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import com.gcode.notes.data.ListDataItem;

import java.util.ArrayList;

public class BaseDisplayContainerAdapter {
    //TODO: implement BaseDisplayContainerAdapter
    LinearLayout mContainer;
    LayoutInflater mInflater;
    BaseDisplayContainerAdapter mOtherContainerAdapter;

    ArrayList<ListDataItem> mData;

    public BaseDisplayContainerAdapter(LinearLayout container, ArrayList<ListDataItem> data) {
        mContainer = container;
        mData = data;
        mInflater = (LayoutInflater) container.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void setOtherContainerAdapter(BaseDisplayContainerAdapter otherContainerAdapter) {
        mOtherContainerAdapter = otherContainerAdapter;
    }

    public int getItemCount() {
        return mData.size();
    }
}
