package miituo.com.miituo.cotizar;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.List;
import miituo.com.miituo.R;

import static miituo.com.miituo.cotizar.PagerAdapter.pager;
import static miituo.com.miituo.cotizar.PagerAdapter.context;

//import static com.miituo.cotizamiituo.cotiza.CotizaActivity.viewPager;

/**
 * Created by miituo on 12/04/18.
 */

public class Datosauto extends Fragment {

    public boolean flagtipo,flaganio,flagmarca,flagmodelo,flagdescripcion;

    public Spinner tipo_vehiculo,anio,marca,subtipo,descripcion;
    List<String> arraySpinnerMarcas;
    List<String> arraySpinnerModelos;
    List<String> arraySpinnerSubtipos;
    List<String> arraySpinnerDescripciones;
    List<String> arraySpinner;

    List<TiposVehiculos> tokenTipos;
    List<ModelosVehiculos> tokenModelos;
    List<MarcasVehiculos> tokenDescripciones;
    List<MarcasVehiculos> tokenMarcas;
    List<MarcasVehiculos> tokenSubtipos;

    Button next;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.datosauto_layout, container, false);

        tipo_vehiculo = v.findViewById(R.id.spinner2);
        anio = v.findViewById(R.id.spinneranio);
        marca = v.findViewById(R.id.spinnermarca);
        subtipo = v.findViewById(R.id.spinnersubtipo);
        descripcion = v.findViewById(R.id.spinnerdescripcion);

        getDataAuto vehiculos = new getDataAuto();
        vehiculos.execute();

        flagtipo = false;
        flaganio = false;
        flagmodelo = false;
        flagmarca = false;
        flagdescripcion = false;

        //button
        next = v.findViewById(R.id.button2);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Log.e("Launch","Show Persona layout");
                pager.setCurrentItem(2);
                if(flagtipo && flaganio && flagmarca && flagmodelo && flagdescripcion){

                    Log.e("Launch","Show Persona layout");
                    //viewPager.setCurrentItem(2);

                }else{
                    Toast.makeText(context,"Selecciona todas las opciones para continuar.",Toast.LENGTH_SHORT).show();
                }
            }
        });
        return v;
    }

//===========================Thread call to fill combo==============================================
    public class getDataAuto extends AsyncTask<String,Void,Void> {

        ProgressDialog progress = new ProgressDialog(context);
        String ErrorCode;

        String[] stringArray;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            arraySpinnerMarcas = new ArrayList<>();
            arraySpinnerSubtipos = new ArrayList<>();
            arraySpinnerDescripciones = new ArrayList<>();

            progress.setTitle("Información");
            progress.setMessage("Recuperando vehículos...");
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

            ApiClientCotiza client = new ApiClientCotiza(context);
            try {
                //recupera datos
                tokenTipos = client.getInfoTipoVehiculos("VehicleTypes");
                tokenModelos = client.getInfoModelosVehiculos("VehicleModel");

                llenarCombo(1);
                llenarCombo(2);

                Log.e("tg","venga");
            } catch (Exception ex) {
                ErrorCode = ex.getMessage();
                this.cancel(true);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            flagtipo = true;
            Adaptador adaptador1 = new Adaptador(context, R.layout.simple_spinner_down,arraySpinner);
            //adaptador1.setDropDownViewResource(R.layout.simple_spinner_down);
            tipo_vehiculo.setAdapter(adaptador1);

            Adaptador adaptador2 = new Adaptador(context, R.layout.simple_spinner_down,arraySpinnerModelos);
            //adaptador2.setDropDownViewResource(R.layout.simple_spinner_down);
            anio.setAdapter(adaptador2);

            tipo_vehiculo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    //Log.e("Tag",arraySpinner[i]);
                }
                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });

            anio.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                    if(i==0)
                        return;

                    flaganio = true;
                    limpiarCombos(1);
                    getDataMarcas marcas = new getDataMarcas();
                    marcas.execute(tokenModelos.get(i).getId()+"","1");
                }
                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });

            limpiarCombos(1);
            progress.dismiss();
        }
    }

