package miituo.com.miituo.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.annotation.NonNull;
import androidx.core.content.FileProvider;

import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;

import android.Manifest;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import miituo.com.miituo.R;
import miituo.com.miituo.TaskCanceler;
import miituo.com.miituo.api.ApiClient;
import miituo.com.miituo.data.IinfoClient;
import miituo.com.miituo.utils.LogHelper;

import static miituo.com.miituo.activities.PrincipalActivity.fotosfaltantesList;

public class VehiclePictures extends AppCompatActivity {

    public boolean flag1,flag2,flag3,flag4;
    public boolean flag1valida,flag2valida,flag3valida,flag4valida;

    private ImageView Img1,Img2,Img3,Img4,Img5;

    private Button btn1,btn6 =null;
    private ApiClient api;

    public int PERMISO;
    public String NOMBRETEMP;

    final int FRONT_VEHICLE=1;
    final int SIDE_RIGHT_VEHICLE=2;
    final int REAR_VEHICLE=3;
    final int SIDE_LEFT_VEHICLE=4;
    final int ODOMETER=5;
    TextView leyenda;
    EditText odometeredit;
    //final String UrlApi="ImageProcessing/";
    final String UrlApi="ImageSendProcess/Array/";
    final String UrlAjustelast="Policy/UpdatePolicyStatusReport/";
    //final String UrlApi="http://10.69.237.103:1001/api/ImageProcessing/";
    final String UrlConfirmOdometer="ImageProcessing/ConfirmOdometer";
    //final String UrlConfirmOdometer="http://10.69.237.103:1001/api/ImageProcessing/ConfirmOdometer";
    Intent i;
    final static int constante=0;
    boolean IsTaken =false;
    Bitmap bmp;

    public File photoFile = null;
    public String mCurrentPhotoPath;
    private Typeface typeface;

    public String polizaFolio,tok;

    private static final int MY_CAMERA_REQUEST_CODE = 100;

    public boolean breakThread;

    public sendVehiclePicture hilofotos;
    private Handler handler = new Handler();

