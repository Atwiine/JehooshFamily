package com.example.jehooshfamily.ui.EmployeeSection;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.jehooshfamily.R;
import com.example.jehooshfamily.ui.URLs.SessionManagerLogin;

public class MainEmployee extends AppCompatActivity {
    SessionManagerLogin sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main_employee);
        sessionManager = new SessionManagerLogin(MainEmployee.this);
        sessionManager.checkLogin();
    }

    public void goOptionsQns(View view) {
        startActivity(new Intent(MainEmployee.this, TypesQns.class));
    }

    public void goInquire(View view) {
        Toast.makeText(this, "coming soon", Toast.LENGTH_SHORT).show();
    }
}