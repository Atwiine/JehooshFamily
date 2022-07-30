package com.example.jehooshfamily.ui.ResponseSection;

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
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.jehooshfamily.R;
import com.example.jehooshfamily.ui.URLs.SessionManager;
import com.example.jehooshfamily.ui.URLs.Urls;
import com.github.ybq.android.spinkit.SpinKitView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class DetailsAnswersObjectives extends AppCompatActivity {

    TextView from;
    TextView date;
    TextView answer_employee;
    TextView answer_boss;
    TextView qn;
    TextView options1;
    TextView options2;
    TextView options3;
    TextView options4;
    TextView options5;
    TextView userid;
    String sent_opt1, sent_opt2,
            sent_opt3, sent_opt4, sent_opt5;
    EditText feedbackObjective;
    SessionManager sessionManager;
    String getId, names, question_id;
    Urls urls;
    SpinKitView spinKitView;
    Button sendobjectivefeedback;
    RelativeLayout correct_detss, wrong_detss;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_details_answers_objective);

        spinKitView = findViewById(R.id.spin_kit);
        correct_detss = findViewById(R.id.correct_detss);
        wrong_detss = findViewById(R.id.wrong_detss);
        sendobjectivefeedback = findViewById(R.id.sendobjectivefeedback);
        feedbackObjective = findViewById(R.id.feedbackObjective);
        spinKitView = findViewById(R.id.spin_kit);
        from = findViewById(R.id.ofrom);
        userid = findViewById(R.id.osent_user_id);
        date = findViewById(R.id.osent_date);
        answer_employee = findViewById(R.id.oanswer);
        answer_boss = findViewById(R.id.osent_correct_answer);
        options1 = findViewById(R.id.osent_options1);
        options2 = findViewById(R.id.osent_options2);
        options3 = findViewById(R.id.osent_options3);
        options4 = findViewById(R.id.osent_options4);
        options5 = findViewById(R.id.osent_options5);
        qn = findViewById(R.id.osent_qn);

//        receive data from the adapter
        from.setText(getIntent().getStringExtra("from"));
        userid.setText(getIntent().getStringExtra("user_id"));
        date.setText(getIntent().getStringExtra("date"));
        qn.setText(getIntent().getStringExtra("sent_question"));
        answer_employee.setText(getIntent().getStringExtra("answer_user"));
        answer_boss.setText(getIntent().getStringExtra("answer_boss"));
        options1.setText(getIntent().getStringExtra("options_a"));
        options2.setText(getIntent().getStringExtra("options_b"));
        options3.setText(getIntent().getStringExtra("options_c"));
        options4.setText(getIntent().getStringExtra("options_d"));
        options5.setText(getIntent().getStringExtra("options_e"));
        question_id = getIntent().getStringExtra("sent_qn_id");
        //check to see if any option is empty
        sent_opt1 = options1.getText().toString();
        sent_opt2 = options2.getText().toString();
        sent_opt3 = options3.getText().toString();
        sent_opt4 = options4.getText().toString();
        sent_opt5 = options5.getText().toString();

        if (!sent_opt1.isEmpty()) {
            options1.setVisibility(View.VISIBLE);
        }
        if (!sent_opt2.isEmpty()) {
            options2.setVisibility(View.VISIBLE);
        }
        if (!sent_opt3.isEmpty()) {
            options3.setVisibility(View.VISIBLE);
        }
        if (!sent_opt4.isEmpty()) {
            options4.setVisibility(View.VISIBLE);
        }
        if (!sent_opt5.isEmpty()) {
            options5.setVisibility(View.VISIBLE);
        }


        ///check if the user's answer is correct or wrong
        String ab = answer_boss.getText().toString();
        String au = answer_employee.getText().toString();

        if (ab.toLowerCase().equals(au.toLowerCase())) {
            correct_detss.setVisibility(View.VISIBLE);
        }
        if (!ab.toLowerCase().equals(au.toLowerCase())) {
            wrong_detss.setVisibility(View.VISIBLE);
        }


        urls = new Urls();
        sessionManager = new SessionManager(this);
        HashMap<String, String> user = sessionManager.getUserDetail();
        getId = user.get(SessionManager.ID);
        names = user.get(SessionManager.NAMES);
        updateAnswer();
    }

    private void updateAnswer() {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, urls.UPDATEREAD_OBJECTIVE,
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
                params.put("sent_qn_id", question_id);
                params.put("boss_id", getId);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    public void goback(View view) {
        startActivity(new Intent(DetailsAnswersObjectives.this, AnswerObjectives.class));
        finish();
    }

    public void sendFeedbackObjective(View view) {
        String checkFeedback = feedbackObjective.getText().toString().trim();
        if (!checkFeedback.isEmpty()) {
            AlertDialog.Builder alertDialog2 = new AlertDialog.Builder(
                    DetailsAnswersObjectives.this);
            alertDialog2.setTitle("Confirm to proceed");
            alertDialog2.setMessage("Please double check your feedback");
            alertDialog2.setIcon(R.drawable.ic_warning);
            alertDialog2.setPositiveButton("YES",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            sendFeedback();
                        }
                    });
            alertDialog2.setNegativeButton("NO",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
            alertDialog2.show();
        } else {
            Toast.makeText(this, "A feedback is required", Toast.LENGTH_SHORT).show();
        }
    }

    private void sendFeedback() {
        final ProgressDialog dialog = new ProgressDialog(DetailsAnswersObjectives.this);
        dialog.setMessage("Please wait...");
        dialog.show();

        final String sendFB = feedbackObjective.getText().toString().trim();
        final String o1 = options1.getText().toString();
        final String o2 = options2.getText().toString();
        final String o3 = options3.getText().toString();
        final String o4 = options4.getText().toString();
        final String o5 = options5.getText().toString();
        final String ans = answer_employee.getText().toString();
        final String uid = userid.getText().toString();
        final String uname = from.getText().toString();
        final String question = qn.getText().toString();


        StringRequest stringRequest = new StringRequest(Request.Method.POST, urls.SEND_FEEDBACK_OBJECTIVES,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            Log.i("tagconvertstr", "[" + response + "]");
                            JSONObject object = new JSONObject(response);
                            String success = object.getString("success");
                            if (success.equals("1")) {
                                dialog.dismiss();
                                sendobjectivefeedback.setVisibility(View.VISIBLE);
                                Toast.makeText(DetailsAnswersObjectives.this, "Feedback sent successfully", Toast.LENGTH_LONG).show();
                                feedbackObjective.getText().clear();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            dialog.dismiss();
                            sendobjectivefeedback.setVisibility(View.VISIBLE);
                            Toast.makeText(DetailsAnswersObjectives.this, "Feedback not sent successful, please try again "
                                    , Toast.LENGTH_LONG).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(DetailsAnswersObjectives.this, "Feedback not sent successful, please check your network and try again", Toast.LENGTH_LONG).show();
                dialog.dismiss();
                sendobjectivefeedback.setVisibility(View.VISIBLE);
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("question", question);
                params.put("options_a", o1);
                params.put("options_b", o2);
                params.put("options_c", o3);
                params.put("options_d", o4);
                params.put("options_e", o5);
                params.put("user_answer", ans);
                params.put("feedback", sendFB);
                params.put("user_id", uid);
                params.put("user_name", uname);
                params.put("boss_name", names);
                params.put("boss_id", getId);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);


    }
}