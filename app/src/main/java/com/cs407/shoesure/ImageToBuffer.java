package com.cs407.shoesure;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

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
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            int imageSize = bitmap.getRowBytes() * bitmap.getHeight();
            ByteBuffer byteBuffer = ByteBuffer.allocateDirect(602112);
            byteBuffer.order(ByteOrder.nativeOrder());

            bitmap.copyPixelsToBuffer(byteBuffer);
            byteBuffer.rewind();

            byteBufferList.add(byteBuffer);
        }

        return byteBufferList;
    }
}