//===========================Thread call to fill combo==============================================
    public class getDataMarcas extends AsyncTask<String,Void,Void>{

        ProgressDialog progress = new ProgressDialog(context);
        String ErrorCode;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progress.setTitle("Información");
            progress.setMessage("Recuperando marcas...");
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

            ApiClientCotiza client = new ApiClientCotiza(context);
            try {
                //recupera datos
                tokenMarcas = client.getInfoMarcaVehiculos("VehicleBrands",strings[0],strings[1]);
                llenarCombo(3);

                Log.e("tg","venga");
            } catch (Exception ex) {
                ErrorCode = ex.getMessage();
                this.cancel(true);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            Adaptador adaptador = new Adaptador(context, R.layout.simple_spinner_down,arraySpinnerMarcas);
            //adaptador.setDropDownViewResource(R.layout.simple_spinner_down);
            marca.setAdapter(adaptador);
            marca.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    Log.e("Tag",arraySpinnerMarcas.get(i));

                    if(i==0)
                        return;

                    flagmarca=true;
                    limpiarCombos(2);
                    getDataModelos subtipos = new getDataModelos();
                    subtipos.execute(tokenMarcas.get(i).getId()+"");
                }
                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {
                }
            });

            progress.dismiss();
        }
    }

//===========================Thread call to fill combo==============================================
    public class getDataModelos extends AsyncTask<String,Void,Void>{

        ProgressDialog progress = new ProgressDialog(context);
        String ErrorCode;
        List<MarcasVehiculos> tokenModelos;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progress.setTitle("Información");
            progress.setMessage("Recuperando modelos...");
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
            ApiClientCotiza client = new ApiClientCotiza(context);
            try {
                //recupera datos
                tokenSubtipos = client.getInfoSubtiposVehiculos("VehicleSubTypes/Brand/",strings[0]);
                llenarCombo(4);

                Log.e("tg","venga");
            } catch (Exception ex) {
                ErrorCode = ex.getMessage();
                this.cancel(true);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            Adaptador adaptador = new Adaptador(context, R.layout.simple_spinner_down,arraySpinnerSubtipos);
            //adaptador.setDropDownViewResource(R.layout.simple_spinner_down);
            subtipo.setAdapter(adaptador);
            subtipo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    Log.e("Tag",arraySpinnerSubtipos.get(i));

                    if(i==0)
                        return;

                    flagmodelo=true;
                    limpiarCombos(3);
                    getDataDescripcion descripcion = new getDataDescripcion();
                    descripcion.execute(tokenSubtipos.get(i).getId()+"");
                }
                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });

            progress.dismiss();
        }
    }

//===========================Thread call to fill combo==============================================
    public class getDataDescripcion extends AsyncTask<String,Void,Void>{

        ProgressDialog progress = new ProgressDialog(context);
        String ErrorCode;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progress.setTitle("Información");
            progress.setMessage("Recuperando descripción...");
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

            ApiClientCotiza client = new ApiClientCotiza(context);
            try {
                //recupera datos
                tokenDescripciones = client.getInfoDescripcionVehiculos("VehicleDescriptions/Subtype/",strings[0]);
                llenarCombo(5);

                Log.e("tg","venga");
            } catch (Exception ex) {
                ErrorCode = ex.getMessage();
                this.cancel(true);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            Adaptador adaptador = new Adaptador(context, R.layout.simple_spinner_down,arraySpinnerDescripciones);
            //adaptador.setDropDownViewResource(R.layout.simple_spinner_down);
            descripcion.setAdapter(adaptador);
            descripcion.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    flagdescripcion = true;
                    Log.e("Tag",tokenDescripciones.get(i).getDescription());
                }
                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });

            progress.dismiss();
        }
    }

