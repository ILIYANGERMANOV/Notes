package com.gcode.notes.adapters;

import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.gcode.notes.R;
import com.gcode.notes.adapters.viewholders.ComposeListInputViewHolder;
import com.gcode.notes.data.ListDataItem;
import com.gcode.notes.data.ListInputItem;

import java.util.ArrayList;

public class ComposeListInputAdapter extends RecyclerView.Adapter<ComposeListInputViewHolder> {
    ArrayList<ListInputItem> mList;

    public ComposeListInputAdapter(ArrayList<ListInputItem> list) {
        mList = list;
    }

    public ArrayList<ListDataItem> getItemsList() {
        ArrayList<ListDataItem> returnList = new ArrayList<>();

        for (ListInputItem listInputItem : mList) {
            ListDataItem currentItem = listInputItem.getListDataItem();
            if (currentItem != null) {
                returnList.add(currentItem);
            }
        }

        return returnList;
    }

    public void addListItem() {
        mList.add(new ListInputItem());
        notifyItemInserted(mList.size() - 1);
    }

    public void focusLastItemEditText() {
        EditText mEditText = mList.get(mList.size() - 1).getContentEditText();
        if (mEditText != null) {
            mEditText.requestFocus();
            //TODO: open keyboard
        }
    }

    public void removeItem(int position) {
        try {
            mList.remove(position);
        } catch (IndexOutOfBoundsException ex) {
            //TODO: handle exception (PRIORITY: HIGH)
        }
        notifyItemRemoved(position);
    }

    @Override
    public ComposeListInputViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        return new ComposeListInputViewHolder(inflater.inflate(R.layout.list_input_item, parent, false));
    }

    @Override
    public void onBindViewHolder(final ComposeListInputViewHolder holder, int position) {
        mList.get(position).setContentEditText(holder.getEdiText());
        mList.get(position).setCheckBox(holder.getCheckBox());

        holder.getEdiText().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    switch (keyCode) {
                        case KeyEvent.KEYCODE_DPAD_CENTER:
                        case KeyEvent.KEYCODE_ENTER:
                            addListItem();
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    focusLastItemEditText();
                                }
                            }, 50);
                            return true;
                        default:
                            break;
                    }
                }
                return false;
            }
        });

        holder.getRemoveButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeItem(holder.getAdapterPosition());
            }
        });

        //TODO: reorder functionality
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }
}
