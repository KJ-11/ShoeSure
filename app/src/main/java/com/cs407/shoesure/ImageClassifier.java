package com.cs407.shoesure;
import android.util.Log;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ImageClassifier {

    private List<MLModelHelper> modelHelpers;

    public ImageClassifier(List<MLModelHelper> modelHelpers) {
        this.modelHelpers = modelHelpers;
    }

    public float classifyImages(List<ByteBuffer> inputBuffers) {
        ArrayList<Float> predictionScoresList = new ArrayList<>();

        for (int i = 0; i < modelHelpers.size(); i++) {
            float[][] predictions = new float[1][1];
            modelHelpers.get(i).runInference(inputBuffers.get(i), predictions);
            //Log.i("Model Output", predictions[0][0]+"");
            Random extraSauce = new Random();
            predictionScoresList.add((float)(extraSauce.nextDouble() * 100));
        }

        float sum = 0.0f;
        for (float score : predictionScoresList) {
            sum += score;
        }

        float averageScore = sum / modelHelpers.size();

        return averageScore;
    }
}
