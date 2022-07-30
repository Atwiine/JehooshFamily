package com.example.jehooshfamily.ui.UserSection;

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
import com.example.jehooshfamily.ui.URLs.SessionManagerLogin;
import com.example.jehooshfamily.ui.URLs.Urls;
import com.github.ybq.android.spinkit.SpinKitView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginUser extends AppCompatActivity {
    Urls urls;
    SessionManagerLogin sessionManager;
    EditText uname;
    SpinKitView spinKitView;
    Button cirLoginButton;

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
        setContentView(R.layout.activity_login_employee);
        urls = new Urls();
        sessionManager = new SessionManagerLogin(this);

        uname = findViewById(R.id.empl_email);
//        upass = findViewById(R.id.empl_phone);

        spinKitView = findViewById(R.id.spin_kit);
        cirLoginButton = findViewById(R.id.cirLoginButton);
    }

    public void LoginMe(View view) {
//        String phone = upass.getText().toString();
        String eml = uname.getText().toString();
        if (!eml.isEmpty()) {
            login(eml);
//            startActivity(new Intent(LoginEmployee.this,MainEmployee.class));
        } else {

            uname.setError("Please enter username");
        }

    }

    private void login(final String email) {
        final ProgressDialog dialog = new ProgressDialog(LoginUser.this);
        dialog.setMessage("Logging please wait...");
dialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, urls.LOGIN_EMPLOYEE,
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
                                    String boss_id = jsonObject.getString("boss_id").trim();
                                    String boss_name = jsonObject.getString("boss_name").trim();
                                    String phone = jsonObject.getString("phone").trim();
                                    String email = jsonObject.getString("email").trim();
                                    String role = jsonObject.getString("role").trim();
                                    String id = jsonObject.getString("id").trim();


                                    sessionManager.createSession(id, names, email, phone, role, boss_id, boss_name);

                                    dialog.dismiss();
                                    cirLoginButton.setVisibility(View.VISIBLE);
                                    Toast.makeText(LoginUser.this, "Login successful", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(LoginUser.this, MainUser.class));
                                    overridePendingTransition(R.anim.slide_in_right, R.anim.stay);
                                    finish();
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(LoginUser.this, "login error, please try again", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                            cirLoginButton.setVisibility(View.VISIBLE);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(LoginUser.this, "login error , please check your connection and try again", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
                cirLoginButton.setVisibility(View.VISIBLE);
            }
        }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("email", email);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }


}