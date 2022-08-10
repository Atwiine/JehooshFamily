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
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
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
import com.example.jehooshfamily.ui.Adapters.ViewDrawQnsAdapter;
import com.example.jehooshfamily.ui.HistorySection.HistoryDrawing;
import com.example.jehooshfamily.ui.Main_Functions.AskActivity;
import com.example.jehooshfamily.ui.Models.DrawQns_Model;
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

public class DrawQns extends AppCompatActivity {
    EditText type_drwqn;
    Button send_draw;
    RecyclerView recyclerView_recent_drawqns;
    TextView no_drwqns;
    List<DrawQns_Model> mData;
    SessionManager sessionManager;
    Urls urls;
    ViewDrawQnsAdapter drawQnsAdapter;
    String getId, names;
    SpinKitView spinKitView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_draw_qns);


        type_drwqn = findViewById(R.id.type_drwqn);
        send_draw = findViewById(R.id.send_draw);
        recyclerView_recent_drawqns = findViewById(R.id.recyclerView_recent_drawqns);
        no_drwqns = findViewById(R.id.no_drwqn);
        spinKitView = findViewById(R.id.spin_kit);
        urls = new Urls();
        sessionManager = new SessionManager(this);
        HashMap<String, String> user = sessionManager.getUserDetail();
        getId = user.get(SessionManager.ID);
        names = user.get(SessionManager.NAMES);

        //handle recyclerview
        mData = new ArrayList<>();
        recyclerView_recent_drawqns.setLayoutManager(new LinearLayoutManager(this));
        drawQnsAdapter = new ViewDrawQnsAdapter(this, mData);
        recyclerView_recent_drawqns.setAdapter(drawQnsAdapter);

//        load recent essays
        loadRecentDrawingQns();


        send_draw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String check = type_drwqn.getText().toString().trim();
                if (check.equals("")) {
                    Toast.makeText(DrawQns.this, "Please enter your question", Toast.LENGTH_LONG).show();
                } else {
                    AlertDialog.Builder alertDialog2 = new AlertDialog.Builder(
                            DrawQns.this);
                    alertDialog2.setTitle("Confirm to proceed");
                    alertDialog2.setMessage("Make sure you double check your question before confirming");
                    alertDialog2.setIcon(R.drawable.ic_warning);
                    alertDialog2.setPositiveButton("YES",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    sendDrawQn();
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

    private void loadRecentDrawingQns() {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, urls.RECENT_DRAW_ADMIN,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            Log.i("tagconvertstr", "[" + response + "]");

                            JSONArray tips = new JSONArray(response);
                            if (tips.length() == 0) {
                                no_drwqns.setVisibility(View.VISIBLE);
                            } else {

                                for (int i = 0; i < tips.length(); i++) {
                                    Log.i("tagconvertstr", "[" + response + "]");

                                    JSONObject jsonObject = tips.getJSONObject(i);
                                    String id = jsonObject.getString("id").trim();
                                    String question = jsonObject.getString("question").trim();
                                    String date = jsonObject.getString("date_sent").trim();

                                    no_drwqns.setVisibility(View.GONE);
                                    DrawQns_Model markets_model = new DrawQns_Model(id, question, date);
                                    mData.add(markets_model);
                                }

                                drawQnsAdapter = new ViewDrawQnsAdapter(DrawQns.this, mData);
                                recyclerView_recent_drawqns.setAdapter(drawQnsAdapter);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            no_drwqns.setVisibility(View.VISIBLE);
                            Toast.makeText(DrawQns.this, "something went wrong, please try again" , Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                no_drwqns.setVisibility(View.VISIBLE);
                Toast.makeText(DrawQns.this, "something went wrong, please check your internet connection and try again", Toast.LENGTH_SHORT).show();
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

    /*clears the recyclerview once a message is sent*/
    public void Clear() {
        mData.clear();
        drawQnsAdapter.notifyDataSetChanged();
    }
    private void sendDrawQn() {

        final ProgressDialog dialog = new ProgressDialog(DrawQns.this);
        dialog.setMessage("Please wait...");

        dialog.show();
        final String essay = this.type_drwqn.getText().toString().trim();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, urls.SEND_DRAW_ADMIN,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            Log.i("tagconvertstr", "[" + response + "]");
                            JSONObject object = new JSONObject(response);
                            String success = object.getString("success");
                            if (success.equals("1")) {
                                dialog.dismiss();
                                Clear();
                                loadRecentDrawingQns();// reload the recent questions after submitting the new question
                                send_draw.setVisibility(View.VISIBLE);
                                Toast.makeText(DrawQns.this, "Question sent successfully", Toast.LENGTH_LONG).show();
                                type_drwqn.getText().clear();

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(DrawQns.this, "Question not sent successful, please try again " +e.toString(), Toast.LENGTH_LONG).show();
                            dialog.dismiss();
                            send_draw.setVisibility(View.VISIBLE);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(DrawQns.this, "Question not sent successful, please check your network and try again", Toast.LENGTH_LONG).show();
                dialog.dismiss();
                send_draw.setVisibility(View.VISIBLE);
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
        startActivity(new Intent(DrawQns.this, AskActivity.class));
    }

    public void viewHisDraw(View view) {
        startActivity(new Intent(DrawQns.this, HistoryDrawing.class));
    }
}