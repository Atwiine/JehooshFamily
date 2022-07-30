package com.example.jehooshfamily.ui.Main_Functions;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.jehooshfamily.R;
import com.example.jehooshfamily.ui.Adapters.ViewRegisteredEmployeesAdapter;
import com.example.jehooshfamily.ui.MainActivity;
import com.example.jehooshfamily.ui.Models.RegisteredEmployee_Model;
import com.example.jehooshfamily.ui.URLs.SessionManager;
import com.example.jehooshfamily.ui.URLs.Urls;
import com.github.ybq.android.spinkit.SpinKitView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SeeRegisteredUser extends AppCompatActivity {

    Urls urls;
    RecyclerView recyclerView_reg_employees;
    CardView no_employees;
    List<RegisteredEmployee_Model> mData;
    SessionManager sessionManager;
    ViewRegisteredEmployeesAdapter employeesAdapter;
    String getId, names;
    EditText filteredit;
    RelativeLayout relaFilter;
    SpinKitView spinKitView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_see_registered_employees);

        spinKitView = findViewById(R.id.spin_kit);
        filteredit = findViewById(R.id.editFilterEssay);
        relaFilter = findViewById(R.id.relaFilter);
        no_employees = findViewById(R.id.no_employees);
        urls = new Urls();
        sessionManager = new SessionManager(this);
        HashMap<String, String> user = sessionManager.getUserDetail();
        getId = user.get(SessionManager.ID);
        names = user.get(SessionManager.NAMES);

        //handle recyclerview
        recyclerView_reg_employees = findViewById(R.id.recyclerView_see_employee);
        mData = new ArrayList<>();
        recyclerView_reg_employees.setLayoutManager(new LinearLayoutManager(this));
        employeesAdapter = new ViewRegisteredEmployeesAdapter(this, mData);
        recyclerView_reg_employees.setAdapter(employeesAdapter);
        loadEmployees();
        setFilteredit();

    }

    public void openEditFilter(View view) {
        filteredit.setVisibility(View.VISIBLE);
    }

    public void setFilteredit() {
        filteredit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filteredit.setFocusableInTouchMode(true);
                filteredit.setFocusable(true);
            }
        });
        filteredit.setFocusableInTouchMode(false);
        filteredit.setFocusable(false);
        filteredit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                filter(s.toString());
            }
        });
    }

    private void filter(String text) {
        ArrayList<RegisteredEmployee_Model> filteredList = new ArrayList<>();
        for (RegisteredEmployee_Model item : mData) {
            if (item.getNames().toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(item);
            }
        }

        employeesAdapter.filterList(filteredList);
    }

    public void goAddEmployees(View view) {
        startActivity(new Intent(SeeRegisteredUser.this, RegisterUser.class));
    }

    private void loadEmployees() {
        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage("Loading...");

        spinKitView.setVisibility(View.VISIBLE);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, urls.SEE_EMPLOYEE,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            Log.i("tagconvertstr", "[" + response + "]");

                            JSONArray tips = new JSONArray(response);
                            if (tips.length() == 0) {
                                spinKitView.setVisibility(View.GONE);
                                no_employees.setVisibility(View.VISIBLE);
                            } else {

                                for (int i = 0; i < tips.length(); i++) {
                                    Log.i("tagconvertstr", "[" + response + "]");

                                    JSONObject marketsObjects = tips.getJSONObject(i);

                                    String name = marketsObjects.getString("names");
                                    String phone = marketsObjects.getString("phone");
                                    String email = marketsObjects.getString("email");
                                    String role = marketsObjects.getString("role");
                                    String boss_name = marketsObjects.getString("boss_name");
                                    String date_register = marketsObjects.getString("date_register");
                                    String id = marketsObjects.getString("id");

                                    dialog.dismiss();
                                    spinKitView.setVisibility(View.GONE);
                                    no_employees.setVisibility(View.GONE);
                                    RegisteredEmployee_Model registeredEmployee_model =
                                            new RegisteredEmployee_Model(id, name, phone, email, role, boss_name, date_register);
                                    mData.add(registeredEmployee_model);

                                }
                                employeesAdapter = new ViewRegisteredEmployeesAdapter(SeeRegisteredUser.this, mData);
                                recyclerView_reg_employees.setAdapter(employeesAdapter);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            dialog.dismiss();
                            spinKitView.setVisibility(View.GONE);
                            no_employees.setVisibility(View.VISIBLE);
                            Toast.makeText(SeeRegisteredUser.this, " something went wrong, please try again", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                dialog.dismiss();
                spinKitView.setVisibility(View.GONE);
                no_employees.setVisibility(View.VISIBLE);
                Toast.makeText(SeeRegisteredUser.this, "could not load your questions, please check your connection and try again", Toast.LENGTH_SHORT).show();
            }
        }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("boss_id", getId);
                return params;

                //see the name from php file and much them
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    public void goback(View view) {
        startActivity(new Intent(SeeRegisteredUser.this, MainActivity.class));
    }
}