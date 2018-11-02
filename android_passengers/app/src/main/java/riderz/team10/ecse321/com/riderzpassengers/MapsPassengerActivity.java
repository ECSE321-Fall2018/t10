package riderz.team10.ecse321.com.riderzpassengers;

import android.content.Intent;
import android.graphics.Color;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.android.PolyUtil;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class MapsPassengerActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private Button confirmationButton;

    private TextView searchTextStart;
    private TextView searchTextEnd;

    private LatLng markerLocation;
    private Boolean isStarting;
    private String msg;

    protected String origin;
    protected String destination;
    protected final String KEY = "AIzaSyARBA8OOAyllhaTKzyroPqIJW8I47b7Nv8" ;

    private final int PLACE_AUTOCOMPLETE_REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps_passenger);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        searchTextStart = (TextView) findViewById(R.id.search_text_start);
        searchTextEnd = (TextView) findViewById(R.id.search_text_end);
        ImageView searchIconStart = (ImageView) findViewById(R.id.search_image_start);
        ImageView searchIconEnd = (ImageView) findViewById(R.id.search_image_end);
        confirmationButton = (Button) findViewById(R.id.confirmation_button);


        confirmationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                asyncHttpRequest();
                Toast.makeText(getApplicationContext(), "Latitude: " +  markerLocation.latitude + "\nLongitude: "  + markerLocation.longitude,Toast.LENGTH_LONG).show();
            }
        });

        searchTextStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isStarting = true;
                createAutoCompleteIntent();
            }
        });

        searchIconStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isStarting = true;
                createAutoCompleteIntent();
            }
        });

        searchTextEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isStarting = false;
                createAutoCompleteIntent();
            }
        });

        searchIconEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isStarting = false;
                createAutoCompleteIntent();
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PLACE_AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {

                Place place = PlaceAutocomplete.getPlace(this, data);
                markerLocation = place.getLatLng();


                double latitude = markerLocation.latitude;
                double longitude = markerLocation.longitude;

                LatLng point = new LatLng(latitude, longitude);
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(point, 15));


                if(isStarting){
                    mMap.addMarker(new MarkerOptions().position(point)
                            .title(place.getAddress().toString()));
                    searchTextStart.setText(place.getAddress());
                    origin = latitude + "," + longitude;
                }
                else {

                    // clear map
                    mMap.clear();

                    mMap.addMarker(new MarkerOptions().position(point)
                            .title(place.getAddress().toString())
                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
                    searchTextEnd.setText(place.getAddress());
                    destination = latitude + "," + longitude;

                    if(origin != null){
                        String [] originArray = origin.split(",");
                        LatLng originPoint = new LatLng(new Double(originArray[0]), new Double(originArray[1]));
                        mMap.addMarker(new MarkerOptions().position(originPoint)
                                .title(searchTextStart.getText().toString()));
                    }
                }

                confirmationButton.setVisibility(View.VISIBLE);

                Log.i("debug", "Place: " + place.getName());
            }

        } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
            Status status = PlaceAutocomplete.getStatus(this, data);
            Toast.makeText(getApplicationContext(),"The Address Was Invalid",Toast.LENGTH_LONG).show();
            Log.i("debug", status.getStatusMessage());

        } else if (resultCode == RESULT_CANCELED) {
            // The user canceled the operation.
        }
    }

    private void createAutoCompleteIntent(){
        try {
            Intent intent = new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_FULLSCREEN)
                            .build(this);
            startActivityForResult(intent, PLACE_AUTOCOMPLETE_REQUEST_CODE);
        } catch (GooglePlayServicesNotAvailableException e) {
            e.printStackTrace();
        } catch (GooglePlayServicesRepairableException e) {
            e.printStackTrace();
        }
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
        mMap = googleMap;
    }

    private void asyncHttpRequest() {
        // Add parameters
        final RequestParams params = new RequestParams();
        params.add("origin", origin);
        params.add("destination", destination);
        params.add("key", KEY);

        Log.e("debug", origin);
        Log.e("debug", destination);
        Log.e("debug", KEY);

        // Execute asynchronous http request on another thread other than main
        new Thread(new Runnable() {
            @Override
            public void run() {
                final String getDirections = "https://maps.googleapis.com/maps/api/directions/json";
                AsyncHttpClient client = new AsyncHttpClient();
                client.setTimeout(2500);
                client.get(getDirections, params, new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                        // Check that we obtained a valid response from the server
                        if (responseBody.length == 0) {
                            // Response is invalid
                            Log.e("debug", "Failed to get directions");
                            msg = "Failed to get directions";
                        } else {
                            // Response is valid
                            msg = "";
                            try {
                                // Parse JSON object and set EditText where valid
                                JSONObject object = new JSONObject(new String(responseBody));
                                JSONArray steps = object.getJSONArray("routes").getJSONObject(0).getJSONArray("legs").getJSONObject(0).getJSONArray("steps");
                                ArrayList<String> points = new ArrayList<>();

                                int length = steps.length();
                                for(int i = 0; i < length; i++){
                                    points.add(steps.getJSONObject(i).getJSONObject("polyline").getString("points"));
                                }

                                double lat = steps.getJSONObject(steps.length()/2).getJSONObject("start_location").getDouble("lat");
                                double lng = steps.getJSONObject(steps.length()/2).getJSONObject("start_location").getDouble("lng");

                                LatLng point = new LatLng(lat, lng);
                                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(point, 14));

                                for(int i = 0; i < length; i++){
                                    PolylineOptions options = new PolylineOptions();
                                    options.color(Color.BLUE);
                                    options.width(10);
                                    options.addAll(PolyUtil.decode(points.get(i)));

                                    mMap.addPolyline(options);

                                }

                                // Fetch origin and destination
                                String [] originArray = origin.split(",");
                                LatLng originPoint = new LatLng(new Double(originArray[0]), new Double(originArray[1]));

                                String [] destinationArray = destination.split(",");
                                LatLng destinationPoint = new LatLng(new Double(destinationArray[0]), new Double(destinationArray[1]));


                                LatLngBounds.Builder builder = new LatLngBounds.Builder();
                                builder.include(originPoint);
                                builder.include(destinationPoint);
                                LatLngBounds bounds = builder.build();

                                CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, 50);
                                mMap.animateCamera(cu, new GoogleMap.CancelableCallback(){
                                    public void onCancel(){}
                                    public void onFinish(){
                                        CameraUpdate zout = CameraUpdateFactory.zoomBy(-1);
                                        mMap.animateCamera(zout);
                                    }
                                });

                            } catch (JSONException e) {
                                Log.e("debug", "Failed to parse JSON object");
                            }
                        }
                        // Display error message
                        //errorMsg.setText(msg);
                        //errorMsg.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                        // Most possible error is statusCode 500
                        Log.e("debug", "Server error - Failed to contact server");
                        //errorMsg.setText("Failed to contact server");
                        //errorMsg.setVisibility(View.VISIBLE);
                    }
                });
            }
        }).run();
    }
}
