package miituo.com.miituo.threats;
import android.app.Activity;
import android.app.ProgressDialog;

import org.json.JSONArray;
import org.json.JSONObject;

import miituo.com.miituo.activities.MainActivity;
import miituo.com.miituo.api.ApiClient;
import miituo.com.miituo.utils.SimpleCallBack;

import android.os.AsyncTask;

import java.io.IOException;

//Clase que hereda de hilo para hacer un llamado al servidor...
public class GetCuponAsync extends AsyncTask<String, Void, Void>{


    Activity c;
    SimpleCallBack cb;
    String resp=null, url="";
    boolean status = false;
    public ProgressDialog progress;

    //Constructor
    public GetCuponAsync (Activity c, String url, SimpleCallBack cb) {
        this.c = c;
        this.url = url;
        this.cb = cb;
        progress = new ProgressDialog(c);
    }

    //preExecute: metodo de la familia AsyncTask
    // funciona para inicializar datos antes de ejecutar el hilo...
    /*protected void onPreExecute() {
        progress.setMessage("Consultando información");
        progress.setIndeterminate(true);
        progress.setCancelable(false);
        progress.show();
    }*/

    //DoInBackGround: en este metodo se hace la peticion  a la api
    //y hace el llamado al servidor
    @Override
    protected Void doInBackground(String... voids) {
        ApiClient ac = new ApiClient(c);
        try{
            status = false;
            resp = ac.getCupon(url);
            status = true;
        }
        catch (IOException e) {
            e.printStackTrace();
            resp = e.getMessage();
            status = false;
        }
        return null;
    }

    //PostExecute: Metodo que llama al callBack para mandar a llamar al hilo mprincipal y realizar
    //la funcion...
    @Override
    protected void onPostExecute(Void aVoid) {
        if(progress!=null){
            progress.dismiss();
        }
        cb.run(status,resp);
    }
}
