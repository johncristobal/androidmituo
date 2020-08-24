package miituo.com.miituo.threats;

import android.app.Activity;
import android.app.DownloadManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.core.content.FileProvider;

import java.io.File;

import miituo.com.miituo.R;
import miituo.com.miituo.activities.AseguradorasActivity;
import miituo.com.miituo.activities.ContratarActivity;
import miituo.com.miituo.activities.PDFViewer;
import miituo.com.miituo.activities.SyncActivity;
import miituo.com.miituo.api.ApiClient;
import miituo.com.miituo.data.IinfoClient;
import miituo.com.miituo.utils.SimpleCallBack;

public class GetPDFSync extends AsyncTask <String, Void, Void>{

    Activity c;
    SimpleCallBack cb;
    String resp=null;
    int id=0;
    boolean isPoliza=true;
    String noPoliza="poliza";
    public AlertDialog alertaPago;
    public ProgressDialog progress;

    public GetPDFSync(Activity c, boolean isPoliza, int id, String noPoliza, SimpleCallBack cb){
        this.c=c;
        this.id=id;
        this.noPoliza=noPoliza;
        this.cb=cb;
        this.isPoliza=isPoliza;
        progress = new ProgressDialog(c);
    }
    @Override
    protected void onPreExecute() {
        progress.setMessage("Descargando documento");
        progress.setIndeterminate(true);
        progress.setCancelable(false);
        progress.show();
    }
    @Override
    protected Void doInBackground(String... voids) {
        ApiClient ac=new ApiClient(c);
        if(isPoliza) {
            resp = ac.getPDF(c,"Policy/getReport/"+ id, noPoliza);
        }else{
            resp = ac.getPDF(c, "Policy/getStatement/"+id, noPoliza+"_"+IinfoClient.getInfoClientObject().getPolicies().getMensualidad());
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        if(progress!=null){
            progress.dismiss();
        }
        if(resp==null){
            cb.run(false,null);
        }
        else{
            cb.run(true,"OK");
        }
    }

    public static Intent openFile(Context context) {
        File f=new File("/sdcard/Download/poliza.pdf");
        Uri u= FileProvider.getUriForFile(context, "miituo.com.miituo.provider",f);
        String type = "application/pdf";

//        Uri path = Uri.fromFile(f);
        Uri path = u;
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Intent chooser = Intent.createChooser(intent, "poliza descargada");
        intent.setDataAndType(path, type);
        return chooser;
    }
}
