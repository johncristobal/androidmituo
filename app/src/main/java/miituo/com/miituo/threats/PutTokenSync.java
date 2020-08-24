package miituo.com.miituo.threats;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import com.google.gson.Gson;

import miituo.com.miituo.api.ApiClient;
import miituo.com.miituo.data.ClientMovil;
import miituo.com.miituo.utils.LogHelper;
import miituo.com.miituo.utils.SimpleCallBack;

public class PutTokenSync extends AsyncTask<String, Void, Void> {

    Context c;
    SimpleCallBack cb;
    ClientMovil cli;
    String ErrorCode=null, url="";
    boolean status=false;
    public ProgressDialog progress;
    String tokencliente;

    public PutTokenSync(String url, Context c, ClientMovil cli, String tokencliente, SimpleCallBack cb){
        this.c = c;
        this.cb=cb;
        this.url=url;
        this.cli = cli;
        this.tokencliente = tokencliente;
        progress = new ProgressDialog(c);
    }

    /*@Override
    protected void onPreExecute() {
        progress.setMessage("Consultando información");
        progress.setIndeterminate(true);
        progress.setCancelable(false);
        progress.show();
    }*/

    @Override
    protected Void doInBackground(String... strings) {
        ApiClient client = new ApiClient(c);
        try {
            status=false;
            Gson parset=new Gson();
            String result=parset.toJson(cli);
            LogHelper.log(c,LogHelper.backTask,"SyncActivity.sendToken","inicio envio de cliente",
                    result, "","","");
            client.updateToken("ClientUser", cli, tokencliente);
            status=true;

        }catch (Exception e){
            e.printStackTrace();
            ErrorCode=e.getMessage();
            status=false;
            this.cancel(true);
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        if(progress!=null){
            progress.dismiss();
        }
        cb.run(status,ErrorCode);
    }

    @Override
    protected void onCancelled() {
        super.onCancelled();

        if(progress!=null){
            progress.dismiss();
        }

        cb.run(status,ErrorCode);
    }
}

