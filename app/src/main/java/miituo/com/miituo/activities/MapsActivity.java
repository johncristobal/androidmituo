package miituo.com.miituo.activities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Handler;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.util.List;
import java.util.Locale;

import miituo.com.miituo.R;

public class MapsActivity extends BaseActivity implements OnMapReadyCallback, LocationListener, GoogleMap.OnMyLocationButtonClickListener, GoogleMap.OnMyLocationClickListener {

    private GoogleMap mMap;
    // Obtain the SupportMapFragment and get notified when the map is ready to be used.
    SupportMapFragment mapFragment;
    private LocationManager mLocationManager;
    Location location;
    private Handler mHandler;
    private TextView lbLlamar,lbDir;
    private ProgressBar progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        lbDir=(TextView)findViewById(R.id.lbDir);
        lbLlamar=(TextView)findViewById(R.id.lbLlamar);
        progress=(ProgressBar)findViewById(R.id.progress);
        mapFragment= (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        lbDir.setText("Obteniendo direccíon...\nContinua moviendote para actualizar la ubicación");
        getLocation();
        mapFragment.getMapAsync(this);
    }

    public void getLocation() {
        try {
            mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 5, (LocationListener) this);

            Handler handler2 = new Handler();
            handler2.postDelayed(new Runnable() {
                public void run() {
                    mHandler = new Handler();
                    startRepeatingTask();
                }
            }, 1000);
        }
        catch(SecurityException e) {
            e.printStackTrace();
        }
    }

    private void loadMap(boolean fromMap){
        // Add a marker in Sydney and move the camera
        if(ContextCompat.checkSelfPermission(this.getApplicationContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
            mMap.getUiSettings().setMyLocationButtonEnabled(true);
        }
        else {
            return;
        }
        if(mMap==null){
            mapFragment.getMapAsync(this);
            return;
        }
            try {
                mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000,
                        10, this);
                Criteria criteria = new Criteria();
                if(!fromMap) {
                    location = mLocationManager.getLastKnownLocation(mLocationManager.getBestProvider(criteria, false));
//                    location=mMap.getMyLocation();
                }
//                location=mLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            }catch (SecurityException e){
                e.printStackTrace();
            }
        mMap.clear();
//        LatLng sydney = new LatLng(19.447592, -99.192256);
        LatLng sydney = new LatLng(location.getLatitude(),location.getLongitude());
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mMap.getUiSettings().setZoomGesturesEnabled(true);
        mMap.getUiSettings().setRotateGesturesEnabled(true);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Tu ubicación"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney,18.1f));
        pintaDir();
    }

    private void pintaDir(){
        Geocoder geocoder;
        progress.setVisibility(View.GONE);
        List<Address> addresses;
        geocoder = new Geocoder(this, Locale.getDefault());
        try {
            addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
            String address = "Direccioón aproximada:\n\n"+
                    addresses.get(0).getThoroughfare()+" "+addresses.get(0).getSubThoroughfare();
            address += "\nCol. " + addresses.get(0).getSubLocality();
            address += "\nC.P.  " + addresses.get(0).getPostalCode();
            address += "\n" + addresses.get(0).getCountryName()+", " + addresses.get(0).getLocality();;
             lbDir.setText(address);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopRepeatingTask();
    }
    void startRepeatingTask() {
        mStatusChecker.run();
    }
    void stopRepeatingTask() {
        mHandler.removeCallbacks(mStatusChecker);
    }
    Runnable mStatusChecker = new Runnable() {
        @Override
        public void run() {
                mHandler.postDelayed(mStatusChecker, 1000);
        }
    };

    @SuppressLint("MissingPermission")
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        if(ContextCompat.checkSelfPermission(this.getApplicationContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
            mMap.getUiSettings().setMyLocationButtonEnabled(true);
            mMap.setOnMyLocationButtonClickListener(this);
            mMap.setOnMyLocationClickListener(this);
            location=mMap.getMyLocation();
        }
        loadMap(false);
    }

    @Override
    public void onLocationChanged(Location location) {
        this.location=location;
        loadMap(false);
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        try {
            mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000,
                    10, this);
        }catch (SecurityException e){
            e.printStackTrace();
        }
    }

    @Override
    public void onProviderEnabled(String provider) {
        try {
            mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000,
                    10, this);
        }catch (SecurityException e){
            e.printStackTrace();
        }
    }

    @Override
    public void onProviderDisabled(String provider) {
        try {
            toast("Por favor habilita tu GPS");
            mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000,
                    10, this);
        }catch (SecurityException e){
            e.printStackTrace();
        }
    }

    @Override
    public boolean onMyLocationButtonClick() {
        location=mMap.getMyLocation();
        loadMap(true);
        return false;
    }

    @Override
    public void onMyLocationClick(@NonNull Location location) {
        this.location=location;
        loadMap(true);
    }

//    public void getCurrentPlace(){
//            // Get the likely places - that is, the businesses and other points of interest that
//            // are the best match for the device's current location.
//            @SuppressWarnings("MissingPermission") final
//            Task<PlaceLikelihoodBufferResponse> placeResult =
//                    mPlaceDetectionClient.getCurrentPlace(null);
//            placeResult.addOnCompleteListener(new OnCompleteListener<PlaceLikelihoodBufferResponse>() {
//                        @Override
//                        public void onComplete(@NonNull Task<PlaceLikelihoodBufferResponse> task) {
//                            if (task.isSuccessful() && task.getResult() != null) {
//                                PlaceLikelihoodBufferResponse likelyPlaces = task.getResult();
//
//                                // Set the count, handling cases where less than 5 entries are returned.
//                                int count;
//                                if (likelyPlaces.getCount() < M_MAX_ENTRIES) {
//                                    count = likelyPlaces.getCount();
//                                } else {
//                                    count = M_MAX_ENTRIES;
//                                }
//
//                                int i = 0;
//                                mLikelyPlaceNames = new String[count];
//                                mLikelyPlaceAddresses = new String[count];
//                                mLikelyPlaceAttributions = new String[count];
//                                mLikelyPlaceLatLngs = new LatLng[count];
//
//                                for (PlaceLikelihood placeLikelihood : likelyPlaces) {
//                                    // Build a list of likely places to show the user.
//                                    mLikelyPlaceNames[i] = (String) placeLikelihood.getPlace().getName();
//                                    mLikelyPlaceAddresses[i] = (String) placeLikelihood.getPlace()
//                                            .getAddress();
//                                    mLikelyPlaceAttributions[i] = (String) placeLikelihood.getPlace()
//                                            .getAttributions();
//                                    mLikelyPlaceLatLngs[i] = placeLikelihood.getPlace().getLatLng();
//
//                                    i++;
//                                    if (i > (count - 1)) {
//                                        break;
//                                    }
//                                }
//
//                                // Release the place likelihood buffer, to avoid memory leaks.
//                                likelyPlaces.release();
//
//                                // Show a dialog offering the user the list of likely places, and add a
//                                // marker at the selected place.
//                                openPlacesDialog();
//
//                            } else {
//                                Log.e(TAG, "Exception: %s", task.getException());
//                            }
//                        }
//                    });
//    }
}
