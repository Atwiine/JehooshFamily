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
import android.widget.ImageView;
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
import com.example.jehooshfamily.ui.Adapters.ViewFeedbackAdapter;
import com.example.jehooshfamily.ui.Adapters.ViewFeedbackDrawingAdapter;
import com.example.jehooshfamily.ui.Adapters.ViewFeedbackEssayAdapter;
import com.example.jehooshfamily.ui.Models.FeedbackDrawing_Model;
import com.example.jehooshfamily.ui.Models.FeedbackEssay_Model;
import com.example.jehooshfamily.ui.Models.Feedback_Model;
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

public class FeedBack extends AppCompatActivity {
    TextView whichfeedback, checkfd;
    Urls urls;
    RecyclerView recyclerView_user_feedback;
    TextView no_feedbacks;
    List<Feedback_Model> mData;
    List<FeedbackDrawing_Model> mDataDrawing;
    List<FeedbackEssay_Model> mDataEssay;
    SessionManagerLogin sessionManager;
    ViewFeedbackAdapter feedbackAdapter;
    ViewFeedbackEssayAdapter feedbackEssayAdapter;
    ViewFeedbackDrawingAdapter drawingAdapter;
    String getId, names, userid;
    EditText filteredit;
    SpinKitView spinKitView;
    ImageView back_img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_feed_backs);

        back_img = findViewById(R.id.back_img);
        spinKitView = findViewById(R.id.spin_kit);
        whichfeedback = findViewById(R.id.whichfeedback);
        checkfd = findViewById(R.id.checkfd);
        no_feedbacks = findViewById(R.id.no_feedbacks);
        urls = new Urls();
        sessionManager = new SessionManagerLogin(this);
        HashMap<String, String> user = sessionManager.getUserDetail();
        getId = user.get(SessionManagerLogin.BOSS_ID);
        names = user.get(SessionManagerLogin.BOSS_NAME);
        userid = user.get(SessionManagerLogin.ID);


        //check which intent was fired and then act accordingly
        whichfeedback.setText(getIntent().getStringExtra("holder"));
        checkfd.setText(getIntent().getStringExtra("check"));

        if (checkfd.getText().toString().equals("1")) {
            whichfeedback.setText("Essay Feedback");
            fetchEssayFeedback();
            updateEssayFeedback();
        }
        if (checkfd.getText().toString().equals("2")) {
            whichfeedback.setText("Objectives Feedback");
            fetchObjectiveFeedback();
            updateObjectiveFeedback();
        }

        if (checkfd.getText().toString().equals("3")) {
            whichfeedback.setText("Drawing Feedback");
            fetchDrawingFeedback();
            updateDrawingFeedback();
        }

        back_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkfd.getText().toString().equals("1")) {
                 startActivity(new Intent(FeedBack.this,UserEssay.class));
                 finish();
                }
                if (checkfd.getText().toString().equals("2")) {
                    startActivity(new Intent(FeedBack.this,UserObjecitves.class));
                    finish();
                }

                if (checkfd.getText().toString().equals("3")) {
                    startActivity(new Intent(FeedBack.this,UserDraw.class));
                    finish();
                }
            }
        });
        //handle recyclerview
        recyclerView_user_feedback = findViewById(R.id.recyclerView_user_feedback);
        mData = new ArrayList<>();
        mDataEssay = new ArrayList<>();
        mDataDrawing = new ArrayList<>();
        recyclerView_user_feedback.setLayoutManager(new LinearLayoutManager(this));
        feedbackAdapter = new ViewFeedbackAdapter(this, mData);
        feedbackEssayAdapter = new ViewFeedbackEssayAdapter(this, mDataEssay);
        drawingAdapter = new ViewFeedbackDrawingAdapter(this, mDataDrawing);
        recyclerView_user_feedback.setAdapter(feedbackAdapter);
        recyclerView_user_feedback.setAdapter(feedbackEssayAdapter);
        recyclerView_user_feedback.setAdapter(drawingAdapter);

    }

    private void updateDrawingFeedback() {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, urls.UPDATE_FEEDBACKDRAW,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            Log.i("tagconvertstr", "[" + response + "]");
                            JSONObject object = new JSONObject(response);
                            String success = object.getString("success");
                            if (success.equals("1")) {
                            } else {
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("user_id", userid);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void updateEssayFeedback() {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, urls.UPDATE_FEEDBACKESSAY,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            Log.i("tagconvertstr", "[" + response + "]");
                            JSONObject object = new JSONObject(response);
                            String success = object.getString("success");
                            if (success.equals("1")) {
                            } else {
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("user_id", userid);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void updateObjectiveFeedback() {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, urls.UPDATE_FEEDBACK_OBJECTIVES,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            Log.i("tagconvertstr", "[" + response + "]");
                            JSONObject object = new JSONObject(response);
                            String success = object.getString("success");
                            if (success.equals("1")) {
                            } else {
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("user_id", userid);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void fetchDrawingFeedback() {
        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage("Loading...");
        dialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, urls.FETCHFEEBACK_DRAW,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            Log.i("tagconvertstr", "[" + response + "]");

                            JSONArray tips = new JSONArray(response);
                            if (tips.length() == 0) {
                                dialog.dismiss();
                                no_feedbacks.setVisibility(View.VISIBLE);
                            } else {

                                for (int i = 0; i < tips.length(); i++) {
                                    Log.i("tagconvertstr", "[" + response + "]");
                                    JSONObject jsonObject = tips.getJSONObject(i);
                                    String id = jsonObject.getString("id").trim();
                                    String question = jsonObject.getString("question").trim();
                                    String feedback = jsonObject.getString("feedback").trim();
                                    String user_answer = jsonObject.getString("user_answer").trim();
                                    String user_name = jsonObject.getString("user_name").trim();
                                    String user_id = jsonObject.getString("user_id").trim();
                                    String date = jsonObject.getString("date").trim();

                                    FeedbackDrawing_Model markets_model = new FeedbackDrawing_Model
                                            (id,
                                                    question,
                                                    feedback,
                                                    user_answer,
                                                    user_name,
                                                    user_id,
                                                    date);
                                    mDataDrawing.add(markets_model);
                                    dialog.dismiss();
                                    no_feedbacks.setVisibility(View.GONE);

                                }
                                drawingAdapter = new ViewFeedbackDrawingAdapter(FeedBack.this, mDataDrawing);
                                recyclerView_user_feedback.setAdapter(drawingAdapter);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            dialog.dismiss();
                            no_feedbacks.setVisibility(View.VISIBLE);
                            Toast.makeText(FeedBack.this, "something went wrong, please try again", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                dialog.dismiss();
                no_feedbacks.setVisibility(View.VISIBLE);
                Toast.makeText(FeedBack.this, "could not load your questions, please check your connection and try again", Toast.LENGTH_LONG).show();
            }
        }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("user_id", userid);
                return params;

            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void fetchEssayFeedback() {
        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage("Loading...");
        dialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, urls.FETCHFEEBACK_ESSAY,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            Log.i("tagconvertstr", "[" + response + "]");

                            JSONArray tips = new JSONArray(response);
                            if (tips.length() == 0) {
                                dialog.dismiss();
                                no_feedbacks.setVisibility(View.VISIBLE);
                            } else {

                                for (int i = 0; i < tips.length(); i++) {
                                    Log.i("tagconvertstr", "[" + response + "]");
                                    JSONObject jsonObject = tips.getJSONObject(i);
                                    String id = jsonObject.getString("id").trim();
                                    String question = jsonObject.getString("question").trim();
                                    String feedback = jsonObject.getString("feedback").trim();
                                    String user_answer = jsonObject.getString("user_answer").trim();
                                    String user_name = jsonObject.getString("user_name").trim();
                                    String user_id = jsonObject.getString("user_id").trim();
                                    String date = jsonObject.getString("date").trim();

                                    FeedbackEssay_Model markets_model = new FeedbackEssay_Model
                                            (id,
                                                    question,
                                                    feedback,
                                                    user_answer,
                                                    user_name,
                                                    user_id,
                                                    date);
                                    mDataEssay.add(markets_model);
                                    dialog.dismiss();
                                    no_feedbacks.setVisibility(View.GONE);

                                }
                                feedbackEssayAdapter = new ViewFeedbackEssayAdapter(FeedBack.this, mDataEssay);
                                recyclerView_user_feedback.setAdapter(feedbackEssayAdapter);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            dialog.dismiss();
                            no_feedbacks.setVisibility(View.VISIBLE);
                            Toast.makeText(FeedBack.this, " something went wrong, please try again", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                dialog.dismiss();
                no_feedbacks.setVisibility(View.VISIBLE);
                Toast.makeText(FeedBack.this, "could not load your questions, please check your connection and try again", Toast.LENGTH_LONG).show();
            }
        }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("user_id", userid);
                return params;

            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void fetchObjectiveFeedback() {
        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage("Loading...");
        dialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, urls.FETCHFEEBACK_OBJECITVES,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            Log.i("tagconvertstr", "[" + response + "]");

                            JSONArray tips = new JSONArray(response);
                            if (tips.length() == 0) {
                                dialog.dismiss();
                                no_feedbacks.setVisibility(View.VISIBLE);
                            } else {

                                for (int i = 0; i < tips.length(); i++) {
                                    Log.i("tagconvertstr", "[" + response + "]");
                                    JSONObject jsonObject = tips.getJSONObject(i);
                                    String id = jsonObject.getString("id").trim();
                                    String question = jsonObject.getString("question").trim();
                                    String feedback = jsonObject.getString("feedback").trim();
                                    String o_a = jsonObject.getString("options_a").trim();
                                    String o_b = jsonObject.getString("options_b").trim();
                                    String o_c = jsonObject.getString("options_c").trim();
                                    String o_d = jsonObject.getString("options_d").trim();
                                    String o_e = jsonObject.getString("options_e").trim();
                                    String user_answer = jsonObject.getString("user_answer").trim();
                                    String user_name = jsonObject.getString("user_name").trim();
                                    String user_id = jsonObject.getString("user_id").trim();
                                    String date = jsonObject.getString("date").trim();

                                    Feedback_Model markets_model = new Feedback_Model
                                            (id,
                                                    question,
                                                    feedback,
                                                    o_a,
                                                    o_b,
                                                    o_c,
                                                    o_d,
                                                    o_e,
                                                    user_answer,
                                                    user_name,
                                                    user_id,
                                                    date);
                                    mData.add(markets_model);
                                    dialog.dismiss();
                                    no_feedbacks.setVisibility(View.GONE);

                                }
                                feedbackAdapter = new ViewFeedbackAdapter(FeedBack.this, mData);
                                recyclerView_user_feedback.setAdapter(feedbackAdapter);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            dialog.dismiss();
                            no_feedbacks.setVisibility(View.VISIBLE);
                            Toast.makeText(FeedBack.this, " something went wrong, please try again", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                dialog.dismiss();
                no_feedbacks.setVisibility(View.VISIBLE);
                Toast.makeText(FeedBack.this, "could not load your questions, please check your connection and try again", Toast.LENGTH_LONG).show();
            }
        }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("user_id", userid);
                return params;

            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

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
        ArrayList<Feedback_Model> filteredList = new ArrayList<>();
        for (Feedback_Model item : mData) {
            if (item.getQuestion().toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(item);
            }
        }

        feedbackAdapter.filterList(filteredList);
    }


    public void goback(View view) {
    }
}