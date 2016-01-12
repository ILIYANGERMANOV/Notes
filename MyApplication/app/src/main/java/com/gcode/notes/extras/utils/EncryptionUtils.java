package com.gcode.notes.extras.utils;

import com.gcode.notes.data.NoteData;
import com.gcode.notes.data.base.ContentBase;
import com.gcode.notes.data.list.ListData;

import se.simbio.encryption.Encryption;

public class EncryptionUtils {
    private static EncryptionUtils mInstance;

    private Encryption mEncryption;
    private String mPassword;

    public EncryptionUtils getInstance(String password) {
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
        final byte[] ENCRYPTION_IV_BYTE_ARRAY = {13, 12, 19, 14, 88};
        mPassword = password;
        mEncryption = Encryption.getDefault(password, ENCRYPTION_SALT, ENCRYPTION_IV_BYTE_ARRAY);
    }

    //encrypt---------------------------------------------------------------------------------------------------------------
    public void encryptNoteData(NoteData noteData) throws Exception{
        encryptContentBase(noteData);
        if(noteData.hasDescription()) {
            //!NOTE: it is safe to use noteData#hasDescription(), cuz there is no constant such as NO_DESCRIPTION
            //which will be fucked up after encryption

            //note has description, encrypt it
            noteData.setDescription(mEncryption.encrypt(noteData.getDescription()));
        }
    }

    public void encryptListData(ListData listData) throws Exception{
        encryptContentBase(listData);

    }

    private void encryptContentBase(ContentBase contentBase) throws Exception {
        contentBase.setTitle(mEncryption.encrypt(contentBase.getTitle()));
        contentBase.setLastModifiedDate(mEncryption.encrypt(contentBase.getLastModifiedDate()));
        contentBase.setCreationDate(mEncryption.encrypt(contentBase.getCreationDate()));
        contentBase.setReminder(mEncryption.encrypt(contentBase.getReminder()));
    }
    //encrypt---------------------------------------------------------------------------------------------------------------

}
