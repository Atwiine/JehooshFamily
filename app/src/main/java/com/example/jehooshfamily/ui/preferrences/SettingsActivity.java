package com.example.jehooshfamily.ui.preferrences;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.jehooshfamily.R;
import com.example.jehooshfamily.ui.Accounts.AdminAccount;
import com.example.jehooshfamily.ui.URLs.SessionManager;
import com.google.android.material.snackbar.Snackbar;

import java.util.HashMap;

public class SettingsActivity extends AppCompatActivity {
    SessionManager sessionManager;
    String getId, names, password;
    TextView bossname;
    private Switch darkModeSwitch;
    LinearLayout vAdmin;
    EditText vAdminPass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_settings);

        bossname = findViewById(R.id.bossname);
        vAdmin = findViewById(R.id.verify_admin);
        vAdminPass = findViewById(R.id.verfiy_admin_password);


        sessionManager = new SessionManager(this);
        HashMap<String, String> user = sessionManager.getUserDetail();
        getId = user.get(SessionManager.ID);
        names = user.get(SessionManager.NAMES);
        password = user.get(SessionManager.PASSWORD);

        //set names
        bossname.setText(names);
    }

    public void goEditAdmin(View view) {
        vAdmin.setVisibility(View.VISIBLE);
    }

    public void goLogout(View view) {
        AlertDialog.Builder alertDialog2 = new AlertDialog.Builder(
                SettingsActivity.this);
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

    public void goNotifications(View view) {
        Toast.makeText(this, "coming soon", Toast.LENGTH_SHORT).show();
    }

    public void verify_passAdmin(View view) {
        String cpass = vAdminPass.getText().toString().trim();
        if (cpass.equals(password)) {
            startActivity(new Intent(SettingsActivity.this, AdminAccount.class));
        } else {
            Snackbar snackbar = Snackbar.make(view, "Sorry but the password entered does not match the current password", Snackbar.LENGTH_SHORT).setAction("Action", null);
            ViewGroup group = (ViewGroup) snackbar.getView();
            group.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.cardColorTeal));
            snackbar.show();
        }
    }

    public void goAbout(View view) {
        startActivity(new Intent(SettingsActivity.this, About.class));
    }


}
