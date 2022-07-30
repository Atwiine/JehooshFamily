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
import com.example.jehooshfamily.ui.Adapters.ViewResponseVotingAdapter;
import com.example.jehooshfamily.ui.Main_Functions.MyResponses;
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

public class ResponseQnVotes extends AppCompatActivity {

    Urls urls;
    RecyclerView recyclerView_vote_essay;
    TextView no_vote_essay;
    List<HistoryVoting_Model> mData;
    SessionManager sessionManager;
    ViewResponseVotingAdapter votingAdapter;
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
        setContentView(R.layout.activity_response_votes);

        spinKitView = findViewById(R.id.spin_kit);
        no_vote_essay = findViewById(R.id.no_vote_essay);
        urls = new Urls();
        filteredit = findViewById(R.id.editFilterEssay);
        relaFilter = findViewById(R.id.relaFilter);
        sessionManager = new SessionManager(this);
        HashMap<String, String> user = sessionManager.getUserDetail();
        getId = user.get(SessionManager.ID);
        names = user.get(SessionManager.NAMES);

        //handle recyclerview
        recyclerView_vote_essay = findViewById(R.id.recyclerView_resp_voting);
        mData = new ArrayList<>();
        recyclerView_vote_essay.setLayoutManager(new LinearLayoutManager(this));
        votingAdapter = new ViewResponseVotingAdapter(this, mData);
        recyclerView_vote_essay.setAdapter(votingAdapter);
        getResponseVotes();
        setFilteredit();
    }

//    @Override
//    public void onBackPressed() {
//        super.onBackPressed();
//        getResponseVotes();
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
        ArrayList<HistoryVoting_Model> filteredList = new ArrayList<>();
        for (HistoryVoting_Model item : mData) {
            if (item.getDate().toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(item);
            }
        }

        votingAdapter.filterList(filteredList);
    }

    private void getResponseVotes() {
        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage("Loading...");
dialog.show();

//        spinKitView.setVisibility(View.VISIBLE);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, urls.RESPONSE_VOTE_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            Log.i("tagconvertstr", "[" + response + "]");

                            JSONArray tips = new JSONArray(response);

                            if (tips.length() == 0) {
                                dialog.dismiss();
                                no_vote_essay.setVisibility(View.VISIBLE);
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
                                    String date = marketsObjects.getString("date_sent").trim();

                                    HistoryVoting_Model markets_model = new HistoryVoting_Model
                                            (id,
                                                    question,
                                                    options1,
                                                    options2,
                                                    options3,
                                                    options4,
                                                    options5,
                                                    date);
                                    mData.add(markets_model);
                                    dialog.dismiss();


                                }
                                votingAdapter = new ViewResponseVotingAdapter(ResponseQnVotes.this, mData);
                                recyclerView_vote_essay.setAdapter(votingAdapter);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            dialog.dismiss();

                            no_vote_essay.setVisibility(View.VISIBLE);
                            Toast.makeText(ResponseQnVotes.this, " something went wrong, please try again", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                dialog.dismiss();

                no_vote_essay.setVisibility(View.VISIBLE);
                Toast.makeText(ResponseQnVotes.this, "could not load your questions, please check your connection and try again", Toast.LENGTH_LONG).show();
            }
        }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
//                params.put("boss_name", names);
                params.put("boss_id", getId);
                return params;

            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    public void goback(View view) {
        startActivity(new Intent(ResponseQnVotes.this, MyResponses.class));

    }
}