package com.cs407.shoesure;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;

public class IndividualFeature extends AppCompatActivity {
    static final int REQUEST_CAMERA_PERMISSION = 1001;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    private boolean isChecked = false;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_individual_feature);

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("checkbox")) {
            String featureKey = intent.getStringExtra("checkbox");

            findViewById(R.id.BackArrow).setOnClickListener(v -> goBackToFeatures(featureKey));
        }
    }

    public void openCamera(View view) {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.CAMERA}, REQUEST_CAMERA_PERMISSION);
        } else {
            launchCamera();
        }
    }
    private void launchCamera() {
        isChecked=true;
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
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

    private void goBackToFeatures(String featureKey) {
        Intent resultIntent = new Intent();
        resultIntent.putExtra("checkboxKey", featureKey);
        resultIntent.putExtra("checkboxStatus", isChecked);
        setResult(RESULT_OK, resultIntent);
        finish();
    }
}