package com.example.jehooshfamily.ui.ResponseSection;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
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
import com.example.jehooshfamily.ui.Adapters.ViewResponseObjecAnswersAdapter;
import com.example.jehooshfamily.ui.Models.AnswersObjectives_Model;
import com.example.jehooshfamily.ui.URLs.SessionManager;
import com.example.jehooshfamily.ui.URLs.Urls;
import com.github.ybq.android.spinkit.SpinKitView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class AnswerObjectives extends AppCompatActivity {
    Urls urls;
    Dialog totalResults;
    RecyclerView recyclerView_answer_obj;
    CardView no_answer_obj;
    List<AnswersObjectives_Model> mData;
    SessionManager sessionManager;
    ViewResponseObjecAnswersAdapter objecAnswersAdapter;
    String getId, names, question_id, sent_opt1, sent_opt2,
            sent_opt3, sent_opt4, sent_opt5;
    TextView qn_id, qn, qn_date, op1, op2, op3, op4, op5, open_folder_show_doc;
    LinearLayout lAA, lBB, lCC, lDD, lEE;
    int correct;
    int wrong;
    int total;
    SpinKitView spinKitView;
    String fileFinalName;
    String fileName = "Objectives-answers";
    LinearLayout download_results_linear,wrong_download_results_linear;
    ImageView export;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_answer_objectives);

        totalResults = new Dialog(this);
        spinKitView = findViewById(R.id.spin_kit);

//part for handling the sent question from the response adapter
        qn_date = findViewById(R.id.resp_to_answer_date);
        qn_id = findViewById(R.id.resp_to_answer_id);
        qn = findViewById(R.id.resp_to_answer_qn);
        op1 = findViewById(R.id.opt_obj1);
        op2 = findViewById(R.id.opt_obj2);
        op3 = findViewById(R.id.opt_obj3);
        op4 = findViewById(R.id.opt_obj4);
        op5 = findViewById(R.id.opt_obj5);

        lAA = findViewById(R.id.linearAA);
        lBB = findViewById(R.id.linearBB);
        lCC = findViewById(R.id.linearCC);
        lDD = findViewById(R.id.linearDD);
        lEE = findViewById(R.id.linearEE);

// attaching the pasted on extras
        qn_date.setText(getIntent().getStringExtra("date"));
        qn_id.setText(getIntent().getStringExtra("id"));
        qn.setText(getIntent().getStringExtra("sent_question"));
        op1.setText(getIntent().getStringExtra("options_a"));
        op2.setText(getIntent().getStringExtra("options_b"));
        op3.setText(getIntent().getStringExtra("options_c"));
        op4.setText(getIntent().getStringExtra("options_d"));
        op5.setText(getIntent().getStringExtra("options_e"));

        //check to see if any option is empty
        sent_opt1 = op1.getText().toString();
        sent_opt2 = op2.getText().toString();
        sent_opt3 = op3.getText().toString();
        sent_opt4 = op4.getText().toString();
        sent_opt5 = op5.getText().toString();

        if (!sent_opt1.isEmpty()) {
            op1.setVisibility(View.VISIBLE);
            lAA.setVisibility(View.VISIBLE);
        }
        if (!sent_opt2.isEmpty()) {
            op2.setVisibility(View.VISIBLE);
            lBB.setVisibility(View.VISIBLE);
        }
        if (!sent_opt3.isEmpty()) {
            op3.setVisibility(View.VISIBLE);
            lCC.setVisibility(View.VISIBLE);
        }
        if (!sent_opt4.isEmpty()) {
            op4.setVisibility(View.VISIBLE);
            lDD.setVisibility(View.VISIBLE);
        }
        if (!sent_opt5.isEmpty()) {
            op5.setVisibility(View.VISIBLE);
            lEE.setVisibility(View.VISIBLE);
        }
        //convert the value of the qn id to string
        question_id = qn_id.getText().toString();

        no_answer_obj = findViewById(R.id.no_answer_objectives);
        urls = new Urls();

        sessionManager = new SessionManager(this);
        HashMap<String, String> user = sessionManager.getUserDetail();
        getId = user.get(SessionManager.ID);
        names = user.get(SessionManager.NAMES);


        export = findViewById(R.id.export);
        open_folder_show_doc = findViewById(R.id.open_folder_show_doc);
        wrong_download_results_linear = findViewById(R.id.wrong_download_results_linear);
        download_results_linear = findViewById(R.id.download_results_linear);


        //handle recyclerview
        recyclerView_answer_obj = findViewById(R.id.recyclerView_answer_obj);
        mData = new ArrayList<>();
        recyclerView_answer_obj.setLayoutManager(new LinearLayoutManager(this));
        objecAnswersAdapter = new ViewResponseObjecAnswersAdapter(this, mData);
        recyclerView_answer_obj.setAdapter(objecAnswersAdapter);
        updateAnswer();
        getResponseObjectives();
    }

