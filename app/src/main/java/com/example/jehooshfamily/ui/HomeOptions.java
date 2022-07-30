package com.example.jehooshfamily.ui;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.example.jehooshfamily.R;
import com.example.jehooshfamily.ui.URLs.PrefOptions;
import com.example.jehooshfamily.ui.URLs.SessionManager;
import com.example.jehooshfamily.ui.URLs.SessionManagerLogin;
import com.example.jehooshfamily.ui.UserSection.MainUser;
import com.example.jehooshfamily.ui.Utilis.InternetDialog;

import java.util.HashMap;
import java.util.Objects;

public class HomeOptions extends AppCompatActivity {

    Dialog message;
    PrefOptions options;
    SessionManager sessionManager;
    SessionManagerLogin sessionManagerLogin;
    String getId, boss_names;
TextView rolee;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_home_options);
        message = new Dialog(this);

        options = new PrefOptions(this);


        // CALL getInternetStatus() function to check for internet and display error dialog
        if (new InternetDialog(this).getInternetStatus()) {
            Toast.makeText(this, "Internet connection is good to go", Toast.LENGTH_SHORT).show();
        }

        sessionManager = new SessionManager(HomeOptions.this);
        HashMap<String, String> user = sessionManager.getUserDetail();
        getId = user.get(SessionManager.ROLE);

        rolee = findViewById(R.id.rolee);
        rolee.setText(getId);

       String ere = rolee.getText().toString();
        if (ere.equals("Admin") || ere.equals("admin")){
            startActivity(new Intent(HomeOptions.this,MainActivity.class));
            finish();
        }

//user side
        sessionManagerLogin = new SessionManagerLogin(HomeOptions.this);
        HashMap<String, String> users = sessionManagerLogin.getUserDetail();
        getId = users.get(SessionManagerLogin.ROLE);

        rolee = findViewById(R.id.rolee);
        rolee.setText(getId);

        String eres = rolee.getText().toString();
        if (eres.equals("User") || eres.equals("user")){
            startActivity(new Intent(HomeOptions.this, MainUser.class));
            finish();
        }

/*cant be logged in both sides at the same time*/
//if (sessionManager.isLogin()){
//    sessionManagerLogin.logout();
//}else if (sessionManagerLogin.isLogin()){
//    sessionManager.logout();
//}
    }

    public void goAdmin(View view) {

        startActivity(new Intent(HomeOptions.this, MainActivity.class));
        overridePendingTransition(R.anim.slide_in_left, android.R.anim.slide_out_right);
    }

    public void goEmployee(View view) {

        startActivity(new Intent(HomeOptions.this, MainUser.class));
        overridePendingTransition(R.anim.slide_in_left, android.R.anim.slide_out_right);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void detspopup(View view) {
        message.setContentView(R.layout.company_dets);
        Button ok = message.findViewById(R.id.ok);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                message.dismiss();
            }
        });
        Objects.requireNonNull(message.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        message.show();
    }
}