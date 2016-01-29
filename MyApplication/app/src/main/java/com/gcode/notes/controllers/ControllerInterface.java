package com.gcode.notes.controllers;

import com.gcode.notes.data.base.ContentBase;

public interface ControllerInterface {
    /**
     * Called by compose activity or when delete note snackbar is dismissed.
     *
     * @param mode the newly added note mode
     */
    void onNewNoteAdded(int mode);

    /**
     * Called only when controller should handle current note mode
     * and notes is not added to adapter.
     *
     * @param contentBase note to be added
     */
    void onAddNote(ContentBase contentBase);

    /**
     * Called when note comes back from display activity or onItemModeChanged()
     * if notes has changed in display.
     *
     * @param item note to be updated
     */
    void onItemChanged(ContentBase item);

    /**
     * Called when display activity has flagged that item mode was changed.
     *
     * @param item note which mode has changed
     */
    void onItemModeChanged(ContentBase item);

    /**
     * @param mode the mode which will be checked
     * @return whether the controller should handle the passed mode
     */
    boolean shouldHandleMode(int mode);
}
