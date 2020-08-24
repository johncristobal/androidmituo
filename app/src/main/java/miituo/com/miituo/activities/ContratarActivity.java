package miituo.com.miituo.activities;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.net.http.SslError;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.text.InputFilter;
import android.text.InputType;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.method.DigitsKeyListener;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.InputMethodManager;
import android.webkit.SslErrorHandler;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.regex.Pattern;

import miituo.com.miituo.R;
import miituo.com.miituo.api.ApiClient;
import miituo.com.miituo.cotizar.ApiClientCotiza;
import miituo.com.miituo.cotizar.RateList;
import miituo.com.miituo.delegates.ContratarDelegate;
import miituo.com.miituo.threats.GetSync;
import miituo.com.miituo.threats.ValidatingCuponSync;
import miituo.com.miituo.utils.Alarma;
import miituo.com.miituo.utils.Evento;
import miituo.com.miituo.utils.SimpleCallBack;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;

public class ContratarActivity extends BaseActivity implements SimpleCallBack{
    private Typeface tipo;
    private static boolean isRFCValid=true;
    public int idCotizacion=0;
    public String cupon=null;
    double pagoUnico=0.0;
    JSONArray arrayProfs,arrayCols,arrayDels,arrayEdo;

    ImageView personas;
    ImageView auto;
    ImageView validacion, ipago;

    private TextView lbPlanSel,lbConductor,lbAuto,lbValidacion,lbPago,
    /////////////////////contractor/////////////////////////
                     lbMismaPersona,lbNombreC,lbAPC,lbAMC,lbNacC,etNacC,lbCPC,lbEmailC,lbRFCC,lbHomoC,lbProf,lbNacionalidadC,lbColC,
                     lbDelC,lbEdoC,lbCalleC,lbExtC,lbIntC,lbSexC,lbCelC,lbConfirmCelC,lbRFCHelp,lbAvisoPriv,lbAvisoPrivLink,
    ////////////////////conductor/////////////////////////
                     lbNombreD,lbAPD,lbAMD,lbEmailD,lbCelD,lbConfirmCelD,lbDatosConductor,lbDatosContractor,
    ////////////////////  auto   //////////////////////////
                     lbSerieAuto,lbColorAuto,lbPlacasAuto,
    ///////////////////  validar  //////////////////////
                     lbToken,lbToken2,lbCelEdit,lbResendToken,
    ///////////////////  token  /////////////////////////
                     lbtitulo,lb1,lb2,lb3,lb4,
    ///////////////////  pago   /////////////////////////
                     lbHasCupon,lbValidaCodigo,lbPromo1,lbPromo2,lbPromo3,
    ////////////////////  botones  ////////////////////////
                     lbToAuto,lbToValidar,lbToPago,lbAceptar;

    private WebView wvPago;

    ////////////////////  personas  /////////////////////
    public EditText etNombreC,etAPC,etAMC,etCPC,etEmailC,etRFCC,etHomoC,
                     etDelC,etEdoC,etCalleC,etExtC,etIntC,etCelC,etConfirmCelC,
                     etNombreD,etAPD,etAMD,etEmailD,etCelD,etConfirmCelD,
    ///////////////////  auto   ////////////////////////
                     etSerieAuto,etColorAuto,etPlacasAuto,
    ///////////////////  validar  //////////////////////
                     etToken,etCupon;

    public RadioButton lbSi,lbNo,rbHC,rbMC;

    private LinearLayout cntConductor,cntDatosAuto,cntTarjetas,cntAmex,cntPaypal, cntPromo;

    private RelativeLayout rlNacC,rlCPC,cntDatosConductor,cntValidar,cntAviso,cntPagar;

    private RadioGroup cntSexC;

    List<String> arraySpinnerProfecion;
    List<String> arraySpinnerNacionalidad;
    List<String> arraySpinnerColonia;

    public Spinner sProfecion,sNacionalidad,sColonia;
    public ScrollView scroll;

    public String token="", dataQuotation,referenciaPago=null,pago="{}";
    public int tipoPago=1,ultimoTipoPago=1;

    /********************* datos car/driver ****************************/
    public String sexD,vehicleId,birthdayD,garage,cpD,plan,formatNacD,nombreD,apD,amD,mailD,celD,reCelD,serieVehicle,colorVehicle,placasVehicle;
    /********************* datos contractor ****************************/
    public String nombreC,apC,amC,birthdayC,formatNacC,cpC,mailC,rfcC,homoclaveC,profesionIdC,profesionDescC,
            nacionalidadC,colC,colDescC,delC,edoC,calleC,extC,intC,sexC,celC,reCelC;

    public RateList rl;
    int pageIndex=0;

    public boolean bandera=false;
    ContratarDelegate delegate;

