package miituo.com.miituo.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.uxcam.UXCam;

import org.json.JSONArray;
import org.json.JSONObject;

import miituo.com.miituo.R;
import miituo.com.miituo.cotizar.CotizaTipos;
import miituo.com.miituo.cotizar.RateList;
import miituo.com.miituo.threats.AvailableSiteSync;
import miituo.com.miituo.utils.Evento;
import miituo.com.miituo.utils.SimpleCallBack;

public class DetallePlanActivity extends AppCompatActivity {

    private RateList rl;
    int id=0;
    private Typeface tipo;
    private TextView lbHeader1,lbHeader2,lbHeader3,
                    lbt1,lbt2,lbt3,lbt4,lbt5,lbt6,lbt7,lbt8,
                    lbs1,lbs2,lbs3,lbs4,lbs5,lbs6,lbs7,lbOK;
    private ImageView mas1,mas2;
    private RelativeLayout cntAsistencia;
    String sex,vehicleId,birthday,garage,cp,formatNac;
    double pagoUnico=0.0;
    boolean isSimulacion=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_plan);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        Gson parseJson = new GsonBuilder().create();
        Intent i=getIntent();
        rl=parseJson.fromJson(i.getExtras().getString("plan"), new TypeToken<RateList>() {}.getType());
        sex=i.getStringExtra("sexo");
        vehicleId=i.getStringExtra("vehicleId");
        birthday=i.getStringExtra("birthday");
        formatNac=i.getStringExtra("formatNac");
        garage=i.getStringExtra("garage");
        cp=i.getStringExtra("cp");
        id=i.getIntExtra("id",0);
        pagoUnico=i.getDoubleExtra("pagoUnico",0.0);
        isSimulacion=i.getBooleanExtra("isSimulacion",false);
        toolbar.setTitle("Detalles ");
        toolbar.setTitleTextColor(Color.BLACK);
        setSupportActionBar(toolbar);
        //get back arrow
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        init();

        mas1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(lbs5.getVisibility()==View.GONE){
                    lbs5.setVisibility(View.VISIBLE);
                    mas1.setImageDrawable(getResources().getDrawable(R.mipmap.menos));
                    return;
                }
                mas1.setImageDrawable(getResources().getDrawable(R.mipmap.btplus));
                lbs5.setVisibility(View.GONE);
            }
        });
        mas2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(cntAsistencia.getVisibility()==View.GONE){
                    cntAsistencia.setVisibility(View.VISIBLE);
                    mas2.setImageDrawable(getResources().getDrawable(R.mipmap.menos));
                    return;
                }
                mas2.setImageDrawable(getResources().getDrawable(R.mipmap.btplus));
                cntAsistencia.setVisibility(View.GONE);
            }
        });
        if(isSimulacion){
            lbOK.setText("Simular");
        }

        lbOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(isSimulacion){
                    simula();
                }
                Gson gson = new Gson();
                String plan=gson.toJson(rl);
                Intent  i=new Intent(DetallePlanActivity.this,ContratarActivity.class);
                i.putExtra("sexo",sex);
                i.putExtra("vehicleId",vehicleId);
                i.putExtra("birthday",birthday);
                i.putExtra("formatNac",formatNac);
                i.putExtra("garage",garage);
                i.putExtra("cp",cp);
                i.putExtra("plan",plan);
                i.putExtra("id",id);
                i.putExtra("pagoUnico",pagoUnico);
                Evento.eventRecord(DetallePlanActivity.this,Evento.CONTRATAR);
                checkSite(i);
            }
        });

    }
    public AlertDialog alertaCotiz;
    private void checkSite(final Intent i){
        new AvailableSiteSync(this, new SimpleCallBack() {
            @Override
            public void run(boolean status, String res) {
                if (!status) {
                    final AlertDialog.Builder builder = new AlertDialog.Builder(DetallePlanActivity.this);
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
                        Intent ii = new Intent(DetallePlanActivity.this, UnavailableSite.class);
                        ii.putExtra("status", res);
                        ii.putExtra("idQuot",id);
                        startActivity(ii);
                    }
                }
            }
        }).execute();
    }

    private void init(){
        tipo=Typeface.createFromAsset(getAssets(), "fonts/herne1.ttf");
        lbHeader1=(TextView)findViewById(R.id.lbPlan);
        lbHeader2=(TextView)findViewById(R.id.lbCostoPlan);
        lbHeader3=(TextView)findViewById(R.id.lbKmPlan);
        lbt1=(TextView)findViewById(R.id.lbDaños);
        lbt2=(TextView)findViewById(R.id.lbRobo);
        lbt3=(TextView)findViewById(R.id.lbDaños3);
        lbt4=(TextView)findViewById(R.id.lbGastos);
        lbt5=(TextView)findViewById(R.id.lbDefensa);
        lbt6=(TextView)findViewById(R.id.lbAsistencia);
        lbt7=(TextView)findViewById(R.id.lbAsistencia21);
        lbt8=(TextView)findViewById(R.id.lbAsistencia23);
        lbs1=(TextView)findViewById(R.id.lbDaños2);
        lbs2=(TextView)findViewById(R.id.lbRobo2);
        lbs3=(TextView)findViewById(R.id.lbDaños32);
        lbs4=(TextView)findViewById(R.id.lbGastos2);
        lbs5=(TextView)findViewById(R.id.lbDefensa2);
        lbs6=(TextView)findViewById(R.id.lbAsistencia22);
        lbs7=(TextView)findViewById(R.id.lbAsistencia24);
        lbOK=(TextView)findViewById(R.id.lbOK);

        lbHeader1.setTypeface(tipo);
        lbHeader2.setTypeface(tipo, Typeface.BOLD);
        lbHeader3.setTypeface(tipo);
        lbt1.setTypeface(tipo, Typeface.BOLD);
        lbt2.setTypeface(tipo, Typeface.BOLD);
        lbt3.setTypeface(tipo, Typeface.BOLD);
        lbt4.setTypeface(tipo, Typeface.BOLD);
        lbt5.setTypeface(tipo, Typeface.BOLD);
        lbt6.setTypeface(tipo, Typeface.BOLD);
        lbt7.setTypeface(tipo, Typeface.BOLD);
        lbt8.setTypeface(tipo, Typeface.BOLD);
        lbs1.setTypeface(tipo);
        lbs2.setTypeface(tipo);
        lbs3.setTypeface(tipo);
        lbs4.setTypeface(tipo);
        lbs5.setTypeface(tipo);
        lbs6.setTypeface(tipo);
        lbs7.setTypeface(tipo);

        mas1=(ImageView)findViewById(R.id.mas1);
        mas2=(ImageView)findViewById(R.id.mas2);

        cntAsistencia=(RelativeLayout)findViewById(R.id.cntAsistencia2);

        lbs5.setText("• Liberación del vehículo\n" +
                "• Obtención de libertad del conductor\n" +
                "• Pago de fianza\n" +
                "• Asesoría para presentar demandas");
        lbs6.setText("• Gastos médicos de emergencia en el extranjero\n" +
                "• Gastos dentales en el extranjero\n" +
                "• Traslado médico\n" +
                "• Reembolso de gastos de asistencia\n   automovilística en el extranjero");
        lbs7.setText("• Cambio de llantas\n" +
                "• Paso de corriente\n" +
                "• Envío de gasolina\n" +
                "• Envío de cerrajero\n" +
                "• Envío de grúa");

        if(rl.getName().contains("imitada")){
            lbt1.setVisibility(View.GONE);
            lbs1.setVisibility(View.GONE);
            lbHeader1.setText("Plan Limitado ");
        }

        String v=String.format("%.2f", rl.getAmount());
        lbHeader2.setText("$"+v);
//        lbHeader2.setText("$"+rl.getAmount());
    }


    public void simula(){
            Gson gson = new Gson();
            String plan = gson.toJson(rl);
            Intent i = new Intent(this, ContratarActivity.class);
            i.putExtra("sexo", sex);
            i.putExtra("vehicleId", vehicleId);
            i.putExtra("birthday", birthday);
            i.putExtra("formatNac", formatNac);
            i.putExtra("garage", garage);
            i.putExtra("cp", cp);
            i.putExtra("plan", plan);
            i.putExtra("pagoUnico", pagoUnico);
            i.putExtra("id", id);
            startActivity(i);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        if (item.getItemId() == android.R.id.home) {
            finish(); // close this activity and return to preview activity (if there is any)
        }
        return true;
    }
}
