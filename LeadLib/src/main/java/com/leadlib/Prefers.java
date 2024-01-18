package com.leadlib;

import android.content.Context;
import android.content.SharedPreferences;

public class Prefers {

    Context context;
    SharedPreferences prefs;

    public static String INSTANCE_CREATED = "false";

    public Prefers(Context con) {
        this.context = con;
        prefs = con.getSharedPreferences("sp", Context.MODE_PRIVATE);
    }

    public void setString(String key, String value) {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(key, value).apply();
    }

    public String getString(String key) {
        return prefs.getString(key, "");
    }
}
