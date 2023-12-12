package com.cs407.shoesure;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import org.tensorflow.lite.Interpreter;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

public class MLModelHelper {
    private Interpreter interpreter;

    public MLModelHelper(Context context, String modelFilename) {
        try {
            MappedByteBuffer modelBuffer = loadModelFile(context, modelFilename);
            interpreter = new Interpreter(modelBuffer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private MappedByteBuffer loadModelFile(Context context, String modelFilename) throws IOException {
        AssetFileDescriptor fileDescriptor = context.getAssets().openFd(modelFilename);
        FileInputStream inputStream = new FileInputStream(fileDescriptor.getFileDescriptor());
        FileChannel fileChannel = inputStream.getChannel();
        long startOffset = fileDescriptor.getStartOffset();
        long declaredLength = fileDescriptor.getDeclaredLength();
        return fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffset, declaredLength);
    }

    public void runInference(ByteBuffer inputBuffer, float[][] outputArray) {
        interpreter.run(inputBuffer, outputArray);
    }

    public void close() {
        interpreter.close();
    }
}
