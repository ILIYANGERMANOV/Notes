package com.gcode.notes.extras.utils.callbacks;

public interface PasswordCallbacks {
    void onCreatePassword(String password);
    void onConfirmPassword(String confirmedPassword);
    void onEnterPassword(String password);
}
