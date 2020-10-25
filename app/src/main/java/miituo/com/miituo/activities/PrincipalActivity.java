package miituo.com.miituo.activities;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager.widget.ViewPager;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import miituo.com.miituo.MenuAdapterSelf;
import miituo.com.miituo.R;
import miituo.com.miituo.VehicleModelAdapter;
import miituo.com.miituo.adapters.PromosAdapter;
import miituo.com.miituo.api.ApiClient;
import miituo.com.miituo.data.ClientMovil;
import miituo.com.miituo.data.FotosFaltantesModel;
import miituo.com.miituo.data.IinfoClient;
import miituo.com.miituo.data.InfoClient;
import miituo.com.miituo.db.DataBaseHelper;
import miituo.com.miituo.db.Notifications;
import miituo.com.miituo.miltiaseguradora.Aseguradoras;
import miituo.com.miituo.threats.AvailableSiteSync;
import miituo.com.miituo.threats.GetCuponAsync;
import miituo.com.miituo.threats.GetPoliciesData;
import miituo.com.miituo.threats.PutTokenSync;
import miituo.com.miituo.utils.BadgeDrawerArrowDrawable;
import miituo.com.miituo.utils.CallBack;
import miituo.com.miituo.utils.Evento;
import miituo.com.miituo.utils.LogHelper;
import miituo.com.miituo.utils.SimpleCallBack;

