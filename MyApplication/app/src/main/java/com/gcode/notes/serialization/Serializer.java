package com.gcode.notes.serialization;


import com.gcode.notes.data.NoteData;
import com.gcode.notes.data.base.MyLocation;
import com.gcode.notes.data.list.ListData;
import com.gcode.notes.data.list.ListDataItem;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

public class Serializer {
    //TODO: REFACTOR AND OPTIMIZE
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

    public static String serializeImagesPathsList(List<String> attachedImagesList) {
        Gson gson = new Gson();
        return gson.toJson(attachedImagesList);
    }

    public static List<String> parseImagesPathsList(String serializedObject) {
        return new Gson().fromJson(serializedObject,
                new TypeToken<List<String>>() {
                }.getType());
    }

    public static String serializeMyLocation(MyLocation myLocation) {
        Gson gson = new Gson();
        return gson.toJson(myLocation);
    }

    public static MyLocation parseMyLocation(String serializedObject) {
        return new Gson().fromJson(serializedObject,
                new TypeToken<MyLocation>() {
                }.getType());
    }
}
