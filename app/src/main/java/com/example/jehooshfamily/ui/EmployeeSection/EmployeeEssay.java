package com.example.jehooshfamily.ui.EmployeeSection;

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
import com.example.jehooshfamily.ui.Adapters.ViewEmployeeEssayAdapter;
import com.example.jehooshfamily.ui.Models.HistoryEssay_Model;
import com.example.jehooshfamily.ui.URLs.SessionManagerLogin;
import com.example.jehooshfamily.ui.URLs.Urls;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EmployeeEssay extends AppCompatActivity {

    //    public static String EMPLOYEE_ESSAY_URL = "http://172.16.1.142/bossApp/history_essay.php";
    Urls urls;
    RecyclerView recyclerView_emplo_essay;
    CardView no_hist_essay;
    List<HistoryEssay_Model> mData;
    SessionManagerLogin sessionManager;
    ViewEmployeeEssayAdapter employeeAdapter;
    String getId, names;
    EditText filteredit;
    RelativeLayout relaFilter;
    private DatePickerDialog.OnDateSetListener mDateSetListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_employee_essay);

        filteredit = findViewById(R.id.editFilterEssay);
        relaFilter = findViewById(R.id.relaFilter);

        no_hist_essay = findViewById(R.id.no_emplo_essay);
        urls = new Urls();
        sessionManager = new SessionManagerLogin(this);
        HashMap<String, String> user = sessionManager.getUserDetail();
        getId = user.get(SessionManagerLogin.BOSS_ID);
        names = user.get(SessionManagerLogin.BOSS_NAME);

        //handle recyclerview
        recyclerView_emplo_essay = findViewById(R.id.recyclerView_emplo_essay);
        mData = new ArrayList<>();
//        mData.add(new HistoryEssay_Model("1", "why are there no reports about last weeks' purchases", "12/02/2021"));
        recyclerView_emplo_essay.setLayoutManager(new LinearLayoutManager(this));
        employeeAdapter = new ViewEmployeeEssayAdapter(this, mData);
        recyclerView_emplo_essay.setAdapter(employeeAdapter);
        getEmployeeEssay();
        setFilteredit();


//        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
//            @Override
//            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
//                month = month + 1;
//                String date = day + "/" + month + "/" + year;
//                filteredit.setText(date);
//            }
//        };
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
                            employeeAdapter = new ViewEmployeeEssayAdapter(EmployeeEssay.this, mData);
                            recyclerView_emplo_essay.setAdapter(employeeAdapter);

                        } catch (JSONException e) {
                            e.printStackTrace();
                            dialog.dismiss();
                            no_hist_essay.setVisibility(View.VISIBLE);
                            Toast.makeText(EmployeeEssay.this, " error" + e.toString(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                dialog.dismiss();
                no_hist_essay.setVisibility(View.VISIBLE);
//                "could not load your questions, please check your connection and try again"
                Toast.makeText(EmployeeEssay.this, error.toString(), Toast.LENGTH_LONG).show();
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
        startActivity(new Intent(EmployeeEssay.this, TypesQns.class));
    }

    public void openEditFilter(View view) {
        filteredit.setVisibility(View.VISIBLE);
    }

 /*   @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void openCalenderFilterEssay(View view) {

        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog dialog = new DatePickerDialog(
                EmployeeEssay.this,
                android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                mDateSetListener,
                year, month, day);
        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }
*/

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
            if (item.getDate().toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(item);
            }
        }

        employeeAdapter.filterList(filteredList);
    }
}