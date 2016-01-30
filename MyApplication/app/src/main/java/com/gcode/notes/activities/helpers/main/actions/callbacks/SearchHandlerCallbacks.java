package com.gcode.notes.activities.helpers.main.actions.callbacks;

import com.gcode.notes.data.base.ContentBase;

public interface SearchHandlerCallbacks {
    void onNewItemAdded(ContentBase contentBase);

    void onItemAdded(ContentBase contentBase);

    void onItemChanged(ContentBase contentBase);

    void onItemRemoved(ContentBase contentBase);
}
