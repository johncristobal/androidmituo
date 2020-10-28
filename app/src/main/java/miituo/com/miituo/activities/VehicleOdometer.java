package miituo.com.miituo.activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.text.Html;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import miituo.com.miituo.R;
import miituo.com.miituo.api.ApiClient;
import miituo.com.miituo.data.DBaseMethods;
import miituo.com.miituo.data.IinfoClient;
import miituo.com.miituo.data.InfoClient;
import miituo.com.miituo.data.modelBase;
import miituo.com.miituo.utils.LogHelper;

import android.Manifest;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.vision.text.TextRecognizer;

import org.json.JSONArray;
import org.json.JSONObject;

import static miituo.com.miituo.activities.MainActivity.result;

public class VehicleOdometer extends BaseActivity {

    public boolean flagodo = false;

    private ImageView Img5;
    private Button btn6 =null;
    final int ODOMETER=5,CANCEL=9;
    final String UrlConfirmOdometer="ImageProcessing/ConfirmOdometer";
    //final String UrlApi="ImageProcessing/";
//    final String  UrlApi="ImageSendProcess/Array";
    final String  UrlApi="ImageSendProcess/AutoRead";
    final String ApiSendReportCancelation ="ReportOdometer/PreviewSaveReport/";
    private ApiClient api;
    public static String odo="";
    public static JSONArray autoRead=new JSONArray();
    public File photoFile = null;
    public String mCurrentPhotoPath,tok;

    boolean IsTaken =false;
    Bitmap bmp;
    private Typeface typeface;

    public String tipoodometro;
    SharedPreferences app_preferences;

    private static final int MY_CAMERA_REQUEST_CODE = 100;

