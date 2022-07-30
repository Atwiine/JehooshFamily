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
import com.example.jehooshfamily.ui.Adapters.ViewResponseDrawingAdapter;
import com.example.jehooshfamily.ui.Main_Functions.MyResponses;
import com.example.jehooshfamily.ui.Models.HistoryDrawing_Model;
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

public class ResponseQnDrawing extends AppCompatActivity {

    Urls urls;
    RecyclerView recyclerView_resp_drawing;
    TextView no_resp_draw;
    List<HistoryDrawing_Model> mData;
    SessionManager sessionManager;
    ViewResponseDrawingAdapter drawingAdapter;
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
        setContentView(R.layout.activity_response_drawing);

        spinKitView = findViewById(R.id.spin_kit);
        filteredit = findViewById(R.id.editFilterEssay);
        relaFilter = findViewById(R.id.relaFilter);
        no_resp_draw = findViewById(R.id.no_resp_draw);
        urls = new Urls();
        sessionManager = new SessionManager(this);
        HashMap<String, String> user = sessionManager.getUserDetail();
        getId = user.get(SessionManager.ID);
        names = user.get(SessionManager.NAMES);

        //handle recyclerview
        recyclerView_resp_drawing = findViewById(R.id.recyclerView_resp_drawing);
        mData = new ArrayList<>();
        recyclerView_resp_drawing.setLayoutManager(new LinearLayoutManager(this));
        drawingAdapter = new ViewResponseDrawingAdapter(this, mData);
        recyclerView_resp_drawing.setAdapter(drawingAdapter);
//        drawingAdapter.notifyDatasetChange();
        getResponseDraw();
        setFilteredit();
    }

//    @Override
//    public void onBackPressed() {
//        super.onBackPressed();
//        getResponseDraw();
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
        ArrayList<HistoryDrawing_Model> filteredList = new ArrayList<>();
        for (HistoryDrawing_Model item : mData) {
            if (item.getDate().toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(item);
            }
        }

        drawingAdapter.filterList(filteredList);
    }

    private void getResponseDraw() {
        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage("Loading...");
        dialog.show();

        if(dialog.isShowing()){
            dialog.dismiss();
        }


        StringRequest stringRequest = new StringRequest(Request.Method.POST, urls.RESPONSE_DRAW_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            Log.i("tagconvertstr", "[" + response + "]");

                            JSONArray tips = new JSONArray(response);
                            if (tips.length() == 0) {
                                dialog.dismiss();
                                no_resp_draw.setVisibility(View.VISIBLE);
                            } else {
                                for (int i = 0; i < tips.length(); i++) {
                                    Log.i("tagconvertstr", "[" + response + "]");

                                    JSONObject marketsObjects = tips.getJSONObject(i);
                                    String id = marketsObjects.getString("id").trim();
                                    String question = marketsObjects.getString("question").trim();
                                    String date = marketsObjects.getString("date_sent").trim();

                                    HistoryDrawing_Model markets_model = new HistoryDrawing_Model(id, question, date);
                                    mData.add(markets_model);
                                    dialog.dismiss();
                                    no_resp_draw.setVisibility(View.GONE);

                                }
                                drawingAdapter = new ViewResponseDrawingAdapter(ResponseQnDrawing.this, mData);
                                recyclerView_resp_drawing.setAdapter(drawingAdapter);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            dialog.dismiss();
                            no_resp_draw.setVisibility(View.VISIBLE);
                            Toast.makeText(ResponseQnDrawing.this, " something went wrong, please try again", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                dialog.dismiss();
                no_resp_draw.setVisibility(View.VISIBLE);
                Toast.makeText(ResponseQnDrawing.this, "could not load your questions, please check your connection and try again", Toast.LENGTH_LONG).show();
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
        startActivity(new Intent(ResponseQnDrawing.this, MyResponses.class));

    }
}