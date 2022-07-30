package com.example.jehooshfamily.ui.AskSection;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
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
import com.example.jehooshfamily.ui.Adapters.ViewEssayQnsAdapter;
import com.example.jehooshfamily.ui.HistorySection.HistoryEssay;
import com.example.jehooshfamily.ui.Main_Functions.AskActivity;
import com.example.jehooshfamily.ui.Models.EssayQns_Model;
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


public class Essay extends AppCompatActivity {

    EditText type_essay;
    Button send_essay;
    RecyclerView recyclerView_essay;
    CardView no_essay;
    List<EssayQns_Model> mData;
    SessionManager sessionManager;
    Urls urls;
    ViewEssayQnsAdapter essayQnsAdapter;
    String getId, names;
    SpinKitView spinKitView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_essay);

        spinKitView = findViewById(R.id.spin_kit);
        type_essay = findViewById(R.id.essay_input);
        send_essay = findViewById(R.id.send_essay);
        recyclerView_essay = findViewById(R.id.recyclerView_recent_essays);
        no_essay = findViewById(R.id.no_essays);

        urls = new Urls();
        sessionManager = new SessionManager(this);
        HashMap<String, String> user = sessionManager.getUserDetail();
        getId = user.get(SessionManager.ID);
        names = user.get(SessionManager.NAMES);

        //handle recyclerview
        mData = new ArrayList<>();
        recyclerView_essay.setLayoutManager(new LinearLayoutManager(this));
        essayQnsAdapter = new ViewEssayQnsAdapter(this, mData);
        recyclerView_essay.setAdapter(essayQnsAdapter);

//        load recent essays
        loadRecentEssay();


        send_essay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String check = type_essay.getText().toString().trim();
                if (check.equals("")) {
                    Toast.makeText(Essay.this, "Please enter your essay", Toast.LENGTH_LONG).show();
                } else {
                    AlertDialog.Builder alertDialog2 = new AlertDialog.Builder(
                            Essay.this);
                    alertDialog2.setTitle("Confirm to proceed");
                    alertDialog2.setMessage("Make sure you double check your question before confirming");
                    alertDialog2.setIcon(R.drawable.ic_warning);
                    alertDialog2.setPositiveButton("YES",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    sendEssay();
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
            }
        });
    }

    private void loadRecentEssay() {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, urls.RECENT_ESSAY_ADMIN,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            Log.i("tagconvertstr", "[" + response + "]");

                            JSONArray tips = new JSONArray(response);
                            if (tips.length() == 0) {
                                no_essay.setVisibility(View.VISIBLE);
                            } else {

                                for (int i = 0; i < tips.length(); i++) {
                                    Log.i("tagconvertstr", "[" + response + "]");

                                    JSONObject jsonObject = tips.getJSONObject(i);
                                    String id = jsonObject.getString("id").trim();
                                    String question = jsonObject.getString("question").trim();
                                    String date = jsonObject.getString("date_sent").trim();

                                    no_essay.setVisibility(View.GONE);
                                    EssayQns_Model markets_model = new EssayQns_Model(id, question, date);
                                    mData.add(markets_model);
                                }

                                essayQnsAdapter = new ViewEssayQnsAdapter(Essay.this, mData);
                                recyclerView_essay.setAdapter(essayQnsAdapter);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            no_essay.setVisibility(View.VISIBLE);
                            Toast.makeText(Essay.this, "something went wrong, please try again", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                no_essay.setVisibility(View.VISIBLE);
                Toast.makeText(Essay.this, "something went wrong, please check your internet connection and try again", Toast.LENGTH_SHORT).show();
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


    /* send  essay */

    private void sendEssay() {

        final ProgressDialog dialog = new ProgressDialog(Essay.this);
        dialog.setMessage("Please wait...");
        dialog.show();
//        Animation main_layout = AnimationUtils.loadAnimation(Essay.this, R.anim.anim);
//        send_essay.setVisibility(View.GONE);
//        send_essay.setAnimation(main_layout);
//
//        spinKitView.setVisibility(View.VISIBLE);
        final String essay = this.type_essay.getText().toString().trim();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, urls.SEND_ESSAY_ADMIN,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            Log.i("tagconvertstr", "[" + response + "]");
                            JSONObject object = new JSONObject(response);
                            String success = object.getString("success");
                            if (success.equals("1")) {
                                dialog.dismiss();
                                loadRecentEssay();//should reload the recent questions part
//                                spinKitView.setVisibility(View.GONE);
                                send_essay.setVisibility(View.VISIBLE);
                                Toast.makeText(Essay.this, "Question sent successfully", Toast.LENGTH_LONG).show();
                                type_essay.getText().clear();

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
//
                            Toast.makeText(Essay.this, "Question not sent successful, please try again ", Toast.LENGTH_LONG).show();
                            dialog.dismiss();
//                            spinKitView.setVisibility(View.GONE);
                            send_essay.setVisibility(View.VISIBLE);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
//
                Toast.makeText(Essay.this, "Question not sent successful, please check your network and try again", Toast.LENGTH_LONG).show();
                dialog.dismiss();
//                spinKitView.setVisibility(View.GONE);
                send_essay.setVisibility(View.VISIBLE);
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("question", essay);
                params.put("boss_name", names);
                params.put("boss_id", getId);

                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    public void goback(View view) {
        startActivity(new Intent(Essay.this, AskActivity.class));
        finish();
    }

    public void viewHisEssay(View view) {
        startActivity(new Intent(Essay.this, HistoryEssay.class));
    }
}