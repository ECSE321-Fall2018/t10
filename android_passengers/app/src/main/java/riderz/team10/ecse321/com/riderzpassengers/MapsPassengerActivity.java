package riderz.team10.ecse321.com.riderzpassengers;

import android.content.Intent;
import android.location.Geocoder;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsPassengerActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private Geocoder geocoder;
    private GeoLocation geoLocation;



    private LatLng startLocation;
    private LatLng stopLocation;


    private String startAddress;
    private String stopAddress;

    private final int PLACE_AUTOCOMPLETE_REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps_passenger);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        EditText searchText = (EditText) findViewById(R.id.search_text);
        ImageView searchIcon = (ImageView) findViewById(R.id.search_image);

        searchText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createAutoCompleteIntent();
            }
        });

        searchIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createAutoCompleteIntent();
            }
        });

//        searchText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
//            @Override
//            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
//                if (i == EditorInfo.IME_ACTION_DONE
//                        || i == EditorInfo.IME_ACTION_SEARCH
//                        || keyEvent.getAction() == KeyEvent.ACTION_DOWN
//                        || keyEvent.getAction() == KeyEvent.KEYCODE_ENTER){
//
//                    EditText searchText = (EditText) findViewById(R.id.search);
//                    startAddress = searchText.getText().toString();
//                    startLocation = geoLocation.getLocationFromAddress(startAddress, geocoder);
//
//                    Log.e("Debug", "Address: " + startAddress);
//
//                    if (startLocation.equals(null)) {
//                        Toast.makeText(getApplicationContext(),"The Address Was Invalid",Toast.LENGTH_LONG).show();
//                        return false;
//                    }
//
//                    double startLatitude = startLocation.latitude;
//                    double startLongitude = startLocation.longitude;
//
//                    Log.e("Debug", "Latitude: " + startLatitude);
//                    Log.e( "Debug", "Longitude: " +startLongitude);
//
//                    LatLng start = new LatLng(startLatitude, startLongitude);
//                    mMap.addMarker(new MarkerOptions().position(start).title("Marker at start Address"));
//                    mMap.moveCamera(CameraUpdateFactory.newLatLng(start));
//                }
//
//                return false;
//            }
//        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PLACE_AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {

                Place place = PlaceAutocomplete.getPlace(this, data);
                startLocation = place.getLatLng();

                double startLatitude = startLocation.latitude;
                double startLongitude = startLocation.longitude;

                LatLng start = new LatLng(startLatitude, startLongitude);
                mMap.addMarker(new MarkerOptions().position(start).title("Marker at start Address"));
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(start, 15));

//                Button confirmationButton = (Button) findViewById(R.id)

                Log.i("debug", "Place: " + place.getName());
            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                Status status = PlaceAutocomplete.getStatus(this, data);
                Toast.makeText(getApplicationContext(),"The Address Was Invalid",Toast.LENGTH_LONG).show();
                Log.i("debug", status.getStatusMessage());

            } else if (resultCode == RESULT_CANCELED) {
                // The user canceled the operation.
            }
        }
    }

    private void createAutoCompleteIntent(){
        try {
            Intent intent = new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_OVERLAY)
                            .build(this);
            startActivityForResult(intent, PLACE_AUTOCOMPLETE_REQUEST_CODE);
        } catch (GooglePlayServicesNotAvailableException e) {
            e.printStackTrace();
        } catch (GooglePlayServicesRepairableException e) {
            e.printStackTrace();
        }
    }

//    EditText searchText = (EditText) findViewById(R.id.search);
//                    startAddress = searchText.getText().toString();
//                    startLocation = geoLocation.getLocationFromAddress(startAddress, geocoder);
//
//                    Log.e("Debug", "Address: " + startAddress);
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

        geoLocation = new GeoLocation();
        geocoder = new Geocoder(this);
        mMap = googleMap;

//        double stopLatitude = stopLocation.latitude;
//        double stopLongitude = stopLocation.longitude;
//        Log.e("Debug", "Latitude: " + stopLatitude);
//        Log.e( "Debug", "Longitude: " +stopLongitude);

    }
}
