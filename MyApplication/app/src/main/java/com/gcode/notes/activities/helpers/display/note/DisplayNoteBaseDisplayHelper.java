package com.gcode.notes.activities.helpers.display.note;

import android.view.View;

import com.gcode.notes.activities.display.note.DisplayNoteBaseActivity;
import com.gcode.notes.activities.helpers.display.note.listeners.DisplayNoteImageOnItemClickListener;
import com.gcode.notes.adapters.note.display.DisplayNoteImagesAdapter;
import com.gcode.notes.data.NoteData;
import com.gcode.notes.extras.utils.AudioUtils;
import com.linearlistview.LinearListView;

public class DisplayNoteBaseDisplayHelper {
    //TODO: REFACTOR AND OPTIMIZE
    public static void displayNoteData(final DisplayNoteBaseActivity displayNoteBaseActivity) {
        NoteData noteData = displayNoteBaseActivity.mNoteData; //create new reference for easier access

        if (noteData.hasDescription()) {
            //note has description, show view and display it
            displayNoteBaseActivity.getDescriptionTextView().setVisibility(View.VISIBLE);
            displayNoteBaseActivity.getDescriptionTextView().setText(noteData.getDescription());
        } else {
            //note hasn't description, hide view
            displayNoteBaseActivity.getDescriptionTextView().setVisibility(View.GONE);
        }

        if (noteData.hasAttachedImage()) {
            //there is attached image, display it
            LinearListView imagesLinearListView = displayNoteBaseActivity.getImagesLinearListView(); //create new reference for easier access
            DisplayNoteImagesAdapter adapter = (DisplayNoteImagesAdapter) imagesLinearListView.getAdapter();
            if (adapter == null) {
                //activity is created for first time, init and setup adapter
                adapter = new DisplayNoteImagesAdapter(displayNoteBaseActivity, noteData.getAttachedImagesPaths());
                imagesLinearListView.setAdapter(adapter);
                imagesLinearListView.setOnItemClickListener(new DisplayNoteImageOnItemClickListener(displayNoteBaseActivity));
            } else {
                //adapter is already created (called from onActivityResult), just update its data
                adapter.clear();
                adapter.addAll(noteData.getAttachedImagesPaths());
            }
        } else {
            //there are no attached images, hide imagesListView
            displayNoteBaseActivity.getImagesLinearListView().setVisibility(View.GONE); //when you come back from compose and all images are deleted
            //hasAttachedImages() is false so true case doesn't handle image remove
        }

        if (noteData.hasAttachedAudio()) {
            //there is attached audio, display audio utils
            if (displayNoteBaseActivity.mAudioUtils == null) {
                //create audio utils if they are not created
                displayNoteBaseActivity.mAudioUtils = new AudioUtils(displayNoteBaseActivity, noteData.getAttachedAudioPath(),
                        displayNoteBaseActivity.getAudioDurationTextView(), displayNoteBaseActivity.getAudioProgressBar(),
                        displayNoteBaseActivity.getAudioPlayPauseButton(), displayNoteBaseActivity.getAudioLayout());
            }
        } else if (displayNoteBaseActivity.mAudioUtils != null) {
            //audio utils are created but there is no attached audio (audio removed from compose),
            //free resources used by audio utils
            displayNoteBaseActivity.mAudioUtils.clearResources();
            displayNoteBaseActivity.mAudioUtils = null;
        }
    }
}
