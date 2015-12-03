package com.gcode.notes.adapters.note.compose;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.gcode.notes.R;
import com.gcode.notes.activities.compose.note.ComposeNoteActivity;
import com.gcode.notes.adapters.note.compose.listeners.DeleteImageButtonOnClickListener;
import com.gcode.notes.adapters.note.compose.listeners.ImageOnClickListener;
import com.gcode.notes.extras.utils.PhotoUtils;
import com.linearlistview.LinearListView;

import java.util.List;

public class ComposeNoteImagesAdapter extends ArrayAdapter<String> {
    ComposeNoteActivity mComposeNoteActivity;
    LinearListView mLinearListView;

    public ComposeNoteImagesAdapter(ComposeNoteActivity composeNoteActivity, List<String> data,
                                    LinearListView linearListView) {

        super(composeNoteActivity, 0, data);
        mComposeNoteActivity = composeNoteActivity;
        mLinearListView = linearListView;
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
        PhotoUtils.loadPhoto(mComposeNoteActivity, photoPath, holder.imageView);

        //set holder's listeners
        holder.imageView.setOnClickListener(new ImageOnClickListener(mComposeNoteActivity, photoPath));
        holder.deleteImageButton.setOnClickListener(new DeleteImageButtonOnClickListener(mComposeNoteActivity,
                this, photoPath));

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