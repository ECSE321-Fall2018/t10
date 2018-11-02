package riderz.team10.ecse321.com.riderzpassengers;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.location.Address;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.*;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;

import cz.msebera.android.httpclient.Header;

public class AdsListings extends AppCompatActivity {

    private TextView editTxt;
    private Button selectAd;
    private ArrayList<JSONObject> adsListings_list = new ArrayList<JSONObject>();
    private ArrayList<JSONObject> adsInfo_list = new ArrayList<JSONObject>();
    private ArrayList<LinearLayout> adsInfo_boxes = new ArrayList<LinearLayout>();
    private String response;

    private int expandable_id= -1;
    private HashMap<String, Integer> hash = new HashMap<>();
    private HashMap<Integer, String> hash_geolocations = new HashMap<>();

    //get data from Ryan
    final String baseUrl = "https://riderz-t10.herokuapp.com/";
    final String itinerary_baseURL = baseUrl + "getItineraryNearDestination";
    final String adsInfo_baseURL = baseUrl + "adInfo";
    private String startingLongitude;
    private String startingLatitude;
    private String endingLongitude;
    private String endingLatitude;
    final String maximumDistance = "1000000000";
    private String arrivalTime;
    private String operator = "mei";



    //define different types of layout params
    LinearLayout.LayoutParams param_1 = new LinearLayout.LayoutParams(100,100,1.0f);

