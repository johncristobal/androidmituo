package miituo.com.miituo.threats;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONObject;

import miituo.com.miituo.activities.CotizarAutoActivity;
import miituo.com.miituo.activities.DetallePlanActivity;
import miituo.com.miituo.activities.MainActivity;
import miituo.com.miituo.cotizar.ApiClientCotiza;
import miituo.com.miituo.utils.SimpleCallBack;

public class AvailableSiteSync extends AsyncTask <String, Void, Void> {

    Activity c;
    SimpleCallBack cb;
    String resp=null;
    boolean status=false;
    public ProgressDialog progress;

    public AvailableSiteSync(Activity c, SimpleCallBack cb) {
        this.c=c;
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
            MainActivity.isRecorderEnabled = ac.getDatos("Parameters/GetParameterByName/AndroidKey");
            if(c.getClass().getSimpleName().equalsIgnoreCase(CotizarAutoActivity.class.getSimpleName()) ||
               c.getClass().getSimpleName().equalsIgnoreCase(DetallePlanActivity.class.getSimpleName())) {
                resp = ac.getDatos("Parameters/GetParameterByName/Cotizando");
            JSONArray array=new JSONArray(resp);
            JSONObject o= array.getJSONObject(0);
            resp=o.getString("Valor");
            }else {
                resp = "0";
            }
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
