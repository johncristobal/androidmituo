package miituo.com.miituo.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.annotation.NonNull;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import static miituo.com.miituo.activities.CotizarAutoActivity.cotizaTipos;
import static miituo.com.miituo.activities.CotizarAutoActivity.datosAuto;
import static miituo.com.miituo.activities.CotizarAutoActivity.datosMios;
import miituo.com.miituo.R;
import miituo.com.miituo.cotizar.ApiClientCotiza;
import miituo.com.miituo.cotizar.CotizaAdapter;

public class PlanesActivityTab extends AppCompatActivity {

    public LayoutInflater factory;
    public View planesView,misdatos,autodatos;

    private RelativeLayout mainView;
    public ListView planes;

    public Context context;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.cotizacion:
                    //mTextMessage.setText(R.string.title_home);

                    mainView.removeAllViews();
                    mainView.addView(planesView);

                    return true;
                case R.id.auto:
                    //mTextMessage.setText(R.string.title_dashboard);
                    mainView.removeAllViews();
                    mainView.addView(autodatos);

                    return true;
                case R.id.datos:
                    //mTextMessage.setText(R.string.title_notifications);
                    mainView.removeAllViews();
                    mainView.addView(misdatos);

                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_planes2);

        mainView = findViewById(R.id.view);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        context = this;

        factory = LayoutInflater.from(PlanesActivityTab.this);

        //load layout for planesView
        planesView = factory.inflate(R.layout.content_planes, null);
        planes = planesView.findViewById(R.id.listaplanes);
        CotizaAdapter adapter = new CotizaAdapter(PlanesActivityTab.this,cotizaTipos.getRateList(),null);
        planes.setAdapter(adapter);
        Button enviarCorreo = planesView.findViewById(R.id.button4);
        enviarCorreo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText nombre = planesView.findViewById(R.id.editTextnombre);
                EditText correo = planesView.findViewById(R.id.editTextCorreo);
                EditText telefono = planesView.findViewById(R.id.editTextTelefono);

                //validate info
                String name = nombre.getText().toString();
                String mail = correo.getText().toString();
                String tel = telefono.getText().toString();

                //name = "Jijo";
                //mail = "john.cristobal@miituo.com";

                sendMailCotizar send = new sendMailCotizar();
                send.execute(name, mail,tel);
                /*if(name.equals("")){
                    Toast.makeText(context,"Ingresa tu nombre.",Toast.LENGTH_SHORT).show();
                }else if(mail.equals("")){
                    Toast.makeText(context,"Ingresa tu correo.",Toast.LENGTH_SHORT).show();
                }else {
                    sendMailCotizar send = new sendMailCotizar();
                    send.execute(name, mail);
                }*/
            }
        });

        ImageView contacto = planesView.findViewById(R.id.imageView4);
        contacto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(context, ContactaActivity.class);
                //Intent i = new Intent(this, DetallesPlanesActivity.class);
                startActivity(i);

            }
        });

        ImageView back = planesView.findViewById(R.id.imageView7);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        //load data mis datos
        misdatos = factory.inflate(R.layout.content_misdatos, null);
        TextView datos0 = misdatos.findViewById(R.id.textViewDate);
        TextView datos1 = misdatos.findViewById(R.id.textViewSexo);
        TextView datos2 = misdatos.findViewById(R.id.textViewPostal);
        TextView datos3 = misdatos.findViewById(R.id.textViewGaraje);

        //loada data mi auto
        autodatos = factory.inflate(R.layout.content_autodatos, null);
        TextView autos0 = autodatos.findViewById(R.id.textViewAnio);
        TextView autos1 = autodatos.findViewById(R.id.textVieMarca);
        TextView autos2 = autodatos.findViewById(R.id.textViewModelo);
        TextView autos3 = autodatos.findViewById(R.id.textViewDescripcion);

        autos0.setText(datosAuto[0]);
        autos1.setText(datosAuto[1]);
        autos2.setText(datosAuto[2]);
        autos3.setText(datosAuto[3]);

        datos0.setText(datosMios[0]);
        datos1.setText(datosMios[1]);
        datos2.setText(datosMios[2]);
        datos3.setText(datosMios[3]);

        mainView.removeAllViews();
        mainView.addView(planesView);
    }

    //===========================Thread send mail=======================================================
    public class sendMailCotizar extends AsyncTask<String,Void,Void> {

        ProgressDialog progress = new ProgressDialog(PlanesActivityTab.this);
        String ErrorCode;
        String res;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progress.setTitle("Información");
            progress.setMessage("Enviando correo...");
            //progress.setIcon(R.drawable.miituo);
            progress.setIndeterminate(true);
            progress.setCancelable(false);
            progress.show();
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
            progress.dismiss();
            Toast message = Toast.makeText(context, ErrorCode, Toast.LENGTH_LONG);
            message.show();
        }

        @Override
        protected Void doInBackground(String... strings) {

            ApiClientCotiza client = new ApiClientCotiza(PlanesActivityTab.this);
            try {
                //recupera datos
                res = client.updateQuotation("Quotation",cotizaTipos.getId(),strings[0],strings[1],strings[2]);
                res = client.sendMailQuotation("Quotation/Email",1,strings[1]);

            } catch (Exception ex) {
                ErrorCode = ex.getMessage();
                this.cancel(true);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            if(res != null){
                if(!res.equals("")){

                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setTitle("");
                    builder.setMessage("La cotización ha sido enviada a su correo electrónico");
                    builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //Intent i = new Intent(LastOdometerActivity.this, PrincipalActivity.class);
                            //startActivity(i);
                        }
                    });
                    AlertDialog alerta = builder.create();
                    alerta.show();

                }else{
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setTitle("");
                    builder.setMessage("Tuvimos un problema, intente más tarde.");
                    builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //Intent i = new Intent(LastOdometerActivity.this, PrincipalActivity.class);
                            //startActivity(i);
                        }
                    });
                    AlertDialog alerta = builder.create();
                    alerta.show();
                }
            }else{

            }

            progress.dismiss();
        }
    }
}
