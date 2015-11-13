package com.gcode.notes.extras.utils;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;

import com.gcode.notes.extras.MyDebugger;
import com.gcode.notes.extras.values.Constants;

import java.io.File;
import java.io.IOException;

public class PhotoUtils {
    public static Uri pathToPhoto;

    public static void dispatchTakePictureIntent(Activity activity) {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(activity.getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
                MyDebugger.log("Creating imageFile IOException", ex.getMessage());
            }

            // Continue only if the File was successfully created
            if (photoFile != null) {
                pathToPhoto = Uri.fromFile(photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
                activity.startActivityForResult(takePictureIntent, Constants.REQUEST_TAKE_PHOTO);
            } else {
                MyDebugger.log("Could not create file for the photo!");
            }
        }
    }


    private static File createImageFile() throws IOException {
        // Create an image file name
        String imageFileName = "JPEG_" + DateUtils.getCurrentTimeSQLiteFormatted() + "_";

        //Creates folder in Pictures
        File storageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                Constants.PHOTOS_FOLDER_NAME);

        if (!storageDir.exists()) {
            //storageDir does not exists, create it
            if (!storageDir.mkdirs()) {
                //failed to create storageDir return nnull
                return null;
            }
        }

        return File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );
    }
}
