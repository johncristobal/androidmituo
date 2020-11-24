package miituo.com.miituo.activities;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SyncStatusObserver;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import miituo.com.miituo.R;
import miituo.com.miituo.api.ApiClient;
import miituo.com.miituo.data.DBaseMethods;
import miituo.com.miituo.data.IinfoClient;
import miituo.com.miituo.data.InfoClient;
import miituo.com.miituo.data.modelBase;
import miituo.com.miituo.utils.Evento;
import miituo.com.miituo.utils.LogHelper;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class LastOdometerActivity extends AppCompatActivity {

    public String odometroAnterior;

    public String tipoodometro,tok;
    SharedPreferences app_preferences;

    final String UrlAjuste="Ticket";
    final String UrlAjusteCasi="UpStateCasification";
    final String UrlAjustelast="Policy/UpdatePolicyStatusReport/";

    final String UrlConfirmOdometer="ImageProcessing/ConfirmOdometer";

    private ApiClient api;

    public Boolean response;
    public String respuesta,res;
    final String ApiSendReport="ReportOdometer/";

    public Typeface typeface;
    public Typeface typefacebold;

    public String datoamount = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_last_odometer);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        typeface = Typeface.createFromAsset(getAssets(), "fonts/herne1.ttf");
        typefacebold = Typeface.createFromAsset(getAssets(), "fonts/herne.ttf");

        TextView leyenda = (TextView)findViewById(R.id.textView8);
        TextView res = (TextView)findViewById(R.id.textView41);
        EditText edit = (EditText)findViewById(R.id.editTextConfirmaOdo);
        edit.setTypeface(typeface);
        leyenda.setTypeface(typeface);
        res.setTypeface(typeface);

        odometroAnterior = getIntent().getStringExtra("valor");
        tok = IinfoClient.getInfoClientObject().getClient().getToken();

        app_preferences= getSharedPreferences("miituo", Context.MODE_PRIVATE);
        tipoodometro = app_preferences.getString("odometro","null");

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

        //get image and how into imageview here
        SharedPreferences preferences = getSharedPreferences("miituo", Context.MODE_PRIVATE);
        String mCurrentPhotoPath = preferences.getString("nombrefotoodometro", "null");

        String filePath = mCurrentPhotoPath;

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 4;
        Bitmap bmp = BitmapFactory.decodeFile(filePath,options);

        ImageView vistaodo = findViewById(R.id.imageView18);
        vistaodo.setImageBitmap(bmp);

        api=new ApiClient(this);
    }

    public void validar(View v)
    {
        //get odometro
        EditText odo = (EditText)findViewById(R.id.editTextConfirmaOdo);
        String odometro = odo.getText().toString();

        //odometroAnterior = "1111111";
        //tipoodometro = "mensual";
        //validar que no sea ""
        //validar que coincida con el anterior
        //lanzar alertdialog custom para verificar odometro una vez mas...
        if(odometro.equals("")){
            Toast.makeText(this,"Es necesario capturar los kms. que marca el odómetro.",Toast.LENGTH_SHORT).show();
        } else if(!odometro.equals(odometroAnterior)){
            Toast.makeText(this,"Los odómetros no coinciden, por favor verifícalos.",Toast.LENGTH_SHORT).show();
        }else{
            //udpate value into listclient
            IinfoClient.InfoClientObject.getPolicies().setRegOdometer(Integer.parseInt(odometro));

            LogHelper.log(LastOdometerActivity.this,LogHelper.user_interaction,"LastOdometerActivity.validar","odometros coinciden:"+odometro,"",
                    "","id:"+IinfoClient.getInfoClientObject().getPolicies().getId(),"");
            //all it's ok...launch alertdialog custom_validation_odometer
            final Dialog dialog = new Dialog(this);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.custom_validation_odometer);
            //dialog.setTitle("Tu odómetro");

            DecimalFormat formatter = new DecimalFormat("#,###,###");
            Double dos = Double.parseDouble(odometro);
            String yourFormattedString = formatter.format(dos);
            TextView odom = (TextView)dialog.findViewById(R.id.textView44);
            odom.setTypeface(typeface);
            odom.setText(yourFormattedString);

            TextView title = (TextView)dialog.findViewById(R.id.textView42);
            TextView title1 = (TextView)dialog.findViewById(R.id.textView43);
            TextView title2 = (TextView)dialog.findViewById(R.id.textView45);
            TextView title3 = (TextView)dialog.findViewById(R.id.textView46);

            title.setTypeface(typeface);
            title1.setTypeface(typeface);
            title2.setTypeface(typeface);
            title3.setTypeface(typeface);

            //buttons actiosn
            Button cancel = (Button)dialog.findViewById(R.id.buttonCancel);
            Button aceptar = (Button)dialog.findViewById(R.id.buttonOk);

            aceptar.setTypeface(typeface);
            cancel.setTypeface(typeface);

            //canecler logic
            cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    //launch vehicle odometet again...
                    Intent i = new Intent(LastOdometerActivity.this, ConfirmActivity.class);
                    //dialog.dismiss();
                    startActivity(i);
                }
            });

            //aceptar logic
            aceptar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Aqui validamos si es first o meontlhy

                    String fotos = app_preferences.getString("solofotos","null");
                    LogHelper.log(LastOdometerActivity.this,LogHelper.user_interaction,"LastOdometerActivity.validar.dialog.aceptar","fotos:"+fotos+" tipoOdometro"+tipoodometro,"",
                            "","id:"+IinfoClient.getInfoClientObject().getPolicies().getId(),"");
                    if(fotos.equals("1")){
                        app_preferences.edit().putString("solofotos","0").apply();
                        sendOdometro odo = new sendOdometro();
                        odo.execute();
                    }
                    else if(tipoodometro.equals("first")){
                        reporteFirst();
                        //dialog.dismiss();
                    }else if(tipoodometro.equals("mensual")){
                        reporteMensual();
                        //dialog.dismiss();
                    }else if(tipoodometro.equals("cancela")){
                        ajusteMensual();    //mismo flujo para cancelacion
                        //dialog.dismiss();
                    }else if(tipoodometro.equals("ajuste")){
                        ajusteMensual();
                        //dialog.dismiss();
                    }else{
                        //dialog.dismiss();
                    }
                }
            });
            dialog.show();
        }
    }

