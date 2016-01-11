package com.gcode.notes.data.list;


import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckedTextView;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.gcode.notes.R;
import com.gcode.notes.adapters.viewholders.main.ListItemViewHolder;
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
        LinearLayout containerLayout = holder.getContainerLayout();
        ImageView moreImageView = holder.getMoreImageView();

        setHolderInDefaultState(containerLayout, moreImageView); //removeAllViews from container and hides moreImageView
        displayBase(holder.getTitleTextView(), holder.getReminderTextView());
        displayListItems(activity, containerLayout, moreImageView);
        displayDivider(holder);
    }

    private void setHolderInDefaultState(LinearLayout containerLayout, ImageView moreImageView) {
        containerLayout.removeAllViews();
        moreImageView.setVisibility(View.GONE);
    }

    private void displayListItems(Activity activity, LinearLayout containerLayout, ImageView moreImageView) {
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
                moreImageView.setVisibility(View.VISIBLE);
                break;
            }
        }
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

    private void displayDivider(ListItemViewHolder holder) {
        if (hasReminder()) {
            //they are attributes, show divider
            holder.getAttributesDivider().setVisibility(View.VISIBLE); //shows attributes divider
        } else {
            //there are no attributes, hide divider
            holder.getAttributesDivider().setVisibility(View.GONE); //hides attributes divider
        }
    }

    private void secureList() {
        if (list == null) {
            list = new ArrayList<>();
        }
    }
}
