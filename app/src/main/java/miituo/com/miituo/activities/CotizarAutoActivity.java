package miituo.com.miituo.activities;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.text.InputFilter;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.gson.Gson;
import com.uxcam.UXCam;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import miituo.com.miituo.R;
import miituo.com.miituo.api.ApiClient;
import miituo.com.miituo.cotizar.ApiClientCotiza;
import miituo.com.miituo.cotizar.CotizaTipos;
import miituo.com.miituo.cotizar.GarageTipos;
import miituo.com.miituo.cotizar.MarcasVehiculos;
import miituo.com.miituo.cotizar.ModelosVehiculos;
import miituo.com.miituo.cotizar.RateList;
import miituo.com.miituo.cotizar.TiposVehiculos;
import miituo.com.miituo.threats.AvailableSiteSync;
import miituo.com.miituo.utils.Evento;
import miituo.com.miituo.utils.SimpleCallBack;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import static miituo.com.miituo.activities.CotizarAutoActivity.cotizaTipos;

public class CotizarAutoActivity extends BaseActivity {

    public boolean flagtipo,flaganio,flagmarca,flagmodelo,flagdescripcion;

    public Spinner tipo_vehiculo,anio,marca,subtipo,descripcion,garagespinner;
    LinearLayout cntDesc,cntda,cntds;
    List<String> arraySpinnerMarcas;
    List<String> arraySpinnerModelos;
    List<String> arraySpinnerSubtipos;
    List<String> arraySpinnerDescripciones;
    List<String> arraySpinner;
    List<String> arraySpinnerGarage;
    List<String> arraySpinnerSexo;

    List<TiposVehiculos> tokenTipos;
    List<ModelosVehiculos> tokenModelos;
    public static List<MarcasVehiculos> tokenDescripciones;
    List<MarcasVehiculos> tokenMarcas;
    List<MarcasVehiculos> tokenSubtipos;
    List<GarageTipos> tokenGarage;

    ImageView auto;
    ImageView persona;
    ImageView cotizacion;

    double pagoUnico=0.0;

    private TextView lbAuto, lbDatos,lbCotiz,  //Tabs
            lbTuAuto,lbTusDatos,lbTuCotizacion,  //Titulos
            lbTipo,lbAnio,lbBrandSellection,lbMarca,lbMod,lbDescription,lbNaci,lbSex,lbCP,lbGarage,lbEnviamos,lbNombre,lbCorreo,lbTelefono,  //Subtitlos
            lbNext,lbCotizar,lbEnviar,lbM1,lbM2,lbM3,lbM4,lbM5,lbM6,lbContrataAmplio,lbContrataSimple,lbSimulaSimple,lbSimulaAmplio,  //Botones
            lbPlanAmplio,lbCostoAmplio,lbKmAmplio,lbDetalleAmplio,  //Plan amplio
            lbPlanSimple,lbCostoSimple,lbKmSimple,lbDetalleSimple,  //plan simple
            lbFecNac,lbAvisoPriv,lbAvisoPrivLink;  //otros

    private RadioButton lbH,lbM;

    private Typeface tipo;

    public String VehicleTMMSDId,Gender,HasGarage,BirthdayDate,ZipCode,formatNac;

    public static CotizaTipos cotizaTipos;