    public TaskCanceler taskCanceler;
    public long startTime,timefotos;
    public SharedPreferences app_preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_vehicle_pictures);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Fotografías");
        toolbar.setTitleTextColor(Color.BLACK);
        setSupportActionBar(toolbar);

        //get back arrow
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        try {
            polizaFolio = IinfoClient.getInfoClientObject().getPolicies().getNoPolicy();
            tok = IinfoClient.getInfoClientObject().getClient().getToken();
        }catch (Exception e){
            e.printStackTrace();
            Toast.makeText(this, "Atencion. Contacte al equipo de desarrollo para posible error.", Toast.LENGTH_SHORT).show();
            finish();
        }
        flag1 = false;
        flag2 = false;
        flag3 = false;
        flag4 = false;

        flag1valida = false;
        flag2valida = false;
        flag3valida = false;
        flag4valida = false;

        breakThread = false;
        hilofotos = new sendVehiclePicture();
        /*FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/

        //Si o tengo todas la fotos...ejecuto view para capturar fitis que falta
        /*
        if(!IinfoClient.InfoClientObject.getPolicies().isHasVehiclePictures()) {
            setContentView(R.layout.activity_vehicle_pictures);
            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            toolbar.setTitle("Fotografías");
            toolbar.setTitleTextColor(Color.BLACK);
            setSupportActionBar(toolbar);

            btn1 = (Button)findViewById(R.id.btn1);

            //get label
            TextView tv = (TextView) findViewById(R.id.leyenda);
            //replace nombre con el nombre que es...
            tv.setText(tv.getText().toString().replace("{nombre}", IinfoClient.InfoClientObject.getClient().getName() + " " +
                    IinfoClient.InfoClientObject.getClient().getLastName() + " " +
                    IinfoClient.InfoClientObject.getClient().getMotherName()
            ));

            //inicializa imagens con clicklistener
            Init();
        }
        //Ya tengo todas las foos...excetp odometro
        else
        {
            //coloco view de caputarr odometro
            //setContentView(R.layout.vehicle_odometer);
            //Init5();

            //lanzamos intent con odometro view...
            Intent odo = new Intent(VehiclePictures.this,VehicleOdometer.class);
            startActivity(odo);
        }
        */
        //inicializa imagens con clicklistener
        TextView leyenda = (TextView)findViewById(R.id.leyenda);
        TextView res = (TextView)findViewById(R.id.textView36);

        Typeface typefacebold = Typeface.createFromAsset(getAssets(), "fonts/herne.ttf");

        typeface = Typeface.createFromAsset(getAssets(), "fonts/herne1.ttf");
        leyenda.setTypeface(typeface);
        res.setTypeface(typeface);

        app_preferences= getSharedPreferences("miituo", Context.MODE_PRIVATE);

        timefotos = app_preferences.getLong("time",0);

        Calendar calendar = Calendar.getInstance();
        startTime = calendar.getTimeInMillis();
        app_preferences.edit().putLong("time",startTime).apply();
        Init();

        //show aler mensaje
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.mensaje_fotos);

        TextView textry = (TextView)dialog.findViewById(R.id.textView48);
        textry.setTypeface(typeface);
        textry = (TextView)dialog.findViewById(R.id.textView49);
        textry.setTypeface(typeface);
        textry = (TextView)dialog.findViewById(R.id.textView66);
        textry.setTypeface(typeface);
        textry = (TextView)dialog.findViewById(R.id.textView71);
        textry.setTypeface(typeface);
        textry = (TextView)dialog.findViewById(R.id.textView72);
        textry.setTypeface(typefacebold);
        textry = (TextView)dialog.findViewById(R.id.textView73);
        textry.setTypeface(typeface);
        textry = (TextView)dialog.findViewById(R.id.textView67);
        textry.setTypeface(typeface);
        textry = (TextView)dialog.findViewById(R.id.textView74);
        textry.setTypeface(typefacebold);
        textry = (TextView)dialog.findViewById(R.id.textView75);
        textry.setTypeface(typeface);
        textry = (TextView)dialog.findViewById(R.id.textView76);
        textry.setTypeface(typefacebold);
        textry = (TextView)dialog.findViewById(R.id.textView77);
        textry.setTypeface(typeface);
        textry = (TextView)dialog.findViewById(R.id.textView68);
        textry.setTypeface(typeface);

        TextView okbutton = (TextView)dialog.findViewById(R.id.textView70);
        okbutton.setTypeface(typeface);
        okbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        api=new ApiClient(VehiclePictures.this);

        dialog.show();
    }

    public void tomarfoto(int p,String name, boolean isupper23){

        LogHelper.log(this,LogHelper.user_interaction,"VehiclePictures."+name,"a tomar foto","",
                "","id:"+IinfoClient.getInfoClientObject().getPolicies().getId(),"");
        Intent takepic=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        //startActivityForResult(i, FRONT_VEHICLE);
        if (takepic.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            try {
                photoFile = createImageFile(name,p);
            } catch (IOException ex) {
                ex.printStackTrace();
                // Error occurred while creating the File...
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI;
                if(isupper23) {
                    photoURI = FileProvider.getUriForFile(VehiclePictures.this, "miituo.com.miituo.provider", photoFile);
                }
                else{
                    photoURI = Uri.fromFile(photoFile);
                }
                takepic.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takepic, p);
            }else{
                Toast.makeText(this,"Tuvimos un problema al tomar la imagen. Intente mas tarde.",Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == MY_CAMERA_REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //Toast.makeText(this, "camera permission granted", Toast.LENGTH_LONG).show();
                Intent takepic=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                //startActivityForResult(i, FRONT_VEHICLE);
                if (takepic.resolveActivity(getPackageManager()) != null) {
                    // Create the File where the photo should go
                    try {
                        photoFile = createImageFile(NOMBRETEMP,PERMISO);
                    } catch (IOException ex) {
                        // Error occurred while creating the File...
                        ex.printStackTrace();
                    }
                    // Continue only if the File was successfully created
                    if (photoFile != null) {
                        Uri photoURI = FileProvider.getUriForFile(VehiclePictures.this, "miituo.com.miituo.provider", photoFile);
                        //Uri photoURI = Uri.fromFile(photoFile);
                        takepic.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                        startActivityForResult(takepic, PERMISO);
                    }else{
                        Toast.makeText(this,"Tuvimos un problema al tomar la imagen. Intente mas tarde.",Toast.LENGTH_SHORT).show();
                    }
                }
            } else {
                Toast.makeText(this, "No se pueden tomar fotos. Acceso denegado.", Toast.LENGTH_LONG).show();
            }
        }
    }

    void Init()
    {
        if(app_preferences.getString("solofotos","null").equals("1")){

            if(fotosfaltantesList.size()>0) {
                flag4 = true;
                flag3 = true;
                flag2 = true;
                flag1 = true;
                for (int i = 0; i < fotosfaltantesList.size(); i++) {
                    int id = fotosfaltantesList.get(i).Id;
                    switch (id) {
                        case 1:
                            flag1 = false;
                            flag1valida = true;
                            break;
                        case 2:
                            flag2 = false;
                            flag2valida = true;
                            break;
                        case 3:
                            flag3 = false;
                            flag3valida = true;
                            break;
                        case 4:
                            flag4 = false;
                            flag4valida = true;
                            break;
                    }
                }
            }
        }

        Img1=(ImageView)findViewById(R.id.Img1);
        Img2=(ImageView)findViewById(R.id.Img2);
        Img3=(ImageView)findViewById(R.id.Img3);
        Img4=(ImageView)findViewById(R.id.Img4);


        if(!flag1) {
            Img1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (Build.VERSION.SDK_INT < 23) {

                        tomarfoto(FRONT_VEHICLE, "frontal",false);
                    } else {
                        if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                            NOMBRETEMP = "frontal";
                            PERMISO = FRONT_VEHICLE;
                            requestPermissions(new String[]{Manifest.permission.CAMERA}, MY_CAMERA_REQUEST_CODE);
                        } else {
                            tomarfoto(FRONT_VEHICLE, "frontal",true);
                        }
                    }
                }
            });
        }else{
            //busco imagen y la muestro
            SharedPreferences preferences = getSharedPreferences("miituo", Context.MODE_PRIVATE);
            mCurrentPhotoPath = preferences.getString("nombrefotofrontal"+polizaFolio, "null");

            String filePath = mCurrentPhotoPath;

            Glide.with(VehiclePictures.this)
                .load(filePath)
                .apply(new RequestOptions().override(150, 200).centerCrop())//.override(150,200)
                //.override(150,200)
                //.centerCrop()
                .into(Img1);
        }

        if(!flag2) {
            Img2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (Build.VERSION.SDK_INT < 23) {

                        tomarfoto(SIDE_RIGHT_VEHICLE, "derecho",false);

                    } else {
                        if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                            NOMBRETEMP = "derecho";
                            PERMISO = SIDE_RIGHT_VEHICLE;
                            requestPermissions(new String[]{Manifest.permission.CAMERA}, MY_CAMERA_REQUEST_CODE);
                        } else {
                            tomarfoto(SIDE_RIGHT_VEHICLE, "derecho",true);
                        }
                    }
                }
            });
        }else{
            //busco imagen y la muestro
            SharedPreferences preferences = getSharedPreferences("miituo", Context.MODE_PRIVATE);
            mCurrentPhotoPath = preferences.getString("nombrefotoderecho"+polizaFolio, "null");

            String filePath = mCurrentPhotoPath;

            Glide.with(VehiclePictures.this)
                .load(filePath)
                .apply(new RequestOptions().override(150, 200).centerCrop())//.override(150,200)
                //.override(150,200)
                //.centerCrop()
                .into(Img2);
        }
        if(!flag3) {
            Img3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (Build.VERSION.SDK_INT < 23) {

                        tomarfoto(REAR_VEHICLE, "back",false);

                    } else {
                        if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                            NOMBRETEMP = "back";
                            PERMISO = REAR_VEHICLE;
                            requestPermissions(new String[]{Manifest.permission.CAMERA}, MY_CAMERA_REQUEST_CODE);
                        } else {
                            tomarfoto(REAR_VEHICLE, "back",true);

                        }
                    }
                }
            });
        }
        else{
            //busco imagen y la muestro
            SharedPreferences preferences = getSharedPreferences("miituo", Context.MODE_PRIVATE);
            mCurrentPhotoPath = preferences.getString("nombrefotoback"+polizaFolio, "null");

            String filePath = mCurrentPhotoPath;

            Glide.with(VehiclePictures.this)
                    .load(filePath)
                    .apply(new RequestOptions().override(150, 200).centerCrop())//.override(150,200)
                    //.override(150,200)
                    //.centerCrop()
                    .into(Img3);
        }
        if(!flag4) {
            Img4.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (Build.VERSION.SDK_INT < 23) {

                        tomarfoto(SIDE_LEFT_VEHICLE, "izquierdo",false);
                    } else {
                        if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                            NOMBRETEMP = "izquierdo";
                            PERMISO = SIDE_LEFT_VEHICLE;
                            requestPermissions(new String[]{Manifest.permission.CAMERA}, MY_CAMERA_REQUEST_CODE);
                        } else {
                            tomarfoto(SIDE_LEFT_VEHICLE, "izquierdo",true);
                        }
                    }
                }
            });
        }else{
            //busco imagen y la muestro
            SharedPreferences preferences = getSharedPreferences("miituo", Context.MODE_PRIVATE);
            mCurrentPhotoPath = preferences.getString("nombrefotoizquierdo"+polizaFolio, "null");

            String filePath = mCurrentPhotoPath;

            Glide.with(VehiclePictures.this)
                    .load(filePath)
                    .apply(new RequestOptions().override(150, 200).centerCrop())//.override(150,200)
                    //.override(150,200)
                    //.centerCrop()
                    .into(Img4);
        }
    }

    /*public void savefrontiamge(View v){
        Intent takepic=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        //startActivityForResult(i, FRONT_VEHICLE);
        if (takepic.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File...
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                //Uri photoURI = FileProvider.getUriForFile(VehiclePictures.this, "miituo.com.miituo", photoFile);
                Uri photoURI = Uri.fromFile(photoFile);
                takepic.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takepic, FRONT_VEHICLE);
            }
        }
    }*/

    //Al tomar la foto...la procesamos en el activty for result
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        try {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode== Activity.RESULT_OK)
        {
            String user = "";
            String picName="";
                switch (requestCode){
                    case 1:
                        user = "frontal";
                        picName="Frontal";
                        break;
                    case 2:
                        user = "derecho";
                        picName="Lateral Derecho";
                        break;
                    case 3:
                        user = "back";
                        picName="Trasera";
                        break;
                    case 4:
                        user = "izquierdo";
                        picName="Lateral Izquierdo";
                        break;
                    default:
                        break;
                }

            LogHelper.log(this,LogHelper.backTask,"VehiclePictures.onActivityResult","se tomo foto: "+picName,"",
                    "","id:"+IinfoClient.getInfoClientObject().getPolicies().getId(),"");
                SharedPreferences preferences = getSharedPreferences("miituo", Context.MODE_PRIVATE);
                mCurrentPhotoPath = preferences.getString("nombrefoto"+user+polizaFolio, "null");

                String filePath = mCurrentPhotoPath;
                /*BitmapFactory.Options options = new BitmapFactory.Options();
                options.inSampleSize =1;

                bmp = BitmapFactory.decodeFile(filePath,options);*/

                //OutputStream imagefile = new FileOutputStream(filePath);
                // Write 'bitmap' to file using JPEG and 80% quality hint for JPEG:
                //bmp.compress(Bitmap.CompressFormat.JPEG, 80, imagefile);

                if (requestCode == 1) {
                    flag1 = true;
                    //comprimer imagen antes de lanzarla al imageview
                    //bmp.compress(Bitmap.CompressFormat.JPEG,15);

                    //Img1.setImageBitmap(resizedtoshow);
                    Glide.with(VehiclePictures.this)
                        .load(filePath)
                        .apply(new RequestOptions().override(150, 200).centerCrop())//.override(150,200)
                        //.override(150,200)
                        //.centerCrop()
                        .into(Img1);
                    //Img1.setImageBitmap(bmp);
                }
                if (requestCode == 2) {
                    flag2 = true;
                    //Bundle ext=data.getExtras();
                    //bmp=(Bitmap)ext.get("data");
                    //Img2.setImageBitmap(resizedtoshow);
                    //Img2.setImageBitmap(bmp);
                    Glide.with(VehiclePictures.this)
                        .load(filePath)
                        .apply(new RequestOptions().override(150, 200).centerCrop())//.override(150,200)
                        //.override(150,200)
                        //.centerCrop()
                        .into(Img2);
                }
                if (requestCode == 3) {
                    flag3 = true;
                    //Bundle ext=data.getExtras();
                    //bmp=(Bitmap)ext.get("data");
                    //Img3.setImageBitmap(resizedtoshow);
                    //Img3.setImageBitmap(bmp);
                    Glide.with(VehiclePictures.this)
                        .load(filePath)
                        .apply(new RequestOptions().override(150, 200).centerCrop())//.override(150,200)
                        //.override(150,200)
                        //.centerCrop()
                        .into(Img3);
                }
                if (requestCode == 4) {
                    flag4 = true;
                    //Bundle ext=data.getExtras();
                    //bmp=(Bitmap)ext.get("data");
                    //Img4.setImageBitmap(resizedtoshow);
                    //Img4.setImageBitmap(bmp);
                    Glide.with(VehiclePictures.this)
                        .load(filePath)
                        .apply(new RequestOptions().override(150, 200).centerCrop())//.override(150,200)
                        // .override(150,200)
                        //.centerCrop()
                        .into(Img4);
                }
                if (requestCode == 5) {
                    //Bundle ext=data.getExtras();
                    //bmp=(Bitmap)ext.get("data");
                    //Img5.setImageBitmap(resizedtoshow);
                    //Img5.setImageBitmap(bmp);
                    Glide.with(VehiclePictures.this)
                            .load(filePath)
                            .apply(new RequestOptions().override(150, 200).centerCrop())//.override(150,200)
                            //.override(150,200)
                            //.centerCrop()
                            .into(Img5);
                }
                IsTaken = true;
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

    private File createImageFile(String username, int tag) throws IOException {
        // Create an image file name
        try {
            String state = Environment.getExternalStorageState();
            File image;
            if (Environment.MEDIA_MOUNTED.equals(state)) {
                image = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES) + File.separator + username + startTime + polizaFolio + ".png");
            } else {
                image = new File(getFilesDir(), "pictures" + File.separator + username + startTime + polizaFolio + ".png");
            }

            // Save a file: path for use with ACTION_VIEW intents
            mCurrentPhotoPath = image.getAbsolutePath();

            SharedPreferences preferences = getSharedPreferences("miituo", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString("nombrefoto" + username + polizaFolio, mCurrentPhotoPath);
            editor.apply();
            return image;
        }catch(Exception e){
            e.printStackTrace();
            return null;
        }
    }

