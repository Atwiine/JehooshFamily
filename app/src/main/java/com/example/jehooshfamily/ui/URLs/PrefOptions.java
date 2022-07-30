package com.example.jehooshfamily.ui.URLs;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.example.jehooshfamily.ui.HomeOptions;
import com.example.jehooshfamily.ui.MainActivity;
import com.example.jehooshfamily.ui.UserSection.MainUser;

public class PrefOptions {
    public static final String Admin = "Admin";
    public static final String User = "User";
    // Shared preferences file name
    private static final String PREF_NAME = "jehoosh family ";
    private static final String IS_FIRST_TIME_LAUNCH = "IsFirstTimeLaunch";
    private static final String ADMIN_SELECETED = "1";
    private static final String USER_SELECETED = "0";
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    Context _context;
    // shared pref mode
    int PRIVATE_MODE = 0;

    public PrefOptions(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public void createSession(String admin, String user) {
        editor.putBoolean(ADMIN_SELECETED, true);
        editor.putBoolean(USER_SELECETED, true);
        editor.putString(Admin, admin);
        editor.putString(User, user);
        editor.apply();

    }

    public boolean optionSelected() {
        return pref.getBoolean(ADMIN_SELECETED, false);
    }

    public boolean optionSelected2() {
        return pref.getBoolean(USER_SELECETED, false);
    }

    public void checkOptions() {
        if (!this.optionSelected()) {
            Intent i = new Intent(_context, HomeOptions.class);
            _context.startActivity(i);
            ((MainActivity) _context).finish();
        } else if (!this.optionSelected2()) {
            Intent i = new Intent(_context, HomeOptions.class);
            _context.startActivity(i);
            ((MainUser) _context).finish();
        } else {
            Intent i = new Intent(_context, HomeOptions.class);
            _context.startActivity(i);
        }
    }

}

