package com.gcode.notes.data;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;

import com.gcode.notes.adapters.viewholders.NoteItemViewHolder;
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
                    @Nullable URI imageURI, @Nullable URI audioURI, @NonNull String reminderString) {

        super(title, mode, reminderString);
        this.description = description;
        this.imageURI = imageURI;
        this.audioURI = audioURI;
        type = Constants.TYPE_NOTE;
        this.hasAttributes = hasAttributes;
    }

    public void displayNote(NoteItemViewHolder holder) {
        displayBase(holder);
        holder.getContentTextView().setText(description);
        displayAttachedImage(holder.getAttachedImageView());
        displayAudioRecord(holder.getVoiceImageView());
        if (hasAttachedAudio() || hasReminder()) {
            holder.getAttributesDivider().setVisibility(View.VISIBLE);
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

    public URI getAudioURI() {
        return audioURI;
    }

    public void setAudioURI(URI audioURI) {
        this.audioURI = audioURI;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}
