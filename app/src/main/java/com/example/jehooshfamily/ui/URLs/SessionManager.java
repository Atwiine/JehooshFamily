package com.example.jehooshfamily.ui.URLs;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.example.jehooshfamily.ui.HomeOptions;
import com.example.jehooshfamily.ui.Login;
import com.example.jehooshfamily.ui.MainActivity;

import java.util.HashMap;

public class SessionManager {
    public static final String NAMES = "NAMES";
    public static final String EMAIL = "EMAIL";
    public static final String PHONE = "PHONE";
    public static final String ROLE = "ROLE";
    public static final String PASSWORD = "PASSWORD";
    public static final String ID = "ID";
    private static final String PREF_NAME = "LOGIN_ADMIN";
    private static final String LOGIN = "IS_LOGIN";
    public SharedPreferences.Editor editor;
    public Context context;
    SharedPreferences sharedPreferences;
    int PRIVATE_MODE = 0;

    public SessionManager(Context context) {
        this.context = context;
        sharedPreferences = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = sharedPreferences.edit();
    }

    public void createSession(String names, String email, String phone, String role, String password, String id) {
        editor.putBoolean(LOGIN, true);
        editor.putString(NAMES, names);
        editor.putString(EMAIL, email);
        editor.putString(PHONE, phone);
        editor.putString(ROLE, role);
        editor.putString(PASSWORD, password);
        editor.putString(ID, id);
        editor.apply();

    }

    public boolean isLogin() {
        return sharedPreferences.getBoolean(LOGIN, false);
    }

    public void checkLogin() {
        if (!this.isLogin()) {
            Intent i = new Intent(context, Login.class);
            context.startActivity(i);
            ((MainActivity) context).finish();
        }
    }

    public HashMap<String, String> getUserDetail() {
        HashMap<String, String> user = new HashMap<>();
        user.put(NAMES, sharedPreferences.getString(NAMES, null));
        user.put(EMAIL, sharedPreferences.getString(EMAIL, null));
        user.put(PHONE, sharedPreferences.getString(PHONE, null));
        user.put(ROLE, sharedPreferences.getString(ROLE, null));
        user.put(PASSWORD, sharedPreferences.getString(PASSWORD, null));
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
