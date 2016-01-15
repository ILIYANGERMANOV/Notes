package com.gcode.notes.extras.utils.encryption;

import com.gcode.notes.data.NoteData;
import com.gcode.notes.data.base.ContentBase;
import com.gcode.notes.data.list.ListData;
import com.gcode.notes.data.list.ListDataItem;
import com.scottyab.aescrypt.AESCrypt;

import java.util.ArrayList;

public class EncryptionUtils {
    //TODO: Optimization: created own AESCrypt, which doesn't create new cipher instance on every encrypt/decrypt
    private static EncryptionUtils mInstance;

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

    private EncryptionUtils(String password) {
        mPassword = password;
    }

    //encrypt---------------------------------------------------------------------------------------------------------------
    public void encryptNoteData(NoteData noteData) throws Exception {
        encryptContentBase(noteData); //encrypt note base
        if (noteData.hasDescription()) {
            //!NOTE: it is safe to use noteData#hasDescription(), cuz there is no constant such as NO_DESCRIPTION
            //which will be fucked up after encryption

            //note has description, encrypt it
            noteData.setDescription(AESCrypt.encrypt(mPassword, noteData.getDescription()));
        }
        if (noteData.hasAttachedImage()) {
            //note has attached image path/paths, encrypt them
            ArrayList<String> encryptedImagePaths = new ArrayList<>();
            for (String imagePath : noteData.getAttachedImagesPaths()) {
                encryptedImagePaths.add(AESCrypt.encrypt(mPassword, imagePath));
            }
            noteData.setAttachedImagesPaths(encryptedImagePaths);
        }
        if (noteData.hasAttachedAudio()) {
            //note has attached audio path, encrypt it
            noteData.setAttachedAudioPath(AESCrypt.encrypt(mPassword, noteData.getAttachedAudioPath()));
        }
    }

    public void encryptListData(ListData listData) throws Exception {
        encryptContentBase(listData); //encrypt list base
        if (listData.hasAttachedList()) {
            //list data has attached list, encrypt it
            ArrayList<ListDataItem> encryptedListItems = new ArrayList<>();
            for (ListDataItem listDataItem : listData.getList()) {
                listDataItem.setContent(AESCrypt.encrypt(mPassword, listDataItem.getContent()));
                encryptedListItems.add(listDataItem);
            }
            listData.setList(encryptedListItems);
        }
    }

    private void encryptContentBase(ContentBase contentBase) throws Exception {
        contentBase.setTitle(AESCrypt.encrypt(mPassword, contentBase.getTitle()));
        contentBase.setLastModifiedDate(AESCrypt.encrypt(mPassword, contentBase.getLastModifiedDate()));
        contentBase.setCreationDate(AESCrypt.encrypt(mPassword, contentBase.getCreationDate()));
    }
    //encrypt---------------------------------------------------------------------------------------------------------------

    //decrypt---------------------------------------------------------------------------------------------------------------
    public void decryptNoteData(NoteData noteData) throws Exception {
        decryptContentBase(noteData); //decrypt note base
        if (noteData.hasDescription()) {
            //note has description, decrypt it
            noteData.setDescription(AESCrypt.decrypt(mPassword, noteData.getDescription()));
        }
        if (noteData.hasAttachedImage()) {
            //note has attached image path/paths, decrypt them
            ArrayList<String> decryptedImagePaths = new ArrayList<>();
            for (String imagePath : noteData.getAttachedImagesPaths()) {
                decryptedImagePaths.add(AESCrypt.decrypt(mPassword, imagePath));
            }
            noteData.setAttachedImagesPaths(decryptedImagePaths);
        }
        if (noteData.hasAttachedAudio()) {
            //note has attached audio path, decrypt it
            noteData.setAttachedAudioPath(AESCrypt.decrypt(mPassword, noteData.getAttachedAudioPath()));
        }
    }

    public void decryptListData(ListData listData) throws Exception {
        decryptContentBase(listData); //decrypt list base
        if (listData.hasAttachedList()) {
            //list data has attached list, decrypt it
            ArrayList<ListDataItem> decryptedListItems = new ArrayList<>();
            for (ListDataItem listDataItem : listData.getList()) {
                listDataItem.setContent(AESCrypt.decrypt(mPassword, listDataItem.getContent()));
                decryptedListItems.add(listDataItem);
            }
            listData.setList(decryptedListItems);
        }
    }

    private void decryptContentBase(ContentBase contentBase) throws Exception {
        contentBase.setTitle(AESCrypt.decrypt(mPassword, contentBase.getTitle()));
        contentBase.setLastModifiedDate(AESCrypt.decrypt(mPassword, contentBase.getLastModifiedDate()));
        contentBase.setCreationDate(AESCrypt.decrypt(mPassword, contentBase.getCreationDate()));
    }
    //decrypt---------------------------------------------------------------------------------------------------------------
}