    @Override
    public void onResume(){
        super.onResume();
        if(pageIndex==3){
//            if(cntTarjetas.getBackground()!=getResources().getDrawable(R.drawable.blueborder)){
            if(tipoPago==2 && ultimoTipoPago==3){
                cntTarjetas.setBackground(getResources().getDrawable(R.drawable.blueborder));
                cntPaypal.setBackground(getResources().getDrawable(R.drawable.border));
                cntAmex.setBackground(getResources().getDrawable(R.drawable.border));
                tipoPago = 3;
//                showUrl();
            }
            else if(tipoPago==2 && ultimoTipoPago==1){
                cntTarjetas.setBackground(getResources().getDrawable(R.drawable.border));
                cntPaypal.setBackground(getResources().getDrawable(R.drawable.border));
                cntAmex.setBackground(getResources().getDrawable(R.drawable.blueborder));
                tipoPago = 1;
//                showUrl();
            }
        }
    }

//    protected void onPause(){
//        super.onPause();
//        ultimoTipoPago=tipoPago;
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contratar);
            initGral();
            init1();
            init2();
            init3();
        setData();
        new getProfs().execute();
        new getCols().execute();
        init4();
        delegate=new ContratarDelegate(this,this);
        new ContratarDelegate.getDataQuotation(idCotizacion,new SimpleCallBack() {
            @Override
            public void run(boolean status, String res) {
                print(res);
                if(status) {
                    dataQuotation=res;
                }
            }
        }).execute();
    }

    private void setData(){
        Intent i=getIntent();
        sexD=i.getStringExtra("sexo");
        sexC=sexD;
        vehicleId=i.getStringExtra("vehicleId");
        birthdayD=i.getStringExtra("birthday");
        birthdayC=birthdayD;
        formatNacD=i.getStringExtra("formatNac");
        formatNacC=formatNacD;
        garage=i.getStringExtra("garage");
        cpD=i.getStringExtra("cp");
        cpC=cpD;
        plan=i.getStringExtra("plan");
        idCotizacion=i.getIntExtra("id",0);
        pagoUnico=i.getDoubleExtra("pagoUnico",0.0);
        pagoUnico=Math.floor(pagoUnico * 100) / 100;
        Gson parseJson = new GsonBuilder().create();
        rl=parseJson.fromJson(getIntent().getExtras().getString("plan"), new TypeToken<RateList>() {}.getType());
        lbPlanSel.setText(rl.getName()+" $"+rl.getAmount()+" por km");

        arraySpinnerNacionalidad = new ArrayList<>();
        arraySpinnerNacionalidad.add("MEXICANA");
        ContratarActivity.Adaptador adaptador3 = new ContratarActivity.Adaptador(ContratarActivity.this, R.layout.simple_spinner_down,arraySpinnerNacionalidad);
        sNacionalidad.setAdapter(adaptador3);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_llamar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            if(pageIndex>0 && pageIndex<3){
                backStep(pageIndex,false);
            }
            else if(pageIndex==0){
                AlertDialog.Builder builder = new AlertDialog.Builder(ContratarActivity.this);
                builder.setTitle("¿Deseas abandonar el sitio?");
                builder.setMessage("Es posible que los cambios que implementaste no se puedan guardar.");
                builder.setCancelable(false);
                builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
//                    super.onBackPressed();
                        alertToClose.dismiss();
                        finish();
                    }
                });
                builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        alertToClose.dismiss();
                    }
                });
                alertToClose = builder.create();
                alertToClose.show();
            }
        }
        else if (item.getItemId() == R.id.menu_llamar) {
            Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", "018009530059", null));
            startActivity(intent);
        }
        return false;
    }

    AlertDialog alertToClose;
    @Override
    public void onBackPressed() {
        if(pageIndex>0 && pageIndex<3){
            backStep(pageIndex,false);
        }
        else if(pageIndex==0){
            AlertDialog.Builder builder = new AlertDialog.Builder(ContratarActivity.this);
            builder.setTitle("¿Deseas abandonar el sitio?");
            builder.setMessage("Es posible que los cambios que implementaste no se puedan guardar.");
            builder.setCancelable(false);
            builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
//                    super.onBackPressed();
                    alertToClose.dismiss();
                    finish();
                }
            });
            builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    alertToClose.dismiss();
                }
            });
            alertToClose = builder.create();
            alertToClose.show();
        }
    }

    private void initGral() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Contratar");
        toolbar.setTitleTextColor(Color.BLACK);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        tipo = Typeface.createFromAsset(getAssets(), "fonts/herne1.ttf");

        personas=(ImageView) findViewById(R.id.conductor);
        auto=(ImageView) findViewById(R.id.auto);
        validacion=(ImageView) findViewById(R.id.validacion);
        ipago=(ImageView) findViewById(R.id.pago);
        personas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(pageIndex==3){
                    return;
                }
                if(pageIndex==1){
                    backStep(1,false);
                }
                else if(pageIndex==2){
                    backStep(2,true);
                }
            }
        });
        auto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(pageIndex==3){
                    return;
                }
                if(pageIndex==0){
                    nextStep(lbToAuto,false);
                }
                else if(pageIndex==2){
                    backStep(2,false);
                }
            }
        });
        validacion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(pageIndex==3){
                    return;
                }
                if(pageIndex==1){
                    new checkSerieAuto().execute();
                }
                else if(pageIndex==0){
                    nextStep(lbToAuto,true);
                }
            }
        });

        lbPlanSel = (TextView) findViewById(R.id.lbPlanSel);
        lbPlanSel.setTypeface(tipo, Typeface.BOLD);

        lbConductor = (TextView) findViewById(R.id.lbConductor);
        lbConductor.setTypeface(tipo);
        lbAuto = (TextView) findViewById(R.id.lbAuto);
        lbAuto.setTypeface(tipo);
        lbValidacion = (TextView) findViewById(R.id.lbValidacion);
        lbValidacion.setTypeface(tipo);
        lbPago = (TextView) findViewById(R.id.lbPago);
        lbPago.setTypeface(tipo);

        arraySpinnerProfecion = new ArrayList<>();
        arraySpinnerNacionalidad = new ArrayList<>();
        arraySpinnerColonia = new ArrayList<>();

        scroll=(ScrollView)findViewById(R.id.scrolConductor);
        cntDatosConductor=(RelativeLayout) findViewById(R.id.cntDatosConductor);
        cntDatosAuto=(LinearLayout) findViewById(R.id.cntDatosAuto);
        cntValidar=(RelativeLayout) findViewById(R.id.cntValidar);
        cntPagar=(RelativeLayout) findViewById(R.id.cntPagar);
    }

    private void init1(){
        lbMismaPersona=(TextView)findViewById(R.id.lbMismaPersona);
        lbMismaPersona.setTypeface(tipo);
        lbNombreC=(TextView)findViewById(R.id.lbNombreC);
        lbNombreC.setTypeface(tipo);
        lbAPC=(TextView)findViewById(R.id.lbAPC);
        lbAPC.setTypeface(tipo);
        lbAMC=(TextView)findViewById(R.id.lbAMC);
        lbAMC.setTypeface(tipo);
        lbNacC=(TextView)findViewById(R.id.lbNacC);
        lbNacC.setTypeface(tipo);
        etNacC=(TextView)findViewById(R.id.etNacC);
        etNacC.setTypeface(tipo);
        lbCPC=(TextView)findViewById(R.id.lbCPC);
        lbCPC.setTypeface(tipo);
        lbEmailC=(TextView)findViewById(R.id.lbEmailC);
        lbEmailC.setTypeface(tipo);
        lbRFCC=(TextView)findViewById(R.id.lbRFCC);
        lbRFCC.setTypeface(tipo);
        lbHomoC=(TextView)findViewById(R.id.lbHomoC);
        lbHomoC.setTypeface(tipo);
        lbProf=(TextView)findViewById(R.id.lbProf);
        lbProf.setTypeface(tipo);
        lbNacionalidadC=(TextView)findViewById(R.id.lbNacionalidadC);
        lbNacionalidadC.setTypeface(tipo);
        lbColC=(TextView)findViewById(R.id.lbColC);
        lbColC.setTypeface(tipo);
        lbDelC=(TextView)findViewById(R.id.lbDelC);
        lbDelC.setTypeface(tipo);
        lbEdoC=(TextView)findViewById(R.id.lbEdoC);
        lbEdoC.setTypeface(tipo);
        lbCalleC=(TextView)findViewById(R.id.lbCalleC);
        lbCalleC.setTypeface(tipo);
        lbExtC=(TextView)findViewById(R.id.lbExtC);
        lbExtC.setTypeface(tipo);
        lbIntC=(TextView)findViewById(R.id.lbIntC);
        lbIntC.setTypeface(tipo);
        lbSexC=(TextView)findViewById(R.id.lbSexC);
        lbSexC.setTypeface(tipo);
        lbCelC=(TextView)findViewById(R.id.lbNoCelC);
        lbCelC.setTypeface(tipo);
        lbConfirmCelC=(TextView)findViewById(R.id.lbConfirmCelC);
        lbConfirmCelC.setTypeface(tipo);
        lbRFCHelp=(TextView)findViewById(R.id.lbRFCHelp);
        lbRFCHelp.setTypeface(tipo);
        lbNombreD=(TextView)findViewById(R.id.lbNombreD);
        lbNombreD.setTypeface(tipo);
        lbAPD=(TextView)findViewById(R.id.lbAPD);
        lbAPD.setTypeface(tipo);
        lbAMD=(TextView)findViewById(R.id.lbAMD);
        lbAMD.setTypeface(tipo);
        lbEmailD=(TextView)findViewById(R.id.lbEmailD);
        lbEmailD.setTypeface(tipo);
        lbCelD=(TextView)findViewById(R.id.lbNoCelD);
        lbCelD.setTypeface(tipo);
        lbConfirmCelD=(TextView)findViewById(R.id.lbConfirmCelD);
        lbConfirmCelD.setTypeface(tipo);
        lbDatosConductor=(TextView)findViewById(R.id.lbDatosConductor);
        lbDatosConductor.setTypeface(tipo,Typeface.BOLD);
        lbDatosContractor=(TextView)findViewById(R.id.lbDatosContractor);
        lbDatosContractor.setTypeface(tipo,Typeface.BOLD);
        lbToAuto=(TextView)findViewById(R.id.lbToAuto);
        lbToAuto.setTypeface(tipo);
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

        etNombreC=(EditText)findViewById(R.id.etNombreC);
        etNombreC.setFilters(new InputFilter[]{new InputFilter.AllCaps(),new InputFilter.LengthFilter(20)});
        etNombreC.setInputType(InputType.TYPE_CLASS_TEXT);
        etNombreC.setTypeface(tipo);
        etNombreC.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                etRFCC.setText("");
            }
        });
        etAPC=(EditText)findViewById(R.id.etAPC);
        etAPC.setFilters(new InputFilter[]{new InputFilter.AllCaps(),new InputFilter.LengthFilter(20)});
        etAPC.setInputType(InputType.TYPE_CLASS_TEXT);
        etAPC.setTypeface(tipo);
        etAPC.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                etRFCC.setText("");
            }
        });
        etAMC=(EditText)findViewById(R.id.etAMC);
        etAMC.setFilters(new InputFilter[]{new InputFilter.AllCaps(),new InputFilter.LengthFilter(20)});
        etAMC.setInputType(InputType.TYPE_CLASS_TEXT);
        etAMC.setTypeface(tipo);
        etAMC.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                etRFCC.setText("");
            }
        });
        etCPC=(EditText)findViewById(R.id.etCPC);
        etCPC.setFilters(new InputFilter[]{new InputFilter.AllCaps(),new InputFilter.LengthFilter(5)});
        etCPC.setInputType(InputType.TYPE_CLASS_NUMBER);
        etCPC.setTypeface(tipo);
        etCPC.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                cpC=etCPC.getText().toString();
                if(!hasFocus){
                    if(!isDatoValido(cpC) || cpC.length()<5){
                        Toast.makeText(ContratarActivity.this,"Verifica el CP del contratante",Toast.LENGTH_LONG).show();
                        return;
                    }
                    else {
                        if(lbNo.isChecked()){
                            new getCols().execute();
                        }
                    }
                }
            }
        });
        etEmailC=(EditText)findViewById(R.id.etEmailC);
        etEmailC.setFilters(new InputFilter[]{new InputFilter.LengthFilter(40)});
        etEmailC.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
        etEmailC.setTypeface(tipo);
        etRFCC=(EditText)findViewById(R.id.etRFCC);
        etRFCC.setFilters(new InputFilter[]{new InputFilter.AllCaps(),new InputFilter.LengthFilter(10)});
        etRFCC.setInputType(InputType.TYPE_CLASS_TEXT);
        etRFCC.setTypeface(tipo);
        etHomoC=(EditText)findViewById(R.id.etHomoC);
        etHomoC.setFilters(new InputFilter[]{new InputFilter.AllCaps(),new InputFilter.LengthFilter(3)});
        etHomoC.setInputType(InputType.TYPE_CLASS_TEXT);
        etHomoC.setTypeface(tipo);
        etDelC=(EditText)findViewById(R.id.etDelC);
        etDelC.setFilters(new InputFilter[]{new InputFilter.AllCaps(),new InputFilter.LengthFilter(20)});
        etDelC.setInputType(InputType.TYPE_CLASS_TEXT);
        etDelC.setTypeface(tipo);
        etEdoC=(EditText)findViewById(R.id.etEdoC);
        etEdoC.setFilters(new InputFilter[]{new InputFilter.AllCaps(),new InputFilter.LengthFilter(20)});
        etEdoC.setInputType(InputType.TYPE_CLASS_TEXT);
        etEdoC.setTypeface(tipo);
        etCalleC=(EditText)findViewById(R.id.etCalleC);
        etCalleC.setFilters(new InputFilter[]{new InputFilter.AllCaps(),new InputFilter.LengthFilter(20)});
        etCalleC.setInputType(InputType.TYPE_CLASS_TEXT);
        etCalleC.setTypeface(tipo);
        etExtC=(EditText)findViewById(R.id.etExtC);
        etExtC.setFilters(new InputFilter[]{new InputFilter.AllCaps(),new InputFilter.LengthFilter(7)});
        etExtC.setInputType(InputType.TYPE_CLASS_TEXT);
        etExtC.setKeyListener(DigitsKeyListener.getInstance("0123456789QWERTYUIOPÑLKJHGFDSAZXCVBNMmnbvcxzasdfghjklñpoiuytrewq -"));
        etExtC.setTypeface(tipo);
        etIntC=(EditText)findViewById(R.id.etIntC);
        etIntC.setFilters(new InputFilter[]{new InputFilter.AllCaps(),new InputFilter.LengthFilter(7)});
        etIntC.setInputType(InputType.TYPE_CLASS_TEXT);
        etIntC.setTypeface(tipo);
        etCelC=(EditText)findViewById(R.id.etNoCelC);
        etCelC.setFilters(new InputFilter[]{new InputFilter.AllCaps(),new InputFilter.LengthFilter(10)});
        etCelC.setInputType(InputType.TYPE_CLASS_PHONE);
        etCelC.setTypeface(tipo);
        etConfirmCelC=(EditText)findViewById(R.id.etConfirmCelC);
        etConfirmCelC.setFilters(new InputFilter[]{new InputFilter.AllCaps(),new InputFilter.LengthFilter(10)});
        etConfirmCelC.setInputType(InputType.TYPE_CLASS_PHONE);
        etConfirmCelC.setTypeface(tipo);
        etNombreD=(EditText)findViewById(R.id.etNombreD);
        etNombreD.setFilters(new InputFilter[]{new InputFilter.AllCaps(),new InputFilter.LengthFilter(20)});
        etNombreD.setInputType(InputType.TYPE_CLASS_TEXT);
        etNombreD.setTypeface(tipo);
        etAPD=(EditText)findViewById(R.id.etAPD);
        etAPD.setFilters(new InputFilter[]{new InputFilter.AllCaps(),new InputFilter.LengthFilter(20)});
        etAPD.setInputType(InputType.TYPE_CLASS_TEXT);
        etAPD.setTypeface(tipo);
        etAMD=(EditText)findViewById(R.id.etAMD);
        etAMD.setFilters(new InputFilter[]{new InputFilter.AllCaps(),new InputFilter.LengthFilter(20)});
        etAMD.setInputType(InputType.TYPE_CLASS_TEXT);
        etAMD.setTypeface(tipo);
        etEmailD=(EditText)findViewById(R.id.etEmailD);
        etEmailD.setFilters(new InputFilter[]{new InputFilter.LengthFilter(40)});
        etEmailD.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
        etEmailD.setTypeface(tipo);
        etCelD=(EditText)findViewById(R.id.etNoCelD);
        etCelD.setFilters(new InputFilter[]{new InputFilter.AllCaps(),new InputFilter.LengthFilter(10)});
        etCelD.setInputType(InputType.TYPE_CLASS_PHONE);
        etCelD.setTypeface(tipo);
        etConfirmCelD=(EditText)findViewById(R.id.etConfirmCelD);
        etConfirmCelD.setFilters(new InputFilter[]{new InputFilter.AllCaps(),new InputFilter.LengthFilter(10)});
        etConfirmCelD.setInputType(InputType.TYPE_CLASS_PHONE);
        etConfirmCelD.setTypeface(tipo);

        sProfecion=(Spinner)findViewById(R.id.spinnerProf);
        sNacionalidad=(Spinner)findViewById(R.id.spinnerNacionalidadC);
        sColonia=(Spinner)findViewById(R.id.spinnerColC);

        lbSi=(RadioButton) findViewById(R.id.si);
        lbSi.setTypeface(tipo,Typeface.BOLD);
        lbNo=(RadioButton) findViewById(R.id.no);
        lbNo.setTypeface(tipo,Typeface.BOLD);
        rbHC=(RadioButton) findViewById(R.id.lbHC);
        rbHC.setTypeface(tipo,Typeface.BOLD);
        rbMC=(RadioButton) findViewById(R.id.lbMC);
        rbMC.setTypeface(tipo,Typeface.BOLD);

        cntConductor=(LinearLayout)findViewById(R.id.cntConductor);
        rlNacC=(RelativeLayout) findViewById(R.id.rlNacC);
        rlCPC=(RelativeLayout) findViewById(R.id.rlCPC);
        cntSexC=(RadioGroup) findViewById(R.id.cntSexC);

        lbSi.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    lbSi.setTextColor(Color.parseColor("#FFFFFF"));
                    lbNo.setTextColor(getResources().getColor(R.color.azul_miituo));
                    cntConductor.setVisibility(View.GONE);
                    lbNacC.setVisibility(View.GONE);
                    rlNacC.setVisibility(View.GONE);
                    lbCPC.setVisibility(View.GONE);
                    rlCPC.setVisibility(View.GONE);
                    lbSexC.setVisibility(View.GONE);
                    cntSexC.setVisibility(View.GONE);
                    cpC=cpD;
                    new getCols().execute();
                }
            }
        });
        lbNo.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    lbNo.setTextColor(Color.parseColor("#FFFFFF"));
                    lbSi.setTextColor(getResources().getColor(R.color.azul_miituo));
                    cntConductor.setVisibility(View.VISIBLE);
                    lbNacC.setVisibility(View.VISIBLE);
                    rlNacC.setVisibility(View.VISIBLE);
                    lbCPC.setVisibility(View.VISIBLE);
                    rlCPC.setVisibility(View.VISIBLE);
                    lbSexC.setVisibility(View.VISIBLE);
                    cntSexC.setVisibility(View.VISIBLE);
                    etCPC.setText("");
                    arraySpinnerColonia = new ArrayList<>();//[tokenTipos.size()+1];
                    arraySpinnerColonia.clear();
                    arraySpinnerColonia.add("Seleccione");
                    ContratarActivity.Adaptador adaptador2 = new ContratarActivity.Adaptador(ContratarActivity.this, R.layout.simple_spinner_down,arraySpinnerColonia);
                    sColonia.setAdapter(adaptador2);
                    colC=""+0;
                    colDescC=""+0;
                }
            }
        });
        lbNo.setChecked(true);
        lbSi.setChecked(true);
        rbHC.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    rbHC.setTextColor(Color.parseColor("#FFFFFF"));
                    rbMC.setTextColor(getResources().getColor(R.color.azul_miituo));
                    sexC="2";
                }
            }
        });
        rbMC.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    rbMC.setTextColor(Color.parseColor("#FFFFFF"));
                    rbHC.setTextColor(getResources().getColor(R.color.azul_miituo));
                    sexC="1";
                }
            }
        });
        rbMC.setChecked(true);
        rbHC.setChecked(true);
        etRFCC.setOnFocusChangeListener(new View.OnFocusChangeListener(){

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                    new getRFC().execute();
                }
            }
        });
        lbToAuto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validaToAuto();
            }
        });
    }

    private void init2(){
        lbSerieAuto=(TextView)findViewById(R.id.lbSerieAuto);
        lbSerieAuto.setTypeface(tipo);
        lbColorAuto=(TextView)findViewById(R.id.lbColorAuto);
        lbColorAuto.setTypeface(tipo);
        lbPlacasAuto=(TextView)findViewById(R.id.lbPlacasAuto);
        lbPlacasAuto.setTypeface(tipo);
        lbToValidar=(TextView)findViewById(R.id.lbToValidar);
        lbToValidar.setTypeface(tipo);
        etSerieAuto=(EditText)findViewById(R.id.etSerieAuto);
        etSerieAuto.setFilters(new InputFilter[]{new InputFilter.AllCaps(),new InputFilter.LengthFilter(17)});
        etSerieAuto.setInputType(InputType.TYPE_CLASS_TEXT);
        etSerieAuto.setTypeface(tipo);
        etColorAuto=(EditText)findViewById(R.id.etColorAuto);
        etColorAuto.setFilters(new InputFilter[]{new InputFilter.AllCaps(),new InputFilter.LengthFilter(30)});
        etColorAuto.setInputType(InputType.TYPE_CLASS_TEXT);
        etColorAuto.setTypeface(tipo);
        etPlacasAuto=(EditText)findViewById(R.id.etPlacasAuto);
        etPlacasAuto.setFilters(new InputFilter[]{new InputFilter.AllCaps(),new InputFilter.LengthFilter(10)});
        etPlacasAuto.setInputType(InputType.TYPE_CLASS_TEXT);
        etPlacasAuto.setTypeface(tipo);
        lbToValidar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validaToValidacion();
            }
        });
    }

    AlertDialog alerta;
    private void init3(){
        cntAviso=(RelativeLayout)findViewById(R.id.cntAviso);
        lbToken=(TextView)findViewById(R.id.lbToken);
        lbToken.setTypeface(tipo);
        lbToken2=(TextView)findViewById(R.id.lbToken2);
        lbToken2.setTypeface(tipo);
        lbCelEdit=(TextView)findViewById(R.id.lbCelEdit);
        lbCelEdit.setTypeface(tipo);
        lbResendToken=(TextView)findViewById(R.id.lbReSendToken);
        lbResendToken.setTypeface(tipo);
        lbToPago=(TextView)findViewById(R.id.lbToPago);
        lbToPago.setTypeface(tipo);
        etToken=(EditText)findViewById(R.id.etToken);
        etToken.setFilters(new InputFilter[]{new InputFilter.AllCaps(),new InputFilter.LengthFilter(6)});
        etToken.setInputType(InputType.TYPE_CLASS_TEXT);
        etToken.setTypeface(tipo);

        lbtitulo=(TextView)findViewById(R.id.lbTitulo);
        lbtitulo.setTypeface(tipo);
        lb1=(TextView)findViewById(R.id.lb1);
        lb1.setTypeface(tipo);
        lb2=(TextView)findViewById(R.id.lb2);
        lb2.setTypeface(tipo);
        lb3=(TextView)findViewById(R.id.lb3);
        lb3.setTypeface(tipo);
        lb4=(TextView)findViewById(R.id.lb4);
        lb4.setTypeface(tipo);
        lbAceptar=(TextView)findViewById(R.id.lbAceptar);
        lbAceptar.setTypeface(tipo);
        lbtitulo.setText(Html.fromHtml("<p>Al introducir el código de verificación <b>miituo</b> aceptas que estás de acuerdo en:</p>"));
        lb1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String urlString="https://miituo.com/Terminos/getPDF?Url=C%3A%5CmiituoFTP%5CTerminos%20y%20condiciones%20miituo.pdf";
                Intent intent=new Intent(Intent.ACTION_VIEW);
                intent.setDataAndType(Uri.parse("http://docs.google.com/gview?embedded=true&url=" + urlString), "text/html");
                startActivity(intent);
            }
        });
        lb2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String urlString="https://www.miituo.com/Terminos/getPDF?Url=c%3A%5CmiituoFTP%5CCondicionesGeneralesSeguro.pdf";
                Intent intent=new Intent(Intent.ACTION_VIEW);
                intent.setDataAndType(Uri.parse("http://docs.google.com/gview?embedded=true&url=" + urlString), "text/html");
                startActivity(intent);
            }
        });
        lbAceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scroll.setVisibility(View.VISIBLE);
                cntAviso.setVisibility(View.GONE);
            }
        });

        lbCelEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Dialog dialog = new Dialog(ContratarActivity.this);
                dialog.setCancelable(false);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                dialog.setContentView(R.layout.alert_cel_edit);
                final EditText input = dialog.findViewById(R.id.celEditAlert);
                final EditText input2 = dialog.findViewById(R.id.recelEditAlert);
                final TextView cancelar=dialog.findViewById(R.id.cancelAlert);
                final TextView aceptar=dialog.findViewById(R.id.aceptarAlert);
                cancelar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                aceptar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String cel=input.getText().toString();
                        String reCel=input2.getText().toString();
                        if(cel==null || cel.length()!=10){
                            Toast.makeText(ContratarActivity.this,"Verifica tu celular",Toast.LENGTH_LONG).show();
                            return;
                        }
                        if(reCel==null || reCel.length()!=10 || !reCel.equalsIgnoreCase(cel)){
                            Toast.makeText(ContratarActivity.this,"Verifica tu confirmación de celular",Toast.LENGTH_LONG).show();
                            return;
                        }
                        etCelC.setText(cel);
                        etConfirmCelC.setText(reCel);
                        celC=cel;
                        reCelC=reCel;
                        saveData();
                        dialog.dismiss();
                        new tokenRequest(true).execute();
                    }
                });

                dialog.show();
            }
        });
        lbResendToken.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new tokenRequest(true).execute();
            }
        });
        lbToPago.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validaToPago();
            }
        });
    }

    private void init4(){
        wvPago=(WebView)findViewById(R.id.wvPago);
        wvPago.setVerticalScrollBarEnabled(true);
        wvPago.setHorizontalScrollBarEnabled(true);
        cntTarjetas=(LinearLayout)findViewById(R.id.cntTarjetas);
        cntPromo=(LinearLayout)findViewById(R.id.cntPromo);
        cntAmex=(LinearLayout)findViewById(R.id.cntAmex);
        cntPaypal=(LinearLayout)findViewById(R.id.cntPaypal);

        lbHasCupon=(TextView)findViewById(R.id.lbHasCupon);
        lbHasCupon.setTypeface(tipo,Typeface.BOLD);
        lbValidaCodigo=(TextView)findViewById(R.id.lbValidaCodigo);
        lbValidaCodigo.setTypeface(tipo,Typeface.BOLD);
        etCupon=(EditText)findViewById(R.id.etCupon);
        etCupon.setTypeface(tipo);
        lbPromo1=(TextView)findViewById(R.id.lbPromo1);
        lbPromo1.setTypeface(tipo,Typeface.BOLD);
        lbPromo2=(TextView)findViewById(R.id.lbPromo2);
        lbPromo2.setTypeface(tipo);
        lbPromo3=(TextView)findViewById(R.id.lbPromo3);
        lbPromo3.setTypeface(tipo,Typeface.BOLD);
        pintaCupon();
        if(SyncActivity.kms>0){
            cntPromo.setVisibility(View.VISIBLE);
            lbPromo1.setText(SyncActivity.kms+" Km");
            lbPromo3.setText(SyncActivity.codigoCupon);
        }

        lbValidaCodigo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String cuponT=etCupon.getText().toString();
                if(!isDatoValido(cuponT)){
                    Toast.makeText(ContratarActivity.this,"Verifica el cupón ingresado",Toast.LENGTH_LONG).show();
                    return;
                }
                new ValidatingCuponSync(ContratarActivity.this, cuponT,idCotizacion, new SimpleCallBack() {
                    @Override
                    public void run(boolean status, String res) {
                        if(status) {
                            final AlertDialog.Builder builder = new AlertDialog.Builder(ContratarActivity.this);
                            if(res.equalsIgnoreCase("true")){
                            cupon = cuponT;
//                            lbValidaCodigo.setEnabled(false);
                            builder.setMessage("Cupón válido.");
                            }
                            else{
                                builder.setMessage("Lo sentimos pero tu código no es válido, ingrésalo nuevamente o intenta con otro");
                            }
                            builder.setCancelable(false);
                            builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    alertToClose.dismiss();
                                }
                            });
                            alertToClose = builder.create();
                            runOnUiThread(new Runnable() {
                                public void run() {
                                    if(!alertToClose.isShowing()) {
                                        alertToClose.show();
                                    }
                                }
                            });
                        }
                        else {
                            Toast.makeText(ContratarActivity.this,"Error: "+res,Toast.LENGTH_LONG).show();
                        }
                    }
                }).execute();
            }
        });

        wvPago.setWebViewClient(new WebViewClient());
        wvPago.getSettings().setJavaScriptEnabled(true);
