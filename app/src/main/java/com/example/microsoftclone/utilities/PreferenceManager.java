package com.example.microsoftclone.utilities;

import android.content.Context;
import android.content.SharedPreferences;

public class PreferenceManager {
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    public PreferenceManager(Context context){
        sharedPreferences= android.preference.PreferenceManager.getDefaultSharedPreferences(context);
        editor=sharedPreferences.edit();
    }

    public void putBoolean(String s,Boolean b){
        editor.putBoolean(s,b);
        editor.commit();
    }

    public Boolean getBoolean(String s){
        return sharedPreferences.getBoolean(s,false);

    }

    public void putString(String s,String b){
        editor.putString(s,b);
        editor.commit();
    }

    public String getString(String s){
        return sharedPreferences.getString(s,null);
    }

    public void clearPreferences(){
        editor.clear();
        editor.commit();
    }
}
