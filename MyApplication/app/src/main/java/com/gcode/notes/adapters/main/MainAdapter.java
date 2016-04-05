package com.gcode.notes.adapters.main;


import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gcode.notes.R;
import com.gcode.notes.activities.MainActivity;
import com.gcode.notes.adapters.main.viewholders.BaseItemViewHolder;
import com.gcode.notes.adapters.main.viewholders.ListItemViewHolder;
import com.gcode.notes.adapters.main.viewholders.NoteItemViewHolder;
import com.gcode.notes.adapters.main.viewholders.listeners.BaseItemListener;
import com.gcode.notes.controllers.BaseController;
import com.gcode.notes.data.NoteData;
import com.gcode.notes.data.base.ContentBase;
import com.gcode.notes.data.list.ListData;
import com.gcode.notes.extras.MyDebugger;
import com.gcode.notes.extras.values.Constants;
import com.gcode.notes.helper.ItemTouchHelperAdapter;
import com.gcode.notes.motions.MyAnimator;
import com.gcode.notes.notes.MyApplication;
import com.gcode.notes.ui.ActionExecutor;

import java.util.ArrayList;
import java.util.Collections;

public class MainAdapter extends RecyclerView.Adapter<BaseItemViewHolder> implements ItemTouchHelperAdapter {
    //TODO: REFACTOR AND OPTIMIZE
    private ArrayList<ContentBase> mData;
    private MainActivity mMainActivity;
    private RecyclerView mRecyclerView;
    private LayoutInflater mInflater;

    private TextView mEmptyView;
    private boolean mEmptyViewVisible;
    private int mLastAnimatedPosition = -1;
    private boolean mAnimate = true;

    public MainAdapter(MainActivity mainActivity, ArrayList<ContentBase> data) {
        mMainActivity = mainActivity;
        mRecyclerView = mainActivity.getRecyclerView();
        mEmptyView = mainActivity.getRecyclerViewEmptyView();
        mData = data;

        mInflater = LayoutInflater.from(mainActivity);
        mEmptyViewVisible = false;
    }

    @SuppressWarnings("unused")
    public void setAnimate(boolean animate) {
        this.mAnimate = animate;
    }

    //getters----------------------------------------------------------------------------------------
    public RecyclerView getRecyclerView() {
        return mRecyclerView;
    }

    public MainActivity getMainActivity() {
        return mMainActivity;
    }

    public ArrayList<ContentBase> getData() {
        return mData;
    }

    //getters----------------------------------------------------------------------------------------

    @Override
    public int getItemViewType(int position) {
        return mData.get(position).getType();
    }

