package com.gcode.notes.data;

import android.net.Uri;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.gcode.notes.adapters.viewholders.main.NoteItemViewHolder;
import com.gcode.notes.extras.MyDebugger;
import com.gcode.notes.extras.values.Constants;
import com.gcode.notes.notes.MyApplication;

import java.net.URI;

public class NoteData extends ContentBase {
    String description;
    String attachedImagesString;
    Uri audioUri;

    public NoteData(int id, int orderId, int targetId, String title, int mode, boolean hasAttributes,
                    String reminderString, String creationDate, String lastModifiedDate, String expirationDateString) {

        super(id, orderId, targetId, title, mode, hasAttributes, reminderString, creationDate, lastModifiedDate, expirationDateString);
    }

    public NoteData(String title, int mode, boolean hasAttributes, String description,
                    @Nullable String attachedImagesString, @Nullable Uri audioUri, @NonNull String reminderString) {

        super(title, mode, reminderString);
        this.description = description;
        this.attachedImagesString = attachedImagesString;
        this.audioUri = audioUri;
        type = Constants.TYPE_NOTE;
        this.hasAttributes = hasAttributes;
    }

    public void displayNoteOnMain(final NoteItemViewHolder holder) {
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
                    MyDebugger.log("displayNoteOnMain linesCount not build.");
                }
            }
        }, 50);
        displayAttachedImages(holder.getImagesContainer());
        displayAudioRecord(holder.getVoiceImageView());
        displayDivider(holder.getAttributesDivider());
    }

    public void displayNote(TextView titleTextView, TextView descriptionTextView, LinearLayout imagesContainer) {
        displayBase(titleTextView);
        descriptionTextView.setText(description);
        displayAttachedImages(imagesContainer);
    }

    private void displayDivider(View attributesDividerView) {
        if (hasAttachedAudio() || hasReminder()) {
            attributesDividerView.setVisibility(View.VISIBLE);
        }
    }

    public void displayAttachedImages(LinearLayout imagesContainer) {
        //TODO: implement (PHOTO)
    }

    public void displayAudioRecord(ImageView voiceImageView) {
        if (hasAttachedAudio()) {
            voiceImageView.setVisibility(View.VISIBLE);
        } else {
            voiceImageView.setVisibility(View.GONE);
        }
    }


    public boolean hasAttachedImage() {
        return attachedImagesString != null;
    }

    public boolean hasAttachedAudio() {
        return audioUri != null;
    }

    public String getAttachedImagesString() {
        return attachedImagesString;
    }

    public void setAttachedImagesString(String attachedImagesString) {
        this.attachedImagesString = attachedImagesString;
    }

    public Uri getAudioUri() {
        return audioUri;
    }

    public void setAudioUri(Uri audioUri) {
        this.audioUri = audioUri;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}
