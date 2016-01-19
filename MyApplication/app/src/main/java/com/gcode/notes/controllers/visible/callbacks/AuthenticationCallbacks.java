package com.gcode.notes.controllers.visible.callbacks;

public interface AuthenticationCallbacks {
    void onAuthenticated(String password);
    void onPasswordTriesEnded();
    void onExitPrivate();
}
