package com.example.jehooshfamily.ui.Main_Functions;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.jehooshfamily.R;
import com.example.jehooshfamily.ui.MainActivity;
import com.example.jehooshfamily.ui.ResponseSection.ResponseQnDrawing;
import com.example.jehooshfamily.ui.ResponseSection.ResponseQnEssay;
import com.example.jehooshfamily.ui.ResponseSection.ResponseQnObjective;
import com.example.jehooshfamily.ui.ResponseSection.ResponseQnVotes;
import com.example.jehooshfamily.ui.preferrences.SettingsActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MyResponses extends AppCompatActivity {
    private BottomNavigationView bottomNavigationView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_my_responses);


        bottomNavigationView = findViewById(R.id.navigations);
        bottomNavigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

    }
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigationAsk:
                    startActivity(new Intent(MyResponses.this,AskActivity.class));
                    overridePendingTransition(0,0);
                    return true;

                case R.id.navigationHome:
                    startActivity(new Intent(MyResponses.this,MainActivity.class));
                    overridePendingTransition(0,0);
                    return true;
                case R.id.navigationHistory:
                    startActivity(new Intent(MyResponses.this,MyHistory.class));
                    overridePendingTransition(0,0);
                    return true;
                case R.id.navigationSettings:
                    startActivity(new Intent(MyResponses.this, SettingsActivity.class));
                    overridePendingTransition(0,0);
//                    DrawerLayout drawer = findViewById(R.id.drawer_layout);
//                    drawer.openDrawer(GravityCompat.START);
                    return true;
            }
            return false;
        }
    };

    public void openRespVoting(View view) {
        startActivity(new Intent(MyResponses.this, ResponseQnVotes.class));
    }

    public void openRespObjectives(View view) {
        startActivity(new Intent(MyResponses.this, ResponseQnObjective.class));

    }

    public void openRespEssay(View view) {
        startActivity(new Intent(MyResponses.this, ResponseQnEssay.class));

    }

    public void openRespDrawing(View view) {
        startActivity(new Intent(MyResponses.this, ResponseQnDrawing.class));

    }
}