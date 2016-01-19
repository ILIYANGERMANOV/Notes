package com.gcode.notes.extras.utils;

import android.app.Activity;

import com.gcode.notes.controllers.visible.callbacks.AuthenticationCallbacks;
import com.gcode.notes.extras.MyDebugger;
import com.gcode.notes.extras.utils.callbacks.PasswordCallbacks;
import com.gcode.notes.extras.values.Constants;
import com.gcode.notes.extras.values.Keys;
import com.gcode.notes.ui.helpers.DialogBuilder;

public class AuthenticationUtils implements PasswordCallbacks {
    //TODO: REFACTOR AND OPTIMIZE
    private static AuthenticationUtils mInstance;

    private Activity mActivity;
    private String mPassword;
    private int mPassTries;

    private AuthenticationCallbacks mCallbacks;

    private AuthenticationUtils(Activity activity, AuthenticationCallbacks authenticationCallbacks) {
        mActivity = activity;
        mCallbacks = authenticationCallbacks;
        mPassTries = MyUtils.readFromPreferences(Keys.PREF_PASS_TRIES, Constants.PASS_MAX_TRIES);
        mPassword = MyUtils.readFromPreferences(Keys.PREF_PASSWORD, Constants.NO_PASSWORD);
    }

    public static AuthenticationUtils getInstance(Activity activity, AuthenticationCallbacks authCallbacks) {
        if (mInstance == null) {
            mInstance = new AuthenticationUtils(activity, authCallbacks);
        } else {
            mInstance.mActivity = activity;
            mInstance.mPassTries = MyUtils.readFromPreferences(Keys.PREF_PASS_TRIES, Constants.PASS_MAX_TRIES);
            mInstance.mPassword = MyUtils.readFromPreferences(Keys.PREF_PASSWORD, Constants.NO_PASSWORD);
        }
        return mInstance;
    }

    public Activity getActivity() {
        return mActivity;
    }

    public AuthenticationCallbacks getCallbacks() {
        return mCallbacks;
    }

    public void authenticate() {
        if (!mPassword.equals(Constants.NO_PASSWORD)) {
            //there is already password set, enter existing
            enterPassword();
        } else {
            //there is no password set, setup new password
            setupNewPassword();
        }
    }

    private void enterPassword() {
        String content = "Enter password for private notes.";
        if (mPassTries == 0) {
            //password tries ended
            mCallbacks.onPasswordTriesEnded();
            return;
        }
        if (mPassTries != 1) {
            //there are more than one password tries
            content += " (" + mPassTries + ")";
        } else {
            //last attempt
            content += "\nWARNING: Only one attempt left! Entering wrong will result in deleting private notes.";
        }
        mPassTries--;
        DialogBuilder.buildEnterPasswordDialog(this, content);
    }

    private void setupNewPassword() {
        DialogBuilder.buildCreatePasswordDialog(this);
    }

    @Override
    public void onCreatePassword(String password) {
        mPassword = password;
        DialogBuilder.buildConfirmPasswordDialog(this);
    }

    @Override
    public void onConfirmPassword(String confirmedPassword) {
        if (mPassword.equals(confirmedPassword)) {
            //password set successfully
            MyDebugger.log("password set correctly", mPassword);
            MyUtils.saveToPreferences(Keys.PREF_PASSWORD, mPassword);
            mCallbacks.onAuthenticated(mPassword);
        } else {
            //password was not confirmed
            DialogBuilder.buildCreatePasswordDialog(this);
        }
    }

    @Override
    public void onEnterPassword(String password) {
        if (mPassword.equals(password)) {
            //password entered successfully
            MyUtils.saveToPreferences(Keys.PREF_PASS_TRIES, Constants.PASS_MAX_TRIES);
            mCallbacks.onAuthenticated(password);
        } else {
            //wrong password entered
            MyUtils.saveToPreferences(Keys.PREF_PASS_TRIES, mPassTries);
            enterPassword();
        }
    }
}
