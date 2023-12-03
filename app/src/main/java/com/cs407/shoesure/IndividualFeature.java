package com.cs407.shoesure;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.hardware.camera2.CameraMetadata;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import android.widget.CheckBox;

public class IndividualFeature extends AppCompatActivity {

    private boolean isChecked = false;
    private String checkboxKey;
    private CheckBox checkBox;

    static final int REQUEST_CAMERA_PERMISSION = 1001;
    static final int REQUEST_IMAGE_CAPTURE = 1;

>>>>>>>>> Temporary merge branch 2
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_individual_feature);

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("checkbox")) {
            String featureKey = intent.getStringExtra("checkbox");

            findViewById(R.id.BackArrow).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    goBackToFeatures(featureKey);
                }
            });
        }
    }

<<<<<<<<< Temporary merge branch 1

=========



    public void openCamera(View view){

            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.CAMERA}, REQUEST_CAMERA_PERMISSION);
            } else {
                launchCamera();
            }
        }

>>>>>>>>> Temporary merge branch 2


    private void launchCamera() {
            isChecked = true;
            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CAMERA_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                launchCamera();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            // Get the captured image and display it
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");

            // Display the image in an ImageView or another suitable view
            ImageView imageView = findViewById(R.id.imageView); // Replace with your ImageView ID
            imageView.setImageBitmap(imageBitmap);

        }
    }

<<<<<<<<< Temporary merge branch 1

=========





    public void setIsChecked(boolean isChecked) {
        this.isChecked = isChecked;
    }




    private void goBackToFeatures(String featureKey) {

        Intent resultIntent = new Intent();
        resultIntent.putExtra("checkboxKey", featureKey);
        resultIntent.putExtra("checkboxStatus", isChecked);
        setResult(RESULT_OK, resultIntent);
        finish();


>>>>>>>>> Temporary merge branch 2
    }
}

