package com.gcode.notes.data.main;


import android.app.Activity;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckedTextView;
import android.widget.LinearLayout;

import com.gcode.notes.R;
import com.gcode.notes.adapters.viewholders.main.ListItemViewHolder;
import com.gcode.notes.data.extras.ListDataItem;
import com.gcode.notes.extras.values.Constants;
import com.gcode.notes.ui.helpers.CheckedTextViewHelper;

import java.util.ArrayList;

public class ListData extends ContentBase {
    //TODO: REFACTOR AND OPTMIZE
    ArrayList<ListDataItem> list;

    public ListData() {
        super();
        list = null;
    }

    public ListData(String title, int mode, boolean hasAttributes,
                    ArrayList<ListDataItem> list, @NonNull String reminderString) {

        super(title, mode, reminderString);
        this.hasAttributesFlag = hasAttributes;
        this.list = list;
    }

    public ListData(int id, int orderId, int targetId, String title, int mode, boolean hasAttributes,
                    String reminderString, String creationDate, String lastModified, String expirationDateString) {

        super(id, orderId, targetId, title, mode, hasAttributes,
                reminderString, creationDate, lastModified, expirationDateString);
    }

    public boolean hasAttributes() {
        return hasAttachedList();
    }

    public boolean isValidList() {
        return hasValidTitle() || hasAttachedList();
    }

    public boolean hasAttachedList() {
        return list != null && list.size() > 0;
    }

    public ArrayList<ListDataItem> getList() {
        secureList(); //prevent returning null list
        return list;
    }

    public void setList(ArrayList<ListDataItem> list) {
        if (this.list != null) {
            //list is already initialized
            if (!this.list.isEmpty()) {
                //list is not empty, clear it
                this.list.clear();
            }
            this.list.addAll(list); //list is existing and empty, add new data
        } else {
            this.list = list; //list in not initialized, init it now
        }
    }

    public void addToList(ArrayList<ListDataItem> list) {
        secureList();
        this.list.addAll(list);
    }

    public void displayListOnMain(Activity activity, ListItemViewHolder holder) {
        displayBase(holder.getTitleTextView(), holder.getReminderTextView());
        if (hasReminder()) {
            //display divider
            holder.getAttributesDivider().setVisibility(View.VISIBLE);
        }
        LinearLayout containerLayout = holder.getContainerLayout();

        //set holder to default state
        holder.getMoreImageView().setVisibility(View.GONE);
        containerLayout.removeAllViews();

        //add list items to container
        if (list == null) return; //abort and prevent null pointer exception
        LayoutInflater inflater = LayoutInflater.from(activity);
        //TODO: display list items based both on MAX_ITEMS_TO_DISPLAY and height
        for (int i = 0; i < list.size(); ++i) {
            if (i < Constants.MAX_LIST_ITEMS_TO_DISPLAY) {
                //display list data item
                containerLayout.addView(createViewForItem(list.get(i), inflater, containerLayout));
            } else {
                //max items to display is reached, show more image view
                holder.getMoreImageView().setVisibility(View.VISIBLE);
                break;
            }
        }
        containerLayout.invalidate();
    }

    private View createViewForItem(ListDataItem item, LayoutInflater inflater, LinearLayout containerLayout) {
        //create view
        View itemView = inflater.inflate(R.layout.list_display_item, containerLayout, false);
        //bind view
        CheckedTextView checkedTextView = (CheckedTextView) itemView.findViewById(R.id.list_data_item_row_checked_text_view);
        if (item.isChecked()) {
            CheckedTextViewHelper.setChecked(checkedTextView);
        } else {
            CheckedTextViewHelper.setUnchecked(checkedTextView);
        }
        checkedTextView.setText(item.getContent());
        return itemView;
    }

    private void secureList() {
        if (list == null) {
            list = new ArrayList<>();
        }
    }
}