//===========================Thread call to fill combo==============================================
    public class Adaptador extends ArrayAdapter<String> {

        public List<String> lista;

        public Adaptador(@NonNull Context context, int resource, @NonNull List<String> objects) {
            super(context, resource, objects);

            lista = objects;
        }

        @Override
        public void setDropDownViewResource(int resource) {
            super.setDropDownViewResource(R.layout.simple_spinner_down);
        }

        @Override
        public boolean isEnabled(int position){
            if(position == 0)
            {
                // Disable the first item from Spinner
                // First item will be use for hint
                return false;
            }
            else
            {
                return true;
            }
        }
        @Override
        public View getDropDownView(int position, View convertView, ViewGroup parent) {
            View view = super.getDropDownView(position, convertView, parent);
            TextView tv = (TextView) view;
            if(position == 0){
                // Set the hint text color gray
                tv.setTextColor(Color.GRAY);
            }
            else {
                tv.setTextColor(Color.BLACK);
            }
            return view;
        }
    }

//===========================clear combos==============================================
    public void limpiarCombos(int ind){

        switch (ind) {
            case 1:
                limpiarMarca();
                limpiarSubtipo();
                limpiarDescripction();
                break;
            case 2:
                limpiarSubtipo();
                limpiarDescripction();
                break;
            case 3:
                limpiarDescripction();
                break;
            default:
                break;
        }
    }

    private void llenarCombo(int indice) {

        switch (indice){
            case 1:
                arraySpinner = new ArrayList<>();//[tokenTipos.size()+1];
                arraySpinner.clear();
                arraySpinner.add("Tipo de vehículo");
                for(int i=0; i<tokenTipos.size();i++){
                    arraySpinner.add(tokenTipos.get(i).getDescription());
                }
                tokenTipos.add(0,new TiposVehiculos(-1,""));
                break;
            case 2:
                arraySpinnerModelos = new ArrayList<>();//new String[tokenModelos.size()+1];
                arraySpinnerModelos.clear();
                arraySpinnerModelos.add("Año");
                for(int i=0;i<tokenModelos.size();i++){
                    arraySpinnerModelos.add(tokenModelos.get(i).getModel()+"");
                }
                tokenModelos.add(0,new ModelosVehiculos(-1,-1));
                break;

            case 3:
                arraySpinnerMarcas.clear();
                arraySpinnerMarcas.add("Marca");
                for(int i=0;i<tokenMarcas.size();i++){
                    arraySpinnerMarcas.add(tokenMarcas.get(i).getDescription());
                }
                tokenMarcas.add(0,new MarcasVehiculos(-1,""));
                break;

            case 4:
                arraySpinnerSubtipos.clear();
                arraySpinnerSubtipos.add("Modelo");
                for(int i=0;i<tokenSubtipos.size();i++){
                    arraySpinnerSubtipos.add(tokenSubtipos.get(i).getDescription());
                }
                tokenSubtipos.add(0,new MarcasVehiculos(-1,""));
                break;

            case 5:
                arraySpinnerDescripciones.clear();
                arraySpinnerDescripciones.add("Descripción");
                for(int i=0;i<tokenDescripciones.size();i++){
                    arraySpinnerDescripciones.add(tokenDescripciones.get(i).getDescription());
                }
                tokenDescripciones.add(0,new MarcasVehiculos(-1,""));
                break;
            default:
                break;
        }
    }

    public void limpiarMarca(){
        arraySpinnerMarcas.clear();
        arraySpinnerMarcas.add("Marca");
        Adaptador adaptador1 = new Adaptador(context, R.layout.simple_spinner_down,arraySpinnerMarcas);
        //adaptador1.setDropDownViewResource(R.layout.simple_spinner_down);
        marca.setAdapter(adaptador1);
    }

    public void limpiarSubtipo(){
        arraySpinnerSubtipos.clear();
        arraySpinnerSubtipos.add("Modelo");
        Adaptador adaptador1 = new Adaptador(context, R.layout.simple_spinner_down,arraySpinnerSubtipos);
        //adaptador1.setDropDownViewResource(R.layout.simple_spinner_down);
        subtipo.setAdapter(adaptador1);
    }

    public void limpiarDescripction(){
        arraySpinnerDescripciones.clear();
        arraySpinnerDescripciones.add("Descripción");
        Adaptador adaptador1 = new Adaptador(context, R.layout.simple_spinner_down,arraySpinnerDescripciones);
        //adaptador1.setDropDownViewResource(R.layout.simple_spinner_down);
        descripcion.setAdapter(adaptador1);

    }


}
