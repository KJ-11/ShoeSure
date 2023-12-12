package com.cs407.shoesure;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import java.text.DecimalFormat;


public class authenticityPage extends AppCompatActivity {

    private static final DecimalFormat df = new DecimalFormat("0.00");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authenticity_page);

        Intent intent = getIntent();
        float averageScore = intent.getFloatExtra("score", 0.0f);
        TextView authenticityScore = (TextView) findViewById(R.id.percent);
        authenticityScore.setText("" + df.format(averageScore) + "%");
    }

    public void backHome(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}