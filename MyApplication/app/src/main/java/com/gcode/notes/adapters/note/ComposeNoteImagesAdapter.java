package com.gcode.notes.adapters.note;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.gcode.notes.R;
import com.gcode.notes.activities.compose.ComposeNoteActivity;
import com.gcode.notes.extras.utils.PhotoUtils;
import com.gcode.notes.ui.ActionExecutor;
import com.linearlistview.LinearListView;

import java.util.ArrayList;

public class ComposeNoteImagesAdapter extends ArrayAdapter<String> {
    //TODO: optimize mData and data and REFACTOR
    ArrayList<String> mData;
    Activity mActivity;
    LinearListView mLinearListView;

    public ComposeNoteImagesAdapter(Activity activity, ArrayList<String> data, LinearListView linearListView) {
        super(activity, 0, data);
        mData = data;
        mActivity = activity;
        mLinearListView = linearListView;
    }

    public ArrayList<String> getData() {
        return mData;
    }

    public void showListView() {
        mLinearListView.setVisibility(View.VISIBLE);
    }

    public void hideListView() {
        mLinearListView.setVisibility(View.GONE);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final String photoPath = getItem(position);
        NoteImageItemHolder holder;

        //create view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.note_compose_image_item, parent, false);
            holder = new NoteImageItemHolder();
            holder.imageView = (ImageView) convertView.findViewById(R.id.note_compose_item_image_view);
            holder.deleteImageButton = (ImageButton) convertView.findViewById(R.id.note_compose_item_delete_image_button);
            convertView.setTag(holder);
        } else {
            holder = (NoteImageItemHolder) convertView.getTag();
        }
        //bind view
        PhotoUtils.loadPhoto(mActivity, photoPath, holder.imageView);

        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!ComposeNoteActivity.mOpenInGalleryLaunched) {
                    ComposeNoteActivity.mOpenInGalleryLaunched = true;
                    PhotoUtils.openPhotoInGallery(mActivity, photoPath);
                }
            }
        });
        holder.deleteImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActionExecutor.removePhotoFromNote(mActivity, ComposeNoteImagesAdapter.this, photoPath);
            }
        });

        return convertView;
    }

    @Override
    public void add(String object) {
        super.add(object);
        if (getCount() == 1) {
            //first item added show list view
            showListView();
        }
    }

    static class NoteImageItemHolder {
        ImageView imageView;
        ImageButton deleteImageButton;
    }
}