package com.example.jehooshfamily.ui.UserSection;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;

import com.example.jehooshfamily.R;

public class TypesQns extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_types_qns);
    }

    public void openEssay(View view) {
        startActivity(new Intent(TypesQns.this, UserEssay.class));
    }

    public void openObjectives(View view) {
        startActivity(new Intent(TypesQns.this, UserObjecitves.class));
    }

    public void openVoting(View view) {
        startActivity(new Intent(TypesQns.this, UserVote.class));
    }

    public void openDraw(View view) {
        startActivity(new Intent(TypesQns.this, UserDraw.class));
    }

    public void openAssistance(View view) {
    }
}