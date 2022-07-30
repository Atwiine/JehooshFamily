package com.example.jehooshfamily.ui.Main_Functions;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.jehooshfamily.R;
import com.example.jehooshfamily.ui.URLs.SessionManager;
import com.example.jehooshfamily.ui.URLs.Urls;
import com.github.ybq.android.spinkit.SpinKitView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class RegisterUser extends AppCompatActivity {
    Urls urls;
    EditText names, email, phone, role;
    String getId, boss_names;
    SessionManager sessionManager;
    SpinKitView spinKitView;
    Button cirRegisterButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_register_employee);

        spinKitView = findViewById(R.id.spin_kit);
        cirRegisterButton = findViewById(R.id.cirRegisterButton);

        names = findViewById(R.id.employee_name);
        email = findViewById(R.id.employee_email);
        phone = findViewById(R.id.employee_phone);
        role = findViewById(R.id.employee_role);

        urls = new Urls();
        sessionManager = new SessionManager(this);
        HashMap<String, String> user = sessionManager.getUserDetail();
        getId = user.get(SessionManager.ID);
        boss_names = user.get(SessionManager.NAMES);

        role.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(RegisterUser.this, "User role is already set", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void onRegister(View view) {
        String cnames = names.getText().toString().trim();
        String cemail = email.getText().toString().trim();
        String crole = "User";
        String cphone = phone.getText().toString().trim();


        if (!cnames.equals("") && !cemail.equals("") && !cphone.equals("")) {
            register();
        } else {
            Toast.makeText(this, "All fields are required", Toast.LENGTH_SHORT).show();
        }
    }

    private void register() {
        final ProgressDialog dialog = new ProgressDialog(RegisterUser.this);
        dialog.setMessage("Please wait...");

        Animation main_layout = AnimationUtils.loadAnimation(RegisterUser.this, R.anim.anim);
        cirRegisterButton.setVisibility(View.GONE);
        cirRegisterButton.setAnimation(main_layout);
        spinKitView.setVisibility(View.VISIBLE);

        final String a_name = this.names.getText().toString();
        final String a_email = this.email.getText().toString();
        final String a_phone = this.phone.getText().toString();
        final String a_role = "User";


        StringRequest stringRequest = new StringRequest(Request.Method.POST, urls.ADD_EMPLOYEE,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            Log.i("tagconvertstr", "[" + response + "]");
                            JSONObject object = new JSONObject(response);
                            String success = object.getString("success");
                            if (success.equals("1")) {
                                dialog.dismiss();
                                Toast.makeText(RegisterUser.this, "Register success", Toast.LENGTH_LONG).show();
                                Intent add = new Intent(RegisterUser.this, SeeRegisteredUser.class);
                                startActivity(add);
                                finish();
                            } else {
                                dialog.dismiss();
                                Toast.makeText(RegisterUser.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();

                            Toast.makeText(RegisterUser.this, "Register not successful, please try again ", Toast.LENGTH_LONG).show();
                            dialog.dismiss();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(RegisterUser.this, "Register not successful, please check your network and try again", Toast.LENGTH_LONG).show();
                dialog.dismiss();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("names", a_name);
                params.put("email", a_email);
                params.put("phone", a_phone);
                params.put("role", a_role);
                params.put("boss_name", boss_names);
                params.put("boss_id", getId);

                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    public void goRegEmployee(View view) {
        startActivity(new Intent(RegisterUser.this, SeeRegisteredUser.class));
    }
}