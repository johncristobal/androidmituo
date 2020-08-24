package miituo.com.miituo.activities;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.telephony.TelephonyManager;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.browser.customtabs.CustomTabsIntent;
import androidx.browser.customtabs.TrustedWebUtils;
import androidx.browser.trusted.TrustedWebActivityIntentBuilder;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.security.ProviderInstaller;
import com.google.androidbrowserhelper.trusted.TwaLauncher;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import com.uxcam.UXCam;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import miituo.com.miituo.R;
import miituo.com.miituo.VehicleModelAdapter;
import miituo.com.miituo.api.ApiClient;
import miituo.com.miituo.data.ClientMovil;
import miituo.com.miituo.data.DBaseMethods;
import miituo.com.miituo.data.InfoClient;
import miituo.com.miituo.data.InsurancePolicyDetail;
import miituo.com.miituo.data.VehicleMovil;
import miituo.com.miituo.data.modelBase;
import miituo.com.miituo.threats.AvailableSiteSync;
import miituo.com.miituo.threats.GetCuponAsync;
import miituo.com.miituo.threats.GetPoliciesData;
import miituo.com.miituo.threats.GetSync;
import miituo.com.miituo.threats.GetTokenSmsSync;
import miituo.com.miituo.tuto.TutorialActivity;
import miituo.com.miituo.utils.Evento;
import miituo.com.miituo.utils.KeyChainStore;
import miituo.com.miituo.utils.LogHelper;
import miituo.com.miituo.utils.SimpleCallBack;

import static miituo.com.miituo.activities.MainActivity.result;
import static miituo.com.miituo.activities.MainActivity.token;

public class SyncActivity extends AppCompatActivity {
    //Array Adapter that will hold our ArrayList and display the items on the ListView
    private ListView vList;
    private VehicleModelAdapter vadapter;
    SharedPreferences app_preferences;
    EditText telefono;

    //public static String token;

    //public threadtosync hilos;

    public Typeface typeface;
    public AlertDialog alerta;

    public ClientMovil cli;
    public String tokencliente;
    TextView b4,hola,hola1,hola2,hola3,hola4,hola5,hola6,hola17,lbVigencia;
    public ProgressDialog progress;
    private LinearLayout cntHola,cntPromo;
    private ImageView imgPromo;
    public static int kms=0;
    public static String codigoCupon="";


    public void installTls12() {
        try {
            ProviderInstaller.installIfNeeded(this);
        } catch (GooglePlayServicesRepairableException e) {
            // Prompt the user to install/update/enable Google Play services.
            GoogleApiAvailability.getInstance()
                    .showErrorNotification(this, e.getConnectionStatusCode());
        } catch (GooglePlayServicesNotAvailableException e) {
            // Indicates a non-recoverable error: let the user know.
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sync);
        installTls12();
        typeface = Typeface.createFromAsset(getAssets(), "fonts/herne1.ttf");
        Typeface typeface2 = Typeface.createFromAsset(getAssets(), "fonts/herne.ttf");

        hola = (TextView) findViewById(R.id.textView3);
        hola1 = (TextView) findViewById(R.id.textView2);
        hola2 = (TextView) findViewById(R.id.textView7);
        hola3 = (TextView) findViewById(R.id.textView5);
        hola4 = (TextView) findViewById(R.id.textView10);
        hola5 = (TextView) findViewById(R.id.textView13);
        hola6 = (TextView) findViewById(R.id.textView16);
        TextView hola14 = (TextView) findViewById(R.id.textView14);
        hola17 = (TextView) findViewById(R.id.textView17);
        b4 = (TextView) findViewById(R.id.button4);
        cntHola = (LinearLayout) findViewById(R.id.cntHola);
        cntPromo = (LinearLayout) findViewById(R.id.cntPromo);
        lbVigencia = (TextView) findViewById(R.id.lbVigencia);
        imgPromo = (ImageView) findViewById(R.id.imgPromo);

        b4.setTypeface(typeface, Typeface.BOLD);
        hola.setTypeface(typeface);
        hola1.setTypeface(typeface);
        hola2.setTypeface(typeface2);
        hola3.setTypeface(typeface);
        hola4.setTypeface(typeface);
        hola5.setTypeface(typeface);
        hola6.setTypeface(typeface);
        hola14.setTypeface(typeface);
        hola17.setTypeface(typeface);

        progress = new ProgressDialog(SyncActivity.this);

        //hilos = new threadtosync();

        app_preferences = getSharedPreferences("miituo", Context.MODE_PRIVATE);
        telefono = (EditText) findViewById(R.id.cephonetext);
        try {
            String mPhoneNumber = "";
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            AlertDialog alert = builder.create();
            boolean newer = getIntent().getBooleanExtra("newer", false);
            if (newer) {
                hola5.setVisibility(View.GONE);
                hola6.setVisibility(View.GONE);
                b4.setTextColor(Color.WHITE);
                b4.setBackground(getResources().getDrawable(R.drawable.button));
            } else {
                hola5.setVisibility(View.VISIBLE);
                hola6.setVisibility(View.VISIBLE);
            }
            if (!app_preferences.contains("Celphone")) {
                pintaCupon();
            } else {
                if (!app_preferences.getString("Celphone", "Default").equals("")) {
                    telefono.setText(app_preferences.getString("Celphone", "Default"));

                    boolean act = getIntent().getBooleanExtra("actualizar", true);
                    if (act) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            boolean showPermiso = shouldShowRequestPermissionRationale(Manifest.permission.READ_PHONE_STATE);
                            if (!showPermiso) {
                                GetSync();
                            }
                        } else {
                            GetSync();
                        }
                    }
                }
                else{
                    pintaCupon();
                }
            }

