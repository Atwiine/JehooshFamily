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
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
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
import com.example.jehooshfamily.ui.Adapters.ViewResponseVotingAnswersAdapter;
import com.example.jehooshfamily.ui.Models.AnswersVoting_Model;
import com.example.jehooshfamily.ui.URLs.SessionManager;
import com.example.jehooshfamily.ui.URLs.Urls;
import com.github.lzyzsd.circleprogress.ArcProgress;
import com.github.lzyzsd.circleprogress.CircleProgress;
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

public class AnswerVotes extends AppCompatActivity {

    Urls urls;
    RecyclerView recyclerView_answer_vote;
    List<AnswersVoting_Model> mData;
    SessionManager sessionManager;
    ViewResponseVotingAnswersAdapter votingAnswersAdapter;
    String getId, names, question_id, sent_opt1, sent_opt2,
            sent_opt3, sent_opt4, sent_opt5;
    TextView qn_id, qn, qn_date, op1, op2, op3, op4, op5, no_answer_vote, open_folder_show_doc;
    LinearLayout lAA, lBB, lCC, lDD, lEE;

    int opt1, opt2, opt3, opt4, opt5;
    int total;
    Dialog totalResultsVotes;
    CircleProgress circleProgress;
    ArcProgress arcProgress;
    SpinKitView spinKitView;
    String fileFinalName;
    String fileName = "Vote-answers";
    LinearLayout download_results_linear,wrong_download_results_linear;
    ImageView export;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_answer_voting);

        totalResultsVotes = new Dialog(this);

        spinKitView = findViewById(R.id.spin_kit);


//part for handling the sent question from the response adapter
        qn_date = findViewById(R.id.resp_to_answer_date);
        qn_id = findViewById(R.id.resp_to_answer_id);
        qn = findViewById(R.id.resp_to_answer_qn);
        op1 = findViewById(R.id.opt_vot1);
        op2 = findViewById(R.id.opt_vot2);
        op3 = findViewById(R.id.opt_vot3);
        op4 = findViewById(R.id.opt_vot4);
        op5 = findViewById(R.id.opt_vot5);


        lAA = findViewById(R.id.linearAAV);
        lBB = findViewById(R.id.linearBBV);
        lCC = findViewById(R.id.linearCCV);
        lDD = findViewById(R.id.linearDDV);
        lEE = findViewById(R.id.linearEEV);
