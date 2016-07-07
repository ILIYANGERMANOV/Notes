package com.gcode.notes.extras.utils;

import android.Manifest;
import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.view.View;

import com.gcode.notes.R;
import com.gcode.notes.extras.MyLogger;

import pl.tajchert.nammu.Nammu;
import pl.tajchert.nammu.PermissionCallback;
import pl.tajchert.nammu.PermissionListener;

public abstract class PermissionsUtils {
    Activity mActivity;

    public PermissionsUtils(Activity activity) {
        mActivity = activity;
        Nammu.init(activity.getApplicationContext());
    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        Nammu.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    public void verifyPermissionsChanges() {
        Nammu.permissionCompare(new PermissionListener() {
            @Override
            public void permissionsChanged(String s) {

            }

            @Override
            public void permissionsGranted(String permissionGranted) {

            }

            @Override
            public void permissionsRemoved(String permissionRemoved) {
                MyLogger.log("Permission removed", permissionRemoved);
                switch (permissionRemoved) {
                    case Manifest.permission.WRITE_EXTERNAL_STORAGE:
                        //write external storage permission removed, ask user to grant it again, cuz its critical for UX
                        askForStoragePermission(null);
                        break;
                }
            }
        });
    }

    public void askForStoragePermission(@Nullable final PermissionCallback storagePermissionCallback) {
        askForPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE, storagePermissionCallback, R.string.write_external_permission_rationale);
    }

    public void askForRecordAudioPermission(@Nullable final PermissionCallback audioPermissionCallback) {
        askForPermission(Manifest.permission.RECORD_AUDIO, audioPermissionCallback, R.string.record_audio_permission_rationale);
    }

    public void askForLocationPermission(@Nullable final PermissionCallback locationPermissionCallback) {
        //TODO: implement ask for location permission
    }

    public void askForPermission(final String permissionName, final PermissionCallback permissionCallback, int rationaleStringId) {
        if (Nammu.shouldShowRequestPermissionRationale(mActivity, permissionName)) {
            //User already refused to give us this permission or removed it
            //Now he/she can mark "never ask again" (sic!)
            Snackbar.make(getRootView(), rationaleStringId,
                    Snackbar.LENGTH_INDEFINITE)
                    .setAction(R.string.ok, new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Nammu.askForPermission(mActivity, permissionName, permissionCallback);
                        }
                    }).show();
        } else {
            //First time asking for permission
            // or phone doesn't offer permission
            // or user marked "never ask again"
            Nammu.askForPermission(mActivity, permissionName, permissionCallback);
        }
    }

    public boolean hasStoragePermission() {
        return Nammu.checkPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE);
    }

    public boolean hasRecordAudioPermissions() {
        return Nammu.checkPermission(Manifest.permission.RECORD_AUDIO);
    }

    public boolean hasLocationPermissions() {
        //TODO: consider letting user to give only coarse location permission
        return Nammu.checkPermission(Manifest.permission.ACCESS_COARSE_LOCATION) && Nammu.checkPermission(Manifest.permission.ACCESS_FINE_LOCATION);
    }

    protected abstract View getRootView();
}
