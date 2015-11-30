package com.gcode.notes.extras.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.widget.ImageView;

import com.gcode.notes.R;
import com.gcode.notes.extras.MyDebugger;
import com.gcode.notes.extras.utils.callbacks.PhotoSelectedCallback;
import com.gcode.notes.extras.values.Constants;
import com.squareup.picasso.Picasso;

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
                photoFile = FileUtils.createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
                MyDebugger.log("Creating imageFile IOException", ex.getMessage());
            }

            // Continue only if the photoFile was successfully created
            if (photoFile != null) {
                pathToPhoto = Uri.fromFile(photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
                activity.startActivityForResult(takePictureIntent, Constants.REQUEST_TAKE_PHOTO);
            } else {
                MyDebugger.log("Could not create file for the photo!");
            }
        }
    }

    public static void choosePhotoFromGallery(Activity activity) {
        Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        activity.startActivityForResult(intent, Constants.REQUEST_OPEN_GALLERY);
    }

    public static void addPhotoToGallery(Context context, Uri pathToPicture) {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        mediaScanIntent.setData(pathToPicture);
        context.sendBroadcast(mediaScanIntent);
    }

    public static void openPhotoInGallery(Activity activity, String pathToPhoto) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.parse(pathToPhoto), "image/*");
        activity.startActivityForResult(intent, Constants.OPEN_PHOTO_IN_GALLERY_REQ_CODE);
    }

    public static void loadPhoto(Context context, String photoPath, ImageView imageView) {
        Picasso.with(context).
                load(photoPath)
                .placeholder(R.drawable.ic_loop_black_48dp)
                .error(R.drawable.ic_error_black_48dp)
                .fit().centerCrop()
                .into(imageView);
    }

    public static void handleSelectedPhotoFromGallery(Activity activity, Intent data, PhotoSelectedCallback callback) {
        //photo selected from gallery
        Uri selectedImage = data.getData();
        String[] filePath = {MediaStore.Images.Media.DATA};
        Cursor c = activity.getContentResolver().query(selectedImage, filePath, null, null, null);
        if (c == null) {
            MyDebugger.log("handleSelectedPhotoFromGallery", "cursor is null, abort operation");
            return;
        }
        Uri photoUri = null;
        if (c.moveToFirst()) {
            int columnIndex = c.getColumnIndex(filePath[0]);
            photoUri = Uri.fromFile(new File(c.getString(columnIndex)));
        } else {
            MyDebugger.log("handleSelectedPhotoFromGallery", "Cursor is empty!");
        }
        c.close();
        if (photoUri != null) {
            //selected photoUri obtained successfully
            callback.onPhotoSelected(photoUri.toString());
        } else {
            MyDebugger.log("handleSelectedPhotoFromGallery", "photoUri is null");
        }
    }
}
