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
import android.widget.LinearLayout;
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
import com.example.jehooshfamily.ui.HistorySection.HistoryObjectives;
import com.example.jehooshfamily.ui.Main_Functions.AskActivity;
import com.example.jehooshfamily.ui.URLs.SessionManager;
import com.example.jehooshfamily.ui.URLs.Urls;
import com.github.ybq.android.spinkit.SpinKitView;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Objectives extends AppCompatActivity {

    Urls urls;
    EditText options_a, options_b, options_c, options_d, options_e, question, answer;
    TextInputLayout con_qn, con_answer;
    LinearLayout container_options;
    SessionManager sessionManager;
    String getId, names;
    SpinKitView spinKitView;
    Button send_objective;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_objectives);

        spinKitView = findViewById(R.id.spin_kit);
        send_objective = findViewById(R.id.send_objective);
        options_a = findViewById(R.id.input_objective_options_a);
        options_b = findViewById(R.id.input_objective_options_b);
        options_c = findViewById(R.id.input_objective_options_c);
        options_d = findViewById(R.id.input_objective_options_d);
        options_e = findViewById(R.id.input_objective_options_e);
        answer = findViewById(R.id.input_objective_answer);
        question = findViewById(R.id.input_objective_question);
        con_answer = findViewById(R.id.con_answer);
        con_qn = findViewById(R.id.con_qn);
        container_options = findViewById(R.id.container_options);

        urls = new Urls();
        sessionManager = new SessionManager(this);
        HashMap<String, String> user = sessionManager.getUserDetail();
        getId = user.get(SessionManager.ID);
        names = user.get(SessionManager.NAMES);
    }


    public void showOptions(View view) {
        container_options.setVisibility(View.VISIBLE);
    }

    public void showQns(View view) {
        con_qn.setVisibility(View.VISIBLE);
    }

    public void showAnswer(View view) {
        con_answer.setVisibility(View.VISIBLE);
    }

    public void sendObjectives(View view) {
        //check to see if all fields are set
        String o1 = options_a.getText().toString().trim();
        String o2 = options_b.getText().toString().trim();
        String o3 = options_c.getText().toString().trim();
        String o4 = options_d.getText().toString().trim();
        String o5 = options_e.getText().toString().trim();
        String ans = answer.getText().toString().trim();
        String qn = question.getText().toString().trim();
        if (ans.isEmpty() && qn.isEmpty() && o1.isEmpty() && o2.isEmpty() && o3.isEmpty() && o4.isEmpty() && o5.isEmpty()) {

            Toast.makeText(this, "all fields are required", Toast.LENGTH_SHORT).show();

        } else {
            AlertDialog.Builder alertDialog2 = new AlertDialog.Builder(
                    Objectives.this);
            alertDialog2.setTitle("Confirm to proceed");
            alertDialog2.setMessage("Make sure you double check your question, options and answer before confirming");
            alertDialog2.setIcon(R.drawable.ic_warning);
            alertDialog2.setPositiveButton("YES",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            sendObjectives();

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



    /* send  objectives */

    private void sendObjectives() {

        final ProgressDialog dialog = new ProgressDialog(Objectives.this);
        dialog.setMessage("Please wait...");
        dialog.show();
//        Animation main_layout = AnimationUtils.loadAnimation(Objectives.this, R.anim.anim);
//        send_objective.setVisibility(View.GONE);
//        send_objective.setAnimation(main_layout);
//
//        spinKitView.setVisibility(View.VISIBLE);
        final String ans = answer.getText().toString().trim();
        final String opt1 = options_a.getText().toString().trim();
        final String opt2 = options_b.getText().toString().trim();
        final String opt3 = options_c.getText().toString().trim();
        final String opt4 = options_d.getText().toString().trim();
        final String opt5 = options_e.getText().toString().trim();
        final String qn = question.getText().toString().trim();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, urls.SEND_OBJECTIVES_ADMIN,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            Log.i("tagconvertstr", "[" + response + "]");
                            JSONObject object = new JSONObject(response);
                            String success = object.getString("success");
                            if (success.equals("1")) {
                                dialog.dismiss();
//                                spinKitView.setVisibility(View.GONE);
                                send_objective.setVisibility(View.VISIBLE);
                                Toast.makeText(Objectives.this, "Question sent successfully", Toast.LENGTH_LONG).show();
                                options_a.getText().clear();
                                options_b.getText().clear();
                                options_c.getText().clear();
                                options_d.getText().clear();
                                options_e.getText().clear();
                                question.getText().clear();
                                answer.getText().clear();

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            dialog.dismiss();
//                            spinKitView.setVisibility(View.GONE);
                            send_objective.setVisibility(View.VISIBLE);
                            Toast.makeText(Objectives.this, "Question not sent successful, please try again "
                                    , Toast.LENGTH_LONG).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(Objectives.this, "Question not sent successful, please check your network and try again", Toast.LENGTH_LONG).show();
                dialog.dismiss();
//                spinKitView.setVisibility(View.GONE);
                send_objective.setVisibility(View.VISIBLE);
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("question", qn);
                params.put("options_a", opt1);
                params.put("options_b", opt2);
                params.put("options_c", opt3);
                params.put("options_d", opt4);
                params.put("options_e", opt5);
                params.put("set_answer", ans);
                params.put("boss_name", names);
                params.put("boss_id", getId);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }

    public void goback(View view) {
        startActivity(new Intent(Objectives.this, AskActivity.class));
        finish();
    }

    public void viewHisObj(View view) {
        startActivity(new Intent(Objectives.this, HistoryObjectives.class));
    }
}