import static miituo.com.miituo.activities.MainActivity.result;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.stmt.QueryBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class PrincipalActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, CallBack {

    public Timer timer;

    private ListView vList;
    private VehicleModelAdapter vadapter;
    private MenuAdapterSelf menuadapter;
    SharedPreferences app_preferences;
    ActionBarDrawerToggle toggle;
    Toolbar toolbar;

    public float velocidad;

    public TextView nombre, resumen, sinPolizas;
    ImageView imgSinPolizas;

    DrawerLayout drawer;
    private Typeface typeface;

    private SwipeRefreshLayout swipeContainer;

    //public threadtosync hilos;

    LocationManager locationManager;

    LocationListener locationListener;
    public AlertDialog alertaCotiz;

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    public long starttime;

    public String tok_basic, tokencliente;
    public int idpoliza;

    public static List<FotosFaltantesModel> fotosfaltantesList;
    private ViewPager viewPager;
    private PromosAdapter adapter;
    private LinearLayout dotsLayout;
    private TextView[] dots;
    private BadgeDrawerArrowDrawable badgeDrawable;
    private List<Notifications> ps = null;

    private boolean isStorage = false;
    static AlertDialog alerta;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Tus pólizas");
        toolbar.setTitleTextColor(Color.BLACK);
        setSupportActionBar(toolbar);

        app_preferences = getSharedPreferences("miituo", Context.MODE_PRIVATE);
        starttime = app_preferences.getLong("time", 0);
        typeface = Typeface.createFromAsset(getAssets(), "fonts/herne1.ttf");
        app_preferences.edit().putString("solofotos", "0").apply();

        configurarAlmacenamiento();
        configurarVelocidad();

        tok_basic = "";
        swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);
        // Setup refresh listener which triggers new data loading
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Your code to refresh the list here.
                // Make sure you call swipeContainer.setRefreshing(false)
                // once the network request has completed successfully.
                //fetchTimelineAsync(0);
                //hilos = new threadtosync();
                LogHelper.log(PrincipalActivity.this,LogHelper.user_interaction,
                        "PrincipalActivity.swipe", "usuario actualiza polizas", "","",  "", "");
                //hilos.Sync.execute(app_preferences.getString("Celphone", "0"));
                getPolizasData(app_preferences.getString("Celphone", "0"));
            }
        });
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        Typeface typefacebold = Typeface.createFromAsset(getAssets(), "fonts/herne.ttf");

        sinPolizas = (TextView) findViewById(R.id.lbSinPolizas);
        imgSinPolizas = (ImageView) findViewById(R.id.imgSinPolizas);
        sinPolizas.setTypeface(typeface);
        nombre = (TextView) findViewById(R.id.textViewNombreleo);
        nombre.setText(app_preferences.getString("nombre", "null"));
        nombre.setTypeface(typefacebold);
        nombre = (TextView) findViewById(R.id.textViewNombre);
        nombre.setText(app_preferences.getString("nombre", "null"));

        TextView hola = (TextView) findViewById(R.id.textView35);
        hola.setTypeface(typefacebold);
        resumen = (TextView) findViewById(R.id.textView25);

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);

        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setItemIconTintList(null);

        final GlobalActivity globalVariable = (GlobalActivity) getApplicationContext();
        result = globalVariable.getPolizas();

        TextView cerrar = (TextView) findViewById(R.id.textView62);
        cerrar.setTypeface(typeface);
        TextView cotizar = (TextView) findViewById(R.id.textView16);
        cotizar.setTypeface(typeface);
        cotizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawer.closeDrawer(GravityCompat.START);
                LogHelper.log(PrincipalActivity.this,LogHelper.user_interaction,"PrincipalActivity.cotizar",
                        "cotizar desde hamburguesa", "","",  "", "");
                new AvailableSiteSync(PrincipalActivity.this, new SimpleCallBack() {
                    @Override
                    public void run(boolean status, String res) {

                        if (!status) {
                            final AlertDialog.Builder builder = new AlertDialog.Builder(PrincipalActivity.this);
                            builder.setTitle("Alerta");
                            builder.setMessage("Error de conexión");
                            builder.setCancelable(false);
                            builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    alertaCotiz.dismiss();
                                }
                            });
                            alertaCotiz = builder.create();
                            alertaCotiz.show();
                        } else {
                            if (res.equalsIgnoreCase("0")) {
                                //i = new Intent(PrincipalActivity.this, CotizarAutoActivity.class);
                                //i = new Intent(PrincipalActivity.this, CotizaActivity.class);
                                //final ApiClient ac = new ApiClient(PrincipalActivity.this);
                                //CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
                                //CustomTabsIntent customTabsIntent = builder.build();
                                //customTabsIntent.launchUrl(PrincipalActivity.this, Uri.parse(ac.UrlCotiza));
                                //i.putExtra("isSimulacion", false);
//                                Intent i = new Intent(PrincipalActivity.this, CotizarAutoActivity.class);
//                                i.putExtra("isSimulacion", false);
//                                startActivity(i);
                                if(Build.VERSION.SDK_INT <= Build.VERSION_CODES.LOLLIPOP_MR1){
                                    Intent i = new Intent(PrincipalActivity.this, CotizarAutoActivity.class);
                                    startActivity(i);
                                }else{
                                    Intent i = new Intent(PrincipalActivity.this, CotizaActivity.class);
                                    i.putExtra("cliente",true);
                                    startActivity(i);
                                }
                            } else {
                                Intent i;
                                i = new Intent(PrincipalActivity.this, UnavailableSite.class);
                                i.putExtra("status", res);
                                startActivity(i);
                            }
                            Evento.eventRecord(PrincipalActivity.this,Evento.COTIZAR_MENU);
                        }
                    }
                }).execute();
            }
        });

        cerrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(PrincipalActivity.this);
                builder.setTitle("Atención");
                builder.setMessage("¿Deseas cerrar sesión?");
                builder.setPositiveButton("Si", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        LogHelper.log(PrincipalActivity.this,LogHelper.user_interaction,"PrincipalActivity.cerrarSesion",
                                "cerrando sesion desde hamburguesa", "","",  "", "");
                        SharedPreferences.Editor editor = app_preferences.edit();
                        editor.putString("Celphone", "");
                        editor.putString("sesion", "");
                        editor.apply();

                        Intent intent = new Intent(PrincipalActivity.this, SyncActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    }
                });

                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

                AlertDialog alerta = builder.create();
                alerta.show();
            }
        });

        if (result != null) {
            vList = (ListView) findViewById(R.id.listviewinfoclient);
            removeInvalidPolicies();
            vadapter = new VehicleModelAdapter(getApplicationContext(), result, typeface, starttime, this);
            vadapter.notifyDataSetChanged();
            vList.setAdapter(vadapter);
            if(result.size() > 0) {
                tokencliente = result.get(0).getClient().getToken();
            }else{
                tokencliente = "";
            }
        }

        String act = getIntent().getStringExtra("actualizar");
        if (act != null) {
            if (act.equals("1")) {
                //hilos = new threadtosync();
                LogHelper.log(PrincipalActivity.this,LogHelper.backTask,"PrincipalActivity.inshurancesList",
                        "actualizando polizas automaticamente", "","",  "", "");
                //hilos.Sync.execute(app_preferences.getString("Celphone", "0"));
                this.getPolizasData(app_preferences.getString("Celphone", "0"));
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        pageSwitcher();
        obtenerCupon();
        sendTokenFirebase();
    }

    @Override
    protected void onPause() {
        super.onPause();
        timer.cancel();
    }

    @Override
    public void onBackPressed() {
        try {
            if (drawer.isDrawerOpen(GravityCompat.START)) {
                drawer.closeDrawer(GravityCompat.START);
            } else {
                super.onBackPressed();
                Intent i = new Intent(this, SyncActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                i.putExtra("actualizar", false);
                startActivity(i);
                //finish();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.principal, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private DataBaseHelper getHelper() {
        DataBaseHelper databaseHelper = OpenHelperManager.getHelper(this, DataBaseHelper.class);
        databaseHelper.setContexto(this);
        return databaseHelper;
    }

    int count = 0;

//TODO - obtener cupon y mostrarlo en los banners---------------------------------------------------
    public void obtenerCupon(){
        String url = "Cupon/getReferredClientCoupon/"+app_preferences.getString("Celphone", "0");
        GetCuponAsync gp = new GetCuponAsync(PrincipalActivity.this, url, new SimpleCallBack(){

            @Override
            public void run(boolean status, String res) {
                if (status) {
                    try {
                        //Crear los elementos json para obtener los datos del servicio...
                        JSONArray array = new JSONArray(res);
                        JSONObject o = array.getJSONObject(0);
                        JSONObject cupones = o.getJSONObject("Cupones");
                        Double kms = cupones.getDouble("Kms");
                        InicializarBanners(kms);
                        //Toast.makeText(PrincipalActivity.this, "kilometraje:"+ kms, Toast.LENGTH_SHORT).show();
                    } catch (JSONException e) {
                        e.printStackTrace();
                        InicializarBanners(100.0);
                    }
                }else{
                    InicializarBanners(100.0);
                }
            }
        });
        gp.execute();
    }
    public void InicializarBanners(Double kms){
        viewPager = (ViewPager) findViewById(R.id.view_pager);
        String cupon = "";
        if (result != null && result.size() > 0) {
            cupon = result.get(0).getClient().getCupon();
        }
        adapter = new PromosAdapter(this, cupon, kms.intValue());
        viewPager.setAdapter(adapter);
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                TextView tv = (TextView) findViewById(R.id.terms);
                if (position == 1) {
                    tv.setText("*Consulta términos y condiciones.");
                } else {
                    tv.setText("*Aplica un cupón al mes por póliza solo si \n" +
                            "reportaste tu odómetro y pagaste el mes anterior.");
                }
                addBottomDots(position);
                timer.cancel();
                pageSwitcher();
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
            }


            @Override
            public void onPageScrollStateChanged(int arg0) {
            }
        });
        dotsLayout = (LinearLayout) findViewById(R.id.layoutDots);
        dotsLayout.removeAllViews();
        TextView tv = (TextView) findViewById(R.id.terms);
        tv.setText("Aplica un cupón al mes por póliza solo si \n" +
                "reportaste tu odómetro y pagaste el mes anterior.");
        addBottomDots(0);
        //pageSwitcher();

        managePushIndicator();
    }

//TODO - colocar cantidad de push notifi en menu ---------------------------------------------------
    private void managePushIndicator() {
        try {
            count = 0;
            QueryBuilder<Notifications, Integer> relacionBuilder = getHelper().getDaoNotifications().queryBuilder();
            relacionBuilder.where().eq("isRead", false);
            ps = getHelper().getDaoNotifications().query(relacionBuilder.prepare());
            if (ps != null || ps.size() > 1) {
                count = ps.size();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        badgeDrawable = new BadgeDrawerArrowDrawable(getSupportActionBar().getThemedContext());
        if (count > 0) {
            toggle.setDrawerArrowDrawable(badgeDrawable);
            badgeDrawable.setText("" + count);
//            toggle.setDrawerIndicatorEnabled(true);
//            toolbar.setNavigationIcon(R.drawable.menu_pink);
        } else {
            toggle = new ActionBarDrawerToggle(
                    this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
            drawer.setDrawerListener(toggle);

            badgeDrawable.setText("");
            badgeDrawable.setEnabled(false);
            toggle.syncState();
        }

        ListView lista = (ListView) findViewById(R.id.lst_menu_items);
        lista.setFastScrollEnabled(true);
        lista.setScrollingCacheEnabled(false);
        menuadapter = new MenuAdapterSelf(getApplicationContext(), count, typeface);
        lista.setAdapter(menuadapter);
        lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                LogHelper.log(PrincipalActivity.this,LogHelper.user_interaction,"PrincipalActivity.menuHamburguesa",
                        "click en: "+position, "","",  "", "");
                if (position == 0) {
                    drawer.closeDrawer(GravityCompat.START);
                    Intent i = new Intent(PrincipalActivity.this, PrincipalActivity.class);
                    startActivity(i);
                } else if (position == 1) {
                    drawer.closeDrawer(GravityCompat.START);
                    Intent i = new Intent(PrincipalActivity.this, NotificationsActivity.class);
                    startActivity(i);
                } else if (position == 2) {
                    drawer.closeDrawer(GravityCompat.START);
                    if (result.size() > 1) {
                        Intent i = new Intent(PrincipalActivity.this, CambioPagoActivity.class);
                        startActivity(i);
                    } else {
                        IinfoClient.setInfoClientObject(result.get(0));
                        startActivity(new Intent(PrincipalActivity.this, WebActivity.class).putExtra("isPoliza", false));
                    }
                } else if (position == 3) {
                    drawer.closeDrawer(GravityCompat.START);
                    Intent i = new Intent(PrincipalActivity.this, AcercaActivity.class);
                    startActivity(i);
                } else if (position == 4) {
                    drawer.closeDrawer(GravityCompat.START);
                    Intent i = new Intent(PrincipalActivity.this, ContactaActivity.class);
                    startActivity(i);
                } else if (position == 5) {
                    drawer.closeDrawer(GravityCompat.START);
                    String urlString = "https://www.miituo.com/Blog";
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(urlString));
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.setPackage("com.android.chrome");
                    try {
                        startActivity(intent);
                    } catch (ActivityNotFoundException ex) {
                        // Chrome browser presumably not installed so allow user to choose instead
                        intent.setPackage(null);
                        startActivity(intent);
                    }
                } else if (position == 6) {
                    drawer.closeDrawer(GravityCompat.START);
                    String urlString = "https://www.miituo.com/Ayuda";
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(urlString));
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.setPackage("com.android.chrome");
                    try {
                        startActivity(intent);
                    } catch (ActivityNotFoundException ex) {
                        // Chrome browser presumably not installed so allow user to choose instead
                        intent.setPackage(null);
                        startActivity(intent);
                    }
                } else if (position == 7) {
                    drawer.closeDrawer(GravityCompat.START);
                    Intent i = new Intent(PrincipalActivity.this, CancelActivity.class);
                    startActivity(i);
                } else if (position == 8) {
                    drawer.closeDrawer(GravityCompat.START);
                    Intent i = new Intent(PrincipalActivity.this, TerminosActivity.class);
                    startActivity(i);
                }
            }
        });
    }

    public void pageSwitcher() {
        timer = new Timer(); // At this line a new Thread will be created
        timer.schedule(new RemindTask(), 9000, 9000);
    }
    // this is an inner class...
    class RemindTask extends TimerTask {
        @Override
        public void run() {
            runOnUiThread(new Runnable() {
                public void run() {
                    if (viewPager.getCurrentItem() == 0) {
                        viewPager.setCurrentItem(1, true);
                    } else {
                        viewPager.setCurrentItem(0, true);
                    }
                }
            });

        }
    }

    private void addBottomDots(int currentPage) {
        dots = new TextView[2];
        int[] colorsActive = getResources().getIntArray(R.array.array_dot_active);
        int[] colorsInactive = getResources().getIntArray(R.array.array_dot_inactive);

        dotsLayout.removeAllViews();
        for (int i = 0; i < dots.length; i++) {
            dots[i] = new TextView(this);
            dots[i].setText("•");//Html.fromHtml("&#8226;"));
            dots[i].setTextSize(20);
            dots[i].setTextColor(colorsInactive[currentPage]);
            dotsLayout.addView(dots[i]);
        }

        if (dots.length > 0)
            dots[currentPage].setTextColor(colorsActive[currentPage]);
    }

