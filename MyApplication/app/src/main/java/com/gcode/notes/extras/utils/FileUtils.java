package com.gcode.notes.extras.utils;

import android.net.Uri;
import android.os.Environment;

import com.gcode.notes.extras.MyDebugger;
import com.gcode.notes.extras.values.Constants;
import com.gcode.notes.notes.MyApplication;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class FileUtils {
    //TODO: REFACTOR AND OPTIMIZE
    public static boolean fileExists(String filePath) {
        return new File(filePath).exists();
    }

    public static boolean deleteFile(String filePath) {
        return deleteFile(new File(filePath));
    }

    public static boolean deleteFile(Uri pathToFile) {
        return deleteFile(new File(pathToFile.getPath()));
    }

    public static boolean deleteFile(File fileToDelete) {
        if (fileToDelete.exists()) {
            //file exists, try to delete it
            if (!fileToDelete.delete()) {
                //deletion has failed
                MyDebugger.log("deleteFile()", "failed to delete file");
                return false;
            }
        } else {
            MyDebugger.log("deleteFile()", "file doesn't exists");
            return false;
        }
        return true;
    }

    public static File createImageFile() throws IOException {
        //Generate file name
        String imageFileName = "JPEG_" + DateUtils.getCurrentTimeSQLiteFormatted() + "_";
        //Create folder in Pictures
        File storageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                Constants.NOTES_FOLDER_NAME);

        if (!storageDir.exists()) {
            //storageDir does not exists, create it
            if (!storageDir.mkdirs()) {
                //failed to create storageDir return null
                MyDebugger.log("Failed to create imageFile storage dir.");
                return null;
            }
        }

        return File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );
    }

    public static String createVoiceRecordFile(Uri srcUri) {
        //Generate file name
        String audioFileName = "RECORD_" + DateUtils.getCurrentTimeSQLiteFormatted() + "_";
        //Create storage dir
        File recordsDir = new File(Environment.getExternalStorageDirectory(), Constants.RECORDS_FOLDER_NAME);

        if (!recordsDir.exists()) {
            //recordsDir does not exists, create it
            if (!recordsDir.mkdirs()) {
                //failed to create recordsDir return null
                MyDebugger.log("Failed to create recordsDir");
                return null;
            }
        }

        File storageDir = new File(recordsDir, Constants.NOTES_FOLDER_NAME);

        if (!storageDir.exists()) {
            //storageDir does not exists, create it
            if (!storageDir.mkdirs()) {
                //failed to create storageDir return null
                MyDebugger.log("Failed to create VoiceRecord storageDir");
                return null;
            }
        }

        File audioFile;
        try {
            audioFile = File.createTempFile(
                    audioFileName,  /* prefix */
                    ".amr",         /* suffix */
                    storageDir      /* directory */
            );
        } catch (IOException e) {
            //failed to create audioFile, return null
            MyDebugger.log("IOException creating audioFile", e.getMessage());
            return null;
        }

        if (copy(srcUri, audioFile)) {
            //copying successful, return audio file path
            return audioFile.getAbsolutePath();
        } else {
            //failed to copy file, return null
            if (!audioFile.delete()) {
                //Failed to delete file, try deleteOnExit()
                audioFile.deleteOnExit();
            }
            return null;
        }
    }

    private static boolean copy(Uri src, File dst) {
        InputStream in;
        OutputStream out;
        try {
            in = MyApplication.getAppContext().getContentResolver().openInputStream(src);
            out = new FileOutputStream(dst);
        } catch (FileNotFoundException e) {
            MyDebugger.log("FileNotFoundException", e.getMessage());
            return false;
        }

        if (in == null) {
            //InputStream is null, abort copy
            MyDebugger.log("copy()", "InputStream is null");
            return false;
        }

        // Transfer bytes from in to out
        byte[] buf = new byte[1024];
        int len;
        try {
            while ((len = in.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
        } catch (IOException e) {
            MyDebugger.log("IOException while copying file", e.getMessage());
            return false;
        } finally {
            try {
                in.close();
                out.close();
            } catch (IOException e) {
                MyDebugger.log("Failed to close stream", e.getMessage());
            }
        }
        return true;
    }
}