    LinearLayout.LayoutParams param_2 = new LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT,
            1.0f);

    LinearLayout.LayoutParams param_3 = new LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT,
            3.0f
    );
    LinearLayout.LayoutParams layoutParams_img = new LinearLayout.LayoutParams( 200, 200);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ads_listings);

        editTxt = (TextView) findViewById(R.id.editText);
        selectAd = (Button) findViewById(R.id.selectAd);

        Bundle bundle = getIntent().getExtras();

        startingLongitude = bundle.getString("startingLongitude");
        startingLatitude = bundle.getString("startingLatitude");
        endingLongitude = bundle.getString("endingLongitude");
        endingLatitude = bundle.getString("endingLatitude");
        arrivalTime = bundle.getString("arrivalTime");

        Log.e("hello" , startingLatitude + ", " + startingLongitude + ", " + endingLatitude + ", " + endingLongitude + "," + arrivalTime );

        final LinearLayout listingsContainer = (LinearLayout) findViewById(R.id.listingsContainer);

        param_1.setMargins(10, 10, 10,10);
        param_2.setMargins(10, 10, 10,10);
        param_3.setMargins(10, 10, 10,10);
        layoutParams_img.setMargins(10, 10, 10,10);

        //HTTP GET request to get itineraries near destination
        getAdsListings_sync();

        //loop for each element from database ///adsListings_list.size()
        for(int i=0; i<adsListings_list.size(); i++){

            //Ads listings row
            final LinearLayout listingsRow = new LinearLayout(this);
            listingsRow.setOrientation(LinearLayout.HORIZONTAL);
            listingsRow.setBackgroundColor(Color.WHITE);
            listingsRow.setPadding(10, 10, 10, 10);

            final LinearLayout textviewBlock = new LinearLayout(this);
            textviewBlock.setOrientation(LinearLayout.VERTICAL);

            final LinearLayout textviewBlock_INNER = new LinearLayout(this);
            textviewBlock_INNER.setOrientation(LinearLayout.VERTICAL);

            //Ads info box
            TextView textView1 = new TextView(this);
            TextView textView2 = new TextView(this);
            TextView textView3 = new TextView(this);
            TextView textView4 = new TextView(this);
            textView1.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
            textView2.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
            textView3.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
            textView4.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);

            //Icon
            ImageView driver_icon_view = new ImageView(this);
            driver_icon_view.setLayoutParams(layoutParams_img);
            driver_icon_view.setImageResource(R.mipmap.driver_logo);

            listingsRow.setLayoutParams(param_2);
            textviewBlock.setLayoutParams(param_3);
            textviewBlock_INNER.setLayoutParams(param_3);
            try{
                int tripID = Integer.parseInt(adsListings_list.get(i).getString("tripID"));
                textviewBlock_INNER.setId(tripID);

                ReverseGeocoding reverseGeocoder = new ReverseGeocoding();
                Double startLatitude = Double.parseDouble(adsListings_list.get(i).getString("startingLatitude"));
                Double startLongitude = Double.parseDouble(adsListings_list.get(i).getString("startingLongitude"));
                Double endLatitude = Double.parseDouble(adsListings_list.get(i).getString("endingLatitude"));
                Double endLongitude = Double.parseDouble(adsListings_list.get(i).getString("endingLongitude"));

                String coordinates = "";
                coordinates += startLatitude + "," + startLongitude + "," + endLatitude + "," + endLongitude;
//                Double [] geolocations = new Double[4];
//                geolocations[0]= startLatitude;
//                geolocations[1]=startLongitude;
//                geolocations[2]=endLatitude;
//                geolocations[3]=endLongitude;
//                Log.e("LOGGGGIIINNG", geolocations[0].toString());
//                Log.e("LOGGGGIIINNG", geolocations[1].toString());
//                Log.e("LOGGGGIIINNG", geolocations[2].toString());
//                Log.e("LOGGGGIIINNG", geolocations[3].toString());
                hash_geolocations.put(tripID, coordinates);
                Log.e("LOGGER", hash_geolocations.toString());

                Address addressStart = reverseGeocoder.reverseLookup(this, startLatitude, startLongitude);
                Address addressEnd = reverseGeocoder.reverseLookup(this, endLatitude, endLongitude);
//TESTING METHODS
//                Address addressStart = reverseGeocoder.reverseLookup(getApplicationContext(),45.4724431,-73.5928623);
//                Address addressEnd = reverseGeocoder.reverseLookup(getApplicationContext(),45.5087437,-73.6908567);

                Double distance = PriceCalculator.calculateDistance(startLatitude, startLongitude, endLatitude, endLongitude);
                Double price = PriceCalculator.calculatePrice(distance);
                DecimalFormat df = new DecimalFormat("#.00");

                textView1.setText("Pick up: " + reverseGeocoder.safeAddressToString(addressStart));
                textView2.setText("Drop-off: " + reverseGeocoder.safeAddressToString(addressEnd));
                textView3.setText("Arrival time: " + SQLCompliance.convertToSQLTimestamp(adsListings_list.get(i).getString("endingTime")));
                textView4.setText("Price: " + df.format(price) + " $");
            }catch(Exception e){
                Log.e("JSON ERROR", "Failed to get JSON field");
            }

            listingsContainer.addView(listingsRow);
            listingsRow.addView(driver_icon_view);
            listingsRow.addView(textviewBlock);
            textviewBlock.addView(textView1);
            textviewBlock.addView(textView2);
            textviewBlock.addView(textView3);
            textviewBlock.addView(textView4);
            textviewBlock.addView(textviewBlock_INNER);

            try {
                getAdsInfo_async(adsListings_list.get(i).getString("tripID"), textviewBlock_INNER);
            }catch(Exception e){
                Log.e("ADS_INFO", "Failed to GET Ads info from server.");
            }

            //clicking on the rows
            listingsRow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    View inner_ads_info = ((LinearLayout) listingsRow.getChildAt(1)).getChildAt(4);
                    if(expandable_id > -1){
                        findViewById(expandable_id).setVisibility(View.GONE);
                    }
                    expandable_id = inner_ads_info.getId();
                    Log.e("LOGGER", "" + expandable_id);
                    inner_ads_info.setVisibility(View.VISIBLE);

                    TextView startingAddress = (TextView)((LinearLayout) listingsRow.getChildAt(1)).getChildAt(0);
                    editTxt.setText(startingAddress.getText());
                    selectAd.setText("Request driver");
                    selectAd.setBackgroundColor(Color.parseColor("#FF5E84"));
                    for(int i=0; i< listingsContainer.getChildCount(); i++){
                        listingsContainer.getChildAt(i).setBackgroundColor(Color.WHITE);
                    }
                    listingsRow.setBackgroundColor(Color.parseColor("#FFC0C3"));
                }
            });

            ConstraintLayout background = (ConstraintLayout) findViewById(R.id.generalBackground);
            background.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(expandable_id > -1){
                        findViewById(expandable_id).setVisibility(View.GONE);
                    }
                    selectAd.setText("Pick a driver");
                    selectAd.setBackgroundColor(Color.parseColor("#d6d7d7"));
                    for(int i=0; i< listingsContainer.getChildCount(); i++){
                        listingsContainer.getChildAt(i).setBackgroundColor(Color.WHITE);
                    }
                }
            });

            selectAd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String startingAddress = ((TextView)((LinearLayout) listingsRow.getChildAt(1)).getChildAt(0)).getText().toString();
                    String endingAddress = ((TextView)((LinearLayout) listingsRow.getChildAt(1)).getChildAt(1)).getText().toString();
                    int tripID = expandable_id;
                    String coordinates = "";
                    Log.e("LOGGER_2", "" + tripID);
                    if(hash_geolocations.containsKey(tripID)){
                        Log.e("LOGGINGGGGGGG_MEI2", hash_geolocations.toString());
                        coordinates = hash_geolocations.get(tripID);
                    }

                    Intent intent = new Intent(AdsListings.this, MapsPassengerActivity.class);
                    if(coordinates.length() >=4){
                        String[] coords = coordinates.split(",");
                        intent.putExtra("startingLatitude", coords[0]);
                        intent.putExtra("startingLongitude", coords[1]);
                        intent.putExtra("endingLatitude", coords[2]);
                        intent.putExtra("endingLongitude", coords[3]);

                        Log.e("LOGGINGGGGGGG_MEI", coords[0].toString());
                        Log.e("LOGGINGGGGGGG", coords[1].toString());
                        Log.e("LOGGINGGGGGGG", coords[2].toString());
                        Log.e("LOGGINGGGGGGG", coords[3].toString());
                    }

