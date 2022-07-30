package com.example.jehooshfamily.ui.ResponseSection;

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
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
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
import com.example.jehooshfamily.ui.Adapters.ViewResponseObjectivesAdapter;
import com.example.jehooshfamily.ui.Main_Functions.MyResponses;
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

public class ResponseQnObjective extends AppCompatActivity {

    Urls urls;
    RecyclerView recyclerView_obj_essay;
    TextView no_obj_essay;
    List<HistoryObjectives_Model> mData;
    SessionManager sessionManager;
    ViewResponseObjectivesAdapter objectivesAdapter;
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
        setContentView(R.layout.activity_response_objective);
        filteredit = findViewById(R.id.editFilterEssay);
        relaFilter = findViewById(R.id.relaFilter);

        spinKitView = findViewById(R.id.spin_kit);
        no_obj_essay = findViewById(R.id.no_objec_essay);
        urls = new Urls();

        sessionManager = new SessionManager(this);
        HashMap<String, String> user = sessionManager.getUserDetail();
        getId = user.get(SessionManager.ID);
        names = user.get(SessionManager.NAMES);

        //handle recyclerview
        recyclerView_obj_essay = findViewById(R.id.recyclerView_resp_objec);
        mData = new ArrayList<>();
        recyclerView_obj_essay.setLayoutManager(new LinearLayoutManager(this));
        objectivesAdapter = new ViewResponseObjectivesAdapter(this, mData);
        recyclerView_obj_essay.setAdapter(objectivesAdapter);
        getResponseObjectives();
        setFilteredit();
    }

//    @Override
//    public void onBackPressed() {
//        super.onBackPressed();
//        getResponseObjectives();
//    }

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

    private void getResponseObjectives() {
        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage("Loading...");
        dialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, urls.RESPONSE_OBJECTIVE_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            Log.i("tagconvertstr", "[" + response + "]");

                            JSONArray tips = new JSONArray(response);

                            if (tips.length() == 0) {
                                dialog.dismiss();
                                no_obj_essay.setVisibility(View.VISIBLE);
                            } else {
                                for (int i = 0; i < tips.length(); i++) {
                                    Log.i("tagconvertstr", "[" + response + "]");

                                    JSONObject marketsObjects = tips.getJSONObject(i);
                                    String id = marketsObjects.getString("id").trim();
                                    String question = marketsObjects.getString("question").trim();
                                    String options1 = marketsObjects.getString("options_a").trim();
                                    String options2 = marketsObjects.getString("options_b").trim();
                                    String options3 = marketsObjects.getString("options_c").trim();
                                    String options4 = marketsObjects.getString("options_d").trim();
                                    String options5 = marketsObjects.getString("options_e").trim();
                                    String set_answer = marketsObjects.getString("set_answer").trim();
                                    String date = marketsObjects.getString("date_sent").trim();

                                    HistoryObjectives_Model markets_model = new HistoryObjectives_Model
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

                                    no_obj_essay.setVisibility(View.GONE);

                                }
                                objectivesAdapter = new ViewResponseObjectivesAdapter(ResponseQnObjective.this, mData);
                                recyclerView_obj_essay.setAdapter(objectivesAdapter);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            dialog.dismiss();
                            no_obj_essay.setVisibility(View.VISIBLE);
                            Toast.makeText(ResponseQnObjective.this, " something went wrong, please try again", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                dialog.dismiss();
                no_obj_essay.setVisibility(View.VISIBLE);
                Toast.makeText(ResponseQnObjective.this, "could not load your questions, please check your connection and try again", Toast.LENGTH_LONG).show();
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
        startActivity(new Intent(ResponseQnObjective.this, MyResponses.class));

    }
}