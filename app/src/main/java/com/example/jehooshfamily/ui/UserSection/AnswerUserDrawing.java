package com.example.jehooshfamily.ui.UserSection;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.jehooshfamily.R;
import com.example.jehooshfamily.ui.Drawing.DrawUserMain;
import com.example.jehooshfamily.ui.Drawing.DrawUserMainHigher;
import com.example.jehooshfamily.ui.Drawing.TakeUpload;
import com.example.jehooshfamily.ui.URLs.SessionManagerLogin;
import com.example.jehooshfamily.ui.URLs.Urls;
import com.github.ybq.android.spinkit.SpinKitView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class AnswerUserDrawing extends AppCompatActivity {
    Urls urls;
    String getIdempl, namesemplo, boss_id, boss_name, sent_qn, sent_qnid;
    TextView qn_id, qn, qn_date;
    SessionManagerLogin sessionManager;
    EditText answer;
    SpinKitView spinKitView;
    Button emp_drawbnt;
    private static final int CAMERA_PERMISSION_CODE = 100;
    private static final int STORAGE_PERMISSION_CODE = 101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_answer_user_drawing);

        spinKitView = findViewById(R.id.spin_kit);
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


        //handle permissions
        checkPermission(
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                STORAGE_PERMISSION_CODE);

//        checkPermission(Manifest.permission.CAMERA,
//                CAMERA_PERMISSION_CODE);
//        handles camera options

    }


    // Function to check and request permission.
    public void checkPermission(String permission, int requestCode) {
        if (ContextCompat.checkSelfPermission(AnswerUserDrawing.this, permission)
                == PackageManager.PERMISSION_DENIED) {

            // Requesting the permission
            ActivityCompat.requestPermissions(AnswerUserDrawing.this,
                    new String[]{permission},
                    requestCode);
        } else {
            Toast.makeText(AnswerUserDrawing.this,
                    "Permission already granted",
                    Toast.LENGTH_SHORT)
                    .show();
        }
    }


    // This function is called when the user accepts or decline the permission.
    // Request Code is used to check which permission called this function.
    // This request code is provided when the user is prompt for permission.

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super
                .onRequestPermissionsResult(requestCode,
                        permissions,
                        grantResults);

       /* if (requestCode == CAMERA_PERMISSION_CODE) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(AnswerEmployeeDrawing.this,
                        "Camera Permission Granted",
                        Toast.LENGTH_SHORT)
                        .show();
            } else {
                Toast.makeText(AnswerEmployeeDrawing.this,
                        "Camera Permission Denied",
                        Toast.LENGTH_SHORT)
                        .show();
            }
        } else */
        if (requestCode == STORAGE_PERMISSION_CODE) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(AnswerUserDrawing.this,
                        "Storage Permission Granted",
                        Toast.LENGTH_SHORT)
                        .show();
            } else {
                Toast.makeText(AnswerUserDrawing.this,
                        "Storage Permission Denied",
                        Toast.LENGTH_SHORT)
                        .show();
            }
        }
    }

    public void sendEssay(View view) {
        String check = answer.getText().toString().trim();
        if (!check.equals("")) {
            AlertDialog.Builder alertDialog2 = new AlertDialog.Builder(
                    AnswerUserDrawing.this);
            alertDialog2.setTitle("Confirm to proceed");
            alertDialog2.setMessage("Make sure you double check your answer before confirming");
            alertDialog2.setIcon(R.drawable.ic_warning);
            alertDialog2.setPositiveButton("YES",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            sendDrawingAnswer();
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

    private void sendDrawingAnswer() {
        final ProgressDialog dialog = new ProgressDialog(AnswerUserDrawing.this);
        dialog.setMessage("Please wait...");
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
                                Toast.makeText(AnswerUserDrawing.this, "answer sent successfully", Toast.LENGTH_LONG).show();
                                answer.getText().clear();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(AnswerUserDrawing.this, "answer not sent successful, please try again ", Toast.LENGTH_LONG).show();
                            dialog.dismiss();
                            spinKitView.setVisibility(View.GONE);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(AnswerUserDrawing.this, "answer not sent successful, please check your network and try again", Toast.LENGTH_LONG).show();
                dialog.dismiss();
                spinKitView.setVisibility(View.GONE);
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("answer", essayanswer);
                params.put("from_who", namesemplo);
                params.put("user_id", getIdempl);
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
        startActivity(new Intent(AnswerUserDrawing.this, TypesQns.class));
        finish();
    }

    public void proceedDrawing(View view) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            Intent ss = new Intent(AnswerUserDrawing.this, DrawUserMainHigher.class);
            ss.putExtra("question_id", sent_qnid);
            ss.putExtra("question", sent_qn);
            startActivity(ss);
        }else {
            Intent ss = new Intent(AnswerUserDrawing.this, DrawUserMain.class);
            ss.putExtra("question_id", sent_qnid);
            ss.putExtra("question", sent_qn);
            startActivity(ss);
        }
    }

    public void proceedCamera(View view) {
        Intent ss = new Intent(AnswerUserDrawing.this, TakeUpload.class);
        ss.putExtra("question_id", sent_qnid);
        ss.putExtra("question", sent_qn);
        startActivity(ss);
    }
}
