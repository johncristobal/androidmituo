package miituo.com.miituo.activities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ListView;
import android.widget.TextView;

import miituo.com.miituo.R;
import miituo.com.miituo.VehicleModelAdapter;
import miituo.com.miituo.adapters.VehicleModelAdapterSelector;
import miituo.com.miituo.data.IinfoClient;
import miituo.com.miituo.data.InfoClient;
import miituo.com.miituo.utils.Alarma;
import miituo.com.miituo.utils.CallBack;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;

import static miituo.com.miituo.activities.MainActivity.result;

public class CambioPagoActivity extends BaseActivity implements CallBack {

    public Typeface typeface;
    private ListView listPolizas;
    private TextView lbOK,lbQuestion,lbSelect;
    private VehicleModelAdapterSelector vadapter;
    SharedPreferences app_preferences;
    public long starttime;
    AlertDialog alert;
    public static int sellected=-1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cambio_pago);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Cambiar método de pago");
        toolbar.setTitleTextColor(Color.BLACK);
        setSupportActionBar(toolbar);
        //get back arrow
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        typeface = Typeface.createFromAsset(getAssets(), "fonts/herne1.ttf");
        lbQuestion=(TextView)findViewById(R.id.lbQuestion);
        lbQuestion.setTypeface(typeface,Typeface.BOLD);
        lbSelect=(TextView)findViewById(R.id.lbSelecciona);
        lbSelect.setTypeface(typeface);
        lbOK=(TextView)findViewById(R.id.lbOK);
        lbOK.setTypeface(typeface);
        lbOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(sellected==-1){
                    AlertDialog.Builder builder = new AlertDialog.Builder(CambioPagoActivity.this);
                    builder.setTitle("Atención");
                    builder.setMessage("Por favor selecciona una póliza para continuar.");
                    builder.setCancelable(false);
                    builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            alert.dismiss();
                        }
                    });
                    alert = builder.create();
                    alert.show();
                }
                else{
                    IinfoClient.setInfoClientObject(result.get(sellected));
                    startActivity(new Intent(CambioPagoActivity.this, WebActivity.class).putExtra("isPoliza",false));
                }
            }
        });
        listPolizas = (ListView) findViewById(R.id.listPolizas);
        sellected=-1;
        removeInvalidPolicies();
        app_preferences= getSharedPreferences("miituo", Context.MODE_PRIVATE);
        starttime = app_preferences.getLong("time",0);
        vadapter = new VehicleModelAdapterSelector(this, result,typeface,starttime, this);
        vadapter.notifyDataSetChanged();
        listPolizas.setAdapter(vadapter);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        if (item.getItemId() == android.R.id.home) {
            finish(); // close this activity and return to preview activity (if there is any)
            Intent i=new Intent(this,PrincipalActivity.class);
            startActivity(i);
        }
        return true;
    }
    private void removeInvalidPolicies(){
        for(int i=(result.size()-1);i>=0;i--){
//            if(!result.get(i).getPolicies().isHasVehiclePictures() &&
//                    !result.get(i).getPolicies().isHasOdometerPicture() &&
            if(result.get(i).getPolicies().getReportState()==4){
                result.remove(i);
            }
        }
    }

    @Override
    public void runInt(int value) {
        sellected=value;
        vadapter.notifyDataSetChanged();
    }
    @Override
    public void runInt2(int value) { }
    @Override
    public void llamarAtlas(InfoClient v) { }
}

