package com.example.jehooshfamily.ui.URLs;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.example.jehooshfamily.ui.HomeOptions;
import com.example.jehooshfamily.ui.UserSection.LoginUser;
import com.example.jehooshfamily.ui.UserSection.MainUser;

import java.util.HashMap;

public class SessionManagerLogin {
    public static final String NAMES = "NAMES_EMPLOYEE";
    public static final String EMAIL = "EMAIL_EMPLOYEE";
    public static final String PHONE = "PHONE_EMPLOYEE";
    public static final String ROLE = "ROLE_EMPLOYEE";
    public static final String BOSS_ID = "BOSS_ID";
    public static final String BOSS_NAME = "BOSS_NAME";
    public static final String ID = "ID_EMPLOYEE";
    private static final String PREF_NAME = "LOGIN_EMPLOYEE";
    private static final String LOGIN = "IS_LOGIN";
    public SharedPreferences.Editor editor;
    public Context context;
    SharedPreferences sharedPreferences;
    int PRIVATE_MODE = 0;
    private String id;
    private String names;
    private String email;
    private String phone;
    private String role;
    private String boss_id;
    private String boss_name;

    public SessionManagerLogin(Context context) {
        this.context = context;
        sharedPreferences = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = sharedPreferences.edit();
    }

    public void createSession(String id,
                              String names,
                              String email,
                              String phone,
                              String role,
                              String boss_id,
                              String boss_name) {
        this.id = id;
        this.names = names;
        this.email = email;
        this.phone = phone;
        this.role = role;
        this.boss_id = boss_id;
        this.boss_name = boss_name;
        editor.putBoolean(LOGIN, true);
        editor.putString(ID, id);
        editor.putString(NAMES, names);
        editor.putString(EMAIL, email);
        editor.putString(PHONE, phone);
        editor.putString(ROLE, role);
        editor.putString(BOSS_ID, boss_id);
        editor.putString(BOSS_NAME, boss_name);
        editor.apply();

    }

    public boolean isLogin() {
        return sharedPreferences.getBoolean(LOGIN, false);
    }

    public void checkLogin() {
        if (!this.isLogin()) {
            Intent i = new Intent(context, LoginUser.class);
            context.startActivity(i);
            ((MainUser) context).finish();
        }
    }

    public HashMap<String, String> getUserDetail() {
        HashMap<String, String> user = new HashMap<>();
        user.put(NAMES, sharedPreferences.getString(NAMES, null));
        user.put(EMAIL, sharedPreferences.getString(EMAIL, null));
        user.put(PHONE, sharedPreferences.getString(PHONE, null));
        user.put(ROLE, sharedPreferences.getString(ROLE, null));
        user.put(BOSS_ID, sharedPreferences.getString(BOSS_ID, null));
        user.put(BOSS_NAME, sharedPreferences.getString(BOSS_NAME, null));
        user.put(ID, sharedPreferences.getString(ID, null));
        return user;
    }

    public void logout() {
        editor.clear();
        editor.commit();
        Intent i = new Intent(context, HomeOptions.class);
        context.startActivity(i);


    }
}
