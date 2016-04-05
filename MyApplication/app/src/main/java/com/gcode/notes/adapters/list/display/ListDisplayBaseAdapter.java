package com.gcode.notes.adapters.list.display;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckedTextView;

import com.gcode.notes.R;
import com.gcode.notes.data.list.ListDataItem;
import com.gcode.notes.ui.helpers.CheckedTextViewHelper;

import java.util.ArrayList;

public abstract class ListDisplayBaseAdapter extends ArrayAdapter<ListDataItem> {
    boolean mIsDeactivated;

    public ListDisplayBaseAdapter(Context context, ArrayList<ListDataItem> data, boolean isDeactivated) {
        super(context, 0, data);
        mIsDeactivated = isDeactivated;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ListDataItem item = getItem(position);
        ListItemHolder holder;

        //create view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).
                    inflate(R.layout.display_list_item, parent, false);

            holder = new ListItemHolder();
            holder.checkedTextView =
                    (CheckedTextView) convertView.findViewById(R.id.list_data_item_row_checked_text_view);
            convertView.setTag(holder);
        } else {
            holder = (ListItemHolder) convertView.getTag();
        }

        //bind view
        if (areItemsChecked()) {
            CheckedTextViewHelper.setChecked(holder.checkedTextView);
        } else {
            CheckedTextViewHelper.setUnchecked(holder.checkedTextView);
        }
        if (mIsDeactivated) {
            convertView.setEnabled(false);
            holder.checkedTextView.setEnabled(false);
        }
        holder.checkedTextView.setText(item.getContent());

        return convertView;
    }

    abstract protected boolean areItemsChecked();

    static class ListItemHolder {
        CheckedTextView checkedTextView;
    }
}
