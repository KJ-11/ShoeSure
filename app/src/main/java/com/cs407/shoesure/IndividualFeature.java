package com.cs407.shoesure;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;

public class IndividualFeature extends AppCompatActivity {

    private boolean isChecked = false;
    private String checkboxKey;
    private CheckBox checkBox;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_individual_feature);

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("checkbox")) {
            String featureKey = intent.getStringExtra("checkbox");

            findViewById(R.id.imageView2).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    goBackToFeatures(featureKey);
                }
            });
        }
    }



    static final int REQUEST_CAMERA_PERMISSION = 1001;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    public void openCamera(View view){
        isChecked = true;

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        //startActivity(intent);
        //if (intent.resolveActivity(getPackageManager()) != null) {

        //}
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            Log.i("Info", "Ask For Camera Perms");
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.CAMERA}, REQUEST_CAMERA_PERMISSION);
        }
        Log.i("Info", "Open Cam");
        startActivity(intent);

    }






    public void setIsChecked(boolean isChecked) {
        this.isChecked = isChecked;
    }




    private void goBackToFeatures(String featureKey) {

        Intent resultIntent = new Intent();
        resultIntent.putExtra("checkboxKey", featureKey);
        resultIntent.putExtra("checkboxStatus", isChecked);
        setResult(RESULT_OK, resultIntent);
        finish();
    }
}

