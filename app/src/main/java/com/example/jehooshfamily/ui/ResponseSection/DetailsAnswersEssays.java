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

public class DetailsAnswersEssays extends AppCompatActivity {

    TextView from, date, answer, uid, qn;
    EditText feedbackEssay;
    SessionManager sessionManager;
    String getId, names, question_id;
    Urls urls;
    SpinKitView spinKitView;
    Button sendessayfeedback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_details_answers_essays);

        sendessayfeedback = findViewById(R.id.sendessayfeedback);
        feedbackEssay = findViewById(R.id.feedbackEssay);
        spinKitView = findViewById(R.id.spin_kit);
        from = findViewById(R.id.efrom);
        date = findViewById(R.id.detdates_essay);
        answer = findViewById(R.id.eanswer);
        qn = findViewById(R.id.esent_qn);
        uid = findViewById(R.id.euserid);

//        receive data from the adapter
        from.setText(getIntent().getStringExtra("from"));
        date.setText(getIntent().getStringExtra("date"));
        uid.setText(getIntent().getStringExtra("user_id"));
        qn.setText(getIntent().getStringExtra("sent_question"));
        answer.setText(getIntent().getStringExtra("answer"));
        question_id = getIntent().getStringExtra("sent_qn_id");
        urls = new Urls();
        sessionManager = new SessionManager(this);
        HashMap<String, String> user = sessionManager.getUserDetail();
        getId = user.get(SessionManager.ID);
        names = user.get(SessionManager.NAMES);
        updateAnswer();
    }

    private void updateAnswer() {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, urls.UPDATEREAD_ESSAY,
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
        startActivity(new Intent(DetailsAnswersEssays.this, AnswersToEssays.class));
    }

    public void sendFeedbackEssay(View view) {
        String checkFeedback = feedbackEssay.getText().toString().trim();
        if (!checkFeedback.isEmpty()) {
            AlertDialog.Builder alertDialog2 = new AlertDialog.Builder(
                    DetailsAnswersEssays.this);
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
        final ProgressDialog dialog = new ProgressDialog(DetailsAnswersEssays.this);
        dialog.setMessage("Please wait...");
        dialog.show();

        final String sendFB = feedbackEssay.getText().toString().trim();
        final String ans = answer.getText().toString();
        final String userid = uid.getText().toString();
        final String uname = from.getText().toString();
        final String question = qn.getText().toString();


        StringRequest stringRequest = new StringRequest(Request.Method.POST, urls.SEND_FEEDBACKESSAY,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            Log.i("tagconvertstr", "[" + response + "]");
                            JSONObject object = new JSONObject(response);
                            String success = object.getString("success");
                            if (success.equals("1")) {
                                dialog.dismiss();
                                sendessayfeedback.setVisibility(View.VISIBLE);
                                Toast.makeText(DetailsAnswersEssays.this, "Feedback sent successfully", Toast.LENGTH_LONG).show();
                                feedbackEssay.getText().clear();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            dialog.dismiss();
                            sendessayfeedback.setVisibility(View.VISIBLE);
                            Toast.makeText(DetailsAnswersEssays.this, "Feedback not sent successful, please try again "
                                    , Toast.LENGTH_LONG).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(DetailsAnswersEssays.this, "Feedback not sent successful, please check your network and try again", Toast.LENGTH_LONG).show();
                dialog.dismiss();
                sendessayfeedback.setVisibility(View.VISIBLE);
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("question", question);
                params.put("user_answer", ans);
                params.put("feedback", sendFB);
                params.put("user_id", userid);
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