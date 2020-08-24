package miituo.com.miituo.threats;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

import miituo.com.miituo.utils.SimpleCallBack;

public class GetTerminosPDFSync extends AsyncTask <String, String, String>{

    Activity c;
    SimpleCallBack cb;
    String resp=null;
    String url="";
    String nombre="";
    public ProgressDialog progress;
    boolean status=false;

    public GetTerminosPDFSync(Activity c, String url, String nombre,SimpleCallBack cb){
        this.c=c;
        this.cb=cb;
        this.nombre=nombre;
        this.url=url;
        progress = new ProgressDialog(c);
    }

    private ProgressDialog progressDialog;

    /**
     * Before starting background thread
     * Show Progress Bar Dialog
     */
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        this.progressDialog = new ProgressDialog(c);
        this.progressDialog.setCancelable(false);
        this.progressDialog.show();
    }

    /**
     * Downloading file in background thread
     */
    @Override
    protected String doInBackground(String... f_url) {
        int count;
        status=false;
        try {
            URL url = new URL(this.url);
            URLConnection connection = url.openConnection();
            connection.connect();
            // getting file length
            int lengthOfFile = connection.getContentLength();

            // input stream to read file - with 8k buffer
            InputStream input = new BufferedInputStream(url.openStream(), 8192);

            //Create androiddeft folder if it does not exist
            File directory = new File(Environment.getExternalStorageDirectory() + File.separator + "Download/");

            if (!directory.exists()) {
                directory.mkdirs();
            }

            // Output stream to write file
            OutputStream output = new FileOutputStream(Environment.getExternalStorageDirectory() + File.separator + "Download/"
                    + nombre+".pdf");

            byte data[] = new byte[1024];

            long total = 0;

//            while ((count = input.read(data)) != -1) {
//                total += count;
//                // publishing the progress....
//                // After this onProgressUpdate will be called
//                publishProgress("" + (int) ((total * 100) / lengthOfFile));
//                // writing data to file
//                output.write(data, 0, count);
//            }

            // flushing output
            output.flush();

            // closing streams
            output.close();
            input.close();
            status=true;
            return "Downloaded at: " + Environment.getExternalStorageDirectory() + File.separator + "Download/"
                    + nombre;

        } catch (Exception e) {
            e.printStackTrace();
            status=false;
        }
        status=false;
        return "Something went wrong";
    }

    /**
     * Updating progress bar
     */
    protected void onProgressUpdate(String... progress) {
        // setting progress percentage
//        progressDialog.setProgress(Integer.parseInt(progress[0]));
    }


    @Override
    protected void onPostExecute(String message) {
        // dismiss the dialog after the file was downloaded
        this.progressDialog.dismiss();
        cb.run(status,"");
    }
}