//        wvPago.loadUrl("https://apiqas2019.miituo.com/miituopayment/RedirectPayments?Url=KacetMZ2hX6a+jTcfv6wsAFy02IwXHGoKqhOWarawVunL+hjWmjonPwBK/Iu2xZFxl78i59ypO8gjz2pI4+DtYuaOhOo+UOW2CWdJkp0aFjIh6xOY/lO1MPee8zcKLuJ/pCtCT1+yQ0UTCjW27vGhMCCF7tav9nDdyF9pxGDJS4xscwN2tEFe3Mam5FzU6yPHeyzrGdirTg+1+ryllMZhQ==&QuotationId=1331");
//        wvPago.loadUrl("https://miituo.com/api/miituopayment/RedirectPayments?Url=MMEb5/z2DSfmroP8r0pWIkiZbhF5UGL7WWkCcho14na04k3y4o2e5ArpEOm5CC0iaOXYKAAyoB2FQxhR04DyLg==&QuotationId=79759");
        cntTarjetas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(cntTarjetas.getBackground()!=getResources().getDrawable(R.drawable.blueborder)) {
                    cntTarjetas.setBackground(getResources().getDrawable(R.drawable.blueborder));
                    cntAmex.setBackground(getResources().getDrawable(R.drawable.border));
                    cntPaypal.setBackground(getResources().getDrawable(R.drawable.border));
                    tipoPago=3;
                    showUrl();
                }
            }
        });
        cntAmex.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(cntAmex.getBackground()!=getResources().getDrawable(R.drawable.blueborder)) {
                    cntAmex.setBackground(getResources().getDrawable(R.drawable.blueborder));
                    cntTarjetas.setBackground(getResources().getDrawable(R.drawable.border));
                    cntPaypal.setBackground(getResources().getDrawable(R.drawable.border));
                    tipoPago=1;
                    showUrl();
                }
            }
        });
        cntPaypal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(cntPaypal.getBackground()!=getResources().getDrawable(R.drawable.blueborder)) {
                    cntPaypal.setBackground(getResources().getDrawable(R.drawable.blueborder));
                    cntTarjetas.setBackground(getResources().getDrawable(R.drawable.border));
                    cntAmex.setBackground(getResources().getDrawable(R.drawable.border));
                    ultimoTipoPago=tipoPago;
                    tipoPago=2;
                    showUrlPaypal();
                }
            }
        });
