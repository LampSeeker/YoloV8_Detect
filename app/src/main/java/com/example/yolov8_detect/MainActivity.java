package com.example.yolov8_detect;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.core.AspectRatio;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageAnalysis;
import androidx.camera.core.ImageProxy;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;

import com.google.common.util.concurrent.ListenableFuture;

import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;

import ai.onnxruntime.OnnxTensor;
import ai.onnxruntime.OrtEnvironment;
import ai.onnxruntime.OrtException;
import ai.onnxruntime.OrtSession;



public class MainActivity extends AppCompatActivity {
    private ProcessCameraProvider processCameraProvider;
    private PreviewView previewView;
    private RectView rectView;
    private SupportOnnx supportOnnx;
    private OrtEnvironment ortEnvironment;
    private OrtSession ortSession;

    private boolean isOverlayVisible = false; // 반투명 레이아웃 보이기/숨기기 상태를 나타내는 변수

    private FrameLayout overlayLayout;
    //mainactivity에서 사용이 가능하도록 전역변수 선언
    private ArrayList<Result> resultsList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        previewView = findViewById(R.id.previewView);
        rectView = findViewById(R.id.rectView);
        overlayLayout = findViewById(R.id.overlayLayout);
        overlayLayout.setVisibility(View.GONE);

        //자동꺼짐 해제
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        //권한 확인
        permissionCheck();

        //Onnx 처리 지원 객체
        supportOnnx = new SupportOnnx(this);


        //모델 불러오기
        load();

        //카메라 빌드
        setCamera();

        //카메라 켜기
        startCamera();

        // 반투명 레이아웃 참조
        overlayLayout = findViewById(R.id.overlayLayout);

        // "물체 정보 보기" 버튼 클릭 이벤트 처리
        Button showInfoButton = findViewById(R.id.showInfoButton);
        showInfoButton.setOnClickListener(v -> {
            if (isOverlayVisible) {
                // 반투명 레이아웃을 숨김
                overlayLayout.setVisibility(View.GONE);
            } else {
                // 반투명 레이아웃을 보임
                overlayLayout.setVisibility(View.VISIBLE);
                //showObjectInfo(resultsList);
            }
            isOverlayVisible = !isOverlayVisible; // 상태를 반전시킴
        });

