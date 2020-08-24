package miituo.com.miituo.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import miituo.com.miituo.R;
import miituo.com.miituo.cotizar.ApiClientCotiza;
import miituo.com.miituo.data.IinfoClient;
import miituo.com.miituo.delegates.ContratarDelegate;
import miituo.com.miituo.utils.Alarma;
import miituo.com.miituo.utils.SimpleCallBack;

public class WebActivity extends BaseActivity implements SimpleCallBack {

    private WebView wvPago;
    private String referenciaPago = "";
    private boolean cambioExitoso=false;
    private LinearLayout cntTarjetas,cntAmex;
    public int tipoPago=1;

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(cambioExitoso){
            finish();
            Intent i = new Intent(this, PrincipalActivity.class);
            i.putExtra("actualizar","1");
            startActivity(i);
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("¿Seguro que deseas abandonar el sitio?");
        builder.setMessage("Es posible que los cambios que implementaste no se puedan guardar.");
        builder.setCancelable(false);
        builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
//                    super.onBackPressed();
                Alarma a=new Alarma();
                a.cancelAlarm(WebActivity.this);
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
        return false;
    }
    AlertDialog alertToClose;
    @Override
    public void onBackPressed() {

        if(cambioExitoso){
            finish();
            Intent i = new Intent(this, PrincipalActivity.class);
            i.putExtra("actualizar","1");
            startActivity(i);
        }
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("¿Seguro que deseas abandonar el sitio?");
            builder.setMessage("Es posible que los cambios que implementaste no se puedan guardar.");
            builder.setCancelable(false);
            builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
//                    super.onBackPressed();
                    Alarma a=new Alarma();
                    a.cancelAlarm(WebActivity.this);
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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Cambiar método de pago");
        toolbar.setTitleTextColor(Color.BLACK);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        wvPago = (WebView) findViewById(R.id.wvPago);
        cntTarjetas=(LinearLayout)findViewById(R.id.cntTarjetas);
        cntAmex=(LinearLayout)findViewById(R.id.cntAmex);
        wvPago.setWebViewClient(new WebViewClient());
        wvPago.getSettings().setJavaScriptEnabled(true);
        cntTarjetas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(cntTarjetas.getBackground()!=getResources().getDrawable(R.drawable.blueborder)) {
                    cntTarjetas.setBackground(getResources().getDrawable(R.drawable.blueborder));
                    cntAmex.setBackground(getResources().getDrawable(R.drawable.border));
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
                    tipoPago=1;
                    showUrl();
                }
            }
        });
        showUrl();
    }

    public void showUrl() {
        new getReference(IinfoClient.getInfoClientObject().getClient().getId(), IinfoClient.getInfoClientObject().getPolicies().getId(), tipoPago, new SimpleCallBack() {
            @Override
            public void run(boolean status, String res) {
                if (status) {
                    try {
                        JSONArray json = new JSONArray(res);
                        String url = json.getString(0);
                        referenciaPago = json.getString(1);
                        if (url.startsWith("http")) {
                            wvPago = (WebView) findViewById(R.id.wvPago);
                            wvPago.loadUrl(url);
                            wvPago.setVisibility(View.VISIBLE);
                            wvPago.setWebViewClient(new WebViewClient());
                            wvPago.getSettings().setJavaScriptEnabled(true);
                            Alarma a = new Alarma();
                            a.setAlarm(WebActivity.this, referenciaPago, "1", 20000, WebActivity.this);
                        } else if (referenciaPago.startsWith("http")) {
                            String temp = referenciaPago;
                            referenciaPago = url;
                            url = temp;
                            wvPago = (WebView) findViewById(R.id.wvPago);
                            wvPago.loadUrl(url);
                            wvPago.setVisibility(View.VISIBLE);
                            wvPago.setWebViewClient(new WebViewClient());
                            wvPago.getSettings().setJavaScriptEnabled(true);
                            Alarma a = new Alarma();
                            a.setAlarm(WebActivity.this, referenciaPago, "1", 20000, WebActivity.this);
                        } else {
                            Toast.makeText(WebActivity.this, "Error al generar referencia, intenta de nuevo: ", Toast.LENGTH_LONG).show();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }).execute();
    }

    public static AlertDialog alertaPago;
    public static boolean ispagoshown = false;
    public static boolean ispagoshown2 = false;

    @Override
    public void run(boolean status, final String res) {
        print("en callback true=termino alarma / false ispaysucceded\n==============================\n" +
                " status: " + status + " res: " + res);
        final Alarma a = new Alarma();
        a.cancelAlarm(WebActivity.this);
        if (status) {
            new getIsPaySucceded(WebActivity.this,referenciaPago, tipoPago,this).execute();
        } else {
            if (res == null) {
                a.setAlarm(WebActivity.this, referenciaPago, "1", 3000, WebActivity.this);
            } else {
                print("ya cayo el pago?: " + res);
                if (res.equalsIgnoreCase("402")) {
                    final AlertDialog.Builder builder = new AlertDialog.Builder(WebActivity.this);
                    builder.setTitle("Error en el pago");
                    builder.setMessage("Pago denegado.");
                    builder.setCancelable(true);
                    builder.setPositiveButton("Reintentar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            showUrl();
                            ispagoshown = false;
                            a.setAlarm(WebActivity.this, res, "1", 3000, WebActivity.this);
                        }
                    });
                    builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
                        @Override
                        public void onDismiss(DialogInterface dialog) {
                            ispagoshown = false;
                        }
                    });
                    runOnUiThread(new Runnable() {
                        public void run() {
                            if (!ispagoshown) {
                                alertaPago = builder.create();
                                ispagoshown = true;
                                alertaPago.show();
                            }
                        }
                    });
                }
                else if(res.contains("ESTA REFERENCIA YA FUE UTILIZADA EN OTRA POLIZA") || res.contains("approved")) {
                    final AlertDialog.Builder builder = new AlertDialog.Builder(WebActivity.this);
                    builder.setTitle("Información");
                    builder.setMessage("Cambio de método de pago exitoso");
                    builder.setCancelable(false);
                    Alarma ab=new Alarma();
                    ab.cancelAlarm(WebActivity.this);
                    cambioExitoso=true;
                    builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ispagoshown2 = false;
                            finish();
                            Intent i = new Intent(WebActivity.this, PrincipalActivity.class);
                            i.putExtra("actualizar","1");
                            startActivity(i);
                        }
                    });
                    runOnUiThread(new Runnable() {
                        public void run() {
                            if (!ispagoshown2) {
                                alertaPago = builder.create();
                                ispagoshown2 = true;
                                alertaPago.show();
                            }
                        }
                    });
                }
            }
        }
    }


    //===========================Thread call to fill combo==============================================
    public class getReference extends AsyncTask<String, Void, Void> {

        ProgressDialog progress = new ProgressDialog(WebActivity.this);
        String ErrorCode, res;
        SimpleCallBack callBack;
        int idCliente = 0;
        int idPoliza = 0;
        int tipoPago=3;
        public getReference(int idCliente, int idPoliza, int tipoPago, SimpleCallBack cb) {
            this.callBack = cb;
            this.idCliente = idCliente;
            this.idPoliza = idPoliza;
            this.tipoPago=tipoPago;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progress.setTitle("Información");
            progress.setMessage("Solicitando información de pago...");
            progress.setIndeterminate(true);
            progress.setCancelable(false);
            progress.show();
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
            progress.dismiss();
            Toast message = Toast.makeText(WebActivity.this, ErrorCode, Toast.LENGTH_LONG);
            message.show();
        }

        @Override
        protected Void doInBackground(String... strings) {

            ApiClientCotiza client = new ApiClientCotiza(WebActivity.this);
            try {
                //recupera datos
                this.res = client.getNewReference("Payment/GetMitNewToken/" + idCliente + "/Policy/" + idPoliza+ "/setPayment/" + tipoPago);
            } catch (Exception ex) {
                ErrorCode = ex.getMessage();
                this.cancel(true);
                callBack.run(false, ErrorCode);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            progress.dismiss();
            callBack.run(true, res);
        }
    }

    //===========================Thread call to fill combo==============================================
    public static class getIsPaySucceded extends AsyncTask<String, Void, Void> {

        String ErrorCode, res;
        String referencia = "";
        int tipoPago=3;
        SimpleCallBack cb;
        Context c;

        public getIsPaySucceded(Context c, String referencia, int tipoPago,SimpleCallBack cb) {
            this.referencia = referencia;
            this.cb = cb;
            this.c=c;
            this.tipoPago=tipoPago;
        }

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

            ApiClientCotiza client = new ApiClientCotiza(c);
            try {
                //recupera datos
                this.res = client.getIsPaySuccededToken("payment/Searchreference/" + referencia + "/"+tipoPago+"/");
                JSONObject j = new JSONObject(res);
                if (res == null || res.contains("AUN NO SE ENCUENTRA") || j.has("Message")) {
                    cb.run(false, null);
                }
            } catch (Exception ex) {
                ErrorCode = ex.getMessage();
                if (ErrorCode == null || ErrorCode.contains("AUN NO SE ENCUENTRA")) {
                    cb.run(false, null);
                }else {
                    cb.run(false, ErrorCode);
                }
                cancel(true);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            cb.run(false, res);
        }
    }
}
