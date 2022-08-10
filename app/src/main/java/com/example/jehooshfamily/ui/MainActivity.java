package com.example.jehooshfamily.ui;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.jehooshfamily.R;
import com.example.jehooshfamily.ui.Main_Functions.AskActivity;
import com.example.jehooshfamily.ui.Main_Functions.MyHistory;
import com.example.jehooshfamily.ui.Main_Functions.MyResponses;
import com.example.jehooshfamily.ui.Main_Functions.RegisterUser;
import com.example.jehooshfamily.ui.Main_Functions.SeeRegisteredUser;
import com.example.jehooshfamily.ui.URLs.SessionManager;
import com.example.jehooshfamily.ui.preferrences.SettingsActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    //
    private static final int MODE_DARK = 0;
    private static final int MODE_LIGHT = 1;
    SessionManager sessionManager;
    private BottomNavigationView bottomNavigationView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        sessionManager = new SessionManager(MainActivity.this);
        sessionManager.checkLogin();


        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        bottomNavigationView = findViewById(R.id.navigations);
        bottomNavigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        BottomNavigationViewHelper.disableShiftMode(bottomNavigationView);
        Menu menu = bottomNavigationView.getMenu();
        MenuItem menuItem = menu.getItem(0);
        menuItem.setChecked(true);

    }


        private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigationAsk:
                    startActivity(new Intent(MainActivity.this,AskActivity.class));
                    overridePendingTransition(0,0);
                    return true;
                case R.id.navigationResponse:
                    startActivity(new Intent(MainActivity.this,MyResponses.class));
                    overridePendingTransition(0,0);
                    return true;
//                case R.id.navigationHome:
//                    startActivity(new Intent(MainActivity.this,MainActivity.class));
//                    return true;
                case R.id.navigationHistory:
                    startActivity(new Intent(MainActivity.this,MyHistory.class));
                    overridePendingTransition(0,0);
                    return true;
                case R.id.navigationSettings:
                    startActivity(new Intent(MainActivity.this,SettingsActivity.class));
                    overridePendingTransition(0,0);
//                    DrawerLayout drawer = findViewById(R.id.drawer_layout);
//                    drawer.openDrawer(GravityCompat.START);
                    return true;
            }
            return false;
        }
    };


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            startActivity(new Intent(MainActivity.this, MainActivity.class));
        } else if (id == R.id.nav_ask) {
            startActivity(new Intent(MainActivity.this, AskActivity.class));

        } else if (id == R.id.nav_response) {
            startActivity(new Intent(MainActivity.this, MyResponses.class));
        } else if (id == R.id.nav_history) {
            startActivity(new Intent(MainActivity.this, MyHistory.class));
        } else if (id == R.id.nav_settings) {
            startActivity(new Intent(MainActivity.this, SettingsActivity.class));
        }
//        else if (id == R.id.nav_logout) {
//            startActivity(new Intent(MainActivity.this,AskActivity.class));
//            //code for setting dark mode
//            //true for dark mode, false for day mode, currently toggling on each click
//            DarkModePrefManager darkModePrefManager = new DarkModePrefManager(this);
//            darkModePrefManager.setDarkMode(!darkModePrefManager.isNightMode());
//            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
//            recreate();
//
//        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    //create a seperate class file, if required in multiple activities
    public void setDarkMode(Window window) {
        if (new DarkModePrefManager(this).isNightMode()) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            changeStatusBar(MODE_DARK, window);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            changeStatusBar(MODE_LIGHT, window);
        }
    }

    public void changeStatusBar(int mode, Window window) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(this.getResources().getColor(R.color.contentStatusBar));
            //Light mode
            if (mode == MODE_LIGHT) {
                window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            }
        }
    }

    public void openHistory(View view) {
        Intent his = new Intent(MainActivity.this, MyHistory.class);
        startActivity(his);
    }

    public void openSeeEmployee(View view) {
        Intent see = new Intent(MainActivity.this, SeeRegisteredUser.class);
        startActivity(see);
    }

    public void openAddEmployee(View view) {
        Intent add = new Intent(MainActivity.this, RegisterUser.class);
        startActivity(add);
    }

    public void openAsk(View view) {
        Intent ask = new Intent(MainActivity.this, AskActivity.class);
        startActivity(ask);
    }

    public void openResponses(View view) {
        Intent ask = new Intent(MainActivity.this, MyResponses.class);
        startActivity(ask);
    }

    public void openSettings(View view) {
        startActivity(new Intent(MainActivity.this, SettingsActivity.class));
    }
}