//        ArrayList<View> views = new ArrayList<View>();
//        for(int x = 0; x < cntPagar.getChildCount(); x++) {
//            views.add(cntPagar.getChildAt(x));
//        }
//        cntPagar.removeAllViews();
//        for(int x = views.size() - 1; x >= 0; x--) {
//            cntPagar.addView(views.get(x));
//        }
    }
    private void pintaCupon(){
        new GetSync(this, "Cupon/getCouponFromLanding/Cotizar", new SimpleCallBack() {
            @Override
            public void run(boolean status, String res) {
                if(status){
                    try{
                        SyncActivity.kms=0;
                        SyncActivity.codigoCupon="";
                        JSONObject cupon=new JSONObject(res);
                        String urlImg=cupon.getJSONArray("Vigencias").getJSONObject(0).getString("URL_ImagenMobile");
                        String txtVig=cupon.getJSONArray("Vigencias").getJSONObject(0).getString("TextoVigenciaCupon");
                        SyncActivity.kms=cupon.getInt("Kms");
                        SyncActivity.codigoCupon=cupon.getString("CodigoCupon");
                        cntPromo.setVisibility(View.VISIBLE);
                        lbPromo1.setText(SyncActivity.kms+" Km");
                        lbPromo3.setText(SyncActivity.codigoCupon);
                    }
                    catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }
        }).execute();
    }

    private void validaToAuto(){

        nombreD=etNombreD.getText().toString();
        apD=etAPD.getText().toString();
        amD=etAMD.getText().toString();
        mailD=etEmailD.getText().toString();
        celD=etCelD.getText().toString();
        reCelD=etConfirmCelD.getText().toString();

        nombreC=etNombreC.getText().toString();
        apC=etAPC.getText().toString();
        amC=etAMC.getText().toString();
        mailC=etEmailC.getText().toString();
        rfcC=etRFCC.getText().toString();
        homoclaveC=etHomoC.getText().toString();
        nacionalidadC="MEXICANA";
        calleC=etCalleC.getText().toString();
        extC=etExtC.getText().toString();
        intC=etIntC.getText().toString();
        celC=etCelC.getText().toString();
        reCelC=etConfirmCelC.getText().toString();
        if(intC.length()==1){intC="00"+intC;}
        if(intC.length()==2){intC="0"+intC;}

        if(lbNo.isChecked()){
            cpC=etCPC.getText().toString();
            if(!isDatoValido(nombreD)){
                Toast.makeText(this,"Verifica el nombre del conductor",Toast.LENGTH_LONG).show();
                return;
            }
            else if(!isDatoValido(apD)){
                Toast.makeText(this,"Verifica el apellido paterno del conductor",Toast.LENGTH_LONG).show();
                return;
            }
            else if(!isDatoValido(amD)){
                Toast.makeText(this,"Verifica el apellido materno del conductor",Toast.LENGTH_LONG).show();
                return;
            }
            else if(!isDatoValido(mailD) || !isValidEmail(mailD)){
                Toast.makeText(this,"Verifica el email del conductor",Toast.LENGTH_LONG).show();
                return;
            }
            else if(!isDatoValido(celD) || celD.length()!=10){
                Toast.makeText(this,"Verifica el celular del conductor",Toast.LENGTH_LONG).show();
                return;
            }
            else if(!isDatoValido(reCelD) || !celD.equalsIgnoreCase(reCelD)){
                Toast.makeText(this,"Verifica la confirmacion del celular del conductor",Toast.LENGTH_LONG).show();
                return;
            }
            else if(!isDatoValido(cpC) || cpC.length()<5){
                Toast.makeText(this,"Verifica el CP del contratante",Toast.LENGTH_LONG).show();
                return;
            }
        }
        else {
            birthdayC=birthdayD;
            formatNacC=formatNacD;
            cpC=cpD;
            sexC=sexD;
        }
        if(!isDatoValido(nombreC)){
            Toast.makeText(this,"Verifica el nombre del contratante",Toast.LENGTH_LONG).show();
            return;
        }
        else if(!isDatoValido(apC)){
            Toast.makeText(this,"Verifica el apellido paterno del contratante",Toast.LENGTH_LONG).show();
            return;
        }
        else if(!isDatoValido(amC)){
            Toast.makeText(this,"Verifica el apellido materno del contratante",Toast.LENGTH_LONG).show();
            return;
        }
        else if(!isDatoValido(mailC) || !isValidEmail(mailC)){
            Toast.makeText(this,"Verifica el email del contratante",Toast.LENGTH_LONG).show();
            return;
        }
        else if(!isDatoValido(rfcC) || rfcC.length()<10 || !isRFCValid){
            Toast.makeText(this,"Verifica el RFC del contratante",Toast.LENGTH_LONG).show();
            return;
        }
        else if(profesionIdC.equalsIgnoreCase("0")){
            Toast.makeText(this,"Verifica la profesión del contratante",Toast.LENGTH_LONG).show();
            return;
        }
        else if(colC.equalsIgnoreCase("0")){
            Toast.makeText(this,"Verifica la colonia del contratante",Toast.LENGTH_LONG).show();
            return;
        }
        else if(delC.equalsIgnoreCase("0")){
            Toast.makeText(this,"Verifica la delegación del contratante",Toast.LENGTH_LONG).show();
            return;
        }
        else if(edoC.equalsIgnoreCase("0")){
            Toast.makeText(this,"Verifica el estado del contratante",Toast.LENGTH_LONG).show();
            return;
        }
        else if(!isDatoValido(calleC)){
            Toast.makeText(this,"Verifica la calle del contratante",Toast.LENGTH_LONG).show();
            return;
        }
        else if(!isDatoValido(extC)){
            Toast.makeText(this,"Verifica el no. exterior del contratante",Toast.LENGTH_LONG).show();
            return;
        }
        else if(!isDatoValido(celC) || celC.length()!=10){
            Toast.makeText(this,"Verifica el celular del contratante",Toast.LENGTH_LONG).show();
            return;
        }
        else if(!isDatoValido(reCelC) || !celC.equalsIgnoreCase(reCelC)){
            Toast.makeText(this,"Verifica la confirmacion del celular del contratante",Toast.LENGTH_LONG).show();
            return;
        }
//        new CPChecker().execute();
        Evento.eventRecord(this,Evento.CONTRATANTE);
        saveData();
        nextStep(lbToAuto,false);
    }

    private boolean isValidEmail(String s){
        Pattern sPattern = Pattern.compile("^[a-zA-Z0-9_.+-]+@+[a-zA-Z0-9-]+.+[a-zA-Z0-9]{2,4}");
        return sPattern.matcher(s).matches();
    }
    private void validaToValidacion(){

        serieVehicle=etSerieAuto.getText().toString();
        colorVehicle=etColorAuto.getText().toString();
        placasVehicle=etPlacasAuto.getText().toString();

        if(!isDatoValido(serieVehicle)){
            Toast.makeText(this,"Verifica el no. de serie del auto",Toast.LENGTH_LONG).show();
            return;
        }
        else if(!isDatoValido(colorVehicle)){
            Toast.makeText(this,"Verifica el color del auto",Toast.LENGTH_LONG).show();
            return;
        }
        else if(!isDatoValido(placasVehicle)){
            Toast.makeText(this,"Verifica las placas del auto",Toast.LENGTH_LONG).show();
            return;
        }
        new checkSerieAuto().execute();
    }

    private void validaToPago(){

        token=etToken.getText().toString();

        if(!isDatoValido(token) || token.length()!=5){
            Toast.makeText(this,"Verifica el código ingresado",Toast.LENGTH_LONG).show();
            return;
        }
        new tokenVerifier().execute();
    }

    private boolean isDatoValido(String dato){
        if(dato==null || dato.equalsIgnoreCase("")){
            return false;
        }
            return true;
    }

    private void saveData(){
        new ContratarDelegate.saveInsurance(delegate.contractModelMaker(), false, new SimpleCallBack() {
            @Override
            public void run(boolean status, String res) {
                print("status: "+status+" resp: "+res);
            }
        }).execute();
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

                birthdayC = yearString+"/"+mesFormateado+"/"+diaFormateado;
                formatNacC=yearString+"-"+mesFormateado+"-"+diaFormateado;
                etNacC.setText(diaFormateado+"/"+mesFormateado+"/"+yearString);
                etRFCC.setText("");
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

    public void backStep(final int v, final boolean isDoble){
        TranslateAnimation anim = new TranslateAnimation(-1500,0,0,0);
        anim.setDuration(500);
        anim.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {
                if(v==1) {
                    cntDatosConductor.setVisibility(View.VISIBLE);
                }
                else if(v==2) {
                    cntDatosAuto.setVisibility(View.VISIBLE);
                }
                else if(v==3) {
                    cntValidar.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                if(v==1) {
                    cntDatosConductor.setVisibility(View.VISIBLE);
                    cntDatosAuto.setVisibility(View.GONE);
                    pageIndex=0;
                    etRFCC.setText(rfcC);
                }
                else if(v==2) {
                    cntDatosAuto.setVisibility(View.VISIBLE);
                    cntValidar.setVisibility(View.GONE);
                    pageIndex=1;
                }
                else if(v==3) {
                    cntValidar.setVisibility(View.VISIBLE);
                    cntPagar.setVisibility(View.GONE);
                    pageIndex=2;
                }
                if(isDoble){
                    backStep(pageIndex,false);
                }
            }
        });
        if(v==1) {
            cntDatosConductor.startAnimation(anim);
        }
        else if(v==2) {
            cntDatosAuto.startAnimation(anim);
        }
        else if(v==3) {
            cntValidar.startAnimation(anim);
        }
    }

    public void nextStep(final View v, final boolean isDoble){
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
                if(v==lbToAuto) {
                    cntDatosAuto.setVisibility(View.VISIBLE);
                }
                else if(v==lbToValidar) {
                    cntValidar.setVisibility(View.VISIBLE);
                }
                else if(v==lbToPago) {
                    cntPagar.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onAnimationEnd(Animation animation) {

                scroll.fullScroll(0);
                if(v==lbToAuto) {
                    cntDatosConductor.setVisibility(View.GONE);
                    cntDatosAuto.setVisibility(View.VISIBLE);
                    pageIndex=1;
                    auto.setImageDrawable(getResources().getDrawable(R.drawable.icon_1_1));
                    lbAuto.setTextColor(getResources().getColor(R.color.azul_miituo));
                }
                else if(v==lbToValidar) {
                    cntDatosAuto.setVisibility(View.GONE);
                    cntValidar.setVisibility(View.VISIBLE);
                    scroll.setVisibility(View.GONE);
                    cntAviso.setVisibility(View.VISIBLE);
                    pageIndex=2;
                    auto.setImageDrawable(getResources().getDrawable(R.drawable.icon_1_1));
                    validacion.setImageDrawable(getResources().getDrawable(R.drawable.con_2_2));
                    lbValidacion.setTextColor(getResources().getColor(R.color.azul_miituo));
                }
                else if(v==lbToPago) {
                    cntValidar.setVisibility(View.GONE);
                    cntPagar.setVisibility(View.VISIBLE);
                    pageIndex=3;
                    personas.setImageDrawable(getResources().getDrawable(R.drawable.con_1));
                    auto.setImageDrawable(getResources().getDrawable(R.drawable.icon_1));
                    validacion.setImageDrawable(getResources().getDrawable(R.drawable.con_2));
                    ipago.setImageDrawable(getResources().getDrawable(R.drawable.con_3_3));
                    int color=lbPago.getCurrentTextColor();
                    lbValidacion.setTextColor(color);
                    lbAuto.setTextColor(color);
                    lbConductor.setTextColor(color);
                    lbPago.setTextColor(getResources().getColor(R.color.azul_miituo));
                    Alarma a=new Alarma();
                    a.setAlarm(ContratarActivity.this,referenciaPago,"1",20000, ContratarActivity.this);
                }
                if(isDoble){
                    new checkSerieAuto().execute();
                }

            }
        });
        if(v==lbToAuto) {
            cntDatosConductor.startAnimation(anim);
        }
        else if(v==lbToValidar) {
            cntDatosAuto.startAnimation(anim);
        }
        else if(v==lbToPago) {
            cntValidar.startAnimation(anim);
        }
    }

    public void showUrlPaypal(){
        new ContratarDelegate.getReference(idCotizacion,pagoUnico, 2, new SimpleCallBack() {
            @Override
            public void run(boolean status, String res) {
                print(res);
                if(status) {
                    try {
                        JSONObject json = new JSONObject(res);
                        String url = json.getString("Url");
                        referenciaPago=json.getString("Reference");
                        String response=json.getString("Response");
                        String msg=json.getString("Description");
                        pago=json.getJSONObject("PaymentReference").getString("ObjectPay");
//                        pago="{\"Referencia\":\""+referenciaPago+"\",\"Response\":\"\",\"Aut\":\"\",\"Error\":\"\",\"ccName\":\"\",\"ccNum\":\"\",\"Amount\":\"\",\"Type\":\"\"}";
//                        saveData();
                            Intent intent=new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            intent.setPackage("com.android.chrome");
                            try {
                                startActivity(intent);
                            } catch (ActivityNotFoundException ex) {
                                // Chrome browser presumably not installed so allow user to choose instead
                                intent.setPackage(null);
                                startActivity(intent);
                            }
                    }
                    catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }
        }).execute();
    }

    public void showUrl(){
//        delegate=new ContratarDelegate(this,this);
//        delegate.cambiaBandera();
        new ContratarDelegate.getReference(idCotizacion,pagoUnico, tipoPago, new SimpleCallBack() {
            @Override
            public void run(boolean status, String res) {
                print(res);
                if(status) {
                    try {
                        JSONObject json = new JSONObject(res);
                        String url = json.getString("Url");
                        referenciaPago=json.getString("Reference");
                        String response=json.getString("Response");
                        String msg=json.getString("Description");
                        pago=json.getJSONObject("PaymentReference").getString("ObjectPay");
//                        pago="{\"Referencia\":\""+referenciaPago+"\",\"Response\":\"\",\"Aut\":\"\",\"Error\":\"\",\"ccName\":\"\",\"ccNum\":\"\",\"Amount\":\"\",\"Type\":\"\"}";
//                        saveData();
                        if(response.equalsIgnoreCase("ok") || response.equalsIgnoreCase("success")) {
                            wvPago.loadUrl(url);
                            wvPago.setWebViewClient(new WebViewClient());
                            wvPago.getSettings().setJavaScriptEnabled(true);
                            if(pageIndex==2) {
                                nextStep(lbToPago, false);
                            }
                        }
                        else {
                            Toast.makeText(ContratarActivity.this,"Error al generar referencia, intenta de nuevo: "+msg,Toast.LENGTH_LONG).show();
                        }
                    }
                    catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }
        }).execute();
    }

    public static String carriers="{}";
    public void generateInsurance(){
        new ContratarDelegate.getCarriers(new SimpleCallBack() {
            @Override
            public void run(boolean status, String res) {
                Intent intent = new Intent(ContratarActivity.this, AseguradorasActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.putExtra("cel", celC);
                intent.putExtra("token", token);
                intent.putExtra("idQuotation", idCotizacion);
                startActivity(intent);
            }
        }).execute();
        new ContratarDelegate.generateInsurance(delegate.contractModelMaker(), new SimpleCallBack() {
            @Override
            public void run(boolean status, String res) {
//                Intent intent = new Intent(ContratarActivity.this, AseguradorasActivity.class);
//                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                intent.putExtra("cel", celC);
//                intent.putExtra("token", token);
//                intent.putExtra("idQuotation", idCotizacion);
//                startActivity(intent);
            }
        }).execute();
    }

   public static AlertDialog alertaPago;
    public static boolean ispagoshown=false;
    public static boolean ispagoshown2=false;
    @Override
    public void run(boolean status, final String res) {
            print("en callback true=termino alarma / false ispaysucceded\n==============================\n" +
                    " status: "+status+" res: "+res);
        final Alarma a=new Alarma();
        a.cancelAlarm(ContratarActivity.this);
        if (status) {
            new ContratarDelegate.getIsPaySucceded(this,referenciaPago,""+tipoPago,this).execute();
        }
        else{
            if(res==null){
                a.setAlarm(ContratarActivity.this,referenciaPago,""+tipoPago,3000, ContratarActivity.this);
            }
            else{
                print("ya cayo el pago?: "+res);
                if(res.equalsIgnoreCase("402")){
                                    final AlertDialog.Builder builder = new AlertDialog.Builder(ContratarActivity.this);
                                    builder.setTitle("Error en el pago");
                                    builder.setMessage("Pago denegado.");
                                    builder.setCancelable(true);
                                    builder.setPositiveButton("Reintentar", new DialogInterface.OnClickListener() {
                                       @Override
                                          public void onClick(DialogInterface dialog, int which) {
                                           showUrl();
                                           ispagoshown=false;
                                           a.setAlarm(ContratarActivity.this,res,""+tipoPago,3000,ContratarActivity.this);
                                        }
                                    });
                                    builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
                                        @Override
                                        public void onDismiss(DialogInterface dialog) {
                                            ispagoshown=false;
                                        }
                                    });
                                    runOnUiThread(new Runnable() {
                                        public void run() {
                                            if(!ispagoshown) {
                                                alertaPago = builder.create();
                                                ispagoshown = true;
                                                alertaPago.show();
                                            }
                                       }
                                    });
                }
                else {
                    pago = res;
                    pageIndex = 4;
                    Evento.eventRecord(ContratarActivity.this,Evento.PAGO);
                    new ContratarDelegate.saveInsurance(delegate.contractModelMaker(), true, new SimpleCallBack() {
                        @Override
                        public void run(boolean status, String res) {
                            print("paso?: status:"+status+"  resp: " + res);
                            if (status) {
                                generateInsurance();
                            }
                            else {
                                final AlertDialog.Builder builder = new AlertDialog.Builder(ContratarActivity.this);
                                builder.setTitle("Error al generar poliza");
                                builder.setMessage(res);
                                builder.setCancelable(false);
                                builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        ispagoshown2=false;
                                        Intent intent = new Intent(ContratarActivity.this, AseguradorasActivity.class);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        intent.putExtra("cel", celC);
                                        intent.putExtra("token", token);
                                        intent.putExtra("idQuotation", idCotizacion);
                                        startActivity(intent);
                                    }
                                });
                                runOnUiThread(new Runnable() {
                                    public void run() {
                                        if(!ispagoshown2) {
                                            alertaPago = builder.create();
                                            ispagoshown2 = true;
                                            alertaPago.show();
                                        }
                                    }
                                });
                            }
                        }
                    }).execute();
                }
            }
        }
    }

    public static int intentosP=1;
    //===========================Thread call to fill combo==============================================
    public class getProfs extends AsyncTask<String,Void,Void> {

//        ProgressDialog progress = new ProgressDialog(ContratarActivity.this);
        String ErrorCode,profs;
        int index=0;

        String[] stringArray;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
//            progress.setTitle("Información");
//            progress.setMessage("Recuperando oficios...");
//            progress.setIndeterminate(true);
//            progress.setCancelable(false);
//            progress.show();
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
            if(intentosP<5) {
                intentosP++;
                new getProfs().execute();
            }
//            progress.dismiss();
//            Toast message = Toast.makeText(ContratarActivity.this, ErrorCode, Toast.LENGTH_LONG);
//            message.show();
        }

        @Override
        protected Void doInBackground(String... strings) {

            ApiClientCotiza client = new ApiClientCotiza(ContratarActivity.this);
            try {
                //recupera datos
                profs = client.getDatos("Jobs/");

                arraySpinnerProfecion = new ArrayList<>();//[tokenTipos.size()+1];
                arraySpinnerProfecion.clear();
                arrayProfs=new JSONArray(profs);
                for(int i=0; i<arrayProfs.length();i++){
                    arraySpinnerProfecion.add(arrayProfs.getJSONObject(i).getString("Descripcion"));
                    if(arrayProfs.getJSONObject(i).getString("Descripcion").equalsIgnoreCase("COMERCIANTE")){
                        index=i;
                    }
                }
                intentosP=1;
            } catch (Exception ex) {
                ErrorCode = ex.getMessage();
                this.cancel(true);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            ContratarActivity.Adaptador adaptador1 = new ContratarActivity.Adaptador(ContratarActivity.this, R.layout.simple_spinner_down,arraySpinnerProfecion);
            sProfecion.setAdapter(adaptador1);

            sProfecion.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        try {
                            profesionIdC=String.valueOf(arrayProfs.getJSONObject(i-1).getInt("Id"));
                            profesionDescC=String.valueOf(arrayProfs.getJSONObject(i-1).getString("Descripcion"));
                        }
                        catch (Exception e){
                            e.printStackTrace();
                        }
                }
                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });
            sProfecion.setSelection(index);
