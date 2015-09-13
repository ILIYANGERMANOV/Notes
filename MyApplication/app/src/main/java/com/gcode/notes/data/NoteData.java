package com.gcode.notes.data;

import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.gcode.notes.adapters.NotesAdapter;
import com.gcode.notes.extras.Constants;

import java.net.URI;
import java.util.Date;

public class NoteData extends ContentBase {
    String description;
    URI imageURI;
    URI audioURI;

    public NoteData(int id, int orderId, int targetId, String title, int mode, boolean hasAttributes,
                    String reminderString, Date creationDate, String expirationDateString) {

        super(id, orderId, targetId, title, mode, hasAttributes, reminderString, creationDate, expirationDateString);
    }

    public NoteData(String title, int mode, boolean hasAttributes, String description,
                    @Nullable URI imageURI, @Nullable URI audioURI, @Nullable String reminderString) {

        super(title, mode, reminderString);
        this.description = description;
        this.imageURI = imageURI;
        this.audioURI = audioURI;
        type = Constants.TYPE_NOTE;
        this.hasAttributes = hasAttributes;
    }

    public void displayNote(NotesAdapter.MyViewHolder holder) {
        holder.getTitleTextView().setText(title);
        holder.getContentTextView().setText(description);
        displayReminder(holder.getReminderTextView());
        displayAttachedImage(holder.getAttachedImageView());
        displayAudioRecord(holder.getVoiceImageView());
        if (hasAttachedAudio() || hasReminder()) {
            holder.getAttributesDivider().setVisibility(View.VISIBLE);
        }
    }

    public void displayReminder(TextView reminderTextView) {
        if (hasReminder()) {
            reminderTextView.setText(reminderString);
        } else {
            reminderTextView.setVisibility(View.GONE);
        }
    }

    public void displayAttachedImage(ImageView noteImageView) {
        if (hasAttachedImage()) {
            //TODO: set image
        } else {
            noteImageView.setVisibility(View.GONE);
        }
    }

    public void displayAudioRecord(ImageView voiceImageView) {
        if (hasAttachedAudio()) {
            voiceImageView.setVisibility(View.VISIBLE);
        } else {
            voiceImageView.setVisibility(View.GONE);
        }
    }


    public boolean hasAttachedImage() {
        return imageURI != null;
    }

    public boolean hasAttachedAudio() {
        return audioURI != null;
    }

    public URI getImageURI() {
        return imageURI;
    }

    public void setImageURI(URI imageURI) {
        this.imageURI = imageURI;
    }

    public void setAudioURI(URI audioURI) {
        this.audioURI = audioURI;
    }

    public URI getAudioURI() {
        return audioURI;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}
