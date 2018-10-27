package riderz.team10.ecse321.com.riderzdrivers;

import android.location.Geocoder;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import riderz.team10.ecse321.com.riderzdrivers.locationFromAddress.GeoLocation;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
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
        // ************** TO CHANGE BASED ON HOW HQ DOES THE PREVIOUS SCREEN *****************
        String startAddress = getIntent().getStringExtra("startAddress");
        String stopAddress = getIntent().getStringExtra("stopAddress");

        // ***********************************************************************************

        Geocoder coder = new Geocoder(this);

        GeoLocation geolocator = new GeoLocation();
        LatLng startLocation = geolocator.getLocationFromAddress(startAddress, coder);
        double startLatitude = startLocation.latitude;
        double startLongitude = startLocation.longitude;
        Log.e("Debug", "Latitude: " + startLatitude);
        Log.e( "Debug", "Longitude: " +startLongitude);

        LatLng stopLocation = geolocator.getLocationFromAddress(stopAddress, coder);
        double stopLatitude = stopLocation.latitude;
        double stopLongitude = stopLocation.longitude;

        mMap = googleMap;


        LatLng start = new LatLng(startLatitude, startLongitude);
        mMap.addMarker(new MarkerOptions().position(start).title("Marker at start Address"));


        LatLng end = new LatLng(stopLatitude, stopLongitude);
        mMap.addMarker(new MarkerOptions().position(start).title("Marker at stop Address"));

        // Need to combine the two fields below
        mMap.moveCamera(CameraUpdateFactory.newLatLng(start));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(end));
    }
}