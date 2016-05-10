package com.gcode.notes.data;

import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.gcode.notes.adapters.main.viewholders.NoteItemViewHolder;
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

    public NoteData(NoteData other) {
        super(other);
        description = other.description;
        if (other.hasAttachedImage()) {
            attachedImagesPaths = new ArrayList<>(other.attachedImagesPaths);
        }
        attachedAudioPath = other.attachedAudioPath;
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
        displayAttachedImages(holder.getNoteImageView());
        displayAudioRecord(holder.getVoiceImageView());
        displayAttributesLayout(holder.getAttributesLayout());
    }

    /**
     * Resets view holder's more image view.
     * Its done here, because more image view should stay hidden while checking text view's lines count.
     *
     * @param holder view holder representing one note
     */
    private void setHolderInDefaultState(NoteItemViewHolder holder) {
        holder.getMoreImageView().setVisibility(View.GONE); //hide gone image view cuz by default should be hidden
    }

    private void displayDescription(final NoteItemViewHolder holder) {
        final TextView descriptionTextView = holder.getDescriptionTextView(); //get reference for easier access
        if (hasDescription()) {
            //note has description, display it and if needed show more image view
            descriptionTextView.setText(description);
            //note has description, determine description max lines and show more image if needed
            final int maxDescriptionLinesToDisplay;
            if (!hasAttachedImage()) {
                maxDescriptionLinesToDisplay = Constants.MAX_DESCRIPTION_LINES_TO_DISPLAY;
            } else {
                //notes has attached image, make desc max lines to display lower
                maxDescriptionLinesToDisplay = Constants.MAX_DESCRIPTION_LINES_TO_DISPLAY / 2;
            }
            descriptionTextView.setMaxLines(maxDescriptionLinesToDisplay); //always set it, cuz view can be reused

            descriptionTextView.setVisibility(View.VISIBLE);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    int linesCount = descriptionTextView.getLineCount();
                    if (linesCount != 0) {
                        if (linesCount > maxDescriptionLinesToDisplay) {
                            holder.getMoreImageView().setVisibility(View.VISIBLE);
                        }
                    } else {
                        MyDebugger.log("displayNoteOnMain linesCount not build.");
                    }
                }
            }, Constants.SHORT_DELAY);
        } else {
            //note hasn't description, hide it
            descriptionTextView.setVisibility(View.GONE); //reset here for better performance
        }
    }

    private void displayAttachedImages(ImageView noteImageVIew) {
        if (hasAttachedImage()) {
            noteImageVIew.setVisibility(View.VISIBLE);
            PhotoUtils.loadPhoto(noteImageVIew.getContext(),
                    attachedImagesPaths.get(0), noteImageVIew); //load 1st note attached image to display
        } else {
            //there is no attached image, hide note image
            noteImageVIew.setVisibility(View.GONE); //reset here for better performance (no need for double hide)
        }
    }

    private void displayAudioRecord(ImageView voiceImageView) {
        if (hasAttachedAudio()) {
            voiceImageView.setVisibility(View.VISIBLE);
        } else {
            voiceImageView.setVisibility(View.GONE);
        }
    }

    private void displayAttributesLayout(View attributesLayout) {
        if (hasAttachedAudio() || hasReminder()) {
            //there are attributes, show attributes layout
            attributesLayout.setVisibility(View.VISIBLE); //shows attributes layout
        } else {
            //there are no attributes, hide attributes layout
            attributesLayout.setVisibility(View.GONE); //hides attributes layout
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
