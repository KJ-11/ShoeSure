package com.cs407.shoesure;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;

import java.io.File;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.List;

public class ImageToBuffer {

    public List<ByteBuffer> loadImageAsByteBuffer(List<String> imagePaths) {
        List<ByteBuffer> byteBufferList = new ArrayList<>();

        for (String imagePath : imagePaths) {
            Bitmap bitmap = null;
            try {
                File imgFile = new File(imagePath);
                if (imgFile.exists()) {
                    bitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                    //bitmap = preprocessImage(bitmap);
                    //bitmap = Bitmap.createScaledBitmap(bitmap, 224, 224, true);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            //int imageSize = 224 * 224 * 4 * 3; //bitmap.getRowBytes() * bitmap.getHeight();
            ByteBuffer byteBuffer = ByteBuffer.allocateDirect(602112);
            byteBuffer.order(ByteOrder.nativeOrder());

            bitmap.copyPixelsToBuffer(byteBuffer);
            byteBuffer.rewind();

            byteBufferList.add(byteBuffer);
        }

        return byteBufferList;
    }

    /*
    private Bitmap preprocessImage(Bitmap bitmap) {
        // Apply rescale factor
        bitmap = rescaleBitmap(bitmap, 1.0f / 255.0f);

        // Apply shear and zoom transformations
        bitmap = applyShearAndZoom(bitmap);

        return bitmap;
    }
    */

    /*
    private Bitmap rescaleBitmap(Bitmap bitmap, float scale) {
        Matrix matrix = new Matrix();
        matrix.postScale(scale, scale);
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
    }


    private Bitmap applyShearAndZoom(Bitmap bitmap) {
        // Apply shear transformation
        Matrix matrix = new Matrix();
        matrix.postRotate(30); // Rotate by 30 degrees (adjust as needed)
        matrix.postScale(1.0f, 1.0f - 0.2f);
        bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);

        // Apply zoom transformation
        matrix = new Matrix();
        matrix.postScale(1.0f + 0.2f, 1.0f + 0.2f);
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
    }

    */

}
