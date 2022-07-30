package com.example.jehooshfamily.ui.Accounts;

import android.app.ProgressDialog;
import android.content.DialogInterface;
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
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.jehooshfamily.R;
import com.example.jehooshfamily.ui.Main_Functions.SeeRegisteredUser;
import com.example.jehooshfamily.ui.URLs.SessionManager;
import com.example.jehooshfamily.ui.URLs.Urls;
import com.github.ybq.android.spinkit.SpinKitView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class UserAccount extends AppCompatActivity {
    Urls urls;
    EditText names, email, phone, role;
    String getId, boss_names;
    SessionManager sessionManager;
    TextView id;
    SpinKitView spinKitView;
    Button cirRegisterButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_user_account);

        spinKitView = findViewById(R.id.spin_kit);
        cirRegisterButton = findViewById(R.id.cirRegisterButton);
        names = findViewById(R.id.edit_employee_name);
        email = findViewById(R.id.edit_employee_email);
        phone = findViewById(R.id.edit_employee_phone);
        role = findViewById(R.id.edit_employee_role);
        id = findViewById(R.id.edit_employee_id);

        names.setText(getIntent().getStringExtra("name"));
        email.setText(getIntent().getStringExtra("email"));
        phone.setText(getIntent().getStringExtra("phone"));
        role.setText(getIntent().getStringExtra("role"));
        id.setText(getIntent().getStringExtra("id"));


        urls = new Urls();
        sessionManager = new SessionManager(this);
        HashMap<String, String> user = sessionManager.getUserDetail();
        getId = user.get(SessionManager.ID);
        boss_names = user.get(SessionManager.NAMES);
    }

    public void EditEmployee(View view) {
        AlertDialog.Builder alertDialog2 = new AlertDialog.Builder(
                UserAccount.this);
        alertDialog2.setTitle("Confirm to proceed");
        alertDialog2.setMessage("Please you double check before proceeding and remember that you will have to notify this employee of your changes");
        alertDialog2.setIcon(R.drawable.ic_warning);
        alertDialog2.setPositiveButton("YES",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        UpdateEmployee();
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

    private void UpdateEmployee() {
        final ProgressDialog dialog = new ProgressDialog(UserAccount.this);
        dialog.setMessage("Please wait ...");

        Animation main_layout = AnimationUtils.loadAnimation(UserAccount.this, R.anim.anim);
        cirRegisterButton.setVisibility(View.GONE);
        cirRegisterButton.setAnimation(main_layout);

        spinKitView.setVisibility(View.VISIBLE);

        final String a_name = this.names.getText().toString();
        final String a_email = this.email.getText().toString();
        final String a_phone = this.phone.getText().toString();
        final String a_role = this.role.getText().toString();
        final String a_id = this.id.getText().toString();


        StringRequest stringRequest = new StringRequest(Request.Method.POST, urls.EDITEMPLOYEE,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            Log.i("tagconvertstr", "[" + response + "]");
                            JSONObject object = new JSONObject(response);
                            String success = object.getString("success");
                            if (success.equals("1")) {
                                dialog.dismiss();
                                spinKitView.setVisibility(View.GONE);
                                cirRegisterButton.setVisibility(View.VISIBLE);
                                Toast.makeText(UserAccount.this, "update success", Toast.LENGTH_LONG).show();
                                startActivity(new Intent(UserAccount.this, SeeRegisteredUser.class));
                                finish();
                            } else {
                                dialog.dismiss();
                                spinKitView.setVisibility(View.GONE);
                                cirRegisterButton.setVisibility(View.VISIBLE);
                                Toast.makeText(UserAccount.this, "Something went wrong, please try again", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(UserAccount.this, "updated not successfully, please check your internet connection and try again", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                            spinKitView.setVisibility(View.GONE);
                            cirRegisterButton.setVisibility(View.VISIBLE);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
//                Register not successful, please check your network and try again
                Toast.makeText(UserAccount.this, "updated not successfully, please check your internet connection and try again", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
                spinKitView.setVisibility(View.GONE);
                cirRegisterButton.setVisibility(View.VISIBLE);
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("names", a_name);
                params.put("email", a_email);
                params.put("phone", a_phone);
                params.put("role", a_role);
                params.put("id", a_id);
                params.put("boss_id", getId);

                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

}