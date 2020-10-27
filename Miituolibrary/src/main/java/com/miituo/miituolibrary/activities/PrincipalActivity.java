package com.miituo.miituolibrary.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ListView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.miituo.miituolibrary.R;
import com.miituo.miituolibrary.activities.adapters.VehicleModelAdapter;
import com.miituo.miituolibrary.activities.data.InfoClient;
import com.miituo.miituolibrary.activities.threats.GetPoliciesData;
import com.miituo.miituolibrary.activities.utils.SimpleCallBack;

import java.util.List;

public class PrincipalActivity extends AppCompatActivity {

    String telefono;
    SharedPreferences app_preferences;

    private ListView vList;
    private VehicleModelAdapter vadapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);

        telefono = getIntent().getStringExtra("telefono");

        app_preferences = getSharedPreferences(getString(R.string.shared_name_prefs), Context.MODE_PRIVATE);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getPolizasData(telefono);
    }

    public void getPolizasData(String telefono){
        String url = "InfoClientMobil/Celphone/"+telefono;
        new GetPoliciesData(url, PrincipalActivity.this, new SimpleCallBack() {
            @Override
            public void run(boolean status, String res) {
                if (!status){
                    String data[] = res.split("@");
                    launchAlert(data[1]);
                }else{
                    //tenemos polizas, recuperamos list y mandamos a sms...
                    SharedPreferences.Editor editor = app_preferences.edit();
                    editor.putString("polizas", res);
                    editor.putString("Celphone", telefono);
                    editor.apply();

                    Gson parseJson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'hh:mm:ss").create();
                    List<InfoClient> InfoList = parseJson.fromJson(res, new TypeToken<List<InfoClient>>() {
                    }.getType());

                    //final GlobalActivity globalVariable = (GlobalActivity) getApplicationContext();
                    //globalVariable.setPolizas(InfoList);

                    List<InfoClient> result = InfoList;
                    if (result.size() < 1) {
                        //showViews(true);
                    }else{
                        //showViews(false);
                        String na = result.get(0).getClient().getName();
                        app_preferences.edit().putString("nombre", na).apply();

                        vList = (ListView) findViewById(R.id.listviewinfoclient);
                        //removeInvalidPolicies();
                        vadapter = new VehicleModelAdapter(getApplicationContext(), result, typeface, starttime, PrincipalActivity.this);

                        vadapter.notifyDataSetChanged();
                        vList.setAdapter(vadapter);
                        if(result.size() > 0) {
                            tokencliente = result.get(0).getClient().getToken();
                        }else{
                            tokencliente = "";
                        }

                        //vList.setAdapter(vadapter);
                        //vadapter.notifyDataSetChanged();
                        swipeContainer.setRefreshing(false);
                    }
                }
            }
        }).execute();
    }
}