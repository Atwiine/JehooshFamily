package com.example.jehooshfamily.ui.Main_Functions;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.jehooshfamily.R;
import com.example.jehooshfamily.ui.BottomNavigationViewHelper;
import com.example.jehooshfamily.ui.HistorySection.HistoryDrawing;
import com.example.jehooshfamily.ui.HistorySection.HistoryEssay;
import com.example.jehooshfamily.ui.HistorySection.HistoryObjectives;
import com.example.jehooshfamily.ui.HistorySection.HistoryVoting;
import com.example.jehooshfamily.ui.MainActivity;
import com.example.jehooshfamily.ui.preferrences.SettingsActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MyHistory extends AppCompatActivity {
    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_my_history);

        bottomNavigationView = findViewById(R.id.navigations);
        bottomNavigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        BottomNavigationViewHelper.disableShiftMode(bottomNavigationView);
        Menu menu = bottomNavigationView.getMenu();
        MenuItem menuItem = menu.getItem(3);
        menuItem.setChecked(true);
    }

    private final BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @SuppressLint("NonConstantResourceId")
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigationResponse:
                    startActivity(new Intent(MyHistory.this,MyResponses.class));
                    overridePendingTransition(0,0);
                    return true;

                case R.id.navigationHome:
                    startActivity(new Intent(MyHistory.this, MainActivity.class));
                    overridePendingTransition(0,0);
                    return true;
                case R.id.navigationAsk:
                    startActivity(new Intent(MyHistory.this,AskActivity.class));
                    overridePendingTransition(0,0);
                    return true;
                case R.id.navigationSettings:
                    startActivity(new Intent(MyHistory.this, SettingsActivity.class));
                    overridePendingTransition(0,0);
//                    DrawerLayout drawer = findViewById(R.id.drawer_layout);
//                    drawer.openDrawer(GravityCompat.START);
                    return true;
            }
            return false;
        }
    };

    public void openHistoryEssay(View view) {
        startActivity(new Intent(MyHistory.this, HistoryEssay.class));
    }

    public void openHistoryObjectives(View view) {
        startActivity(new Intent(MyHistory.this, HistoryObjectives.class));
    }

    public void openHistoryVoting(View view) {
        startActivity(new Intent(MyHistory.this, HistoryVoting.class));
    }

    public void openHistoryDrawing(View view) {
        startActivity(new Intent(MyHistory.this, HistoryDrawing.class));
    }
}