//******************************Thread to upload files**********************************************
    public void subirFotos(View v){
        //valida si ya subio todas las fotosv
        //if (ImageList.size() > 3 && !IinfoClient.InfoClientObject.getPolicies().isHasVehiclePictures()) {
        if (flag1 && flag2 && flag3 && flag4) {

            try {
                //create thread to set timeout
                //taskCanceler = new TaskCanceler(hilofotos);
                //handler.postDelayed(taskCanceler,20*1000);
                LogHelper.log(this,LogHelper.user_interaction,"VehiclePictures.btn1","Subiendo fotos...","",
                        "","id:"+IinfoClient.getInfoClientObject().getPolicies().getId(),"");
                hilofotos.execute();

                //sendVehiclePicture.execute();//.get(10000, TimeUnit.MILLISECONDS);
            }
            catch(Exception e){
                //so much time to upload photos...so break thread and continue...
                Log.e("Error","Se acabo el tiempo...");
                hilofotos.cancel(true);
            }
        } else {
            //si no ha subido las fotos...err
            Toast msg = Toast.makeText(getApplicationContext(), "Falta tomar las fotos obligatorias.", Toast.LENGTH_LONG);
            msg.show();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);

        if (item.getItemId() == android.R.id.home) {
            finish(); // close this activity and return to preview activity (if there is any)
        }
        return true;
    }

