package com.gcode.notes.extras.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.gcode.notes.extras.MyDebugger;
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

    public static void choosePhotoFromGallery(Activity activity) {
        Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        activity.startActivityForResult(intent, Constants.REQUEST_OPEN_GALLERY);
    }

    public static void addPhotoToGallery(Context context, Uri pathToPicture) {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        mediaScanIntent.setData(pathToPicture);
        context.sendBroadcast(mediaScanIntent);
    }

    public static void addPhotoToContainer(Context context, LinearLayout imagesContainer, Uri photoUri) {
        //TODO: scale down picture in order to not skip frames
        ImageView imageView = new ImageView(context);
        imageView.setMaxHeight(Utils.convertDpInPixels(300));
        imageView.setAdjustViewBounds(true);
        imagesContainer.addView(imageView);
        Picasso.with(context).load(photoUri).into(imageView);
    }
}