    public static String[] datosAuto;
    public static String[] datosMios;
    ScrollView svAuto, svPersona,svCotizacion;
    EditText postal,etNombre,etCorreo,etTelefono;
    int amplioIndex,simpleIndex,pageIndex=0;
    boolean isSimulacion=false;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 1) {
            if(resultCode == AppCompatActivity.RESULT_OK){
                String result=data.getStringExtra("result");
                int index=data.getIntExtra("resultInt",0);
                descripcion.setSelection(index);
                svAuto.scrollTo(0, svAuto.getBottom());
            }
            if (resultCode == AppCompatActivity.RESULT_CANCELED) {
                //Write your code if there's no result
            }
        }
    }//onActivityResult

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cotizar);
        isSimulacion=getIntent().getBooleanExtra("isSimulacion",false);
        init();
        new getDataAuto().execute();
    }

    private void init(){
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(isSimulacion?"Simulación":"Cotizar");
        toolbar.setTitleTextColor(Color.BLACK);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        tipo=Typeface.createFromAsset(getAssets(), "fonts/herne1.ttf");

        svAuto =(ScrollView)findViewById(R.id.scrolAuto);
        svPersona =(ScrollView)findViewById(R.id.scrolPersona);
        svCotizacion =(ScrollView)findViewById(R.id.scrollCotizacion);

        lbFecNac = findViewById(R.id.editTextFecha);
        postal = findViewById(R.id.editTextPostal);
        etNombre = findViewById(R.id.etNombre);
        etCorreo = findViewById(R.id.etCorreo);
        etTelefono = findViewById(R.id.etTelefono);

        tipo_vehiculo = findViewById(R.id.spinner2);
        anio = findViewById(R.id.spinneranio);
        marca = findViewById(R.id.spinnermarca);
        subtipo = findViewById(R.id.spinnersubtipo);
        descripcion=findViewById(R.id.spinnerdescripcion);
        garagespinner = findViewById(R.id.spinnerGarage);

        lbAuto=(TextView)findViewById(R.id.lbAuto);
        lbAuto.setTypeface(tipo);
        lbDatos =(TextView)findViewById(R.id.lbDatos);
        lbDatos.setTypeface(tipo);
        lbCotiz=(TextView)findViewById(R.id.lbCotiz);
        lbCotiz.setTypeface(tipo);

        lbTuAuto =(TextView)findViewById(R.id.textView2);
        lbTuAuto.setTypeface(tipo,Typeface.BOLD);
        lbTusDatos =(TextView)findViewById(R.id.textView14);
        lbTusDatos.setTypeface(tipo,Typeface.BOLD);
        lbTuCotizacion =(TextView)findViewById(R.id.lbTuCotizacion);
        lbTuCotizacion.setTypeface(tipo,Typeface.BOLD);

        lbTipo=(TextView)findViewById(R.id.labelTipo);
        lbTipo.setTypeface(tipo);
        lbAnio=(TextView)findViewById(R.id.labelAnio);
        lbAnio.setTypeface(tipo);
        lbBrandSellection=(TextView)findViewById(R.id.lbBrandSelection);
        lbBrandSellection.setTypeface(tipo);
        lbMarca=(TextView)findViewById(R.id.labelMarca);
        lbMarca.setTypeface(tipo);
        lbMod=(TextView)findViewById(R.id.lbModelo);
        lbMod.setTypeface(tipo);
        lbDescription=(TextView)findViewById(R.id.lbDesc);
        lbDescription.setTypeface(tipo);
        lbNaci=(TextView)findViewById(R.id.lbNac);
        lbNaci.setTypeface(tipo);
        lbSex=(TextView)findViewById(R.id.lbSex);
        lbSex.setTypeface(tipo);
        lbCP=(TextView)findViewById(R.id.lbCP);
        lbCP.setTypeface(tipo);
        lbGarage=(TextView)findViewById(R.id.lbGarage);
        lbGarage.setTypeface(tipo);
        lbNombre=(TextView)findViewById(R.id.lbnombre);
        lbNombre.setTypeface(tipo,Typeface.BOLD);
        lbCorreo=(TextView)findViewById(R.id.lbCorreo);
        lbCorreo.setTypeface(tipo,Typeface.BOLD);
        lbTelefono=(TextView)findViewById(R.id.lbTelefono);
        lbTelefono.setTypeface(tipo,Typeface.BOLD);
        lbEnviamos=(TextView)findViewById(R.id.lbEnviamos);
        lbEnviamos.setTypeface(tipo);
        lbEnviamos.setText(Html.fromHtml("<p>¿Enviamos la <b>cotización</b> a tu correo?</p>"));
        lbPlanAmplio=(TextView)findViewById(R.id.lbPlanAmplio);
        lbPlanAmplio.setTypeface(tipo,Typeface.BOLD);
        lbPlanSimple=(TextView)findViewById(R.id.lbPlanSimple);
        lbPlanSimple.setTypeface(tipo,Typeface.BOLD);
        lbCostoAmplio=(TextView)findViewById(R.id.lbCostoAmplio);
        lbCostoAmplio.setTypeface(tipo,Typeface.BOLD);
        lbCostoSimple=(TextView)findViewById(R.id.lbCostoSimple);
        lbCostoSimple.setTypeface(tipo,Typeface.BOLD);
        lbKmAmplio=(TextView)findViewById(R.id.lbKmAmplio);
        lbKmAmplio.setTypeface(tipo);
        lbKmSimple=(TextView)findViewById(R.id.lbKmSimple);
        lbKmSimple.setTypeface(tipo);
        lbDetalleAmplio=(TextView)findViewById(R.id.lbDetalleAmplio);
        lbDetalleAmplio.setTypeface(tipo);
        lbDetalleSimple=(TextView)findViewById(R.id.lbDetalleSimple);
        lbDetalleSimple.setTypeface(tipo);
        lbContrataAmplio=(TextView)findViewById(R.id.lbContrataAmplio);
        lbContrataAmplio.setTypeface(tipo);
        lbContrataSimple=(TextView)findViewById(R.id.lbContrataSimple);
        lbContrataSimple.setTypeface(tipo);
        lbSimulaAmplio=(TextView)findViewById(R.id.lbSimulaAmplio);
        lbSimulaAmplio.setTypeface(tipo);
        lbSimulaSimple=(TextView)findViewById(R.id.lbSimulaSimple);
        lbSimulaSimple.setTypeface(tipo);
        lbNext=(TextView)findViewById(R.id.lbCotizaNext);
        lbNext.setTypeface(tipo);
        lbCotizar=(TextView)findViewById(R.id.lbCotizar);
        lbCotizar.setTypeface(tipo);
        lbEnviar=(TextView)findViewById(R.id.lbEnviar);
        lbEnviar.setTypeface(tipo);
        lbM1=(TextView)findViewById(R.id.tvM1);
        lbM1.setTypeface(tipo);
        lbM2=(TextView)findViewById(R.id.tvM2);
        lbM2.setTypeface(tipo);
        lbM3=(TextView)findViewById(R.id.tvM3);
        lbM3.setTypeface(tipo);
        lbM4=(TextView)findViewById(R.id.tvM4);
        lbM4.setTypeface(tipo);
        lbM5=(TextView)findViewById(R.id.tvM5);
        lbM5.setTypeface(tipo);
        lbM6=(TextView)findViewById(R.id.tvM6);
        lbM6.setTypeface(tipo);
        lbH=(RadioButton) findViewById(R.id.lbH);
        lbH.setTypeface(tipo,Typeface.BOLD);
        lbM=(RadioButton) findViewById(R.id.lbM);
        lbM.setTypeface(tipo,Typeface.BOLD);
        lbAvisoPriv=(TextView)findViewById(R.id.lbAvisoPriv);
        lbAvisoPriv.setTypeface(tipo);
        lbAvisoPrivLink=(TextView)findViewById(R.id.lbAvisoPrivLink);
        lbAvisoPrivLink.setTypeface(tipo,Typeface.BOLD);
        lbAvisoPrivLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String urlString="https://miituo.com/Terminos/getPDF?Url=C%3A%5CmiituoFTP%5CAviso_de_Privacidad.pdf";
                Intent intent=new Intent(Intent.ACTION_VIEW);
                intent.setDataAndType(Uri.parse("http://docs.google.com/gview?embedded=true&url=" + urlString), "text/html");
                startActivity(intent);
            }
        });

        View.OnClickListener c=new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                brandSellector(v);
            }
        };

        lbM1.setOnClickListener(c);
        lbM2.setOnClickListener(c);
        lbM3.setOnClickListener(c);
        lbM4.setOnClickListener(c);
        lbM5.setOnClickListener(c);
        lbM6.setOnClickListener(c);

        lbH.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    lbH.setTextColor(Color.parseColor("#FFFFFF"));
                    lbM.setTextColor(getResources().getColor(R.color.azul_miituo));
                    Gender = "2";
                    datosMios[1] = "Hombre";
                }
            }
        });
        lbM.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    lbM.setTextColor(Color.parseColor("#FFFFFF"));
                    lbH.setTextColor(getResources().getColor(R.color.azul_miituo));
                    Gender = "1";
                    datosMios[1]="Mujer";
                }
            }
        });
        datosMios = new String[]{"","","",""};
        Gender="2";
        lbM.setChecked(true);
        lbH.setChecked(true);

        descripcion.setEnabled(false);
        cntda = (LinearLayout)findViewById(R.id.cntda);
        cntds = (LinearLayout)findViewById(R.id.cntds);
        cntDesc = (LinearLayout)findViewById(R.id.cntDesc);
        cntDesc.setLayoutParams(descripcion.getLayoutParams());
        cntDesc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(tokenDescripciones!=null && tokenDescripciones.size()>0){
                    startActivityForResult(new Intent(CotizarAutoActivity.this,DescriptionSellector.class),1);
                }
                else{
                    Toast.makeText(CotizarAutoActivity.this,"Por favor indica el modelo",Toast.LENGTH_LONG).show();
                }
            }
        });

        datosAuto = new String[]{"", "", "", ""};

        flagtipo = false;
        flaganio = false;
        flagmodelo = false;
        flagmarca = false;
        flagdescripcion = false;

        VehicleTMMSDId = "";
        BirthdayDate = "";
        formatNac="";
        HasGarage = "";
        ZipCode = "";

        lbContrataSimple.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Gson gson = new Gson();
                String plan=gson.toJson(cotizaTipos.getRateList().get(simpleIndex));
                Intent  i=new Intent(CotizarAutoActivity.this,ContratarActivity.class);
                i.putExtra("sexo",Gender);
                i.putExtra("vehicleId",VehicleTMMSDId);
                i.putExtra("birthday",BirthdayDate);
                i.putExtra("formatNac",formatNac);
                i.putExtra("garage",HasGarage);
                i.putExtra("cp",ZipCode);
                i.putExtra("plan",plan);
                i.putExtra("pagoUnico",pagoUnico);
                i.putExtra("id",cotizaTipos.getId());
                Evento.eventRecord(CotizarAutoActivity.this,Evento.CONTRATAR);
                checkSite(i);
            }
        });
        lbContrataAmplio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Gson gson = new Gson();
                String plan=gson.toJson(cotizaTipos.getRateList().get(amplioIndex));
                Intent  i=new Intent(CotizarAutoActivity.this,ContratarActivity.class);
                i.putExtra("sexo",Gender);
                i.putExtra("vehicleId",VehicleTMMSDId);
                i.putExtra("birthday",BirthdayDate);
                i.putExtra("formatNac",formatNac);
                i.putExtra("garage",HasGarage);
                i.putExtra("cp",ZipCode);
                i.putExtra("plan",plan);
                i.putExtra("pagoUnico",pagoUnico);
                i.putExtra("id",cotizaTipos.getId());
                Evento.eventRecord(CotizarAutoActivity.this,Evento.CONTRATAR);
                checkSite(i);
            }
        });
        auto=(ImageView) findViewById(R.id.auto);
        persona=(ImageView) findViewById(R.id.persona);
        cotizacion=(ImageView) findViewById(R.id.cotizacion);
        auto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(pageIndex==1){
                    backStep(true,false);
                }
                else if(pageIndex==2){
                    backStep(false,true);
                }
            }
        });
        persona.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(pageIndex==0){
                    nextStep(lbNext);
                }
                else if(pageIndex==2){
                    backStep(false,false);
                }
            }
        });
        cotizacion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(pageIndex==1){
                    sendCotizar(null);
                }
                else if(pageIndex==0){
                    nextStep(null);
                }
            }
        });
    }

    public AlertDialog alertaCotiz;
    private void checkSite(final Intent i){
        new AvailableSiteSync(this, new SimpleCallBack() {
            @Override
            public void run(boolean status, String res) {
                if (!status) {
                    final AlertDialog.Builder builder = new AlertDialog.Builder(CotizarAutoActivity.this);
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
                        startActivity(i);
                    } else {
                        Intent ii = new Intent(CotizarAutoActivity.this, UnavailableSite.class);
                        ii.putExtra("status", res);
                        ii.putExtra("idQuot",cotizaTipos.getId());
                        startActivity(ii);
                    }
                }
            }
        }).execute();
    }

    public void brandSellector(View v){
        if(tokenMarcas==null || tokenMarcas.size()<2){
            Toast.makeText(this,getString(R.string.sellect_anio),Toast.LENGTH_LONG).show();
            return;
        }
        int index=0;
        if (v==lbM1){
            index=getIndexBrand("NISSAN");
            lbM1.setBackgroundColor(getResources().getColor(R.color.colorPrimarySellected));
            lbM2.setBackgroundColor(getResources().getColor(R.color.colorPrimaryUnsellected));
            lbM3.setBackgroundColor(getResources().getColor(R.color.colorPrimaryUnsellected));
            lbM4.setBackgroundColor(getResources().getColor(R.color.colorPrimaryUnsellected));
            lbM5.setBackgroundColor(getResources().getColor(R.color.colorPrimaryUnsellected));
            lbM6.setBackgroundColor(getResources().getColor(R.color.colorPrimaryUnsellected));
        }
        else if (v==lbM2){
            index=getIndexBrand("GENERAL MOTORS");
            lbM2.setBackgroundColor(getResources().getColor(R.color.colorPrimarySellected));
            lbM1.setBackgroundColor(getResources().getColor(R.color.colorPrimaryUnsellected));
            lbM3.setBackgroundColor(getResources().getColor(R.color.colorPrimaryUnsellected));
            lbM4.setBackgroundColor(getResources().getColor(R.color.colorPrimaryUnsellected));
            lbM5.setBackgroundColor(getResources().getColor(R.color.colorPrimaryUnsellected));
            lbM6.setBackgroundColor(getResources().getColor(R.color.colorPrimaryUnsellected));
        }
        else if (v==lbM3){
            index=getIndexBrand("VOLKSWAGEN");
            lbM3.setBackgroundColor(getResources().getColor(R.color.colorPrimarySellected));
            lbM2.setBackgroundColor(getResources().getColor(R.color.colorPrimaryUnsellected));
            lbM1.setBackgroundColor(getResources().getColor(R.color.colorPrimaryUnsellected));
            lbM4.setBackgroundColor(getResources().getColor(R.color.colorPrimaryUnsellected));
            lbM5.setBackgroundColor(getResources().getColor(R.color.colorPrimaryUnsellected));
            lbM6.setBackgroundColor(getResources().getColor(R.color.colorPrimaryUnsellected));
        }
        else if (v==lbM4){
            index=getIndexBrand("FORD");
            lbM4.setBackgroundColor(getResources().getColor(R.color.colorPrimarySellected));
            lbM2.setBackgroundColor(getResources().getColor(R.color.colorPrimaryUnsellected));
            lbM3.setBackgroundColor(getResources().getColor(R.color.colorPrimaryUnsellected));
            lbM1.setBackgroundColor(getResources().getColor(R.color.colorPrimaryUnsellected));
            lbM5.setBackgroundColor(getResources().getColor(R.color.colorPrimaryUnsellected));
            lbM6.setBackgroundColor(getResources().getColor(R.color.colorPrimaryUnsellected));
        }
        else if (v==lbM5){
            index=getIndexBrand("TOYOTA");
            lbM5.setBackgroundColor(getResources().getColor(R.color.colorPrimarySellected));
            lbM2.setBackgroundColor(getResources().getColor(R.color.colorPrimaryUnsellected));
            lbM3.setBackgroundColor(getResources().getColor(R.color.colorPrimaryUnsellected));
            lbM4.setBackgroundColor(getResources().getColor(R.color.colorPrimaryUnsellected));
            lbM1.setBackgroundColor(getResources().getColor(R.color.colorPrimaryUnsellected));
            lbM6.setBackgroundColor(getResources().getColor(R.color.colorPrimaryUnsellected));
        }
        else if (v==lbM6){
            index=getIndexBrand("HONDA");
            lbM6.setBackgroundColor(getResources().getColor(R.color.colorPrimarySellected));
            lbM2.setBackgroundColor(getResources().getColor(R.color.colorPrimaryUnsellected));
            lbM3.setBackgroundColor(getResources().getColor(R.color.colorPrimaryUnsellected));
            lbM4.setBackgroundColor(getResources().getColor(R.color.colorPrimaryUnsellected));
            lbM5.setBackgroundColor(getResources().getColor(R.color.colorPrimaryUnsellected));
            lbM1.setBackgroundColor(getResources().getColor(R.color.colorPrimaryUnsellected));
        }
        marca.setSelection(index,true);
    }

    private int getIndexBrand(String s){
        int index=0;
        for(int i=0;i<tokenMarcas.size();i++){
            if(tokenMarcas.get(i).getDescription().equalsIgnoreCase(s)){
                index=i;
                break;
            }
        }
        return index;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            if(pageIndex==1){
                backStep(true,false);
            }
            else if(pageIndex==2){
                backStep(false,false);
            }
            else{
                super.onOptionsItemSelected(item);
                finish();
            }
        }
        return false;
    }
    @Override
    public void onBackPressed() {
        if(pageIndex==1){
            backStep(true,false);
        }
        else if(pageIndex==2){
            backStep(false,false);
        }
        else{
            super.onBackPressed();
            finish();
        }
    }

    public void openDatePicker(View v){
        final Calendar c = Calendar.getInstance();
        int yearshow = c.get(Calendar.YEAR);
        int monthshow = c.get(Calendar.MONTH);
        int dayshow = c.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog recogerFecha = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                //Esta variable lo que realiza es aumentar en uno el mes ya que comienza desde 0 = enero
                final int mesActual = month + 1;
                //Formateo el día obtenido: antepone el 0 si son menores de 10
                String diaFormateado = (dayOfMonth < 10)? "0" + String.valueOf(dayOfMonth):String.valueOf(dayOfMonth);
                //Formateo el mes obtenido: antepone el 0 si son menores de 10
                String mesFormateado = (mesActual < 10)? "0" + String.valueOf(mesActual):String.valueOf(mesActual);
                //Muestro la fecha con el formato deseado
                String yearString = String.valueOf(year);
                Log.e("fecha---",(diaFormateado + "/" + mesFormateado + "/" + year));

                datosMios[0]=diaFormateado + "/" + mesFormateado + "/" + year;

                BirthdayDate = yearString+"/"+mesFormateado+"/"+diaFormateado;
                formatNac=yearString+"-"+mesFormateado+"-"+diaFormateado;
                lbFecNac.setText(diaFormateado+"/"+mesFormateado+"/"+yearString);

            }
            //Estos valores deben ir en ese orden, de lo contrario no mostrara la fecha actual
            /**
             *También puede cargar los valores que usted desee
             */
        },yearshow, monthshow, dayshow);
        Calendar m=Calendar.getInstance();
        m.add(Calendar.YEAR, -18);
        recogerFecha.getDatePicker().setMaxDate(m.getTimeInMillis());
        Calendar m2=Calendar.getInstance();
        m2.add(Calendar.YEAR, -99);
        recogerFecha.getDatePicker().setMinDate(m2.getTimeInMillis());
        //Muestro el widget
        recogerFecha.show();
    }

    public void pintaPlanes(){
        pagoUnico=0.0;
        for(int i=0;i<cotizaTipos.getRateList().size();i++) {
            RateList rl=cotizaTipos.getRateList().get(i);
            print("suma: "+rl.getAmount()+" de: "+rl.getName());
            if(rl.getCoverageId()!=1 && rl.getCoverageId()!=2){
                pagoUnico=pagoUnico+rl.getAmount();
            }
            if(rl.getMovementTypePolicyId()==10 && rl.getCoverageId()==1) {
                amplioIndex=i;
                String v=String.format("%.2f", rl.getAmount());
                lbCostoAmplio.setText("$"+v);
                if(isSimulacion){
                    lbSimulaAmplio.setVisibility(View.VISIBLE);
                }
//                lbCostoAmplio.setText("$"+rl.getAmount());
            }
            if(rl.getMovementTypePolicyId()==10 && rl.getCoverageId()==2) {
                simpleIndex=i;
                String v=String.format("%.2f", rl.getAmount());
                lbCostoSimple.setText("$"+v);
                if(isSimulacion){
                    lbSimulaSimple.setVisibility(View.VISIBLE);
                }
//                lbCostoSimple.setText("$"+rl.getAmount());
            }
        }
        print("el pago unico es: "+String.format("%.2f", pagoUnico));
    }

    public void simula(final View v){
        if(v==lbSimulaSimple) {
            Gson gson = new Gson();
            String plan = gson.toJson(cotizaTipos.getRateList().get(simpleIndex));
            Intent i = new Intent(CotizarAutoActivity.this, ContratarActivity.class);
            i.putExtra("sexo", Gender);
            i.putExtra("vehicleId", VehicleTMMSDId);
            i.putExtra("birthday", BirthdayDate);
            i.putExtra("formatNac", formatNac);
            i.putExtra("garage", HasGarage);
            i.putExtra("cp", ZipCode);
            i.putExtra("plan", plan);
            i.putExtra("pagoUnico", pagoUnico);
            i.putExtra("id", cotizaTipos.getId());
            startActivity(i);
        }
        else{
            Gson gson = new Gson();
            String plan = gson.toJson(cotizaTipos.getRateList().get(amplioIndex));
            Intent i = new Intent(CotizarAutoActivity.this, ContratarActivity.class);
            i.putExtra("sexo", Gender);
            i.putExtra("vehicleId", VehicleTMMSDId);
            i.putExtra("birthday", BirthdayDate);
            i.putExtra("formatNac", formatNac);
            i.putExtra("garage", HasGarage);
            i.putExtra("cp", ZipCode);
            i.putExtra("plan", plan);
            i.putExtra("pagoUnico", pagoUnico);
            i.putExtra("id", cotizaTipos.getId());
            startActivity(i);
        }
    }
