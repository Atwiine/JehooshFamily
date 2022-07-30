package com.example.jehooshfamily.ui.Main_Functions;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.example.jehooshfamily.R;
import com.example.jehooshfamily.ui.AskSection.DrawQns;
import com.example.jehooshfamily.ui.AskSection.Essay;
import com.example.jehooshfamily.ui.AskSection.Objectives;
import com.example.jehooshfamily.ui.AskSection.Voting;
import com.example.jehooshfamily.ui.MainActivity;
import com.example.jehooshfamily.ui.preferrences.SettingsActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class AskActivity extends AppCompatActivity {
    CardView essay, voting, objectives;
    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_ask);

        bottomNavigationView = findViewById(R.id.navigations);
        bottomNavigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

    }

    private final BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @SuppressLint("NonConstantResourceId")
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigationResponse:
                    startActivity(new Intent(AskActivity.this,MyResponses.class));
                    overridePendingTransition(0,0);
                    return true;

                case R.id.navigationHome:
                    startActivity(new Intent(AskActivity.this, MainActivity.class));
                    overridePendingTransition(0,0);
                    return true;
                case R.id.navigationHistory:
                    startActivity(new Intent(AskActivity.this,MyHistory.class));
                    overridePendingTransition(0,0);
                    return true;
                case R.id.navigationSettings:
                    startActivity(new Intent(AskActivity.this, SettingsActivity.class));
                    overridePendingTransition(0,0);
//                    DrawerLayout drawer = findViewById(R.id.drawer_layout);
//                    drawer.openDrawer(GravityCompat.START);
                    return true;
            }
            return false;
        }
    };

    public void openEssay(View view) {
        Intent essay = new Intent(AskActivity.this, Essay.class);
        startActivity(essay);
    }

    public void openObjectives(View view) {
        Intent objec = new Intent(AskActivity.this, Objectives.class);
        startActivity(objec);
    }

    public void openVoting(View view) {
        startActivity(new Intent(AskActivity.this, Voting.class));
    }

    public void goToResponses(View view) {
        startActivity(new Intent(AskActivity.this, MyResponses.class));
    }

    public void openDrawing(View view) {
        startActivity(new Intent(AskActivity.this, DrawQns.class));
    }
}