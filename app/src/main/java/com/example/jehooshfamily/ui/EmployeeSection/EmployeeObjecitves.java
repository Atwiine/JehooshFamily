package com.example.jehooshfamily.ui.EmployeeSection;

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
import com.example.jehooshfamily.ui.Adapters.ViewEmployeeObjectivesAdapter;
import com.example.jehooshfamily.ui.Models.HistoryObjectives_Model;
import com.example.jehooshfamily.ui.URLs.SessionManagerLogin;
import com.example.jehooshfamily.ui.URLs.Urls;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EmployeeObjecitves extends AppCompatActivity {

    //    public static String EMPLOYEE_OBJECTIVES_URL = "http://172.16.1.142/bossApp/history_objectives.php";
    Urls urls;
    RecyclerView recyclerView_emplo_objectives;
    CardView no_hist_objectives;
    List<HistoryObjectives_Model> mData;
    SessionManagerLogin sessionManager;
    ViewEmployeeObjectivesAdapter objectivesAdapter;
    String getId, names;
    EditText filteredit;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_employee_objecitves);

        filteredit = findViewById(R.id.editFilterObjective);

        no_hist_objectives = findViewById(R.id.no_emplo_objective);
        urls = new Urls();
        sessionManager = new SessionManagerLogin(this);
        HashMap<String, String> user = sessionManager.getUserDetail();
        getId = user.get(SessionManagerLogin.BOSS_ID);
        names = user.get(SessionManagerLogin.BOSS_NAME);

        //handle recyclerview
        recyclerView_emplo_objectives = findViewById(R.id.recyclerView_emplo_objective);
        mData = new ArrayList<>();
//        mData.add(new HistoryObjectives_Model("2", "which of the following seems right to you", "A - Fish on a stick, B - Cat in boots, C - Dog in car, D - Monkey cooking", "D", "11/02/2021"));
        recyclerView_emplo_objectives.setLayoutManager(new LinearLayoutManager(this));
        objectivesAdapter = new ViewEmployeeObjectivesAdapter(this, mData);
        recyclerView_emplo_objectives.setAdapter(objectivesAdapter);
        getEmployeeObjectives();
        setFilteredit();

    }

    private void getEmployeeObjectives() {
        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage("Loading...");
        dialog.show();


        StringRequest stringRequest = new StringRequest(Request.Method.POST, urls.EMPLOYEE_OBJECTIVES_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            Log.i("tagconvertstr", "[" + response + "]");

                            JSONArray tips = new JSONArray(response);

                            for (int i = 0; i < tips.length(); i++) {
                                Log.i("tagconvertstr", "[" + response + "]");
                                JSONObject jsonObject = tips.getJSONObject(i);
                                String id = jsonObject.getString("id").trim();
                                String question = jsonObject.getString("question").trim();
                                String o_a = jsonObject.getString("options_a").trim();
                                String o_b = jsonObject.getString("options_b").trim();
                                String o_c = jsonObject.getString("options_c").trim();
                                String o_d = jsonObject.getString("options_d").trim();
                                String o_e = jsonObject.getString("options_e").trim();
                                String set_answer = jsonObject.getString("set_answer").trim();
                                String date = jsonObject.getString("date_sent").trim();

                                HistoryObjectives_Model markets_model = new HistoryObjectives_Model
                                        (id, question, o_a, o_b, o_c, o_d, o_e, set_answer, date);
                                mData.add(markets_model);
                                dialog.dismiss();
                                no_hist_objectives.setVisibility(View.GONE);

                            }
                            objectivesAdapter = new ViewEmployeeObjectivesAdapter(EmployeeObjecitves.this, mData);
                            recyclerView_emplo_objectives.setAdapter(objectivesAdapter);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            dialog.dismiss();
                            no_hist_objectives.setVisibility(View.VISIBLE);
                            Toast.makeText(EmployeeObjecitves.this, " error" + e.toString(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                dialog.dismiss();
                no_hist_objectives.setVisibility(View.VISIBLE);
//                "could not load your question, please check your connection and try again"
                Toast.makeText(EmployeeObjecitves.this, error.toString(), Toast.LENGTH_LONG).show();
            }
        }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("boss_id", getId);
                return params;

            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    public void goback(View view) {
        startActivity(new Intent(EmployeeObjecitves.this, TypesQns.class));
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
        ArrayList<HistoryObjectives_Model> filteredList = new ArrayList<>();
        for (HistoryObjectives_Model item : mData) {
            if (item.getDate().toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(item);
            }
        }

        objectivesAdapter.filterList(filteredList);
    }
}