//*******************************************not attribute =! class*********************************
    public class sendVehiclePicture extends AsyncTask<Void, Void, Void> {
        public ProgressDialog progress=new ProgressDialog(VehiclePictures.this);
        String ErrorCode="";
        public boolean flag = true;
        public int cont = 0;

        public void showAlerta(){
            AlertDialog.Builder builder = new AlertDialog.Builder(VehiclePictures.this);
            builder.setTitle("Atención.");
            builder.setMessage("Hubo un problema al subir las fotos. Lo intentaremos más tarde.");
            builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    //change this...now launch odometer if error...
                    IinfoClient.InfoClientObject.getPolicies().setHasVehiclePictures(false);
                    //Intent odo = new Intent(VehiclePictures.this, VehicleOdometer.class);
                    //startActivity(odo);

                    Intent i=new Intent(VehiclePictures.this,PrincipalActivity.class);
                    startActivity(i);
                }
            });

            AlertDialog alerta = builder.create();
            alerta.show();
        }

        @Override
        protected void onCancelled() {
            try {
                //Toast msg = Toast.makeText(getApplicationContext(), "Ocurrio un Error:" + ErrorCode, Toast.LENGTH_LONG);
                //msg.show();
                LogHelper.sendLog(VehiclePictures.this,IinfoClient.getInfoClientObject().getPolicies().getId());
                progress.dismiss();
                showAlerta();
            }catch(Exception e){
                showAlerta();
            }
        }

        @Override
        protected void onPreExecute() {
            try {
                progress.setTitle("Subiendo Fotos del Vehículo...");
                progress.setMessage("Espere un momento en lo que subimos las imágenes de su vehiculo...");
                //progress.setIcon(R.drawable.miituo);
                progress.setCancelable(false);
                progress.setIndeterminate(false);
                progress.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                progress.setMax(4);
                progress.setProgress(0);
                progress.show();
            }catch(Exception e){
                showAlerta();
            }
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                String sf=app_preferences.getString("solofotos","null");
                LogHelper.log(VehiclePictures.this,LogHelper.backTask,"VehiclePictures.sendVehiclePictures","Inicio del proceso subir fotos autos","", "","","");
                if(sf.equals("1")) {

                    String username = "";
                    if(flag1valida){
                        username = "frontal";
                        launchImagen(username,1);
                    }
                    if(flag2valida){
                        username = "derecho";
                        launchImagen(username,2);
                    }
                    if(flag3valida){
                        username = "back";
                        launchImagen(username,3);
                    }
                    if(flag4valida){
                        username = "izquierdo";
                        launchImagen(username,4);
                    }
                    //lo mismo con los demas

                }else{
                //while(flag) {
                    //for(int i=0;i<ImageList.size();i++) {
                    for (int i = 1; i <= 4; i++) {
                        String username = "";
                        if (i == 1)
                            username = "frontal";
                        if (i == 2)
                            username = "derecho";
                        if (i == 3)
                            username = "back";
                        if (i == 4)
                            username = "izquierdo";

                        //File image = new File();

                        BitmapFactory.Options options = new BitmapFactory.Options();
                        options.inSampleSize = 4;
                        Bitmap bit = BitmapFactory.decodeFile(getExternalFilesDir(Environment.DIRECTORY_PICTURES) + File.separator + username + startTime + polizaFolio + ".png", options);

                        int a = api.UploadPhoto(i, bit, UrlApi, tok,"","0","0","");
                        if(a <= 4 && a != 0){
                            //subio correcto
                            progress.incrementProgressBy(1);
                            cont++;
                        }
                    }
                    //}

                    if(!flag){
                        this.cancel(true);
                    }
                }
            }
            catch (Exception ex)
            {
                ErrorCode=ex.getMessage();
                LogHelper.log(VehiclePictures.this,LogHelper.backTask,"VehiclePictures.sendVehiclePictures.doing","Error al subir imagenes de auto",
                        "", "","",LogHelper.getException(ex));
                this.cancel(true);
            }
            return null;
        }

    private void launchImagen(String username,int tipo) {

        try {
            SharedPreferences preferences = getSharedPreferences("miituo", Context.MODE_PRIVATE);
            mCurrentPhotoPath = preferences.getString("nombrefoto" + username + polizaFolio, "null");

            String filePath = mCurrentPhotoPath;

            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 4;
            Bitmap bit = BitmapFactory.decodeFile(filePath, options);

            int a = api.UploadPhoto(tipo, bit, UrlApi, tok,"","0","0","");
            if (a <= 4 && a != 0) {
                progress.incrementProgressBy(1);
                cont++;
            }
        }catch(Exception e){
            ErrorCode = e.toString();
            LogHelper.log(VehiclePictures.this,LogHelper.backTask,"VehiclePictures.sendVehiclePictures.launchImage","",
                    "", "","",LogHelper.getException(e));
            this.cancel(true);
        }
    }

    @Override
        protected void onPostExecute(Void aVoid) {
            try {
                new AlertDialog.Builder(VehiclePictures.this)
                    .setTitle("Fotos de Vehículo")
                    .setMessage("Las fotos se han subido correctamente. !Gracias¡")
                    .setCancelable(false)
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            progress.dismiss();
                            IinfoClient.InfoClientObject.getPolicies().setHasVehiclePictures(true);

                            //valido foto de odometro tmb antes de lanzar a sendodometer***
                            app_preferences= getSharedPreferences("miituo", Context.MODE_PRIVATE);
                            app_preferences.edit().putLong("time",startTime).apply();
                            String fotos = app_preferences.getString("solofotos","null");
                            if(fotos.equals("1")){
                                //aqu ivalido siguiente foto odometro
                                //entonces hay mas de una foto que solicitaron
                                //busco si hay de odometro, si no, entonces updateStauts
                                boolean miniodo = false;
                                for(int k=0;k<fotosfaltantesList.size();k++){
                                    if(fotosfaltantesList.get(k).getId() == 5){
                                        miniodo = true;
                                        break;
                                    }
                                }
                                if(miniodo){
                                    Intent i = new Intent(VehiclePictures.this,VehicleOdometer.class);
                                    startActivity(i);

                                }else{
                                    app_preferences.edit().putString("solofotos","0").apply();
                                    updateStatus odometro = new updateStatus();
                                    odometro.execute();

                                }
                            }else{
                                Intent odo = new Intent(VehiclePictures.this, VehicleOdometer.class);
                                startActivity(odo);
                            }
                            }
                        })
                        .show();
            }catch(Exception e){
                showAlerta();
            }
        }
    };

