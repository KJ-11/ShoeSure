package com.cs407.shoesure;


import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Button;
import android.widget.Toast;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class features extends AppCompatActivity {
    private SharedPreferences sharedPreferences;
    private CheckBox checkBoxLogo;
    private CheckBox checkBoxSole;
    private CheckBox checkBoxBox;
    private CheckBox checkBoxEntireShoe;
    private Button verifyButton;

    private CheckBox checkBoxInnerLabel;
    private CheckBox checkBoxInsole;
    private static final int INDIV_FEATURE_REQUEST_CODE = 101;

    // ML Model Variables
    private ImageToBuffer bufferHelper;
    private MLModelHelper modelHelperLabel;
    private MLModelHelper modelHelperSole;
    private MLModelHelper modelHelperBox;
    private MLModelHelper modelHelperShoe;
    private MLModelHelper modelHelperInnerLabel;
    private MLModelHelper modelHelperBackLogo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_features);


        checkBoxLogo = findViewById(R.id.checkBox8);
        checkBoxSole = findViewById(R.id.checkBox9);
        checkBoxBox = findViewById(R.id.checkBox10);
        checkBoxInnerLabel = findViewById(R.id.checkBox11);
        checkBoxEntireShoe = findViewById(R.id.checkBox12);
        checkBoxInsole = findViewById(R.id.checkBox13);
        verifyButton = findViewById(R.id.verifyB);


        checkBoxLogo.setChecked(false);
        checkBoxSole.setChecked(false);
        checkBoxBox.setChecked(false);
        checkBoxInnerLabel.setChecked(false);
        checkBoxEntireShoe.setChecked(false);
        checkBoxInsole.setChecked(false);



        setTextViewClickListener(R.id.textView, "logoChecked", "Logo");
        setTextViewClickListener(R.id.textView2, "soleChecked", "Sole");
        setTextViewClickListener(R.id.textView3, "boxChecked", "Box");
        setTextViewClickListener(R.id.textView4, "innerLabelChecked", "Inner Label");
        setTextViewClickListener(R.id.textView5, "entireShoeChecked", "Wide View");
        setTextViewClickListener(R.id.textView6, "insoleChecked", "Insole");


    }


    private void setTextViewClickListener(int textViewId, final String checkboxKey, String featureName) {
        TextView textView = findViewById(textViewId);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                goToIndividualFeature(checkboxKey, featureName);


            }
        });
    }

    private void goToIndividualFeature(String checkboxKey, String featureName) {
        Intent intent = new Intent(this, IndividualFeature.class);
        intent.putExtra("checkbox", checkboxKey);
        intent.putExtra("feature", featureName);
        startActivityForResult(intent, INDIV_FEATURE_REQUEST_CODE);
    }

    public void backHome(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void goBack(View view) {
        Intent intent = new Intent(this, ShoeListActivity.class);
        startActivity(intent);
    }

    public void VerifyClick(View view) {

        if (!areAllCheckboxesChecked()) {
            Toast.makeText(this, "Please upload all images to check all boxes first", Toast.LENGTH_SHORT).show();
        } else {
            float averageScore = MLModelDeployment();
            //new DatabaseHelper(this).clearDatabase();
            Intent intent = new Intent(this, authenticityPage.class);
            intent.putExtra("score", averageScore);
            startActivity(intent);
        }
    }
    private boolean areAllCheckboxesChecked() {

        return checkBoxLogo.isChecked() &&
                checkBoxSole.isChecked() &&
                checkBoxBox.isChecked() &&
                checkBoxInnerLabel.isChecked() &&
                checkBoxEntireShoe.isChecked() &&
                checkBoxInsole.isChecked();

    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == INDIV_FEATURE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {

                String checkboxKey = data.getStringExtra("checkboxKey");
                boolean isChecked = data.getBooleanExtra("checkboxStatus", false);
                updateCheckbox(checkboxKey, isChecked);
            }
        }
    }

    private void updateCheckbox(String checkboxKey, boolean isChecked) {
        switch (checkboxKey) {
            case "logoChecked":
                checkBoxLogo.setChecked(isChecked);
                break;
            case "soleChecked":
                checkBoxSole.setChecked(isChecked);
                break;
            case "boxChecked":
                checkBoxBox.setChecked(isChecked);
                break;
            case "entireShoeChecked":
                checkBoxEntireShoe.setChecked(isChecked);
                break;
            case "innerLabelChecked":
                checkBoxInnerLabel.setChecked(isChecked);
                break;
            case "insoleChecked":
                checkBoxInsole.setChecked(isChecked);
                break;

        }
    }

    public float MLModelDeployment() {
        List<String> imagePaths = new ArrayList<>();

        SQLiteDatabase db = new DatabaseHelper(this).getReadableDatabase();
        Cursor cursor = null;
        try {
            cursor = db.rawQuery("SELECT * FROM ShoeImages", null);

            if (cursor != null && cursor.moveToFirst()) {
                do {
                    String imagePath = cursor.getString(cursor.getColumnIndex("image_path"));
                    String feature = cursor.getString(cursor.getColumnIndex("feature"));
                    if (feature.equals("Logo")) {
                        imagePaths.add(0, imagePath);
                    } if (feature.equals("Sole")) {
                        imagePaths.add(1, imagePath);
                    } if (feature.equals("Box")) {
                        imagePaths.add(2, imagePath);
                    } if (feature.equals("Inner Label")) {
                        imagePaths.add(3, imagePath);
                    } if (feature.equals("Wide View")) {
                        imagePaths.add(4, imagePath);
                    } if (feature.equals("Insole")) {
                        imagePaths.add(5, imagePath);
                    }
                } while (cursor.moveToNext());
            } else {
                Log.i("DB Error", "No data in the database");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            db.close();
        }

        bufferHelper = new ImageToBuffer();
        List<ByteBuffer> inputBuffers = bufferHelper.loadImageAsByteBuffer(imagePaths);

        modelHelperLabel = new MLModelHelper(this, "model_label.tflite");
        modelHelperSole = new MLModelHelper(this, "model_sole.tflite");
        modelHelperBox = new MLModelHelper(this, "model_box.tflite");
        modelHelperShoe = new MLModelHelper(this, "model_shoe.tflite");
        modelHelperInnerLabel = new MLModelHelper(this, "model_inner_label.tflite");
        modelHelperBackLogo = new MLModelHelper(this, "model_back_logo.tflite");

        ImageClassifier imageClassifier = new ImageClassifier(Arrays.asList(modelHelperLabel,
                modelHelperSole, modelHelperBox, modelHelperShoe, modelHelperInnerLabel,
                modelHelperBackLogo));

        float averageScore = imageClassifier.classifyImages(inputBuffers);
        return averageScore;
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (modelHelperLabel != null) {
            modelHelperLabel.close();
        }
        if (modelHelperSole != null) {
            modelHelperSole.close();
        }
        if (modelHelperBox != null) {
            modelHelperBox.close();
        }
        if (modelHelperShoe != null) {
            modelHelperShoe.close();
        }
        if (modelHelperInnerLabel != null) {
            modelHelperInnerLabel.close();
        }
        if (modelHelperBackLogo != null) {
            modelHelperBackLogo.close();
        }
    }
}