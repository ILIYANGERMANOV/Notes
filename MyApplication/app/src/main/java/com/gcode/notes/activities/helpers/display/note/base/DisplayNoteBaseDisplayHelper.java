package com.gcode.notes.activities.helpers.display.note.base;

import android.view.View;

import com.gcode.notes.activities.display.note.DisplayNoteBaseActivity;
import com.gcode.notes.activities.helpers.display.note.base.listeners.DisplayNoteImageOnItemClickListener;
import com.gcode.notes.adapters.note.display.DisplayNoteImagesAdapter;
import com.gcode.notes.data.main.NoteData;
import com.gcode.notes.extras.utils.AudioUtils;
import com.linearlistview.LinearListView;

public class DisplayNoteBaseDisplayHelper {
    public static void displayNoteDataBase(final DisplayNoteBaseActivity displayNoteBaseActivity) {
        NoteData mNoteData = displayNoteBaseActivity.mNoteData; //create new reference for easier access
        displayNoteBaseActivity.getDatesTextView().setText(mNoteData.getDateDetails());
        mNoteData.displayNote(displayNoteBaseActivity.getTitleTextView(), displayNoteBaseActivity.getDescriptionTextView());
        if (mNoteData.hasAttachedImage()) {
            LinearListView mImagesLinearListView = displayNoteBaseActivity.getImagesLinearListView(); //create new reference for easier access
            DisplayNoteImagesAdapter adapter = (DisplayNoteImagesAdapter) mImagesLinearListView.getAdapter();
            if (adapter == null) {
                //activity is created for first time, init and setup adapter
                adapter = new DisplayNoteImagesAdapter(displayNoteBaseActivity, mNoteData.getAttachedImagesPaths());
                mImagesLinearListView.setAdapter(adapter);
                mImagesLinearListView.setOnItemClickListener(new DisplayNoteImageOnItemClickListener(displayNoteBaseActivity));
            } else {
                //adapter is already created (called from onActivityResult), just update its data
                adapter.clear();
                adapter.addAll(mNoteData.getAttachedImagesPaths());
            }
        } else {
            displayNoteBaseActivity.getImagesLinearListView().setVisibility(View.GONE); //when you come back from compose and all images are deleted
            //hasAttachedImages() is false so true case doesn't handle image remove
        }

        if (mNoteData.hasAttachedAudio()) {
            if (displayNoteBaseActivity.mAudioUtils == null) {
                //create audio utils if they are not created
                displayNoteBaseActivity.mAudioUtils = new AudioUtils(displayNoteBaseActivity, mNoteData.getAttachedAudioPath(),
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
