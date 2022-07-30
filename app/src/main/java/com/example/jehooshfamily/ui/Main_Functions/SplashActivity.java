package com.example.jehooshfamily.ui.Main_Functions;

import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.jehooshfamily.R;
import com.example.jehooshfamily.ui.HomeOptions;
import com.example.jehooshfamily.ui.URLs.PrefOptions;

public class SplashActivity extends AppCompatActivity {
    public static int SplashTime = 2000;
    ImageView logo;
    TextView motto;
    PrefOptions options;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);

//        if (new InternetDialog(this).getInternetStatus()) {
//
//        }

        logo = findViewById(R.id.imageViewss);

        new android.os.Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent in = new Intent(SplashActivity.this, HomeOptions.class);
                startActivity(in);
                finish();
            }
        }, SplashTime);

    }

}
