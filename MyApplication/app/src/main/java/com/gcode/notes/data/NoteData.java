package com.gcode.notes.data;

import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.gcode.notes.adapters.viewholders.main.NoteItemViewHolder;
import com.gcode.notes.extras.MyDebugger;
import com.gcode.notes.extras.values.Constants;

import java.net.URI;

public class NoteData extends ContentBase {
    String description;
    URI imageURI;
    URI audioURI;

    public NoteData(int id, int orderId, int targetId, String title, int mode, boolean hasAttributes,
                    String reminderString, String creationDate, String lastModifiedDate, String expirationDateString) {

        super(id, orderId, targetId, title, mode, hasAttributes, reminderString, creationDate, lastModifiedDate, expirationDateString);
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

    public void displayNote(final NoteItemViewHolder holder) {
        displayBase(holder.getTitleTextView(), holder.getReminderTextView());
        final TextView descriptionTextView = holder.getDescriptionTextView();
        descriptionTextView.setText(description);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                int linesCount = descriptionTextView.getLineCount();
                if (linesCount != 0) {
                    if (linesCount <= Constants.MAX_DESCRIPTION_LINES_TO_DISPLAY) {
                        holder.getMoreImageView().setVisibility(View.GONE);
                    } else {
                        holder.getMoreImageView().setVisibility(View.VISIBLE);
                    }
                } else {
                    MyDebugger.log("displayNote linesCount not build.");
                }
            }
        }, 50);
        displayAttachedImage(holder.getAttachedImageView());
        displayAudioRecord(holder.getVoiceImageView());
        displayDivider(holder.getAttributesDivider());
    }

    public void displayNote(TextView titleTextView, TextView descriptionTextView, ImageView attachedImageView) {
        displayBase(titleTextView);
        descriptionTextView.setText(description);
        displayAttachedImage(attachedImageView);
    }

    private void displayDivider(View attributesDividerView) {
        if (hasAttachedAudio() || hasReminder()) {
            attributesDividerView.setVisibility(View.VISIBLE);
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