            lanzaralertapush();

        } catch (Exception e) {
            e.printStackTrace();
        }

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        //every three digits, must set a coma in the edit...
        telefono.setTypeface(typeface);
        InputFilter[] filterArray = new InputFilter[1];
        filterArray[0] = new InputFilter.LengthFilter(10);
        telefono.setFilters(filterArray);
       /* telefono.addTextChangedListener(new TextWatcher() {
            String c="";
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(charSequence.length()==10){
                    c=charSequence.toString();
                }
            else{
            c="";
            }
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void afterTextChanged(Editable s) {
                if(s.length()>10){
                 telefono.setText(c);
                }
            }
        });*/

//       try{
//           String a=null;
//           a.length();
//       }
//       catch (Exception e){
//           String b=e.getLocalizedMessage() +"  :::  "+e.getStackTrace()[0].getLineNumber();
//           Log.d("exc","ex: "+b);
//       }
        String isLogPending=new KeyChainStore(this).fetchValueForKey("logPending");
        if(isLogPending.equalsIgnoreCase("1")){
            String idPolizaPending=new KeyChainStore(this).fetchValueForKey("idPending");
            LogHelper.sendLog(this,Integer.valueOf(idPolizaPending));
        }
    }

    @Override
    public void onResume(){
        super.onResume();
        if (!app_preferences.contains("Celphone")) {
            pintaCupon();
        } else {
            if (app_preferences.getString("Celphone", "Default").equals("")){
                pintaCupon();
            }
        }
    }
    private void pintaCupon(){
        kms=0;
        codigoCupon="";
        LogHelper.log(this,LogHelper.backTask,"SyncActivity.getingCoupon","","", "","","");

        new GetSync(this, "Cupon/getCouponFromLanding/Cotizar", (status, res) -> {
            if(status){
                try{
                    JSONObject cupon=new JSONObject(res);
                    String urlImg=cupon.getJSONArray("Vigencias").getJSONObject(0).getString("URL_ImagenMobile");
                    String txtVig=cupon.getJSONArray("Vigencias").getJSONObject(0).getString("TextoVigenciaCupon");
                    kms=cupon.getInt("Kms");
                    codigoCupon=cupon.getString("CodigoCupon");

                    Picasso.get().invalidate(urlImg);
                    Picasso.get().load(urlImg)
                            .memoryPolicy(MemoryPolicy.NO_CACHE)
                            .networkPolicy(NetworkPolicy.NO_CACHE)
                            .into(imgPromo);

                    cntHola.setVisibility(View.GONE);
                    hola3.setText("Bienvenido a ");
                    lbVigencia.setText(txtVig);
                    cntPromo.setVisibility(View.VISIBLE);
                    lbVigencia.setVisibility(View.VISIBLE);
                    hola5.setVisibility(View.GONE);
                    b4.setTextColor(Color.WHITE);
                    b4.setBackground(getResources().getDrawable(R.drawable.button));
                    Spannable wordtoSpan = new SpannableString(txtVig);
                    wordtoSpan.setSpan(new ForegroundColorSpan(getApplicationContext().getResources().getColor(R.color.azul_miituo)), txtVig.lastIndexOf("Consu"), txtVig.length()-1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    lbVigencia.setText(wordtoSpan);
                    LogHelper.log(SyncActivity.this,LogHelper.backTask,"SyncActivity.getingCoupon","obtencion correcta","", "","","");
                    lbVigencia.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String urlString="https://www.miituo.com/Terminos/getPDF?Url=C%3A%5CmiituoFTP%5CTerminos%20y%20condiciones%20miituo.pdf";
                            Intent intent=new Intent(Intent.ACTION_VIEW);
                            intent.setDataAndType(Uri.parse("http://docs.google.com/gview?embedded=true&url=" + urlString), "text/html");
                            startActivity(intent);
                        }
                    });
//                        lbVigencia.setText("Valido del 29 de Diciembre del 2019 al 3 de Enero del 2020.");
                }
                catch (Exception e){
                    LogHelper.log(SyncActivity.this,LogHelper.backTask,"SyncActivity.getingCoupon","obtencion incorrecta","", "","",LogHelper.getException(e));
                    e.printStackTrace();
                }
            }
        }).execute();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(this, "Permission denied to read your External storage", Toast.LENGTH_SHORT).show();
                }
                return;
            }
        }
    }

    private void lanzaralertapush() {

        ActivityCompat.requestPermissions(SyncActivity.this,
                new String[]{Manifest.permission.READ_PHONE_STATE},
                1);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Atención usuario");
        builder.setCancelable(false);
        builder.setMessage("Le recordamos que es importante activar las notificaciones para saber cuando reportar.");
        builder.setPositiveButton("Si, dejar notificaciones activas", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                app_preferences.edit().putString("pushalert", "si").apply();
            }
        });

        builder.setNegativeButton("No, desactivar notificaciones", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                app_preferences.edit().putString("pushalert", "no").apply();
            }
        });


        String alerta = app_preferences.getString("pushalert", "null");
        if (alerta.equals("null")) {
            AlertDialog alertashow = builder.create();
            alertashow.show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_listapolizas, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        switch (item.getItemId()) {
            /*
             * Autor: moon
             * Ojo, aqui no lo borres aun, podria dar en cancelar
             * cuando tngas el nuev numeero, entonces si borras y actualizas
             */
            case R.id.cambia_numero:
                SharedPreferences.Editor editor = app_preferences.edit();
                editor.putString("Celphone", "");
                editor.commit();
                setContentView(R.layout.activity_sync);

                break;
        }
        return true;
    }

    /*
     * Autor: moon
     * Thread to get data (json) from WS
     * Show listview with data
     * */

    public AlertDialog alertaCotiz;

    public void openView(final View v) {
        LogHelper.log(SyncActivity.this,LogHelper.backTask,"SyncActivity.goingtoQuot","iniciando","", "","","");
        new AvailableSiteSync(this, new SimpleCallBack() {
            @Override
            public void run(boolean status, String res) {

                if (!status) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(SyncActivity.this);
                    builder.setTitle("Alerta");
                    builder.setMessage("Error de conexión");
                    builder.setCancelable(false);
                    builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            alertaCotiz.dismiss();
                        }
                    });
                    alertaCotiz = builder.create();
                    alertaCotiz.show();
                } else {
                  if (res.equalsIgnoreCase("0")) {
                      if(MainActivity.isRecorderEnabled!=null) {
                          try{
                              JSONArray array=new JSONArray(MainActivity.isRecorderEnabled);
                              JSONObject o= array.getJSONObject(0);
                              String key=o.getString("Valor");
                              if (key!=null && !key.equals("")) {
                                  UXCam.startWithKey(key);
                              }
                          }
                          catch (Exception ex){
                              ex.printStackTrace();
                          }
                      }
                      /*Intent i = new Intent(SyncActivity.this, CotizarAutoActivity.class);
                      if(v==hola6){
                          i.putExtra("isSimulacion",false);
                      }
                      else{
                          i.putExtra("isSimulacion",true);
                      }
                      Evento.eventRecord(SyncActivity.this,Evento.COTIZAR);
                      startActivity(i);
                      */

                      //final ApiClient ac = new ApiClient(SyncActivity.this);
                      if(Build.VERSION.SDK_INT <= Build.VERSION_CODES.LOLLIPOP_MR1){
                          Intent i = new Intent(SyncActivity.this, CotizarAutoActivity.class);
                          if(v==hola6){
                              i.putExtra("isSimulacion",false);
                          }
                          else{
                              i.putExtra("isSimulacion",true);
                          }
                          Evento.eventRecord(SyncActivity.this,Evento.COTIZAR);
                          startActivity(i);
                      }else{
                          Evento.eventRecord(SyncActivity.this,Evento.COTIZAR);
                          Intent i = new Intent(SyncActivity.this, CotizaActivity.class);
                          i.putExtra("cliente",false);
                          startActivity(i);
                      }
                      //Intent i = new Intent(SyncActivity.this, CotizaActivity.class);
                      //Intent intent = new Intent(SyncActivity.this, com.google.androidbrowserhelper.trusted.LauncherActivity.class);
                      //intent.setData(Uri.parse(ac.UrlCotiza));
                      //startActivity(intent);

                      //TrustedWebActivityIntentBuilder builder = new TrustedWebActivityIntentBuilder(ac.UrlCotiza);
                              //.setAdditionalTrustedOrigins(origins);


                      //new TwaLauncher(SyncActivity.this).launch(builder, null, null);
                      //CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
                      //CustomTabsIntent customTabsIntent = builder.build();
                      //TrustedWebUtils.launchAsTrustedWebActivity(
                      //        SyncActivity.this,
                      //        customTabsIntent,
                      //       Uri.parse(ac.UrlCotiza));
                      //Intent intent = new Intent(this, com.google.androidbrowserhelper.trusted.LauncherActivity.class);
                      //intent.setData(Uri.parse("ANOTHER_SITE_URL"));
                      //startActivity(intent);
                      /*CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
                      builder.setShowTitle(false);
                      builder.enableUrlBarHiding();
                      builder.setCloseButtonIcon(BitmapFactory.decodeResource(getResources(), R.drawable.backarrow));

                      CustomTabsIntent customTabsIntent = builder.build();
                      customTabsIntent.launchUrl(SyncActivity.this, Uri.parse(ac.UrlCotiza));
                      */

                    } else {
                        Intent i = new Intent(SyncActivity.this, UnavailableSite.class);
                        i.putExtra("status", res);
                        startActivity(i);
                    }
                }
            }
        }).execute();
    }

    //-------------Llamada del boton to call    ---------------------------------
    public void GetSync(View view) {
        LogHelper.log(this,LogHelper.user_interaction,"SyncActivity.btnLogin","inicio de sincronizacion por boton: ",
                "", "","","");
        GetSync();
    }

    //-------------Llamada del boton to call    ---------------------------------
    public void GetSync() {
        LogHelper.log(this,LogHelper.backTask,"SyncActivity.getSync","inicio de sincronizacion automatico: ",
                "", "","","");
      if (!telefono.getText().toString().equals("")) {
            //valida si esta conectado a la red
            ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);//getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo netInfo = connMgr.getActiveNetworkInfo();
            if (netInfo != null && netInfo.isConnected()) {
                //hilos = new threadtosync();
                //hilos.Sync.execute(telefono.getText().toString());

                String url = "InfoClientMobil/Celphone/"+telefono.getText().toString();
                new GetPoliciesData(url, SyncActivity.this, new SimpleCallBack() {
                    @Override
                    public void run(boolean status, String res) {
                        if (!status){
                            String data[] = res.split("@");
                            launchAlert(data[1]);
                        }else{
                            //tenemos polizas, recuperamos list y mandamos a sms...
                            SharedPreferences.Editor editor = app_preferences.edit();
                            editor.putString("polizas", res);
                            editor.putString("Celphone", telefono.getText().toString());
                            editor.apply();

                            Gson parseJson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'hh:mm:ss").create();
                            List<InfoClient> InfoList = parseJson.fromJson(res, new TypeToken<List<InfoClient>>() {
                            }.getType());

                            final GlobalActivity globalVariable = (GlobalActivity) getApplicationContext();
                            globalVariable.setPolizas(InfoList);

                            String na = InfoList.get(0).getClient().getName();
                            tokencliente = InfoList.get(0).getClient().getToken();
                            app_preferences.edit().putString("nombre", na).apply();

                            //check session flag to launch main or sms
                            String sesion = app_preferences.getString("sesion","null");
                            if (sesion.equals("1")) {
                                LogHelper.log(SyncActivity.this, LogHelper.backTask, "SyncActivity.sendToken", "sesion activa->home",
                                        "", "", "", "");
                                Intent ii = new Intent(SyncActivity.this, PrincipalActivity.class);
                                startActivity(ii);
                            } else {
                                int idPolizaTemp = InfoList.get(0).getPolicies().getId();
                                getTokenSMS(telefono.getText().toString(), idPolizaTemp);
                            }
                        }
                    }
                }).execute();
                //hilos.sendToken.execute(cli);
            } else {
                //else de sin conexion...
                Toast.makeText(getApplicationContext(), "Sin conexión a Internet. Intente más tarde.", Toast.LENGTH_LONG).show();
                /*try {
                    //result se llena directamente de la base...& launch principal
                    //get data from DataBase
                    DBaseMethods.ThreadDBRead leerdata = new DBaseMethods.ThreadDBRead();
                    DBaseMethods.ThreadDBRead leerdatapoli = new DBaseMethods.ThreadDBRead();
                    DBaseMethods.ThreadDBRead leerdatacarro = new DBaseMethods.ThreadDBRead();
                    ArrayList<Object> datos = leerdata.execute(modelBase.FeedEntryUsuario.TABLE_NAME).get();
                    ArrayList<Object> polizas = leerdatapoli.execute(modelBase.FeedEntryPoliza.TABLE_NAME).get();
                    ArrayList<Object> carritos = leerdatacarro.execute(modelBase.FeedEntryVehiculo.TABLE_NAME).get();

                    List<InfoClient> InfoList = new ArrayList<>();
                    if (datos.size() != 0) {
                        for (int k = 0; k < datos.size(); k++) {
                            ClientMovil auto1 = (ClientMovil) datos.get(k);

                            VehicleMovil autito = (VehicleMovil) carritos.get(k);//new VehicleMovil(0, "color", "placas", "motor", 5, new VehicleBrand(0, "descr"), new VehicleModel(0, 10), new VehicleSubtype(0, "subtype"), new VehicleType(0, "type"), new VehicleDescription(0, "vehicledesc"));
                            InsurancePolicyDetail poliza1 = (InsurancePolicyDetail) polizas.get(k);//new InsurancePolicyDetail(false, false, 1, "numpoliza", new Date(), new Date(), 0.0f, 100, new Date(), new InsuranceCarrier(0, "carrier"), new InsurancePolicyState(0, "description"), new Coverage("coverage", 0), autito, 12);

                            poliza1.setVehicle(autito);

                            InfoClient ciente1 = new InfoClient(poliza1, auto1);
                            InfoList.add(ciente1);
                        }
                        result = InfoList;

                        //readDataBase();
                        Intent ii = new Intent(SyncActivity.this, PrincipalActivity.class);
                        startActivity(ii);
                    } else {

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }*/
            }
        } else {
            //introducir numero celular
            Toast.makeText(getApplicationContext(), "Introduce número de teléfono", Toast.LENGTH_LONG).show();
        }
    }

    public void getTokenSMS(String cel, int Idpoliza){
        String url = "TemporalToken/GetTemporalTokenPhone/" + cel + "/" + Idpoliza + "/Apptoken";
        new GetTokenSmsSync(url, SyncActivity.this, (status, res) -> {
            if(status) {
                token = res;
                Intent ii = new Intent(SyncActivity.this, TokenSmsActivity.class);
                startActivity(ii);
            }else{
                String data[] = res.split("@");
                launchAlert(data[1]);
            }
        }).execute();;
    }

    public void launchAlert(String res){
        AlertDialog.Builder builder = new AlertDialog.Builder(SyncActivity.this);
        builder.setTitle("Atención");
        builder.setMessage(res);
        builder.setPositiveButton("Ok", (dialog, which) -> {
            SharedPreferences.Editor editor = app_preferences.edit();
            editor.putString("Celphone", "");
            editor.putString("sesion", "");
            editor.apply();
        });
        AlertDialog alerta = builder.create();
        alerta.show();
    }

    //***********************************
    private class threadtosync {
        //************************** Asytask para web service **********************************************
        AsyncTask<ClientMovil, Void, Void> sendToken = new AsyncTask<ClientMovil, Void, Void>() {
            String ErrorCode = "";

            @Override
            protected void onCancelled() {
                super.onCancelled();
                progress.dismiss();
                if(ErrorCode.contains("version")){
                    final AlertDialog.Builder builder = new AlertDialog.Builder(SyncActivity.this);
                    builder.setMessage(ErrorCode);
                    builder.setCancelable(false);
                    builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            alerta.dismiss();
                            onPostExecute(null);
                        }
                    });
                    runOnUiThread(new Runnable() {
                        public void run() {
                            alerta = builder.create();
                            alerta.show();
                        }
                    });

                }
                else {
                    Toast msg = Toast.makeText(getApplicationContext(), "Error de conexión. Intenta más tarde.", Toast.LENGTH_LONG);
                    msg.show();
                }
            }

            @Override
            protected Void doInBackground(ClientMovil... params) {
                ApiClient client = new ApiClient(SyncActivity.this);
                try {
                    Gson parset=new Gson();
                    String result=parset.toJson(params[0]);
                    LogHelper.log(SyncActivity.this,LogHelper.backTask,"SyncActivity.sendToken","inicio envio de cliente",
                            result, "","","");
                    client.updateToken("ClientUser", params[0], tokencliente);
                } catch (Exception ex) {
                    ErrorCode = ex.getMessage();
                    this.cancel(true);
                }
                return null;
            }

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                progress.dismiss();

                //Creamos alert para validar odometro antes de enviarlo...
                //Ejecuto hilo para validar telefono
                String sesion = app_preferences.getString("sesion", "null");
                //String tuto = sharedPreferences.getString("tutohecho","null");

                //si ya inicio sesion antes, lamzamos a polizas
                if (sesion.equals("1")) {
                    LogHelper.log(SyncActivity.this,LogHelper.backTask,"SyncActivity.sendToken","sesion activa->home",
                            "", "","","");
                    Intent ii = new Intent(SyncActivity.this,PrincipalActivity.class);
                    startActivity(ii);
                } else {
                    //caso contrario, pedimos codigo token
                    //hilos = new threadtosync();
                    LogHelper.log(SyncActivity.this,LogHelper.backTask,"SyncActivity.sendToken","sesion inactiva->solicitud de token",
                            "", "","","");
                    //hilos.SyncToken.execute(telefono.getText().toString(), result.get(0).getPolicies().getId() + "", "Apptoken");
//                    hilos.SyncToken.execute(txtCelphone.getText().toString(), result.get(0).getClient().getEmail() + "", "Apptoken");

                }
                //hilos.Sync.execute(txtCelphone.getText().toString());
            }
        };

        //************************** Asytask para obtener datos ws **********************************************
        AsyncTask<String, Void, Void> Sync = new AsyncTask<String, Void, Void>() {

            String ErrorCode = "", Celphone = "";

            //Si cnacelo...inicio nuevo layout para reiniciar y borro de preferences
            @Override
            protected void onCancelled() {
                progress.dismiss();
                if (ErrorCode != null) {
                    if (ErrorCode.equalsIgnoreCase("El número proporcionado no existe en los registros")) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(SyncActivity.this);
                        builder.setTitle("Atención");
                        builder.setMessage("No tienes pólizas registradas. Vuelve a cotizar.");
                        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                SharedPreferences.Editor editor = app_preferences.edit();
                                editor.putString("Celphone", "");
                                editor.putString("sesion", "");
                                editor.commit();

                                //Intent intent = new Intent(SyncActivity.this,MainActivity.class);
                                //startActivity(intent);
                            }
                        });

                        AlertDialog alerta = builder.create();
                        alerta.show();
                    } else {
                        Toast message = Toast.makeText(getApplicationContext(), ErrorCode, Toast.LENGTH_LONG);
                        message.show();
                    }
                }
                super.onCancelled();
            }

            //Coloca datos de progress antes de arrancar
            //This gonna help for me
            @Override
            protected void onPreExecute() {
                progress.setTitle("Información");
                progress.setMessage("Actualizando pólizas...");
                //progress.setIcon(R.drawable.miituo);
                progress.setIndeterminate(true);
                progress.setCancelable(false);
                progress.show();
            }

            //va por listas de clientes
            //result es un List<InfoClient>
            @Override
            protected Void doInBackground(String... params) {
                ApiClient client = new ApiClient(SyncActivity.this);
                try {
                    result = client.getInfoClient("InfoClientMobil/Celphone/" + params[0], SyncActivity.this);
                    if (result == null) {
                        progress.dismiss();
                        ErrorCode = "Estamos haciendo algunas actualizaciones, si sigues teniendo problemas para ingresar, contáctate con nosotros.";
                        this.cancel(true);
                    } else {
                        //guardar en Celphone el telefono que viene en params[0]
                        //get name
                        String na = result.get(0).getClient().getName();
                        tokencliente = result.get(0).getClient().getToken();
                        app_preferences.edit().putString("nombre", na).apply();

                        LogHelper.log(SyncActivity.this,LogHelper.backTask,"SyncActivity.Sync","inicio descarga de fotos polizas: ",
                                "", "","","");
                        //recover iamges----------------------------------------------------------------
                        /*for (int i = 0; i < result.size(); i++) {
                            String filePathString = getExternalFilesDir(Environment.DIRECTORY_PICTURES) + File.separator + "frontal_" + result.get(i).getPolicies().getNoPolicy() + ".png";
                            File f = new File(filePathString);

                            //Si no hay imagen...intentamos recuperarla...
                            if (!f.exists() && result.get(i).getPolicies().isHasVehiclePictures()) {
                                try {
                                    String id = result.get(i).getPolicies().getId() + "";
                                    int idCarrier = result.get(i).getPolicies().getInsuranceCarrier().getId();


                                    Bitmap m = client.DownloadPhoto("ImageSendProcess/GetFrontImageCarApp/" + id, SyncActivity.this, tokencliente);
//                                    Bitmap m = client.DownloadPhotoMultiaseguradora("ImageSendProcess/Insurance/" + idCarrier+
//                                           "/GetFrontImageCarApp/"+id , SyncActivity.this,tokencliente);

                                    if (m != null) {
                                        //before load...save it in order to not load again...
                                        FileOutputStream out = null;
                                        try {
                                            out = new FileOutputStream(filePathString);
                                            m.compress(Bitmap.CompressFormat.PNG, 50, out); // bmp is your Bitmap instance
                                            // PNG is a lossless format, the compression factor (100) is ignored
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        } finally {
                                            try {
                                                if (out != null) {
                                                    out.close();
                                                }
                                            } catch (IOException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }*/
                        //borrarDB();

                        LogHelper.log(SyncActivity.this,LogHelper.backTask,"SyncActivity.Sync","guardando polizas: ",
                                "", "","","");

                        for (int i = 0; i < result.size(); i++) {
                            InsurancePolicyDetail polizatemp = result.get(i).getPolicies();
                            ClientMovil clienttemp = result.get(i).getClient();
                            //String inse = inserta.execute(modelBase.FeedEntryUsuario.TABLE_NAME,clienttemp.getName(),clienttemp.getMotherName(),clienttemp.getLastName(),clienttemp.getCelphone(),clienttemp.getToken()).get();
                            String inse = saveData(modelBase.FeedEntryUsuario.TABLE_NAME, clienttemp.getName(), clienttemp.getMotherName(), clienttemp.getLastName(), clienttemp.getCelphone(), clienttemp.getToken());

                            if (!inse.equals("")) {

                                String date = new SimpleDateFormat("dd-MM-yyyy").format(polizatemp.getLimitReportDate());
                                //String insepoli = insertapoli.execute(
                                String insepoli = saveData(
                                        modelBase.FeedEntryPoliza.TABLE_NAME,
                                        clienttemp.getId() + "",
                                        polizatemp.getInsuranceCarrier().getName(),
                                        polizatemp.getLastOdometer() + "",
                                        polizatemp.getNoPolicy(),
                                        polizatemp.isHasOdometerPicture() + "",
                                        polizatemp.isHasVehiclePictures() + "",
                                        polizatemp.getReportState() + "",
                                        polizatemp.getRate() + "",
                                        date + "",
                                        polizatemp.getPaymentType());

                                if (!insepoli.equals("")) {

                                    VehicleMovil carritotemp = polizatemp.getVehicle();
                                    //String insecarrito = insertacar.execute(
                                    String insecarrito = saveData(
                                            modelBase.FeedEntryVehiculo.TABLE_NAME,
                                            polizatemp.getNoPolicy(),
                                            carritotemp.getBrand().getDescription(),
                                            carritotemp.getCapacity() + "",
                                            carritotemp.getColor(),
                                            carritotemp.getDescription().getDescription(),
                                            carritotemp.getModel().getModel() + "",
                                            carritotemp.getPlates(),
                                            carritotemp.getType().getDescription(),
                                            carritotemp.getSubtype().getDescription()
                                    );

                                    if (!insecarrito.equals("")) {
                                        //Log.w("Ok","Datos guardados correctamente");
                                    }
                                }
                            }
                        }
                    }

                    //Log.e("Aqui", "Actualizando datos de nuevo");
                    Celphone = params[0];
                } catch (Exception ex) {
                    if (ErrorCode.equals("")) {
                        ErrorCode = ex.getMessage();
                    }
                    this.cancel(true);
                }
                return null;
            }

            //Guardo y muestro infoClientList layout
            //recupero polizas
            @Override
            protected void onPostExecute(Void aVoid) {
                //guardo telefono en preferences
                SharedPreferences.Editor editor = app_preferences.edit();
                editor.putString("Celphone", Celphone);
                editor.commit();
                //inicio infoclientelist layout
                //setContentView(R.layout.infoclientlist);

                //Intent ii = new Intent(SyncActivity.this,TokenSmsActivity.class);
                //startActivity(ii);

                //hilos = new threadtosync();

                String celData = "";
                try {
                    String macAddress="";
                    PackageInfo pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
                    float version = Float.parseFloat(pInfo.versionName);
                    TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
                    if (ActivityCompat.checkSelfPermission(SyncActivity.this, Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {
                        macAddress = telephonyManager.getDeviceId();
                    }
                    String marca = Build.MANUFACTURER;
                    String modelo = Build.MODEL;
                    String versionRelease = Build.VERSION.RELEASE;
                    String carrierName = telephonyManager.getNetworkOperatorName();
                    JsonObject j=new JsonObject();
                    j.addProperty("imei",macAddress);
                    j.addProperty("os","ANDROID");
                    j.addProperty("marca",marca);
                    j.addProperty("modelo",modelo);
                    j.addProperty("osVersion",versionRelease);
                    j.addProperty("appVersion",version);
                    j.addProperty("carrier",carrierName);
                    celData=j.toString();
                }
                catch (Exception e){
                    e.printStackTrace();
                }
                cli.setDatacelphone(celData);
                //hilos.sendToken.execute(cli);
            }

            public String saveData(String... strings){
                //Aqui va el nombre de la tala
                String val = strings[0];
                //Log.w("Here",val);

                switch (val){

                    case modelBase.FeedEntryUsuario.TABLE_NAME:
                        // Gets the data repository in write mode
                        SQLiteDatabase db2 = DBaseMethods.base.getWritableDatabase();

                        // Create a new map of values, where column names are the keys
                        ContentValues values2 = new ContentValues();
                        //values.put(modelBase.FeedEntryArticle.COLUMN_ID, strings[6]);
                        values2.put(modelBase.FeedEntryUsuario.COLUMN_NAME, strings[1]);
                        values2.put(modelBase.FeedEntryUsuario.COLUMN_MOTHERNAME, strings[2]);
                        //values2.put(modelBase.FeedEntryUsuario.COLUMN_UBI, strings[3]);
                        values2.put(modelBase.FeedEntryUsuario.COLUMN_LASTNAME, strings[3]);
                        values2.put(modelBase.FeedEntryUsuario.COLUMN_CELPHONE, strings[4]);
                        values2.put(modelBase.FeedEntryUsuario.COLUMN_TOKEN, strings[5]);

                        // Insert the new row, returning the primary key value of the new row
                        //Just change name table and the values....
                        long newRowId2 = db2.insert(val, null, values2);
                        try {
                            Thread.sleep(50);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                        return "" + newRowId2;

                    case modelBase.FeedEntryPoliza.TABLE_NAME:
                        // Gets the data repository in write mode
                        SQLiteDatabase db = DBaseMethods.base.getWritableDatabase();

                        // Create a new map of values, where column names are the keys
                        ContentValues values = new ContentValues();
                        //values.put(modelBase.FeedEntryArticle.COLUMN_ID, strings[6]);
                        values.put(modelBase.FeedEntryPoliza.COLUMN_IDUSER, strings[1]);
                        values.put(modelBase.FeedEntryPoliza.COLUMN_INSURANCE, strings[2]);
                        values.put(modelBase.FeedEntryPoliza.COLUMN_LASTODOMETER, strings[3]);
                        values.put(modelBase.FeedEntryPoliza.COLUMN_NOPOLICY, strings[4]);
                        values.put(modelBase.FeedEntryPoliza.COLUMN_ODOMETERPIE, strings[5]);
                        values.put(modelBase.FeedEntryPoliza.COLUMN_VEHICLEPIE, strings[6]);
                        values.put(modelBase.FeedEntryPoliza.COLUMN_REPORT_STATE, strings[7]);
                        values.put(modelBase.FeedEntryPoliza.COLUMN_RATE, strings[8]);
                        values.put(modelBase.FeedEntryPoliza.COLUMN_LIMIT_DATE, strings[9]);
                        values.put(modelBase.FeedEntryPoliza.COLUMN_PAYMENT, strings[10]);

                        // Insert the new row, returning the primary key value of the new row
                        //Just change name table and the values....
                        long newRowId = db.insert(val, null, values);
                        try {
                            Thread.sleep(50);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                        return "" + newRowId;

                    case modelBase.FeedEntryVehiculo.TABLE_NAME:
                        // Gets the data repository in write mode
                        SQLiteDatabase dbvei = DBaseMethods.base.getWritableDatabase();

                        // Create a new map of values, where column names are the keys
                        ContentValues valuesvei = new ContentValues();
                        //values.put(modelBase.FeedEntryArticle.COLUMN_ID, strings[6]);
                        valuesvei.put(modelBase.FeedEntryVehiculo.COLUMN_POLIZA, strings[1]);
                        valuesvei.put(modelBase.FeedEntryVehiculo.COLUMN_BRAND, strings[2]);
                        valuesvei.put(modelBase.FeedEntryVehiculo.COLUMN_CAPACITY, strings[3]);
                        valuesvei.put(modelBase.FeedEntryVehiculo.COLUMN_COLOR, strings[4]);
                        valuesvei.put(modelBase.FeedEntryVehiculo.COLUMN_DESCRIPTION, strings[5]);
                        valuesvei.put(modelBase.FeedEntryVehiculo.COLUMN_MODEL, strings[6]);
                        valuesvei.put(modelBase.FeedEntryVehiculo.COLUMN_PLATES, strings[7]);
                        valuesvei.put(modelBase.FeedEntryVehiculo.COLUMN_TYPE, strings[8]);
                        valuesvei.put(modelBase.FeedEntryVehiculo.COLUMN_SUBTYPE, strings[9]);

                        // Insert the new row, returning the primary key value of the new row
                        //Just change name table and the values....
                        long newRowvei = dbvei.insert(val, null, valuesvei);
                        try {
                            Thread.sleep(50);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                        return "" + newRowvei;

                    default:
                        break;
                }

                return "";
            }

            public void borrarDB(){
                DBaseMethods.base= new modelBase(getApplicationContext(), Integer.parseInt(getResources().getString(R.string.dbversion)));
                SQLiteDatabase db = DBaseMethods.base.getWritableDatabase();

                db.execSQL("DELETE FROM " + modelBase.FeedEntryUsuario.TABLE_NAME);
                db.execSQL("DELETE FROM " + modelBase.FeedEntryPoliza.TABLE_NAME);
                db.execSQL("DELETE FROM " + modelBase.FeedEntryVehiculo.TABLE_NAME);
                db.close();
            }
        };

//************************** Asytask para obtener recuperat otken ws **********************************************
        AsyncTask<String, Void, Void> SyncToken = new AsyncTask<String, Void, Void>() {
            ProgressDialog progress = new ProgressDialog(SyncActivity.this);
            String ErrorCode = "", Celphone = "";

            //Si cnacelo...inicio nuevo layout para reiniciar y borro de preferences
            @Override
            protected void onCancelled() {
                progress.dismiss();
                Toast message = Toast.makeText(getApplicationContext(), ErrorCode, Toast.LENGTH_LONG);
                message.show();
                setContentView(R.layout.error_sync);
                Button reintent = (Button) findViewById(R.id.reintentar);
                reintent.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(SyncActivity.this, SyncActivity.class);
                        startActivity(i);
                    }
                });
                SharedPreferences.Editor editor = app_preferences.edit();
                editor.putString("Celphone", "");
                editor.commit();
                super.onCancelled();
            }

            //Coloca datos de progress antes de arrancar
            //This gonna help for me
            @Override
            protected void onPreExecute() {
                progress.setTitle("Información");
                progress.setMessage("Actualizando...");
                //progress.setIcon(R.drawable.miituo);
                progress.setIndeterminate(true);
                progress.setCancelable(false);
                progress.show();
            }

            //va por listas de clientes
            //result es un List<InfoClient>
            @Override
            protected Void doInBackground(String... params) {
                ApiClient client = new ApiClient(SyncActivity.this);
                try {
                    //recupera datos
                    //params[0] es el telefono
                    //result.clear();
                    //result = client.getInfoClient("InfoClientMobil/Celphone/" + params[0]);
                    token = client.getToken("TemporalToken/GetTemporalTokenPhone/" + params[0] + "/" + params[1]+ "/"+ params[2]);
                    //guardar en Celphone el telefono que viene en params[0]

                    //Log.e("Aqui", "Actualizando datos de nuevo");
                    Celphone = params[0];
                } catch (Exception ex) {
                    ErrorCode = ex.getMessage();
                    this.cancel(true);
                }
                return null;
            }

            //Guardo y muestro infoClientList layout
            //recupero polizas
            @Override
            protected void onPostExecute(Void aVoid) {
                //guardo telefono en preferences
                SharedPreferences.Editor editor = app_preferences.edit();
                editor.putString("Celphone", Celphone);
                editor.commit();
                //inicio infoclientelist layout
                //setContentView(R.layout.infoclientlist);

                LogHelper.log(SyncActivity.this,LogHelper.backTask,"SyncActivity.SyncToken","SyncToken ok->pantalla token",
                        "", "","","");
                Intent ii = new Intent(SyncActivity.this, TokenSmsActivity.class);
                startActivity(ii);

                progress.dismiss();
            }
        };
    }
}
