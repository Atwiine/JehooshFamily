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
import android.widget.ImageView;
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
import com.bumptech.glide.Glide;
import com.example.jehooshfamily.R;
import com.example.jehooshfamily.ui.URLs.SessionManager;
import com.example.jehooshfamily.ui.URLs.Urls;
import com.github.ybq.android.spinkit.SpinKitView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class DetailsAnswersDrawing extends AppCompatActivity {

    TextView from, date, uid, qn, qnid, imgname,explanation;
    EditText feedbackDraw;
    SessionManager sessionManager;
    String getId, names, question_id;
    Urls urls;
    ImageView answer;
    SpinKitView spinKitView;
    Button senddrawfeedback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_details_answers_drawing);

        senddrawfeedback = findViewById(R.id.senddrawfeedback);
        feedbackDraw = findViewById(R.id.feedbackDrawing);
        spinKitView = findViewById(R.id.spin_kit);
        from = findViewById(R.id.dfrom);
        date = findViewById(R.id.detdates_drawing);
        answer = findViewById(R.id.answerImage);
        qn = findViewById(R.id.dsent_qn);
        qnid = findViewById(R.id.dsent_qn_id);
        imgname = findViewById(R.id.detimagename_drawing);
        uid = findViewById(R.id.duserid);
        explanation = findViewById(R.id.explanation);

//        receive data from the adapter
        from.setText(getIntent().getStringExtra("from"));
        date.setText(getIntent().getStringExtra("date"));
        uid.setText(getIntent().getStringExtra("user_id"));
        qn.setText(getIntent().getStringExtra("sent_question"));
        qnid.setText(getIntent().getStringExtra("sent_qn_id"));
        imgname.setText(getIntent().getStringExtra("imagename"));
        explanation.setText(getIntent().getStringExtra("explanation"));
        String imgurl = getIntent().getStringExtra("photo");
        question_id = getIntent().getStringExtra("sent_qn_id");

        Glide.with(this)
                .load(imgurl)
                .placeholder(R.drawable.frutorials)
                .error(R.drawable.ic_terrain)
                .fallback(R.drawable.android_developer)
                .into(answer);

        urls = new Urls();
        sessionManager = new SessionManager(this);
        HashMap<String, String> user = sessionManager.getUserDetail();
        getId = user.get(SessionManager.ID);
        names = user.get(SessionManager.NAMES);
        updateAnswer();
    }

    public void goback(View view) {
        startActivity(new Intent(DetailsAnswersDrawing.this, AnswersToEssays.class));
    }

    private void updateAnswer() {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, urls.UPDATEREAD_DRAWING,
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

    public void sendFeedbackDrawing(View view) {
        String checkFeedback = feedbackDraw.getText().toString().trim();
        if (!checkFeedback.isEmpty()) {
            AlertDialog.Builder alertDialog2 = new AlertDialog.Builder(
                    DetailsAnswersDrawing.this);
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
            Toast.makeText(this, "Your feedback is required", Toast.LENGTH_SHORT).show();
        }
    }

    private void sendFeedback() {
        final ProgressDialog dialog = new ProgressDialog(DetailsAnswersDrawing.this);
        dialog.setMessage("Please wait...");
        dialog.show();
        final String sendFB = feedbackDraw.getText().toString().trim();
        final String ans = imgname.getText().toString();
        final String userid = uid.getText().toString();
        final String uname = from.getText().toString();
        final String question_id = qnid.getText().toString();
        final String question = qn.getText().toString();


        StringRequest stringRequest = new StringRequest(Request.Method.POST, urls.SEND_FEEDBACKDRAW,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            Log.i("tagconvertstr", "[" + response + "]");
                            JSONObject object = new JSONObject(response);
                            String success = object.getString("success");
                            if (success.equals("1")) {
                                dialog.dismiss();
                                senddrawfeedback.setVisibility(View.VISIBLE);
                                Toast.makeText(DetailsAnswersDrawing.this, "Feedback sent successfully", Toast.LENGTH_LONG).show();
                                feedbackDraw.getText().clear();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            dialog.dismiss();
                            senddrawfeedback.setVisibility(View.VISIBLE);
                            Toast.makeText(DetailsAnswersDrawing.this, "Feedback not sent successful, please try again "
                                    , Toast.LENGTH_LONG).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(DetailsAnswersDrawing.this, "Feedback not sent successful, please check your network and try again", Toast.LENGTH_LONG).show();
                dialog.dismiss();
                senddrawfeedback.setVisibility(View.VISIBLE);
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("question", question);
                params.put("user_answer", ans);
                params.put("feedback", sendFB);
                params.put("sent_qn_id", question_id);
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