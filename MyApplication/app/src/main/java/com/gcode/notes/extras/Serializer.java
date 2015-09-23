package com.gcode.notes.extras;


import com.gcode.notes.data.ListData;
import com.gcode.notes.data.ListDataItem;
import com.gcode.notes.data.NoteData;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;

public class Serializer {
    public static String serialize(ArrayList<ListDataItem> list) {
        Gson gson = new Gson();
        return gson.toJson(list);
    }

    public static ArrayList<ListDataItem> parse(String serializedObject) {
        return new Gson().fromJson(serializedObject,
                new TypeToken<ArrayList<ListDataItem>>() {
                }.getType());
    }

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

}
