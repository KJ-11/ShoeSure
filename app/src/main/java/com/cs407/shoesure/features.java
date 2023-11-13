package com.cs407.shoesure;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;

public class features extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_features);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.menu_drawer,menu);
        return true;
    }

    public void VerifyClick(View view) {
        Intent intent = new Intent(this, authenticityPage.class);
        startActivity(intent);
    }

    public void goIndivFeatures(View view) {
        Intent intent = new Intent(this, IndividualFeature.class);
        startActivity(intent);
    }
}