//    @Override
//    public void onBackPressed() {
//        super.onBackPressed();
//        updateAnswer();
//        getResponseObjectives();
//    }

    private void updateAnswer() {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, urls.UPDATEANSWER_OBJECTIVE,
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

    //    this gets the answer attached to this question
    private void getResponseObjectives() {
        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage("Loading...");

        spinKitView.setVisibility(View.VISIBLE);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, urls.RESPONSE_OBJECTIVES_ANSWER_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            Log.i("tagconvertstr", "[" + response + "]");

                            JSONArray tips = new JSONArray(response);

                            if (tips.length() == 0) {
                                spinKitView.setVisibility(View.GONE);
                                no_answer_obj.setVisibility(View.VISIBLE);
                            } else {
                                for (int i = 0; i < tips.length(); i++) {
                                    Log.i("tagconvertstr", "[" + response + "]");
                                    total++;
                                    JSONObject jsonObject = tips.getJSONObject(i);
                                    export.setVisibility(View.VISIBLE);
                                    String id = jsonObject.getString("id").trim();
                                    String answer_employee = jsonObject.getString("answer_employee").trim();
                                    String user_id = jsonObject.getString("user_id").trim();
                                    String from = jsonObject.getString("from_who").trim();
                                    String question_sent = jsonObject.getString("question_sent").trim();
                                    String question_sent_id = jsonObject.getString("sent_qn_id").trim();
                                    String answer_boss = jsonObject.getString("answer_boss").trim();
                                    String options1 = jsonObject.getString("options_a").trim();
                                    String options2 = jsonObject.getString("options_b").trim();
                                    String options3 = jsonObject.getString("options_c").trim();
                                    String options4 = jsonObject.getString("options_d").trim();
                                    String options5 = jsonObject.getString("options_e").trim();
                                    String boss_name = jsonObject.getString("boss_name").trim();
                                    String boss_id = jsonObject.getString("boss_id").trim();
                                    String date_submission = jsonObject.getString("date_submission").trim();


                                    //check to see for how many wrong or right
                                    if (answer_boss.toLowerCase().equals(answer_employee.toLowerCase())) {
                                        correct++;
                                    } else {
                                        wrong++;
                                    }


                                    AnswersObjectives_Model markets_model =
                                            new AnswersObjectives_Model(id,
                                                    answer_employee,
                                                    user_id,
                                                    from,
                                                    question_sent,
                                                    question_sent_id,
                                                    answer_boss,
                                                    options1,
                                                    options2,
                                                    options3,
                                                    options4,
                                                    options5,
                                                    boss_name,
                                                    boss_id,
                                                    date_submission);
                                    mData.add(markets_model);
                                    dialog.dismiss();
                                    spinKitView.setVisibility(View.GONE);
                                    no_answer_obj.setVisibility(View.GONE);

                                }

                                objecAnswersAdapter = new ViewResponseObjecAnswersAdapter(AnswerObjectives.this, mData);
                                recyclerView_answer_obj.setAdapter(objecAnswersAdapter);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            dialog.dismiss();
                            export.setVisibility(View.GONE);
                            no_answer_obj.setVisibility(View.VISIBLE);
                            Toast.makeText(AnswerObjectives.this, " something went wrong, please try again", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                dialog.dismiss();
                no_answer_obj.setVisibility(View.VISIBLE);
                export.setVisibility(View.GONE);
                Toast.makeText(AnswerObjectives.this, "sdf" + "could not load your answers, please check your connection and try again", Toast.LENGTH_LONG).show();
            }
        }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("boss_id", getId);
                params.put("sent_qn_id", question_id);
                return params;

            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    public void goback(View view) {
        startActivity(new Intent(AnswerObjectives.this, ResponseQnObjective.class));
    }

    public void showTotalResults(View view) {
        totalResults.setContentView(R.layout.totalresults);
        TextView tcount = totalResults.findViewById(R.id.totalCount);
        TextView ccount = totalResults.findViewById(R.id.CorrectCount);
        TextView wcount = totalResults.findViewById(R.id.wrongCount);

        //change the ints to strings
        String tc = String.valueOf(total);
        String cc = String.valueOf(correct);
        String wc = String.valueOf(wrong);

        //attach the results to the textviews

        tcount.setText(tc);
        ccount.setText(cc);
        wcount.setText(wc);

        Button ok = totalResults.findViewById(R.id.ok);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                totalResults.dismiss();
            }
        });
        Objects.requireNonNull(totalResults.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        totalResults.show();
    }



    /**
     * the exporting part*/
    public void exportEssay(View view) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("Are you sure, you want to export these results to an excel file");
        alertDialogBuilder.setPositiveButton("yes",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        exportEssayAnswers();
                    }

                }).setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }


    private void exportEssayAnswers() {
        String iidd = qn_id.getText().toString();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, urls.EXPORT_OBJECTIVEANSWERS,
                response -> {
                    try {
                        Log.i("tagconvertstr", "[" + response + "]");
                        JSONArray tips = new JSONArray(response);

                        for (int i = 0; i < tips.length(); i++) {
                            JSONObject inputsObjects = tips.getJSONObject(i);
//                            exportFile();
                            download_results_linear.setVisibility(View.VISIBLE);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        wrong_download_results_linear.setVisibility(View.VISIBLE);
//                        show_error.setText(e.toString());
                        Toast.makeText(this, "er "+ e.toString(), Toast.LENGTH_SHORT).show();
                    }
                }, error -> {
            wrong_download_results_linear.setVisibility(View.VISIBLE);
//            show_error.setText(error.toString());
            Toast.makeText(this, "er "+ error.toString(), Toast.LENGTH_SHORT).show();
        }) {
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("sent_qn_id", iidd);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    public void exportFile() {
        try {
            File imageStorageDir = new File(Environment.getExternalStorageDirectory() + "/Documents");
            if (!imageStorageDir.exists()) {
                imageStorageDir.mkdirs();
            }
            String imgExtension = ".xls";

            String date = DateFormat.getDateTimeInstance().format(new Date());
            @SuppressLint("SimpleDateFormat") String timeStamp = new SimpleDateFormat("yyyy-MM-dd HH:mm").format(new Date());
            fileFinalName = fileName + "-" + timeStamp.replace(" ", " ").
                    replace(":", ":").replace(".", ".") + imgExtension;
            DownloadManager dm = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
            Uri downloadUri = Uri.parse(urls.download_objective_answers);
            DownloadManager.Request request = new DownloadManager.Request(downloadUri);
            request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE)
                    .setDestinationInExternalPublicDir(Environment.DIRECTORY_DOCUMENTS,fileFinalName)
//                    .setDestinationInExternalPublicDir(imageStorageDir + File.separator, fileFinalName)
                    .setTitle(fileFinalName).setDescription(getString(R.string.save_img))
                    .setAllowedOverMetered(true)// Set if download is allowed on Mobile network
                    .setAllowedOverRoaming(true)// Set if download is allowed on roaming network
                    .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
            dm.enqueue(request);

            open_folder_show_doc.setVisibility(View.VISIBLE);
        } catch (IllegalStateException ex) {
            Toast.makeText(getApplicationContext(), "Storage Error", Toast.LENGTH_LONG).show();
            ex.printStackTrace();
        } catch (Exception ex) {
            Toast.makeText(getApplicationContext(), "Unable to save file, please check your connection and try again", Toast.LENGTH_LONG).show();
            ex.printStackTrace();
        }
    }

    /**
     * opens the downloaded file
     */
    public void open_intents(View view) {
//        File myFile = new File("/storage/emulated/0/Documents" + File.separator + fileFinalName);
//        try {
//            FileOpen.openFile(AnswerObjectives.this, myFile);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        exportFile();
    }

}