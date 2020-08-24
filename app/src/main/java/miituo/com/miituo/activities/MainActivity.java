package miituo.com.miituo.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.VideoView;
import com.google.firebase.iid.FirebaseInstanceId;
import com.uxcam.UXCam;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import miituo.com.miituo.GcmIntentService;
import miituo.com.miituo.R;
import miituo.com.miituo.data.DBaseMethods;
import miituo.com.miituo.data.InfoClient;
import miituo.com.miituo.data.modelBase;
import miituo.com.miituo.tuto.TutorialActivity;

public class MainActivity extends BaseActivity {

    private static final long SPLASH_SCREEN_DELAY = 4000;

    VideoView mVideoView;
    SharedPreferences app_preferences;
    public modelBase base;

    public static String token;
    public static String isRecorderEnabled =null;
    public static List<InfoClient> result;
    String sesion;
    TextView lbTitle, lb1, lb2, lbOK,
            lbPagoUnico, lbCosto, lbPlan, lbTarifa, lbCostoTarifa, lbSeguro,
            lbl1,lbl2,lbl3,lbl4;
    LinearLayout cnt2,cntCancelar;
    private Typeface tipo;
    boolean isNormalStart = true, fromPrincipal=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String tokenF = FirebaseInstanceId.getInstance().getToken();
        print("token: " + tokenF);

        base = new modelBase(getApplicationContext(), Integer.parseInt(getResources().getString(R.string.dbversion)));
        DBaseMethods.base = base;
        app_preferences = getSharedPreferences("miituo", Context.MODE_PRIVATE);
        sesion = app_preferences.getString("sesion", "null");

//        ShortcutBadger.applyCount(this,1);

//        Intent i = new Intent(getApplicationContext(), TextRecognizer.class);
//        finish();
//        startActivity(i);
//        if (true){
//            return;
//        }

