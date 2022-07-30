package com.example.jehooshfamily.ui;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
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

public class Register extends AppCompatActivity {
    private static final String TAG = "Register";

    SessionManager sessionManager;
    Urls urls;
    EditText names, email, phone, role, password, conpassword;
    Button cirRegisterButton;
    SpinKitView spinKitView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_register);
        urls = new Urls();
        sessionManager = new SessionManager(this);
        changeStatusBarColor();
        spinKitView = findViewById(R.id.spin_kit);
        names = findViewById(R.id.admin_name);
        email = findViewById(R.id.admin_email);
        phone = findViewById(R.id.admin_phone);
        role = findViewById(R.id.admin_role);
        password = findViewById(R.id.admin_password);
        conpassword = findViewById(R.id.admin_conpassword);
        cirRegisterButton = findViewById(R.id.cirRegisterButton);

        role.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(Register.this, "This field is already set", Toast.LENGTH_SHORT).show();
            }
        });

    }


    private void changeStatusBarColor() {
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
//            window.setStatusBarColor(Color.TRANSPARENT);
        window.setStatusBarColor(getResources().getColor(R.color.register_bk_color));
    }

    public void onLoginClick(View view) {
        startActivity(new Intent(this, Login.class));
        overridePendingTransition(R.anim.slide_in_left, android.R.anim.slide_out_right);

    }

    public void RegisterAdmin(View view) {
        String pass = password.getText().toString().trim();
        String cpass = conpassword.getText().toString().trim();
        String cnames = names.getText().toString().trim();
        String cemail = email.getText().toString().trim();
        String crole = "Admin";
        String cphone = phone.getText().toString().trim();


        if (!cnames.equals("") && !cemail.equals("") && !cphone.equals("")) {

            if (!pass.equals(cpass)) {
                Toast.makeText(this, "Passwords don't match", Toast.LENGTH_SHORT).show();
            } else {
                register();
            }
        } else {
            Toast.makeText(this, "All fields are required", Toast.LENGTH_SHORT).show();
        }

    }

    private void register() {
        final ProgressDialog dialog = new ProgressDialog(Register.this);
        dialog.setMessage("Please wait...");
        dialog.show();

        final String a_name = this.names.getText().toString();
        final String a_email = this.email.getText().toString();
        final String a_phone = this.phone.getText().toString();
        final String a_role = "Admin";
        final String a_password = this.password.getText().toString();


        StringRequest stringRequest = new StringRequest(Request.Method.POST, urls.REGISTER_ADMIN,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            Log.i("tagconvertstr", "[" + response + "]");
                            JSONObject object = new JSONObject(response);
                            String success = object.getString("success");
                            if (success.equals("1")) {
                                dialog.dismiss();
                                cirRegisterButton.setVisibility(View.VISIBLE);
                                Toast.makeText(Register.this, "Register success", Toast.LENGTH_LONG).show();
                                Intent add = new Intent(Register.this, Login.class);
                                startActivity(add);
                                overridePendingTransition(R.anim.slide_in_right, R.anim.stay);
                                finish();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(Register.this, "Register not successful, please try again ", Toast.LENGTH_LONG).show();
                            dialog.dismiss();
                            cirRegisterButton.setVisibility(View.VISIBLE);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(Register.this, "Register not successful, please check your internet connection and try again " +error.toString(), Toast.LENGTH_LONG).show();
                dialog.dismiss();
                cirRegisterButton.setVisibility(View.VISIBLE);
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("names", a_name);
                params.put("email", a_email);
                params.put("role", a_role);
                params.put("phone", a_phone);
                params.put("password", a_password);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }


    public void goLogin(View view) {
        startActivity(new Intent(Register.this, Login.class));
    }
}
