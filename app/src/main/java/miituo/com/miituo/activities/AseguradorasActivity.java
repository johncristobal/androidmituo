package miituo.com.miituo.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.firebase.analytics.FirebaseAnalytics;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

import miituo.com.miituo.R;
import miituo.com.miituo.api.ApiClient;
import miituo.com.miituo.cotizar.ApiClientCotiza;
import miituo.com.miituo.utils.Evento;

public class AseguradorasActivity extends AppCompatActivity implements View.OnClickListener{
    public Typeface tipo;
    public TextView lbQuestion,lbQuestion2,lbInfo,lbLimite,lbContinuar,lbWellcome,lbThanks;
    public Spinner spinnerCarriers,spinnerOptions;
    List<String> arraySpinnerCarriers;
    List<String> arraySpinnerOptions;
    private int carrierId,optionId;
    private String carrierDesc,optionDesc;
    int idQuotation=0;
    public static String carriers="{}";
    String cel="",token="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aseguradoras);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Contratar");
        toolbar.setTitleTextColor(Color.BLACK);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        tipo = Typeface.createFromAsset(getAssets(), "fonts/herne1.ttf");
        lbQuestion=(TextView)findViewById(R.id.lbQuestion);
        lbQuestion.setTypeface(tipo);
        lbQuestion2=(TextView)findViewById(R.id.lbQuestion2);
        lbQuestion2.setTypeface(tipo);
        lbInfo=(TextView)findViewById(R.id.lbInfo);
        lbInfo.setTypeface(tipo);
        lbContinuar=(TextView)findViewById(R.id.lbContinuar);
        lbContinuar.setTypeface(tipo);
        lbLimite=(TextView)findViewById(R.id.lbLimite);
        lbLimite.setTypeface(tipo,Typeface.BOLD);
        lbWellcome=(TextView)findViewById(R.id.lbWellcome);
        lbWellcome.setTypeface(tipo,Typeface.BOLD);
        lbThanks=(TextView)findViewById(R.id.lbThanks);
        lbThanks.setTypeface(tipo,Typeface.BOLD);
        lbThanks.setText("¡Gracias por ser parte del consumo equitativo!\n\nEn un momento recibirás tu póliza");
        lbLimite.setText("Tienes 24 hrs. a partir de este momento.\n¡No lo olvides!");
        spinnerCarriers=(Spinner)findViewById(R.id.spinnerCarriers);
        spinnerOptions=(Spinner)findViewById(R.id.spinneroptions);
        Intent i=getIntent();
        cel=i.getStringExtra("cel");
        token=i.getStringExtra("token");
        idQuotation=i.getIntExtra("idQuotation",0);
        pintacarriers();
        lbContinuar.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(carrierDesc.equalsIgnoreCase("0")){
            Toast.makeText(AseguradorasActivity.this,"Por favor indica tu aseguradora anterior.",
            Toast.LENGTH_LONG).show();
        }
        else if(optionDesc.equalsIgnoreCase("0")){
            Toast.makeText(AseguradorasActivity.this,"Por favor indica cómo te enteraste de nosotros.",
                    Toast.LENGTH_LONG).show();
        }
        else {
            new setPreviousCarrier().execute();
            Intent i = new Intent(this, SyncActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            i.putExtra("actualizar", true);
            i.putExtra("newer", true);
            startActivity(i);
        }
    }

    String ErrorCode,profs;
    JSONArray arrayProfs;
    public void pintacarriers(){
        try {
            profs = carriers;
            arraySpinnerCarriers = new ArrayList<>();//[tokenTipos.size()+1];
            arraySpinnerCarriers.clear();
            arraySpinnerCarriers.add("Selecciona");
            arrayProfs=new JSONArray(profs);
            for(int i=0; i<arrayProfs.length();i++){
                arraySpinnerCarriers.add(arrayProfs.getJSONObject(i).getString("Name"));
            }

            arraySpinnerOptions= new ArrayList<>();//[tokenTipos.size()+1];
            arraySpinnerOptions.clear();
            arraySpinnerOptions.add("Selecciona");
            arraySpinnerOptions.add("Facebook");
            arraySpinnerOptions.add("Google");
            arraySpinnerOptions.add("Me lo recomendaron");

        } catch (Exception ex) {
            ErrorCode = ex.getMessage();
        }

        Adaptador adaptador1 = new Adaptador(AseguradorasActivity.this, R.layout.simple_spinner_down,arraySpinnerCarriers);
        spinnerCarriers.setAdapter(adaptador1);

        spinnerCarriers.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(i==0) {
                    carrierId=0;
                    carrierDesc=""+0;
                }
                else{
                    try {
                        carrierId=arrayProfs.getJSONObject(i-1).getInt("Id");
                        carrierDesc=String.valueOf(arrayProfs.getJSONObject(i-1).getString("Name"));
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


        Adaptador adaptador2 = new Adaptador(AseguradorasActivity.this, R.layout.simple_spinner_down,arraySpinnerOptions);
        spinnerOptions.setAdapter(adaptador2);

        spinnerOptions.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(i==0) {
                    optionId=0;
                    optionDesc=""+0;
                }
                else{
                    try {
                        optionId=(i);
                        optionDesc=arraySpinnerOptions.get(i);
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

    }

    //===========================Thread call to fill combo==============================================
    public class setPreviousCarrier extends AsyncTask<String,Void,Void> {

        String ErrorCode,profs;
        JSONArray arrayProfs;

        String[] stringArray;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
        }

        @Override
        protected Void doInBackground(String... strings) {

            ApiClientCotiza client = new ApiClientCotiza(AseguradorasActivity.this);
            try {
                //recupera datos
                profs = client.setPreviousCarrier("Poll/PreCotization/",idQuotation,carrierDesc,optionDesc);

            } catch (Exception ex) {
                ErrorCode = ex.getMessage();
                this.cancel(true);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
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
            if(position == 0){
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
                tv.setTextColor(Color.BLACK);
            }
            else {
                tv.setTextColor(Color.BLACK);
            }
            return view;
        }
    }
}
