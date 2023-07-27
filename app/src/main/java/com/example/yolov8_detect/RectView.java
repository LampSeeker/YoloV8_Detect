package com.example.yolov8_detect;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import androidx.annotation.Nullable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class RectView extends View {
    private Rect[] rects;
    private Map<Integer, Map<RectF, String>> resultMap; // Paint 객체를 따로 저장하지 않고, 라벨과 정보만 저장합니다.

    // 텍스트를 그리기 위한 Paint 변수
    private Paint textPaint;
    private Paint emptyBoxPaint;

    public RectView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        rects = new Rect[0];
        resultMap = new HashMap<>();

        textPaint = new Paint();
        textPaint.setColor(Color.WHITE);
        textPaint.setTextSize(50.0f);
    }

    public void addRectToObject(int left, int top, int right, int bottom) {
        Rect newRect = new Rect(left, top, right, bottom);
        Rect[] newRects = Arrays.copyOf(rects, rects.length + 1);
        newRects[rects.length] = newRect;
        rects = newRects;
        invalidate();
    }

    private String[] labels;
    public void setLabels(String[] labels) {
        this.labels = labels;
    }

    public String[] getLabels() {
        return labels;
    }
    // rectF 비율 수정
    public ArrayList<Result> transFormRect(ArrayList<Result> resultArrayList) {
        // 핸드폰의 기종에 따라 PreviewView의 크기는 변한다.
        float scaleX = getWidth() / (float) SupportOnnx.INPUT_SIZE;
        float scaleY = scaleX * 9f / 16f;
        float realY = getWidth() * 9f / 16f;
        float diffY = realY - getHeight();

        ArrayList<Result> transformedResults = new ArrayList<>();

        for (Result result : resultArrayList) {
            RectF transformedRectF = new RectF(result.getRectF());

            transformedRectF.left *= scaleX;
            transformedRectF.right *= scaleX;
            transformedRectF.top = transformedRectF.top * scaleY - (diffY / 2f);
            transformedRectF.bottom = transformedRectF.bottom * scaleY - (diffY / 2f);

            Result transformedResult = new Result(result.getLabel(), result.getScore(), transformedRectF);
            transformedResults.add(transformedResult);
        }
        return transformedResults;
    }
    // 초기화
    public void clear() {
        resultMap.clear();
    }
    public void resultToList(ArrayList<Result> results) {
        resultMap.clear(); // 이전 정보를 모두 지우고 새로운 결과를 저장합니다.
        for (Result result : results) {
            RectF rectF = result.getRectF();
            int label = result.getLabel();
            String labelInfo = labels[label] + ", " + Math.round(result.getScore() * 100) + "%";
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                resultMap.computeIfAbsent(label, k -> new HashMap<>()).put(rectF, labelInfo);
            }
        }
    }
    // 라벨에 따른 색상을 반환하는 메서드
    private Paint getPaintByLabel(int label) {
        Paint labelPaint = new Paint();
        switch (label) {
            // ... 각 라벨에 따른 색상 지정
            case 0:
                labelPaint.setColor(Color.CYAN);
                break;
            case 1:
                labelPaint.setColor(Color.BLUE);
                break;
            case 2:
                labelPaint.setColor(Color.GREEN);
                break;
            case 3:
                labelPaint.setColor(Color.MAGENTA);
                break;
            case 4:
                labelPaint.setColor(Color.LTGRAY);
                break;
            case 5:
                labelPaint.setColor(Color.YELLOW);
                break;
            case 6:
                labelPaint.setColor(Color.BLACK);
                break;
            case 7:
                labelPaint.setColor(Color.GRAY);
                break;
            case 8:
                labelPaint.setColor(Color.DKGRAY);
                break;
            case 9:
                labelPaint.setColor(Color.CYAN);
                break;
            case 10:
                labelPaint.setColor(Color.BLUE);
                break;
            case 11:
                labelPaint.setColor(Color.GREEN);
                break;
            case 12:
                labelPaint.setColor(Color.MAGENTA);
                break;
            case 13:
                labelPaint.setColor(Color.LTGRAY);
                break;
            case 14:
                labelPaint.setColor(Color.YELLOW);
                break;
            case 15:
                labelPaint.setColor(Color.BLACK);
                break;
            case 16:
                labelPaint.setColor(Color.GRAY);
                break;
            case 17:
                labelPaint.setColor(Color.DKGRAY);
                break;
            case 18:
                labelPaint.setColor(Color.CYAN);
                break;
            case 19:
                labelPaint.setColor(Color.BLUE);
                break;
            case 20:
                labelPaint.setColor(Color.GREEN);
                break;
            case 21:
                labelPaint.setColor(Color.MAGENTA);
                break;
            case 22:
                labelPaint.setColor(Color.LTGRAY);
                break;
            case 23:
                labelPaint.setColor(Color.YELLOW);
                break;
            case 24:
                labelPaint.setColor(Color.BLACK);
                break;
            case 25:
                labelPaint.setColor(Color.GRAY);
                break;
            case 26:
                labelPaint.setColor(Color.DKGRAY);
                break;
            case 27:
                labelPaint.setColor(Color.MAGENTA);
                break;
            case 28:
                labelPaint.setColor(Color.LTGRAY);
                break;
        }
        return labelPaint;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        // 모든 맵에 대해 반복하면서 맵의 내용을 그립니다.
        for (Map.Entry<Integer, Map<RectF, String>> entry : resultMap.entrySet()) {
            int label = entry.getKey();
            Map<RectF, String> rectMap = entry.getValue();
            Paint boxPaint = getPaintByLabel(label); // 바운딩 박스 색상 가져오기

            for (Map.Entry<RectF, String> rectEntry : rectMap.entrySet()) {
                RectF rect = rectEntry.getKey();
                String labelInfo = rectEntry.getValue();
                // 외곽선 상자를 그리기 위해 새로운 Paint 객체 생성
                Paint outlineBoxPaint = new Paint();
                outlineBoxPaint.setStyle(Paint.Style.STROKE);
                outlineBoxPaint.setStrokeWidth(5);
                outlineBoxPaint.setColor(boxPaint.getColor()); // 바운딩 박스와 동일한 색상 설정
                // 바운딩 박스를 외곽선 스타일로 그리도록 수정
                canvas.drawRect(rect, outlineBoxPaint); // 외곽선 상자를 그리는 Paint로 변경
                canvas.drawText(labelInfo, rect.left + 10.0f, rect.top + 60.0f, textPaint);
            }
        }
        super.onDraw(canvas);
    }
}
