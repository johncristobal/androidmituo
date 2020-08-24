package miituo.com.miituo.Servicio;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;

import miituo.com.miituo.activities.MainActivity;
import miituo.com.miituo.R;

public class TimerBroadcastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        //app dead, restart service
        //c = context;
        //PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        //PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "");
        //wl.acquire();

        // Put here YOUR code.
        Toast.makeText(context, "Alarm !!!!!!!!!!", Toast.LENGTH_LONG).show(); // For example
        postNotif("Hola receiver",context);

        //wl.release();
        //context.startService(new Intent(context, backPhotosService.class));

    }

    private void postNotif(String notifString,Context c) {
        /*NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        int icon = R.drawable.miituo;

        RemoteMessage.Notification notification = new RemoteMessage.Notification(icon, "Firebase" + Math.random(), System.currentTimeMillis());
        //Notification notification = new Notification(icon, "Firebase" + Math.random(), System.currentTimeMillis());
//		notification.flags |= Notification.FLAG_AUTO_CANCEL;

        Context context = getApplicationContext();
        CharSequence contentTitle = "Background" + Math.random();
        Intent notificationIntent = new Intent(context, MainActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(context, 0, notificationIntent, 0);
        notification.setLatestEventInfo(context, contentTitle, notifString, contentIntent);
        mNotificationManager.notify(1, notification);*/

        Intent intent;

        intent = new Intent(c,MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pending=PendingIntent.getActivity(c,0,intent,PendingIntent.FLAG_ONE_SHOT);

        Uri sound= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder noti=new NotificationCompat.Builder(c)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("miituo")
                .setContentText(notifString)
                .setAutoCancel(true)
                .setContentIntent(pending)
                .setSound(sound);
        NotificationManager nmg=(NotificationManager)c.getSystemService(Context.NOTIFICATION_SERVICE);
        nmg.notify(0,noti.build());

    }
}
