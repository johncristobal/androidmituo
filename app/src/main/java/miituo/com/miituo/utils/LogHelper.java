package miituo.com.miituo.utils;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.QueryBuilder;

import org.json.JSONArray;
import org.json.JSONObject;

import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import miituo.com.miituo.activities.PrincipalActivity;
import miituo.com.miituo.activities.WebActivity;
import miituo.com.miituo.api.ApiClient;
import miituo.com.miituo.cotizar.ApiClientCotiza;
import miituo.com.miituo.db.DataBaseHelper;
import miituo.com.miituo.db.Logger;

public class LogHelper{
    public static final String request = "AND_REQUEST";
    public static final String user_interaction = "AND_USER_INTERACTION";
    public static final String backTask = "AND_PROCESS_INTO_THREAD";
    public static Context context;
    public static int idPolicy = 0;

    public static void log(Context c, String event, String url, String msg, String input, String output, String datosPoliza, String excepcion) {
        Dao<Logger, Integer> logDAO = getHelper(c).getDaoLogger();
        try {
            Logger l = new Logger(event, url, msg, input, output, datosPoliza, excepcion);
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
            String strDate = dateFormat.format(new Date());
            l.setDate(strDate);
            logDAO.create(l);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static JSONObject getLog(Context c, int idPolicy) {
        Dao<Logger, Integer> logDAO = getHelper(c).getDaoLogger();
        JSONObject js = new JSONObject();
        try {
            List<Logger> list = logDAO.queryForAll();
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
            String strDate = dateFormat.format(new Date());
            // js.put("Id",idPolicy);
            js.put("DownLoad", strDate);
            JSONArray arr = new JSONArray();
            for (Logger l : list) {
                JSONObject j = new JSONObject();
                j.put("Id", l.getId());
                j.put("Event", l.getEvent() + " - " + l.getMsg() + " - " + l.getInshuranceData());
                j.put("Url", l.getUrl());
                j.put("Input", l.getInput());
                j.put("Output", l.getOutput());
                j.put("Exception", l.getExcepcion());
                j.put("Date", l.getDate());
                arr.put(j);
            }
            js.put("Regs", arr);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return js;
    }

    public static void clearLog(Context c) {
        Dao<Logger, Integer> logDAO = getHelper(c).getDaoLogger();
        try {
            List<Logger> list = logDAO.queryForAll();
            logDAO.delete(list);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static DataBaseHelper getHelper(Context c) {
        DataBaseHelper databaseHelper = OpenHelperManager.getHelper(c, DataBaseHelper.class);
        databaseHelper.setContexto(c);
        return databaseHelper;
    }

    public static void sendLog(final Context c, final int idPolicy) {
        LogHelper.idPolicy = idPolicy;
        JSONObject js = getLog(c, idPolicy);
        Log.d("sendLog0", "inicio de envio de log, primera vez");
        new SendLogTask(c, "" + idPolicy, js.toString(), new SimpleCallBack() {
            @Override
            public void run(boolean status, String res) {
                KeyChainStore ks=new KeyChainStore(c);
                if (res!=null && res.equalsIgnoreCase("true")) {
                    ks.storeValueForKey("logPending","0");
                    Alarma a = new Alarma();
                    a.cancelAlarm(c);
                    clearLog(c);
                } else {
                    Alarma a = new Alarma();
                    ks.storeValueForKey("logPending","1");
                    ks.storeValueForKey("idPending",""+idPolicy);
                    a.setAlarm(c, "", "", 60000, cb);
                }
            }
        }).execute();

    }

    public static SimpleCallBack cb = new SimpleCallBack() {

    @Override
    public void run(boolean status, final String res) {
        Log.d("sending log", "en callback true=termino alarma / false=logSent\n==============================\n" +
                " status: " + status + " res: " + res);
        final Alarma a = new Alarma();
        a.cancelAlarm(context);
        if (status) {
            new SendLogTask(context, "" + idPolicy, getLog(context, idPolicy).toString(), cb).execute();
        } else {
            KeyChainStore ks=new KeyChainStore(context);
            if (res != null && res.equalsIgnoreCase("true")) {
                Log.d("sending log", "resp: " + res);
                ks.storeValueForKey("logPending","0");
                Alarma ab = new Alarma();
                ab.cancelAlarm(context);
                clearLog(context);
            } else {
                ks.storeValueForKey("logPending","1");
                ks.storeValueForKey("idPending",""+idPolicy);
                a.setAlarm(context, "", "", 60000, cb);
            }
        }
    }
};
    //===========================Thread call to fill combo==============================================
    public static class SendLogTask extends AsyncTask<String, Void, Void> {

        String ErrorCode, res,finalResp="";
        String log = "",idPolicy="0";
        SimpleCallBack cb;
        Context c;

        public SendLogTask(Context c,String idPolicy, String log,SimpleCallBack cb) {
            this.log = log;
            this.cb = cb;
            this.c=c;
            this.idPolicy=idPolicy;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
        }

        @Override
        protected Void doInBackground(String... strings) {

            ApiClient client = new ApiClient(c);
            try {
                    this.res = client.postDatos("PolicyAppLog/Policy/" + idPolicy, log);
                    finalResp = this.res;
            } catch (Exception ex) {
                ErrorCode = ex.getMessage();
                finalResp=ErrorCode;
//                    cb.run(false, ErrorCode);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
                cb.run(false,finalResp);
        }
    }

    public static String getException(Exception e){
        String ex="";
        if(e!=null){
            ex=e.getMessage();
            StackTraceElement[] list=e.getStackTrace();
            if(list!=null && list.length>0){
                for(int i=0;i<list.length;i++){
                    if(list[i].getClassName().contains("miituo")){
                        ex+="\n"+list[i].getClassName()+" :: "+list[i].getMethodName()+" :: "+list[i].getLineNumber();
                        break;
                    }
                }
            }
        }
        return ex;
    }

}
