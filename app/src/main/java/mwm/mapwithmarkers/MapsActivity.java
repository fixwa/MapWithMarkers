package mwm.mapwithmarkers;

import android.*;
import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import mwm.mapwithmarkers.request.RemoteFetch;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {

    private Context context;
    private FusedLocationProviderClient mFusedLocationClient;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private GoogleMap mMap;

    private GPSTracker gpsTracker;
    private Location mLocation;
    double latitude, longitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);

//        gpsTracker = new GPSTracker();
//        mLocation = gpsTracker.getLocation(getApplicationContext());
//
//        latitude = mLocation.getLatitude();
//        longitude = mLocation.getLongitude();

        mapFragment.getMapAsync(this);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            googleMap.setMyLocationEnabled(true);

            LocationManager locationManager = (LocationManager)
                    getSystemService(Context.LOCATION_SERVICE);
            Criteria criteria = new Criteria();

            Location currentLocation = locationManager.getLastKnownLocation(locationManager.getBestProvider(criteria, false));

            LatLng sydney = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());

            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, 13));

        } else {
            PermissionUtils.requestPermission(this, LOCATION_PERMISSION_REQUEST_CODE, Manifest.permission.ACCESS_FINE_LOCATION, true);
        }

        updateMarkersData(googleMap);

    }

    private void updateMarkersData(final GoogleMap googleMap) {

        final Handler handler = new Handler();
        new Thread() {
            public void run() {
                final JSONObject json = RemoteFetch.getJSON("Cordoba, AR");
                if (json == null) {
                    handler.post(new Runnable() {
                        public void run() {
                            Toast.makeText(context, "No se encuentra", Toast.LENGTH_LONG).show();
                        }
                    });
                } else {
                    handler.post(new Runnable() {
                        public void run() {
                            // Iterate Json response and create Markers in the Google' map

                            try {
                                JSONArray markersData = json.getJSONArray("data");
                                for (int i=0; i < markersData.length(); i++) {
                                    JSONObject markerData = markersData.getJSONObject(i);
                                    LatLng latLng = new LatLng(
                                            Float.parseFloat(markerData.getString("latitude")),
                                            Float.parseFloat(markerData.getString("longitude"))
                                    );
                                    googleMap.addMarker(new MarkerOptions().position(latLng).title(markerData.getString("title")));
                                }
                            } catch (JSONException e) {
                                return;
                            }
                        }
                    });
                }
            }
        }.start();
    }
}
