package com.example.yolov8_detect;

import android.graphics.RectF;

public class Result {
    private final int label;
    private final float score;
    private final RectF rectF;
    private  int index;

    public Result(int label, float score, RectF rectF, int index) {
        this.label = label;
        this.score = score;
        this.rectF = rectF;
        this.index =index;
    }

    public int getLabel() {
        return label;
    }

    public float getScore() {
        return score;
    }

    public RectF getRectF() {
        return rectF;
    }
    public int getIndex() {
        return index;
    }
    public void setIndex(int index) {
        this.index = index;
    }
}