        Bundle b = getIntent().getExtras();
        String idPush = null;
        idPush = b != null ? b.getString("idPush", null) : null;
        String idPushS = null;
        idPushS = getIntent().getStringExtra("idPush");
        print(" idPush: " + idPush + "\nidPushS: " + idPushS);
//        idPush="3";
        if (idPush != null && !idPush.equalsIgnoreCase("") && (idPush.equalsIgnoreCase("2") || idPush.equalsIgnoreCase("3"))
                || idPushS != null && !idPushS.equalsIgnoreCase("") && (idPushS.equalsIgnoreCase("2") || idPushS.equalsIgnoreCase("3"))) {
            if (idPush != null && (!idPush.equalsIgnoreCase(""))) {
                isNormalStart = false;
                pushStart(idPush, b.getString("tarifa", null),b.getBoolean("fromPrincipal",false));
            } else {
                isNormalStart = true;
                pushStart(idPushS, getIntent().getStringExtra("tarifa"),getIntent().getBooleanExtra("fromPrincipal",false));
            }
        } else {
            normalStart();
        }
        startService(new Intent(this, GcmIntentService.class));
    }

    private void normalStart() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        mVideoView = (VideoView) findViewById(R.id.videoView);
        String uri = "android.resource://" + getPackageName() + "/" + R.raw.logo_ani_miituo;
        if (mVideoView != null) {
            mVideoView.setVideoURI(Uri.parse(uri));
            mVideoView.setZOrderOnTop(true);
            mVideoView.requestFocus();
            mVideoView.start();
        }
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
//                startActivity(new Intent(getApplicationContext(),MapsActivity.class));
                if (sesion.equals("1")) {
                    Intent i = new Intent(getApplicationContext(), SyncActivity.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    finish();
                    startActivity(i);
                } else {
                    Intent i = new Intent(getApplicationContext(), TutorialActivity.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    finish();
                    startActivity(i);
                }
            }
        };
        Timer timer = new Timer();
        timer.schedule(task, SPLASH_SCREEN_DELAY);
    }

    private void pushStart(String idPush, String tarifa,boolean fromPrincipal) {
        setContentView(R.layout.activity_info);
        this.fromPrincipal=fromPrincipal;
        tipo = Typeface.createFromAsset(getAssets(), "fonts/herne1.ttf");

        cnt2 = (LinearLayout) findViewById(R.id.cnt2);
        cntCancelar = (LinearLayout) findViewById(R.id.cntCancelar);
        lbTitle = (TextView) findViewById(R.id.lbTitle);
        lbTitle.setTypeface(tipo, Typeface.BOLD);
        lb1 = (TextView) findViewById(R.id.lb1);
        lb1.setTypeface(tipo);
        lb2 = (TextView) findViewById(R.id.lb2);
        lb2.setTypeface(tipo);
        lbOK = (TextView) findViewById(R.id.lbOK);
        lbOK.setTypeface(tipo, Typeface.BOLD);
        lbPagoUnico = (TextView) findViewById(R.id.lbPagoUnico);
        lbPagoUnico.setTypeface(tipo);
        lbCosto = (TextView) findViewById(R.id.lbCosto);
        lbCosto.setTypeface(tipo, Typeface.BOLD);
        lbPlan = (TextView) findViewById(R.id.lbPlan);
        lbPlan.setTypeface(tipo);
        lbTarifa = (TextView) findViewById(R.id.lbTarifa);
        lbTarifa.setTypeface(tipo);
        lbCostoTarifa = (TextView) findViewById(R.id.lbCostoTarifa);
        lbCostoTarifa.setTypeface(tipo, Typeface.BOLD);
        lbl1 = (TextView) findViewById(R.id.lbl1);
        lbl1.setTypeface(tipo);
        lbl2 = (TextView) findViewById(R.id.lbl2);
        lbl2.setTypeface(tipo);
        lbl3 = (TextView) findViewById(R.id.lbl3);
        lbl3.setTypeface(tipo);
        lbl4 = (TextView) findViewById(R.id.lbl4);
        lbl4.setTypeface(tipo);
        lbSeguro = (TextView) findViewById(R.id.lbSeguro);
        lbSeguro.setTypeface(tipo);
        lbSeguro.setText("Seguro de responsabilidad civil\nDerecho de póliza");
        String[] valores = tarifa.split("/");
        if (valores.length >= 4) {
            switch (idPush) {
                case "2":
                    cnt2.setVisibility(View.VISIBLE);
                    cntCancelar.setVisibility(View.VISIBLE);
                    lbTitle.setText("Renovación de póliza");
                    lb1.setText(Html.fromHtml("<p>Tu renovación esta lista, la recibirás el día <b>" + valores[2] + ".</b></p>"));
                    lb2.setText(Html.fromHtml("Recuerda que después de tu siguiente reporte haremos el cargo de:"));
                    lbCostoTarifa.setText("$" + valores[0]);
                    lbCosto.setText("$" + valores[1]);
                    lbTarifa.setText(""+valores[3]);
                    lbl3.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", "5584210500", null));
                            startActivity(intent);
                        }
                    });
                    lbl4.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent2 = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", "018009530059", null));
                        startActivity(intent2);
                    }
                });
                    break;
                case "3":
                    cnt2.setVisibility(View.GONE);
                    cntCancelar.setVisibility(View.GONE);
                    lbTitle.setText("Cargo por renovación de póliza");
                    lb1.setText(Html.fromHtml("<p>Ahora haremos el cargo correspondiente por <b>renovación de póliza.</b></p>"));
                    lb2.setText(Html.fromHtml("Ya tienes tu póliza en tu correo, solo falta hacer el cargo y todo estará listo."));
                    lbCosto.setText("$" + valores[1]);
                    lbPlan.setVisibility(View.GONE);
                    break;
            }
        } else {
            finish();
        }
    }

    public void entendido(View v) {
        if(fromPrincipal){
            finish();
            return;
        }
        if (!isNormalStart) {
            Intent i = new Intent(MainActivity.this, SyncActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(i);
        }
        else {
            finish();
        }
    }

    @Override
    public void onBackPressed() {
        if(fromPrincipal){
            finish();
            return;
        }
        if (!isNormalStart) {
            Intent i = new Intent(MainActivity.this, SyncActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(i);
        }
        else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        if(fromPrincipal){
            finish();
            return true;
        }
        if (item.getItemId() == android.R.id.home) {
            finish(); // close this activity and return to preview activity (if there is any)
        }
        return true;
    }

}