//            progress.dismiss();
        }
    }
    public static int intentosC=1;
    //===========================Thread call to fill combo==============================================
    public class getCols extends AsyncTask<String,Void,Void> {

//        ProgressDialog progress = new ProgressDialog(ContratarActivity.this);
        String ErrorCode,cols;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
//            progress.setTitle("Información");
//            progress.setMessage("Recuperando Colonias...");
//            progress.setIndeterminate(true);
//            progress.setCancelable(false);
//            progress.show();
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
            delC="0";
            etDelC.setText("");
            etEdoC.setText("");
            edoC="0";
            if(intentosC<5) {
                intentosC++;
                new getCols().execute();
            }
            else if(ErrorCode.equalsIgnoreCase("No encontramos colonias para este código postal.")){
            Toast message = Toast.makeText(ContratarActivity.this, ErrorCode, Toast.LENGTH_LONG);
            message.show();
                arraySpinnerColonia = new ArrayList<>();//[tokenTipos.size()+1];
                arraySpinnerColonia.clear();
                arraySpinnerColonia.add("Seleccione");
                ContratarActivity.Adaptador adaptador2 = new ContratarActivity.Adaptador(ContratarActivity.this, R.layout.simple_spinner_down,arraySpinnerColonia);
                sColonia.setAdapter(adaptador2);
                colC=""+0;
                colDescC=""+0;
            }
//            progress.dismiss();
//            Toast message = Toast.makeText(ContratarActivity.this, ErrorCode, Toast.LENGTH_LONG);
//            message.show();
        }

        @Override
        protected Void doInBackground(String... strings) {

            ApiClientCotiza client = new ApiClientCotiza(ContratarActivity.this);
            try {
                //recupera datos
                cols = client.getDatos("Neighborhood/ZipCode/"+cpC);

                arraySpinnerColonia = new ArrayList<>();//[tokenTipos.size()+1];
                arraySpinnerColonia.clear();
                arraySpinnerColonia.add("Seleccione");
                arrayCols=new JSONArray(cols);
                for(int i=0; i<arrayCols.length();i++){
                    arraySpinnerColonia.add(arrayCols.getJSONObject(i).getString("Description"));
                }
                intentosC=1;

            } catch (Exception ex) {
                ErrorCode = ex.getMessage();
                this.cancel(true);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            ContratarActivity.Adaptador adaptador2 = new ContratarActivity.Adaptador(ContratarActivity.this, R.layout.simple_spinner_down,arraySpinnerColonia);
            sColonia.setAdapter(adaptador2);

            sColonia.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    if(i==0) {
                        colC=""+0;
                        colDescC=""+0;
                        return;
                    }
                    else{
                        try {
                            colC=String.valueOf(arrayCols.getJSONObject(i-1).getInt("Id"));
                            colDescC=arrayCols.getJSONObject(i-1).getString("Description");
                            new getDel().execute(new String[]{String.valueOf(arrayCols.getJSONObject(i-1).getInt("Id"))});
                        }
                        catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                }
                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });
