package miituo.com.miituo.activities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.viewpager.widget.ViewPager;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.android.material.tabs.TabLayout;

import java.io.File;
import java.util.List;

import miituo.com.miituo.R;
import miituo.com.miituo.api.ApiClient;
import miituo.com.miituo.data.IinfoClient;
import miituo.com.miituo.data.InfoClient;
import miituo.com.miituo.fragments.PagerAdapter;
import miituo.com.miituo.threats.GetPDFSync;
import miituo.com.miituo.utils.CallBack;
import miituo.com.miituo.utils.SimpleCallBack;
import static miituo.com.miituo.fragments.PagerAdapter.context;

public class DetallesActivity extends AppCompatActivity {
    ViewPager viewPager;
    PagerAdapter adapter;
    private Typeface typeface, typefacebold;
    public static File pdf;
    public static AlertDialog alertaPago;
    public String pathPhotos = new ApiClient(this).pathPhotos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalles);
        SharedPreferences app_preferences= getSharedPreferences("miituo", Context.MODE_PRIVATE);
        long starttime = app_preferences.getLong("time",0);

        typeface = Typeface.createFromAsset(getAssets(), "fonts/herne1.ttf");
        typefacebold = Typeface.createFromAsset(getAssets(), "fonts/herne.ttf");

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbartaby);
        toolbar.setTitle("General");
        toolbar.setTitleTextColor(Color.BLACK);
        setSupportActionBar(toolbar);
        //get back arrow
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.addTab(tabLayout.newTab().setText("").setIcon(R.drawable.general));
        //tabLayout.addTab(tabLayout.newTab().setText("").setIcon(R.drawable.historial));
        tabLayout.addTab(tabLayout.newTab().setText("").setIcon(R.drawable.odo));
        //tabLayout.addTab(tabLayout.newTab().setText("").setIcon(R.drawable.pago));
        tabLayout.addTab(tabLayout.newTab().setText("").setIcon(R.drawable.sini));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        tabLayout.setSelectedTabIndicatorColor(Color.rgb(34,201,252));
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
                //change icon
                if(tab.getPosition()==0){
                    tab.setIcon(R.drawable.general);
                }else if(tab.getPosition()==1){
                    tab.setIcon(R.drawable.odoaz);
                }else if(tab.getPosition()==2){
                    tab.setIcon(R.drawable.sinia);
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                //change icon
                if(tab.getPosition()==0){
                    tab.setIcon(R.drawable.generalg);
                }else if(tab.getPosition()==1){
                    tab.setIcon(R.drawable.odo);
                }else if(tab.getPosition()==2){
                    tab.setIcon(R.drawable.sini);
                }
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        init();
        viewPager = (ViewPager) findViewById(R.id.pager);
        adapter = new PagerAdapter(getSupportFragmentManager(), tabLayout.getTabCount(),this,typeface,typefacebold,starttime,viewPager);
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
    }

    public void init(){
        TextView  lbDescargar = findViewById(R.id.textView37);
        TextView poliza = findViewById(R.id.textViewpolizadetail);
        //poliza.setTypeface(PagerAdapter.tipo);
        poliza.setText(IinfoClient.InfoClientObject.getPolicies().getNoPolicy());
        lbDescargar.setTypeface(PagerAdapter.tipo);
        lbDescargar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new GetPDFSync(DetallesActivity.this,true, IinfoClient.InfoClientObject.getPolicies().getId(),IinfoClient.InfoClientObject.getPolicies().getNoPolicy(), new SimpleCallBack() {
                    @Override
                    public void run(boolean status, String res) {
                        if(!status){
//            Toast.makeText(c,"descarga fallida",Toast.LENGTH_LONG).show();
                            final AlertDialog.Builder builder = new AlertDialog.Builder(DetallesActivity.this);
                            builder.setTitle("Descarga Fallida");
                            builder.setMessage("Por favor intentalo más tarde.");
                            builder.setCancelable(false);
                            builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    alertaPago.dismiss();
                                }
                            });
                            alertaPago = builder.create();
                            alertaPago.show();
                        }else{
                            // Toast.makeText(c,"descarga exitosa",Toast.LENGTH_LONG).show();
                            final AlertDialog.Builder builder = new AlertDialog.Builder(DetallesActivity.this);
                            builder.setTitle("Descarga completa");
                            builder.setMessage("Ahora tu póliza se encuentra en tus descargas.");
                            builder.setCancelable(false);
                            builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    alertaPago.dismiss();
//                    c.startActivity(new Intent(DownloadManager.ACTION_VIEW_DOWNLOADS));
                                    startActivity(new Intent(getApplicationContext(), PDFViewer.class).putExtra("isPoliza",true));
                                }
                            });
                            alertaPago = builder.create();
                            alertaPago.show();
                        }
                    }
                }).execute();
            }
        });

        ImageView fotocarro = (ImageView)findViewById(R.id.imageView5);
        if(fotocarro!= null){
            //pintaImg(imagen,position);
            pintaImg(fotocarro, IinfoClient.InfoClientObject.getPolicies().getId());
        }
    }

    private void pintaImg(ImageView iv, int id) {
        //ruta del archivo...https://filesdev.miituo.com/21/FROM_VEHICLE.png

        Glide.with(this).asBitmap().
                load(pathPhotos + id + "/FROM_VEHICLE.png")
                //.into(iv);
                .listener(new RequestListener<Bitmap>() {
                              @Override
                              public boolean onLoadFailed(@Nullable GlideException e, Object o, Target<Bitmap> target, boolean b) {
                                  iv.setImageResource(R.drawable.foto);
                                  return false;
                              }

                              @Override
                              public boolean onResourceReady(Bitmap bitmap, Object o, Target<Bitmap> target, DataSource dataSource, boolean b) {
                                  iv.setImageBitmap(bitmap);// .setImage(ImageSource.bitmap(bitmap));
                                  return false;
                              }
                          }
                ).submit();

    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.manu_principal,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        if (item.getItemId() == android.R.id.home) {
            finish(); // close this activity and return to preview activity (if there is any)
        }
        return true;
    }
}
