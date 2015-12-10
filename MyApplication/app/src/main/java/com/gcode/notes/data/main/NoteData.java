package com.gcode.notes.data.main;

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
import java.util.List;

public class NoteData extends ContentBase {
    //TODO: REFACTOR AND OPTIMIZE, remove useless null checks
    String description;
    List<String> attachedImagesPaths;
    String attachedAudioPath;

    public NoteData() {
        super();
        description = "";
        attachedImagesPaths = null;
        attachedAudioPath = Constants.NO_AUDIO;
    }

    public NoteData(int id, int orderId, int targetId, String title, int mode, boolean hasAttributes,
                    String reminderString, String creationDate, String lastModifiedDate, String expirationDateString) {

        super(id, orderId, targetId, title, mode, hasAttributes,
                reminderString, creationDate, lastModifiedDate, expirationDateString);

        attachedImagesPaths = null;
    }

    public NoteData(String title, int mode, boolean hasAttributes,
                    String description, ArrayList<String> attachedImagesPaths,
                    String attachedAudioPath, String reminderString) {

        super(title, mode, reminderString);
        this.description = description;
        this.attachedImagesPaths = attachedImagesPaths;
        this.attachedAudioPath = attachedAudioPath;
        type = Constants.TYPE_NOTE;
        this.hasAttributesFlag = hasAttributes;
    }

    public void displayNoteOnMain(final NoteItemViewHolder holder) {
        displayBase(holder.getTitleTextView(), holder.getReminderTextView());
        final TextView descriptionTextView = holder.getDescriptionTextView();
        setHolderInDefaultState(holder);
        if (hasDescription()) {
            descriptionTextView.setVisibility(View.VISIBLE);
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
        }
        //set images container to default state
        LinearLayout imagesContainer = holder.getImagesContainer();
        imagesContainer.removeAllViews();
        if (hasAttachedImage()) {
            displayAttachedImages(imagesContainer);
        }
        displayAudioRecord(holder.getVoiceImageView());
        displayDivider(holder.getAttributesDivider());
    }

    private void setHolderInDefaultState(NoteItemViewHolder holder) {
        holder.getDescriptionTextView().setText(description);
        holder.getAttributesDivider().setVisibility(View.GONE);
    }

    public void displayNote(TextView titleTextView, TextView descriptionTextView) {
        displayBase(titleTextView);
        if (hasDescription()) {
            descriptionTextView.setVisibility(View.VISIBLE);
        }
        descriptionTextView.setText(description);
    }

    private void displayDivider(View attributesDividerView) {
        if (hasAttachedAudio() || hasReminder()) {
            attributesDividerView.setVisibility(View.VISIBLE);
        }
    }

    public void displayAttachedImages(LinearLayout imagesContainer) {
        Context context = imagesContainer.getContext();
        for (int i = 0; i < attachedImagesPaths.size() && i < Constants.MAX_IMAGES_TO_DISPLAY; ++i) {
            //TODO: REFACTOR (use layout file)
            //create image view
            ImageView imageView = new ImageView(context);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    Utils.convertDpInPixels(150));

            lp.topMargin = Utils.convertDpInPixels(2);

            imageView.setLayoutParams(lp);
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);

            imagesContainer.addView(imageView);

            //display image
            PhotoUtils.loadPhoto(context, attachedImagesPaths.get(i), imageView);
        }
    }

    public void displayAudioRecord(ImageView voiceImageView) {
        if (hasAttachedAudio()) {
            voiceImageView.setVisibility(View.VISIBLE);
        } else {
            voiceImageView.setVisibility(View.GONE);
        }
    }

    public boolean hasDescription() {
        //description can be null atm
        return description != null && description.trim().length() > 0;
    }

    public boolean hasAttachedImage() {
        return attachedImagesPaths != null && attachedImagesPaths.size() > 0;
    }

    public boolean hasAttachedAudio() {
        return attachedAudioPath != null && !attachedAudioPath.equals(Constants.NO_AUDIO);
    }

    public boolean hasAttributes() {
        return hasDescription() || hasAttachedImage() || hasAttachedAudio();
    }

    public boolean isValidNote() {
        return hasValidTitle() || hasAttributes();
    }

    public List<String> getAttachedImagesPaths() {
        secureAttachedImagesPathsList();
        return attachedImagesPaths;
    }

    public void setAttachedImagesPaths(List<String> attachedImagesPaths) {
        if (this.attachedImagesPaths != null) {
            if (!this.attachedImagesPaths.isEmpty()) {
                this.attachedImagesPaths.clear();
            }
            this.attachedImagesPaths.addAll(attachedImagesPaths);
        } else {
            this.attachedImagesPaths = attachedImagesPaths;
        }
    }

    public void addAttachedImagePath(String attachedImagePath) {
        secureAttachedImagesPathsList();
        attachedImagesPaths.add(attachedImagePath);
    }

    public String getAttachedAudioPath() {
        return attachedAudioPath;
    }

    public void setAttachedAudioPath(String attachedAudioPath) {
        this.attachedAudioPath = attachedAudioPath;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    private void secureAttachedImagesPathsList() {
        if (attachedImagesPaths == null) {
            attachedImagesPaths = new ArrayList<>();
        }
    }

}
