package com.example.yolov8_detect;

import android.annotation.SuppressLint;

import android.graphics.Bitmap;

import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.core.AspectRatio;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageAnalysis;
import androidx.camera.core.ImageProxy;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.core.content.ContextCompat;

import com.google.common.util.concurrent.ListenableFuture;


import java.io.IOException;
import java.io.InputStream;
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

    private ImageAnalysis imageAnalysis;

    private boolean isImageAnalysisEnabled = true;
    private boolean isPreviewPaused = false;

    private boolean isOverlayVisible = false; // 반투명 레이아웃 보이기/숨기기 상태를 나타내는 변수
    private FrameLayout overlayLayout;

    private ArrayList<Result> resultsList = new ArrayList<>();

    private String[] labels;

    //재활용 사진 관리 배열
    private Bitmap[] recycle_method_images;


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
        //재활용 방법 이미지 불러오기
        loadRecycleImages();
        //카메라 빌드
        setCamera();

        //카메라 켜기
        startCamera();

        //label naem 가져오기
        labels=rectView.getLabels();

        // 반투명 레이아웃 참조
        overlayLayout = findViewById(R.id.overlayLayout);

        // "물체 정보 보기" 버튼 클릭 이벤트 처리
        Button showInfoButton = findViewById(R.id.showInfoButton);
        showInfoButton.setOnClickListener(v -> {
            if (isOverlayVisible) {
                // 반투명 레이아웃을 숨김
                overlayLayout.setVisibility(View.GONE);
                // 이미지 분석 다시 시작
                enableImageAnalysis();
                // 프리뷰 화면 재개
                resumePreview();
            } else {
                // 반투명 레이아웃을 보임
                overlayLayout.setVisibility(View.VISIBLE);
                // 이미지 분석 중지
                disableImageAnalysis();
                // 프리뷰 화면 일시정지
                pausePreview();
            }
            isOverlayVisible = !isOverlayVisible; // 상태를 반전시킴
        });

        //출력 결과 띄우기

    }

    private void loadRecycleImages() {
        try {
            // AssetManager를 이용해 assets 폴더에 있는 recycle_method 디렉토리 내의 파일들을 가져옴
            String[] imageFiles = getAssets().list("recycle_method");
            // 비트맵 배열 초기화
            recycle_method_images = new Bitmap[imageFiles.length];

            // 각 파일들을 비트맵으로 변환하여 배열에 저장
            for (int i = 0; i < imageFiles.length; i++) {
                InputStream inputStream = getAssets().open("recycle_method/" + imageFiles[i]);

                recycle_method_images[i] = BitmapFactory.decodeStream(inputStream);
                // logcat에서 불러온 이미지 파일 개수 확인
                Log.d("LoadRecycleImages", "Number of images loaded: " + imageFiles.length);

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showImageDialog(Bitmap imageBitmap) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.dialog_image, null);
        builder.setView(view);

        ImageView imageView = view.findViewById(R.id.imageView);
        imageView.setImageBitmap(imageBitmap);

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    //스냅샷 기능 구현 위해 imageAnalysis를 전역변수로 두고 on/off 기능 추가
    private void enableImageAnalysis() {
        if (!isImageAnalysisEnabled) {
            // 이미지 분석 활성화
            imageAnalysis.setAnalyzer(Executors.newSingleThreadExecutor(), this::imageProcessing);
            isImageAnalysisEnabled = true;
        }
    }
    private void disableImageAnalysis() {
        if (isImageAnalysisEnabled) {
            // 이미지 분석 비활성화
            // 이미지 분석 비활성화 상태에서는 아무 작업도 수행하지 않음
            imageAnalysis.setAnalyzer(Executors.newSingleThreadExecutor(), ImageProxy::close);
            isImageAnalysisEnabled = false;
        }
    }
    private void pausePreview() {
        if (!isPreviewPaused && processCameraProvider != null) {
            // 프리뷰 화면 일시정지
            processCameraProvider.unbindAll();
            isPreviewPaused = true;
        }
    }

    private void resumePreview() {
        if (isPreviewPaused && processCameraProvider != null) {
            // 프리뷰 화면 재개
            startCamera();
            isPreviewPaused = false;
        }
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
        imageAnalysis = new ImageAnalysis.Builder()
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
        if (isImageAnalysisEnabled && ortSession != null) { // ortSession이 초기화되었는지 확인
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
        if (childCount > 1) {
            // 헤더 행을 제외한 나머지 행들을 삭제
            tableLayout.removeViews(1, childCount - 1);
        }
        if (resultsList != null || !resultsList.isEmpty())  {

            // 결과 데이터를 테이블에 추가
            for (int i = 0; i < resultsList.size(); i++) { // 여기서 i 변수를 선언 및 초기화
                Result result = resultsList.get(i);
                TableRow tableRow = new TableRow(this);

                // Add label and score to the row
                TextView labelTextView = new TextView(this);
                labelTextView.setText(String.valueOf(labels[result.getLabel()]));
                labelTextView.setTextSize(24);
                tableRow.addView(labelTextView);

                TextView indexTextView = new TextView(this);
                indexTextView.setText(String.valueOf(result.getIndex()));
                indexTextView.setTextSize(24);
                tableRow.addView(indexTextView);

                TextView recycleTextView = new TextView(this);
                if (result.getLabel() >= 0 && result.getLabel() <= 11) {
                    recycleTextView.setText("재활용");
                    recycleTextView.setTextColor(Color.GREEN);
                } else {
                    recycleTextView.setText("일반쓰레기");
                    recycleTextView.setTextColor(Color.YELLOW);
                }
                recycleTextView.setTextSize(24);
                // Set text color to yellow
                tableRow.addView(recycleTextView);

                recycleTextView.setOnClickListener(v -> {
                    // 해당 TableRow를 터치했을 때 실행될 로직을 여기에 작성합니다.
                    if (recycle_method_images != null && result.getLabel() < recycle_method_images.length) {
                        // label에 해당하는 이미지가 있는 경우, 이미지를 보여주는 다이얼로그를 띄웁니다.
                        showImageDialog(recycle_method_images[result.getLabel()]);
                    }
                });
                /*
                TextView scoreTextView = new TextView(this);
                scoreTextView.setText(String.format("%.2f", result.getScore()));
                scoreTextView.setTextSize(24);
                tableRow.addView(scoreTextView);



                TextView RectTextView = new TextView(this);
                RectTextView.setText(result.getRectF().toString()); // 사각형 정보를 출력
                RectTextView.setTextSize(24);
                tableRow.addView(RectTextView);
                */
                // Set margin to the TableRow
                int marginInPx = getResources().getDimensionPixelSize(R.dimen.table_row_margin);
                TableRow.LayoutParams layoutParams = new TableRow.LayoutParams(
                        TableRow.LayoutParams.MATCH_PARENT,
                        TableRow.LayoutParams.WRAP_CONTENT
                );
                layoutParams.setMargins(0, marginInPx, 0, 0); // Add top margin to the TableRow
                tableRow.setLayoutParams(layoutParams);

                // Add a separator View (black margin) between TableRow elements
                if (i > 0) {
                    View separator = new View(this);
                    separator.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, 1));
                    separator.setBackgroundColor(ContextCompat.getColor(this, R.color.black)); // Use the divider_black color
                    tableLayout.addView(separator);
                }

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
        disableImageAnalysis();
        try {
            ortSession.close();
            ortEnvironment.close();
        } catch (OrtException e) {
            e.printStackTrace();
        }
        super.onDestroy();
    }
}
