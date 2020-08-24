package miituo.com.miituo.utils;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import miituo.com.miituo.activities.ContratarActivity;

/****************Alarma***************/
public class Alarma extends BroadcastReceiver {
    String referencia,tipoPago;
    static SimpleCallBack cb;

    @Override
    public void onReceive(final Context context, Intent intent) {
        LogHelper.context=context;
        if(cb!=null) {
            cb.run(true, null);
        }
    }

    public void setAlarm(Context context,String ref,String tipoPay,long mils,SimpleCallBack cb) {
        this.cb=cb;
        referencia=ref;
        tipoPago=tipoPay;

        Intent i = new Intent(context, Alarma.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, i, 0);
        AlarmManager manager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
//        manager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + mils, pendingIntent);
        if (Build.VERSION.SDK_INT >= 23) {
            manager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP,System.currentTimeMillis() + mils, pendingIntent);
        } else if (Build.VERSION.SDK_INT >= 19) {
            manager.setExact(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + mils, pendingIntent);
        } else {
            manager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + mils, pendingIntent);
        }
    }

    /**
     * Metodo que cancela la alarma. Este metodo finaliza o para la alarma.
     */
    public void cancelAlarm(Context context) {
        Intent intent = new Intent(context, Alarma.class);
        PendingIntent sender = PendingIntent.getBroadcast(context, 0, intent, 0);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(sender);
    }

}