//========================change view===============================================================
    public void backStep(final boolean v,final boolean isDoble){
        TranslateAnimation anim = new TranslateAnimation(-1500,0,0,0);
        anim.setDuration(500);
        anim.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {
                if(v) {
                    svAuto.setVisibility(View.VISIBLE);
                }
                else {
                    svPersona.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                if(v) {
                    svAuto.setVisibility(View.VISIBLE);
                    svPersona.setVisibility(View.GONE);
                    pageIndex=0;
                }
                else {
                    svPersona.setVisibility(View.VISIBLE);
                    svCotizacion.setVisibility(View.GONE);
                    pageIndex=1;
                    if(isDoble){
                        backStep(true,false);
                    }
                }
            }
        });
        if(v) {
            svAuto.startAnimation(anim);
        }
        else{
            svPersona.startAnimation(anim);
        }
    }

//========================change view===============================================================
    public void nextStep(final View v){
        Log.e("Launch","Show next");
        if(v==null || v==lbNext) {
            if (!flagtipo) {
                Toast.makeText(this, "Selecciona el tipo de tu vehiculo.", Toast.LENGTH_SHORT).show();
                return;
            } else if (!flaganio) {
                Toast.makeText(this, "Selecciona el año de tu vehiculo.", Toast.LENGTH_SHORT).show();
                return;
            } else if (!flagmarca) {
                Toast.makeText(this, "Selecciona la marca de tu vehiculo.", Toast.LENGTH_SHORT).show();
                return;
            } else if (!flagmodelo) {
                Toast.makeText(this, "Selecciona el modelo de tu vehiculo.", Toast.LENGTH_SHORT).show();
                return;
            } else if (!flagdescripcion) {
                Toast.makeText(this, "Selecciona la descripción de tu vehiculo.", Toast.LENGTH_SHORT).show();
                return;
            }
        }
        TranslateAnimation anim = new TranslateAnimation(0,-1500,0,0);
        anim.setDuration(500);
        anim.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {
                try {
                    InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                    inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                }
                catch (Exception e){
                    e.printStackTrace();
                }
                if(v==null || v==lbNext) {
                    svPersona.setVisibility(View.VISIBLE);
                }
                else {
                    svCotizacion.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                if(v==null){
                    svAuto.setVisibility(View.GONE);
                    svPersona.setVisibility(View.VISIBLE);
                    pageIndex=1;
                    persona.setImageDrawable(getResources().getDrawable(R.drawable.icon_2_2));
                    lbDatos.setTextColor(getResources().getColor(R.color.azul_miituo));
                    sendCotizar(null);
                }
                else if(v==lbNext) {
                    svAuto.setVisibility(View.GONE);
                    svPersona.setVisibility(View.VISIBLE);
                    pageIndex=1;
                    persona.setImageDrawable(getResources().getDrawable(R.drawable.icon_2_2));
                    lbDatos.setTextColor(getResources().getColor(R.color.azul_miituo));
                }
                else{
                    svPersona.setVisibility(View.GONE);
                    svCotizacion.setVisibility(View.VISIBLE);
                    pageIndex=2;
                    cotizacion.setImageDrawable(getResources().getDrawable(R.drawable.icon_3_3));
                    lbCotiz.setTextColor(getResources().getColor(R.color.azul_miituo));
                }
            }
        });
        if(v==null || v==lbNext) {
            svAuto.startAnimation(anim);
        }
        else{
            svPersona.startAnimation(anim);
        }
    }

