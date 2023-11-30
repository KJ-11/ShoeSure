package com.cs407.shoesure;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

public class features extends AppCompatActivity {
    private SharedPreferences sharedPreferences;
    private CheckBox checkBoxLogo;
    private CheckBox checkBoxSole;
    private CheckBox checkBoxBox;
    private CheckBox checkBoxEntireShoe;

    private CheckBox checkBoxInnerLabel;
    private CheckBox checkBoxInsole;
    private static final int INDIV_FEATURE_REQUEST_CODE = 101;



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

        checkBoxLogo.setChecked(false);
        checkBoxSole.setChecked(false);
        checkBoxBox.setChecked(false);
        checkBoxInnerLabel.setChecked(false);
        checkBoxEntireShoe.setChecked(false);
        checkBoxInsole.setChecked(false);

        setTextViewClickListener(R.id.textView, "logoChecked");
        setTextViewClickListener(R.id.textView2, "soleChecked");
        setTextViewClickListener(R.id.textView3, "boxChecked");
        setTextViewClickListener(R.id.textView4, "innerLabelChecked");
        setTextViewClickListener(R.id.textView5, "entireShoeChecked");
        setTextViewClickListener(R.id.textView6, "insoleChecked");

    }
    private void setTextViewClickListener(int textViewId, final String checkboxKey) {
        TextView textView = findViewById(textViewId);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToIndividualFeature(checkboxKey);
            }
        });
    }
    private void goToIndividualFeature(String checkboxKey) {
        Intent intent = new Intent(this, IndividualFeature.class);
        intent.putExtra("checkbox", checkboxKey);
        startActivityForResult(intent, INDIV_FEATURE_REQUEST_CODE);
    }

    public void backHome(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }


    public void VerifyClick(View view) {
        Intent intent = new Intent(this, authenticityPage.class);
        startActivity(intent);
    }

    public void goIndivFeatures(View view) {
        Intent intent = new Intent(this, IndividualFeature.class);
        startActivity(intent);
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
}