//solo actualiza y ya*******************************************************************************
    //thread to call api and save furst odometer
    public class sendOdometro extends AsyncTask<Void, Void, Void>  {
        ProgressDialog progress = new ProgressDialog(LastOdometerActivity.this);
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

            LogHelper.log(LastOdometerActivity.this,LogHelper.backTask,"LastOdometerActivity.sendOdometer.onPostExecute","response"+response,"",
                    "","id:"+IinfoClient.getInfoClientObject().getPolicies().getId(),"");
            if (!response) {
                //Toast msg = Toast.makeText(getApplicationContext(), "Error inesperado, no se pudo procesar la imagen", Toast.LENGTH_LONG);
                //msg.show();
                AlertDialog.Builder builder = new AlertDialog.Builder(LastOdometerActivity.this);
                builder.setTitle("Error");
                builder.setMessage("Error al subir información. Intente más tarde");
                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent i = new Intent(LastOdometerActivity.this, PrincipalActivity.class);
                        startActivity(i);
                    }
                });
                AlertDialog alerta = builder.create();
                alerta.show();
            } else {
                new android.app.AlertDialog.Builder(LastOdometerActivity.this)
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

                                Intent i = new Intent(LastOdometerActivity.this, PrincipalActivity.class);
                                i.putExtra("actualizar","1");
                                startActivity(i);
                            }
                        })
                        .show();
            }
            //progress.dismiss();
        }

        @Override
        protected void onCancelled() {
            LogHelper.log(LastOdometerActivity.this,LogHelper.backTask,"LastOdometerActivity.sendOdometer.onCancelled","errorCode"+ErrorCode,"",
                    "","id:"+IinfoClient.getInfoClientObject().getPolicies().getId(),"");
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
            LogHelper.sendLog(LastOdometerActivity.this,IinfoClient.getInfoClientObject().getPolicies().getId());
        }
    };