//===========================Thread call to fill combo==============================================
    public class getDataAuto extends AsyncTask<String,Void,Void> {

        ProgressDialog progress = new ProgressDialog(CotizarAutoActivity.this);
        String ErrorCode;

        String[] stringArray;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            arraySpinnerMarcas = new ArrayList<>();
            arraySpinnerSubtipos = new ArrayList<>();
            arraySpinnerDescripciones = new ArrayList<>();

            progress.setTitle("Información");
            progress.setMessage("Recuperando vehículos...");
            //progress.setIcon(R.drawable.miituo);
            progress.setIndeterminate(true);
            progress.setCancelable(false);
            progress.show();
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
            progress.dismiss();
            Toast message = Toast.makeText(CotizarAutoActivity.this, ErrorCode, Toast.LENGTH_LONG);
            message.show();
        }

        @Override
        protected Void doInBackground(String... strings) {

            ApiClientCotiza client = new ApiClientCotiza(CotizarAutoActivity.this);
            try {
                //recupera datos
                tokenTipos = client.getInfoTipoVehiculos("VehicleTypes");
                tokenModelos = client.getInfoModelosVehiculos("VehicleModel");
                tokenGarage = client.getInfoGarageTipos("Factors/GetFactorsGarage");

                llenarCombo(1);
                llenarCombo(2);

                //aprivecho para llenar los combos de sexo y garage
                llenarCombo(6); //sexo
                llenarCombo(7); //garage

                Log.e("tg","venga");
            } catch (Exception ex) {
                ErrorCode = ex.getMessage();
                this.cancel(true);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            flagtipo = false;
            Adaptador adaptador1 = new Adaptador(CotizarAutoActivity.this, R.layout.simple_spinner_down,arraySpinner);
            //adaptador1.setDropDownViewResource(R.layout.simple_spinner_down);
            tipo_vehiculo.setAdapter(adaptador1);

            Adaptador adaptador2 = new Adaptador(CotizarAutoActivity.this, R.layout.simple_spinner_down,arraySpinnerModelos);
            //adaptador2.setDropDownViewResource(R.layout.simple_spinner_down);
            anio.setAdapter(adaptador2);

            Adaptador adaptador3 = new Adaptador(CotizarAutoActivity.this, R.layout.simple_spinner_down,arraySpinnerGarage);
            //adaptador2.setDropDownViewResource(R.layout.simple_spinner_down);
            garagespinner.setAdapter(adaptador3);

            tipo_vehiculo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    //Log.e("Tag",arraySpinner[i]);
                    if(i==0)
                        return;
                    flagtipo = true;
                }
                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });

            anio.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                    if(i==0)
                        return;

                    flaganio = true;
                    limpiarCombos(1);
                    lbM1.setBackgroundColor(getResources().getColor(R.color.colorPrimaryUnsellected));
                    lbM2.setBackgroundColor(getResources().getColor(R.color.colorPrimaryUnsellected));
                    lbM3.setBackgroundColor(getResources().getColor(R.color.colorPrimaryUnsellected));
                    lbM4.setBackgroundColor(getResources().getColor(R.color.colorPrimaryUnsellected));
                    lbM5.setBackgroundColor(getResources().getColor(R.color.colorPrimaryUnsellected));
                    lbM6.setBackgroundColor(getResources().getColor(R.color.colorPrimaryUnsellected));
                    getDataMarcas marcas = new getDataMarcas();
                    marcas.execute(tokenModelos.get(i).getId()+"","1");

                    datosAuto[0] = tokenModelos.get(i).getModel()+"";
                }
                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });

            garagespinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                    if(i==0)
                        return;

                    HasGarage = String.valueOf(tokenGarage.get(i).getId());
                    datosMios[3]=tokenGarage.get(i).getDescription();

                }
                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });
            limpiarCombos(1);
            progress.dismiss();
        }
    }

