package miituo.com.miituo.activities;

import android.app.Dialog;
import android.app.NotificationManager;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.stmt.UpdateBuilder;

import java.sql.SQLException;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import me.leolin.shortcutbadger.ShortcutBadger;
import miituo.com.miituo.R;
import miituo.com.miituo.VehicleModelAdapter;
import miituo.com.miituo.adapters.PushAdapter;
import miituo.com.miituo.db.DataBaseHelper;
import miituo.com.miituo.db.EnviosPendientes;
import miituo.com.miituo.db.Notifications;
import miituo.com.miituo.swipe.SwipeActionAdapter;
import miituo.com.miituo.swipe.SwipeDirection;
import miituo.com.miituo.utils.CallBack;
import miituo.com.miituo.utils.SimpleCallBack;

import static miituo.com.miituo.activities.MainActivity.result;

public class NotificationsActivity extends BaseActivity implements SimpleCallBack ,SwipeActionAdapter.SwipeActionListener{

    private Typeface typeface;
    private TextView lb1,lbAceptar;
    private LinearLayout cntNoNotificaciones;
    private ListView listPush;
    private List<Notifications> ps=null;
    private PushAdapter vadapter;
    protected SwipeActionAdapter mAdapter;
    private static  Dialog alertaPago;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Alertas");
        toolbar.setTitleTextColor(Color.BLACK);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        typeface = Typeface.createFromAsset(getAssets(), "fonts/herne1.ttf");
        lb1=(TextView)findViewById(R.id.lb1);
        lb1.setTypeface(typeface,Typeface.BOLD);
        lbAceptar=(TextView)findViewById(R.id.lbAceptar);
        lbAceptar.setTypeface(typeface);
        cntNoNotificaciones=(LinearLayout)findViewById(R.id.cntNoPush);
        listPush=(ListView)findViewById(R.id.listPushes);

        try {
            ps= getHelper().getDaoNotifications().queryForAll();
            if(ps==null || ps.size()<1){
                cntNoNotificaciones.setVisibility(View.VISIBLE);
                lbAceptar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                    }
                });
            }
            else{
                cntNoNotificaciones.setVisibility(View.GONE);
                showPushes();
            }

            UpdateBuilder<Notifications, Integer> updateBuilder = getHelper().getDaoNotifications().updateBuilder();
            updateBuilder.updateColumnValue("isRead", true);
            updateBuilder.update();
            ShortcutBadger.applyCount(this,0);
            NotificationManager notificationManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.cancelAll();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showPushes(){
        try {
            Collections.sort(ps);
            vadapter = new PushAdapter(getApplicationContext(), ps,typeface,this);
//            mAdapter=new SwipeActionAdapter(vadapter);
//            mAdapter.setSwipeActionListener(this)
//                    .setDimBackgrounds(false)
//                    .setListView(listPush);
            listPush.setAdapter(vadapter);
            vadapter.notifyDataSetChanged();
            // Set backgrounds for the swipe directions
//            mAdapter.addBackground(SwipeDirection.DIRECTION_NORMAL_LEFT,R.layout.push_item_left);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return true;
    }
    private DataBaseHelper getHelper() {
        DataBaseHelper databaseHelper = OpenHelperManager.getHelper(this, DataBaseHelper.class);
        databaseHelper.setContexto(this);
        return databaseHelper;
    }

    @Override
    public void run(boolean status, final String res) {
        print("staus: "+status+"  res: "+res);
        if(!status){
            final AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Atención");
            builder.setMessage("Seguro que deseas borrar la notificación?");
            builder.setCancelable(false);
            builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    alertaPago.dismiss();
                    deleteNotif(Integer.parseInt(res));
                }
            });
            builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    alertaPago.dismiss();
                }
            });
            runOnUiThread(new Runnable() {
                public void run() {
                    alertaPago = builder.create();
                    alertaPago.show();
                }
            });
        }
        else{
            if(ps.get(Integer.parseInt(res)).getTipoPush()==8) {
                String url = ps.get(Integer.parseInt(res)).getExtra();
                if(url.contains("http")) {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.setPackage("com.android.chrome");
                    try {
                        startActivity(intent);
                    } catch (ActivityNotFoundException ex) {
                        // Chrome browser presumably not installed so allow user to choose instead
                        intent.setPackage(null);
                        startActivity(intent);
                    }
                }
            }
        }
    }

    private void deleteNotif(int id){
        try {
            getHelper().getDaoNotifications().deleteById(id);
            ps= getHelper().getDaoNotifications().queryForAll();
            if(ps==null || ps.size()<1){
                cntNoNotificaciones.setVisibility(View.VISIBLE);
                lbAceptar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                    }
                });
            }
            else{
                cntNoNotificaciones.setVisibility(View.GONE);
                showPushes();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }





    @Override
    public boolean hasActions(int position, SwipeDirection direction){
        if(direction.isLeft()) return true;
//        if(direction.isRight()) return true;
        return false;
    }

    @Override
    public boolean shouldDismiss(int position, SwipeDirection direction){
        return true;//direction == SwipeDirection.DIRECTION_NORMAL_LEFT;
    }

    @Override
    public void onSwipe(int[] positionList, SwipeDirection[] directionList){
        for(int i=0;i<positionList.length;i++) {
            SwipeDirection direction = directionList[i];
            int position = positionList[i];
            String dir = "";

            switch (direction) {
                case DIRECTION_NORMAL_LEFT:
                    dir = "Left";
                    break;
                case DIRECTION_NORMAL_RIGHT:
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setTitle("Test Dialog").setMessage("You swiped right").create().show();
                    dir = "Right";
                    break;
            }
            Toast.makeText(
                    this,
                    dir + " swipe Action triggered on " + mAdapter.getItem(position),
                    Toast.LENGTH_SHORT
            ).show();
            mAdapter.notifyDataSetChanged();
        }
    }
}
