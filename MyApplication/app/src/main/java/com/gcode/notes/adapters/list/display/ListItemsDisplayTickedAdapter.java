package com.gcode.notes.adapters.list.display;


import android.os.Handler;
import android.view.View;
import android.widget.Button;

import com.gcode.notes.data.ListDataItem;
import com.gcode.notes.extras.MyDebugger;
import com.gcode.notes.ui.SnackbarHelper;

import org.solovyev.android.views.llm.LinearLayoutManager;

import java.util.ArrayList;

public class ListItemsDisplayTickedAdapter extends ListItemsDisplayBaseAdapter {
    Button mDoneButton;
    View mRootView;
    LinearLayoutManager mLayoutManager;

    public ListItemsDisplayTickedAdapter(ArrayList<ListDataItem> data, Button doneButton,
                                         View rootView, LinearLayoutManager layoutManager) {
        super(data);
        mDoneButton = doneButton;
        mRootView = rootView;
        mLayoutManager = layoutManager;
    }

    public void add(ListDataItem item) {
        item.setChecked(true);
        mData.add(item);
        notifyItemInserted(getItemCount() - 1);
        if (mDoneButton.getVisibility() == View.GONE) {
            //show done button if its gone
            mDoneButton.setVisibility(View.VISIBLE);
        }
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                MyDebugger.log("lastVisPos", mLayoutManager.findLastVisibleItemPosition());
                MyDebugger.log("itemCount", getItemCount());
                if (mLayoutManager.findLastVisibleItemPosition() != getItemCount() - 1) {
                    SnackbarHelper.showShortSnackbar(mRootView, "Item added to done");
                }
            }
        }, 50);
    }

    @Override
    public void remove(int position) {
        super.remove(position);
        if (getItemCount() == 0) {
            //hide doneButton
            mDoneButton.setVisibility(View.GONE);
        }
    }
}