//===========================Thread call to fill combo==============================================
    public class getDataMarcas extends AsyncTask<String,Void,Void>{

        ProgressDialog progress = new ProgressDialog(CotizarAutoActivity.this);
        String ErrorCode;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progress.setTitle("Información");
            progress.setMessage("Recuperando marcas...");
            //progress.setIcon(R.drawable.miituo);
            progress.setIndeterminate(true);
            progress.setCancelable(false);
            progress.show();
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
            progress.dismiss();
            Toast message = Toast.makeText(CotizarAutoActivity.this, ErrorCode, Toast.LENGTH_LONG);
            message.show();
        }

        @Override
        protected Void doInBackground(String... strings) {

            ApiClientCotiza client = new ApiClientCotiza(CotizarAutoActivity.this);
            try {
                //recupera datos
                tokenMarcas = client.getInfoMarcaVehiculos("VehicleBrands",strings[0],strings[1]);
                llenarCombo(3);

                Log.e("tg","venga");
            } catch (Exception ex) {
                ErrorCode = ex.getMessage();
                this.cancel(true);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            flagmarca=false;
            Adaptador adaptador = new Adaptador(CotizarAutoActivity.this, R.layout.simple_spinner_down,arraySpinnerMarcas);
            //adaptador.setDropDownViewResource(R.layout.simple_spinner_down);
            marca.setAdapter(adaptador);
            marca.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    Log.e("Tag",arraySpinnerMarcas.get(i));

                    if(i==0)
                        return;

                    flagmarca=true;
                    limpiarCombos(2);
                    getDataModelos subtipos = new getDataModelos();
                    subtipos.execute(tokenMarcas.get(i).getId()+"");

                    datosAuto[1] = tokenMarcas.get(i).getDescription()+"";

                }
                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {
                }
            });

            progress.dismiss();
        }
    }

