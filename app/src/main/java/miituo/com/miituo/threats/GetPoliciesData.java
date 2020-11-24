package miituo.com.miituo.threats;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.List;

import miituo.com.miituo.activities.SyncActivity;
import miituo.com.miituo.api.ApiClient;
import miituo.com.miituo.cotizar.ApiClientCotiza;
import miituo.com.miituo.data.InfoClient;
import miituo.com.miituo.utils.LogHelper;
import miituo.com.miituo.utils.SimpleCallBack;

public class GetPoliciesData extends AsyncTask<String, Void, Void> {

    Context c;
    SimpleCallBack cb;
    String ErrorCode=null, url="";
    boolean status=false;

    public ProgressDialog progress;

    public GetPoliciesData(String url, Context c, SimpleCallBack cb){
        this.c = c;
        this.cb=cb;
        this.url=url;
        progress = new ProgressDialog(c);
    }

    @Override
    protected void onPreExecute() {
        progress.setMessage("Consultando información");
        progress.setIndeterminate(true);
        progress.setCancelable(false);
        progress.show();
    }

    @Override
    protected Void doInBackground(String... strings) {
        ApiClient client = new ApiClient(c);
        try {
            status=false;
            ErrorCode = client.getInfoClientData(url, c);
            if (ErrorCode.contains("error@")) {
                status=false;
                //ErrorCode = "Estamos haciendo algunas actualizaciones, si sigues teniendo problemas para ingresar, contáctate con nosotros.";
                this.cancel(true);
            } else {
                status=true;
            }
            //resp = ac.getDatos(url);
            //Gson parset=new Gson();
            //String result=parset.toJson(strings[0]);
            //LogHelper.log(c,LogHelper.backTask,"SyncActivity.sendToken","inicio envio de cliente", result, "","","");
            //client.updateToken("ClientUser", strings[0], tokencliente);
        }catch (Exception e){
            e.printStackTrace();
            ErrorCode=e.getMessage();
            status=false;
            this.cancel(true);
        }

//        try {
//            Gson parset=new Gson();
//            String result=parset.toJson(strings[0]);
//            LogHelper.log(c,LogHelper.backTask,"SyncActivity.sendToken","inicio envio de cliente", result, "","","");
//            client.updateToken("ClientUser", strings[0], tokencliente);
//        } catch (Exception ex) {
//            ErrorCode = ex.getMessage();
//            this.cancel(true);
//        }
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        try {
            if (progress != null) {
                progress.dismiss();
            }
            cb.run(status, ErrorCode);
        }catch(Exception e){
            e.printStackTrace();
            cb.run(status, ErrorCode);
        }
    }

    @Override
    protected void onCancelled() {
        super.onCancelled();

        if(progress!=null){
            progress.dismiss();
        }

        cb.run(status,ErrorCode);

        /*if (ErrorCode != null) {
            if (ErrorCode.equalsIgnoreCase("El número proporcionado no existe en los registros")) {
                AlertDialog.Builder builder = new AlertDialog.Builder(SyncActivity.this);
                builder.setTitle("Atención");
                builder.setMessage("No tienes pólizas registradas. Vuelve a cotizar.");
                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        SharedPreferences.Editor editor = app_preferences.edit();
                        editor.putString("Celphone", "");
                        editor.putString("sesion", "");
                        editor.commit();

                        //Intent intent = new Intent(SyncActivity.this,MainActivity.class);
                        //startActivity(intent);
                    }
                });

                AlertDialog alerta = builder.create();
                alerta.show();
            } else {
                Toast message = Toast.makeText(getApplicationContext(), ErrorCode, Toast.LENGTH_LONG);
                message.show();
            }
        }*/

    }
}
