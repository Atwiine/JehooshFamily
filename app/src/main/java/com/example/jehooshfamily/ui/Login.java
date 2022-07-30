package com.example.jehooshfamily.ui;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Login extends AppCompatActivity {

    Urls urls;
    SessionManager sessionManager;
    EditText uname, uemail;
    Button cirLoginButton;
    SpinKitView spinKitView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //for changing status bar icon colors
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
        setContentView(R.layout.activity_login);


        spinKitView = findViewById(R.id.spin_kit);
        cirLoginButton = findViewById(R.id.cirLoginButton);

        urls = new Urls();
        sessionManager = new SessionManager(Login.this);

        uname = findViewById(R.id.admin_uname);
        uemail = findViewById(R.id.admin_uemail);
    }


    public void ForgotPassword(View view) {
        Toast.makeText(this, "coming soon", Toast.LENGTH_SHORT).show();
    }

    public void LoginMe(View view) {
        String eml = uemail.getText().toString();
        String names = uname.getText().toString();
        if (!eml.isEmpty() && !names.isEmpty()) {
            login(names, eml);
        } else {
            uemail.setError("Please enter email");
            uname.setError("Please enter username");
        }
    }


    private void login(final String name, final String email) {
        final ProgressDialog dialog = new ProgressDialog(Login.this);
        dialog.setMessage("Logging please wait...");
dialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, urls.LOGIN_ADMIN,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            Log.i("tagconvertstr", "[" + response + "]");
                            JSONObject object = new JSONObject(response);
                            String success = object.getString("success");
                            JSONArray jsonArray = object.getJSONArray("login");
                            if (success.equals("1")) {

                                for (int i = 0; i < jsonArray.length(); i++) {
                                    Log.i("tagconvertstr", "[" + response + "]");
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                                    String names = jsonObject.getString("names").trim();
                                    String password = jsonObject.getString("password").trim();
                                    String phone = jsonObject.getString("phone").trim();
                                    String email = jsonObject.getString("email").trim();
                                    String role = jsonObject.getString("role").trim();
                                    String id = jsonObject.getString("id").trim();


                                    sessionManager.createSession(names, email, phone, role, password, id);

                                    dialog.dismiss();
                                    cirLoginButton.setVisibility(View.VISIBLE);
                                    Toast.makeText(Login.this, "Login successful", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(Login.this, MainActivity.class));
                                    overridePendingTransition(R.anim.slide_in_right, R.anim.stay);
                                    finish();

                                }
                            }else {
                                Toast.makeText(Login.this, "Account not found, please enter the correct credentials and try again", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(Login.this, "login error, please try again", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                            cirLoginButton.setVisibility(View.VISIBLE);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(Login.this, "login error, please check your connection and try again", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
                cirLoginButton.setVisibility(View.VISIBLE);
            }
        }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("email", email);
                params.put("names", name);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }


    public void onRegisterClick(View view) {
        startActivity(new Intent(this, Register.class));
        overridePendingTransition(R.anim.slide_in_left, android.R.anim.slide_out_right);

    }

    public void gooRegisterClick(View view) {
        startActivity(new Intent(this, Register.class));
        overridePendingTransition(R.anim.slide_in_left, android.R.anim.slide_out_right);

    }
}
