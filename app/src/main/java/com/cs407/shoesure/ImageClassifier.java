package com.cs407.shoesure;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

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
            predictionScoresList.add(predictions[0][0]);
        }

        float sum = 0.0f;
        for (float score : predictionScoresList) {
            sum += score;
        }

        float averageScore = sum / modelHelpers.size();

        return averageScore;
    }
}
