package miituo.com.miituo.threats;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.stmt.QueryBuilder;

import java.sql.SQLException;
import java.util.Collections;
import java.util.List;

import miituo.com.miituo.api.ApiClient;
import miituo.com.miituo.db.DataBaseHelper;
import miituo.com.miituo.db.EnviosPendientes;
import miituo.com.miituo.db.Policies;
import miituo.com.miituo.utils.AlarmaForPendingShipments;

public class SincroEnviosPendientes extends AsyncTask<Void, Void, Void> {
    String ErrorCode="";
    Context context;
    public SincroEnviosPendientes(Context c){
        context=c;
    }
    @Override
    protected void onPreExecute() {
        try {
            List<EnviosPendientes> ep = getHelper().getDaoEnviosPendientes().queryForAll();
            if(ep!=null && ep.size()>0){
                Collections.sort(ep);
                ApiClient api=new ApiClient(context);
                for(int i=0; i<ep.size();i++){
                    EnviosPendientes e=ep.get(i);
                    QueryBuilder<Policies, Integer> relacionBuilder = getHelper().getDaoPolicies().queryBuilder();
                    relacionBuilder.where().eq("Id", e.getIdPoliza());
                    List<Policies> cola = getHelper().getDaoPolicies().query(relacionBuilder.prepare());
                    if (cola.isEmpty()) {
                        continue;
                    }
                    if(e.getTipoEnvio()==0){
                        BitmapFactory.Options options = new BitmapFactory.Options();
                        options.inSampleSize = 4;
                        String uri = e.getUri();
                        Bitmap bit = BitmapFactory.decodeFile(uri, options);

                        int a = api.UploadPhoto(e.getPictureType(), bit, e.getRequest(),
                                cola.get(0).getClientToken(),"","0","0","");
//                        int a = api.UploadPhoto(e.getPictureType(), bit, e.getRequest(),
//                                cola.get(0).getClientToken(),e.getIdPoliza(), cola.get(0).getNoPolicy());
                        if (a <= 4 && a != 0) {
                            //subio correcto
                            getHelper().getDaoEnviosPendientes().delete(e);
                        } else {
                            continue;
                        }
                    }
                    else if(e.getTipoEnvio()==1){
                        boolean response = api.ConfirmOdometer(e.getPictureType(), e.getRequest(),cola.get(0).getClientToken()).booleanValue();
//                        boolean response = api.ConfirmOdometer(e.getPictureType(), e.getRequest(),cola.get(0).getClientToken(),
//                                cola.get(0).getId(),
//                                cola.get(0).getNoPolicy(),
//                                e.getOdometro()).booleanValue();
                       if(response){
                           getHelper().getDaoEnviosPendientes().delete(e);
                       }
                    }
                    else if(e.getTipoEnvio()==2){

                    }
                    else if(e.getTipoEnvio()==3){
                        boolean response = api.AjusteOdometerLast(e.getPictureType(),
                                e.getRequest(),e.getIdPoliza(),cola.get(0).getClientToken());
                        if(response){
                            getHelper().getDaoEnviosPendientes().delete(e);
                        }
                    }
                    else if(e.getTipoEnvio()==4){

                    }

                }
            }
        }
        catch (SQLException e){
            e.printStackTrace();
        }
        catch (Exception e){
            e.printStackTrace();
        }
        finally {
            try{
            List<EnviosPendientes> ep2 = getHelper().getDaoEnviosPendientes().queryForAll();
                if(ep2!=null && ep2.size()>0){
                    AlarmaForPendingShipments a=new AlarmaForPendingShipments();
                    a.setAlarm(context,180000);
                }
        }
        catch (SQLException e){
            e.printStackTrace();
        }
        }
    }

    @Override
    protected Void doInBackground(Void... params) {

        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
    }

    private DataBaseHelper getHelper() {
        DataBaseHelper databaseHelper = OpenHelperManager.getHelper(context, DataBaseHelper.class);
        databaseHelper.setContexto(context);
        return databaseHelper;
    }
}