//===========================Thread call to fill combo==============================================
    public class getDataModelos extends AsyncTask<String,Void,Void>{

        ProgressDialog progress = new ProgressDialog(CotizarAutoActivity.this);
        String ErrorCode;
        List<MarcasVehiculos> tokenModelos;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progress.setTitle("Información");
            progress.setMessage("Recuperando modelos...");
            //progress.setIcon(R.drawable.miituo);
            progress.setIndeterminate(true);
            progress.setCancelable(false);
            progress.show();
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
            progress.dismiss();
            Toast message = Toast.makeText(CotizarAutoActivity.this, ErrorCode, Toast.LENGTH_LONG);
            message.show();
        }

        @Override
        protected Void doInBackground(String... strings) {
            ApiClientCotiza client = new ApiClientCotiza(CotizarAutoActivity.this);
            try {
                //recupera datos
                tokenSubtipos = client.getInfoSubtiposVehiculos("VehicleSubTypes/Brand/",strings[0]);
                llenarCombo(4);

                Log.e("tg","venga");
            } catch (Exception ex) {
                ErrorCode = ex.getMessage();
                this.cancel(true);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            flagmodelo=false;
            Adaptador adaptador = new Adaptador(CotizarAutoActivity.this, R.layout.simple_spinner_down,arraySpinnerSubtipos);
            //adaptador.setDropDownViewResource(R.layout.simple_spinner_down);
            subtipo.setAdapter(adaptador);
            subtipo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    Log.e("Tag",arraySpinnerSubtipos.get(i));

                    if(i==0)
                        return;

                    flagmodelo=true;
                    flagdescripcion=false;
                    limpiarCombos(3);
                    getDataDescripcion descripcion = new getDataDescripcion();
                    descripcion.execute(tokenSubtipos.get(i).getId()+"");

                    datosAuto[2]=tokenSubtipos.get(i).getDescription()+"";

                }
                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });

            progress.dismiss();
        }
    }

//===========================Thread call to fill combo==============================================
    public class getDataDescripcion extends AsyncTask<String,Void,Void>{

        ProgressDialog progress = new ProgressDialog(CotizarAutoActivity.this);
        String ErrorCode;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progress.setTitle("Información");
            progress.setMessage("Recuperando descripción...");
            //progress.setIcon(R.drawable.miituo);
            progress.setIndeterminate(true);
            progress.setCancelable(false);
            progress.show();
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
            progress.dismiss();
            Toast message = Toast.makeText(CotizarAutoActivity.this, ErrorCode, Toast.LENGTH_LONG);
            message.show();
        }

        @Override
        protected Void doInBackground(String... strings) {

            ApiClientCotiza client = new ApiClientCotiza(CotizarAutoActivity.this);
            try {
                //recupera datos
                tokenDescripciones = client.getInfoDescripcionVehiculos("VehicleDescriptions/Subtype/",strings[0]);
                llenarCombo(5);

                Log.e("tg","venga");
            } catch (Exception ex) {
                if(tokenDescripciones!=null){
                tokenDescripciones.clear();}
                ErrorCode = ex.getMessage();
                this.cancel(true);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            flagdescripcion=false;
            Adaptador adaptador = new Adaptador(CotizarAutoActivity.this, R.layout.simple_spinner_down,arraySpinnerDescripciones);
            //adaptador.setDropDownViewResource(R.layout.simple_spinner_down);
            descripcion.setAdapter(adaptador);
            descripcion.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                    if(i==0)
                        return;
                    flagdescripcion = true;
                    Log.e("Tag Descripcion",tokenDescripciones.get(i).getId()+"");

                    VehicleTMMSDId = String.valueOf(tokenDescripciones.get(i).getId());
                    datosAuto[3]=tokenDescripciones.get(i).getDescription()+"";

                }
                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });

            progress.dismiss();
        }
    }