//                    intent.putExtra("startingAddress", startingAddress);
//                    intent.putExtra("endingAddress", endingAddress);
                    intent.putExtra("tripID", tripID);
                    intent.putExtra("isPreviewing", true);
                    startActivity(intent);


                }
            });

        }
    }

    public void getAdsListings_sync() {
        if(adsListings_list != null && adsListings_list.size()>0){
            adsListings_list.clear();
        }
        final RequestParams params = new RequestParams();
        params.add("startingLongitude", startingLongitude);
        params.add("startingLatitude", startingLatitude);
        params.add("endingLongitude", endingLongitude);
        params.add("endingLatitude", endingLatitude);
        params.add("maximumDistance", maximumDistance);
        params.add("arrivalTime", arrivalTime);
        params.add("operator", operator);

        // Instantiate a new Runnable object which will handle http requests asynchronously
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                // Instantiate new synchronous http client, set timeout to 5 seconds and
                // perform GET request
                SyncHttpClient client = new SyncHttpClient();
                client.setTimeout(5500);
                client.get(itinerary_baseURL, params, new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                        String responseString = new String(responseBody);
                        if(!responseString.isEmpty()){
                            Log.d("Ads_Listings", "itinerary not empty");
                            Log.d("Ads_Listings", responseString);
                            response = "SUCCESS";
                            try {
                                JSONArray jr = new JSONArray(responseString);
                                JSONObject jb;
                                if(jr.length()>0) {
                                    for(int i=0; i<jr.length(); i++){
                                        jb = (JSONObject)jr.getJSONObject(i);
                                        adsListings_list.add(jb);
                                    }
                                }
                            }catch (Exception e) {
                                Log.e("Ads_Listings", "Failed to retrieve itinerary");
                                response = "ERROR";
                            }
                        }else{
                            Log.e("Ads_Listings", "Failed to retrieve itinerary");
                            response = "ERROR";
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers,
                                          byte[] responseBody, Throwable error) {
                        Log.e("Ads_Listings", "Server error - could not contact server");
                    }
                });
            }
        });
        t.start();

        // Await until thread t has finished its execution before proceeding
        try {
            t.join();
        } catch (InterruptedException e) {
            Log.e("Ads_Listings", "Thread exception in login thread");
        }
    }

    public void getAdsInfo_async(final String tripID, final LinearLayout collapsableBox) {
        if(adsInfo_list != null && adsInfo_list.size()>0){
            adsInfo_list.clear();
        }
        final Context context = getApplicationContext();
        final RequestParams params = new RequestParams();
        //initialize retry flags
        hash.put(tripID,0);
        params.add("tripID", tripID);

        // Instantiate a new Runnable object which will handle http requests asynchronously
        new Thread(new Runnable() {
            @Override
            public void run() {
                // Instantiate new synchronous http client, set timeout to 5 seconds and
                // perform GET request
                AsyncHttpClient client = new AsyncHttpClient();
                client.setTimeout(5500);
                client.get(adsInfo_baseURL, params, new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                        String responseString = new String(responseBody);
                        if(!responseString.isEmpty()){
                            Log.d("Ads_Info", "Ads Info not empty");
                            Log.d("Ads_Info", responseString);
                            response = "SUCCESS";
                            try {
                                JSONObject jb = new JSONObject(responseString);
                                adsInfo_list.add(jb);

                                //create contents
                                TextView textView1 = new TextView(context);
                                TextView textView2 = new TextView(context);
                                TextView textView3 = new TextView(context);
                                TextView textView4 = new TextView(context);

                                //attach contents to collapsable box
                                collapsableBox.addView(textView1);
                                collapsableBox.addView(textView2);
                                collapsableBox.addView(textView3);
                                collapsableBox.addView(textView4);

                                textView1.setText("Driver's name: " + jb.getString("name"));
                                textView2.setText("Tel: " + jb.getString("phone"));
                                textView3.setText("Car info: " + jb.getString("make") + ", " + jb.getString("model") +
                                        ", " + jb.getString("year"));
                                textView4.setText("License plate: " + jb.getString("licensePlate"));

                                //hide it until clicked
                                collapsableBox.setVisibility(View.GONE);

                            }catch (Exception e) {
                                Log.e("Ads_Info", "Failed to retrieve Ads Info");
                                Log.e("Ads_Info", e.toString());

                                response = "ERROR";
                            }
                        }else{
                            Log.e("Ads_Info", "Failed to retrieve Ads Info");
                            response = "ERROR";
                        }
                    }

                    public void onFailure(int statusCode, Header[] headers,
                                          byte[] responseBody, Throwable error) {
                        Log.e("Ads_Info", "Server error - could not contact server");
                        if(hash.containsKey(tripID)){
                            int retry_counts = hash.get(tripID);
                            if(retry_counts < 3){
                                retry_counts++;
                                Log.e("Ads_Info", "Server error - retrying " + retry_counts + " times in thread with trip ID: " + tripID);
                                hash.put(tripID, retry_counts);
                                getAdsInfo_async(tripID, collapsableBox);
                            }else {
                                Log.e("Ads_Info", "Server error - Numbers of retries exceeded. Could not contact server");
                            }
                        }
                    }
                });
            }
        }).run();
    }

}
