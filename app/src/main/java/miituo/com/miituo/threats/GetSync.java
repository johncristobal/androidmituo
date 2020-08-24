package miituo.com.miituo.threats;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONObject;

import miituo.com.miituo.activities.MainActivity;
import miituo.com.miituo.cotizar.ApiClientCotiza;
import miituo.com.miituo.utils.SimpleCallBack;

public class GetSync extends AsyncTask <String, Void, Void>{

    Activity c;
    SimpleCallBack cb;
    String resp=null, url="";
    boolean status=false;
    public ProgressDialog progress;

    public GetSync(Activity c, String url, SimpleCallBack cb){
        this.c=c;
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
    protected Void doInBackground(String... voids) {
        ApiClientCotiza ac=new ApiClientCotiza(c);
        try {
            status=false;
            resp = ac.getDatos(url);
            status=true;
        }catch (Exception e){
            e.printStackTrace();
            resp=e.getMessage();
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
