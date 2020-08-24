package miituo.com.miituo.activities;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ListView;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;

import miituo.com.miituo.R;
import miituo.com.miituo.adapters.TerminosAdapter;
import miituo.com.miituo.threats.GetTerminosPDFSync;
import miituo.com.miituo.utils.SimpleCallBack;

public class TerminosActivity extends BaseActivity implements SimpleCallBack {

    private Typeface typeface;
    private ListView listaTerminos;
    private TerminosAdapter vadapter;
    public static AlertDialog alertaPago;

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return true;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_terminos);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Avisos legales y Condiciones");
        toolbar.setTitleTextColor(Color.BLACK);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        typeface = Typeface.createFromAsset(getAssets(), "fonts/herne1.ttf");
        listaTerminos=(ListView)findViewById(R.id.listaTerminos);
        vadapter = new TerminosAdapter(getApplicationContext(),typeface,this);
        listaTerminos.setAdapter(vadapter);
    }

    @Override
    public void run(boolean status, String res) {
        String urlString="",pdf="",name="";
        final String nameF;
        switch (res){
            case "0":
                urlString="https://www.miituo.com/Terminos/getPDF?Url=C%3A%5CmiituoFTP%5CAviso_de_Privacidad.pdf";
                pdf="https://www.miituo.com/Terminos/downloadPDF?Url=C:/miituoFTP/Aviso_de_Privacidad.pdf";
                name="AvisoPrivacidad";
                break;
            case "1":
                urlString="https://www.miituo.com/Terminos/getPDF?Url=C%3A%5CmiituoFTP%5CTerminos%20y%20condiciones%20miituo.pdf";
                pdf="https://www.miituo.com/Terminos/downloadPDF?Url=C:/miituoFTP/Terminos%20y%20condiciones%20miituo.pdf";
                name="TerminosMiituo";
                break;
            case "2":
                urlString="https://www.miituo.com/Terminos/getPDF?Url=c%3A%5CmiituoFTP%5CCondicionesGeneralesSeguro.pdf";
                pdf="https://www.miituo.com/Terminos/downloadPDF?Url=C:/miituoFTP/CondicionesGeneralesSeguro.pdf";
                name="CondicionesGeneralesSeguro";
                break;
//            case "3":
//                urlString="https://www.miituo.com/Terminos/getPDF?Url=c%3A%5CmiituoFTP%5CCG_SEGURO_OBLIGATORIO_VEH%20RCV_NOV_2016.pdf";
//                pdf="https://www.miituo.com/Termensminos/downloadPDF?Url=C:/miituoFTP/CG_SEGURO_OBLIGATORIO_VEH%20RCV_NOV_2016.pdf";
//                name="SeguroObligatorio";
//                break;
            case "3":
                urlString="https://www.miituo.com/Terminos/getPDF?Url=C%3A%5CmiituoFTP%5CDerechosdeAsegurado.pdf";
                pdf="https://www.miituo.com/Terminos/downloadPDF?Url=C:/miituoFTP/DerechosdeAsegurado.pdf";
                name="DerechosAsegurado";
                break;
        }
        if(!status){
            Intent intent=new Intent(Intent.ACTION_VIEW);
            intent.setDataAndType(Uri.parse("http://docs.google.com/gview?embedded=true&url=" + urlString), "text/html");
            startActivity(intent);
        }
        else{
            nameF=name;
            new GetTerminosPDFSync(this,pdf,name,new SimpleCallBack() {
                @Override
                public void run(boolean status, String res) {
                    if(!status){
//            Toast.makeText(c,"descarga fallida",Toast.LENGTH_LONG).show();
                        final AlertDialog.Builder builder = new AlertDialog.Builder(TerminosActivity.this);
                        builder.setTitle("Descarga Fallida");
                        builder.setMessage("Por favor intentalo más tarde.");
                        builder.setCancelable(false);
                        builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                alertaPago.dismiss();
                            }
                        });
                        TerminosActivity.this.runOnUiThread(new Runnable() {
                            public void run() {
                                alertaPago = builder.create();
                                alertaPago.show();
                            }
                        });
                    }else{
                        // Toast.makeText(c,"descarga exitosa",Toast.LENGTH_LONG).show();
                        final AlertDialog.Builder builder = new AlertDialog.Builder(TerminosActivity.this);
                        builder.setTitle("Descarga completa");
                        builder.setMessage("Ahora "+nameF+".pdf se encuentra en tus descargas.");
                        builder.setCancelable(false);
                        builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                alertaPago.dismiss();
//                    c.startActivity(new Intent(DownloadManager.ACTION_VIEW_DOWNLOADS));
//                                startActivity(new Intent(TerminosActivity.this, PDFViewer.class).putExtra("isPoliza",false));
                            }
                        });
                        TerminosActivity.this.runOnUiThread(new Runnable() {
                            public void run() {
                                alertaPago = builder.create();
                                alertaPago.show();
                            }
                        });
                    }
                }
            }).execute();
        }
    }
}
