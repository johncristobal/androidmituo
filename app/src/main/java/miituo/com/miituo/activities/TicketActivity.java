package miituo.com.miituo.activities;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.Collections;
import java.util.List;

import miituo.com.miituo.R;
import miituo.com.miituo.data.IinfoClient;
import miituo.com.miituo.data.Report;
import androidx.appcompat.widget.Toolbar;
import androidx.appcompat.app.AppCompatActivity;

public class TicketActivity extends AppCompatActivity {

    private Typeface typeface, typefacebold;
    private Report lastReport;
    int before=0;
    private boolean isUnico=true;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.manu_principal,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        if (item.getItemId() == android.R.id.home) {
            finish(); // close this activity and return to preview activity (if there is any)
        }
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticket);
        typeface = Typeface.createFromAsset(getAssets(), "fonts/herne1.ttf");
        typefacebold = Typeface.createFromAsset(getAssets(), "fonts/herne.ttf");
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbartaby);
        toolbar.setTitle("último reporte odómetro");
        toolbar.setTitleTextColor(Color.BLACK);
        setSupportActionBar(toolbar);
        //get back arrow
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        setLast();
        pintaTicket();
    }

    private void setLast(){
        List<Report> list=IinfoClient.getInfoClientObject().getPolicies().getReports();
        Collections.sort(list);
        lastReport=list.get(0);
        if(list.size()>1){
            isUnico=false;
            before=list.get(1).getOdometro();
        }
    }

    private void pintaTicket(){
        try {
            String datoamount = String.valueOf(lastReport.getTotal());
            JSONArray cupones =lastReport.getCupones().equalsIgnoreCase("")? new JSONArray("[]"): new JSONArray(lastReport.getCupones());

            String actual = String.valueOf(lastReport.getOdometro());
            String recorridos="";
            String fee ="0";
            String feepormktarifa = String.valueOf(1.16*IinfoClient.getInfoClientObject().getPolicies().getRate());
            String anterior = String.valueOf(before);
            String diferencia = String.valueOf(lastReport.getRecorridos());
            String KmsDescuento=String.valueOf(lastReport.getCondonacion());


            TextView t47 = (TextView)findViewById(R.id.textView55);
            t47.setTypeface(typeface);
            t47 = (TextView)findViewById(R.id.textView9);
            t47.setTypeface(typeface);
            t47 = (TextView)findViewById(R.id.textView12);
            t47.setTypeface(typeface);
            t47 = (TextView)findViewById(R.id.textView15);
            t47.setTypeface(typeface);
            if(!KmsDescuento.equalsIgnoreCase("0")){t47.setText("Kms recorridos \ndesde el último reporte");}
            t47 = (TextView)findViewById(R.id.textView20);
            t47.setTypeface(typeface);
            t47 = (TextView)findViewById(R.id.textView22);
            t47.setTypeface(typeface);
            t47 = (TextView)findViewById(R.id.textView18);
            t47.setTypeface(typeface);
            t47 = (TextView)findViewById(R.id.textView26);
            t47.setTypeface(typeface);
            t47 = (TextView)findViewById(R.id.textoiva);
            t47.setTypeface(typeface);
            t47 = (TextView)findViewById(R.id.lbCondonados);
            t47.setTypeface(typeface);
            TextView lbCondonados2 = (TextView)findViewById(R.id.lbCondonados2);
            lbCondonados2.setTypeface(typeface);
            t47 = (TextView)findViewById(R.id.lbVacaciones);
            t47.setTypeface(typeface);
            TextView lbVacaciones2 = (TextView)findViewById(R.id.lbVacaciones2);
            lbVacaciones2.setTypeface(typeface);
            t47 = (TextView)findViewById(R.id.lbReferidos);
            t47.setTypeface(typeface);
            TextView lbReferidos2 = (TextView)findViewById(R.id.lbReferidos2);
            lbReferidos2.setTypeface(typeface);
            TextView lbTerminos = (TextView)findViewById(R.id.textoTerminos);
            lbTerminos.setTypeface(typeface);
            lbTerminos.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String urlString="https://www.miituo.com/Terminos/getPDF?Url=C%3A%5CmiituoFTP%5CCondiciones_Generales.pdf";
                    Intent intent=new Intent(Intent.ACTION_VIEW);
                    intent.setDataAndType(Uri.parse("http://docs.google.com/gview?embedded=true&url=" + urlString), "text/html");
                    startActivity(intent);
                }
            });

            recorridos=""+(Integer.parseInt(actual)-Integer.parseInt(anterior));
            String vacacionesHelp="Sabemos que es posible que salgas de vacaciones un par de veces al año," +
                    " por eso ésta vez aplicamos un tope vacacional para que ¡Solo pagues 1200 kms!";
            SpannableStringBuilder str = new SpannableStringBuilder(vacacionesHelp);
            str.setSpan(new android.text.style.StyleSpan(android.graphics.Typeface.BOLD),
                    vacacionesHelp.indexOf("¡Solo"), (vacacionesHelp.length()-1), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

            TextView lbVacaciones=(TextView)findViewById(R.id.lbVacacionesHelp);
            lbVacaciones.setTypeface(typeface);
            lbVacaciones.setText(str);
            if(cupones.length()>0){
                for (int i=0;i<cupones.length();i++) {
                    JSONObject o = cupones.getJSONObject(i);
                    if(o.getInt("Type")==3){
                        lbVacaciones.setVisibility(View.VISIBLE);
                        LinearLayout ll=(LinearLayout)findViewById(R.id.cntVacaciones);
                        ll.setVisibility(View.VISIBLE);
                        diferencia="1200";
                        if(cupones.length()==2){
                            diferencia="1100";
                            LinearLayout ll2=(LinearLayout)findViewById(R.id.cntReferidos);
                            ll2.setVisibility(View.VISIBLE);
                            DecimalFormat formattertarifa = new DecimalFormat("0.0000");
                            datoamount=""+formattertarifa.format((Double.parseDouble(diferencia)*Double.parseDouble(feepormktarifa)));
                            break;
                        }
                    }
                    if(o.getInt("Type")==1 || o.getInt("Type")==2){
                        LinearLayout ll=(LinearLayout)findViewById(R.id.cntReferidos);
                        ll.setVisibility(View.VISIBLE);
                        int acobrar=Integer.parseInt(recorridos)-100;
                        diferencia=""+acobrar;
                        if(acobrar<=0){diferencia="0";datoamount="0.0";}
                        else{
                            DecimalFormat formattertarifa = new DecimalFormat("0.0000");
                            datoamount=""+formattertarifa.format((Double.parseDouble(diferencia)*Double.parseDouble(feepormktarifa)));
                        }
                        if(cupones.length()==2){
                            diferencia="1100";
                            LinearLayout ll2=(LinearLayout)findViewById(R.id.cntVacaciones);
                            ll2.setVisibility(View.VISIBLE);
                            DecimalFormat formattertarifa = new DecimalFormat("0.0000");
                            datoamount=""+formattertarifa.format((Double.parseDouble(diferencia)*Double.parseDouble(feepormktarifa)));
                            break;
                        }
                    }
                }
            }
            else if(!KmsDescuento.equalsIgnoreCase("0")){
                LinearLayout ll=(LinearLayout)findViewById(R.id.cntCondonados);
                ll.setVisibility(View.VISIBLE);
                int acobrar=Integer.parseInt(recorridos)-Integer.parseInt(KmsDescuento);
                lbCondonados2.setText("-"+KmsDescuento);
                diferencia=""+acobrar;
                if(acobrar<0){
                    diferencia="0";
                }
            }

            DecimalFormat formatter = new DecimalFormat("$ #,###.00");
            //GET textviews to show info

            DecimalFormat formatterTwo = new DecimalFormat("#,###,###");

            //odometros-----------------------------------------------------------------
            TextView hoy = (TextView)findViewById(R.id.textView11);
            hoy.setTypeface(typeface);

            Double dos = Double.parseDouble(actual);
            String yourFormattedString = formatterTwo.format(dos);
            hoy.setText(yourFormattedString);

            TextView antes = (TextView)findViewById(R.id.textView14);
            antes.setTypeface(typeface);
            Double antesdos = Double.parseDouble(anterior);
            String yourFormattedStringTwo = formatterTwo.format(antesdos);
            antes.setText(yourFormattedStringTwo);

            TextView difer = (TextView)findViewById(R.id.lbDiferencia);
            difer.setTypeface(typeface);
//                        Double antestres = Double.parseDouble(diferencia);
            Double antestres = Double.parseDouble(recorridos);
            String formatedThree = formatterTwo.format(antestres);
            difer.setText(formatedThree);

            //Tarifas-------------------------------------------------------------------
            double s1 = Double.parseDouble(feepormktarifa);
            TextView tarifa = (TextView)findViewById(R.id.textView21);
            tarifa.setTypeface(typeface);
            DecimalFormat formattertarifa = new DecimalFormat("$ 0.0000");
            tarifa.setText(formattertarifa.format(s1));

            s1 = Double.parseDouble(datoamount);
            //ending.setText(formatter.format(s1));
            String total="";
            if(datoamount.equalsIgnoreCase("0.0")){
                total="$ 0.00";
            }
            else {
                total=formatter.format(s1);
            }

            if(isUnico){
                TextView basepormes = (TextView)findViewById(R.id.textView19);
                basepormes.setTypeface(typefacebold);
                basepormes.setVisibility(View.VISIBLE);
                TextView t18 = (TextView)findViewById(R.id.textView18);
                t18.setTypeface(typefacebold);
                t18.setVisibility(View.VISIBLE);
                basepormes.setText(total);
            }

            TextView tarifafinal = (TextView)findViewById(R.id.textView24);
            tarifafinal.setTypeface(typefacebold);
            tarifafinal.setText(diferencia);//formatter.format(s1+s3));

            //str = dato.toString().replaceAll("[^\\d]", "");
            //TextView ending = (TextView)dialog.findViewById(R.id.textView27);
            TextView ending2 = (TextView)findViewById(R.id.textView50);
            //ending.setTypeface(typeface);
            ending2.setTypeface(typefacebold);
            ending2.setText(total);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
