package com.example.jehooshfamily.ui.Drawing;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.jehooshfamily.R;
import com.example.jehooshfamily.ui.MainActivity;
import com.example.jehooshfamily.ui.URLs.SessionManagerLogin;
import com.example.jehooshfamily.ui.URLs.Urls;
import com.example.jehooshfamily.ui.UserSection.UserDraw;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class UploadPicture extends AppCompatActivity {
    private static final String URL_UPLOAD = "http://192.168.1.76:8080/fallyarmywormpics/attach_photo.php";
    String getId, getPpnumber;
    ImageView profile_image;
    Urls urls;
    SessionManagerLogin managerLogin;
    String names;
    //    private static final String TAG = MainActivity.class.getSimpleName();
    private Button btn_send_answer, btn_phot_upload, get_location;
    private Menu action;
    private Bitmap bitmap;
    String question_name, question_id, boss_name, boss_id,question_explain;
//    SpinKitView spinKitView;
    EditText explain_draw_input;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_upload_picture);

//        spinKitView = findViewById(R.id.spin_kit);
        explain_draw_input = findViewById(R.id.explain_draw_input);

        question_id = getIntent().getStringExtra("question_id");
        question_name = getIntent().getStringExtra("question");
//        question_explain = getIntent().getStringExtra("question_explain");

        explain_draw_input.setText(question_explain);

        managerLogin = new SessionManagerLogin(this);
        urls = new Urls();
        HashMap<String, String> user = managerLogin.getUserDetail();
        getId = user.get(SessionManagerLogin.ID);
        names = user.get(SessionManagerLogin.NAMES);
        boss_id = user.get(SessionManagerLogin.BOSS_ID); // boss's names getIdempl =  user.get(SessionManagerLogin.ID); //employee's id
        boss_name = user.get(SessionManagerLogin.BOSS_NAME);

        btn_send_answer = findViewById(R.id.btn_send_answer);
        btn_phot_upload = findViewById(R.id.btn_photo);
        profile_image = findViewById(R.id.profile_image);
        btn_phot_upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chooseFile();
            }
        });

        btn_send_answer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String ss =  explain_draw_input.getText().toString().trim();
                if (ss.isEmpty()){
                    Toast.makeText(UploadPicture.this, "Please provide an explanation for your drawing before submitting", Toast.LENGTH_LONG).show();
                }
                AlertDialog.Builder alertDialog2 = new AlertDialog.Builder(
                        UploadPicture.this);
                alertDialog2.setTitle("Confirm to proceed");
                alertDialog2.setMessage("Are you sure this is right image selected");
                alertDialog2.setIcon(R.drawable.ic_warning);
                alertDialog2.setPositiveButton("YES",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                UploadPicture(getId, getStringImage(bitmap),ss);
                            }
                        });
                alertDialog2.setNegativeButton("NO",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                chooseFile();
                            }
                        });
                alertDialog2.show();

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_action, menu);
        action = menu;
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {

            case R.id.menu_home:

                Intent intent = new Intent(UploadPicture.this, MainActivity.class);
                startActivity(intent);

                return true;

            case R.id.menu_upload:

                Intent i = new Intent(UploadPicture.this, TakeUpload.class);
                startActivity(i);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }

    }


    private void chooseFile() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select picture"), 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri filePath = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                profile_image.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }

            AlertDialog.Builder alertDialog2 = new AlertDialog.Builder(
                    UploadPicture.this);
            alertDialog2.setTitle("Confirm to proceed");
            alertDialog2.setMessage("Are you sure this is right image selected");
            alertDialog2.setIcon(R.drawable.ic_warning);
            alertDialog2.setPositiveButton("YES",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            btn_phot_upload.setVisibility(View.GONE);
                            btn_send_answer.setVisibility(View.VISIBLE);
                        }
                    });
            alertDialog2.setNegativeButton("NO",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            chooseFile();
                        }
                    });
            alertDialog2.show();

        }
    }

    @SuppressLint("NotConstructor")
    private void UploadPicture(final String id, final String photo, final String explanation) {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Uploading.....");
progressDialog.show();
//        Animation main_layout = AnimationUtils.loadAnimation(UploadPicture.this, R.anim.anim);
//        btn_phot_upload.setVisibility(View.GONE);
//        btn_phot_upload.setAnimation(main_layout);
//
//        spinKitView.setVisibility(View.VISIBLE);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, urls.UPLOADDRAWINGUSER,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();
                        Log.i("tagconvertstr", "[" + response + "]");
                        try {
                            Log.i("tagconvertstr", "[" + response + "]");
                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("success");
                            if (success.equals("1")) {
//                                spinKitView.setVisibility(View.GONE);
                                Toast.makeText(UploadPicture.this, "Upload Success!", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(UploadPicture.this, UserDraw.class));
                                finish();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            progressDialog.dismiss();
//                            spinKitView.setVisibility(View.GONE);
                            Toast.makeText(UploadPicture.this, "Upload Error!" + e.toString(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
//                spinKitView.setVisibility(View.GONE);
                Toast.makeText(UploadPicture.this, "Upload Errorssss!" + error.toString(), Toast.LENGTH_SHORT).show();

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("photo", photo);
                params.put("user_id", id);
                params.put("from_who", names);
                params.put("sent_question", question_name);
                params.put("sent_qn_id", question_id);
//                params.put("sent_question_explain", question_explain);
                params.put("boss_id", boss_id);
                params.put("boss_name", boss_name);
                params.put("explanation", explanation);

                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    public String getStringImage(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        byte[] imageByteArray = byteArrayOutputStream.toByteArray();
        String encodedImage = Base64.encodeToString(imageByteArray, Base64.DEFAULT);

        return encodedImage;
    }

    public void goback(View view) {
        startActivity(new Intent(getApplicationContext(), DrawUserMain.class));
        finish();
    }
}