    @Override
    public BaseItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == Constants.TYPE_NOTE) {
            return new NoteItemViewHolder(mMainActivity, mInflater.inflate(R.layout.note, parent, false), mData);
        } else {
            return new ListItemViewHolder(mMainActivity, mInflater.inflate(R.layout.list, parent, false), mData);
        }
    }

    @Override
    public void onBindViewHolder(final BaseItemViewHolder holder, int position) {
        holder.setStartState();
        ContentBase currentItem = mData.get(position);
        if (currentItem != null) {
            if (currentItem.getType() == Constants.TYPE_NOTE) {
                ((NoteData) currentItem).displayNoteOnMain((NoteItemViewHolder) holder);
            } else {
                ((ListData) currentItem).displayListOnMain(mMainActivity, (ListItemViewHolder) holder);
            }
        }
        runEnterAnimation(holder.itemView, position);
    }

    private void runEnterAnimation(View itemView, int position) {
        if (position > mLastAnimatedPosition && mAnimate) {
            MyAnimator.startAnimationOnView(mMainActivity, itemView, R.anim.recycler_view_item_appear);
            mLastAnimatedPosition = position;
        }
    }

    @Override
    public void onViewDetachedFromWindow(BaseItemViewHolder holder) {
        //clear animation, because they are problems on fast scrolling
        //with animating view while adapter is trying to reuse it
        holder.clearAnimation();
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public int getIndexOfItem(ContentBase item) {
        int itemIndex = Constants.NO_VALUE;
        for (int i = 0; i < getItemCount(); ++i) {
            if (mData.get(i).getId() == item.getId()) {
                itemIndex = i;
                break;
            }
        }
        return itemIndex;
    }

    public boolean itemExists(ContentBase item) {
        for (ContentBase currentItem : mData) {
            if (currentItem.getId() == item.getId())
                return true;
        }
        return false;
    }

    /**
     * Adds item to adapter by its order id.
     *
     * @param contentBase item to be added
     * @return the position of the added item in the list
     */
    public int addItemByOrderId(ContentBase contentBase) {
        int itemOrderId = contentBase.getOrderId();
        for (int i = 0; i < getItemCount(); ++i) {
            if (itemOrderId > mData.get(i).getOrderId()) {
                addItem(i, contentBase);
                return i;
            }
        }
        //add item as last
        mData.add(contentBase);
        //!NOTE: getItemCount() is increment after #add() is called,
        //so item position is getItemCount() - 1
        int itemPosition = getItemCount() - 1;
        notifyItemInserted(itemPosition);
        return itemPosition;
    }

    public boolean addItem(int position, ContentBase item) {
        try {
            mData.add(position, item);
            notifyItemInserted(position);

            return true;
        } catch (IndexOutOfBoundsException exception) {
            MyDebugger.log("MainAdapter(): indexOutOfBoundsException caught.");
            mData.add(item);
            notifyItemInserted(getItemCount() - 1);
            return false;
        } finally {
            if (mEmptyViewVisible) {
                hideEmptyView();
            }
        }
    }

    /**
     * Updates item in adapter.
     *
     * @param item item to be updated
     * @return whether the item was updated
     */
    public boolean updateItem(ContentBase item) {
        int position = getIndexOfItem(item);
        if (position != Constants.NO_VALUE) {
            //item found in list, update it
            mData.set(position, item);
            notifyItemChanged(position);
            return true;
        } else {
            //item not found in list, return false because it is not updated
            return false;
        }
    }

    public ContentBase removeItem(int position) {
        ContentBase removedItem = mData.remove(position);
        notifyItemRemoved(position);
        checkForEmptyState();
        return removedItem;
    }

    public void moveItem(int fromPosition, int toPosition) {
        ContentBase model = mData.remove(fromPosition);
        try {
            mData.add(toPosition, model);
            notifyItemMoved(fromPosition, toPosition);
        } catch (IndexOutOfBoundsException e) {
            MyDebugger.log("MainAdapter moveItem(): IndexOutOfBoundsException", e.getMessage());
        }
    }

    public void animateTo(ArrayList<ContentBase> filteredList) {
        applyAndAnimateRemovals(filteredList);
        applyAndAnimateAdditions(filteredList);
        applyAndAnimateMovedItems(filteredList);
    }

    private void applyAndAnimateRemovals(ArrayList<ContentBase> newNotes) {
        for (int i = mData.size() - 1; i >= 0; i--) {
            final ContentBase contentBase = mData.get(i);
            if (!newNotes.contains(contentBase)) {
                removeItem(i);
            }
        }
    }

    private void applyAndAnimateAdditions(ArrayList<ContentBase> newNotes) {
        for (int i = 0, count = newNotes.size(); i < count; i++) {
            final ContentBase contentBase = newNotes.get(i);
            if (!mData.contains(contentBase)) {
                addItem(i, contentBase);
            }
        }
    }

    private void applyAndAnimateMovedItems(ArrayList<ContentBase> newNotes) {
        for (int toPosition = newNotes.size() - 1; toPosition >= 0; toPosition--) {
            final ContentBase contentBase = newNotes.get(toPosition);
            final int fromPosition = mData.indexOf(contentBase);
            if (fromPosition >= 0 && fromPosition != toPosition) {
                moveItem(fromPosition, toPosition);
            }
        }
    }

    @Override
    public void onItemDismissUnrecoverable(int position) {
        ActionExecutor.deleteNotePermanently(mMainActivity, mData.get(position), position);
        removeItem(position);
    }

    @Override
    public void onItemDismiss(int position) {
        ActionExecutor.popNoteDeletedSnackbar(mMainActivity, position, mData.get(position));
        removeItem(position);
    }

    @Override
    public boolean onItemMove(int fromPosition, int toPosition) {
        if (fromPosition < toPosition) {
            for (int i = fromPosition; i < toPosition; i++) {
                Collections.swap(mData, i, i + 1);
                MyApplication.getWritableDatabase().swapNotesPosition(mData.get(i), mData.get(i + 1));
            }
        } else {
            for (int i = fromPosition; i > toPosition; i--) {
                Collections.swap(mData, i, i - 1);
                MyApplication.getWritableDatabase().swapNotesPosition(mData.get(i), mData.get(i - 1));
            }
        }
        notifyItemMoved(fromPosition, toPosition);
        return true;
    }


    public void updateContent(ArrayList<ContentBase> newData) {
        mLastAnimatedPosition = -1; //reset animated position to run animation again
        mData.clear();
        mData.addAll(newData);
        notifyDataSetChanged();
        checkForEmptyState();
    }

    public void setListenersDisabled(boolean disabled) {
        for (int i = 0; i < mRecyclerView.getChildCount(); ++i) {
            View childView = mRecyclerView.getChildAt(i);
            BaseItemListener baseItemListener =
                    ((BaseItemViewHolder) mRecyclerView.getChildViewHolder(childView)).getItemBaseListener();
            if (baseItemListener != null) {
                baseItemListener.setDisabled(disabled);
            }
        }
    }

    public void checkForEmptyState() {
        if (getItemCount() == 0) {
            showEmptyView();
        } else {
            hideEmptyView();
        }
    }

    public void showEmptyView() {
        //Don't check if already shown, cuz text can change but view visibility isn't necessary new value
        mEmptyView.setVisibility(View.VISIBLE);
        BaseController.getInstance().setupEmptyView(mEmptyView);
        MyAnimator.startAnimationOnView(mMainActivity, mEmptyView, R.anim.fade_in_empty_view);
        mEmptyViewVisible = true;
    }

    public void hideEmptyView() {
        if (mEmptyView.getVisibility() != View.GONE) {
            mEmptyView.setVisibility(View.GONE);
            MyAnimator.startAnimationOnView(mMainActivity, mEmptyView, R.anim.fade_out_empty_view);
            mEmptyViewVisible = false;
        }
    }
}
