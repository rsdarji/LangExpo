package com.langexpo.utility;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.util.Set;

public class Session {

    private static SharedPreferences prefs;

    public Session(Context cntx) {
        /* TODO Auto-generated constructor stub */
        prefs = PreferenceManager.getDefaultSharedPreferences(cntx);
    }

    public static void set(String key,String value) {
        prefs.edit().putString(key, value).commit();
    }

    public static void set(String key, Set<String> value) {
        prefs.edit().putStringSet(key, value).commit();
    }

    public static Set<String> getSet(String key) {
        Set<String> value = prefs.getStringSet(key, null);
        return value;
    }

    public static String get(String key) {
        String value = prefs.getString(key,"");
        return value;
    }

    public void clear(){
        SharedPreferences.Editor editor = prefs.edit();
        editor.clear();
        editor.commit();
    }
}

