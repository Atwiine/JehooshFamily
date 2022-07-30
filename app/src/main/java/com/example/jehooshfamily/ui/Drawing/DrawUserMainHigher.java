package com.example.jehooshfamily.ui.Drawing;

import android.Manifest;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.EmbossMaskFilter;
import android.graphics.MaskFilter;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.jehooshfamily.R;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class DrawUserMainHigher extends AppCompatActivity implements ColorPickerDialog.OnColorChangedListener {
    private static final int COLOR_MENU_ID = Menu.FIRST;
    private static final int EMBOSS_MENU_ID = Menu.FIRST + 1;
    private static final int BLUR_MENU_ID = Menu.FIRST + 2;
    private static final int ERASE_MENU_ID = Menu.FIRST + 3;
    private static final int SRCATOP_MENU_ID = Menu.FIRST + 4;
    private static final int Save = Menu.FIRST + 5;
    MyView mv;
    AlertDialog dialog;
    Bitmap b;
    ImageView iv;
    private Paint mPaint;
    private MaskFilter mEmboss;
    private MaskFilter mBlur;
    String question_name, question_id,question_explain;
EditText explain_draw_input;
    final private int REQUEST_CODE_ASK_PERMISSIONS = 123;
    Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_draw_user_main);

        question_id = getIntent().getStringExtra("question_id");
        question_name = getIntent().getStringExtra("question");

        iv = findViewById(R.id.iv);
        explain_draw_input = findViewById(R.id.explain_draw_input);

        mv = new MyView(this);
        mv.setDrawingCacheEnabled(true);
        mv.setBackgroundResource(R.drawable.bg_canvas);//set the back ground if you wish to
        setContentView(mv);
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        mPaint.setColor(0xFFFF0000);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeJoin(Paint.Join.ROUND);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setStrokeWidth(8);
        mEmboss = new EmbossMaskFilter(new float[]{1, 1, 1},
                0.4f, 6, 3.5f);
        mBlur = new BlurMaskFilter(8, BlurMaskFilter.Blur.NORMAL);

        requestPermission();
    }

    @Override
    protected void onResume() {
        super.onResume();
        iv.setImageBitmap(null);
    }

    @Override
    public void onBackPressed() {
        iv.setImageBitmap(null);
//        mv.setBackgroundResource(R.drawable.bg_canvas);
        super.onBackPressed();
    }

    public void colorChanged(int color) {
        mPaint.setColor(color);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);

        menu.add(0, COLOR_MENU_ID, 0, "Color").setShortcut('3', 'c');
        menu.add(0, EMBOSS_MENU_ID, 0, "Emboss").setShortcut('4', 's');
        menu.add(0, BLUR_MENU_ID, 0, "Blur").setShortcut('5', 'z');
        menu.add(0, ERASE_MENU_ID, 0, "Erase").setShortcut('5', 'z');
        menu.add(0, SRCATOP_MENU_ID, 0, "SrcATop").setShortcut('5', 'z');
        menu.add(0, Save, 0, "Save").setShortcut('5', 'z');

        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        return true;
    }


    private void requestPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ) {
            ActivityCompat
                    .requestPermissions(DrawUserMainHigher.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CODE_ASK_PERMISSIONS);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_ASK_PERMISSIONS:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission Granted
                    Toast.makeText(DrawUserMainHigher.this, "Permission Granted", Toast.LENGTH_SHORT)
                            .show();
                } else {
                    // Permission Denied
                    Toast.makeText(DrawUserMainHigher.this, "Permission Denied", Toast.LENGTH_SHORT)
                            .show();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        mPaint.setXfermode(null);
        mPaint.setAlpha(0xFF);

        switch (item.getItemId()) {
            case COLOR_MENU_ID:
                new ColorPickerDialog(this, this, mPaint.getColor()).show();
                return true;
            case EMBOSS_MENU_ID:
                if (mPaint.getMaskFilter() != mEmboss) {
                    mPaint.setMaskFilter(mEmboss);
                } else {
                    mPaint.setMaskFilter(null);
                }
                return true;
            case BLUR_MENU_ID:
                if (mPaint.getMaskFilter() != mBlur) {
                    mPaint.setMaskFilter(mBlur);
                } else {
                    mPaint.setMaskFilter(null);
                }
                return true;
            case ERASE_MENU_ID:
                mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
                mPaint.setAlpha(0x80);
                return true;
            case SRCATOP_MENU_ID:

                mPaint.setXfermode(new PorterDuffXfermode(
                        PorterDuff.Mode.SRC_ATOP));
                mPaint.setAlpha(0x80);
                return true;
            case Save:
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                    AlertDialog.Builder editalert = new AlertDialog.Builder(DrawUserMainHigher.this);
                    editalert.setTitle("Please Entersss the name with which you want to Save");
                    final EditText input = new EditText(DrawUserMainHigher.this);
                    LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.FILL_PARENT,
                            LinearLayout.LayoutParams.FILL_PARENT);
                    input.setLayoutParams(lp);
                    editalert.setView(input);
                    editalert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {

                            final String name = input.getText().toString();
                            boolean saved;
                            OutputStream fos = null;
                            Bitmap bitmap = mv.getDrawingCache();
                            iv.setImageBitmap(bitmap);
//so see if i can make an other activity to handle the higher android versions

                            //check to see if the name of the pic is set
                            if (name.isEmpty()) {
                                Toast.makeText(DrawUserMainHigher.this, "image name is required", Toast.LENGTH_SHORT).show();
                            } else {


/**FOR HIGHER VERSIONS */
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
//                                    ContentResolver resolver = this.getContentResolver();
                                    ContentValues contentValues = new ContentValues();
                                    contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, name);
                                    contentValues.put(MediaStore.MediaColumns.MIME_TYPE, "image/png");
                                    contentValues.put(MediaStore.MediaColumns.RELATIVE_PATH, "DCIM/" + "Jehoash Family");
                                    Uri imageUri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
                                    try {
                                        fos = getContentResolver().openOutputStream(imageUri);
//                                        Toast.makeText(DrawUserMainHigher.this, name + " " + DrawUserMainHigher.this.getResources().getString(R.string.str_save_image_text), Toast.LENGTH_LONG).show();
                                        Intent sendimage = new Intent(DrawUserMainHigher.this, UploadPicture.class);
                                        sendimage.putExtra("question_id", question_id);
                                        sendimage.putExtra("question", question_name);
                                        startActivity(sendimage);
                                    } catch (FileNotFoundException e) {
                                        e.printStackTrace();
                                    }
                                } else {
                                    String imagesDir = Environment.getExternalStoragePublicDirectory(
                                            Environment.DIRECTORY_PICTURES).toString() + File.separator + "Jehoash Family";

                                    File file = new File(imagesDir);

                                    if (!file.exists()) {
                                        file.mkdir();
                                    }

                                    File image = new File(imagesDir, name + ".png");
                                    try {
                                        fos = new FileOutputStream(image);
//                                        Toast.makeText(DrawUserMain.this, name + " " + DrawUserMain.this.getResources().getString(R.string.str_save_image_text), Toast.LENGTH_LONG).show();
                                        Intent sendimage = new Intent(DrawUserMainHigher.this, UploadPicture.class);
                                        sendimage.putExtra("question_id", question_id);
                                        sendimage.putExtra("question", question_name);
                                        startActivity(sendimage);
                                    } catch (FileNotFoundException e) {
                                        e.printStackTrace();
                                    }

                                }

                                saved = bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
                                try {
                                    fos.flush();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                try {
                                    fos.close();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }

                            }
                        }
                    });

                    editalert.show();
                } else {
                    // Request permission from the user
                    ActivityCompat.requestPermissions(this,
                            new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);

                }


                return true;
        }
        return super.onOptionsItemSelected(item);
    }



    public class MyView extends View {

        private static final float MINP = 0.25f;
        private static final float MAXP = 0.75f;
        private static final float TOUCH_TOLERANCE = 4;
        Context context;
        private Bitmap mBitmap;
        private Canvas mCanvas;
        private Path mPath;
        private Paint mBitmapPaint;
        private float mX, mY;

        public MyView(Context c) {
            super(c);
            context = c;
            mPath = new Path();
            mBitmapPaint = new Paint(Paint.DITHER_FLAG);

        }

        @Override
        protected void onSizeChanged(int w, int h, int oldw, int oldh) {
            super.onSizeChanged(w, h, oldw, oldh);
            mBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
            mCanvas = new Canvas(mBitmap);

        }

        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);

            canvas.drawBitmap(mBitmap, 0, 0, mBitmapPaint);
            canvas.drawPath(mPath, mPaint);
        }

        private void touch_start(float x, float y) {
            //showDialog();
            mPath.reset();
            mPath.moveTo(x, y);
            mX = x;
            mY = y;

        }

        private void touch_move(float x, float y) {
            float dx = Math.abs(x - mX);
            float dy = Math.abs(y - mY);
            if (dx >= TOUCH_TOLERANCE || dy >= TOUCH_TOLERANCE) {
                mPath.quadTo(mX, mY, (x + mX) / 2, (y + mY) / 2);
                mX = x;
                mY = y;
            }
        }

        private void touch_up() {
            mPath.lineTo(mX, mY);
            // commit the path to our offscreen
            mCanvas.drawPath(mPath, mPaint);
            // kill this so we don't double draw
            mPath.reset();
            mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SCREEN));
            //mPaint.setMaskFilter(null);
        }

        @Override
        public boolean onTouchEvent(MotionEvent event) {
            float x = event.getX();
            float y = event.getY();

            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    touch_start(x, y);
                    invalidate();
                    break;
                case MotionEvent.ACTION_MOVE:

                    touch_move(x, y);
                    invalidate();
                    break;
                case MotionEvent.ACTION_UP:
                    touch_up();
                    invalidate();
                    break;
            }
            return true;
        }
    }
}