//            progress.dismiss();
        }
    }

    //===========================Thread call to fill combo==============================================
    public class getDel extends AsyncTask<String,Void,Void> {

        ProgressDialog progress = new ProgressDialog(ContratarActivity.this);
        String ErrorCode,dels;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progress.setTitle("Información");
            progress.setMessage("Recuperando delegaciones...");
            progress.setIndeterminate(true);
            progress.setCancelable(false);
            progress.show();
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
            progress.dismiss();
            Toast message = Toast.makeText(ContratarActivity.this, ErrorCode, Toast.LENGTH_LONG);
            message.show();
        }

        @Override
        protected Void doInBackground(String... strings) {

            ApiClientCotiza client = new ApiClientCotiza(ContratarActivity.this);
            try {
                //recupera datos
                dels = client.getDatos("Municipality/"+strings[0]);
                arrayDels=new JSONArray(dels);
            } catch (Exception ex) {
                ErrorCode = ex.getMessage();
                this.cancel(true);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            try {
                etDelC.setText(arrayDels.getJSONObject(0).getString("Description"));
                delC=String.valueOf(arrayDels.getJSONObject(0).getInt("Id"));
                new getEdo().execute(new String[]{String.valueOf(arrayDels.getJSONObject(0).getInt("Id"))});
            }
            catch (Exception e){
                e.printStackTrace();
            }
            progress.dismiss();
        }
    }

    //===========================Thread call to fill combo==============================================
    public class getEdo extends AsyncTask<String,Void,Void> {

        ProgressDialog progress = new ProgressDialog(ContratarActivity.this);
        String ErrorCode,edo;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progress.setTitle("Información");
            progress.setMessage("Recuperando estado...");
            progress.setIndeterminate(true);
            progress.setCancelable(false);
            progress.show();
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
            progress.dismiss();
            Toast message = Toast.makeText(ContratarActivity.this, ErrorCode, Toast.LENGTH_LONG);
            message.show();
        }

        @Override
        protected Void doInBackground(String... strings) {

            ApiClientCotiza client = new ApiClientCotiza(ContratarActivity.this);
            try {
                //recupera datos
                edo = client.getDatos("State/"+strings[0]);
                arrayEdo=new JSONArray(edo);
            } catch (Exception ex) {
                ErrorCode = ex.getMessage();
                this.cancel(true);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            try {
                etEdoC.setText(arrayEdo.getJSONObject(0).getString("Description"));
                edoC=String.valueOf(arrayEdo.getJSONObject(0).getInt("Id"));
            }
            catch (Exception e){
                e.printStackTrace();
            }
            progress.dismiss();
        }
    }

    //===========================Thread call to fill combo==============================================
    public class getRFC extends AsyncTask<String,Void,Void> {

//        ProgressDialog progress = new ProgressDialog(ContratarActivity.this);
        String ErrorCode,rfc,res;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
//            progress.setTitle("Información");
//            progress.setMessage("Recuperando RFC...");
//            progress.setIndeterminate(true);
//            progress.setCancelable(false);
//            progress.show();
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
//            progress.dismiss();
            Toast message = Toast.makeText(ContratarActivity.this, ErrorCode, Toast.LENGTH_LONG);
            message.show();
        }

        @Override
        protected Void doInBackground(String... strings) {

            ApiClientCotiza client = new ApiClientCotiza(ContratarActivity.this);
            try {
                //recupera datos
                this.rfc = client.getRFC("Contract/generateRFC",etNombreC.getText().toString(),etAPC.getText().toString(),etAMC.getText().toString(),formatNacC);
                rfcC=this.rfc.replaceAll("\"","");
                res=client.rfcVerifier("Contract/ValidateRFC/"+rfcC);

            } catch (Exception ex) {
                ErrorCode = ex.getMessage();
                this.cancel(true);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
//            progress.dismiss();
            try {
                etRFCC.setText(rfcC);
                etHomoC.requestFocus();
                JSONObject j=new JSONObject(res);
                boolean val=j.getBoolean("Validate");
                boolean den=j.getBoolean("Denegate");
                isRFCValid=true;
                if(!val || den){
                    String msg=j.getString("Mensaje");
                    if(msg!=null){
                        Toast.makeText(ContratarActivity.this,"Favor de verificar tu RFC: "+msg,Toast.LENGTH_LONG).show();
                        isRFCValid=false;
                    }
                    else{
                        Toast.makeText(ContratarActivity.this,"Favor de verificar tu RFC",Toast.LENGTH_LONG).show();
                        isRFCValid=false;
                    }
                }
            }
            catch (Exception e){
                e.printStackTrace();
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
            AlertDialog.Builder builder = new AlertDialog.Builder(ContratarActivity.this);
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

            ApiClientCotiza client = new ApiClientCotiza(ContratarActivity.this);
            try {
                //recupera datos
                this.res = client.CPChecker("CP",sexC,vehicleId,birthdayC,cpC);
            } catch (Exception ex) {
                ErrorCode = ex.getMessage();
                this.cancel(true);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            if(res!=null && res.equalsIgnoreCase("true")){
                saveData();
                nextStep(lbToAuto,false);
            }
            else{
                AlertDialog.Builder builder = new AlertDialog.Builder(ContratarActivity.this);
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

    //===========================Thread call to fill combo==============================================
    public class checkSerieAuto extends AsyncTask<String,Void,Void> {

        ProgressDialog progress = new ProgressDialog(ContratarActivity.this);
        String ErrorCode,res,msg,msgError;
        int status=0,errorId=0;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progress.setTitle("Información");
            progress.setMessage("Validando No. de serie...");
            progress.setIndeterminate(true);
            progress.setCancelable(false);
            progress.show();
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
            progress.dismiss();
            Toast message = Toast.makeText(ContratarActivity.this, ErrorCode, Toast.LENGTH_LONG);
            message.show();
        }

        @Override
        protected Void doInBackground(String... strings) {

            ApiClientCotiza client = new ApiClientCotiza(ContratarActivity.this);
            try {
                //recupera datos
                this.res = client.getDatos("VIN/VINFormat/VIN/"+serieVehicle+"/IdQuotation/"+idCotizacion);  // para validacion o no del vin
//                this.res = client.checkVIN("VIN",serieVehicle);  // original
//                JSONObject json = new JSONObject(res);
//                status=json.getInt("Status");
//                msg=json.getString("ValidationMessage");
//                errorId=json.getInt("ErrorId");
//                msgError=json.getString("DescriptionError");
            } catch (Exception ex) {
                ErrorCode = ex.getMessage();
                this.cancel(true);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
//            if(status==1 && msg.equalsIgnoreCase("Correcta")){
            if(res.equalsIgnoreCase("true")){
                Evento.eventRecord(ContratarActivity.this,Evento.AUTO);
                saveData();
                new tokenRequest(false).execute();
            }
            else {
                ErrorCode="Tu VIN no es valido, favor de ingresarlo de nuevo.";
                this.cancel(true);
                onCancelled();
            }
            progress.dismiss();
        }
    }

    //===========================Thread call to fill combo==============================================
    public class tokenVerifier extends AsyncTask<String,Void,Void> {

        ProgressDialog progress = new ProgressDialog(ContratarActivity.this);
        String ErrorCode,res;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progress.setTitle("Información");
            progress.setMessage("Validando código...");
            progress.setIndeterminate(true);
            progress.setCancelable(false);
            progress.show();
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
            progress.dismiss();
            ContratarActivity.this.runOnUiThread(new Runnable() {
                public void run() {
                    Toast.makeText(ContratarActivity.this, ErrorCode, Toast.LENGTH_LONG).show();
                }
            });
        }

        @Override
        protected Void doInBackground(String... strings) {

            ApiClientCotiza client = new ApiClientCotiza(ContratarActivity.this);
            try {
                //recupera datos
                this.res = client.tokenVerifier("TemporalToken/ValidateTemporalToken/"+token);
            } catch (Exception ex) {
                ErrorCode = ex.getMessage();
                this.cancel(true);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if(res.equalsIgnoreCase("true")){
                Evento.eventRecord(ContratarActivity.this,Evento.TOKEN);
                showUrl();
            }
            else {
                ErrorCode="Código invalido";
                this.cancel(true);
                onCancelled();
            }
            progress.dismiss();
        }
    }

    //===========================Thread call to fill combo==============================================
    public class tokenRequest extends AsyncTask<String,Void,Void> {

        ProgressDialog progress = new ProgressDialog(ContratarActivity.this);
        String ErrorCode,res,token="";
        boolean isResend=false;
        public tokenRequest(boolean isResend){
            this.isResend=isResend;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progress.setTitle("Información");
            progress.setMessage("Generando token...");
            progress.setIndeterminate(true);
            progress.setCancelable(false);
            progress.show();
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
            progress.dismiss();
            Toast message = Toast.makeText(ContratarActivity.this, ErrorCode, Toast.LENGTH_LONG);
            message.show();
        }

        @Override
        protected Void doInBackground(String... strings) {

            ApiClientCotiza client = new ApiClientCotiza(ContratarActivity.this);
            try {
                //recupera datos
                this.res = client.tokenRequest("TemporalToken/GetTemporalTokenContract/"+celC+"/contratacion/"+mailC);
//                this.res = client.tokenRequest("TemporalToken/Quotation/"+idCotizacion+
//                        "/GetTemporalTokenContract/"+celC+"/contratacion/"+mailC);
                JSONArray json=new JSONArray(res);
                token=json.getString(0);
            } catch (Exception ex) {
                ErrorCode = ex.getMessage();
                this.cancel(true);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if(token!=null && token.length()==5){
                if(!isResend) {
                    nextStep(lbToValidar,false);
                }
                else{
                    Toast.makeText(ContratarActivity.this,"Código reenviado.",Toast.LENGTH_LONG).show();
                }
            }
            else {
                ErrorCode="Error de conexión, vuelve a intentar";
                this.cancel(true);
            }
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

                return true;
        }

        @Override
        public View getDropDownView(int position, View convertView, ViewGroup parent) {
            View view = super.getDropDownView(position, convertView, parent);
            TextView tv = (TextView) view;
//            if(position == 0){
//                // Set the hint text color gray
//                tv.setTextColor(Color.GRAY);
//            }
//            else {
                tv.setTextColor(Color.BLACK);
//            }
            return view;
        }
    }
}
