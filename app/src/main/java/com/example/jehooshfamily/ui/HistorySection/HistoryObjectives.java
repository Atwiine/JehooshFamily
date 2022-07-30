package com.example.jehooshfamily.ui.HistorySection;

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
import com.example.jehooshfamily.ui.Adapters.ViewHistoryObjectivesAdapter;
import com.example.jehooshfamily.ui.Main_Functions.MyHistory;
import com.example.jehooshfamily.ui.Models.HistoryObjectives_Model;
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

public class HistoryObjectives extends AppCompatActivity {

    //    public static String HISTORY_OBJECTIVES_URL = "http://172.16.2.232/bossApp/history_objectives.php";
    Urls urls;
    RecyclerView recyclerView_hist_objectives;
    CardView no_hist_objectives;
    List<HistoryObjectives_Model> mData;
    SessionManager sessionManager;
    ViewHistoryObjectivesAdapter objectivesAdapter;
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
        setContentView(R.layout.activity_history_objectives);

        spinKitView = findViewById(R.id.spin_kit);
        filteredit = findViewById(R.id.editFilterEssay);
        relaFilter = findViewById(R.id.relaFilter);
        no_hist_objectives = findViewById(R.id.no_his_objective);
        urls = new Urls();
        sessionManager = new SessionManager(this);
        HashMap<String, String> user = sessionManager.getUserDetail();
        getId = user.get(SessionManager.ID);
        names = user.get(SessionManager.NAMES);

        //handle recyclerview
        recyclerView_hist_objectives = findViewById(R.id.recyclerView_hist_objective);
        mData = new ArrayList<>();
        recyclerView_hist_objectives.setLayoutManager(new LinearLayoutManager(this));
        objectivesAdapter = new ViewHistoryObjectivesAdapter(this, mData);
        recyclerView_hist_objectives.setAdapter(objectivesAdapter);
        getHistoryObjectives();
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
        ArrayList<HistoryObjectives_Model> filteredList = new ArrayList<>();
        for (HistoryObjectives_Model item : mData) {
            if (item.getDate().toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(item);
            }
        }

        objectivesAdapter.filterList(filteredList);
    }

    private void getHistoryObjectives() {
        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage("Loading...");
dialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, urls.HIST_OBJECTIVE_ADMIN,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            Log.i("tagconvertstr", "[" + response + "]");

                            JSONArray tips = new JSONArray(response);
                            if (tips.length() == 0) {
                                dialog.dismiss();
                                no_hist_objectives.setVisibility(View.VISIBLE);
                            } else {

                                for (int i = 0; i < tips.length(); i++) {
                                    Log.i("tagconvertstr", "[" + response + "]");
                                    JSONObject jsonObject = tips.getJSONObject(i);
                                    String id = jsonObject.getString("id").trim();
                                    String question = jsonObject.getString("question").trim();
                                    String options1 = jsonObject.getString("options_a").trim();
                                    String options2 = jsonObject.getString("options_b").trim();
                                    String options3 = jsonObject.getString("options_c").trim();
                                    String options4 = jsonObject.getString("options_d").trim();
                                    String options5 = jsonObject.getString("options_e").trim();
                                    String set_answer = jsonObject.getString("set_answer").trim();
                                    String date = jsonObject.getString("date_sent").trim();


                                    HistoryObjectives_Model markets_model
                                            = new HistoryObjectives_Model
                                            (id,
                                                    question,
                                                    options1,
                                                    options2,
                                                    options3,
                                                    options4,
                                                    options5,
                                                    set_answer,
                                                    date);
                                    mData.add(markets_model);
                                    dialog.dismiss();
                                    spinKitView.setVisibility(View.GONE);
                                    no_hist_objectives.setVisibility(View.GONE);

                                }

                                objectivesAdapter = new ViewHistoryObjectivesAdapter(HistoryObjectives.this, mData);
                                recyclerView_hist_objectives.setAdapter(objectivesAdapter);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            dialog.dismiss();
                            spinKitView.setVisibility(View.GONE);
                            no_hist_objectives.setVisibility(View.VISIBLE);
                            Toast.makeText(HistoryObjectives.this, "  something went wrong, please try again", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                dialog.dismiss();
                spinKitView.setVisibility(View.GONE);
                no_hist_objectives.setVisibility(View.VISIBLE);
                Toast.makeText(HistoryObjectives.this, "could not load your profile, please check your connection and try again", Toast.LENGTH_LONG).show();
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
        startActivity(new Intent(HistoryObjectives.this, MyHistory.class));
    }
}