//TODO - send data forebase token ------------------------------------------------------------------
    public void sendTokenFirebase(){
        String celData = "";
        try {
            String macAddress="";
            PackageInfo pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            float version = Float.parseFloat(pInfo.versionName);
            TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
            if (ActivityCompat.checkSelfPermission(PrincipalActivity.this, Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {
                macAddress = telephonyManager.getDeviceId();
            }
            String marca = Build.MANUFACTURER;
            String modelo = Build.MODEL;
            String versionRelease = Build.VERSION.RELEASE;
            String carrierName = telephonyManager.getNetworkOperatorName();
            JsonObject j=new JsonObject();
            j.addProperty("imei",macAddress);
            j.addProperty("os","ANDROID");
            j.addProperty("marca",marca);
            j.addProperty("modelo",modelo);
            j.addProperty("osVersion",versionRelease);
            j.addProperty("appVersion",version);
            j.addProperty("carrier",carrierName);
            celData=j.toString();
        }
        catch (Exception e){
            e.printStackTrace();
        }
        ClientMovil cli = new ClientMovil();
        cli.setDatacelphone(celData);
        cli.setCelphone(app_preferences.getString("Celphone", "0"));
        String token = FirebaseInstanceId.getInstance().getToken();
        cli.setToken(token);

        //send token firebase to server...
        new PutTokenSync("ClientUser", PrincipalActivity.this, cli, tokencliente, (status, res) -> {
            if(status){
                Log.e("OK","Token enviado correctamente");
            }else{
                Log.e("Error","Error al enviar token");
            }
        }).execute();
    }

//TODO - borra polizas inactivas  ------------------------------------------------------------------
    private void removeInvalidPolicies() {
        for (int i = (result.size() - 1); i >= 0; i--) {
//            if(!result.get(i).getPolicies().isHasVehiclePictures() &&
//                    !result.get(i).getPolicies().isHasOdometerPicture() &&
            if (result.get(i).getPolicies().getReportState() == 4) {
//            if(i>0){
                result.remove(i);
            }
        }
    }

//TODO -------------------------------------- Almacenamiento ---------------------------------------
    private void configurarAlmacenamiento() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            // ask for permission
            isStorage = false;
        } else {
            isStorage = true;
        }
    }

