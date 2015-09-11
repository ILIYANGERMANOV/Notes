package com.gcode.notes.data;

import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.gcode.notes.adapters.NotesAdapter;

import java.net.URI;
import java.util.Calendar;
import java.util.Date;

public class NoteData {
    String title;
    URI imageURI;
    String text;
    Date reminder;
    String audioRecord;

    Date creationDate;

    public NoteData(String title, @Nullable String text,
                    @Nullable URI imageURI, @Nullable Date reminder,
                    @Nullable String audioRecord) {

        this.title = title;
        this.text = text;
        this.imageURI = imageURI;
        this.reminder = reminder;
        this.audioRecord = audioRecord;
        creationDate = Calendar.getInstance().getTime();
    }

    public NoteData(String title, String text) {
        this.title = title;
        this.text = text;
        imageURI = null;
        reminder = null;
        audioRecord = null;
        creationDate = Calendar.getInstance().getTime();
    }

    public void displayNote(NotesAdapter.MyViewHolder holder) {
        holder.getTitleTextView().setText(title);
        displayTextContent(holder.getContentTextView());
        displayReminder(holder.getReminderTextView());
        displayAttachedImage(holder.getAttachedImageView());
        displayAudioRecord(holder.getVoiceImageView());
        if (hasAudioRecord() || hasReminder()) {
            holder.getAttributesDivider().setVisibility(View.VISIBLE);
        }
    }

    public void displayTextContent(TextView contentTextView) {
        if (hasTextContent()) {
            contentTextView.setText(text);
        } else {
            contentTextView.setText("");
        }
    }

    public void displayReminder(TextView reminderTextView) {
        if (hasReminder()) {
            reminderTextView.setText(reminder.toString());
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
        if (hasAudioRecord()) {
            voiceImageView.setVisibility(View.VISIBLE);
        } else {
            voiceImageView.setVisibility(View.GONE);
        }
    }


    boolean hasAttachedImage() {
        return imageURI != null;
    }

    boolean hasReminder() {
        return reminder != null;
    }

    boolean hasAudioRecord() {
        return audioRecord != null;
    }

    boolean hasTextContent() {
        return text != null;
    }

    public URI getImageURI() {
        return imageURI;
    }

    public void setImageURI(URI imageURI) {
        this.imageURI = imageURI;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setAudioRecord(String audioRecord) {
        this.audioRecord = audioRecord;
    }

    public String getAudioRecord() {
        return audioRecord;
    }

    public Date getReminder() {
        return reminder;
    }

    public void setReminder(Date reminder) {
        this.reminder = reminder;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

}
