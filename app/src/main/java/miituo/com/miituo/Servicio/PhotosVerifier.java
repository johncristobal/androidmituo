//package miituo.com.miituo.Servicio;
//
//import java.text.SimpleDateFormat;
//import java.util.Calendar;
//import java.util.Date;
//import java.util.Locale;
//
//import android.app.AlarmManager;
//import android.app.AlertDialog;
//import android.app.PendingIntent;
//import android.content.BroadcastReceiver;
//import android.content.Context;
//import android.content.DialogInterface;
//import android.content.Intent;
//import android.content.SharedPreferences;
//import android.graphics.Bitmap;
//import android.graphics.BitmapFactory;
//import android.os.AsyncTask;
//import android.util.Log;
//import miituo.com.miituo.VehicleOdometer;
//import miituo.com.miituo.activities.VehiclePictures;
//import miituo.com.miituo.api.ApiClient;
//import miituo.com.miituo.data.IinfoClient;
//
//import static miituo.com.miituo.activities.PrincipalActivity.fotosfaltantesList;
//
//public class PhotosVerifier extends BroadcastReceiver {
//    public Context context = null;
//    public static final int timeAct = 15;
//    public boolean isFotosOk=false;
//    public SharedPreferences preferences;
//    public  ApiClient api;
//
//    @Override
//    public void onReceive(final Context context, Intent intent) {
//        this.context = context;
//        api=new ApiClient(context);
//        Log.d("Alarma", "Inicio subida de fotos");
//        // Ejecutamos la sincronizacion de subida
//        preferences = context.getSharedPreferences("miituo", Context.MODE_PRIVATE);
//        boolean isUpload1 = preferences.getBoolean("isUpload1", false);
//        boolean isUpload2 = preferences.getBoolean("isUpload2", false);
//        boolean isUpload3 = preferences.getBoolean("isUpload3", false);
//        boolean isUpload4 = preferences.getBoolean("isUpload4", false);
//        boolean isUpload5 = preferences.getBoolean("isUpload5", false);
//        isFotosOk=isUpload1 && isUpload2 && isUpload3 && isUpload4;
//            if (!isFotosOk){
//                new sendVehiclePicture().execute();
//            }
//    }
//
//    public void setAlarm(Context context) {
//        Locale current = context.getResources().getConfiguration().locale;
//        SimpleDateFormat ff = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", current);
//        Log.i("Alarma", "setAlarm Alarma, Fecha = " + ff.format(new Date()));
//
//        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
//        Intent i = new Intent(context, PhotosVerifier.class);
//        PendingIntent pi = PendingIntent.getBroadcast(context, 0, i, 0);
//        // Millisec * Second * Minute, 15 minutes.
//        am.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), 1000 * 60 * timeAct, pi);
//        // Ejecutamos la primer sincronizacion inmediatamente.
//        this.onReceive(context, i);
//    }
//
//    public void cancelAlarm(Context context) {
//        Locale current = context.getResources().getConfiguration().locale;
//        SimpleDateFormat ff = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", current);
//        Log.i("Alarma", "Cancelando Alarma, Fecha = " + ff.format(new Date()));
//        Intent intent = new Intent(context, PhotosVerifier.class);
//        PendingIntent sender = PendingIntent.getBroadcast(context, 0, intent, 0);
//        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
//        alarmManager.cancel(sender);
//    }
//
//    public class sendVehiclePicture extends AsyncTask<Void, Void, Void> {
//        String ErrorCode="";
//        public boolean flag = true;
//        public int cont = 0;
//
//        @Override
//        protected void onCancelled() {
//
//        }
//
//        @Override
//        protected void onPreExecute() {
//
//        }
//
//        @Override
//        protected Void doInBackground(Void... params) {
//            try {
//
//                if(!isFotosOk) {
//                     String username = "";
//                     username = "frontal";
//                     launchImagen(username,1);
//                     username = "derecho";
//                     launchImagen(username,2);
//                     username = "back";
//                     launchImagen(username,3);
//                     username = "izquierdo";
//                     launchImagen(username,4);
//                }
//            }
//            catch (Exception ex)
//            {
//                ErrorCode=ex.getMessage();
//                this.cancel(true);
//            }
//            return null;
//        }
//
//        private void launchImagen(String username,int tipo) {
//
//            try {
//                String filePath  = preferences.getString("nombrefoto" + username + "poliza folio", "null");
//                preferences.edit().putBoolean("isUpload"+tipo,false).apply();
//
//                BitmapFactory.Options options = new BitmapFactory.Options();
//                options.inSampleSize = 4;
//                Bitmap bit = BitmapFactory.decodeFile(filePath, options);
//
//                int a = api.UploadPhoto(tipo, bit, UrlApi, tok);
//                if (a <= 4 && a != 0) {
//                    preferences.edit().putBoolean("isUpload"+tipo,true).apply();
//                    cont++;
//                }
//            }catch(Exception e){
//                ErrorCode = e.toString();
//                this.cancel(true);
//            }
//        }
//
//        @Override
//        protected void onPostExecute(Void aVoid) {
//            try {
//                new AlertDialog.Builder(context)
//                        .setTitle("Fotos de Vehículo")
//                        .setMessage("Las fotos se han subido correctamente. !Gracias¡")
//                        .setCancelable(false)
//                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                IinfoClient.InfoClientObject.getPolicies().setHasVehiclePictures(true);
//
//                                //valido foto de odometro tmb antes de lanzar a sendodometer***
//                                long l=Calendar.getInstance().getTimeInMillis();
//                                preferences.edit().putLong("time",l).apply();
//                                String fotos = preferences.getString("solofotos","null");
//                                if(fotos.equals("1")){
//                                    //aqu ivalido siguiente foto odometro
//                                    //entonces hay mas de una foto que solicitaron
//                                    //busco si hay de odometro, si no, entonces updateStauts
//                                    boolean miniodo = false;
//                                    for(int k=0;k<fotosfaltantesList.size();k++){
//                                        if(fotosfaltantesList.get(k).getId() == 5){
//                                            miniodo = true;
//                                            break;
//                                        }
//                                    }
//                                    if(miniodo){
//                                        Intent i = new Intent(VehiclePictures.this,VehicleOdometer.class);
//                                        startActivity(i);
//
//                                    }else{
//                                        app_preferences.edit().putString("solofotos","0").apply();
//                                        VehiclePictures.updateStatus odometro = new VehiclePictures.updateStatus();
//                                        odometro.execute();
//
//                                    }
//                                }else{
//                                    Intent odo = new Intent(VehiclePictures.this, VehicleOdometer.class);
//                                    startActivity(odo);
//                                }
//                            }
//                        })
//                        .show();
//            }catch(Exception e){
//
//            }
//        }
//    }
//}
