package com.example.yolov8_detect;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class RectView extends View {
    private final Map<RectF, String> fireMap = new HashMap<>();
    private final Paint firePaint = new Paint();
    private final Map<RectF, String> smokeMap = new HashMap<>();
    private final Paint smokePaint = new Paint();
    private final Map<RectF, String> T2map = new HashMap<>();
    private final Paint T2paint = new Paint();
    private final Map<RectF, String> T3map = new HashMap<>();
    private final Paint T3paint = new Paint();
    private final Map<RectF, String> T4map = new HashMap<>();
    private final Paint T4paint = new Paint();
    private final Map<RectF, String> T5map = new HashMap<>();
    private final Paint T5paint = new Paint();
    private final Map<RectF, String> T6map = new HashMap<>();
    private final Paint T6paint = new Paint();
    private final Map<RectF, String> T7map = new HashMap<>();
    private final Paint T7paint = new Paint();
    private final Map<RectF, String> T8map = new HashMap<>();
    private final Paint T8paint = new Paint();
    private final Map<RectF, String> T9map = new HashMap<>();
    private final Paint T9paint = new Paint();
    private final Map<RectF, String> T10map = new HashMap<>();
    private final Paint T10paint = new Paint();
    private final Map<RectF, String> T11map = new HashMap<>();
    private final Paint T11paint = new Paint();
    private final Map<RectF, String> T12map = new HashMap<>();
    private final Paint T12paint = new Paint();
    private final Map<RectF, String> T13map = new HashMap<>();
    private final Paint T13paint = new Paint();
    private final Map<RectF, String> T14map = new HashMap<>();
    private final Paint T14paint = new Paint();
    private final Map<RectF, String> T15map = new HashMap<>();
    private final Paint T15paint = new Paint();
    private final Map<RectF, String> T16map = new HashMap<>();
    private final Paint T16paint = new Paint();
    private final Map<RectF, String> T17map = new HashMap<>();
    private final Paint T17paint = new Paint();
    private final Map<RectF, String> T18map = new HashMap<>();
    private final Paint T18paint = new Paint();
    private final Map<RectF, String> T19map = new HashMap<>();
    private final Paint T19paint = new Paint();
    private final Map<RectF, String> T20map = new HashMap<>();
    private final Paint T20paint = new Paint();
    private final Map<RectF, String> T21map = new HashMap<>();
    private final Paint T21paint = new Paint();
    private final Map<RectF, String> T22map = new HashMap<>();
    private final Paint T22paint = new Paint();
    private final Map<RectF, String> T23map = new HashMap<>();
    private final Paint T23paint = new Paint();
    private final Map<RectF, String> T24map = new HashMap<>();
    private final Paint T24paint = new Paint();
    private final Map<RectF, String> T25map = new HashMap<>();
    private final Paint T25paint = new Paint();
    private final Map<RectF, String> T26map = new HashMap<>();
    private final Paint T26paint = new Paint();
    private final Map<RectF, String> T27map = new HashMap<>();
    private final Paint T27paint = new Paint();
    private final Map<RectF, String> T28map = new HashMap<>();
    private final Paint T28paint = new Paint();
    private final Paint textPaint = new Paint();

    private String[] labels;

    public RectView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        firePaint.setStyle(Paint.Style.STROKE);     //빈 사각형 그림
        firePaint.setStrokeWidth(10.0f);            //굵기 10
        firePaint.setColor(Color.WHITE);              //검은색
        firePaint.setStrokeCap(Paint.Cap.ROUND);    //끝을 뭉특하게
        firePaint.setStrokeJoin(Paint.Join.ROUND);  //끝 주위도 뭉특하게
        firePaint.setStrokeMiter(100);              //뭉특한 정도 100도

        smokePaint.setStyle(Paint.Style.STROKE);
        smokePaint.setStrokeWidth(10.0f);
        smokePaint.setColor(Color.WHITE);
        smokePaint.setStrokeCap(Paint.Cap.ROUND);
        smokePaint.setStrokeJoin(Paint.Join.ROUND);
        smokePaint.setStrokeMiter(100);

        T2paint.setStyle(Paint.Style.STROKE);
        T2paint.setStrokeWidth(10.0f);
        T2paint.setColor(Color.WHITE);
        T2paint.setStrokeCap(Paint.Cap.ROUND);
        T2paint.setStrokeJoin(Paint.Join.ROUND);
        T2paint.setStrokeMiter(100);

        T3paint.setStyle(Paint.Style.STROKE);
        T3paint.setStrokeWidth(10.0f);
        T3paint.setColor(Color.GREEN);
        T3paint.setStrokeCap(Paint.Cap.ROUND);
        T3paint.setStrokeJoin(Paint.Join.ROUND);
        T3paint.setStrokeMiter(100);

        T4paint.setStyle(Paint.Style.STROKE);
        T4paint.setStrokeWidth(10.0f);
        T4paint.setColor(Color.MAGENTA);
        T4paint.setStrokeCap(Paint.Cap.ROUND);
        T4paint.setStrokeJoin(Paint.Join.ROUND);
        T4paint.setStrokeMiter(100);

        T5paint.setStyle(Paint.Style.STROKE);
        T5paint.setStrokeWidth(10.0f);
        T5paint.setColor(Color.CYAN);
        T5paint.setStrokeCap(Paint.Cap.ROUND);
        T5paint.setStrokeJoin(Paint.Join.ROUND);
        T5paint.setStrokeMiter(100);

        T6paint.setStyle(Paint.Style.STROKE);
        T6paint.setStrokeWidth(10.0f);
        T6paint.setColor(Color.YELLOW);
        T6paint.setStrokeCap(Paint.Cap.ROUND);
        T6paint.setStrokeJoin(Paint.Join.ROUND);
        T6paint.setStrokeMiter(100);

        T7paint.setStyle(Paint.Style.STROKE);
        T7paint.setStrokeWidth(10.0f);
        T7paint.setColor(Color.BLACK);
        T7paint.setStrokeCap(Paint.Cap.ROUND);
        T7paint.setStrokeJoin(Paint.Join.ROUND);
        T7paint.setStrokeMiter(100);

        T8paint.setStyle(Paint.Style.STROKE);
        T8paint.setStrokeWidth(10.0f);
        T8paint.setColor(Color.CYAN);
        T8paint.setStrokeCap(Paint.Cap.ROUND);
        T8paint.setStrokeJoin(Paint.Join.ROUND);
        T8paint.setStrokeMiter(100);

        T9paint.setStyle(Paint.Style.STROKE);
        T9paint.setStrokeWidth(10.0f);
        T9paint.setColor(Color.CYAN);
        T9paint.setStrokeCap(Paint.Cap.ROUND);
        T9paint.setStrokeJoin(Paint.Join.ROUND);
        T9paint.setStrokeMiter(100);

        T10paint.setStyle(Paint.Style.STROKE);
        T10paint.setStrokeWidth(10.0f);
        T10paint.setColor(Color.CYAN);
        T10paint.setStrokeCap(Paint.Cap.ROUND);
        T10paint.setStrokeJoin(Paint.Join.ROUND);
        T10paint.setStrokeMiter(100);

        T11paint.setStyle(Paint.Style.STROKE);
        T11paint.setStrokeWidth(10.0f);
        T11paint.setColor(Color.CYAN);
        T11paint.setStrokeCap(Paint.Cap.ROUND);
        T11paint.setStrokeJoin(Paint.Join.ROUND);
        T11paint.setStrokeMiter(100);

        T12paint.setStyle(Paint.Style.STROKE);
        T12paint.setStrokeWidth(10.0f);
        T12paint.setColor(Color.CYAN);
        T12paint.setStrokeCap(Paint.Cap.ROUND);
        T12paint.setStrokeJoin(Paint.Join.ROUND);
        T12paint.setStrokeMiter(100);

        T13paint.setStyle(Paint.Style.STROKE);
        T13paint.setStrokeWidth(10.0f);
        T13paint.setColor(Color.CYAN);
        T13paint.setStrokeCap(Paint.Cap.ROUND);
        T13paint.setStrokeJoin(Paint.Join.ROUND);
        T13paint.setStrokeMiter(100);

        T14paint.setStyle(Paint.Style.STROKE);
        T14paint.setStrokeWidth(10.0f);
        T14paint.setColor(Color.CYAN);
        T14paint.setStrokeCap(Paint.Cap.ROUND);
        T14paint.setStrokeJoin(Paint.Join.ROUND);
        T14paint.setStrokeMiter(100);

        T15paint.setStyle(Paint.Style.STROKE);
        T15paint.setStrokeWidth(10.0f);
        T15paint.setColor(Color.CYAN);
        T15paint.setStrokeCap(Paint.Cap.ROUND);
        T15paint.setStrokeJoin(Paint.Join.ROUND);
        T15paint.setStrokeMiter(100);

        T16paint.setStyle(Paint.Style.STROKE);
        T16paint.setStrokeWidth(10.0f);
        T16paint.setColor(Color.CYAN);
        T16paint.setStrokeCap(Paint.Cap.ROUND);
        T16paint.setStrokeJoin(Paint.Join.ROUND);
        T16paint.setStrokeMiter(100);

        T17paint.setStyle(Paint.Style.STROKE);
        T17paint.setStrokeWidth(10.0f);
        T17paint.setColor(Color.CYAN);
        T17paint.setStrokeCap(Paint.Cap.ROUND);
        T17paint.setStrokeJoin(Paint.Join.ROUND);
        T17paint.setStrokeMiter(100);

        T18paint.setStyle(Paint.Style.STROKE);
        T18paint.setStrokeWidth(10.0f);
        T18paint.setColor(Color.CYAN);
        T18paint.setStrokeCap(Paint.Cap.ROUND);
        T18paint.setStrokeJoin(Paint.Join.ROUND);
        T18paint.setStrokeMiter(100);

        T19paint.setStyle(Paint.Style.STROKE);
        T19paint.setStrokeWidth(10.0f);
        T19paint.setColor(Color.CYAN);
        T19paint.setStrokeCap(Paint.Cap.ROUND);
        T19paint.setStrokeJoin(Paint.Join.ROUND);
        T19paint.setStrokeMiter(100);

        T20paint.setStyle(Paint.Style.STROKE);
        T20paint.setStrokeWidth(10.0f);
        T20paint.setColor(Color.CYAN);
        T20paint.setStrokeCap(Paint.Cap.ROUND);
        T20paint.setStrokeJoin(Paint.Join.ROUND);
        T20paint.setStrokeMiter(100);

        T21paint.setStyle(Paint.Style.STROKE);
        T21paint.setStrokeWidth(10.0f);
        T21paint.setColor(Color.CYAN);
        T21paint.setStrokeCap(Paint.Cap.ROUND);
        T21paint.setStrokeJoin(Paint.Join.ROUND);
        T21paint.setStrokeMiter(100);

        T22paint.setStyle(Paint.Style.STROKE);
        T22paint.setStrokeWidth(10.0f);
        T22paint.setColor(Color.CYAN);
        T22paint.setStrokeCap(Paint.Cap.ROUND);
        T22paint.setStrokeJoin(Paint.Join.ROUND);
        T22paint.setStrokeMiter(100);

        T23paint.setStyle(Paint.Style.STROKE);
        T23paint.setStrokeWidth(10.0f);
        T23paint.setColor(Color.CYAN);
        T23paint.setStrokeCap(Paint.Cap.ROUND);
        T23paint.setStrokeJoin(Paint.Join.ROUND);
        T23paint.setStrokeMiter(100);

        T24paint.setStyle(Paint.Style.STROKE);
        T24paint.setStrokeWidth(10.0f);
        T24paint.setColor(Color.CYAN);
        T24paint.setStrokeCap(Paint.Cap.ROUND);
        T24paint.setStrokeJoin(Paint.Join.ROUND);
        T24paint.setStrokeMiter(100);

        T25paint.setStyle(Paint.Style.STROKE);
        T25paint.setStrokeWidth(10.0f);
        T25paint.setColor(Color.CYAN);
        T25paint.setStrokeCap(Paint.Cap.ROUND);
        T25paint.setStrokeJoin(Paint.Join.ROUND);
        T25paint.setStrokeMiter(100);

        T26paint.setStyle(Paint.Style.STROKE);
        T26paint.setStrokeWidth(10.0f);
        T26paint.setColor(Color.CYAN);
        T26paint.setStrokeCap(Paint.Cap.ROUND);
        T26paint.setStrokeJoin(Paint.Join.ROUND);
        T26paint.setStrokeMiter(100);

        T27paint.setStyle(Paint.Style.STROKE);
        T27paint.setStrokeWidth(10.0f);
        T27paint.setColor(Color.CYAN);
        T27paint.setStrokeCap(Paint.Cap.ROUND);
        T27paint.setStrokeJoin(Paint.Join.ROUND);
        T27paint.setStrokeMiter(100);

        T28paint.setStyle(Paint.Style.STROKE);
        T28paint.setStrokeWidth(10.0f);
        T28paint.setColor(Color.CYAN);
        T28paint.setStrokeCap(Paint.Cap.ROUND);
        T28paint.setStrokeJoin(Paint.Join.ROUND);
        T28paint.setStrokeMiter(100);

        textPaint.setTextSize(60.0f);
        textPaint.setColor(Color.WHITE);
    }

    public void setLabels(String[] labels) {
        this.labels = labels;
    }

    // rectF 비율 수정
    public ArrayList<Result> transFormRect(ArrayList<Result> resultArrayList) {
        //핸드폰의 기종에 따라 PreviewView 의 크기는 변한다.
        float scaleX = getWidth() / (float) SupportOnnx.INPUT_SIZE;
        // float scaleY = getHeight() / (float) SupportOnnx.INPUT_SIZE;
        float scaleY = scaleX * 9f / 16f;
        float realY = getWidth() * 9f / 16f;
        float diffY = realY - getHeight();

        for (Result result : resultArrayList) {
            result.getRectF().left *= scaleX;
            result.getRectF().right *= scaleX;
            result.getRectF().top = result.getRectF().top * scaleY - (diffY / 2f);
            result.getRectF().bottom = result.getRectF().bottom * scaleY - (diffY / 2f);
        }
        return resultArrayList;
    }

    //초기화
    public void clear() {
        fireMap.clear();
        smokeMap.clear();
        T2map.clear();
        T3map.clear();
        T4map.clear();
        T5map.clear();
        T6map.clear();
        T7map.clear();
        T8map.clear();
        T9map.clear();
        T10map.clear();
        T11map.clear();
        T12map.clear();
        T13map.clear();
        T14map.clear();
        T15map.clear();
        T16map.clear();
        T17map.clear();
        T18map.clear();
        T19map.clear();
        T20map.clear();
        T21map.clear();
        T22map.clear();
        T23map.clear();
        T24map.clear();
        T25map.clear();
        T26map.clear();
        T27map.clear();
        T28map.clear();
    }

    // Result -> 각각의 해시맵 (fireMap, smokeMap)
    public void resultToList(ArrayList<Result> results) {
        for (Result result : results) {
            if (result.getLabel() == 0) { // 종이
                fireMap.put(result.getRectF(), labels[0] + ", " + Math.round(result.getScore() * 100) + "%");
            } else if (result.getLabel() == 1) { // 종이팩
                smokeMap.put(result.getRectF(), labels[1] + ", " + Math.round(result.getScore() * 100) + "%");
            } else if (result.getLabel() == 2) { // 종이컵
                T2map.put(result.getRectF(), labels[2] + ", " + Math.round(result.getScore() * 100) + "%");
            }
            else if (result.getLabel() == 3) { // 캔류
                T3map.put(result.getRectF(), labels[3] + ", " + Math.round(result.getScore() * 100) + "%");
            }
            else if (result.getLabel() == 4) { // 재사용유리
                T4map.put(result.getRectF(), labels[4] + ", " + Math.round(result.getScore() * 100) + "%");
            }
            else if (result.getLabel() == 5) { // 갈색유리
                T5map.put(result.getRectF(), labels[5] + ", " + Math.round(result.getScore() * 100) + "%");
            }
            else if (result.getLabel() == 6) { // 녹색유리
                T6map.put(result.getRectF(), labels[6] + ", " + Math.round(result.getScore() * 100) + "%");
            }
            else if (result.getLabel() == 7) { // 백색유리
                T7map.put(result.getRectF(), labels[7] + ", " + Math.round(result.getScore() * 100) + "%");
            }
            else if (result.getLabel() == 8) { // 기타유리
                T8map.put(result.getRectF(), labels[8] + ", " + Math.round(result.getScore() * 100) + "%");
            }
            else if (result.getLabel() == 9) { // 페트병
                T9map.put(result.getRectF(), labels[9] + ", " + Math.round(result.getScore() * 100) + "%");
            }
            else if (result.getLabel() == 10) { // 플라스틱
                T10map.put(result.getRectF(), labels[10] + ", " + Math.round(result.getScore() * 100) + "%");
            }
            else if (result.getLabel() == 11) { // 비닐
                T11map.put(result.getRectF(), labels[11] + ", " + Math.round(result.getScore() * 100) + "%");
            }
            else if (result.getLabel() == 12) { // 종이+이물질
                T12map.put(result.getRectF(), labels[12] + ", " + Math.round(result.getScore() * 100) + "%");
            }
            else if (result.getLabel() == 13) { // 종이컵+이물질
                T13map.put(result.getRectF(), labels[13] + ", " + Math.round(result.getScore() * 100) + "%");
            }
            else if (result.getLabel() == 14) { // 캔+이물질
                T14map.put(result.getRectF(), labels[14] + ", " + Math.round(result.getScore() * 100) + "%");
            }
            else if (result.getLabel() == 15) { // 기타유리+이물질
                T15map.put(result.getRectF(), labels[15] + ", " + Math.round(result.getScore() * 100) + "%");
            }
            else if (result.getLabel() == 16) { // 페트+이물질+다중포장재
                T16map.put(result.getRectF(), labels[16] + ", " + Math.round(result.getScore() * 100) + "%");
            }
            else if (result.getLabel() == 17) { // 페트+이물질
                T17map.put(result.getRectF(), labels[17] + ", " + Math.round(result.getScore() * 100) + "%");
            }
            else if (result.getLabel() == 18) { // 플라스틱+이물질
                T18map.put(result.getRectF(), labels[18] + ", " + Math.round(result.getScore() * 100) + "%");
            }
            else if (result.getLabel() == 19) { // 비닐+이물질
                T19map.put(result.getRectF(), labels[19] + ", " + Math.round(result.getScore() * 100) + "%");
            }
            else if (result.getLabel() == 20) { // 재사용유리
                T20map.put(result.getRectF(), labels[20] + ", " + Math.round(result.getScore() * 100) + "%");
            }
            else if (result.getLabel() == 21) { // 갈색유리+다중포장재
                T21map.put(result.getRectF(), labels[21] + ", " + Math.round(result.getScore() * 100) + "%");
            }
            else if (result.getLabel() == 22) { // 녹색유리+다중포장재
                T22map.put(result.getRectF(), labels[22] + ", " + Math.round(result.getScore() * 100) + "%");
            }
            else if (result.getLabel() == 23) { // 백색유리+다중포장재
                T23map.put(result.getRectF(), labels[23] + ", " + Math.round(result.getScore() * 100) + "%");
            }
            else if (result.getLabel() == 24) { // 페트+다중포장재
                T24map.put(result.getRectF(), labels[24] + ", " + Math.round(result.getScore() * 100) + "%");
            }
            else if (result.getLabel() == 25) { // 흰색 스티로폼
                T25map.put(result.getRectF(), labels[25] + ", " + Math.round(result.getScore() * 100) + "%");
            }
            else if (result.getLabel() == 26) { // 컬러 스티로폼
                T26map.put(result.getRectF(), labels[26] + ", " + Math.round(result.getScore() * 100) + "%");
            }
            else if (result.getLabel() == 27) { // 스티로폼+이물질
                T27map.put(result.getRectF(), labels[27] + ", " + Math.round(result.getScore() * 100) + "%");
            }
            else if (result.getLabel() == 28) { // 건전지
                T28map.put(result.getRectF(), labels[28] + ", " + Math.round(result.getScore() * 100) + "%");
            }
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        // fire(HashMap) -> canvas
        for (Map.Entry<RectF, String> fire : fireMap.entrySet()) {
            canvas.drawRect(fire.getKey(), firePaint);
            canvas.drawText(fire.getValue(), fire.getKey().left + 10.0f, fire.getKey().top + 60.0f, textPaint);
        }
        // smoke(HashMap) -> canvas
        for (Map.Entry<RectF, String> smoke : smokeMap.entrySet()) {
            canvas.drawRect(smoke.getKey(), smokePaint);
            canvas.drawText(smoke.getValue(), smoke.getKey().left + 10.0f, smoke.getKey().top + 60.0f, textPaint);
        }
        // T2map(HashMap) -> canvas
        for (Map.Entry<RectF, String> t2map : T2map.entrySet()) {
            canvas.drawRect(t2map.getKey(), T2paint);
            canvas.drawText(t2map.getValue(), t2map.getKey().left + 10.0f, t2map.getKey().top + 60.0f, textPaint);
        }
        // T3map(HashMap) -> canvas
        for (Map.Entry<RectF, String> t3map : T3map.entrySet()) {
            canvas.drawRect(t3map.getKey(), T3paint);
            canvas.drawText(t3map.getValue(), t3map.getKey().left + 10.0f, t3map.getKey().top + 60.0f, textPaint);
        }
        // T4map(HashMap) -> canvas
        for (Map.Entry<RectF, String> t4map : T4map.entrySet()) {
            canvas.drawRect(t4map.getKey(), T4paint);
            canvas.drawText(t4map.getValue(), t4map.getKey().left + 10.0f, t4map.getKey().top + 60.0f, textPaint);
        }
        // T5map(HashMap) -> canvas
        for (Map.Entry<RectF, String> t5map : T5map.entrySet()) {
            canvas.drawRect(t5map.getKey(), T5paint);
            canvas.drawText(t5map.getValue(), t5map.getKey().left + 10.0f, t5map.getKey().top + 60.0f, textPaint);
        }
        for (Map.Entry<RectF, String> t6map : T6map.entrySet()) {
            canvas.drawRect(t6map.getKey(), T6paint);
            canvas.drawText(t6map.getValue(), t6map.getKey().left + 10.0f, t6map.getKey().top + 60.0f, textPaint);
        }
        for (Map.Entry<RectF, String> t7map : T7map.entrySet()) {
            canvas.drawRect(t7map.getKey(), T7paint);
            canvas.drawText(t7map.getValue(), t7map.getKey().left + 10.0f, t7map.getKey().top + 60.0f, textPaint);
        }
        for (Map.Entry<RectF, String> t8map : T8map.entrySet()) {
            canvas.drawRect(t8map.getKey(), T8paint);
            canvas.drawText(t8map.getValue(), t8map.getKey().left + 10.0f, t8map.getKey().top + 60.0f, textPaint);
        }
        for (Map.Entry<RectF, String> t9map : T9map.entrySet()) {
            canvas.drawRect(t9map.getKey(), T9paint);
            canvas.drawText(t9map.getValue(), t9map.getKey().left + 10.0f, t9map.getKey().top + 60.0f, textPaint);
        }
        for (Map.Entry<RectF, String> t10map : T10map.entrySet()) {
            canvas.drawRect(t10map.getKey(), T10paint);
            canvas.drawText(t10map.getValue(), t10map.getKey().left + 10.0f, t10map.getKey().top + 60.0f, textPaint);
        }
        for (Map.Entry<RectF, String> t11map : T11map.entrySet()) {
            canvas.drawRect(t11map.getKey(), T11paint);
            canvas.drawText(t11map.getValue(), t11map.getKey().left + 10.0f, t11map.getKey().top + 60.0f, textPaint);
        }
        for (Map.Entry<RectF, String> t12map : T12map.entrySet()) {
            canvas.drawRect(t12map.getKey(), T12paint);
            canvas.drawText(t12map.getValue(), t12map.getKey().left + 10.0f, t12map.getKey().top + 60.0f, textPaint);
        }
        for (Map.Entry<RectF, String> t13map : T13map.entrySet()) {
            canvas.drawRect(t13map.getKey(), T13paint);
            canvas.drawText(t13map.getValue(), t13map.getKey().left + 10.0f, t13map.getKey().top + 60.0f, textPaint);
        }
        for (Map.Entry<RectF, String> t14map : T14map.entrySet()) {
            canvas.drawRect(t14map.getKey(), T14paint);
            canvas.drawText(t14map.getValue(), t14map.getKey().left + 10.0f, t14map.getKey().top + 60.0f, textPaint);
        }
        for (Map.Entry<RectF, String> t15map : T15map.entrySet()) {
            canvas.drawRect(t15map.getKey(), T15paint);
            canvas.drawText(t15map.getValue(), t15map.getKey().left + 10.0f, t15map.getKey().top + 60.0f, textPaint);
        }
        for (Map.Entry<RectF, String> t16map : T16map.entrySet()) {
            canvas.drawRect(t16map.getKey(), T16paint);
            canvas.drawText(t16map.getValue(), t16map.getKey().left + 10.0f, t16map.getKey().top + 60.0f, textPaint);
        }
        for (Map.Entry<RectF, String> t17map : T17map.entrySet()) {
            canvas.drawRect(t17map.getKey(), T17paint);
            canvas.drawText(t17map.getValue(), t17map.getKey().left + 10.0f, t17map.getKey().top + 60.0f, textPaint);
        }
        for (Map.Entry<RectF, String> t18map : T18map.entrySet()) {
            canvas.drawRect(t18map.getKey(), T18paint);
            canvas.drawText(t18map.getValue(), t18map.getKey().left + 10.0f, t18map.getKey().top + 60.0f, textPaint);
        }
        for (Map.Entry<RectF, String> t19map : T19map.entrySet()) {
            canvas.drawRect(t19map.getKey(), T19paint);
            canvas.drawText(t19map.getValue(), t19map.getKey().left + 10.0f, t19map.getKey().top + 60.0f, textPaint);
        }
        for (Map.Entry<RectF, String> t20map : T20map.entrySet()) {
            canvas.drawRect(t20map.getKey(), T20paint);
            canvas.drawText(t20map.getValue(), t20map.getKey().left + 10.0f, t20map.getKey().top + 60.0f, textPaint);
        }
        for (Map.Entry<RectF, String> t21map : T21map.entrySet()) {
            canvas.drawRect(t21map.getKey(), T21paint);
            canvas.drawText(t21map.getValue(), t21map.getKey().left + 10.0f, t21map.getKey().top + 60.0f, textPaint);
        }
        for (Map.Entry<RectF, String> t22map : T22map.entrySet()) {
            canvas.drawRect(t22map.getKey(), T22paint);
            canvas.drawText(t22map.getValue(), t22map.getKey().left + 10.0f, t22map.getKey().top + 60.0f, textPaint);
        }
        for (Map.Entry<RectF, String> t23map : T23map.entrySet()) {
            canvas.drawRect(t23map.getKey(), T23paint);
            canvas.drawText(t23map.getValue(), t23map.getKey().left + 10.0f, t23map.getKey().top + 60.0f, textPaint);
        }
        for (Map.Entry<RectF, String> t24map : T24map.entrySet()) {
            canvas.drawRect(t24map.getKey(), T24paint);
            canvas.drawText(t24map.getValue(), t24map.getKey().left + 10.0f, t24map.getKey().top + 60.0f, textPaint);
        }
        for (Map.Entry<RectF, String> t25map : T25map.entrySet()) {
            canvas.drawRect(t25map.getKey(), T25paint);
            canvas.drawText(t25map.getValue(), t25map.getKey().left + 10.0f, t25map.getKey().top + 60.0f, textPaint);
        }
        for (Map.Entry<RectF, String> t26map : T26map.entrySet()) {
            canvas.drawRect(t26map.getKey(), T26paint);
            canvas.drawText(t26map.getValue(), t26map.getKey().left + 10.0f, t26map.getKey().top + 60.0f, textPaint);
        }
        for (Map.Entry<RectF, String> t27map : T27map.entrySet()) {
            canvas.drawRect(t27map.getKey(), T27paint);
            canvas.drawText(t27map.getValue(), t27map.getKey().left + 10.0f, t27map.getKey().top + 60.0f, textPaint);
        }
        for (Map.Entry<RectF, String> t28map : T28map.entrySet()) {
            canvas.drawRect(t28map.getKey(), T28paint);
            canvas.drawText(t28map.getValue(), t28map.getKey().left + 10.0f, t28map.getKey().top + 60.0f, textPaint);
        }
        super.onDraw(canvas);
    }
}