//reporte firt time*********************************************************************************
    //thread to call api and save furst odometer
    public class updateStatus extends AsyncTask<Void, Void, Void>  {
        ProgressDialog progress = new ProgressDialog(VehiclePictures.this);
        String ErrorCode = "";
        boolean response;

        @Override
        protected void onPreExecute() {
            progress.setTitle("Actualizando...");
            progress.setMessage("Procesando información.");
            //progress.setIcon(R.drawable.miituo);
            progress.setCancelable(false);
            progress.setIndeterminate(true);
            progress.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                //response = api.ConfirmOdometer(5, UrlConfirmOdometer);
                response = api.AjusteOdometerLast(5, UrlAjustelast,IinfoClient.getInfoClientObject().getPolicies().getId(),tok);

            } catch (IOException ex) {
                ErrorCode = ex.getMessage();
                this.cancel(true);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {

            if (!response) {
                //Toast msg = Toast.makeText(getApplicationContext(), "Error inesperado, no se pudo procesar la imagen", Toast.LENGTH_LONG);
                //msg.show();
                AlertDialog.Builder builder = new AlertDialog.Builder(VehiclePictures.this);
                builder.setTitle("Error");
                builder.setMessage("Error al subir información. Intente más tarde");
                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent i = new Intent(VehiclePictures.this, PrincipalActivity.class);
                        startActivity(i);
                    }
                });
                AlertDialog alerta = builder.create();
                alerta.show();
            } else {
                IinfoClient.getInfoClientObject().getPolicies().setLastOdometer(IinfoClient.getInfoClientObject().getPolicies().getRegOdometer());
                //IinfoClient.getInfoClientObject().getPolicies().setHasVehiclePictures(true);
                //IinfoClient.getInfoClientObject().getPolicies().setHasOdometerPicture(true);
                IinfoClient.getInfoClientObject().getPolicies().setReportState(12);

                //String rs = UpdateDataBase(modelBase.FeedEntryPoliza.TABLE_NAME,IinfoClient.getInfoClientObject().getPolicies().getNoPolicy());
                Intent i = new Intent(VehiclePictures.this, PrincipalActivity.class);
                i.putExtra("actualizar","1");
                startActivity(i);

                /*new android.app.AlertDialog.Builder(VehiclePictures.this)
                        .setTitle("Gracias.")
                        .setMessage("Tu información ha sido actualizada.")
                        //.setIcon(R.drawable.miituo)
                        .setCancelable(false)
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //progresslast.dismiss();
                                //finalizamos....
                                //IinfoClient.getInfoClientObject().getPolicies().setReportState(13);

                                //updateReportState();
                                IinfoClient.getInfoClientObject().getPolicies().setLastOdometer(IinfoClient.getInfoClientObject().getPolicies().getRegOdometer());
                                //IinfoClient.getInfoClientObject().getPolicies().setHasVehiclePictures(true);
                                //IinfoClient.getInfoClientObject().getPolicies().setHasOdometerPicture(true);
                                IinfoClient.getInfoClientObject().getPolicies().setReportState(12);

                                //String rs = UpdateDataBase(modelBase.FeedEntryPoliza.TABLE_NAME,IinfoClient.getInfoClientObject().getPolicies().getNoPolicy());

                                Intent i = new Intent(VehiclePictures.this, PrincipalActivity.class);
                                i.putExtra("actualizar","1");
                                startActivity(i);
                            }
                        })
                        .show();*/
            }
            //progress.dismiss();
        }

        @Override
        protected void onCancelled() {
            if (ErrorCode.equals("1000")) {
                progress.dismiss();
                Toast msg = Toast.makeText(getApplicationContext(), ErrorCode, Toast.LENGTH_LONG);
                msg.show();

                super.onCancelled();

            } else {
                progress.dismiss();

                Toast msg = Toast.makeText(getApplicationContext(), "Ocurrio un Error:" + ErrorCode, Toast.LENGTH_LONG);
                msg.show();
            }
        }
    };
}


