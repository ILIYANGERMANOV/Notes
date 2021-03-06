package com.gcode.notes.data.list;


import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckedTextView;
import android.widget.LinearLayout;

import com.gcode.notes.R;
import com.gcode.notes.adapters.main.viewholders.ListItemViewHolder;
import com.gcode.notes.data.base.ContentBase;
import com.gcode.notes.extras.values.Constants;
import com.gcode.notes.ui.helpers.CheckedTextViewHelper;

import java.util.ArrayList;

public class ListData extends ContentBase {
    //TODO: OPTIMIZE
    ArrayList<ListDataItem> list;

    public ListData() {
        super();
        list = null;
    }

    public ListData(int id, int orderId, int targetId, String title, int mode, boolean hasAttributes,
                    String reminderString, String creationDate, String lastModified,
                    String expirationDateString, String myLocationSerialized) {

        super(id, orderId, targetId, title, mode, hasAttributes,
                reminderString, creationDate, lastModified,
                expirationDateString, myLocationSerialized);
    }

    public ListData(ListData other) {
        super(other);
        if (other.hasAttachedList()) {
            //needs deep copy
            list = new ArrayList<>();
            for (ListDataItem otherListDataItem : other.list) {
                list.add(new ListDataItem(otherListDataItem));
            }
        }
    }

    public boolean isValidList(boolean hadValidTitleBeforeSaveBase) {
        return hadValidTitleBeforeSaveBase || hasAttachedList();
    }

    /**
     * This method is created just for better readability.
     *
     * @return if the list data has attached attributes (hasAttachedList())
     */
    public boolean hasAttributes() {
        return hasAttachedList();
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
        setHolderInDefaultState(holder); //removeAllViews from container and hides moreImageView
        displayBase(holder.getTitleTextView(), holder.getReminderTextView());
        displayListItems(activity, holder);
        displayAttributesLayout(holder);
    }

    private void setHolderInDefaultState(ListItemViewHolder holder) {
        holder.getContainerLayout().removeAllViews();
        holder.getMoreImageView().setVisibility(View.GONE);
    }

    private void displayListItems(Activity activity, ListItemViewHolder holder) {
        //add list items to container
        if (list == null) return; //abort and prevent null pointer exception
        LinearLayout containerLayout = holder.getContainerLayout();
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
    }

    private View createViewForItem(ListDataItem item, LayoutInflater inflater, LinearLayout containerLayout) {
        //create view
        View itemView = inflater.inflate(R.layout.list_main_item, containerLayout, false);

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

    private void displayAttributesLayout(ListItemViewHolder holder) {
        if (hasReminder()) {
            //they are attributes, show attributes layout
            holder.getAttributesLayout().setVisibility(View.VISIBLE); //shows attributes layout
        } else {
            //there are no attributes, hide divider
            holder.getAttributesLayout().setVisibility(View.GONE); //hides attributes layout
        }
    }

    private void secureList() {
        if (list == null) {
            list = new ArrayList<>();
        }
    }
}
