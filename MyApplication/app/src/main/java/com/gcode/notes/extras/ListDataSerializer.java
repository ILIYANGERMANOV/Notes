package com.gcode.notes.extras;


import com.gcode.notes.data.ListDataItem;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;

public class ListDataSerializer {
    public static ArrayList<ListDataItem> parse(String serializedObject) {
        return new Gson().fromJson(serializedObject,
                new TypeToken<ArrayList<ListDataItem>>() {
                }.getType());
    }

    public static String serialize(ArrayList<ListDataItem> list) {
        Gson gson = new Gson();
        return gson.toJson(list);
    }
}
