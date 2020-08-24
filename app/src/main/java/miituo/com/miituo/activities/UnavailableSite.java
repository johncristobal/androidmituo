package miituo.com.miituo.activities;

import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;
import java.util.regex.Pattern;

import miituo.com.miituo.R;
import miituo.com.miituo.threats.AvailableSiteSync;
import miituo.com.miituo.threats.GeneratingCallSync;
import miituo.com.miituo.utils.SimpleCallBack;

public class UnavailableSite extends AppCompatActivity {
    String status="0";
    int idQuot=0;
    ImageView imgTop, imgBottom;
    TextView lb1,lb2,lb3,lb4;
    EditText et1,et2,et3;
    String name="",tel="",email="",hora="";
    RelativeLayout cntBack;
    private Typeface tipo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_unavailable_site);
        cntBack=(RelativeLayout)findViewById(R.id.cntBack);
        imgBottom=(ImageView)findViewById(R.id.imgBottom);
        imgTop=(ImageView)findViewById(R.id.imgTop);
        tipo = Typeface.createFromAsset(getAssets(), "fonts/herne1.ttf");
        lb1=(TextView)findViewById(R.id.lb1);
        lb2=(TextView)findViewById(R.id.lb2);
        lb3=(TextView)findViewById(R.id.lb3);
        et1=(EditText)findViewById(R.id.et1);
        et2=(EditText)findViewById(R.id.et2);
        et3=(EditText)findViewById(R.id.et3);
        lb4=(TextView) findViewById(R.id.et4);
        et1.setFilters(new InputFilter[]{new InputFilter.LengthFilter(20)});
        et1.setInputType(InputType.TYPE_CLASS_TEXT);
        et1.setTypeface(tipo);
        et2.setFilters(new InputFilter[]{new InputFilter.LengthFilter(10)});
        et2.setInputType(InputType.TYPE_CLASS_PHONE);
        et2.setTypeface(tipo);
        et3.setFilters(new InputFilter[]{new InputFilter.LengthFilter(40)});
        et3.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
        et3.setTypeface(tipo);
        lb1.setTypeface(tipo);
        lb2.setTypeface(tipo);
        lb3.setTypeface(tipo);
        lb4.setTypeface(tipo);
        status=getIntent().getStringExtra("status");
        idQuot=getIntent().getIntExtra("idQuot",0);
        valida();
        lb1.setText("Gracias por tu interés en contratar con nosotros," +
                " en éste momento hacemos algunas actualizaciones.Podrás continuar el día de mañana a partir de las 07:00 a.m." +
                "\n\nDéjanos tus datos y con gusto te ayudaremos a contratar tu seguro:");
        lb3.setText("No olvides al momento de contratar tener a la mano el número de serie de tu vehiculo, " +
                "lo puedes encontrar en la factura o tarjeta de circulación");
    }

    private void valida(){
        if(status.equalsIgnoreCase("1")){
            if(isFin()){
                cntBack.setBackgroundColor(Color.parseColor("#FFFFFF"));
                lb1.setTextColor(Color.parseColor("#000000"));
                lb2.setTextColor(Color.parseColor("#000000"));
                lb3.setTextColor(Color.parseColor("#000000"));
                imgTop.setImageDrawable(getResources().getDrawable(R.drawable.back_sol));
                imgBottom.setImageDrawable(getResources().getDrawable(R.drawable.back_fin_bottom));
            }
            else{
                cntBack.setBackgroundColor(Color.parseColor("#000000"));
                lb1.setTextColor(Color.parseColor("#FFFFFF"));
                lb2.setTextColor(Color.parseColor("#FFFFFF"));
                lb3.setTextColor(Color.parseColor("#FFFFFF"));
                imgTop.setImageDrawable(getResources().getDrawable(R.drawable.back_luna));
                imgBottom.setImageDrawable(getResources().getDrawable(R.drawable.back_noche_bottom));
            }
        }
        else{
            cntBack.setBackgroundColor(Color.parseColor("#FFFFFF"));
            lb1.setTextColor(Color.parseColor("#000000"));
            lb2.setTextColor(Color.parseColor("#000000"));
            lb3.setTextColor(Color.parseColor("#000000"));
            imgTop.setImageDrawable(getResources().getDrawable(R.drawable.back_sol));
            imgBottom.setImageDrawable(getResources().getDrawable(R.drawable.back_fix_bottom));
        }
    }

    private boolean isFin(){
        boolean fin=false;
        Calendar c= Calendar.getInstance();
        if(c.get(Calendar.DAY_OF_WEEK)==Calendar.SATURDAY || c.get(Calendar.DAY_OF_WEEK)==Calendar.SUNDAY){
            fin=true;
        }
        return fin;
    }

    public AlertDialog alertaCotiz;
    public void enviar(View v){
        if(validaDatos()) {
            new GeneratingCallSync(this, name,tel,email,hora,idQuot,new SimpleCallBack() {
                @Override
                public void run(boolean status, String res) {

                    if (!status) {
                        final AlertDialog.Builder builder = new AlertDialog.Builder(UnavailableSite.this);
                        builder.setTitle("Alerta");
                        builder.setMessage("Error de conexión");
                        builder.setCancelable(false);
                        builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                alertaCotiz.dismiss();
                            }
                        });
                        alertaCotiz = builder.create();
                        alertaCotiz.show();
                    } else {
                        if (res.equalsIgnoreCase("true")) {
                            final AlertDialog.Builder builder = new AlertDialog.Builder(UnavailableSite.this);
                            builder.setTitle("Información");
                            builder.setMessage("Llamada agendada.");
                            builder.setCancelable(false);
                            builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    alertaCotiz.dismiss();
                                    finish();
                                }
                            });
                            alertaCotiz = builder.create();
                            alertaCotiz.show();
                        }
                        else{
                            final AlertDialog.Builder builder = new AlertDialog.Builder(UnavailableSite.this);
                            builder.setTitle("Alerta");
                            builder.setMessage("No se pudo agendar la llamada, intenta nuevamente.");
                            builder.setCancelable(false);
                            builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    alertaCotiz.dismiss();
                                }
                            });
                            alertaCotiz = builder.create();
                            alertaCotiz.show();
                        }
                    }
                }
            }).execute();
        }
    }

    private boolean validaDatos(){
        name=et1.getText().toString();
        tel=et2.getText().toString();
        email=et3.getText().toString();
        if(!isDatoValido(name)){
            Toast.makeText(this,"Verifica el tu nombre",Toast.LENGTH_LONG).show();
            return false;
        }
        if(!isDatoValido(tel) || tel.length()!=10){
            Toast.makeText(this,"Verifica el tu número celular",Toast.LENGTH_LONG).show();
            return false;
        }
        if(!isDatoValido(email) || !isValidEmail(email)){
            Toast.makeText(this,"Verifica el tu email",Toast.LENGTH_LONG).show();
            return false;
        }
        if(!isDatoValido(hora)){
            Toast.makeText(this,"Verifica el la hora",Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

    private boolean isDatoValido(String dato){
        if(dato==null || dato.equalsIgnoreCase("")){
            return false;
        }
        return true;
    }

    private boolean isValidEmail(String s){
        Pattern sPattern = Pattern.compile("^[a-zA-Z0-9_.+-]+@+[a-zA-Z0-9-]+.+[a-zA-Z0-9]{2,4}");
        return sPattern.matcher(s).matches();
    }

    public void getTime(View v){
        TimePickerDialog recogerHora = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                //Formateo el hora obtenido: antepone el 0 si son menores de 10
                String horaFormateada =  (hourOfDay < 10)? String.valueOf("0" + hourOfDay) : String.valueOf(hourOfDay);
                //Formateo el minuto obtenido: antepone el 0 si son menores de 10
                String minutoFormateado = (minute < 10)? String.valueOf("0" + minute):String.valueOf(minute);
                //Obtengo el valor a.m. o p.m., dependiendo de la selección del usuario
                String AM_PM;
                if(hourOfDay < 12) {
                    AM_PM = "a.m.";
                } else {
                    AM_PM = "p.m.";
                }
                hora=horaFormateada + ":" + minutoFormateado;

                //Muestro la hora con el formato deseado
                lb4.setText( hora+ " " + "hrs.");
            }
            //Estos valores deben ir en ese orden
            //Al colocar en false se muestra en formato 12 horas y true en formato 24 horas
            //Pero el sistema devuelve la hora en formato 24 horas
        }, 8,0, true);

        recogerHora.show();
    }
}