        //출력 결과 띄우기

    }



    public void permissionCheck() {
        PermissionSupport permissionSupport = new PermissionSupport(this, this);
        permissionSupport.checkPermissions();
    }

    public void load() {
        //model, label 불러오기
        supportOnnx.loadModel();
        supportOnnx.loadLabel();
        try {
            //onnxRuntime 활성화
            ortEnvironment = OrtEnvironment.getEnvironment();
            ortSession = ortEnvironment.createSession(this.getFilesDir().getAbsolutePath() + "/" + SupportOnnx.fileName,
                    new OrtSession.SessionOptions());
        } catch (OrtException e) {
            e.printStackTrace();
        }
    }

    public void setCamera() {
        try {
            ListenableFuture<ProcessCameraProvider> cameraProviderListenableFuture = ProcessCameraProvider.getInstance(this);
            processCameraProvider = cameraProviderListenableFuture.get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void startCamera() {
        //화면 중앙
        previewView.setScaleType(PreviewView.ScaleType.FILL_CENTER);
        //후면 카메라
        CameraSelector cameraSelector = new CameraSelector.Builder()
                .requireLensFacing(CameraSelector.LENS_FACING_BACK)
                .build();

        Preview preview = new Preview.Builder().setTargetAspectRatio(AspectRatio.RATIO_16_9).build();

        preview.setSurfaceProvider(previewView.getSurfaceProvider());
        //이미지 분석 빌드
        ImageAnalysis imageAnalysis = new ImageAnalysis.Builder()
                .setTargetAspectRatio(AspectRatio.RATIO_16_9)
                .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST).build();

        //label 정보 전달
        rectView.setLabels(supportOnnx.getLabels());

        //분석
        imageAnalysis.setAnalyzer(Executors.newSingleThreadExecutor(), imageProxy -> {
            imageProcessing(imageProxy);
            imageProxy.close();
        });

        //생명 주기 설정
        processCameraProvider.bindToLifecycle(this, cameraSelector, preview, imageAnalysis);
    }

    @SuppressLint("UnsafeOptInUsageError")
    public void imageProcessing(ImageProxy imageProxy) {
        if (ortSession != null) { // ortSession이 초기화되었는지 확인
            // 나머지 imageProcessing() 메서드의 내용은 그대로 유지
            Image image = imageProxy.getImage();
            if (image != null) {
                // image -> bitmap
                Bitmap bitmap = supportOnnx.imageToBitmap(image);
                Bitmap bitmap_640 = supportOnnx.rescaleBitmap(bitmap);
                // bitmap -> float buffer
                FloatBuffer imgDataFloat = supportOnnx.bitmapToFloatBuffer(bitmap_640);

                //모델명
                String inputName = ortSession.getInputNames().iterator().next();
                //모델의 요구 입력값
                long[] shape = {SupportOnnx.BATCH_SIZE, SupportOnnx.PIXEL_SIZE, SupportOnnx.INPUT_SIZE, SupportOnnx.INPUT_SIZE};

                try {
                    // float buffer -> tensor
                    OnnxTensor inputTensor = OnnxTensor.createTensor(ortEnvironment, imgDataFloat, shape);
                    // 추론
                    OrtSession.Result result = ortSession.run(Collections.singletonMap(inputName, inputTensor));
                    // 결과 (v8 의 출력은 [1][xywh + label 의 개수][8400] 입니다.
                    float[][][] output = (float[][][]) result.get(0).getValue();

                    int rows = output[0][0].length; //8400
                    // tensor -> label, score, rectF
                    resultsList = supportOnnx.outputsToNMSPredictions(output, rows);

                    // rectF 를 보이는 화면의 비율에 맞게 수정
                    resultsList = rectView.transFormRect(resultsList);

                    // Result(label, score, rectF) -> 화면에 출력
                    rectView.clear();
                    rectView.resultToList(resultsList);
                    rectView.invalidate();
                    // UI 수정은 메인 스레드에서만 가능하므로 runOnUiThread로 메인 스레드에 전달
                    runOnUiThread(() -> {
                        if(isOverlayVisible){
                            showObjectInfo(resultsList);
                        }
                    });
                } catch (OrtException e) {
                    e.printStackTrace();
                }
            }
        }

    }
    private void showObjectInfo(ArrayList<Result> resultsList) {
        TableLayout tableLayout = findViewById(R.id.tableLayout);
        int childCount = tableLayout.getChildCount();
        // 테이블이 이미 차 있는 경우 테이블 초기화
        if (childCount > 1) {
            // 헤더 행을 제외한 나머지 행들을 삭제
            tableLayout.removeViews(1, childCount - 1);
        }
        if (resultsList != null || !resultsList.isEmpty()) {

            // 결과 데이터(label/확률/좌표값)를 테이블에 추가해서 출력
            for (Result result : resultsList) {
                TableRow tableRow = new TableRow(this);

                // Add label and score to the row
                TextView labelTextView = new TextView(this);
                labelTextView.setText(String.valueOf(result.getLabel()));
                tableRow.addView(labelTextView);

                TextView scoreTextView = new TextView(this);
                scoreTextView.setText(String.format("%.2f", result.getScore()));
                tableRow.addView(scoreTextView);

                TextView RectTextView = new TextView(this);
                scoreTextView.setText(result.getRectF().toString());
                tableRow.addView(RectTextView);


                // Add the tableRow to the tableLayout
                tableLayout.addView(tableRow);
            }
        }

    }

    @Override
    protected void onStop() {
        try {
            ortSession.endProfiling();
        } catch (OrtException e) {
            e.printStackTrace();
        }
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        try {
            ortSession.close();
            ortEnvironment.close();
        } catch (OrtException e) {
            e.printStackTrace();
        }
        super.onDestroy();
    }
}