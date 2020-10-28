package com.miituo.miituolibrary.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.miituo.miituolibrary.R;
import com.miituo.miituolibrary.activities.api.ApiClient;
import com.miituo.miituolibrary.activities.data.IinfoClient;
import com.miituo.miituolibrary.activities.data.InfoClient;

import org.json.JSONArray;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static com.miituo.miituolibrary.activities.PrincipalActivity.result;

public class VehicleOdometer extends AppCompatActivity {

    public static JSONArray autoRead=new JSONArray();
    public String mCurrentPhotoPath,tok;
    final String  UrlApi="ImageSendProcess/AutoRead";
    final String ApiSendReportCancelation ="ReportOdometer/PreviewSaveReport/";
    private ApiClient api;
    private static final int MY_CAMERA_REQUEST_CODE = 100;

    public File photoFile = null;
    final int ODOMETER=5,CANCEL=9;

    ImageView Img5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vehicle_odometer);

        try {
            tok = IinfoClient.getInfoClientObject().getClient().getToken();
        }catch (Exception e){
            List<InfoClient> polizas = result;
            if(polizas.size() > 0) {
                tok = polizas.get(0).getClient().getToken();
            }else{
                Toast.makeText(this, "Sin autorización, intente mas tarde.", Toast.LENGTH_SHORT).show();
            }
        }

        TextView btnSinAuto = (TextView)findViewById(R.id.btnSinAuto);
        if(getIntent().getBooleanExtra("isCancelada",false)){
            btnSinAuto.setVisibility(View.VISIBLE);
            btnSinAuto.setEnabled(true);
        }
        else{
            btnSinAuto.setVisibility(View.INVISIBLE);
            btnSinAuto.setEnabled(false);
        }

        Init5();
        api=new ApiClient(VehicleOdometer.this);
    }

    void Init5(){
        //edittext with odometer
        //odometeredit = (EditText)findViewById(R.id.editText);
        //edit2 = (EditText)findViewById(R.id.editText2);

        Img5=(ImageView)findViewById(R.id.img5);
        Button btn6=(Button)findViewById(R.id.btn6);
        /*btn6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });*/

        //lanzar picturoe para capturar foto
        Img5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                        requestPermissions(new String[]{Manifest.permission.CAMERA}, MY_CAMERA_REQUEST_CODE);
                    } else {
                        tomarFotografia();
                    }
                }else{
                    tomarFotografia();
                }
            }
        });
    }

    public void tomarFotografia(){

        if (Build.VERSION.SDK_INT < 23) {
            Intent takepic = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (takepic.resolveActivity(getPackageManager()) != null) {
                // Create the File where the photo should go
                try {
                    photoFile = createImageFile();
                } catch (IOException ex) {
                    // Error occurred while creating the File...
                    showAlertaFoto();
                }
                // Continue only if the File was successfully created
                if (photoFile != null) {
                    //Uri photoURI = FileProvider.getUriForFile(VehicleOdometer.this, "miituo.com.miituo.provider", photoFile);
                    Uri photoURI = Uri.fromFile(photoFile);
                    takepic.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                    startActivityForResult(takepic, ODOMETER);
//                    Intent i= new Intent(this,CamActivity.class);
//                    i.putExtra("img",photoFile);
//                    startActivityForResult(i,ODOMETER);
                }
            }
        }else{
            if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                //PERMISO = FRONT_VEHICLE;
                requestPermissions(new String[]{Manifest.permission.CAMERA}, MY_CAMERA_REQUEST_CODE);
            }else{
                Intent takepic = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (takepic.resolveActivity(getPackageManager()) != null) {
                    // Create the File where the photo should go
                    try {
                        photoFile = createImageFile();
                    } catch (IOException ex) {
                        // Error occurred while creating the File...
                        showAlertaFoto();
                    }
                    // Continue only if the File was successfully created
                    if (photoFile != null) {
                        Uri photoURI = FileProvider.getUriForFile(VehicleOdometer.this, "com.miituo.miituolibrary.provider", photoFile);
                        //Uri photoURI = Uri.fromFile(photoFile);
                        takepic.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                        startActivityForResult(takepic, ODOMETER);
//                        Intent i= new Intent(this,CamActivity.class);
//                        i.putExtra("img",photoFile);
//                        startActivityForResult(i,ODOMETER);
                    }
                }
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == MY_CAMERA_REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //Toast.makeText(this, "camera permission granted", Toast.LENGTH_LONG).show();
                tomarFotografia();
            } else {
                Toast.makeText(this, "No se pueden tomar fotos. Acceso denegado.", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        try {
            super.onActivityResult(requestCode, resultCode, data);
            if(resultCode== Activity.RESULT_OK)
            {
                String filePath = mCurrentPhotoPath;
                //flagodo = true;
                //Img5.setImageBitmap(bmp);
                Glide.with(VehicleOdometer.this)
                        .load(filePath)
                        .apply(new RequestOptions().override(150, 200).centerCrop())
                        .into(Img5);
//                Bundle extras = data.getExtras();
//                Bitmap imageBitmap = (Bitmap) extras.get("data");
//                Glide.with(VehicleOdometer.this)
//                        .load(imageBitmap)
//                        .apply(new RequestOptions().override(150, 200).centerCrop())//.override(150,200)
//                        //.override(150,200)
//                        //.centerCrop()
//                        .into(Img1);
                //Img1.setImageBitmap(imageBitmap);
//                String user = "";
//                String picName="";
//                switch (requestCode){
//                    case 1:
//                        user = "frontal";
//                        picName="Frontal";
//                        break;
//                    case 2:
//                        user = "derecho";
//                        picName="Lateral Derecho";
//                        break;
//                    case 3:
//                        user = "back";
//                        picName="Trasera";
//                        break;
//                    case 4:
//                        user = "izquierdo";
//                        picName="Lateral Izquierdo";
//                        break;
//                    default:
//                        break;
//                }
//
//                String filePath = currentPhotoPath;
//
//                if (requestCode == 1) {
//                    //flag1 = true;
//                    //comprimer imagen antes de lanzarla al imageview
//                    //bmp.compress(Bitmap.CompressFormat.JPEG,15);
//
//                    //Img1.setImageBitmap(resizedtoshow);
//                    Glide.with(VehiclePictures.this)
//                            .load(filePath)
//                            .apply(new RequestOptions().override(150, 200).centerCrop())//.override(150,200)
//                            //.override(150,200)
//                            //.centerCrop()
//                            .into(Img1);
//                    //Img1.setImageBitmap(bmp);
//                }
//                if (requestCode == 2) {
//                    flag2 = true;
//                    //Bundle ext=data.getExtras();
//                    //bmp=(Bitmap)ext.get("data");
//                    //Img2.setImageBitmap(resizedtoshow);
//                    //Img2.setImageBitmap(bmp);
//                    Glide.with(VehiclePictures.this)
//                            .load(filePath)
//                            .apply(new RequestOptions().override(150, 200).centerCrop())//.override(150,200)
//                            //.override(150,200)
//                            //.centerCrop()
//                            .into(Img2);
//                }
//                if (requestCode == 3) {
//                    flag3 = true;
//                    //Bundle ext=data.getExtras();
//                    //bmp=(Bitmap)ext.get("data");
//                    //Img3.setImageBitmap(resizedtoshow);
//                    //Img3.setImageBitmap(bmp);
//                    Glide.with(VehiclePictures.this)
//                            .load(filePath)
//                            .apply(new RequestOptions().override(150, 200).centerCrop())//.override(150,200)
//                            //.override(150,200)
//                            //.centerCrop()
//                            .into(Img3);
//                }
//                if (requestCode == 4) {
//                    flag4 = true;
//                    //Bundle ext=data.getExtras();
//                    //bmp=(Bitmap)ext.get("data");
//                    //Img4.setImageBitmap(resizedtoshow);
//                    //Img4.setImageBitmap(bmp);
//                    Glide.with(VehiclePictures.this)
//                            .load(filePath)
//                            .apply(new RequestOptions().override(150, 200).centerCrop())//.override(150,200)
//                            // .override(150,200)
//                            //.centerCrop()
//                            .into(Img4);
//                }
//                if (requestCode == 5) {
//                    //Bundle ext=data.getExtras();
//                    //bmp=(Bitmap)ext.get("data");
//                    //Img5.setImageBitmap(resizedtoshow);
//                    //Img5.setImageBitmap(bmp);
//                    Glide.with(VehiclePictures.this)
//                            .load(filePath)
//                            .apply(new RequestOptions().override(150, 200).centerCrop())//.override(150,200)
//                            //.override(150,200)
//                            //.centerCrop()
//                            .into(Img5);
//                }
//                IsTaken = true;
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

    public void showAlertaFoto(){
        AlertDialog.Builder builder = new AlertDialog.Builder(VehicleOdometer.this);
        builder.setTitle("Atención");
        builder.setMessage("No podemos abrir tu cámara. Revisa el dispositivo.");
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent i=new Intent(VehicleOdometer.this,PrincipalActivity.class);
                startActivity(i);
            }
        });

        AlertDialog alerta = builder.create();
        alerta.show();
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        try {
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            String imageFileName = "PNG_" + timeStamp + "_";
            File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
            //File storageDir = getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);

            File image = File.createTempFile(
                    imageFileName,  // prefix
                    ".jpeg",         // suffix
                    storageDir      // directory
            );

            //File image = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES)+File.separator+"odometro_"+polizaFolio+".png");

            // Save a file: path for use with ACTION_VIEW intents
            mCurrentPhotoPath = image.getAbsolutePath();

//            SharedPreferences preferences = getSharedPreferences("miituo", Context.MODE_PRIVATE);
//            SharedPreferences.Editor editor = preferences.edit();
//            editor.putString("nombrefotoodometro", mCurrentPhotoPath);
//            editor.apply();
            return image;
        }catch(Exception e){
            e.printStackTrace();
            return null;
        }
    }
}