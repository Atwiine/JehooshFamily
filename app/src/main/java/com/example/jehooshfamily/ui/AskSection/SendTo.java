package com.example.jehooshfamily.ui.AskSection;

import android.os.Bundle;
import android.view.View;
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
import com.example.jehooshfamily.ui.Adapters.ViewSendToAdapter;
import com.example.jehooshfamily.ui.Models.SentTo_Model;
import com.example.jehooshfamily.ui.URLs.SessionManager;
import com.example.jehooshfamily.ui.URLs.Urls;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SendTo extends AppCompatActivity {

//    public static String SEE_EMPLOYEE_URL = "http://172.16.0.35/bossApp/get_registered_employees.php";
    RecyclerView recyclerView_sendto;
    CardView no_employee;
    List<SentTo_Model> mData;
    SessionManager sessionManager;
    ViewSendToAdapter sendToAdapter;
    String getId, namess;
    Urls urls;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_to);


        no_employee = findViewById(R.id.no_sends_to);


        sessionManager = new SessionManager(this);
        HashMap<String, String> user = sessionManager.getUserDetail();
        getId = user.get(SessionManager.ID);
        namess = user.get(SessionManager.NAMES);
        urls = new Urls();
        //handle recyclerview
        recyclerView_sendto = findViewById(R.id.recyclerView_sendto);
        mData = new ArrayList<>();
        recyclerView_sendto.setLayoutManager(new LinearLayoutManager(this));
        sendToAdapter = new ViewSendToAdapter(this, mData);
        recyclerView_sendto.setAdapter(sendToAdapter);

        loadEmployee();

    }


    private void loadEmployee() {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, urls.SEE_EMPLOYEE_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray tips = new JSONArray(response);

                            for (int i = 0; i < tips.length(); i++) {
                                JSONObject marketsObjects = tips.getJSONObject(i);

                                String name = marketsObjects.getString("name");
                                String phone = marketsObjects.getString("phone");
                                String role = marketsObjects.getString("role");
                                String boss_name = marketsObjects.getString("boss_name");
                                String date_register = marketsObjects.getString("date_register");
                                String id = marketsObjects.getString("id");

                                no_employee.setVisibility(View.GONE);
                                SentTo_Model sentToModel =
                                        new SentTo_Model(id, name, phone, role, boss_name, date_register);
                                mData.add(sentToModel);
                            }

                            sendToAdapter = new ViewSendToAdapter(SendTo.this, mData);
                            recyclerView_sendto.setAdapter(sendToAdapter);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                no_employee.setVisibility(View.VISIBLE);
                Toast.makeText(SendTo.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("boss_id", getId);
                return params;

                //see the name from php file and much them
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }


    public void FinalizeSendingProcess(View view) {

        Toast.makeText(this, "still have things to figure out", Toast.LENGTH_SHORT).show();

    }
}