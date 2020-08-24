package miituo.com.miituo.activities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import miituo.com.miituo.R;
import miituo.com.miituo.api.ApiClient;
import miituo.com.miituo.threats.GetUrlAsync;
import miituo.com.miituo.utils.SimpleCallBack;

public class CotizaActivity extends AppCompatActivity {

    public WebView vista;
    SharedPreferences app_preferences;
    public Boolean flagContrata = false;
    Boolean flagClient = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cotiza);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        toolbar.setTitleTextColor(Color.BLACK);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        final ApiClient ac = new ApiClient(CotizaActivity.this);

        vista = (WebView)findViewById(R.id.vistaweb);
        WebSettings webSettings = vista.getSettings();
        //Se habilita javaScrip para mostrar los elementos en el webView que ocupen esta tecnologia...
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDomStorageEnabled(true);

        flagClient = getIntent().getBooleanExtra("cliente",false);
        Log.w("cliente", ""+flagClient);

        app_preferences= getSharedPreferences("miituo", Context.MODE_PRIVATE);
    }

    @Override
    protected void onResume() {
        super.onResume();

        String url  = "Parameters/GetParameterByName/AppUrlAndroid";

        GetUrlAsync gu = new GetUrlAsync(CotizaActivity.this, url, new SimpleCallBack(){
            @Override
            public void run(boolean status, String res) {
                if (status){
                    try{
                        JSONArray array = new JSONArray(res);
                        JSONObject o = array.getJSONObject(0);
                        String url = o.getString("Valor");
                        JSONObject obj= new JSONObject(url);
                        String u2 = obj.getString("UrlNoClient");
                        String u = obj.getString("UrlClient");

                        if(flagClient)
                            inicializarWeb(u);
                        else
                            inicializarWeb(u2);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }else{

                }
            }
        });
        gu.execute();
    }

    public void inicializarWeb(String u){
        Log.w("url cotiza:", u);
        vista.setWebChromeClient(new WebChromeClient());
        vista.loadUrl(u);
        vista.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url){
                view.loadUrl(url);
                Log.e("url", url);
                if(url.contains("Contratar")){
                    flagContrata = true;
                }
                if(url.contains("Gracias")){
                    flagContrata = false;
                }
                if (url.contains("Home") || url.contains("home")){
                    if (flagContrata){
                        launchAlert();
                    }else {
                        finish();
                    }
                }
                return true;
            }
        });
    }

    @Override
    public void onBackPressed() {
        if(flagContrata){
            launchAlert();
        }else {
            super.onBackPressed();
        }
    }

    public void launchAlert(){
        AlertDialog alertToClose;
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("¿Deseas abandonar el sitio?");
        builder.setMessage("Es posible que los cambios que implementaste no se puedan guardar.");
        builder.setCancelable(false);
        builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
//                    super.onBackPressed();
                //alertToClose.dismiss();
                finish();
            }
        });
        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //alertToClose.dismiss();
            }
        });
        alertToClose = builder.create();
        alertToClose.show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);

        if (item.getItemId() == android.R.id.home) {

            if(flagContrata){
                launchAlert();
            }else {
                finish();
                //super.onBackPressed();
            }
            /*if(app_preferences.getString("sesion","null").equals("1")){
                Intent i = new Intent(CotizaActivity.this, PrincipalActivity.class);
                startActivity(i);
            }else{
                finish();
            }*/
        }
        return true;
    }
}
