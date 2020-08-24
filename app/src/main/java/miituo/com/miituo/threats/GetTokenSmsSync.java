package miituo.com.miituo.threats;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import miituo.com.miituo.api.ApiClient;
import miituo.com.miituo.utils.SimpleCallBack;

public class GetTokenSmsSync extends AsyncTask<String, Void, Void> {

    Context c;
    SimpleCallBack cb;
    String ErrorCode=null, url="";
    boolean status=false;

    public ProgressDialog progress;

    public GetTokenSmsSync(String url, Context c, SimpleCallBack cb){
        this.c = c;
        this.cb=cb;
        this.url=url;
        progress = new ProgressDialog(c);
    }

    @Override
    protected void onPreExecute() {
        progress.setMessage("Recuperando token...");
        progress.setIndeterminate(true);
        progress.setCancelable(false);
        progress.show();
    }

    @Override
    protected Void doInBackground(String... strings) {
        ApiClient client = new ApiClient(c);
        try {
            status=false;
            ErrorCode = client.getToken(url);
            if (ErrorCode.contains("error@")) {
                status=false;
                //ErrorCode = "Estamos haciendo algunas actualizaciones, si sigues teniendo problemas para ingresar, contáctate con nosotros.";
                this.cancel(true);
            } else {
                status=true;
            }
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
