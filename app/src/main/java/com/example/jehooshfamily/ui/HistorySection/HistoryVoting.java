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
import com.example.jehooshfamily.ui.Adapters.ViewHistoryVotingAdapter;
import com.example.jehooshfamily.ui.Main_Functions.MyHistory;
import com.example.jehooshfamily.ui.Models.HistoryVoting_Model;
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

public class HistoryVoting extends AppCompatActivity {

    Urls urls;
    RecyclerView recyclerView_hist_voting;
    CardView no_hist_votes;
    List<HistoryVoting_Model> mData;
    SessionManager sessionManager;
    ViewHistoryVotingAdapter votingAdapter;
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
        setContentView(R.layout.activity_history_voting);

        spinKitView = findViewById(R.id.spin_kit);
        filteredit = findViewById(R.id.editFilterEssay);
        relaFilter = findViewById(R.id.relaFilter);

        no_hist_votes = findViewById(R.id.no_his_voting);
        urls = new Urls();
        sessionManager = new SessionManager(this);
        HashMap<String, String> user = sessionManager.getUserDetail();
        getId = user.get(SessionManager.ID);
        names = user.get(SessionManager.NAMES);

        //handle recyclerview
        recyclerView_hist_voting = findViewById(R.id.recyclerView_hist_voting);
        mData = new ArrayList<>();
        recyclerView_hist_voting.setLayoutManager(new LinearLayoutManager(this));
        votingAdapter = new ViewHistoryVotingAdapter(this, mData);
        recyclerView_hist_voting.setAdapter(votingAdapter);
        getHistoryVoting();
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
        ArrayList<HistoryVoting_Model> filteredList = new ArrayList<>();
        for (HistoryVoting_Model item : mData) {
            if (item.getQuestion().toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(item);
            }
        }

        votingAdapter.filterList(filteredList);
    }

    private void getHistoryVoting() {
        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage("Loading...");
dialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, urls.HIST_VOTING_ADMIN,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            Log.i("tagconvertstr", "[" + response + "]");

                            JSONArray tips = new JSONArray(response);
                            if (tips.length() == 0) {
                                dialog.dismiss();
                                no_hist_votes.setVisibility(View.VISIBLE);
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
                                    String date = jsonObject.getString("date_sent").trim();


                                    HistoryVoting_Model markets_model = new HistoryVoting_Model
                                            (id, question, options1, options2, options3, options4, options5, date);
                                    mData.add(markets_model);
                                    dialog.dismiss();
                                    no_hist_votes.setVisibility(View.GONE);

                                }

                                votingAdapter = new ViewHistoryVotingAdapter(HistoryVoting.this, mData);
                                recyclerView_hist_voting.setAdapter(votingAdapter);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            dialog.dismiss();
                            no_hist_votes.setVisibility(View.VISIBLE);
                            Toast.makeText(HistoryVoting.this, "  something went wrong, please try again", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                dialog.dismiss();
                no_hist_votes.setVisibility(View.VISIBLE);
                Toast.makeText(HistoryVoting.this, "could not load your questions, please check your connection and try again", Toast.LENGTH_LONG).show();
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
        startActivity(new Intent(HistoryVoting.this, MyHistory.class));
    }
}