//===========================Thread call to fill combo==============================================
    public class Adaptador extends ArrayAdapter<String> {

        public List<String> lista;

        public Adaptador(@NonNull Context context, int resource, @NonNull List<String> objects) {
            super(context, resource, objects);

            lista = objects;
        }

        @Override
        public void setDropDownViewResource(int resource) {
            super.setDropDownViewResource(R.layout.simple_spinner_down);
        }

        @Override
        public boolean isEnabled(int position){
            if(position == 0)
            {
                // Disable the first item from Spinner
                // First item will be use for hint
                return false;
            }
            else
            {
                return true;
            }
        }

        @Override
        public View getDropDownView(int position, View convertView, ViewGroup parent) {
            View view = super.getDropDownView(position, convertView, parent);
            TextView tv = (TextView) view;
            if(position == 0){
                // Set the hint text color gray
                tv.setTextColor(Color.GRAY);
            }
            else {
                tv.setTextColor(Color.BLACK);
            }
            return view;
        }
    }

//===========================clear combos==============================================
    public void limpiarCombos(int ind){

        switch (ind) {
            case 1:
                limpiarMarca();
                limpiarSubtipo();
                limpiarDescripction();
                break;
            case 2:
                limpiarSubtipo();
                limpiarDescripction();
                break;
            case 3:
                limpiarDescripction();
                break;
            default:
                break;
        }
    }

    private void llenarCombo(int indice) {

        switch (indice){
            case 1:
                arraySpinner = new ArrayList<>();//[tokenTipos.size()+1];
                arraySpinner.clear();
                arraySpinner.add("");
                for(int i=0; i<tokenTipos.size();i++){
                    arraySpinner.add(tokenTipos.get(i).getDescription());
                }
                tokenTipos.add(0,new TiposVehiculos(-1,""));
                break;
            case 2:
                arraySpinnerModelos = new ArrayList<>();//new String[tokenModelos.size()+1];
                arraySpinnerModelos.clear();
                arraySpinnerModelos.add("");
                for(int i=0;i<tokenModelos.size();i++){
                    arraySpinnerModelos.add(tokenModelos.get(i).getModel()+"");
                }
                tokenModelos.add(0,new ModelosVehiculos(-1,-1));
                break;

            case 3:
                arraySpinnerMarcas.clear();
                arraySpinnerMarcas.add("");
                for(int i=0;i<tokenMarcas.size();i++){
                    arraySpinnerMarcas.add(tokenMarcas.get(i).getDescription());
                }
                tokenMarcas.add(0,new MarcasVehiculos(-1,""));
                break;

            case 4:
                arraySpinnerSubtipos.clear();
                arraySpinnerSubtipos.add("");
                for(int i=0;i<tokenSubtipos.size();i++){
                    arraySpinnerSubtipos.add(tokenSubtipos.get(i).getDescription());
                }
                tokenSubtipos.add(0,new MarcasVehiculos(-1,""));
                break;

            case 5:
                arraySpinnerDescripciones.clear();
                arraySpinnerDescripciones.add("");
                for(int i=0;i<tokenDescripciones.size();i++){
                    arraySpinnerDescripciones.add(tokenDescripciones.get(i).getDescription());
                }
                tokenDescripciones.add(0,new MarcasVehiculos(-1,""));
                break;

            case 6:

                arraySpinnerGarage = new ArrayList<>();//new String[tokenModelos.size()+1];
                arraySpinnerGarage.clear();
                arraySpinnerGarage.add("");
                for(int i=0;i<tokenGarage.size();i++){
                    arraySpinnerGarage.add(tokenGarage.get(i).getDescription()+"");
                }
                tokenGarage.add(0,new GarageTipos(0,"",0,0));
                break;

            case 7:
                arraySpinnerSexo = new ArrayList<>();
                arraySpinnerSexo.add("Sexo");
                arraySpinnerSexo.add("Mujer");
                arraySpinnerSexo.add("Hombre");
                break;

            default:
                break;
        }
    }

    public void limpiarMarca(){
        arraySpinnerMarcas.clear();
        arraySpinnerMarcas.add("");
        Adaptador adaptador1 = new Adaptador(CotizarAutoActivity.this, R.layout.simple_spinner_down,arraySpinnerMarcas);
        marca.setAdapter(adaptador1);
    }

    public void limpiarSubtipo(){
        arraySpinnerSubtipos.clear();
        arraySpinnerSubtipos.add("");
        Adaptador adaptador1 = new Adaptador(CotizarAutoActivity.this, R.layout.simple_spinner_down,arraySpinnerSubtipos);
        subtipo.setAdapter(adaptador1);
    }

    public void limpiarDescripction(){
        arraySpinnerDescripciones.clear();
        arraySpinnerDescripciones.add("");
        Adaptador adaptador1 = new Adaptador(CotizarAutoActivity.this, R.layout.simple_spinner_down,arraySpinnerDescripciones);
        descripcion.setAdapter(adaptador1);

    }

    //=======================Send data to cotizar=======================================================
    public void sendCotizar(View v){
        if(BirthdayDate.equals("")){
            Toast.makeText(this,"Coloca tu fecha de nacicmiento.",Toast.LENGTH_SHORT).show();
        }else if(Gender.equals("0")){
            Toast.makeText(this,"Selecciona tu género.",Toast.LENGTH_SHORT).show();
        }else if(postal.getText().toString().equals("") || postal.getText().toString().length()<5){
            Toast.makeText(this,"Coloca tu código postal.",Toast.LENGTH_SHORT).show();
        }else if(HasGarage.equals("")){
            Toast.makeText(this,"Coloca tu tipo de garaje.",Toast.LENGTH_SHORT).show();
        }else {
            ZipCode = postal.getText().toString();
            datosMios[2]=ZipCode;
            new CPChecker().execute();
        }
    }

    //=======================Enviar cotizacion=======================================================
    public void EnviarCotizacion(View v){
        if(etNombre.getText().toString().equals("")){
            Toast.makeText(this,"Por favor ingresa tu nombre.",Toast.LENGTH_SHORT).show();
        }else if(etCorreo.getText().toString().equals("") || !etCorreo.getText().toString().contains("@")){
            Toast.makeText(this,"Por favor ingresa tu correo.",Toast.LENGTH_SHORT).show();
        }else if(etTelefono.getText().toString().equals("") || etTelefono.getText().toString().length()!=10){
            Toast.makeText(this," Por favor ingresa un número de al menos 10 dígitos",Toast.LENGTH_SHORT).show();
        }else {
            sendMailCotizar send = new sendMailCotizar(this);
            send.execute(etNombre.getText().toString(), etCorreo.getText().toString(),etTelefono.getText().toString());
        }
    }

    public void goToDetails(View v){
        Intent i=new Intent(this,DetallePlanActivity.class);
        i.putExtra("sexo",Gender);
        i.putExtra("vehicleId",VehicleTMMSDId);
        i.putExtra("birthday",BirthdayDate);
        i.putExtra("formatNac",formatNac);
        i.putExtra("garage",HasGarage);
        i.putExtra("cp",ZipCode);
        i.putExtra("pagoUnico",pagoUnico);
        i.putExtra("id",cotizaTipos.getId());
        Gson gson = new Gson();
        String plan="";
        if(v==cntda){
            plan=gson.toJson(cotizaTipos.getRateList().get(amplioIndex));
            System.out.println("json: "+plan);
            i.putExtra("plan",plan);
        }
        else{
            plan=gson.toJson(cotizaTipos.getRateList().get(simpleIndex));
            System.out.println("json: "+plan);
            i.putExtra("plan",plan);
        }
        if(!isSimulacion){
            i.putExtra("isSimulacion",false);
        }
        else{
            i.putExtra("isSimulacion",true);
        }
        startActivity(i);
    }

