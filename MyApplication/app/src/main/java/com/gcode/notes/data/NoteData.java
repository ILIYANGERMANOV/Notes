package com.gcode.notes.data;

import android.content.Context;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gcode.notes.R;
import com.gcode.notes.adapters.viewholders.main.NoteItemViewHolder;
import com.gcode.notes.data.base.ContentBase;
import com.gcode.notes.extras.MyDebugger;
import com.gcode.notes.extras.utils.PhotoUtils;
import com.gcode.notes.extras.values.Constants;

import java.util.ArrayList;
import java.util.List;

public class NoteData extends ContentBase {
    String description;
    List<String> attachedImagesPaths;
    String attachedAudioPath;

    public NoteData() {
        super();
        setAttributesToDefaultValues();
    }

    public NoteData(int id, int orderId, int targetId, String title, int mode, boolean hasAttributes,
                    String reminderString, String creationDate, String lastModifiedDate,
                    String expirationDateString, String myLocationSerialized) {

        super(id, orderId, targetId, title, mode, hasAttributes,
                reminderString, creationDate, lastModifiedDate,
                expirationDateString, myLocationSerialized);

        setAttributesToDefaultValues();
    }

    public boolean hasDescription() {
        return description.trim().length() > 0;
    }

    public boolean hasAttachedImage() {
        return attachedImagesPaths != null && attachedImagesPaths.size() > 0;
    }

    public boolean hasAttachedAudio() {
        return attachedAudioPath != null;
    }

    public boolean hasAttributes() {
        return hasDescription() || hasAttachedImage() || hasAttachedAudio();
    }

    public boolean isValidNote(boolean hadValidTitleBeforeSaveBase) {
        return hadValidTitleBeforeSaveBase || hasAttributes();
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

    public void displayNoteOnMain(final NoteItemViewHolder holder) {
        //recycler view uses already created holders for optimization, so clear holder
        setHolderInDefaultState(holder); //sets view holder in default state
        displayBase(holder.getTitleTextView(), holder.getReminderTextView()); //display title and reminder
        displayDescription(holder);
        if (hasAttachedImage()) {
            displayAttachedImages(holder.getImagesContainer());
        }
        displayAudioRecord(holder.getVoiceImageView());
        displayDivider(holder.getAttributesDivider());
    }

    private void setHolderInDefaultState(NoteItemViewHolder holder) {
        holder.getImagesContainer().removeAllViews(); //sets images container to default state
        holder.getAttributesDivider().setVisibility(View.GONE); //hides attribute divider cuz  by default should be hidden
        holder.getMoreImageView().setVisibility(View.GONE); //hide gone image view cuz by default should be hidden
    }

    private void displayDescription(final NoteItemViewHolder holder) {
        final TextView descriptionTextView = holder.getDescriptionTextView(); //get reference for easier access
        descriptionTextView.setText(description); //set description always cuz its not done in setHolderDefaultState()
        if (hasDescription()) {
            descriptionTextView.setVisibility(View.VISIBLE);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    int linesCount = descriptionTextView.getLineCount();
                    if (linesCount != 0) {
                        if (linesCount > Constants.MAX_DESCRIPTION_LINES_TO_DISPLAY) {
                            holder.getMoreImageView().setVisibility(View.VISIBLE);
                        }
                    } else {
                        MyDebugger.log("displayNoteOnMain linesCount not build.");
                    }
                }
            }, Constants.MEDIUM_DELAY);
        }
    }

    private void displayAttachedImages(LinearLayout imagesContainer) {
        Context context = imagesContainer.getContext();
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        for (int i = 0; i < attachedImagesPaths.size() && i < Constants.MAX_IMAGES_TO_DISPLAY; ++i) {
            ImageView imageView = (ImageView) layoutInflater.inflate(R.layout.note_main_image_item, imagesContainer, false); //inflates image view from layout
            imagesContainer.addView(imageView); //adds image view to container in order to display
            PhotoUtils.loadPhoto(context, attachedImagesPaths.get(i), imageView); //makes load query with Picasso
        }
    }

    private void displayDivider(View attributesDividerView) {
        if (hasAttachedAudio() || hasReminder()) {
            //there are attributes, show divider
            attributesDividerView.setVisibility(View.VISIBLE); //shows divider
        } else {
            //there are no attributes, hide divider
            attributesDividerView.setVisibility(View.GONE); //hides divider
        }
    }

    private void displayAudioRecord(ImageView voiceImageView) {
        if (hasAttachedAudio()) {
            voiceImageView.setVisibility(View.VISIBLE);
        } else {
            voiceImageView.setVisibility(View.GONE);
        }
    }

    private void secureAttachedImagesPathsList() {
        if (attachedImagesPaths == null) {
            attachedImagesPaths = new ArrayList<>();
        }
    }

    private void setAttributesToDefaultValues() {
        attachedImagesPaths = null;
        description = "";
        attachedAudioPath = null;
    }

}
