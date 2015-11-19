package com.gcode.notes.adapters.note;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.gcode.notes.R;
import com.gcode.notes.extras.utils.PhotoUtils;

import java.util.ArrayList;

public class DisplayNoteImagesAdapter extends ArrayAdapter<String> {

    public DisplayNoteImagesAdapter(Context context, ArrayList<String> data) {
        super(context, 0, data);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        NoteImageItemHolder holder;

        //create view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.note_display_image_item, parent, false);
            holder = new NoteImageItemHolder();
            holder.imageView = (ImageView) convertView.findViewById(R.id.note_display_item_image_view);
            convertView.setTag(holder);
        } else {
            holder = (NoteImageItemHolder) convertView.getTag();
        }

        //bind view
        PhotoUtils.loadPhoto(getContext(), getItem(position), holder.imageView);

        return convertView;
    }

    static class NoteImageItemHolder {
        ImageView imageView;
    }
}