//TODO -------------------------------------- Velocidad --------------------------------------------
    private void configurarVelocidad() {
        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                //Log.i("Location", location.toString());
                //Toast.makeText(PrincipalActivity.this,"speed: "+location.getSpeed(),Toast.LENGTH_SHORT).show();
                velocidad = location.getSpeed();

            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {
            }

            @Override
            public void onProviderEnabled(String s) {
            }

            @Override
            public void onProviderDisabled(String s) {
            }
        };

        // If device is running SDK < 23
        //if (Build.VERSION.SDK_INT < 23) {
        //    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
        //    locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
        //} else {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            // ask for permission
            if (isStorage) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
            }

        } else {// ask for permission
            if (!isStorage) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 6);
            }
            // we have permission!
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
        }
        //}
    }

//TODO -------------------------------------- requeste permission ----------------------------------
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 6: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(this, "Permission denied to write your External storage", Toast.LENGTH_SHORT).show();
                }
                return;
            }
            default: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)

                        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
                }
                return;
            }
        }
    }

//TODO - options selected menu nav -----------------------------------------------------------------
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.ayuda) {
            // Handle the camera action
            Intent i = new Intent(PrincipalActivity.this, AcercaActivity.class);
            startActivity(i);
        } else if (id == R.id.cerrar) {

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Atención");
            builder.setMessage("¿Deseas cerrar sesión?");
            builder.setPositiveButton("Si", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    SharedPreferences.Editor editor = app_preferences.edit();
                    editor.putString("Celphone", "");
                    editor.putString("sesion", "");
                    editor.apply();

                    Intent intent = new Intent(PrincipalActivity.this, SyncActivity.class);
                    startActivity(intent);
                }
            });

            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                }
            });

            AlertDialog alerta = builder.create();
            alerta.show();

        } else if (id == R.id.polizas) {

        }/* else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }*/

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void runInt(int value) {
        if (velocidad >= 5) {
            AlertDialog.Builder builder = new AlertDialog.Builder(PrincipalActivity.this);
            builder.setTitle("¡Vas manejando!");
            builder.setMessage("Reporta más tarde…");
            builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent i = new Intent(PrincipalActivity.this, SyncActivity.class);
                    startActivity(i);
                }
            });
            AlertDialog alerta = builder.create();
            alerta.show();
        } else {
            //Clic into row to launch activity
            InfoClient item = result.get(value);
            //static class -- set client in this part
            LogHelper.log(this,LogHelper.user_interaction,"PrincipalActivity.insgurancesList",
                    "clik en la poliza: "+item.getPolicies().getId(), "","",  "", "");
            IinfoClient.setInfoClientObject(item);
            IinfoClient.getInfoClientObject().getClient().setCelphone(app_preferences.getString("Celphone", "0"));

            tok_basic = item.getClient().getToken();
            idpoliza = IinfoClient.getInfoClientObject().getPolicies().getId();

            //firebase to get tokne....temp for now
            //String token = IinfoClient.getInfoClientObject().getClient().getToken();

            //set token...
            //IinfoClient.getInfoClientObject().getClient().setToken(token);
            Intent i;
//            item.getPolicies().setReportState(13);
            if (!item.getPolicies().isHasVehiclePictures() && !item.getPolicies().isHasOdometerPicture()) {
                i = new Intent(PrincipalActivity.this, VehiclePictures.class);
                app_preferences.edit().putString("odometro", "first").apply();
                startActivity(i);
            } else if (!item.getPolicies().isHasVehiclePictures() && item.getPolicies().isHasOdometerPicture()) {
                i = new Intent(PrincipalActivity.this, VehiclePictures.class);
                app_preferences.edit().putString("odometro", "first").apply();
                app_preferences.edit().putString("solofotos", "1").apply();
                startActivity(i);
            } else if (item.getPolicies().isHasVehiclePictures() && !item.getPolicies().isHasOdometerPicture()) {
                i = new Intent(PrincipalActivity.this, VehicleOdometer.class);
                app_preferences.edit().putString("odometro", "first").apply();
                startActivity(i);
            } else if (item.getPolicies().getReportState() == 13) {
                i = new Intent(PrincipalActivity.this, VehicleOdometer.class);
                app_preferences.edit().putString("odometro", "mensual").apply();
                startActivity(i);
            } else if (item.getPolicies().getReportState() == 14) {
                i = new Intent(PrincipalActivity.this, VehicleOdometer.class);
                app_preferences.edit().putString("odometro", "cancela").apply();
                i.putExtra("isCancelada", true);
                startActivity(i);
            } else if (item.getPolicies().getReportState() == 15) {
                i = new Intent(PrincipalActivity.this, VehicleOdometer.class);
                app_preferences.edit().putString("odometro", "ajuste").apply();
                startActivity(i);
            } else if (item.getPolicies().getReportState() == 21) {
                app_preferences.edit().putString("solofotos", "1").apply();
                getfotosFaltantes fotos = new getfotosFaltantes();
                fotos.execute();
            } else {
                if (item.getPolicies().getState().getId() == 15) {
                    i = new Intent(PrincipalActivity.this, InfoCancelActivity.class);
                } else {
                    i = new Intent(PrincipalActivity.this, DetallesActivity.class);
                }
                startActivity(i);
            }
        }
    }

    @Override
    public void runInt2(int value) {

        //Clic into row to launch activity
        InfoClient item = result.get(value);
        //static class -- set client in this part
        IinfoClient.setInfoClientObject(item);
        IinfoClient.getInfoClientObject().getClient().setCelphone(app_preferences.getString("Celphone", "0"));

        tok_basic = item.getClient().getToken();
        idpoliza = IinfoClient.getInfoClientObject().getPolicies().getId();
        getNewQuotation(item.getPolicies().getId());
    }