    public sendOdometro sendodo;
    public static TextRecognizer txtR;

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == MY_CAMERA_REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //Toast.makeText(this, "camera permission granted", Toast.LENGTH_LONG).show();
                Intent takepic=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
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
                        Uri photoURI = FileProvider.getUriForFile(VehicleOdometer.this, "miituo.com.miituo.provider", photoFile);
                        //Uri photoURI = Uri.fromFile(photoFile);
                        takepic.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                        startActivityForResult(takepic, ODOMETER);
//                        Intent i= new Intent(this,CamActivity.class);
//                        i.putExtra("img",photoFile);
//                        startActivityForResult(i,ODOMETER);
                    }
                }
            } else {
                Toast.makeText(this, "No se pueden tomar fotos. Acceso denegado.", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vehicle_odometer);
        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        try {
            tok = IinfoClient.getInfoClientObject().getClient().getToken();
        }catch (Exception e){
            final GlobalActivity globalVariable = (GlobalActivity) getApplicationContext();
            List<InfoClient> polizas = globalVariable.getPolizas();
            if(polizas.size() > 0) {
                tok = polizas.get(0).getClient().getToken();
            }else{
                Toast.makeText(this, "Sin autorización, intente mas tarde.", Toast.LENGTH_SHORT).show();
            }
        }

        typeface = Typeface.createFromAsset(getAssets(), "fonts/herne1.ttf");
        TextView leyenda = (TextView)findViewById(R.id.textView40);
        leyenda.setTypeface(typeface);
        TextView res = (TextView)findViewById(R.id.textView38);
        res.setTypeface(typeface);
        TextView btnSinAuto = (TextView)findViewById(R.id.btnSinAuto);
        btnSinAuto.setTypeface(typeface);

        txtR = new TextRecognizer.Builder(this).build();

        if(getIntent().getBooleanExtra("isCancelada",false)){
            btnSinAuto.setVisibility(View.VISIBLE);
            btnSinAuto.setEnabled(true);
        }
        else{
            btnSinAuto.setVisibility(View.INVISIBLE);
            btnSinAuto.setEnabled(false);
        }

        ImageButton back = (ImageButton)findViewById(R.id.imageView12);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        Typeface typefacebold = Typeface.createFromAsset(getAssets(), "fonts/herne.ttf");
        TextView titulo = (TextView)findViewById(R.id.textView27);
        titulo.setTypeface(typefacebold);

        Init5();
        api=new ApiClient(VehicleOdometer.this);
    }

    void Init5(){
        //edittext with odometer
        //odometeredit = (EditText)findViewById(R.id.editText);
        //edit2 = (EditText)findViewById(R.id.editText2);

        Img5=(ImageView)findViewById(R.id.img5);
        btn6=(Button)findViewById(R.id.btn6);
        /*btn6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });*/

        //lanzar picturoe para capturar foto
        Img5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tomarFotografia();
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
                if(checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
                    requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1009);
                }else {
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
                            Uri photoURI = FileProvider.getUriForFile(VehicleOdometer.this, "miituo.com.miituo.provider", photoFile);
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
    }

    public void subirFoto(View v){
        if (btn6.getText().toString().contains("omar")){
            LogHelper.log(this,LogHelper.user_interaction,"VehicleOdometer.tomarFoto","tomar foto odometro","",
                    "","id:"+IinfoClient.getInfoClientObject().getPolicies().getId(),"");
            tomarFotografia();
        }else {
            if (flagodo) {
                    sendodo = new sendOdometro();
                LogHelper.log(this,LogHelper.backTask,"VehicleOdometer.tomarFoto","inicio envio de foto","",
                        "","id:"+IinfoClient.getInfoClientObject().getPolicies().getId(),"");
                    sendodo.execute("OK");
            } else {
                Toast msg = Toast.makeText(getApplicationContext(), "Tome la foto del odómetro para finalizar", Toast.LENGTH_LONG);
                msg.show();
            }
        }
    }

    public void sinAuto(View v){
            getCobro(false);
    }

    static AlertDialog alert;
    public void showPago(String resp){
        String noDias="";
        String saldo="";
            try{
            JSONObject json = new JSONObject(resp);
            JSONObject datos = json.getJSONArray("ResulList").getJSONObject(0);
            noDias=datos.getString("Dias_sin_reportar");
            saldo=datos.getString("amount");
        }
        catch (Exception e){
                e.printStackTrace();
        }
        final AlertDialog.Builder sort = new AlertDialog.Builder(this);
        // Get the layout inflater
        LayoutInflater inflater = getLayoutInflater();
        View sortView = inflater.inflate(R.layout.activity_no_reporte_odometro, null);
        sort.setView(sortView);
        sort.setCancelable(false);
        TextView back=(TextView) sortView.findViewById(R.id.btnRegresar);
        TextView pagar=(TextView) sortView.findViewById(R.id.btnPagar);
        TextView dias=(TextView) sortView.findViewById(R.id.lbGral);
        back.setTypeface(typeface);
        pagar.setTypeface(typeface);
        dias.setTypeface(typeface);
        dias.setText(Html.fromHtml("<p>Han transcurrido <b>"+noDias+" días" +
                "</b> desde tu<br> último reporte de odómetro,<br>tu <b>cargo es de $"+saldo+"</b></p>"));
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alert.dismiss();
            }
        });
        pagar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendodo = new sendOdometro();
                sendodo.execute("NO");
            }
        });
        alert=sort.create();
        alert.show();
    }

    public void getCobro(final boolean isPagar){
            AsyncTask<Void, Void, Void> sendReporter = new AsyncTask<Void, Void, Void>() {
                ProgressDialog progress = new ProgressDialog(VehicleOdometer.this);
                String respuesta="",ErrorCode = "";

                @Override
                protected void onPreExecute() {
                    progress.setTitle("Odómetro");
                    progress.setMessage("Enviando información...");
                    progress.setIndeterminate(true);
                    progress.setCancelable(false);
                    //progress.setIcon(R.drawable.miituo);
                    progress.show();
                }

                @Override
                protected void onCancelled() {
                    progress.dismiss();
                    Toast msg = Toast.makeText(getApplicationContext(), ErrorCode, Toast.LENGTH_LONG);
                    msg.show();

                    super.onCancelled();
                }

                @Override
                protected Void doInBackground(Void... params) {
                    try {
                        respuesta = api.ConfirmReport(ApiSendReportCancelation,tok,true, isPagar);
                    } catch (Exception ex) {
                        ErrorCode = ex.getMessage();
                        this.cancel(true);
                    }
                    return null;
                }

                @Override
                protected void onPostExecute(Void aVoid){
                    try {
                        progress.dismiss();
                            if(!isPagar) {
//                                Intent i = new Intent(VehicleOdometer.this, NoReporteOdometroActivity.class);
//                                i.putExtra("json", respuesta);
//                                startActivityForResult(i, CANCEL);
                                showPago(respuesta);
                                return;
                            }
                            else{
                                JSONObject json=new JSONObject(respuesta);
                                JSONObject datos=json.getJSONArray("ResulList").getJSONObject(0);
                                boolean isOK=datos.isNull("ConfirmReport")?false:datos.getBoolean("ConfirmReport");

                                if (isOK) {
                                    AlertDialog.Builder builder = new AlertDialog.Builder(VehicleOdometer.this);
                                    builder.setTitle("Error");
                                    builder.setMessage("Error al subir información. Intente más tarde");
                                    builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            Intent i = new Intent(VehicleOdometer.this, PrincipalActivity.class);
                                            finish();
                                            startActivity(i);
                                        }
                                    });
                                    AlertDialog alerta = builder.create();
                                    alerta.show();
                                } else {
                                    new android.app.AlertDialog.Builder(VehicleOdometer.this)
                                            .setTitle("Gracias.")
                                            .setMessage("Tu información ha sido actualizada.")
                                            .setCancelable(false)
                                            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    IinfoClient.getInfoClientObject().getPolicies().setLastOdometer(IinfoClient.getInfoClientObject().getPolicies().getRegOdometer());
                                                    IinfoClient.getInfoClientObject().getPolicies().setReportState(12);

                                                    //String rs = UpdateDataBase(modelBase.FeedEntryPoliza.TABLE_NAME,IinfoClient.getInfoClientObject().getPolicies().getNoPolicy());

                                                    Intent i = new Intent(VehicleOdometer.this, PrincipalActivity.class);
                                                    i.putExtra("actualizar","1");
                                                    finish();
                                                    startActivity(i);
                                                }
                                            })
                                            .show();
                                }
                            }
                    }catch(Exception e){
                        e.printStackTrace();
                    }
                }


                public String UpdateDataBase(String...strings){

                    String val = strings[0];
                    //Log.w("Here",val);

                    switch (val){

                        case modelBase.FeedEntryPoliza.TABLE_NAME:
                            // Gets the data repository in write mode
                            SQLiteDatabase db = DBaseMethods.base.getWritableDatabase();

                            // Create a new map of values, where column names are the keys
                            ContentValues values = new ContentValues();
                            //values.put(modelBase.FeedEntryArticle.COLUMN_ID, strings[6]);
                            values.put(modelBase.FeedEntryPoliza.COLUMN_NOPOLICY, strings[1]);
                            //values.put(modelBase.FeedEntryPoliza.COLUMN_ODOMETERPIE, true);
                            //values.put(modelBase.FeedEntryPoliza.COLUMN_VEHICLEPIE, true);
                            values.put(modelBase.FeedEntryPoliza.COLUMN_LASTODOMETER,IinfoClient.getInfoClientObject().getPolicies().getRegOdometer()+"");
                            values.put(modelBase.FeedEntryPoliza.COLUMN_REPORT_STATE, 12);

                            // Insert the new row, returning the primary key value of the new row
                            //Just change name table and the values....
                            long newRowId = db.update(val, values,modelBase.FeedEntryPoliza.COLUMN_NOPOLICY+"='"+strings[1]+"'",null);
                            try {
                                Thread.sleep(50);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }

                            return "" + newRowId;

                        default:
                            break;
                    }
                    return "";
                }
            };
            sendReporter.execute();
    }

    //Al tomar la foto...la procesamos en el activty for result
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode== Activity.RESULT_OK) {
            if (requestCode==CANCEL) {
                sendodo = new sendOdometro();
                sendodo.execute("NO");
            }
            else if (requestCode == 5) {
            try {
                LogHelper.log(this,LogHelper.backTask,"VehicleOdometer.onActivityResult","se tomo foto","",
                        "","id:"+IinfoClient.getInfoClientObject().getPolicies().getId(),"");
                SharedPreferences preferences = getSharedPreferences("miituo", Context.MODE_PRIVATE);
                mCurrentPhotoPath = preferences.getString("nombrefotoodometro", "null");
                String filePath = mCurrentPhotoPath;
                flagodo = true;
                //Img5.setImageBitmap(bmp);
                Glide.with(VehicleOdometer.this)
                        .load(filePath)
                        .apply(new RequestOptions().override(150, 200).centerCrop())
                        .into(Img5);
//                odo=data.getStringExtra("result");
                IsTaken = true;
                btn6.setText("Continuar");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
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

            SharedPreferences preferences = getSharedPreferences("miituo", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString("nombrefotoodometro", mCurrentPhotoPath);
            editor.apply();
            return image;
        }catch(Exception e){
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.manu_principal,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        return true;
    }

//*******************************************not attribute =! class*********************************
    public class sendOdometro extends AsyncTask<String, Void, Void> {
        ProgressDialog progress = new ProgressDialog(VehicleOdometer.this);
        String ErrorCode = "";
        String isFoto="OK";

        public void showAlerta(){
            AlertDialog.Builder builder = new AlertDialog.Builder(VehicleOdometer.this);
            builder.setTitle("Atención");
            builder.setMessage("Hubo un problema. Lo intentaremos más tarde.");
            builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent i=new Intent(VehicleOdometer.this,ConfirmActivity.class);
                    startActivity(i);
                }
            });

            AlertDialog alerta = builder.create();
            alerta.show();
        }

        @Override
        protected void onPreExecute() {
            try {
                progress.setTitle("Registro de odómetro");
                progress.setMessage("Subiendo imagen...");
                //progress.setIcon(R.drawable.miituo);
                progress.setCancelable(false);
                progress.setIndeterminate(true);
                progress.show();
            }
            catch(Exception e){
                showAlerta();
            }
        }

        @Override
        protected Void doInBackground(String... params) {
            try {
                app_preferences= getSharedPreferences("miituo", Context.MODE_PRIVATE);
                tipoodometro = app_preferences.getString("odometro","null");
                isFoto=params[0];
                LogHelper.log(VehicleOdometer.this,LogHelper.backTask,"VehiclePictures.sendOdometer.doing","inicio subir foto odometro, isFoto: "+isFoto,"",
                        "","id:"+IinfoClient.getInfoClientObject().getPolicies().getId(),"");
                if(isFoto.equals("NO")){
                    bmp = BitmapFactory.decodeResource(VehicleOdometer.this.getResources(),R.drawable.ayuda);
                    //api.UploadPhoto(6, ImageList.get(0).getImage(), UrlApi);
                    api.UploadPhoto(6, bmp, UrlApi,tok,"","0","0","");
                }else{
                    //Subimos foto de odometro.....
                    mCurrentPhotoPath = app_preferences.getString("nombrefotoodometro", "null");
                    BitmapFactory.Options options = new BitmapFactory.Options();
                    options.inSampleSize = 4;
                    bmp = BitmapFactory.decodeFile(mCurrentPhotoPath,options);
                    String lat=null, lon=null, cp=null;
                    try
                    {
                        LogHelper.log(VehicleOdometer.this,LogHelper.backTask,"VehiclePictures.sendOdometer.doing","obteniendo datos geograficos","",
                                "","id:"+IinfoClient.getInfoClientObject().getPolicies().getId(),"");
                        ExifInterface exif = new ExifInterface(mCurrentPhotoPath);
                        lat=exif.getAttribute(ExifInterface.TAG_GPS_LATITUDE);
                        lon=exif.getAttribute(ExifInterface.TAG_GPS_LONGITUDE);
                        lat=null;
                        if(lat==null || lon==null ){
                            if(ContextCompat.checkSelfPermission(getApplicationContext(),
                                    android.Manifest.permission.ACCESS_FINE_LOCATION)
                                    == PackageManager.PERMISSION_GRANTED) {
                                LocationManager mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                                Criteria criteria = new Criteria();
                                Location location = mLocationManager.getLastKnownLocation(mLocationManager.getBestProvider(criteria, false));
                                if(location!=null) {
                                    lat = location.getLatitude() + "";
                                    lon = "" + location.getLongitude();
                                    cp = getZipCode(lat, lon);
                                }
                                else{
                                    lat="0";
                                    lon="0";
                                    cp="";
                                }
                            }
                            else{
                                lat="0";
                                lon="0";
                                cp="";
                            }
                        }
                        else{
                            cp=getZipCode(lat, lon);
                        }
                    }
                    catch (IOException e){
                        e.printStackTrace();
                        LogHelper.log(VehicleOdometer.this,LogHelper.backTask,"VehiclePictures.sendOdometer.doing","excepcion en geograficos","",
                                "","id:"+IinfoClient.getInfoClientObject().getPolicies().getId(),LogHelper.getException(e));
                    }
                    api.UploadPhoto((tipoodometro.equals("cancela")?6:5), bmp, UrlApi,tok,odo,lat,lon,cp);
//                    api.UploadPhoto((tipoodometro.equals("cancela")?6:5), bmp, UrlApi,tok);
                }
                IinfoClient.InfoClientObject.getPolicies().setRegOdometer(Integer.parseInt("1000"));
            } catch (IOException ex) {
                ErrorCode = ex.getMessage();
                LogHelper.log(VehicleOdometer.this,LogHelper.backTask,"VehiclePictures.sendOdometer.doing","excepcion en doing","",
                        "","id:"+IinfoClient.getInfoClientObject().getPolicies().getId(),LogHelper.getException(ex));
                this.cancel(true);
            }
            return null;
        }

    private String getZipCode(String lat, String lon){
        Geocoder geocoder;
        List<Address> addresses;
        String cp="";
        geocoder = new Geocoder(VehicleOdometer.this, Locale.getDefault());
        try {
            addresses = geocoder.getFromLocation(Double.valueOf(lat), Double.valueOf(lon), 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
            cp= (addresses.get(0).getPostalCode()!=null)?addresses.get(0).getPostalCode():"";
        }
        catch (Exception e){
            e.printStackTrace();
            LogHelper.log(VehicleOdometer.this,LogHelper.backTask,"VehiclePictures.sendOdometer.getZipCode","excepcion en geograficos","",
                    "","id:"+IinfoClient.getInfoClientObject().getPolicies().getId(),LogHelper.getException(e));
            cp="";
        }
        return cp;
    }
        @Override
        protected void onPostExecute(Void aVoid) {

            try {
                progress.dismiss();
                if(isFoto.equals("NO")){
                    getCobro(true);
                }
                else {
                    Intent i = new Intent(VehicleOdometer.this, ConfirmActivity.class);
                    startActivity(i);
                }
            }
            catch (Exception e){
                showAlerta();
            }
        }

        @Override
        protected void onCancelled() {
            try {
                progress.dismiss();
                if (ErrorCode.equals("1000")) {
                    Intent i = new Intent(VehicleOdometer.this, ConfirmActivity.class);
                    startActivity(i);
                } else {
                    Toast msg = Toast.makeText(getApplicationContext(), "Tuvimos un problema:" + ErrorCode, Toast.LENGTH_LONG);
                    msg.show();
                    LogHelper.sendLog(VehicleOdometer.this,IinfoClient.getInfoClientObject().getPolicies().getId());
                }
            }catch (Exception e){
                showAlerta();
            }
        }
    };

}
