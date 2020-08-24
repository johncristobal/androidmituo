package miituo.com.miituo.threats;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONObject;

import miituo.com.miituo.cotizar.ApiClientCotiza;
import miituo.com.miituo.utils.SimpleCallBack;

public class GeneratingCallSync extends AsyncTask <String, Void, Void>{

    Activity c;
    SimpleCallBack cb;
    String resp=null;
    String name="",tel="",email="",hora="";
    boolean status=false;
    int idQuot=0;
    public ProgressDialog progress;

    public GeneratingCallSync(Activity c, String name,String tel,String email,String hora,int idQuot, SimpleCallBack cb){
        this.c=c;
        this.name=name;
        this.tel=tel;
        this.email=email;
        this.hora=hora;
        this.idQuot=idQuot;
        this.cb=cb;
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
    protected Void doInBackground(String... voids) {
        ApiClientCotiza ac=new ApiClientCotiza(c);
        try {
            status=false;
            resp = ac.generatingCall("Poll/PreCotization",name,tel,email,hora,idQuot,2);
            status=true;
        }catch (Exception e){
            e.printStackTrace();
            resp=null;
            status=false;
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        if(progress!=null){
            progress.dismiss();
        }
            cb.run(status,resp);
    }
}