//TODO - lanzamos vista para nueva cuota renovacion ------------------------------------------------
    public void getNewQuotation(final int insuranceId) {
        //thread to call api and save furst odometer
        AsyncTask<Void, Void, Void> getQuotation = new AsyncTask<Void, Void, Void>() {
            ProgressDialog progress = new ProgressDialog(PrincipalActivity.this);
            String ErrorCode = "";
            String resp = "";

            @Override
            protected void onPreExecute() {
                progress.setTitle("Consultando información");
                progress.setMessage("Espere por favor");
                //progress.setIcon(R.drawable.miituo);
                progress.setCancelable(false);
                progress.setIndeterminate(true);
                progress.show();
            }

            @Override
            protected Void doInBackground(Void... params) {
                try {
                    ApiClient api = new ApiClient(PrincipalActivity.this);
                    resp = api.getQuotation("Quotation/GetPreQuotationInfo/" + insuranceId, PrincipalActivity.this);
//                    resp = api.getQuotation( "Quotation/GetPreQuotationInfo/3",PrincipalActivity.this);
                } catch (Exception ex) {
                    ErrorCode = ex.getMessage();
                    this.cancel(true);
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                progress.dismiss();
                if (resp != null && !resp.equalsIgnoreCase("null")) {
                    try {
                        JSONObject jo = new JSONObject(resp);
                        Intent i = new Intent(PrincipalActivity.this, MainActivity.class);
                        i.putExtra("idPush", "2");
                        DateFormat df = new SimpleDateFormat("dd 'de' MMMM 'del' yyyy");
                        Calendar c = Calendar.getInstance();
                        Date fechacadena = IinfoClient.InfoClientObject.getPolicies().getVigencyDate();
                        c.setTime(fechacadena);
                        c.add(Calendar.DATE, -1);
                        fechacadena = c.getTime();
                        String reportDate = df.format(fechacadena);
                        i.putExtra("fromPrincipal", true);
                        i.putExtra("tarifa", jo.getString("strNewRate") + "/" +
                                jo.getString("PolicyCost").substring(0, jo.getString("PolicyCost").indexOf(".")) + "/" + reportDate +
                                "/" + IinfoClient.InfoClientObject.getPolicies().getCoverage().getDescription());
                        startActivity(i);
                    } catch (Exception e) {
                        ErrorCode = e.getMessage();
                        this.cancel(true);
                    }
                } else {
                    Toast msg = Toast.makeText(getApplicationContext(), "Error al cargar información", Toast.LENGTH_LONG);
                    msg.show();
                }
            }

            @Override
            protected void onCancelled() {
                if (progress != null) {
                    progress.dismiss();
                }
                Toast msg = Toast.makeText(getApplicationContext(), "Ocurrio un Error:" + ErrorCode, Toast.LENGTH_LONG);
                msg.show();
            }
        };
        getQuotation.execute();
    }

//TODO - recuperamos fotos faltantes auditoria -----------------------------------------------------
    public class getfotosFaltantes extends AsyncTask<String, Void, Void> {

        ProgressDialog progress = new ProgressDialog(PrincipalActivity.this);
        String ErrorCode = "", Celphone = "";


        //Si cnacelo...inicio nuevo layout para reiniciar y borro de preferences
        @Override
        protected void onCancelled() {
            super.onCancelled();

            progress.dismiss();
            Toast message = Toast.makeText(getApplicationContext(), "Error al actualizar. Intenté más tarde.", Toast.LENGTH_LONG);
            message.show();
        }

        //Coloca datos de progress antes de arrancar
        //This gonna help for me
        @Override
        protected void onPreExecute() {
            progress.setTitle("Procesando");
            progress.setMessage("Actualizando póliza...");
            //progress.setIcon(R.drawable.miituo);
            progress.setIndeterminate(true);
            progress.setCancelable(false);
            progress.show();
        }

        @Override
        protected Void doInBackground(String... strings) {

            ApiClient client = new ApiClient(PrincipalActivity.this);

            try {
                fotosfaltantesList = client.getFotosFaltantes("ImageSendProcess/GetFotosFaltantes", idpoliza, tok_basic);
            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {

            progress.dismiss();

            //checar si son fotos vehiculo o solo foto odometro...
            if (fotosfaltantesList.size() == 1) {
                if (fotosfaltantesList.get(0).getId() == 5) {
                    //odometro, lanzamos odometro, solo pide esa
                    app_preferences.edit().putString("solofotos", "1").apply();
                    Intent i = new Intent(PrincipalActivity.this, VehicleOdometer.class);
                    startActivity(i);
                } else {
                    app_preferences.edit().putString("solofotos", "1").apply();
                    Intent i = new Intent(PrincipalActivity.this, VehiclePictures.class);
                    startActivity(i);
                }
            } else {
                app_preferences.edit().putString("solofotos", "1").apply();
                Intent i = new Intent(PrincipalActivity.this, VehiclePictures.class);
                startActivity(i);
            }
        }
    }

//TODO - call Atlas action -------------------------------------------------------------------------
    public void llamarAtlas(final InfoClient v) {

        //startActivity(new Intent(this, MapsActivity.class));
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.AlertDialogTheme);
        builder.setTitle(v.getPolicies().getInsuranceCarrier().getName());
        builder.setMessage("¡Reportar siniestro!");
        builder.setPositiveButton("Llamar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                alerta.dismiss();
                try {
                    String noTel = "";
                    switch (v.getPolicies().getInsuranceCarrier().getId()) {
                        case Aseguradoras.atlas:
                            noTel = getString(R.string.tel_atlas);
                            break;
                        case Aseguradoras.ana:
                            noTel = getString(R.string.tel_ana);
                            break;
                        case Aseguradoras.gnp:
                            noTel = getString(R.string.tel_gnp);
                            break;
                    }
                    Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", noTel, null));
                    startActivity(intent);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                alerta.dismiss();
            }
        });
        alerta = builder.create();
        alerta.show();
    }

    public void showViews(final boolean b) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (b) {
                    sinPolizas.setVisibility(View.VISIBLE);
                    imgSinPolizas.setVisibility(View.VISIBLE);
                    swipeContainer.setVisibility(View.GONE);
                    sinPolizas.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            new AvailableSiteSync(PrincipalActivity.this, new SimpleCallBack() {
                                @Override
                                public void run(boolean status, String res) {

                                    if (!status) {
                                        final AlertDialog.Builder builder = new AlertDialog.Builder(PrincipalActivity.this);
                                        builder.setTitle("Alerta");
                                        builder.setMessage("Error de conexión");
                                        builder.setCancelable(false);
                                        builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                alertaCotiz.dismiss();
                                            }
                                        });
                                        alertaCotiz = builder.create();
                                        alertaCotiz.show();
                                    } else {
                                        if (res.equalsIgnoreCase("0")) {
//                                            Intent i = new Intent(PrincipalActivity.this, CotizarAutoActivity.class);
//                                            i.putExtra("isSimulacion", false);
//                                            startActivity(i);
                                            if(Build.VERSION.SDK_INT <= Build.VERSION_CODES.LOLLIPOP_MR1){
                                                Intent i = new Intent(PrincipalActivity.this, CotizarAutoActivity.class);
                                                i.putExtra("isSimulacion", false);
                                                startActivity(i);
                                            }else{
                                                Intent i = new Intent(PrincipalActivity.this, CotizaActivity.class);
                                                i.putExtra("cliente",true);
                                                startActivity(i);
                                            }
                                        } else {
                                            Intent i = new Intent(PrincipalActivity.this, UnavailableSite.class);
                                            i.putExtra("status", res);
                                            startActivity(i);
                                        }
                                    }
                                }
                            }).execute();
