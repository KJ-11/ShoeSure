package com.cs407.shoesure;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class IndividualFeature extends AppCompatActivity {

    static final int REQUEST_CAMERA_PERMISSION = 1001;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    String shoeFeature = null;
    private static final String IMAGE_PATH_KEY = "image_path_key";
    private String savedImagePath;
    private static final String PREF_IMAGE_TAKEN = "pref_image_taken";
    private boolean pictureTaken = false;


    private boolean isChecked = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_individual_feature);

        SQLiteDatabase db = new DatabaseHelper(this).getWritableDatabase();
        db.close();


        Intent intent = getIntent();
        shoeFeature = intent.getStringExtra("feature");

        if (intent != null && intent.hasExtra("checkbox")) {
            String featureKey = intent.getStringExtra("checkbox");
            findViewById(R.id.BackArrow).setOnClickListener(v -> goBackToFeatures(featureKey));
        }

        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        pictureTaken = sharedPreferences.getBoolean(PREF_IMAGE_TAKEN, false);
        if (pictureTaken) {
            // Load the image from storage and display it
            String imagePath = loadImagePathFromDatabase();
            if (imagePath != null) {
                Bitmap imageBitmap = loadImageFromPath(imagePath);
                if (imageBitmap != null) {
                    ImageView imageView = findViewById(R.id.imageView);
                    imageView.setImageBitmap(imageBitmap);
                    findViewById(R.id.TakePicButton).setVisibility(View.GONE);
                    isChecked = pictureTaken;
                }
            }
        }
        if (savedInstanceState != null) {
            savedImagePath = savedInstanceState.getString(IMAGE_PATH_KEY);
            if (savedImagePath != null) {
                // Load the image from the savedImagePath and display it
                Bitmap imageBitmap = loadImageFromPath(savedImagePath);
                if (imageBitmap != null) {
                    ImageView imageView = findViewById(R.id.imageView);
                    imageView.setImageBitmap(imageBitmap);
                }
            }
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(IMAGE_PATH_KEY, savedImagePath);
    }

    private Bitmap loadImageFromPath(String imagePath) {
        Bitmap bitmap = null;
        try {
            File imgFile = new File(imagePath);
            if (imgFile.exists()) {
                bitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    private String loadImagePathFromDatabase() {
        String imagePath = null;
        SQLiteDatabase db = new DatabaseHelper(this).getReadableDatabase();
        Cursor cursor = null;

        try {
            cursor = db.rawQuery("SELECT image_path FROM ShoeImages WHERE feature = ?", new String[]{shoeFeature});

            if (cursor != null && cursor.moveToFirst()) {
                imagePath = cursor.getString(cursor.getColumnIndex("image_path"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            db.close();
        }

        return imagePath;
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

            // Save the image to the database
            String imagePath = saveImageToDatabase(imageBitmap);

            // Display the image in an ImageView or another suitable view
            ImageView imageView = findViewById(R.id.imageView); // Replace with your ImageView ID
            imageView.setImageBitmap(imageBitmap);

            findViewById(R.id.TakePicButton).setVisibility(View.GONE);
            pictureTaken = true;

            // Store the state in SharedPreferences
            SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean(PREF_IMAGE_TAKEN, true);
            editor.apply();
        }
    }


    private String saveImageToDatabase(Bitmap imageBitmap) {
        // Code to save the image to storage and get the image path
        String imagePath = saveImageToFile(imageBitmap);

        // Save the image path to the database
        if (imagePath != null) {
            savedImagePath = imagePath; // Save the image path

            SQLiteDatabase db = new DatabaseHelper(this).getWritableDatabase();

            ContentValues values = new ContentValues();
            values.put("image_path", imagePath);

            values.put("feature", shoeFeature);

            db.insert("ShoeImages", null, values);
            db.close();
        }

        return imagePath;
    }

    private String saveImageToFile(Bitmap imageBitmap) {
        // Get the directory for the app's private pictures directory.
        File directory = getExternalFilesDir(Environment.DIRECTORY_PICTURES);

        // Create a unique file name using a timestamp
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File imageFile = null;
        try {
            // Create the image file in the specified directory
            imageFile = File.createTempFile(imageFileName, ".jpg", directory);

            // Save the bitmap to the created file
            FileOutputStream outputStream = new FileOutputStream(imageFile);
            imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (imageFile != null) {
            // Return the absolute path of the image file
            return imageFile.getAbsolutePath();
        } else {
            return null;
        }
    }

    private void goBackToFeatures(String featureKey) {
        testDatabase();

        Intent resultIntent = new Intent();
        resultIntent.putExtra("checkboxKey", featureKey);
        resultIntent.putExtra("checkboxStatus", isChecked);
        setResult(RESULT_OK, resultIntent);
        finish();
    }


    private void testDatabase() {
        SQLiteDatabase db = new DatabaseHelper(this).getReadableDatabase();
        Cursor cursor = null;

        try {
            cursor = db.rawQuery("SELECT * FROM ShoeImages", null);

            if (cursor != null && cursor.moveToFirst()) {
                do {
                    String imagePath = cursor.getString(cursor.getColumnIndex("image_path"));
                    String feature = cursor.getString(cursor.getColumnIndex("feature"));
                    // Log the data
                    Log.i("DatabaseData", "Image Path: " + imagePath + ", Feature: " + feature);
                } while (cursor.moveToNext());
            } else {
                // No data in the database or cursor is null
                Log.i("DatabaseData", "No data in the database");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            db.close();
        }
    }
}