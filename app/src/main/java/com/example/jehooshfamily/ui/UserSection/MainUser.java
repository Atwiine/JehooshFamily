package com.example.jehooshfamily.ui.UserSection;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.jehooshfamily.R;
import com.example.jehooshfamily.ui.URLs.SessionManagerLogin;

import java.util.Calendar;
import java.util.HashMap;

public class MainUser extends AppCompatActivity {
    SessionManagerLogin sessionManager;
    Dialog message;
    TextView imageswitcher;
    //ViewFlipper flipper;
    String format;
    String AM, PM;
    TextView showTime, showMonth, showDate, showYear,names,username;
    LinearLayout layout_settings;
    RelativeLayout layout_welcome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main_employee);
        message = new Dialog(this);

        sessionManager = new SessionManagerLogin(MainUser.this);
        sessionManager.checkLogin();
        HashMap<String, String> user = sessionManager.getUserDetail();
       String namess = user.get(SessionManagerLogin.NAMES);

        names = findViewById(R.id.names);
        names.setText(namess);

        username = findViewById(R.id.username);
        username.setText(namess);


        layout_welcome = findViewById(R.id.layout_welcome);
        layout_settings = findViewById(R.id.layout_settings);
        imageswitcher = findViewById(R.id.imageswitcher);
        showDate = findViewById(R.id.showDate);
        showMonth = findViewById(R.id.showMonth);
        showYear = findViewById(R.id.showYear);
        showTime = findViewById(R.id.showTime);
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

//        if (hour == 0) {
//            hour += 12;
//            format = AM;
//
//        } else if (hour == 12) {
//            format = PM;
//            imageswitcher.setImageResource(R.drawable.afternoon2);
//        } else if (hour > 12) {
//            hour -= 12;
//            format = PM;
//        } else if (hour > 15) {
//            imageswitcher.setImageResource(R.drawable.even3);
//        } else {
//            format = AM;
//        }


//        Date currentTime = Calendar.getInstance().getTime();
//        String formmattedDate = DateFormat.getDateInstance(DateFormat.FULL).format(currentTime);
//        String[] splitDate = formmattedDate.split(",");
//
//        showDate.setText(splitDate[0]);
//        showMonth.setText(splitDate[1]);
//        showYear.setText(splitDate[2]);
//
////        showTime(hour, minute);
//
//        if (hour > 12) {
//            showTime.setText((hour - 12) + ":" + (minute + "pm"));
//
//        } else if (hour - 12 >= 4) {
//            imageswitcher.setText("Good evening");
//        } else if (hour == 12) {
//            showTime.setText("12" + ":" + (minute + "pm"));
//            imageswitcher.setText("Good afternoon");
//        } else if (hour < 12) {
//            if (hour != 0) {
//                showTime.setText(hour + ":" + (minute + "am"));
//                imageswitcher.setText("Good morning");
//            } else {
//                showTime.setText("12" + ":" + (minute + "am"));
//                imageswitcher.setText("Good morning");
//            }
//        }
    }

    /*private void showTime(int hour, int minute) {

        if (hour == 0) {
            hour += 12;
            format = AM;
            Toast.makeText(this, format = AM, Toast.LENGTH_SHORT).show();
            imageswitcher.setImageResource(R.drawable.morning2);
        } else if (hour == 12) {
            format = PM;
            imageswitcher.setImageResource(R.drawable.afternoon2);
        } else if (hour > 12) {
            hour -= 12;
            format = PM;
        } else if (hour > 15) {
            imageswitcher.setImageResource(R.drawable.even3);
        } else {
            format = AM;
        }
        showTime.setText(new StringBuilder().append(hour).append(":").append(minute).append(" ") + format);

    }*/

    public void goOptionsQns(View view) {
        startActivity(new Intent(MainUser.this, TypesQns.class));
    }

    public void goInquire(View view) {
        Toast.makeText(this, "coming soon", Toast.LENGTH_SHORT).show();
    }

    public void openEmployeeMenu(View view) {
        Animation main_layout = AnimationUtils.loadAnimation(MainUser.this, R.anim.anim);
        layout_welcome.setVisibility(View.GONE);
        layout_welcome.setAnimation(main_layout);
        layout_settings.setVisibility(View.VISIBLE);
    }

    public void openEmployeeStart(View view) {
    }


    public void closeSettings(View view) {
        Animation main_layout = AnimationUtils.loadAnimation(MainUser.this, R.anim.anim);
        layout_settings.setVisibility(View.INVISIBLE);
        layout_settings.setAnimation(main_layout);
        layout_welcome.setVisibility(View.VISIBLE);

    }

    public void goLogout(View view) {
        AlertDialog.Builder alertDialog2 = new AlertDialog.Builder(
                MainUser.this);
        alertDialog2.setTitle("Confirm to proceed");
        alertDialog2.setMessage("Are you sure you want to logout");
        alertDialog2.setIcon(R.drawable.ic_warning);
        alertDialog2.setPositiveButton("YES",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        sessionManager.logout();
                        finish();
                    }
                });
        alertDialog2.setNegativeButton("NO",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        alertDialog2.show();
    }
}