//                            Intent i = new Intent(PrincipalActivity.this,CotizarAutoActivity.class);
//                            startActivity(i);
//                            String urlString="https://miituo.com";
//                            Intent intent=new Intent(Intent.ACTION_VIEW, Uri.parse(urlString));
//                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                            intent.setPackage("com.android.chrome");
//                            try {
//                                startActivity(intent);
//                            } catch (ActivityNotFoundException ex) {
//                                // Chrome browser presumably not installed so allow user to choose instead
//                                intent.setPackage(null);
//                                startActivity(intent);
//                            }
                        }
                    });
                    resumen.setText("Aun no tienes pólizas contratadas,\n" +
                            "puedes cotizar una en este momento");
                } else {
                    sinPolizas.setVisibility(View.GONE);
                    imgSinPolizas.setVisibility(View.GONE);
                    swipeContainer.setVisibility(View.VISIBLE);
                    sinPolizas.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                        }
                    });
                    resumen.setText("Estas son tus pólizas contratadas");
                }
            }
        });
    }

    public void launchAlert(String res){
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(PrincipalActivity.this);
        builder.setTitle("Atención");
        builder.setMessage(res);
        builder.setPositiveButton("Ok", (dialog, which) -> {
            SharedPreferences.Editor editor = app_preferences.edit();
            editor.putString("Celphone", "");
            editor.putString("sesion", "");
            editor.apply();
        });
        android.app.AlertDialog alerta = builder.create();
        alerta.show();
    }

    public void getPolizasData(String telefono){
        String url = "InfoClientMobil/Celphone/"+telefono;
        new GetPoliciesData(url, PrincipalActivity.this, new SimpleCallBack() {
            @Override
            public void run(boolean status, String res) {
                if (!status){
                    String data[] = res.split("@");
                    launchAlert(data[1]);
                }else{
                    //tenemos polizas, recuperamos list y mandamos a sms...
                    SharedPreferences.Editor editor = app_preferences.edit();
                    editor.putString("polizas", res);
                    editor.putString("Celphone", telefono);
                    editor.apply();

                    Gson parseJson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'hh:mm:ss").create();
                    List<InfoClient> InfoList = parseJson.fromJson(res, new TypeToken<List<InfoClient>>() {
                    }.getType());

                    final GlobalActivity globalVariable = (GlobalActivity) getApplicationContext();
                    globalVariable.setPolizas(InfoList);

                    result = InfoList;
                    if (result.size() < 1) {
                        showViews(true);
                    }else{
                        showViews(false);
                        String na = result.get(0).getClient().getName();
                        app_preferences.edit().putString("nombre", na).apply();

                        vList = (ListView) findViewById(R.id.listviewinfoclient);
                        removeInvalidPolicies();
                        vadapter = new VehicleModelAdapter(getApplicationContext(), result, typeface, starttime, PrincipalActivity.this);
                        vList.setAdapter(vadapter);
                        vadapter.notifyDataSetChanged();
                        swipeContainer.setRefreshing(false);
                    }
                }
            }
        }).execute();
    }

    /*public void validarVistas(boolean fin){
        if (fin) {
            showViews(fin);
        } else {
            Toast message = Toast.makeText(getApplicationContext(), "Error al actualizar. Intenté más tarde.", Toast.LENGTH_LONG);
            message.show();
            swipeContainer.setRefreshing(false);
        }
    }*/

    //**************************************************************************************************
    /*private class threadtosync {
        AsyncTask<String, Void, Void> Sync = new AsyncTask<String, Void, Void>() {
            ProgressDialog progress = new ProgressDialog(PrincipalActivity.this);
            String ErrorCode = "", Celphone = "";
            boolean fin = false;

            //Si cnacelo...inicio nuevo layout para reiniciar y borro de preferences
            @Override
            protected void onCancelled() {
                super.onCancelled();
                progress.dismiss();
                if (fin) {
                    showViews(fin);
                } else {
                    Toast message = Toast.makeText(getApplicationContext(), "Error al actualizar. Intenté más tarde.", Toast.LENGTH_LONG);
                    message.show();
                    swipeContainer.setRefreshing(false);
                }
            }

            //Coloca datos de progress antes de arrancar
            //This gonna help for me
            @Override
            protected void onPreExecute() {
                showViews(false);
                progress.setTitle("Información");
                progress.setMessage("Actualizando pólizas...");
                //progress.setIcon(R.drawable.miituo);
                progress.setIndeterminate(true);
                progress.setCancelable(false);
                fin = false;
                //progress.show();
            }

            //va por listas de clientes
            //result es un List<InfoClient>
            @Override
            protected Void doInBackground(String... params) {
                ApiClient client = new ApiClient(PrincipalActivity.this);
                try {
                    result = client.getInfoClient("InfoClientMobil/Celphone/" + params[0], PrincipalActivity.this);
                    if (result == null) {
                        progress.dismiss();
                        ErrorCode = "Estamos haciendo algunas actualizaciones, si sigues teniendo problemas para ingresar, contáctate con nosotros.";
                        this.cancel(true);
                    } else {
                        //guardar en Celphone el telefono que viene en params[0]
                        //get name
                        if (result.size() < 1) {
                            fin = true;
                            this.cancel(true);
                        } else {
                            String na = result.get(0).getClient().getName();
                            app_preferences.edit().putString("nombre", na).apply();

                            //recover iamges----------------------------------------------------------------
                            for (int i = 0; i < result.size(); i++) {
                                String filePathString = getExternalFilesDir(Environment.DIRECTORY_PICTURES) + File.separator + "frontal_" + result.get(i).getPolicies().getNoPolicy() + ".png";
                                File f = new File(filePathString);

                                //Si no hay imagen...intentamos recuperarla...
                                if (!f.exists() && result.get(i).getPolicies().isHasVehiclePictures()) {
                                    try {
                                        String id = result.get(i).getPolicies().getId() + "";
                                        int idCarrier = result.get(i).getPolicies().getInsuranceCarrier().getId();

                                        Bitmap m = client.DownloadPhoto("ImageSendProcess/GetFrontImageCarApp/" + id, PrincipalActivity.this, result.get(i).getClient().getToken());
//                                    Bitmap m = client.DownloadPhotoMultiaseguradora("ImageSendProcess/Insurance/" + idCarrier+
//                                           "/GetFrontImageCarApp/"+id , SyncActivity.this,tokencliente);
                                        if (m != null) {
                                            //before load...save it in order to not load again...
                                            FileOutputStream out = null;
                                            try {
                                                out = new FileOutputStream(filePathString);
                                                m.compress(Bitmap.CompressFormat.PNG, 50, out); // bmp is your Bitmap instance
                                                // PNG is a lossless format, the compression factor (100) is ignored
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            } finally {
                                                try {
                                                    if (out != null) {
                                                        out.close();
                                                    }
                                                } catch (IOException e) {
                                                    e.printStackTrace();
                                                }
                                            }
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                            borrarDB();

                            for (int i = 0; i < result.size(); i++) {
                                InsurancePolicyDetail polizatemp = result.get(i).getPolicies();
                                ClientMovil clienttemp = result.get(i).getClient();
                                //String inse = inserta.execute(modelBase.FeedEntryUsuario.TABLE_NAME,clienttemp.getName(),clienttemp.getMotherName(),clienttemp.getLastName(),clienttemp.getCelphone(),clienttemp.getToken()).get();
                                String inse = saveData(modelBase.FeedEntryUsuario.TABLE_NAME, clienttemp.getName(), clienttemp.getMotherName(), clienttemp.getLastName(), clienttemp.getCelphone(), clienttemp.getToken());

                                if (!inse.equals("")) {

                                    String date = new SimpleDateFormat("dd-MM-yyyy").format(polizatemp.getLimitReportDate());

                                    //String insepoli = insertapoli.execute(
                                    String insepoli = saveData(
                                            modelBase.FeedEntryPoliza.TABLE_NAME,
                                            clienttemp.getId() + "",
                                            polizatemp.getInsuranceCarrier().getName(),
                                            polizatemp.getLastOdometer() + "",
                                            polizatemp.getNoPolicy(),
                                            polizatemp.isHasOdometerPicture() + "",
                                            polizatemp.isHasVehiclePictures() + "",
                                            polizatemp.getReportState() + "",
                                            polizatemp.getRate() + "",
                                            date + "",
                                            polizatemp.getPaymentType() + "");

                                    if (!insepoli.equals("")) {

                                        VehicleMovil carritotemp = polizatemp.getVehicle();
                                        //String insecarrito = insertacar.execute(
                                        String insecarrito = saveData(
                                                modelBase.FeedEntryVehiculo.TABLE_NAME,
                                                polizatemp.getNoPolicy(),
                                                carritotemp.getBrand().getDescription(),
                                                carritotemp.getCapacity() + "",
                                                carritotemp.getColor(),
                                                carritotemp.getDescription().getDescription(),
                                                carritotemp.getModel().getModel() + "",
                                                carritotemp.getPlates(),
                                                carritotemp.getType().getDescription(),
                                                carritotemp.getSubtype().getDescription()
                                        );

                                        if (!insecarrito.equals("")) {
                                            //Log.w("Ok","Datos guardados correctamente");
                                        }
                                    }
                                }
                            }

                            //Log.e("Aqui", "Actualizando datos de nuevo");
                            Celphone = params[0];
                        }
                    }
                } catch (IOException ex) {
                    ErrorCode = ex.getMessage();
                    this.cancel(true);
                }
                return null;
            }

            //Guardo y muestro infoClientList layout
            //recupero polizas
            @Override
            protected void onPostExecute(Void aVoid) {
                //guardo telefono en preferences
                SharedPreferences.Editor editor = app_preferences.edit();
                editor.putString("Celphone", Celphone);
                editor.commit();
                //progress.dismiss();

                String sesion = app_preferences.getString("sesion", "null");
                //String tuto = sharedPreferences.getString("tutohecho","null");

                //RELOAD ADAPATER
                vList = (ListView) findViewById(R.id.listviewinfoclient);
                removeInvalidPolicies();
                vadapter = new VehicleModelAdapter(getApplicationContext(), result, typeface, starttime, PrincipalActivity.this);
                vList.setAdapter(vadapter);
                vadapter.notifyDataSetChanged();
                swipeContainer.setRefreshing(false);
            }

            public String saveData(String... strings) {
                //Aqui va el nombre de la tala
                String val = strings[0];
                //Log.w("Here",val);

                switch (val) {

                    case modelBase.FeedEntryUsuario.TABLE_NAME:
                        // Gets the data repository in write mode
                        SQLiteDatabase db2 = DBaseMethods.base.getWritableDatabase();

                        // Create a new map of values, where column names are the keys
                        ContentValues values2 = new ContentValues();
                        //values.put(modelBase.FeedEntryArticle.COLUMN_ID, strings[6]);
                        values2.put(modelBase.FeedEntryUsuario.COLUMN_NAME, strings[1]);
                        values2.put(modelBase.FeedEntryUsuario.COLUMN_MOTHERNAME, strings[2]);
                        //values2.put(modelBase.FeedEntryUsuario.COLUMN_UBI, strings[3]);
                        values2.put(modelBase.FeedEntryUsuario.COLUMN_LASTNAME, strings[3]);
                        values2.put(modelBase.FeedEntryUsuario.COLUMN_CELPHONE, strings[4]);
                        values2.put(modelBase.FeedEntryUsuario.COLUMN_TOKEN, strings[5]);

                        // Insert the new row, returning the primary key value of the new row
                        //Just change name table and the values....
                        long newRowId2 = db2.insert(val, null, values2);
                        try {
                            Thread.sleep(50);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                        return "" + newRowId2;

                    case modelBase.FeedEntryPoliza.TABLE_NAME:
                        // Gets the data repository in write mode
                        SQLiteDatabase db = DBaseMethods.base.getWritableDatabase();

                        // Create a new map of values, where column names are the keys
                        ContentValues values = new ContentValues();
                        //values.put(modelBase.FeedEntryArticle.COLUMN_ID, strings[6]);
                        values.put(modelBase.FeedEntryPoliza.COLUMN_IDUSER, strings[1]);
                        values.put(modelBase.FeedEntryPoliza.COLUMN_INSURANCE, strings[2]);
                        values.put(modelBase.FeedEntryPoliza.COLUMN_LASTODOMETER, strings[3]);
                        values.put(modelBase.FeedEntryPoliza.COLUMN_NOPOLICY, strings[4]);
                        values.put(modelBase.FeedEntryPoliza.COLUMN_ODOMETERPIE, strings[5]);
                        values.put(modelBase.FeedEntryPoliza.COLUMN_VEHICLEPIE, strings[6]);
                        values.put(modelBase.FeedEntryPoliza.COLUMN_REPORT_STATE, strings[7]);
                        values.put(modelBase.FeedEntryPoliza.COLUMN_RATE, strings[8]);
                        values.put(modelBase.FeedEntryPoliza.COLUMN_LIMIT_DATE, strings[9]);
                        values.put(modelBase.FeedEntryPoliza.COLUMN_PAYMENT, strings[10]);

                        // Insert the new row, returning the primary key value of the new row
                        //Just change name table and the values....
                        long newRowId = db.insert(val, null, values);
                        try {
                            Thread.sleep(50);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                        return "" + newRowId;

                    case modelBase.FeedEntryVehiculo.TABLE_NAME:
                        // Gets the data repository in write mode
                        SQLiteDatabase dbvei = DBaseMethods.base.getWritableDatabase();

                        // Create a new map of values, where column names are the keys
                        ContentValues valuesvei = new ContentValues();
                        //values.put(modelBase.FeedEntryArticle.COLUMN_ID, strings[6]);
                        valuesvei.put(modelBase.FeedEntryVehiculo.COLUMN_POLIZA, strings[1]);
                        valuesvei.put(modelBase.FeedEntryVehiculo.COLUMN_BRAND, strings[2]);
                        valuesvei.put(modelBase.FeedEntryVehiculo.COLUMN_CAPACITY, strings[3]);
                        valuesvei.put(modelBase.FeedEntryVehiculo.COLUMN_COLOR, strings[4]);
                        valuesvei.put(modelBase.FeedEntryVehiculo.COLUMN_DESCRIPTION, strings[5]);
                        valuesvei.put(modelBase.FeedEntryVehiculo.COLUMN_MODEL, strings[6]);
                        valuesvei.put(modelBase.FeedEntryVehiculo.COLUMN_PLATES, strings[7]);
                        valuesvei.put(modelBase.FeedEntryVehiculo.COLUMN_TYPE, strings[8]);
                        valuesvei.put(modelBase.FeedEntryVehiculo.COLUMN_SUBTYPE, strings[8]);

                        // Insert the new row, returning the primary key value of the new row
                        //Just change name table and the values....
                        long newRowvei = dbvei.insert(val, null, valuesvei);
                        try {
                            Thread.sleep(50);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                        return "" + newRowvei;

                    default:
                        break;
                }

                return "";
            }

            public void borrarDB() {

                SQLiteDatabase db = DBaseMethods.base.getWritableDatabase();

                db.execSQL("DELETE FROM " + modelBase.FeedEntryUsuario.TABLE_NAME);
                db.execSQL("DELETE FROM " + modelBase.FeedEntryPoliza.TABLE_NAME);
                db.execSQL("DELETE FROM " + modelBase.FeedEntryVehiculo.TABLE_NAME);
                db.close();

            }
        };
    }*/
}


