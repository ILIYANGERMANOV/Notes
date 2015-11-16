package com.gcode.notes.data;

import android.content.Context;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gcode.notes.adapters.viewholders.main.NoteItemViewHolder;
import com.gcode.notes.extras.MyDebugger;
import com.gcode.notes.extras.utils.PhotoUtils;
import com.gcode.notes.extras.utils.Utils;
import com.gcode.notes.extras.values.Constants;

import java.util.ArrayList;

public class NoteData extends ContentBase {
    //TODO: REFACTOR AND OPTIMIZE, remove useless null checks
    String description;
    ArrayList<String> attachedImagesPaths;
    ArrayList<String> attachedAudioPaths;

    public NoteData(int id, int orderId, int targetId, String title, int mode, boolean hasAttributes,
                    String reminderString, String creationDate, String lastModifiedDate, String expirationDateString) {

        super(id, orderId, targetId, title, mode, hasAttributes, reminderString, creationDate, lastModifiedDate, expirationDateString);
    }

    public NoteData(String title, int mode, boolean hasAttributes,
                    String description, ArrayList<String> attachedImagesPaths,
                    ArrayList<String> attachedAudioPaths, String reminderString) {

        super(title, mode, reminderString);
        this.description = description;
        this.attachedImagesPaths = attachedImagesPaths;
        this.attachedAudioPaths = attachedAudioPaths;
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
        if (hasAttachedImage()) {
            //displayAttachedImages(holder.getImagesContainer());
        }
        displayAudioRecord(holder.getVoiceImageView());
        displayDivider(holder.getAttributesDivider());
    }

    public void displayNote(TextView titleTextView, TextView descriptionTextView, LinearLayout imagesContainer) {
        displayBase(titleTextView);
        descriptionTextView.setText(description);
        if (hasAttachedImage()) {
            displayAttachedImages(imagesContainer);
        }
    }

    private void displayDivider(View attributesDividerView) {
        if (hasAttachedAudio() || hasReminder()) {
            attributesDividerView.setVisibility(View.VISIBLE);
        }
    }

    public void displayAttachedImages(LinearLayout imagesContainer) {
        Context context = imagesContainer.getContext();
        for (String photoPath : attachedImagesPaths) {
            //TODO: implement proper display
            //create image view
            ImageView imageView = new ImageView(context);
            ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    Utils.convertDpInPixels(150));

            imageView.setLayoutParams(lp);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);

            imagesContainer.addView(imageView);

            //display image
            PhotoUtils.loadPhoto(context, photoPath, imageView);
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
        //null check in order not to drop old database
        return attachedImagesPaths != null && attachedImagesPaths.size() > 0;
    }

    public boolean hasAttachedAudio() {
        return attachedAudioPaths != null && attachedAudioPaths.size() > 0;
    }

    public ArrayList<String> getAttachedImagesPaths() {
        return attachedImagesPaths;
    }

    public void setAttachedImagesPaths(ArrayList<String> attachedImagesPaths) {
        this.attachedImagesPaths = attachedImagesPaths;
    }

    public ArrayList<String> getAttachedAudioPaths() {
        return attachedAudioPaths;
    }

    public void setAttachedAudioPaths(ArrayList<String> attachedAudioPaths) {
        this.attachedAudioPaths = attachedAudioPaths;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}
