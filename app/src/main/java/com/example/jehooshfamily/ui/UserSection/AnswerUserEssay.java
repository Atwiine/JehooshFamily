package com.example.jehooshfamily.ui.UserSection;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
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
import com.example.jehooshfamily.ui.URLs.SessionManagerLogin;
import com.example.jehooshfamily.ui.URLs.Urls;
import com.github.ybq.android.spinkit.SpinKitView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class AnswerUserEssay extends AppCompatActivity {
    Urls urls;
    String getIdempl, namesemplo, boss_id, boss_name, sent_qn, sent_qnid;
    TextView qn_id, qn, qn_date;
    SessionManagerLogin sessionManager;
    EditText answer;
    SpinKitView spinKitView;
    Button emp_essaybnt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_answer_user_essay);

        spinKitView = findViewById(R.id.spin_kit);
        emp_essaybnt = findViewById(R.id.emp_essaybnt);
        answer = findViewById(R.id.answer_emp_essay);

        //part for handling the sent question from the response adapter
        qn_date = findViewById(R.id.resp_to_answer_date_essay);
        qn_id = findViewById(R.id.resp_to_answer_id_essay);
        qn = findViewById(R.id.resp_to_answer_qn_essay);

// attaching the pasted on extras
        qn_date.setText(getIntent().getStringExtra("date"));
        qn_id.setText(getIntent().getStringExtra("id"));
        qn.setText(getIntent().getStringExtra("sent_question"));

        sent_qn = qn.getText().toString();
        sent_qnid = qn_id.getText().toString();

        urls = new Urls();
        sessionManager = new SessionManagerLogin(this);
        HashMap<String, String> user = sessionManager.getUserDetail();
        getIdempl = user.get(SessionManagerLogin.ID);
        namesemplo = user.get(SessionManagerLogin.NAMES);
        boss_id = user.get(SessionManagerLogin.BOSS_ID);
        boss_name = user.get(SessionManagerLogin.BOSS_NAME);


    }

    public void sendEssay(View view) {
        String check = answer.getText().toString().trim();
        if (!check.equals("")) {
            AlertDialog.Builder alertDialog2 = new AlertDialog.Builder(
                    AnswerUserEssay.this);
            alertDialog2.setTitle("Confirm to proceed");
            alertDialog2.setMessage("Make sure you double check your answer before confirming");
            alertDialog2.setIcon(R.drawable.ic_warning);
            alertDialog2.setPositiveButton("YES",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            sendEssayAnswer();
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
            answer.setError("your answer is required please");
        }

    }

    private void sendEssayAnswer() {
        final ProgressDialog dialog = new ProgressDialog(AnswerUserEssay.this);
        dialog.setMessage("Please wait...");
        Animation main_layout = AnimationUtils.loadAnimation(AnswerUserEssay.this, R.anim.anim);
        emp_essaybnt.setVisibility(View.GONE);
        emp_essaybnt.setAnimation(main_layout);
        spinKitView.setVisibility(View.VISIBLE);

        final String essayanswer = this.answer.getText().toString();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, urls.SEND_ESSAY_EMPLOY,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            Log.i("tagconvertstr", "[" + response + "]");
                            JSONObject object = new JSONObject(response);
                            String success = object.getString("success");
                            if (success.equals("1")) {
                                dialog.dismiss();
                                spinKitView.setVisibility(View.GONE);
                                emp_essaybnt.setVisibility(View.VISIBLE);
                                Toast.makeText(AnswerUserEssay.this, "answer sent successfully", Toast.LENGTH_LONG).show();
                                answer.getText().clear();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(AnswerUserEssay.this, "answer not sent successful, please try again ", Toast.LENGTH_LONG).show();
                            dialog.dismiss();
                            spinKitView.setVisibility(View.GONE);
                            emp_essaybnt.setVisibility(View.VISIBLE);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(AnswerUserEssay.this, "answer not sent successful, please check your network and try again", Toast.LENGTH_LONG).show();
                dialog.dismiss();
                spinKitView.setVisibility(View.GONE);
                emp_essaybnt.setVisibility(View.VISIBLE);
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("answer", essayanswer);
                params.put("user_id", getIdempl);
                params.put("from_who", namesemplo);
                params.put("sent_question", sent_qn);
                params.put("sent_qn_id", sent_qnid);
                params.put("boss_id", boss_id);
                params.put("boss_name", boss_name);

                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    public void goback(View view) {
        startActivity(new Intent(AnswerUserEssay.this, UserEssay.class));
    }

}
