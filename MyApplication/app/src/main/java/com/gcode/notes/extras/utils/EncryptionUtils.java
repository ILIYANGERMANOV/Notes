package com.gcode.notes.extras.utils;

import com.gcode.notes.data.NoteData;
import com.gcode.notes.data.base.ContentBase;
import com.gcode.notes.data.list.ListData;
import com.gcode.notes.data.list.ListDataItem;

import java.util.ArrayList;

import se.simbio.encryption.Encryption;

public class EncryptionUtils {
    private static EncryptionUtils mInstance;

    private Encryption mEncryption;
    private static String mPassword;

    public static EncryptionUtils getInstance(String password) {
        //!NOTE: if mPassword is null it won't be problem, cuz mInstance will be null too
        //so it will enter the if condition instantly
        if (mInstance == null || !password.equals(mPassword)) {
            //mInstance is not created or new password is selected
            mInstance = new EncryptionUtils(password);
        }
        return mInstance;
    }

    public Encryption getEncryption() {
        return mEncryption;
    }

    public String getPassword() {
        return mPassword;
    }

    private EncryptionUtils(String password) {
        final String ENCRYPTION_SALT = "samo_#Levski!";
        final byte[] ENCRYPTION_IV_BYTE_ARRAY = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16};
        mPassword = password;
        mEncryption = Encryption.getDefault(password, ENCRYPTION_SALT, ENCRYPTION_IV_BYTE_ARRAY);
    }

    //encrypt---------------------------------------------------------------------------------------------------------------
    public void encryptNoteData(NoteData noteData) throws Exception {
        encryptContentBase(noteData); //encrypt note base
        if (noteData.hasDescription()) {
            //!NOTE: it is safe to use noteData#hasDescription(), cuz there is no constant such as NO_DESCRIPTION
            //which will be fucked up after encryption

            //note has description, encrypt it
            noteData.setDescription(mEncryption.encrypt(noteData.getDescription()));
        }
        if (noteData.hasAttachedImage()) {
            //note has attached image path/paths, encrypt them
            ArrayList<String> encryptedImagePaths = new ArrayList<>();
            for (String imagePath : noteData.getAttachedImagesPaths()) {
                encryptedImagePaths.add(mEncryption.encrypt(imagePath));
            }
            noteData.setAttachedImagesPaths(encryptedImagePaths);
        }
        if (noteData.hasAttachedAudio()) {
            //note has attached audio path, encrypt it
            noteData.setAttachedAudioPath(mEncryption.encrypt(noteData.getAttachedAudioPath()));
        }
    }

    public void encryptListData(ListData listData) throws Exception {
        encryptContentBase(listData); //encrypt list base
        if (listData.hasAttachedList()) {
            //list data has attached list, encrypt it
            ArrayList<ListDataItem> encryptedListItems = new ArrayList<>();
            for (ListDataItem listDataItem : listData.getList()) {
                listDataItem.setContent(mEncryption.encrypt(listDataItem.getContent()));
                encryptedListItems.add(listDataItem);
            }
            listData.setList(encryptedListItems);
        }
    }

    private void encryptContentBase(ContentBase contentBase) throws Exception {
        contentBase.setTitle(mEncryption.encrypt(contentBase.getTitle()));
        contentBase.setLastModifiedDate(mEncryption.encrypt(contentBase.getLastModifiedDate()));
        contentBase.setCreationDate(mEncryption.encrypt(contentBase.getCreationDate()));
        if (contentBase.hasExpirationDate()) {
            //note has expiration date, encrypt it
            contentBase.setExpirationDate(mEncryption.encrypt(contentBase.getExpirationDate()));
        }
        if (contentBase.hasReminder()) {
            //note has reminder, encrypt it
            contentBase.setReminder(mEncryption.encrypt(contentBase.getReminder()));
        }
    }
    //encrypt---------------------------------------------------------------------------------------------------------------

}
