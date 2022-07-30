package com.example.jehooshfamily.ui.UserSection;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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

public class AnswerUserVoting extends AppCompatActivity {
    Urls urls;
    String getIdempl, namesemplo, boss_id,
            boss_name, sent_qn, sent_qnid, sent_opt1, sent_opt2,
            sent_opt3, sent_opt4, sent_opt5;
    TextView qn_id, qn, qn_date;
    SessionManagerLogin sessionManager;
    RadioGroup groupVotOpt;
    RadioButton op1, op2, op3, op4, op5;
    String voteanswer, userid;
    SpinKitView spinKitView;
    Button emp_votbnt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_answer_user_voting);

        spinKitView = findViewById(R.id.spin_kit);

        emp_votbnt = findViewById(R.id.emp_votbnt);
        qn_date = findViewById(R.id.empvot_date);
        op1 = findViewById(R.id.em_optvot_a);
        op2 = findViewById(R.id.em_optvot_b);
        op3 = findViewById(R.id.em_optvot_c);
        op4 = findViewById(R.id.em_optvot_d);
        op5 = findViewById(R.id.em_optvot_e);
        qn_id = findViewById(R.id.empvot_id);
        qn = findViewById(R.id.emplvot_to_answer_qn);
        groupVotOpt = findViewById(R.id.group_empvot_opts);

// attaching the pasted on extras
        qn_date.setText(getIntent().getStringExtra("date"));
        qn_id.setText(getIntent().getStringExtra("id"));
        qn.setText(getIntent().getStringExtra("sent_question"));
        op1.setText(getIntent().getStringExtra("options_a"));
        op2.setText(getIntent().getStringExtra("options_b"));
        op3.setText(getIntent().getStringExtra("options_c"));
        op4.setText(getIntent().getStringExtra("options_d"));
        op5.setText(getIntent().getStringExtra("options_e"));

        //check to see if the options contain data
        sent_qn = qn.getText().toString();
        sent_qnid = qn_id.getText().toString();
        sent_opt1 = op1.getText().toString();
        sent_opt2 = op2.getText().toString();
        sent_opt3 = op3.getText().toString();
        sent_opt4 = op4.getText().toString();
        sent_opt5 = op5.getText().toString();
        if (!sent_opt1.isEmpty()) {
            op1.setVisibility(View.VISIBLE);
        }
        if (!sent_opt2.isEmpty()) {
            op2.setVisibility(View.VISIBLE);
        }
        if (!sent_opt3.isEmpty()) {
            op3.setVisibility(View.VISIBLE);
        }
        if (!sent_opt4.isEmpty()) {
            op4.setVisibility(View.VISIBLE);
        }
        if (!sent_opt5.isEmpty()) {
            op5.setVisibility(View.VISIBLE);
        }
        urls = new Urls();


        sessionManager = new SessionManagerLogin(this);
        HashMap<String, String> user = sessionManager.getUserDetail();
        getIdempl = user.get(SessionManagerLogin.ID);
        namesemplo = user.get(SessionManagerLogin.NAMES);
        boss_id = user.get(SessionManagerLogin.BOSS_ID);
        boss_name = user.get(SessionManagerLogin.BOSS_NAME);

    }

    @SuppressLint("NonConstantResourceId")
    public void checkRadioButtonClickedVote(View view) {
        // is the button checked
        boolean checked = ((RadioButton) view).isChecked();

        //check which radio btn was checked
        switch (view.getId()) {
            case R.id.em_optvot_a:
                if (checked) {
                    voteanswer = sent_opt1;
                }
                break;

            case R.id.em_optvot_b:
                if (checked) {
                    voteanswer = sent_opt2;
                }
                break;
            case R.id.em_optvot_c:
                if (checked) {
                    voteanswer = sent_opt3;
                }
                break;

            case R.id.em_optvot_d:
                if (checked) {
                    voteanswer = sent_opt4;
                }
                break;

            case R.id.em_optvot_e:
                if (checked) {
                    voteanswer = sent_opt5;
                }
                break;

        }
    }


    public void sendObjectives(View view) {
        if (groupVotOpt.getCheckedRadioButtonId() == -1) {
            Toast.makeText(this, "please select an answer before sending", Toast.LENGTH_SHORT).show();

        } else {
            AlertDialog.Builder alertDialog2 = new AlertDialog.Builder(
                    AnswerUserVoting.this);
            alertDialog2.setTitle("Confirm to proceed");
            alertDialog2.setMessage("Make sure you double check your answer before confirming");
            alertDialog2.setIcon(R.drawable.ic_warning);
            alertDialog2.setPositiveButton("YES",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            sendVoteAnswer();
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

    private void sendVoteAnswer() {
        final ProgressDialog dialog = new ProgressDialog(AnswerUserVoting.this);
        dialog.setMessage("Please wait...");
dialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, urls.SEND_VOTING_EMPLOY,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            Log.i("tagconvertstr", "[" + response + "]");
                            JSONObject object = new JSONObject(response);
                            String success = object.getString("success");
                            if (success.equals("1")) {
                                dialog.dismiss();
                                emp_votbnt.setVisibility(View.VISIBLE);
                                Toast.makeText(AnswerUserVoting.this, "answer sent successfully", Toast.LENGTH_LONG).show();
                                op1.setChecked(false);
                                op2.setChecked(false);
                                op3.setChecked(false);
                                op4.setChecked(false);
                                op5.setChecked(false);
                                Intent ds = new Intent(AnswerUserVoting.this,UserVote.class);
                                startActivity(ds);
                                finish();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(AnswerUserVoting.this, " answer not sent successful, please try again", Toast.LENGTH_LONG).show();
                            dialog.dismiss();
                            emp_votbnt.setVisibility(View.VISIBLE);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(AnswerUserVoting.this, "answer not sent successful, please check your network and try again", Toast.LENGTH_LONG).show();
                dialog.dismiss();
                emp_votbnt.setVisibility(View.VISIBLE);
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("answer_employee", voteanswer);
                params.put("from_who", namesemplo);
                params.put("user_id", getIdempl);
                params.put("question_sent", sent_qn);
                params.put("sent_qn_id", sent_qnid);
                params.put("options_a", sent_opt1);
                params.put("options_b", sent_opt2);
                params.put("options_c", sent_opt3);
                params.put("options_d", sent_opt4);
                params.put("options_e", sent_opt5);
                params.put("boss_name", boss_name);
                params.put("boss_id", boss_id);

                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }


    public void goback(View view) {
        startActivity(new Intent(AnswerUserVoting.this, UserVote.class));
    }
}