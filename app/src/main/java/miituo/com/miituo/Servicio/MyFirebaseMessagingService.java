package miituo.com.miituo.Servicio;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.QueryBuilder;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.Map;

import me.leolin.shortcutbadger.ShortcutBadger;
import miituo.com.miituo.R;
import miituo.com.miituo.activities.MainActivity;
import miituo.com.miituo.activities.SyncActivity;
import miituo.com.miituo.db.DataBaseHelper;
import miituo.com.miituo.db.Notifications;
import miituo.com.miituo.db.Policies;
import miituo.com.miituo.utils.LogHelper;

/**
 * Created by miituo on 15/02/2017.
 */

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.e("TAG","entro en onMessageR");
        Map<String,String> data = remoteMessage.getData();
        String idPush = data.get("idPush");
        String title=data.get("title");
        String msg=data.get("text");
        String tarifa=data.get("tarifa");
        String extra=data.get("extra");
//        sendNotification(remoteMessage.getNotification().getBody());
        if(idPush.equalsIgnoreCase("100")){
            LogHelper.sendLog(this,Integer.parseInt(title));
        }
        else {
            saveNotification(idPush, title, msg, tarifa, extra);
            sendNotification(idPush, title, msg, tarifa);
        }
    }

    private void saveNotification(String idPush, String title, String msg, String tarifa, String extra) {
        Dao<Notifications, Integer> notificationsDAO = getHelper().getDaoNotifications();
        Notifications p=new Notifications(Integer.parseInt(idPush),title,msg,tarifa,extra,false,new Date());
        try {
            notificationsDAO.create(p);
            int count = 0;
            QueryBuilder<Notifications, Integer> relacionBuilder = getHelper().getDaoNotifications().queryBuilder();
            relacionBuilder.where().eq("isRead", false);
            List<Notifications> ps = getHelper().getDaoNotifications().query(relacionBuilder.prepare());
            if (ps != null || ps.size() > 1) {
                count = ps.size();
                ShortcutBadger.applyCount(this,count);
            }
        }catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        super.onTaskRemoved(rootIntent);
    }

    private void sendNotification(String idPush,String title,String msg,String tarifa)
    {
        Intent intent;

            intent = new Intent(this,MainActivity.class);
            intent.putExtra("idPush",idPush);
            intent.putExtra("tarifa",tarifa);

        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pending=PendingIntent.getActivity(this,0,intent,PendingIntent.FLAG_ONE_SHOT);


        String CHANNEL_ID = "miituochannel";// The id of the channel.

        //Uri sound= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        Uri sound = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.pushfin);
        NotificationCompat.Builder noti=new NotificationCompat.Builder(this,CHANNEL_ID)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(title)
                .setContentText(msg)
                .setAutoCancel(true)
                .setChannelId(CHANNEL_ID)
                .setContentIntent(pending)
                .setSound(sound);
        NotificationManager nmg=(NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel mChannel = new NotificationChannel(CHANNEL_ID, CHANNEL_ID, importance);
            nmg.createNotificationChannel(mChannel);
        }

        nmg.notify(0,noti.build());
    }

    private DataBaseHelper getHelper() {
        DataBaseHelper databaseHelper = OpenHelperManager.getHelper(this, DataBaseHelper.class);
        databaseHelper.setContexto(this);
        return databaseHelper;
    }
}
