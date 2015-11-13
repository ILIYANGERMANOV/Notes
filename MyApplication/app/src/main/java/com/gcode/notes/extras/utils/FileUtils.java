package com.gcode.notes.extras.utils;

import android.net.Uri;
import android.os.Environment;

import com.gcode.notes.extras.MyDebugger;
import com.gcode.notes.extras.values.Constants;

import java.io.File;
import java.io.IOException;

public class FileUtils {
    public static boolean deleteFile(Uri pathToFile) {
        File createdTempFile = new File(pathToFile.getPath());
        if (createdTempFile.exists()) {
            //file exists, try to delete it
            if (!createdTempFile.delete()) {
                //deletion has failed
                return false;
            } else {
                MyDebugger.log("deleteFile()", "failed to delete file");
            }
        } else {
            MyDebugger.log("deleteFile()","file doesn't exists");
        }
        //
        return true;
    }

    public static File createImageFile() throws IOException {
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