//reporte firt time*****************************************************************************
    public void ajusteMensual(){
        //thread to call api and save furst odometer
        AsyncTask<Void, Void, Void> sendOdometro = new AsyncTask<Void, Void, Void>() {
            ProgressDialog progress = new ProgressDialog(LastOdometerActivity.this);
            String ErrorCode = "";

            @Override
            protected void onPreExecute() {
                progress.setTitle("Registrando odómetro");
                progress.setMessage("Procesando información.");
                //progress.setIcon(R.drawable.miituo);
                progress.setCancelable(false);
                progress.setIndeterminate(true);
                progress.show();
            }

            @Override
            protected Void doInBackground(Void... params) {
                try {
                    LogHelper.log(LastOdometerActivity.this,LogHelper.backTask,"LastOdometerActivity.ajusteMensual.onDoing","inicio back del ajuste","",
                            "","id:"+IinfoClient.getInfoClientObject().getPolicies().getId(),"");
                    //response = api.ConfirmOdometer(5, UrlConfirmOdometer);
                    response = api.AjusteOdometerCasification(5, UrlAjusteCasi,LastOdometerActivity.this,tok);
                    response = api.AjusteOdometerTicket(5, UrlAjuste,LastOdometerActivity.this,tok);
                    response = api.AjusteOdometerLast(5, UrlAjustelast,IinfoClient.getInfoClientObject().getPolicies().getId(),tok);

                } catch (IOException ex) {
                    ErrorCode = ex.getMessage();
                    this.cancel(true);
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {

                LogHelper.log(LastOdometerActivity.this,LogHelper.backTask,"LastOdometerActivity.ajusteMensual.onPostExecute","response"+response,"",
                        "","id:"+IinfoClient.getInfoClientObject().getPolicies().getId(),"");
                if (!response) {
                    //Toast msg = Toast.makeText(getApplicationContext(), "Error inesperado, no se pudo procesar la imagen", Toast.LENGTH_LONG);
                    //msg.show();
                    AlertDialog.Builder builder = new AlertDialog.Builder(LastOdometerActivity.this);
                    builder.setTitle("Error");
                    builder.setMessage("Error al subir información. Intente más tarde");
                    builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent i = new Intent(LastOdometerActivity.this, PrincipalActivity.class);
                            startActivity(i);
                        }
                    });
                    AlertDialog alerta = builder.create();
                    alerta.show();
                } else {
                    new android.app.AlertDialog.Builder(LastOdometerActivity.this)
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

                                    Intent i = new Intent(LastOdometerActivity.this, PrincipalActivity.class);
                                    i.putExtra("actualizar","1");
                                    startActivity(i);
                                }
                            })
                            .show();
                }
                //progress.dismiss();
            }

            @Override
            protected void onCancelled() {
                LogHelper.log(LastOdometerActivity.this,LogHelper.backTask,"LastOdometerActivity.ajusteMensual.onCancelled","errorCode: "+ErrorCode,"",
                        "","id:"+IinfoClient.getInfoClientObject().getPolicies().getId(),"");
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
                LogHelper.sendLog(LastOdometerActivity.this,IinfoClient.getInfoClientObject().getPolicies().getId());
            }

            /*public String UpdateDataBase(String...strings){

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
            }*/
        };
        sendOdometro.execute();
    }

    //reporte firt time*****************************************************************************
    public void reporteFirst(){
        //thread to call api and save furst odometer
        AsyncTask<Void, Void, Void> sendOdometro = new AsyncTask<Void, Void, Void>() {
            ProgressDialog progress = new ProgressDialog(LastOdometerActivity.this);
            String ErrorCode = "";

            @Override
            protected void onPreExecute() {
                progress.setTitle("Registrando odómetro");
                progress.setMessage("Procesando información.");
                //progress.setIcon(R.drawable.miituo);
                progress.setCancelable(false);
                progress.setIndeterminate(true);
                progress.show();
            }

            @Override
            protected Void doInBackground(Void... params) {
                try {
                    response = api.ConfirmOdometer(5, UrlConfirmOdometer,tok);

                } catch (IOException ex) {
                    ErrorCode = ex.getMessage();
                    LogHelper.log(LastOdometerActivity.this,LogHelper.backTask,"LastOdometerActivity.reporteFirst.doInB",
                            "error al subir 1er odometro","",
                            "","id:"+IinfoClient.getInfoClientObject().getPolicies().getId(),LogHelper.getException(ex));
                    this.cancel(true);
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {

                LogHelper.log(LastOdometerActivity.this,LogHelper.backTask,"LastOdometerActivity.reporteFirst.onPostExecute","response"+response,"",
                        "","id:"+IinfoClient.getInfoClientObject().getPolicies().getId(),"");
                if (!response) {
                    //Toast msg = Toast.makeText(getApplicationContext(), "Error inesperado, no se pudo procesar la imagen", Toast.LENGTH_LONG);
                    //msg.show();
                    AlertDialog.Builder builder = new AlertDialog.Builder(LastOdometerActivity.this);
                    builder.setTitle("Error");
                    builder.setMessage("Error al subir información. Intente más tarde");
                    builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent i = new Intent(LastOdometerActivity.this, PrincipalActivity.class);
                            i.putExtra("actualizar","1");
                            startActivity(i);
                        }
                    });
                    AlertDialog alerta = builder.create();
                    alerta.show();
                    LogHelper.sendLog(LastOdometerActivity.this,IinfoClient.getInfoClientObject().getPolicies().getId());
                } else {
                    new android.app.AlertDialog.Builder(LastOdometerActivity.this)
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
                                    IinfoClient.getInfoClientObject().getPolicies().setHasVehiclePictures(true);
                                    IinfoClient.getInfoClientObject().getPolicies().setHasOdometerPicture(true);
                                    IinfoClient.getInfoClientObject().getPolicies().setReportState(12);

                                    //String rs = UpdateDataBase(modelBase.FeedEntryPoliza.TABLE_NAME,IinfoClient.getInfoClientObject().getPolicies().getNoPolicy());

                                    Intent i = new Intent(LastOdometerActivity.this, PrincipalActivity.class);
                                    i.putExtra("actualizar","1");
                                    startActivity(i);
                                }
                            })
                            .show();
                }
                //progress.dismiss();
            }

            @Override
            protected void onCancelled() {
                LogHelper.log(LastOdometerActivity.this,LogHelper.backTask,"LastOdometerActivity.reporteFirst.onCancelled",
                        "error al subir 1er odometro","",
                        "","id:"+IinfoClient.getInfoClientObject().getPolicies().getId(),"");
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
                LogHelper.sendLog(LastOdometerActivity.this,IinfoClient.getInfoClientObject().getPolicies().getId());
            }

            /*public String UpdateDataBase(String...strings){

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
                        values.put(modelBase.FeedEntryPoliza.COLUMN_ODOMETERPIE, true);
                        values.put(modelBase.FeedEntryPoliza.COLUMN_VEHICLEPIE, true);
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
            }*/
        };
        sendOdometro.execute();
    }

    //reporte firt time*****************************************************************************
    public void getNewQuotation(final int mensualidad){
        //thread to call api and save furst odometer
        AsyncTask<Void, Void, Void> getQuotation = new AsyncTask<Void, Void, Void>() {
            ProgressDialog progress = new ProgressDialog(LastOdometerActivity.this);
            String ErrorCode = "";
            String resp="";
            @Override
            protected void onPreExecute() {
                progress.setTitle("Consultando información");
                progress.setMessage("Espere por favor");
                //progress.setIcon(R.drawable.miituo);
                progress.setCancelable(false);
                progress.setIndeterminate(true);
                progress.show();
            }

            @Override
            protected Void doInBackground(Void... params) {
                try {
                    resp = api.getQuotation( "Quotation/GetPreQuotationInfo/"+IinfoClient.getInfoClientObject().getPolicies().getId(),LastOdometerActivity.this);
//                    resp = api.getQuotation( "Quotation/GetPreQuotationInfo/165",LastOdometerActivity.this);
                } catch (Exception ex) {
                    ErrorCode = ex.getMessage();
                    this.cancel(true);
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                progress.dismiss();
                if (resp != null && !resp.equalsIgnoreCase("null")) {
                    try {
                        JSONObject jo = new JSONObject(resp);
                        if (mensualidad == 10) {
                            Intent i = new Intent(LastOdometerActivity.this, MainActivity.class);
                            i.putExtra("idPush", "2");
                            DateFormat df = new SimpleDateFormat("dd 'de' MMMM 'del' yyyy");
//                            Date today = IinfoClient.getInfoClientObject().getPolicies().getVigencyDate();
//                            String reportDate = df.format(today);


                            Calendar c = Calendar.getInstance();
                            Date fechacadena = IinfoClient.InfoClientObject.getPolicies().getVigencyDate();
                            c.setTime(fechacadena);
                            c.add(Calendar.DATE, -1);
                            fechacadena = c.getTime();
                            String reportDate = df.format(fechacadena);

                            i.putExtra("tarifa", jo.getString("strNewRate")+"/"+
                                     jo.getString("PolicyCost").substring(0,jo.getString("PolicyCost").indexOf("."))+"/"+reportDate+
                            "/"+IinfoClient.InfoClientObject.getPolicies().getCoverage().getDescription());
                            startActivity(i);
                        } else {
                            Intent i = new Intent(LastOdometerActivity.this, MainActivity.class);
                            i.putExtra("idPush", "3");
                            startActivity(i);
                        }
                    } catch (Exception e) {
                        ErrorCode = e.getMessage();
                        this.cancel(true);
                    }
                }
                else {
                    Intent i = new Intent(LastOdometerActivity.this, PrincipalActivity.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    i.putExtra("actualizar", "1");
                    startActivity(i);
                }
            }
            @Override
            protected void onCancelled() {
//                progress.dismiss();
                    Toast msg = Toast.makeText(getApplicationContext(), "Ocurrio un Error:" + ErrorCode, Toast.LENGTH_LONG);
                    msg.show();
                Intent i = new Intent(LastOdometerActivity.this, PrincipalActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                i.putExtra("actualizar", "1");
                startActivity(i);
            }
        };
        getQuotation.execute();
    }

//reporte mensaul****************************************************************************
    public void reporteMensual(){

        try {
            /*NumberFormat format = NumberFormat.getInstance(Locale.US);
            Number number = format.parse(odometro.getText().toString());
            //int d = number.intValue();//.doubleValue();
            int d = Integer.parseInt(odometro.getText().toString());//.doubleValue();
            Log.w("fon", d + "");*/

            IinfoClient.getInfoClientObject().getPolicies().setRegOdometer(Integer.parseInt(odometroAnterior));

            AsyncTask<Void, Void, Void> sendReporter = new AsyncTask<Void, Void, Void>() {
                ProgressDialog progress = new ProgressDialog(LastOdometerActivity.this);
                String ErrorCode = "";

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
                    LogHelper.log(LastOdometerActivity.this,LogHelper.backTask,"LastOdometerActivity.reporteMensual.onCancelled","errorCode"+ErrorCode,"",
                            "","id:"+IinfoClient.getInfoClientObject().getPolicies().getId(),"");
                    progress.dismiss();
                    Toast msg = Toast.makeText(getApplicationContext(), ErrorCode, Toast.LENGTH_LONG);
                    msg.show();

                    LogHelper.sendLog(LastOdometerActivity.this,IinfoClient.getInfoClientObject().getPolicies().getId());
                    super.onCancelled();
                }

                @Override
                protected Void doInBackground(Void... params) {
                    try {
                        respuesta = api.ConfirmReport(ApiSendReport,tok,false,false);

                    } catch (Exception ex) {
                        ErrorCode = ex.getMessage();
                        this.cancel(true);
                    }
                    return null;
                }

                @Override
                protected void onPostExecute(Void aVoid){
                    try {
                        LogHelper.log(LastOdometerActivity.this,LogHelper.backTask,"LastOdometerActivity.reporteMensual.onPostExecute","inicio pintar ticket"+"response"+respuesta,"",
                                "","id:"+IinfoClient.getInfoClientObject().getPolicies().getId(),"");
                        progress.dismiss();
                        JSONObject object = new JSONObject(respuesta);
                        datoamount = object.getString("Amount");
                        JSONArray arrayfee = new JSONArray(object.getString("Parameters"));
                        JSONArray cupones = object.isNull("Cupons")?new JSONArray():object.getJSONArray("Cupons");

                        String actual = odometroAnterior;
                        String recorridos="";
                        String fee = "";
                        String promo = "";
                        String feepormktarifa = "";
                        String anterior = "";
                        String diferencia = "";
                        String cobroportarifa = "";
                        String ivafee = "";
                        String ivatarifa = "";
                        String KmsCupon_Vacaciones="";
                        String KmsCupon_Cliente="";
                        String KmsCupon_Referido="";
                        String Cupones="";
                        String KmsDescuento="";
                        String TopeKms="";
                        String parametro_tope_kms="";

                        for (int i=0;i<arrayfee.length();i++){
                            JSONObject o=arrayfee.getJSONObject(i);
                            String name=o.getString("Name");
                            if(name.equalsIgnoreCase("Fee")){fee =o.getString("Amount");}
                            else if(name.equalsIgnoreCase("tarifa")){feepormktarifa =o.getString("Amount");}
                            else if(name.equalsIgnoreCase("Tope_Kms")){TopeKms =o.getString("Amount");}
                            else if(name.equalsIgnoreCase("kilometroAct")){actual =o.getString("Amount");}
                            else if(name.equalsIgnoreCase("kilometroReg")){anterior =o.getString("Amount");}
                            else if(name.equalsIgnoreCase("KmCobrado")){diferencia =o.getString("Amount");}
                            else if(name.equalsIgnoreCase("TarifaNeta")){cobroportarifa =o.getString("Amount");}
                            else if(name.equalsIgnoreCase("KmsCupon_Cliente")){KmsCupon_Cliente =o.getString("Amount");}
                            else if(name.equalsIgnoreCase("KmsCupon_Referido")){KmsCupon_Referido =o.getString("Amount");}
                            else if(name.equalsIgnoreCase("FIva")){ivafee =o.getString("Amount");}
                            else if(name.equalsIgnoreCase("IT")){ivatarifa =o.getString("Amount");}
                            else if(name.equalsIgnoreCase("Cupones")){Cupones =o.getString("Amount");}
                            else if(name.equalsIgnoreCase("KmsDescuento")){KmsDescuento =o.getString("Amount");}
                            else if(name.equalsIgnoreCase("promocion")){promo =o.getString("Amount");}
                            else if(name.equalsIgnoreCase("parametro_tope_kms")){parametro_tope_kms =o.getString("Amount");}
                            else if(name.equalsIgnoreCase("KmsCupon_Vacaciones")){KmsCupon_Vacaciones =o.getString("Amount");}
                        }

                        final Dialog dialog = new Dialog(LastOdometerActivity.this);
                        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        dialog.setContentView(R.layout.custom_alert);

                        TextView t47 = (TextView)dialog.findViewById(R.id.textView47);
                        t47.setTypeface(typeface);
                        t47 = (TextView)dialog.findViewById(R.id.textView55);
                        t47.setTypeface(typeface);
                        t47 = (TextView)dialog.findViewById(R.id.textView9);
                        t47.setTypeface(typeface);
                        t47 = (TextView)dialog.findViewById(R.id.textView12);
                        t47.setTypeface(typeface);
                        t47 = (TextView)dialog.findViewById(R.id.textView15);
                        t47.setTypeface(typeface);
                        if(!KmsDescuento.equalsIgnoreCase("")){t47.setText("Kms recorridos \ndesde el último reporte");}
                        t47 = (TextView)dialog.findViewById(R.id.textView20);
                        t47.setTypeface(typeface);
                        t47 = (TextView)dialog.findViewById(R.id.textView22);
                        t47.setTypeface(typeface);
                        t47 = (TextView)dialog.findViewById(R.id.textView18);
                        t47.setTypeface(typeface);
                        t47 = (TextView)dialog.findViewById(R.id.textView26);
                        t47.setTypeface(typeface);
                        t47 = (TextView)dialog.findViewById(R.id.textoiva);
                        t47.setTypeface(typeface);
                        t47 = (TextView)dialog.findViewById(R.id.lbCondonados);
                        t47.setTypeface(typeface);
                        TextView lbCondonados2 = (TextView)dialog.findViewById(R.id.lbCondonados2);
                        lbCondonados2.setTypeface(typeface);
                        t47 = (TextView)dialog.findViewById(R.id.lbVacaciones);
                        t47.setTypeface(typeface);
                        TextView lbVacaciones2 = (TextView)dialog.findViewById(R.id.lbVacaciones2);
                        lbVacaciones2.setTypeface(typeface);
                        t47 = (TextView)dialog.findViewById(R.id.lbReferidos);
                        t47.setTypeface(typeface);
                        TextView lbReferidos2 = (TextView)dialog.findViewById(R.id.lbReferidos2);
                        lbReferidos2.setTypeface(typeface);
                        TextView lbVacaciones=(TextView)dialog.findViewById(R.id.lbVacacionesHelp);
                        lbVacaciones.setTypeface(typeface);
                        lbVacaciones.setText("Este mes recorriste más de 1000 km,\npero no te preocupes solo te cobraremos 1000 km");
                        TextView lbTerminos = (TextView)dialog.findViewById(R.id.textoTerminos);
                        lbTerminos.setTypeface(typeface);
                        lbTerminos.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                String urlString="https://www.miituo.com/Terminos/getPDF?Url=C%3A%5CmiituoFTP%5CCondiciones_Generales.pdf";
                                Intent intent=new Intent(Intent.ACTION_VIEW);
                                intent.setDataAndType(Uri.parse("http://docs.google.com/gview?embedded=true&url=" + urlString), "text/html");
                                startActivity(intent);
                            }
                        });

                        recorridos=""+(Integer.parseInt(actual)-Integer.parseInt(anterior));

                        if(!KmsDescuento.equalsIgnoreCase("")){
                            LinearLayout ll=(LinearLayout)dialog.findViewById(R.id.cntCondonados);
                            ll.setVisibility(View.VISIBLE);
                            LinearLayout cntOdos=(LinearLayout)dialog.findViewById(R.id.cntOdometros);
                            cntOdos.setVisibility(View.VISIBLE);
                            int acobrar=Integer.parseInt(recorridos)-Integer.parseInt(KmsDescuento);
                            lbCondonados2.setText("-"+KmsDescuento);
                            diferencia=""+acobrar;
                            if(acobrar<=0){
                                diferencia="0";
                            }
                            else if((cupones.length()>0) && (acobrar>Integer.parseInt(parametro_tope_kms))){
                                acobrar=Integer.parseInt(parametro_tope_kms);
                                diferencia=""+acobrar;
                                lbVacaciones.setVisibility(View.VISIBLE);
                                LinearLayout ll2=(LinearLayout)dialog.findViewById(R.id.cntVacaciones);
                                ll2.setVisibility(View.VISIBLE);
                                lbVacaciones2.setText(diferencia);
                            }
                        }
                        else if(cupones.length()>0){
                            LogHelper.log(LastOdometerActivity.this,LogHelper.backTask,"LastOdometerActivity.reporteMensual.onPostExecute","pintando cupones ","",
                                    "","id:"+IinfoClient.getInfoClientObject().getPolicies().getId(),"");
                            for (int i=0;i<cupones.length();i++) {
                                JSONObject o = cupones.getJSONObject(i);
                                //if(o.getInt("Type")==3){
                                if(o.getInt("Type")==3 || o.getInt("Type")==5){
                                    lbVacaciones.setVisibility(View.VISIBLE);
                                    LinearLayout ll=(LinearLayout)dialog.findViewById(R.id.cntVacaciones);
                                    ll.setVisibility(View.VISIBLE);
                                    diferencia=parametro_tope_kms;
                                    lbVacaciones2.setText(diferencia);
                                    if(cupones.length()==2){
                                        //JSONObject o2 = cupones.getJSONObject(i+1);
                                        JSONObject o2 = cupones.getJSONObject(0);
                                        if (o.getInt("Type")==5){
                                            o2 = cupones.getJSONObject(0);
                                        }else{
                                            o2 = cupones.getJSONObject(1);
                                        }
                                        int dif=Integer.parseInt(parametro_tope_kms)-o2.getInt("Kms");
                                        diferencia=""+dif;
                                        LinearLayout ll2=(LinearLayout)dialog.findViewById(R.id.cntReferidos);
                                        ll2.setVisibility(View.VISIBLE);
                                        lbReferidos2.setText("-"+o2.getInt("Kms"));
//                                        DecimalFormat formattertarifa = new DecimalFormat("0.0000");
//                                        datoamount=""+formattertarifa.format((Double.parseDouble(diferencia)*Double.parseDouble(feepormktarifa)));
                                        break;
                                    }
                                }
                                if(o.getInt("Type")==1 || o.getInt("Type")==2){
                                    LinearLayout ll=(LinearLayout)dialog.findViewById(R.id.cntReferidos);
                                    ll.setVisibility(View.VISIBLE);
                                    int acobrar=Integer.parseInt(recorridos)-o.getInt("Kms");
                                    lbReferidos2.setText("-"+o.getInt("Kms"));
                                    diferencia=""+acobrar;
                                    if(acobrar<=0){diferencia="0";datoamount="0.0";}
//                                    else{
//                                        DecimalFormat formattertarifa = new DecimalFormat("0.0000");
//                                        datoamount=""+formattertarifa.format((Double.parseDouble(diferencia)*Double.parseDouble(feepormktarifa)));
//                                    }
                                    if(cupones.length()==2){
                                        JSONObject o2 = cupones.getJSONObject(i+1);
                                        int dif=Integer.parseInt(parametro_tope_kms)-o.getInt("Kms");
                                        diferencia=""+dif;
//                                        diferencia="1100";
                                        LinearLayout ll2=(LinearLayout)dialog.findViewById(R.id.cntVacaciones);
                                        ll2.setVisibility(View.VISIBLE);
                                        lbVacaciones.setVisibility(View.VISIBLE);
                                        lbVacaciones2.setText(parametro_tope_kms);
//                                        DecimalFormat formattertarifa = new DecimalFormat("0.0000");
//                                        datoamount=""+formattertarifa.format((Double.parseDouble(diferencia)*Double.parseDouble(feepormktarifa)));
                                        break;
                                    }
                                }
                            }
                        }

                        Button cancel = (Button)dialog.findViewById(R.id.button3);
                        cancel.setTypeface(typeface);
                        cancel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                LogHelper.log(LastOdometerActivity.this,LogHelper.user_interaction,"LastOdometerActivity.reporteMensual.onPostExecute.dialog","usuario cerro el ticket","",
                                        "","id:"+IinfoClient.getInfoClientObject().getPolicies().getId(),"");
                                Intent i = new Intent(LastOdometerActivity.this, ConfirmActivity.class);
                                startActivity(i);
                            }
                        });
                        Button aceptar = (Button)dialog.findViewById(R.id.button2);
                        aceptar.setTypeface(typeface);
                        aceptar.setOnClickListener(new View.OnClickListener() {
                            //call the other method to confirm odometer...

                            @Override
                            public void onClick(View view) {
                                LogHelper.log(LastOdometerActivity.this,LogHelper.user_interaction,"LastOdometerActivity.reporteMensual.onPostExecute.dialog","usuario acepta ticket","",
                                        "","id:"+IinfoClient.getInfoClientObject().getPolicies().getId(),"");
                                Evento.eventRecord(LastOdometerActivity.this,Evento.REPORTE);
                                saygoodbye();
                            }
                        });

                        DecimalFormat formatter = new DecimalFormat("$ #,###.00");
                        //GET textviews to show info

                        DecimalFormat formatterTwo = new DecimalFormat("#,###,###");

                        //odometros-----------------------------------------------------------------
                        TextView hoy = (TextView)dialog.findViewById(R.id.textView11);
                        hoy.setTypeface(typeface);

                        Double dos = Double.parseDouble(actual);
                        String yourFormattedString = formatterTwo.format(dos);
                        hoy.setText(yourFormattedString);

                        TextView antes = (TextView)dialog.findViewById(R.id.textView14);
                        antes.setTypeface(typeface);
                        Double antesdos = Double.parseDouble(anterior);
                        String yourFormattedStringTwo = formatterTwo.format(antesdos);
                        antes.setText(yourFormattedStringTwo);

                        TextView difer = (TextView)dialog.findViewById(R.id.lbDiferencia);
                        difer.setTypeface(typeface);
//                        Double antestres = Double.parseDouble(diferencia);
                        Double antestres = Double.parseDouble(recorridos);
                        String formatedThree = formatterTwo.format(antestres);
                        difer.setText(formatedThree);

                        //Tarifas-------------------------------------------------------------------
                        double s1 = Double.parseDouble(feepormktarifa);
                        TextView tarifa = (TextView)dialog.findViewById(R.id.textView21);
                        tarifa.setTypeface(typeface);
                        DecimalFormat formattertarifa = new DecimalFormat("$ 0.0000");
                        tarifa.setText(formattertarifa.format(s1));

                        //str = fee.toString().replaceAll("[^\\d]", "");
                        s1 = Double.parseDouble(fee);
                        Double s2 = Double.parseDouble(ivafee);
                        TextView basepormes = (TextView)dialog.findViewById(R.id.textView19);
                        basepormes.setTypeface(typefacebold);
                        //basepormes.setText(formatter.format(Math.round(s1+s2)));
                        basepormes.setText(formatter.format((s1+s2)));

                        if(fee.equals("0")){
                            basepormes.setVisibility(View.GONE);
                            TextView t18 = (TextView)dialog.findViewById(R.id.textView18);
                            t18.setVisibility(View.GONE);
                        }

                        //str = cobroportarifa.toString().replaceAll("[^\\d]", "");
                        s1 = Double.parseDouble(cobroportarifa);
                        Double s3 = Double.parseDouble(ivatarifa);
                        TextView tarifafinal = (TextView)dialog.findViewById(R.id.textView24);
                        tarifafinal.setTypeface(typefacebold);
                        tarifafinal.setText(diferencia);//formatter.format(s1+s3));

                        //str = dato.toString().replaceAll("[^\\d]", "");
                        s1 = Double.parseDouble(datoamount);
                        //TextView ending = (TextView)dialog.findViewById(R.id.textView27);
                        TextView ending2 = (TextView)dialog.findViewById(R.id.textView50);
                        //ending.setTypeface(typeface);
                        ending2.setTypeface(typefacebold);
                        //ending.setText(formatter.format(s1));
                        if(datoamount.equalsIgnoreCase("0.0")){
                            ending2.setText("$ 0.00");
                        }
                        else {
                            ending2.setText(formatter.format(s1));
                        }
                        dialog.show();
                    }catch(Exception e){

                        e.printStackTrace();
                    }
                }
            };
            sendReporter.execute();
        }
        catch(Exception e){
            LogHelper.log(LastOdometerActivity.this,LogHelper.backTask,"LastOdometerActivity.reporteMensual.onPostExecute","","",
                    "","id:"+IinfoClient.getInfoClientObject().getPolicies().getId(),LogHelper.getException(e));
            e.printStackTrace();
        }
    }

    public void saygoodbye(){
        AsyncTask<Void, Void, Void> sendthelast = new AsyncTask<Void, Void, Void>() {
            ProgressDialog progress = new ProgressDialog(LastOdometerActivity.this);
            String ErrorCode = "";

            @Override
            protected void onPreExecute() {
                progress.setTitle("Estamos realizando tu reporte.");
                progress.setMessage("Esto puede tardar un momento, espere por favor...");
                progress.setIndeterminate(true);
                progress.setCancelable(false);
                //progress.setIcon(R.drawable.miituo);
                progress.show();
            }

            @Override
            protected void onCancelled() {
                try {
                    LogHelper.log(LastOdometerActivity.this, LogHelper.backTask, "LastOdometerActivity.sayGoodBye.onCancelled", "errorCode: " + ErrorCode, "",
                            "", "id:" + IinfoClient.getInfoClientObject().getPolicies().getId(), "");
                    progress.dismiss();
                    //Toast msg = Toast.makeText(getApplicationContext(), ErrorCode, Toast.LENGTH_LONG);
                    //msg.show();

                    new android.app.AlertDialog.Builder(LastOdometerActivity.this)
                            .setTitle("Atención usuario...")
                            .setMessage(ErrorCode)
                            //.setIcon(R.drawable.miituo)
                            .setCancelable(false)
                            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    //progresslast.dismiss();
                                    Intent i = new Intent(LastOdometerActivity.this, PrincipalActivity.class);
                                    i.putExtra("actualizar", "1");
                                    startActivity(i);
                                }
                            })
                            .show();

                    super.onCancelled();

                    LogHelper.sendLog(LastOdometerActivity.this, IinfoClient.getInfoClientObject().getPolicies().getId());
                }catch(Exception e){
                    new android.app.AlertDialog.Builder(LastOdometerActivity.this)
                            .setTitle("Atención usuario...")
                            .setMessage("Tuvimos un problema, intente más tarde.")
                            //.setIcon(R.drawable.miituo)
                            .setCancelable(false)
                            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    //progresslast.dismiss();
                                    Intent i = new Intent(LastOdometerActivity.this, PrincipalActivity.class);
                                    i.putExtra("actualizar", "1");
                                    startActivity(i);
                                }
                            })
                            .show();
                }
            }

            @Override
            protected Void doInBackground(Void... params) {
                //before launch alert, we have to send the confirmReport
                try {
                    res = api.ConfirmReportLast("ReportOdometer/Confirmreport",tok);

                }catch(Exception e){
                    e.printStackTrace();
                    ErrorCode = "Al parecer tuvimos un problema, inténtalo más tarde.";
                    this.cancel(true);
                }

                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                progress.dismiss();

                String title = "Tu reporte se ha realizado";
                String mensajee = "Gracias por ser parte del consumo equitativo.";
                if(datoamount.equals("0.0")){
                    title = "Gracias";
                    mensajee = "Recibimos tu reporte mensual de odómetro.";
                }else if (IinfoClient.getInfoClientObject().getPolicies().getPaymentType().equals("AMEX")){
                    title = "Tu pago esta en proceso.";
                    mensajee = "En cuanto quede listo te lo haremos saber.";
                }

                if (res.equals("false") || res.equals("true"))
                {
                    LogHelper.log(LastOdometerActivity.this,LogHelper.backTask,"LastOdometerActivity.sayGoodBye.onPostExecute","reporte realizado, response: "+res,"",
                            "","id:"+IinfoClient.getInfoClientObject().getPolicies().getId(),"");
                    new android.app.AlertDialog.Builder(LastOdometerActivity.this)
                            .setTitle(title)
                            .setMessage(mensajee)
                            //.setIcon(R.drawable.miituo)
                            .setCancelable(false)
                            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    //progresslast.dismiss();
                                    //finalizamos....
                                    //IinfoClient.getInfoClientObject().getPolicies().setReportState(13);

                                    //updateReportState();
                                    IinfoClient.getInfoClientObject().getPolicies().setReportState(12);
                                    IinfoClient.getInfoClientObject().getPolicies().setLastOdometer(IinfoClient.getInfoClientObject().getPolicies().getRegOdometer());

                                    //String rs = UpdateDataBase(modelBase.FeedEntryPoliza.TABLE_NAME,IinfoClient.getInfoClientObject().getPolicies().getNoPolicy());
//                                    IinfoClient.getInfoClientObject().getPolicies().setMensualidad(10);
                                    if(IinfoClient.getInfoClientObject().getPolicies().getMensualidad()==10){
                                        getNewQuotation(10);
                                    }
                                    else if(IinfoClient.getInfoClientObject().getPolicies().getMensualidad()==11){
                                        getNewQuotation(11);
                                    }
                                    else {
                                        Intent i = new Intent(LastOdometerActivity.this, PrincipalActivity.class);
                                        i.putExtra("actualizar", "1");
                                        startActivity(i);
                                    }
                                }
                            })
                            .show();
                    LogHelper.clearLog(LastOdometerActivity.this);
                } else {

                    LogHelper.log(LastOdometerActivity.this,LogHelper.backTask,"LastOdometerActivity.sayGoodBye.onPostExecute","error en el reporte: response: "+res,"",
                            "","id:"+IinfoClient.getInfoClientObject().getPolicies().getId(),"");
                    if (res.contains("Por el momento tu pago")) {
                        String mensaje;
                        try {
                            JSONObject mes = new JSONObject(res);
                            mensaje = mes.getString("Message");
                            new android.app.AlertDialog.Builder(LastOdometerActivity.this)
                                    .setTitle("Odómetro")
                                    .setMessage(mensaje)
                                    //.setIcon(R.drawable.miituo)
                                    .setCancelable(false)
                                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            //progresslast.dismiss();
                                            Intent i = new Intent(LastOdometerActivity.this, PrincipalActivity.class);
                                            i.putExtra("actualizar", "1");
                                            startActivity(i);
                                        }
                                    })
                                    .show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    } else {
                        new android.app.AlertDialog.Builder(LastOdometerActivity.this)
                                .setTitle("Odómetro no reportado")
                                .setMessage("Problema al reportar odómetro, intente más tarde.")
                                //.setIcon(R.drawable.miituo)
                                .setCancelable(false)
                                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        //progresslast.dismiss();
                                        Intent i = new Intent(LastOdometerActivity.this, PrincipalActivity.class);
                                        i.putExtra("actualizar", "1");
                                        startActivity(i);
                                    }
                                })
                                .show();
                    }
                }
            }

            /*public String UpdateDataBase(String...strings){
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
            }*/
        };
        sendthelast.execute();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.manu_principal,menu);
        //invalidateOptionsMenu();

        //Aqui necesito el estatus para validarlo y saber si muestro o no el menu...
        //when push notification...open activity to take odometer

        //get regOdometer and validate...
        //validate regOdometer to set visible the menu...
        //String res = "0";
        //if(res.equals("1")){

        /*else if (regOdometer == 13){

        }*/

        //MenuItem item = menu.findItem(R.id.rpt_odometer);
        //item.setVisible(false);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        /*switch (item.getItemId())
        {
            case R.id.rpt_odometer:
                Intent i=new Intent(this,ReportOdometerActivity.class);
                startActivity(i);
                break;
        }*/

        if (item.getItemId() == android.R.id.home) {
            finish(); // close this activity and return to preview activity (if there is any)
            //Intent i = new Intent(LastOdometerActivity.this, PrincipalActivity.class);
            //startActivity(i);
        }
        return true;
    }
}
