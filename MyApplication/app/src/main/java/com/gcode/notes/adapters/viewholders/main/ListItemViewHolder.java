package com.gcode.notes.adapters.viewholders.main;


import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.gcode.notes.R;

import butterknife.Bind;
import butterknife.ButterKnife;

public class ListItemViewHolder extends BaseItemViewHolder {
    @Bind(R.id.list_item_recycler_view)
    RecyclerView mRecyclerView;

    @Bind(R.id.list_item_card_view)
    CardView mCardView;

    public ListItemViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    @Override
    public void setStartState() {
        mCardView.setCardBackgroundColor(Color.WHITE);
    }

    public RecyclerView getRecyclerView() {
        return mRecyclerView;
    }
}
