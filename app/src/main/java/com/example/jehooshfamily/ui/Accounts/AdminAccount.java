package com.example.jehooshfamily.ui.Accounts;

import android.app.ProgressDialog;
import android.content.DialogInterface;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class AdminAccount extends AppCompatActivity {
    EditText namesAdmin, emailAdmin, phoneAdmin, roleAdmin, passwordAdmin;
    TextView idAdmin;
    SessionManager sessionManager;
    Urls urls;
    String getId, names, password, email, role, phone;
    SpinKitView spinKitView;
    Button cirRegisterButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_admin_account);

        urls = new Urls();
        sessionManager = new SessionManager(this);
        spinKitView = findViewById(R.id.spin_kit);
        cirRegisterButton = findViewById(R.id.cirRegisterButton);
        namesAdmin = findViewById(R.id.edit_admin_name);
        emailAdmin = findViewById(R.id.edit_admin_email);
        phoneAdmin = findViewById(R.id.edit_admin_phone);
        roleAdmin = findViewById(R.id.edit_admin_role);
        passwordAdmin = findViewById(R.id.edit_admin_password);
        idAdmin = findViewById(R.id.edit_admin_id);

        HashMap<String, String> user = sessionManager.getUserDetail();
        getId = user.get(SessionManager.ID);
        names = user.get(SessionManager.NAMES);
        password = user.get(SessionManager.PASSWORD);
        email = user.get(SessionManager.EMAIL);
        role = user.get(SessionManager.ROLE);
        phone = user.get(SessionManager.PHONE);


        idAdmin.setText(getId);
        namesAdmin.setText(names);
        passwordAdmin.setText(password);
        emailAdmin.setText(email);
        roleAdmin.setText(role);
        phoneAdmin.setText(phone);

    }


    public void EditAdmin(View view) {
        AlertDialog.Builder alertDialog2 = new AlertDialog.Builder(
                AdminAccount.this);
        alertDialog2.setTitle("Confirm to proceed");
        alertDialog2.setMessage("Please you double check before proceeding");
        alertDialog2.setIcon(R.drawable.ic_warning);
        alertDialog2.setPositiveButton("YES",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        UpdateAdmin();
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

    private void UpdateAdmin() {
        final String Anames = namesAdmin.getText().toString().trim();
        final String Aphone = phoneAdmin.getText().toString().trim();
        final String Arole = roleAdmin.getText().toString().trim();
        final String Apassword = passwordAdmin.getText().toString().trim();
        final String Aemail = emailAdmin.getText().toString().trim();

        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage("Please wait as we update your profile...");

        Animation main_layout = AnimationUtils.loadAnimation(AdminAccount.this, R.anim.anim);
        cirRegisterButton.setVisibility(View.GONE);
        cirRegisterButton.setAnimation(main_layout);

        spinKitView.setVisibility(View.VISIBLE);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, urls.EDITADMIN,
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
                                Toast.makeText(AdminAccount.this, " profile updated successfully", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            dialog.dismiss();
                            spinKitView.setVisibility(View.GONE);
                            cirRegisterButton.setVisibility(View.VISIBLE);
                            Toast.makeText(AdminAccount.this, "profile updated not successfully, please try again", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                dialog.dismiss();
                spinKitView.setVisibility(View.GONE);
                cirRegisterButton.setVisibility(View.VISIBLE);
                Toast.makeText(AdminAccount.this, "profile updated not successfully, please check your internet connection and try again", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("names", Anames);
                params.put("email", Aemail);
                params.put("role", Arole);
                params.put("phone", Aphone);
                params.put("password", Apassword);
                params.put("id", getId);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }
}