// attaching the pasted on extras
        qn_date.setText(getIntent().getStringExtra("date"));
        qn_id.setText(getIntent().getStringExtra("id"));
        op1.setText(getIntent().getStringExtra("options_a"));
        op2.setText(getIntent().getStringExtra("options_b"));
        op3.setText(getIntent().getStringExtra("options_c"));
        op4.setText(getIntent().getStringExtra("options_d"));
        op5.setText(getIntent().getStringExtra("options_e"));
        qn.setText(getIntent().getStringExtra("sent_question"));

        //check to see if any option is empty
        sent_opt1 = op1.getText().toString();
        sent_opt2 = op2.getText().toString();
        sent_opt3 = op3.getText().toString();
        sent_opt4 = op4.getText().toString();
        sent_opt5 = op5.getText().toString();

        if (!sent_opt1.isEmpty()) {
//            op1.setVisibility(View.VISIBLE);
            lAA.setVisibility(View.VISIBLE);
        }
        if (!sent_opt2.isEmpty()) {
//            op2.setVisibility(View.VISIBLE);
            lBB.setVisibility(View.VISIBLE);
        }
        if (!sent_opt3.isEmpty()) {
//            op3.setVisibility(View.VISIBLE);
            lCC.setVisibility(View.VISIBLE);
        }
        if (!sent_opt4.isEmpty()) {
//            op4.setVisibility(View.VISIBLE);
            lDD.setVisibility(View.VISIBLE);
        }
        if (!sent_opt5.isEmpty()) {
//            op5.setVisibility(View.VISIBLE);
            lEE.setVisibility(View.VISIBLE);
        }

        //convert the value of the qn id to string
        question_id = qn_id.getText().toString();

        no_answer_vote = findViewById(R.id.no_answer_votes);
        urls = new Urls();

        sessionManager = new SessionManager(this);
        HashMap<String, String> user = sessionManager.getUserDetail();
        getId = user.get(SessionManager.ID);
        names = user.get(SessionManager.NAMES);

        open_folder_show_doc = findViewById(R.id.open_folder_show_doc);
        wrong_download_results_linear = findViewById(R.id.wrong_download_results_linear);
        download_results_linear = findViewById(R.id.download_results_linear);
        export = findViewById(R.id.export);

        //handle recyclerview
        recyclerView_answer_vote = findViewById(R.id.recyclerView_answer_voting);
        mData = new ArrayList<>();
        recyclerView_answer_vote.setLayoutManager(new LinearLayoutManager(this));
        votingAnswersAdapter = new ViewResponseVotingAnswersAdapter(this, mData);
        recyclerView_answer_vote.setAdapter(votingAnswersAdapter);

        updateAnswer();

        getResponseVotes();
    }

    private void updateAnswer() {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, urls.UPDATEANSWER_VOTE,
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
    private void getResponseVotes() {
        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage("Loading...");
        dialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, urls.RESPONSE_VOTING_ANSWER_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            Log.i("tagconvertstr", "[" + response + "]");

                            JSONArray tips = new JSONArray(response);

                            if (tips.length() == 0) {
                                dialog.dismiss();
                                no_answer_vote.setVisibility(View.VISIBLE);
                            } else {
                                for (int i = 0; i < tips.length(); i++) {
                                    Log.i("tagconvertstr", "[" + response + "]");

                                    JSONObject jsonObject = tips.getJSONObject(i);
                                    export.setVisibility(View.VISIBLE);
                                    String id = jsonObject.getString("id").trim();
                                    String answer_employee = jsonObject.getString("answer_employee").trim();
                                    String from = jsonObject.getString("from_who").trim();
                                    String question_sent = jsonObject.getString("question_sent").trim();
                                    String sent_qn_id = jsonObject.getString("sent_qn_id").trim();
                                    String options1 = jsonObject.getString("options_a").trim();
                                    String options2 = jsonObject.getString("options_b").trim();
                                    String options3 = jsonObject.getString("options_c").trim();
                                    String options4 = jsonObject.getString("options_d").trim();
                                    String options5 = jsonObject.getString("options_e").trim();
                                    String boss_id = jsonObject.getString("boss_id").trim();
                                    String boss_name = jsonObject.getString("boss_name").trim();
                                    String date_submission = jsonObject.getString("date_submission").trim();

                                    //check to see for how many wrong or right
                                    if (answer_employee.toLowerCase().equals(options1.toLowerCase())) {
                                        opt1++;
                                    }
                                    if (answer_employee.toLowerCase().equals(options2.toLowerCase())) {
                                        opt2++;
                                    }
                                    if (answer_employee.toLowerCase().equals(options3.toLowerCase())) {
                                        opt3++;
                                    }
                                    if (answer_employee.toLowerCase().equals(options4.toLowerCase())) {
                                        opt4++;
                                    }
                                    if (answer_employee.toLowerCase().equals(options5.toLowerCase())) {
                                        opt5++;
                                    }


                                    AnswersVoting_Model markets_model =
                                            new AnswersVoting_Model
                                                    (id,
                                                            answer_employee,
                                                            from,
                                                            question_sent,
                                                            sent_qn_id,
                                                            options1,
                                                            options2,
                                                            options3,
                                                            options4,
                                                            options5,
                                                            boss_id,
                                                            boss_name,
                                                            date_submission);
                                    mData.add(markets_model);
                                    dialog.dismiss();
//                                    export.setVisibility(View.GONE);
                                    no_answer_vote.setVisibility(View.GONE);

                                }
                                votingAnswersAdapter = new ViewResponseVotingAnswersAdapter(AnswerVotes.this, mData);
                                recyclerView_answer_vote.setAdapter(votingAnswersAdapter);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            dialog.dismiss();
                            no_answer_vote.setVisibility(View.VISIBLE);
                            export.setVisibility(View.GONE);
                            Toast.makeText(AnswerVotes.this, " something went wrong, please try again", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                dialog.dismiss();
                spinKitView.setVisibility(View.GONE);
                no_answer_vote.setVisibility(View.VISIBLE);
                Toast.makeText(AnswerVotes.this, "could not load your answers, please check your connection and try again", Toast.LENGTH_LONG).show();
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
        startActivity(new Intent(AnswerVotes.this, ResponseQnVotes.class));
    }

    public void showTotalResults(View view) {
        //might take all this to the next page

        totalResultsVotes.setContentView(R.layout.totalresultsvotes);
        TextView oo1 = totalResultsVotes.findViewById(R.id.vopt1);
        TextView oo2 = totalResultsVotes.findViewById(R.id.vopt2);
        TextView oo3 = totalResultsVotes.findViewById(R.id.vopt3);
        TextView oo4 = totalResultsVotes.findViewById(R.id.vopt4);
        TextView oo5 = totalResultsVotes.findViewById(R.id.vopt5);
        TextView tto = totalResultsVotes.findViewById(R.id.VtotalCount);

        TextView vv1 = totalResultsVotes.findViewById(R.id.vvo1);
        TextView vv2 = totalResultsVotes.findViewById(R.id.vvo2);
        TextView vv3 = totalResultsVotes.findViewById(R.id.vvo3);
        TextView vv4 = totalResultsVotes.findViewById(R.id.vvo4);
        TextView vv5 = totalResultsVotes.findViewById(R.id.vvo5);

       RelativeLayout lv1 = totalResultsVotes.findViewById(R.id.lccount);
        RelativeLayout lv2 = totalResultsVotes.findViewById(R.id.v2);
        RelativeLayout lv3 = totalResultsVotes.findViewById(R.id.v3);
        RelativeLayout lv4 = totalResultsVotes.findViewById(R.id.v4);
        RelativeLayout lv5 = totalResultsVotes.findViewById(R.id.v5);

        //change the ints to strings
        String vo1 = String.valueOf(opt1);
        String vo2 = String.valueOf(opt2);
        String vo3 = String.valueOf(opt3);
        String vo4 = String.valueOf(opt4);
        String vo5 = String.valueOf(opt5);

        //attach the results to the textviews

        oo1.setText(vo1);
        oo2.setText(vo2);
        oo3.setText(vo3);
        oo4.setText(vo4);
        oo5.setText(vo5);

        //from db names options
        vv1.setText(sent_opt1);
        vv2.setText(sent_opt2);
        vv3.setText(sent_opt3);
        vv4.setText(sent_opt4);
        vv5.setText(sent_opt5);

        String checkv1 = vv1.getText().toString();
        String checkv2 = vv2.getText().toString();
        String checkv3 = vv3.getText().toString();
        String checkv4 = vv4.getText().toString();
        String checkv5 = vv5.getText().toString();

        //get the total
        total = opt1 + opt2 + opt3 + opt4 + opt5;
        String tots = String.valueOf(total);
        tto.setText(tots);

        //get the %centage and pass it to the progress thingy
        int percentage;


        if (!checkv1.isEmpty()) {
            lv1.setVisibility(View.VISIBLE);
            arcProgress = totalResultsVotes.findViewById(R.id.arc_progress_op1);
            percentage = (opt1 * 100 / total);
//            arcProgress.setBottomText(checkv1);
            arcProgress.setSuffixText("%");
            arcProgress.setProgress(percentage);
        }
        if (!checkv2.isEmpty()) {
            lv2.setVisibility(View.VISIBLE);
            arcProgress = totalResultsVotes.findViewById(R.id.arc_progress_op2);
            percentage = (opt2 * 100 / total);
            arcProgress.setSuffixText("%");
//            arcProgress.setBottomText(checkv2);
            arcProgress.setProgress(percentage);
        }
        if (!checkv3.isEmpty()) {
            lv3.setVisibility(View.VISIBLE);
            arcProgress = totalResultsVotes.findViewById(R.id.arc_progress_op3);
            percentage = (opt3 * 100 / total);
            arcProgress.setSuffixText("%");
//            arcProgress.setBottomText(checkv3);
            arcProgress.setProgress(percentage);
        }
        if (!checkv4.isEmpty()) {
            lv4.setVisibility(View.VISIBLE);
            arcProgress = totalResultsVotes.findViewById(R.id.arc_progress_op4);
            percentage = (opt4 * 100 / total);
            arcProgress.setSuffixText("%");
//            arcProgress.setBottomText(checkv4);
            arcProgress.setProgress(percentage);
        }
        if (!checkv5.isEmpty()) {
            lv5.setVisibility(View.VISIBLE);
            arcProgress = totalResultsVotes.findViewById(R.id.arc_progress_op5);
            percentage = (opt5 * 100 / total);
            arcProgress.setSuffixText("%");
//            arcProgress.setBottomText(checkv5);
            arcProgress.setProgress(percentage);
        }

        Button ok = totalResultsVotes.findViewById(R.id.ok);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                totalResultsVotes.dismiss();
            }
        });
        Objects.requireNonNull(totalResultsVotes.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        totalResultsVotes.show();
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
        StringRequest stringRequest = new StringRequest(Request.Method.POST, urls.EXPORT_VOTEANSWERS,
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
            Uri downloadUri = Uri.parse(urls.download_vote_answers);
            DownloadManager.Request request = new DownloadManager.Request(downloadUri);
            request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE)
//                    .setDestinationInExternalPublicDir(imageStorageDir + File.separator, fileFinalName)
                    .setDestinationInExternalPublicDir(Environment.DIRECTORY_DOCUMENTS,fileFinalName)
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
//            FileOpen.openFile(AnswerVotes.this, myFile);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

        exportFile();
    }

}