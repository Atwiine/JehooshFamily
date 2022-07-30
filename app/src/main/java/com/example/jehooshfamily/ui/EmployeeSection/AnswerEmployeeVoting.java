package com.example.jehooshfamily.ui.EmployeeSection;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class AnswerEmployeeVoting extends AppCompatActivity {
    //    public static String SEND_VOTING_URL = "http://172.16.0.35/bossApp/send_answer_emp_votes.php";
//    public static String SEND_VOTING_URL = "http://172.16.0.35/bossApp/send_answer_emp_votes.php";
    Urls urls;

    String getIdempl, namesemplo, boss_id,
            boss_name, sent_qn, sent_qnid, sent_opt1, sent_opt2,
            sent_opt3, sent_opt4, sent_opt5;
    TextView qn_id, qn, qn_date;
    SessionManagerLogin sessionManager;
    //    EditText answer;
    RadioGroup groupVotOpt;
    RadioButton op1, op2, op3, op4, op5;
    String voteanswer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_answer_employee_voting);

//        answer = findViewById(R.id.answer_emp_vote);
        //part for handling the sent question from the response adapter
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




//        answer_employee from_who,question_sent,answer_boss,options,boss_name
        sessionManager = new SessionManagerLogin(this);
        HashMap<String, String> user = sessionManager.getUserDetail();
        getIdempl = user.get(SessionManagerLogin.ID); //employee's id
        namesemplo = user.get(SessionManagerLogin.NAMES); // employee's names getIdempl =  user.get(SessionManagerLogin.ID); //employee's id
        boss_id = user.get(SessionManagerLogin.BOSS_ID); // boss's names getIdempl =  user.get(SessionManagerLogin.ID); //employee's id
        boss_name = user.get(SessionManagerLogin.BOSS_NAME); // boss's names

//        Toast.makeText(this, boss_id, Toast.LENGTH_SHORT).show();


    }

    public void checkRadioButtonClickedVote(View view) {
        // is the button checked
        boolean checked = ((RadioButton) view).isChecked();

        //check which radio btn was checked
        switch (view.getId()) {
            case R.id.em_optvot_a:
                if (checked) {
                    voteanswer = sent_opt1;
                    Toast.makeText(this, "checked" + voteanswer, Toast.LENGTH_SHORT).show();
                }
                break;

            case R.id.em_optvot_b:
                if (checked) {
                    voteanswer = sent_opt2;
                    Toast.makeText(this, "checked" + voteanswer, Toast.LENGTH_SHORT).show();

                }
                break;
            case R.id.em_optvot_c:
                if (checked) {
                    voteanswer = sent_opt3;
                    Toast.makeText(this, "checked" + voteanswer, Toast.LENGTH_SHORT).show();

                }
                break;

            case R.id.em_optvot_d:
                if (checked) {
                    voteanswer = sent_opt4;
                    Toast.makeText(this, "checked" + voteanswer, Toast.LENGTH_SHORT).show();

                }
                break;

            case R.id.em_optvot_e:
                if (checked) {
                    voteanswer = sent_opt5;
                    Toast.makeText(this, "checked" + voteanswer, Toast.LENGTH_SHORT).show();

                }
                break;

        }
    }


    public void sendObjectives(View view) {
        if (groupVotOpt.getCheckedRadioButtonId() == -1) {
            Toast.makeText(this, "please select an answer before sending", Toast.LENGTH_SHORT).show();

        } else {
            AlertDialog.Builder alertDialog2 = new AlertDialog.Builder(
                    AnswerEmployeeVoting.this);
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
        final ProgressDialog dialog = new ProgressDialog(AnswerEmployeeVoting.this);
        dialog.setMessage("Please wait...");
        dialog.show();

//        final String voteanswer = this.op1.getText().toString() +
//                this.op2.getText().toString() + this.op3.getText().toString() +
//                this.op4.getText().toString() + this.op5.getText().toString();

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
                                Toast.makeText(AnswerEmployeeVoting.this, "answer sent successfully", Toast.LENGTH_LONG).show();
                                op1.setChecked(false);
                                op2.setChecked(false);
                                op3.setChecked(false);
                                op4.setChecked(false);
                                op5.setChecked(false);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
//                            answer not sent successful, please try again
                            Toast.makeText(AnswerEmployeeVoting.this, "errss " + e.toString(), Toast.LENGTH_LONG).show();
                            dialog.dismiss();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
//                "answer not sent successful, please check your network and try again"
                Toast.makeText(AnswerEmployeeVoting.this, error.toString(), Toast.LENGTH_LONG).show();
                dialog.dismiss();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("answer_employee", voteanswer);
                params.put("from_who", namesemplo);
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
        startActivity(new Intent(AnswerEmployeeVoting.this, EmployeeVote.class));
    }
}