package com.example.jehooshfamily.ui.UserSection;

import android.app.DatePickerDialog;
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
import android.widget.LinearLayout;
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
import com.example.jehooshfamily.ui.Adapters.ViewUserEssayAdapter;
import com.example.jehooshfamily.ui.Models.HistoryEssay_Model;
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

public class UserEssay extends AppCompatActivity {

    Urls urls;
    RecyclerView recyclerView_emplo_essay;
    CardView no_hist_essay;
    List<HistoryEssay_Model> mData;
    SessionManagerLogin sessionManager;
    ViewUserEssayAdapter employeeAdapter;
    String getId, names, userid;
    EditText filteredit;
    RelativeLayout relaFilter;
    private DatePickerDialog.OnDateSetListener mDateSetListener;
    LinearLayout feedback_essay_open;
    SpinKitView spinKitView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_user_essay);

        filteredit = findViewById(R.id.editFilterEssay);
        relaFilter = findViewById(R.id.relaFilter);

        spinKitView = findViewById(R.id.spin_kit);

        no_hist_essay = findViewById(R.id.no_emplo_essay);
        urls = new Urls();
        sessionManager = new SessionManagerLogin(this);
        HashMap<String, String> user = sessionManager.getUserDetail();
        getId = user.get(SessionManagerLogin.BOSS_ID);
        names = user.get(SessionManagerLogin.BOSS_NAME);
        userid = user.get(SessionManagerLogin.ID);

        //handle recyclerview
        recyclerView_emplo_essay = findViewById(R.id.recyclerView_emplo_essay);
        mData = new ArrayList<>();
        recyclerView_emplo_essay.setLayoutManager(new LinearLayoutManager(this));
        employeeAdapter = new ViewUserEssayAdapter(this, mData);
        recyclerView_emplo_essay.setAdapter(employeeAdapter);
        getEmployeeEssay();
        setFilteredit();

//        checkForNewFeedBack();
    }

    //count new feedbacks
    private void checkForNewFeedBack() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, urls.NEWFEEBACK_ESSAY,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            Log.i("tagconvertstr", "[" + response + "]");
                            JSONArray tips = new JSONArray(response);

                            for (int i = 0; i < tips.length(); i++) {
                                JSONObject marketsObjects = tips.getJSONObject(i);

                                String cart = marketsObjects.getString("cartNo");

//                                feedback_newnumber.setVisibility(View.VISIBLE);
//                                feedback_newnumber.setText(cart);

                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(UserEssay.this, " sorry couldn't load number of new feedback", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(UserEssay.this, " sorry couldn't load number of new feedback, please check your connection and try again", Toast.LENGTH_LONG).show();
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

    private void getEmployeeEssay() {
        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage("Loading...");
        dialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, urls.EMPLOYEE_ESSAY_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            Log.i("tagconvertstr", "[" + response + "]");

                            JSONArray tips = new JSONArray(response);
                            if (tips.length() == 0) {
                                dialog.dismiss();
                                no_hist_essay.setVisibility(View.VISIBLE);
                            } else {

                                for (int i = 0; i < tips.length(); i++) {
                                    Log.i("tagconvertstr", "[" + response + "]");
                                    JSONObject jsonObject = tips.getJSONObject(i);
                                    String id = jsonObject.getString("id").trim();
                                    String question = jsonObject.getString("question").trim();
                                    String date = jsonObject.getString("date_sent").trim();

                                    HistoryEssay_Model markets_model = new HistoryEssay_Model(id, question, date);
                                    mData.add(markets_model);
                                    dialog.dismiss();
                                    no_hist_essay.setVisibility(View.GONE);

                                }
                                employeeAdapter = new ViewUserEssayAdapter(UserEssay.this, mData);
                                recyclerView_emplo_essay.setAdapter(employeeAdapter);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            dialog.dismiss();
                            no_hist_essay.setVisibility(View.VISIBLE);
                            Toast.makeText(UserEssay.this, " something went wrong, please try again", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                dialog.dismiss();
                no_hist_essay.setVisibility(View.VISIBLE);
                Toast.makeText(UserEssay.this, "could not load your questions, please check your connection and try again", Toast.LENGTH_LONG).show();
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
        startActivity(new Intent(UserEssay.this, TypesQns.class));
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
        ArrayList<HistoryEssay_Model> filteredList = new ArrayList<>();
        for (HistoryEssay_Model item : mData) {
            if (item.getQuestion().toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(item);
            }
        }

        employeeAdapter.filterList(filteredList);
    }

    public void openFeedBackEssay(View view) {
        String ofb = "Essay Feedback";
        String ofasb = "1";
        Intent ssas = new Intent(UserEssay.this, FeedBack.class);
        ssas.putExtra("holder", ofb);
        ssas.putExtra("check", ofasb);
        startActivity(ssas);
    }


}