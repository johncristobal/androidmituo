package miituo.com.miituo.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Bundle;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.text.TextBlock;
import java.io.File;
import java.io.FileOutputStream;
import java.lang.reflect.Field;
import android.Manifest;
import android.content.pm.PackageManager;
import androidx.core.app.ActivityCompat;

import miituo.com.miituo.R;

public class CamActivity extends BaseActivity implements CameraSource.PictureCallback, SurfaceHolder.Callback {

    private SurfaceView preview = null;
    private SurfaceHolder previewHolder = null;
    ImageView image;
    Bitmap bmp;
    static Bitmap mutableBitmap;
    File finalImage = null;
    ProgressDialog dialog;
    TextView lbCapturar,lbAyuda;
    Uri photoURI = null;
    private SeekBar zoomControls;
    CameraSource mCameraSource;
    private boolean isPictureTaken=false;
    private String odo="";
    Typeface typeface;


    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        // Checks the orientation of the screen
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            lbAyuda.setText("Alinea el odómetro dentro del área del visor");
            restart();
//            Camera.Parameters parameters = getCamera().getParameters();
//            parameters.setRotation(90);
//            getCamera().setParameters(parameters);
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT){
            lbAyuda.setText("Alinea el odómetro\ndentro del área del visor");
            restart();
        }
    }

    private void restart(){

        typeface = Typeface.createFromAsset(getAssets(), "fonts/herne1.ttf");

        finalImage = (File) getIntent().getExtras().get("img");
        photoURI = (Uri) getIntent().getExtras().get("uri");

        image = (ImageView) findViewById(R.id.image);
        preview = (SurfaceView) findViewById(R.id.surface);
        zoomControls = (SeekBar) findViewById(R.id.zoomer);
        lbCapturar = (TextView) findViewById(R.id.lbCapturar);
        lbAyuda = (TextView) findViewById(R.id.lbAyuda);
        lbCapturar.setTypeface(typeface,Typeface.BOLD);
        lbAyuda.setTypeface(typeface,Typeface.BOLD);

        previewHolder = preview.getHolder();
        setVision();
        previewHolder.addCallback(this);
        previewHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        previewHolder.setFixedSize(getWindow().getWindowManager().getDefaultDisplay().getWidth(), getWindow().getWindowManager().getDefaultDisplay().getHeight());
        enableZoom();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cam);
        restart();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        try {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) ==
                    PackageManager.PERMISSION_GRANTED) {
                mCameraSource.start(preview.getHolder());
            }
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
    }

    private Camera getCamera(){
        Camera camera;
        Field[] declaredFields = CameraSource.class.getDeclaredFields();
        for (Field field : declaredFields) {
            if (field.getType() == Camera.class) {
                field.setAccessible(true);
                try {
                    camera = (Camera) field.get(mCameraSource);
                    if (camera != null) {
                        return camera;
                    }
                    return null;
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
                break;
            }
        }
        return null;
    }

    public void setVision() {
        if (VehicleOdometer.txtR.isOperational()) {
            mCameraSource = new CameraSource.Builder(this, VehicleOdometer.txtR)
                    .setFacing(CameraSource.CAMERA_FACING_BACK)
                    .setRequestedPreviewSize(1280, 1024)
                    .setAutoFocusEnabled(true)
                    .setRequestedFps(2.0f)
                    .build();

            VehicleOdometer.txtR.setProcessor(new Detector.Processor<TextBlock> (){
                @Override
                public void release() {}
                @Override
                public void receiveDetections(Detector.Detections<TextBlock> detections) {
                    if (detections.getDetectedItems().size() <= 0) {
                        return;
                    }
                    if(!isPictureTaken) {
                        String cad = "";
                        for (int i = 0; i < detections.getDetectedItems().size(); i++) {
                            TextBlock item = detections.getDetectedItems().valueAt(i);
                            cad = cad + item.getValue();
                            cad = cad + "\n";
                        }
                        odo = cad;
                    }
                }
            });
        }
        else{
            mCameraSource = new CameraSource.Builder(this, VehicleOdometer.txtR)
                    .setFacing(CameraSource.CAMERA_FACING_BACK)
                    .setRequestedPreviewSize(1280, 1024)
                    .setAutoFocusEnabled(true)
                    .setRequestedFps(2.0f)
                    .build();

            VehicleOdometer.txtR.setProcessor(new Detector.Processor<TextBlock> (){
                @Override
                public void release() {}
                @Override
                public void receiveDetections(Detector.Detections<TextBlock> detections) {
                    if (detections.getDetectedItems().size() <= 0) {
                        return;
                    }
                    if(!isPictureTaken) {
                        String cad = "";
                        for (int i = 0; i < detections.getDetectedItems().size(); i++) {
                            TextBlock item = detections.getDetectedItems().valueAt(i);
                            cad = cad + item.getValue();
                            cad = cad + "\n";
                        }
                        odo = cad;
                    }
                }
            });
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        if(mCameraSource!=null)
        mCameraSource.stop();
    }

    public void tomarFoto(View v) {
        mCameraSource.takePicture(null, this);
    }

    private void enableZoom() {
        zoomControls.setMax(100);
        zoomControls.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                zoomCamera(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
    }

    public void zoomCamera(int zoomInOrOut) {
        if (mCameraSource != null) {
            Camera.Parameters parameter = getCamera().getParameters();
            if (parameter.isZoomSupported()) {
                int MAX_ZOOM = parameter.getMaxZoom();
                int currnetZoom = parameter.getZoom();
                currnetZoom = zoomInOrOut * MAX_ZOOM / 100;
                parameter.setZoom(currnetZoom);
            }
            getCamera().setParameters(parameter);
        }
    }

//    @Override
//    public void onPictureTaken(final byte[] data, final Camera camera) {
//        dialog = ProgressDialog.show(CamActivity.this, "Guardando Foto", "Por favor espera un momento");
//        new Thread() {
//            public void run() {
//                try {
//                    Thread.sleep(100);
//                } catch (Exception ex) {
//                }
//                onPictureTake(data, camera);
//            }
//        }.start();
//    }

    @Override
    public void onPictureTaken(final byte[] bytes) {
        isPictureTaken=true;
        dialog = ProgressDialog.show(CamActivity.this, "Guardando Foto", "Por favor espera un momento");
        new Thread() {
            public void run() {
                try {
                    Thread.sleep(100);
                } catch (Exception ex) {
                }
                onPictureTake(bytes, getCamera());
            }
        }.start();
    }

    public void onPictureTake(byte[] data, Camera camera) {
        bmp = BitmapFactory.decodeByteArray(data, 0, data.length);
//        mutableBitmap = bmp.copy(Bitmap.Config.ARGB_8888, true);
        savePhoto(bmp);
        dialog.dismiss();
        Intent returnIntent = new Intent();
        returnIntent.putExtra("result",odo);
        setResult(RESULT_OK, returnIntent);
        finish();
    }

    public void savePhoto(Bitmap bmp) {
        Bitmap bOutput;
//        float degrees = 90;//rotation degree
//        Matrix matrix = new Matrix();
//        matrix.setRotate(degrees);
//        bOutput = Bitmap.createBitmap(bmp, 0, 0, bmp.getWidth(), bmp.getHeight(), matrix, true);
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(finalImage);
//            bOutput.compress(Bitmap.CompressFormat.PNG, 80, out);
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent returnIntent = new Intent();
        setResult(RESULT_CANCELED, returnIntent);
        finish();
    }

}
