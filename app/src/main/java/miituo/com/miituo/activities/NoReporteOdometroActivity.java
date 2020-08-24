package miituo.com.miituo.activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Html;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import miituo.com.miituo.R;

public class NoReporteOdometroActivity extends AppCompatActivity {

    private Typeface tipo;
    JSONObject json=null;
    private TextView lbGral,lbA1,lbA2,lbB1,lbB2,lbC1,lbC2,lbD1,lbD2,lbPagar,lbRegresar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_no_reporte_odometro);
        init();
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);

        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return true;
    }

    private void init(){
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        toolbar.setTitleTextColor(Color.BLACK);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        tipo= Typeface.createFromAsset(getAssets(), "fonts/herne1.ttf");
        lbGral=(TextView)findViewById(R.id.lbGral);
        lbGral.setTypeface(tipo);
        lbA1=(TextView)findViewById(R.id.lbA1);
        lbA1.setTypeface(tipo);
        lbA2=(TextView)findViewById(R.id.lbA2);
        lbA2.setTypeface(tipo);
        lbB1=(TextView)findViewById(R.id.lbB1);
        lbB1.setTypeface(tipo);
        lbB2=(TextView)findViewById(R.id.lbB2);
        lbB2.setTypeface(tipo);
        lbC1=(TextView)findViewById(R.id.lbC1);
        lbC1.setTypeface(tipo);
        lbC2=(TextView)findViewById(R.id.lbC2);
        lbC2.setTypeface(tipo);
        lbD1=(TextView)findViewById(R.id.lbD1);
        lbD1.setTypeface(tipo);
        lbD2=(TextView)findViewById(R.id.lbD2);
        lbD2.setTypeface(tipo);
        lbPagar=(TextView)findViewById(R.id.btnPagar);
        lbPagar.setTypeface(tipo);
        lbRegresar=(TextView)findViewById(R.id.btnRegresar);
        lbRegresar.setTypeface(tipo);
        JSONObject datos;
        try {
            json=new JSONObject(getIntent().getStringExtra("json"));
            datos=json.getJSONArray("ResulList").getJSONObject(0);
            lbGral.setText(Html.fromHtml("<p>Han transcurrido <b>"+datos.getString("Dias_sin_reportar")+" días" +
                    "</b> desde tu<br> último reporte de odómetro,<br>tu <b>cargo es de $"+datos.getString("amount")+"</b></p>"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public void regresar(View v){
        finish();
    }

    public void pagar(View v){
        Intent returnIntent = new Intent();
        returnIntent.putExtra("result","OK");
        setResult(Activity.RESULT_OK,returnIntent);
        finish();
    }
}
