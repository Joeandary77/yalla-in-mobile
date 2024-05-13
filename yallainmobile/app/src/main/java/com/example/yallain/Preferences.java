package com.example.yallain;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;

import androidx.annotation.RequiresApi;

//TODO ; all
public class Preferences {
    static final String PREF_NAME = "MyPrefs";
    private static final String NAME_KEY = "fullName";
    private static final String EMAIL_KEY = "email";
    private static final String USERTYPE_KEY = "userType";
    static SharedPreferences prefs;
    static SharedPreferences.Editor editor;
    static void init(Context context){
        prefs=context.getSharedPreferences(PREF_NAME,Context.MODE_PRIVATE);
        editor=prefs.edit();
    }
    static int GetUserType(Context context){
        init(context);
        return prefs.getInt(USERTYPE_KEY,UserModel.USER);
    }
    static String GetUserEmail(Context context){
        init(context);
        return prefs.getString(EMAIL_KEY,"");
    }
    static String GetUserName(Context context){
        init(context);
        return prefs.getString(NAME_KEY,"");
    }
    @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD)
    static void SaveName(Context context, String name){
        init(context);
        editor.putString(NAME_KEY,name);
        editor.apply();
    }
    @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD)
    static void SaveEmail(Context context, String email){
        init(context);
        editor.putString(EMAIL_KEY,email);
        editor.apply();
    }
    @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD)
    static void SaveUserType(Context context, int userType){
        init(context);
        editor.putInt(USERTYPE_KEY,userType);
        editor.apply();
    }


}
