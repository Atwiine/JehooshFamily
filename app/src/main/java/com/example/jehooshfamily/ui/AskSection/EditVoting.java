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
import com.example.jehooshfamily.ui.HistorySection.HistoryVoting;
import com.example.jehooshfamily.ui.Main_Functions.AskActivity;
import com.example.jehooshfamily.ui.URLs.SessionManager;
import com.example.jehooshfamily.ui.URLs.Urls;
import com.github.ybq.android.spinkit.SpinKitView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class EditVoting extends AppCompatActivity {
    Urls urls;
    Button sendVote;
    EditText qn_vote, op1, op2, op3, op4, op5;
    SessionManager sessionManager;
    String getId, names;
    SpinKitView spinKitView;
    TextView idd,obj_vot;
    RelativeLayout item_find_donor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_voting);

        spinKitView = findViewById(R.id.spin_kit);
        sendVote = findViewById(R.id.send_voting);
        qn_vote = findViewById(R.id.input_voting);
        op1 = findViewById(R.id.voting_option_field1);
        op2 = findViewById(R.id.voting_option_field2);
        op3 = findViewById(R.id.voting_option_field3);
        op4 = findViewById(R.id.voting_option_field4);
        op5 = findViewById(R.id.voting_option_field5);
        idd = findViewById(R.id.idd);
        obj_vot = findViewById(R.id.obj_vot);
        item_find_donor = findViewById(R.id.item_find_donor);

        urls = new Urls();
        sessionManager = new SessionManager(this);
        HashMap<String, String> user = sessionManager.getUserDetail();
        getId = user.get(SessionManager.ID);
        names = user.get(SessionManager.NAMES);

        op1.setText(getIntent().getStringExtra("op_a"));
        op2.setText(getIntent().getStringExtra("op_b"));
        op3.setText(getIntent().getStringExtra("op_c"));
        op4.setText(getIntent().getStringExtra("op_d"));
        op5.setText(getIntent().getStringExtra("op_e"));
        qn_vote.setText(getIntent().getStringExtra("question"));
        idd.setText(getIntent().getStringExtra("id"));

        obj_vot.setText("Edit vote questions");
        item_find_donor.setVisibility(View.GONE);
    }

    public void sendVotes(View view) {

        String o1 = op1.getText().toString().trim();
        String o2 = op2.getText().toString().trim();
        String o3 = op3.getText().toString().trim();
        String o4 = op4.getText().toString().trim();
        String o5 = op5.getText().toString().trim();
        String opts = "A)" + op1.getText().toString().trim() + " " + "B)" + op2.getText().toString().trim() +
                " " + "C)" + op3.getText().toString().trim() + " " + "D)" + op4.getText().toString().trim() +
                " " + "E)" + op5.getText().toString().trim();
        String qn = qn_vote.getText().toString().trim();
        if (qn.isEmpty() && o3.isEmpty()&& o4.isEmpty()&& o5.isEmpty()&&o1.isEmpty() || o2.isEmpty()) {
            Toast.makeText(this, "All fields are required", Toast.LENGTH_SHORT).show();

        }
//        else if () {
//            Toast.makeText(this, "options are required", Toast.LENGTH_SHORT).show();
////        || o3.isEmpty() || o4.isEmpty() || o5.isEmpty()
//        }
        else {
            AlertDialog.Builder alertDialog2 = new AlertDialog.Builder(
                    EditVoting.this);
            alertDialog2.setTitle("Confirm to proceed");
            alertDialog2.setMessage("Make sure you double check your question and options before confirming");
            alertDialog2.setIcon(R.drawable.ic_warning);
            alertDialog2.setPositiveButton("YES",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            sendVoting();
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


    /* send  votes */

    private void sendVoting() {

        final ProgressDialog dialog = new ProgressDialog(EditVoting.this);
        dialog.setMessage("Please wait...");
     dialog.show();
        final String opts1 = this.op1.getText().toString().trim();
        final String opts2 = this.op2.getText().toString().trim();
        final String opts3 = this.op3.getText().toString().trim();
        final String opts4 = this.op4.getText().toString().trim();
        final String opts5 = this.op5.getText().toString().trim();
        final String qn = this.qn_vote.getText().toString().trim();
        final String idd = this.idd.getText().toString().trim();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, urls.EDIT_VOTE_QUESTION,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            Log.i("tagconvertstr", "[" + response + "]");
                            JSONObject object = new JSONObject(response);
                            String success = object.getString("success");
                            if (success.equals("1")) {
                                dialog.dismiss();
                                dialog.dismiss();
                                sendVote.setVisibility(View.VISIBLE);
                                Toast.makeText(EditVoting.this, "Question sent successfully", Toast.LENGTH_LONG).show();
                                qn_vote.getText().clear();
                                op1.getText().clear();
                                op2.getText().clear();
                                op3.getText().clear();
                                op4.getText().clear();
                                op5.getText().clear();

                                Intent rf = new Intent(EditVoting.this,HistoryVoting.class);
                                startActivity(rf);
                                finish();

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            dialog.dismiss();
                            sendVote.setVisibility(View.VISIBLE);
                            Toast.makeText(EditVoting.this, "Question not sent successful, please try again ", Toast.LENGTH_LONG).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(EditVoting.this, "Question not sent successful, please check your network and try again", Toast.LENGTH_LONG).show();
                dialog.dismiss();
                sendVote.setVisibility(View.VISIBLE);
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("question", qn);
                params.put("options_a", opts1);
                params.put("options_b", opts2);
                params.put("options_c", opts3);
                params.put("options_d", opts4);
                params.put("options_e", opts5);
                params.put("id", idd);

                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }

    public void goback(View view) {
        startActivity(new Intent(EditVoting.this, AskActivity.class));
        finish();
    }

    public void viewHisVot(View view) {
        startActivity(new Intent(EditVoting.this, HistoryVoting.class));
    }
}