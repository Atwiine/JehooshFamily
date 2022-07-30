package com.example.jehooshfamily.ui.EmployeeSection;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;

import com.example.jehooshfamily.R;

public class TypesQns extends AppCompatActivity {

    /*come back and fix the layout design*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_types_qns);
    }

    public void openEssay(View view) {
        startActivity(new Intent(TypesQns.this, EmployeeEssay.class));
    }

    public void openObjectives(View view) {
        startActivity(new Intent(TypesQns.this, EmployeeObjecitves.class));
    }

    public void openVoting(View view) {
        startActivity(new Intent(TypesQns.this, EmployeeVote.class));
    }
}