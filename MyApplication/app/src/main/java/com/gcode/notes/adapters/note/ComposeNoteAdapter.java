package com.gcode.notes.adapters.note;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.gcode.notes.R;
import com.gcode.notes.extras.MyDebugger;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ComposeNoteAdapter extends ArrayAdapter<Uri> {

    public ComposeNoteAdapter(Context context, ArrayList<Uri> data) {
        super(context, 0, data);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        Uri photoUri = getItem(position);
        NoteImageItemHolder holder;

        //create view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.note_compose_image_item, parent, false);
            holder = new NoteImageItemHolder();
            holder.imageView = (ImageView) convertView.findViewById(R.id.note_attached_image_view);
            holder.deleteImageButton = (ImageButton) convertView.findViewById(R.id.note_delete_image_button);
            convertView.setTag(holder);
        } else {
            holder = (NoteImageItemHolder) convertView.getTag();
        }

        //bind view
        Picasso.with(getContext()).
                load(photoUri)
                .fit().centerCrop()
                .into(holder.imageView);

        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyDebugger.log(getItem(position).toString() + " clicked");
                //TODO: open image with gallery app
            }
        });
        holder.deleteImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyDebugger.log("Delete image", Integer.toString(position));
                //TODO: pop delete image dialog
            }
        });

        return convertView;
    }

    static class NoteImageItemHolder {
        ImageView imageView;
        ImageButton deleteImageButton;
    }
}