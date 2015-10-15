package com.gcode.notes.serialization;


import android.widget.LinearLayout;

import com.gcode.notes.data.ListData;
import com.gcode.notes.data.ListDataItem;
import com.gcode.notes.data.NoteData;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;

public class Serializer {
    public static String serializeListData(ListData listData) {
        Gson gson = new Gson();
        return gson.toJson(listData);
    }

    public static ListData parseListData(String serializedListData) {
        return new Gson().fromJson(serializedListData,
                new TypeToken<ListData>() {
                }.getType());
    }

    public static String serializeNoteData(NoteData noteData) {
        Gson gson = new Gson();
        return gson.toJson(noteData);
    }

    public static NoteData parseNoteData(String serializedNoteData) {
        return new Gson().fromJson(serializedNoteData,
                new TypeToken<NoteData>() {
                }.getType());
    }

    public static String serializeListDataItems(ArrayList<ListDataItem> listDataItems) {
        Gson gson = new Gson();
        return gson.toJson(listDataItems);
    }

    public static ArrayList<ListDataItem> parseListDataItems(String serializedObject) {
        return new Gson().fromJson(serializedObject,
                new TypeToken<ArrayList<ListDataItem>>() {
                }.getType());
    }

}
