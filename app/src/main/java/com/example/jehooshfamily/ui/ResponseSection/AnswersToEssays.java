package com.example.jehooshfamily.ui.ResponseSection;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
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
import com.example.jehooshfamily.ui.Adapters.ViewResponseEssayAmswersAdapter;
import com.example.jehooshfamily.ui.Models.AnswersEssay_Model;
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

public class AnswersToEssays extends AppCompatActivity {

    Urls urls;
    RecyclerView recyclerView_answer_essay;
    CardView no_answer_essay;
    List<AnswersEssay_Model> mData;
    SessionManager sessionManager;
    ViewResponseEssayAmswersAdapter amswersAdapter;
    String getId, names, question_id;
    TextView qn_id, qn, qn_date, open_folder_show_doc;
    SpinKitView spinKitView;
    String fileFinalName;
    String fileName = "Essay-answers";
    LinearLayout download_results_linear,wrong_download_results_linear;
    ImageView export;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_answers_to_essays);

        spinKitView = findViewById(R.id.spin_kit);

//part for handling the sent question from the response adapter
        qn_date = findViewById(R.id.resp_to_answer_date);
        qn_id = findViewById(R.id.resp_to_answer_id);
        qn = findViewById(R.id.resp_to_answer_qn);

// attaching the pasted on extras
        qn_date.setText(getIntent().getStringExtra("date"));
        qn_id.setText(getIntent().getStringExtra("id"));
        qn.setText(getIntent().getStringExtra("sent_question"));

        question_id = qn_id.getText().toString();

        no_answer_essay = findViewById(R.id.no_answer_essay);

        urls = new Urls();
        sessionManager = new SessionManager(this);
        HashMap<String, String> user = sessionManager.getUserDetail();
        getId = user.get(SessionManager.ID);
        names = user.get(SessionManager.NAMES);


        open_folder_show_doc = findViewById(R.id.open_folder_show_doc);
        wrong_download_results_linear = findViewById(R.id.wrong_download_results_linear);
        export = findViewById(R.id.export);
        download_results_linear = findViewById(R.id.download_results_linear);

        //handle recyclerview
        recyclerView_answer_essay = findViewById(R.id.recyclerView_answer_essay);
        mData = new ArrayList<>();
        recyclerView_answer_essay.setLayoutManager(new LinearLayoutManager(this));
        amswersAdapter = new ViewResponseEssayAmswersAdapter(this, mData);
        recyclerView_answer_essay.setAdapter(amswersAdapter);

        updateAnswer();
        getResponseEssay();

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        updateAnswer();
        getResponseEssay();
    }


    private void updateAnswer() {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, urls.UPDATEANSWER_ESSAY,
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
    private void getResponseEssay() {
        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage("Please wait as we fetch the answers for this question");

        dialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, urls.RESPONSE_ESSAY_ANSWER_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            Log.i("tagconvertstr", "[" + response + "]");

                            JSONArray tips = new JSONArray(response);
                            if (tips.length() == 0) {
                                dialog.dismiss();
                                no_answer_essay.setVisibility(View.VISIBLE);
                            } else {
                                for (int i = 0; i < tips.length(); i++) {
                                    Log.i("tagconvertstr", "[" + response + "]");

                                    JSONObject jsonObject = tips.getJSONObject(i);
                                    export.setVisibility(View.VISIBLE);
                                    String id = jsonObject.getString("id").trim();
                                    String answer = jsonObject.getString("answer").trim();
                                    String from_who = jsonObject.getString("from_who").trim();
                                    String userid = jsonObject.getString("user_id").trim();
                                    String sent_question = jsonObject.getString("sent_question").trim();
                                    String sent_qn_id = jsonObject.getString("sent_qn_id").trim();
                                    String boss_name = jsonObject.getString("boss_name").trim();
                                    String boss_id = jsonObject.getString("boss_id").trim();
                                    String date_submission = jsonObject.getString("date_submission").trim();


                                    AnswersEssay_Model markets_model = new AnswersEssay_Model(id,
                                            answer,
                                            from_who,
                                            userid,
                                            sent_question,
                                            sent_qn_id,
                                            boss_name,
                                            boss_id,
                                            date_submission);
                                    mData.add(markets_model);
                                    dialog.dismiss();
                                    no_answer_essay.setVisibility(View.GONE);

                                }
                                amswersAdapter = new ViewResponseEssayAmswersAdapter(AnswersToEssays.this, mData);
                                recyclerView_answer_essay.setAdapter(amswersAdapter);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            dialog.dismiss();
                            export.setVisibility(View.GONE);
                            no_answer_essay.setVisibility(View.VISIBLE);
                            Toast.makeText(AnswersToEssays.this, " something went wrong, please try again", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                dialog.dismiss();
                export.setVisibility(View.GONE);
                no_answer_essay.setVisibility(View.VISIBLE);
//
                Toast.makeText(AnswersToEssays.this, "could not load your answers, please check your connection and try again", Toast.LENGTH_LONG).show();
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
        startActivity(new Intent(AnswersToEssays.this, ResponseQnEssay.class));
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
        Toast.makeText(this, "id is " +iidd, Toast.LENGTH_SHORT).show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, urls.EXPORT_ESSAYANSWERS,
                response -> {
                    try {
                        Log.i("tagconvertstr", "[" + response + "]");
                        JSONArray tips = new JSONArray(response);

                        for (int i = 0; i < tips.length(); i++) {
                            JSONObject inputsObjects = tips.getJSONObject(i);

//                            String boss_name = inputsObjects.getString("boss_name");
//                            Toast.makeText(this, "bb "+ boss_name, Toast.LENGTH_SHORT).show();
//                            wrong_download_results_linear.setVisibility(View.GONE);
//                            if (boss_name.length() != 0) {

                                download_results_linear.setVisibility(View.VISIBLE);
//                            }
                            wrong_download_results_linear.setVisibility(View.GONE);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        wrong_download_results_linear.setVisibility(View.VISIBLE);
//                        show_error.setText();
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
            Uri downloadUri = Uri.parse(urls.download_essay_answers);
            DownloadManager.Request request = new DownloadManager.Request(downloadUri);
            request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE)
//                    .setDestinationInExternalPublicDir(imageStorageDir + File.separator, )
            .setDestinationInExternalPublicDir(Environment.DIRECTORY_DOCUMENTS,fileFinalName)

                    .setTitle(fileFinalName).setDescription(getString(R.string.save_img))
                    .setAllowedOverMetered(true)// Set if download is allowed on Mobile network
                    .setAllowedOverRoaming(true)// Set if download is allowed on roaming network
                    .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
            dm.enqueue(request);

            open_folder_show_doc.setVisibility(View.VISIBLE);
        } catch (IllegalStateException ex) {
            Toast.makeText(getApplicationContext(), "Storage Error " + ex.toString(), Toast.LENGTH_LONG).show();
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
//            FileOpen.openFile(AnswersToEssays.this, myFile);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

        exportFile();
    }







}