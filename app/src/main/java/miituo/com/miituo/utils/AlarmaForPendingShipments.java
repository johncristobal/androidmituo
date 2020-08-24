package miituo.com.miituo.utils;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import miituo.com.miituo.threats.SincroEnviosPendientes;


/****************Alarma***************/
public class AlarmaForPendingShipments extends BroadcastReceiver {

    @Override
    public void onReceive(final Context context, Intent intent) {
        Log.d("APP","ejecutando alarma");
        new Thread(new Runnable() {
            public void run() {
                //Aquí ejecutamos nuestras tareas costosas
                new SincroEnviosPendientes(context).execute();
            }
        }).start();
    }

    public void setAlarm(Context context,long mils) {
        Intent i = new Intent(context, AlarmaForPendingShipments.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, i, 0);
        AlarmManager manager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        manager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + mils, pendingIntent);
    }

    /**
     * Metodo que cancela la alarma. Este metodo finaliza o para la alarma.
     */
    public void cancelAlarm(Context context) {
        Intent intent = new Intent(context, AlarmaForPendingShipments.class);
        PendingIntent sender = PendingIntent.getBroadcast(context, 0, intent, 0);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(sender);
    }

}