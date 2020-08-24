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

import miituo.com.miituo.R;
import miituo.com.miituo.api.ApiClient;

public class CotizaActivity extends AppCompatActivity {

    public WebView vista;
    SharedPreferences app_preferences;
    public Boolean flagContrata = false;

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
        /*FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/

        vista = (WebView)findViewById(R.id.vistaweb);
        WebSettings webSettings = vista.getSettings();
        //Se habilita javaScrip para mostrar los elementos en el webView que ocupen esta tecnologia...
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDomStorageEnabled(true);

        //vista.loadUrl("https://websitedev2019.miituo.com/Cotizar");
        vista.setWebChromeClient(new WebChromeClient());
        vista.loadUrl(ac.UrlCotiza);

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

        //vista.loadUrl(ac.UrlCotiza);
        app_preferences= getSharedPreferences("miituo", Context.MODE_PRIVATE);
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
