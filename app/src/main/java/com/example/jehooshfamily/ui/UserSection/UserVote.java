package com.example.jehooshfamily.ui.UserSection;

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
import com.example.jehooshfamily.ui.Adapters.ViewUserVotesAdapter;
import com.example.jehooshfamily.ui.Models.HistoryVoting_Model;
import com.example.jehooshfamily.ui.URLs.SessionManagerLogin;
import com.example.jehooshfamily.ui.URLs.Urls;
import com.github.ybq.android.spinkit.SpinKitView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserVote extends AppCompatActivity {

    Urls urls;
    RecyclerView recyclerView_emplo_vote;
    CardView no_hist_objectives;
    List<HistoryVoting_Model> mData;
    SessionManagerLogin sessionManager;
    ViewUserVotesAdapter votesAdapter;
    String getId, names, userid;
    EditText filteredit;
    SpinKitView spinKitView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_user_vote);

        filteredit = findViewById(R.id.editFilterVote);
        spinKitView = findViewById(R.id.spin_kit);

        no_hist_objectives = findViewById(R.id.no_emplo_votes);
        urls = new Urls();
        sessionManager = new SessionManagerLogin(this);
        HashMap<String, String> user = sessionManager.getUserDetail();
        getId = user.get(SessionManagerLogin.BOSS_ID);
        names = user.get(SessionManagerLogin.BOSS_NAME);
        userid = user.get(SessionManagerLogin.ID);

        //handle recyclerview
        recyclerView_emplo_vote = findViewById(R.id.recyclerView_emplo_votes);
        mData = new ArrayList<>();
        recyclerView_emplo_vote.setLayoutManager(new LinearLayoutManager(this));
        votesAdapter = new ViewUserVotesAdapter(this, mData);
        recyclerView_emplo_vote.setAdapter(votesAdapter);
        getEmployeeVotes();
        setFilteredit();

    }

    private void getEmployeeVotes() {
        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage("Loading...");
dialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, urls.EMPLOYEE_VOTES_URL,
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
                                    String o_a = jsonObject.getString("options_a").trim();
                                    String o_b = jsonObject.getString("options_b").trim();
                                    String o_c = jsonObject.getString("options_c").trim();
                                    String o_d = jsonObject.getString("options_d").trim();
                                    String o_e = jsonObject.getString("options_e").trim();
                                    String date = jsonObject.getString("date_sent").trim();

                                    HistoryVoting_Model markets_model = new HistoryVoting_Model
                                            (id, question, o_a, o_b, o_c, o_d, o_e, date);
                                    mData.add(markets_model);
                                    dialog.dismiss();
                                    no_hist_objectives.setVisibility(View.GONE);

                                }
                                votesAdapter = new ViewUserVotesAdapter(UserVote.this, mData);
                                recyclerView_emplo_vote.setAdapter(votesAdapter);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            dialog.dismiss();
                            no_hist_objectives.setVisibility(View.VISIBLE);
                            Toast.makeText(UserVote.this, "something went wrong, please try again", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                dialog.dismiss();
                no_hist_objectives.setVisibility(View.VISIBLE);
                Toast.makeText(UserVote.this, "could not load your questions, please check your connection and try again", Toast.LENGTH_LONG).show();
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
        startActivity(new Intent(UserVote.this, TypesQns.class));
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

        votesAdapter.filterList(filteredList);
    }

}