//===========================Thread call to fill combo==============================================
    public class getDataCotizar extends AsyncTask<String,Void,Void>{

        ProgressDialog progress = new ProgressDialog(CotizarAutoActivity.this);
        String ErrorCode;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progress.setTitle("Información");
            progress.setMessage("Recuperando datos...");
            //progress.setIcon(R.drawable.miituo);
            progress.setIndeterminate(true);
            progress.setCancelable(false);
            progress.show();
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
            progress.dismiss();
            Toast message = Toast.makeText(CotizarAutoActivity.this, ErrorCode, Toast.LENGTH_LONG);
            message.show();
        }

        @Override
        protected Void doInBackground(String... strings) {

            ApiClientCotiza client = new ApiClientCotiza(CotizarAutoActivity.this);
            try {
                //recupera datos
                cotizaTipos = client.getQuotation("Quotation",Gender,VehicleTMMSDId,BirthdayDate,HasGarage,ZipCode);
            } catch (Exception ex) {
                ErrorCode = ex.getMessage();
                this.cancel(true);

            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if(cotizaTipos != null){
                if(cotizaTipos.getRateList().size()>0){
                    pintaPlanes();
                    nextStep(lbCotizar);
//                    Intent i = new Intent(CotizarAutoActivity.this,PlanesActivityTab.class);
//                    startActivity(i);
                }
            }
            progress.dismiss();
        }
    }

    //===========================Thread send mail=======================================================
    public class sendMailCotizar extends AsyncTask<String,Void,Void> {

        ProgressDialog progress;
        String ErrorCode;
        String res;
        Context c;
        AlertDialog alerta;

        public sendMailCotizar(Context c){
            this.c=c;
            progress = new ProgressDialog(this.c);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progress.setTitle("Información");
            progress.setMessage("Enviando correo...");
            //progress.setIcon(R.drawable.miituo);
            progress.setIndeterminate(true);
            progress.setCancelable(false);
            progress.show();
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
            progress.dismiss();
            Toast.makeText(c, ErrorCode, Toast.LENGTH_LONG).show();
        }

        @Override
        protected Void doInBackground(String... strings) {

            ApiClientCotiza client = new ApiClientCotiza(CotizarAutoActivity.this);
            try {
                //recupera datos
                res = client.generatingCall("Poll/PreCotization",strings[0],strings[2],strings[1],"",cotizaTipos.getId(),5);
                res = client.updateQuotation("Quotation",cotizaTipos.getId(),strings[0],strings[1],strings[2]);
                res = client.sendMailQuotation("Quotation/Email",1,strings[1]);

            } catch (Exception ex) {
                ErrorCode = ex.getMessage();
                this.cancel(true);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            progress.dismiss();
            if(res != null){
                if(!res.equals("")){

                    AlertDialog.Builder builder = new AlertDialog.Builder(c);
                    builder.setTitle("");
                    builder.setMessage("La cotización ha sido enviada a su correo electrónico");
                    builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //Intent i = new Intent(LastOdometerActivity.this, PrincipalActivity.class);
                            //startActivity(i);
                            alerta.dismiss();
                        }
                    });
                    alerta = builder.create();
                    alerta.show();

                }else{
                    AlertDialog.Builder builder = new AlertDialog.Builder(c);
                    builder.setTitle("");
                    builder.setMessage("Tuvimos un problema, intente más tarde.");
                    builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //Intent i = new Intent(LastOdometerActivity.this, PrincipalActivity.class);
                            //startActivity(i);
                            alerta.dismiss();
                        }
                    });
                    alerta = builder.create();
                    alerta.show();
                }
            }else{

            }
        }
    }

    //===========================Thread call to fill combo==============================================
    public class CPChecker extends AsyncTask<String,Void,Void> {

        String ErrorCode,res;
        AlertDialog alerta;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
            AlertDialog.Builder builder = new AlertDialog.Builder(CotizarAutoActivity.this);
            builder.setTitle("");
//            builder.setMessage(ErrorCode);
            builder.setMessage("El Código Postal es inválido, introduce uno correcto.");
            builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    alerta.dismiss();
                }
            });
            alerta = builder.create();
            alerta.show();
        }

        @Override
        protected Void doInBackground(String... strings) {

            ApiClientCotiza client = new ApiClientCotiza(CotizarAutoActivity.this);
            try {
                //recupera datos
                this.res = client.CPChecker("CP",Gender,VehicleTMMSDId,BirthdayDate,ZipCode);
            } catch (Exception ex) {
                ErrorCode = ex.getMessage();
                this.cancel(true);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            if(res!=null && res.equalsIgnoreCase("true")){
                new getDataCotizar().execute();
            }
            else{
                AlertDialog.Builder builder = new AlertDialog.Builder(CotizarAutoActivity.this);
                builder.setTitle("");
                builder.setMessage("El Código Postal es inválido, introduce uno correcto.");
                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        alerta.dismiss();
                    }
                });
                alerta = builder.create();
                alerta.show();
            }
        }
    }
}
