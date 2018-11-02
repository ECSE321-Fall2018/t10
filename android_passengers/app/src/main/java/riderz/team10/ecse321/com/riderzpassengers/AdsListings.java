package riderz.team10.ecse321.com.riderzpassengers;

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

import com.loopj.android.http.*;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class AdsListings extends AppCompatActivity {

    private TextView editTxt;
    private Button selectAd;
    private ArrayList<JSONObject> arrayList = new ArrayList<JSONObject>();
    private String response;

    //get data from Ryan
    final String itinerary_baseURL = "https://riderz-t10.herokuapp.com/getItineraryNearDestination";
    final String startingLongitude = "22.2222";
    final String startingLatitude = "-33.33333";
    final String endingLongitude = "12.232323";
    final String endingLatitude = "-52.525252";
    final String maximumDistance = "10000000000000000";
    final String arrivalTime = "2051-01-01 02:30:00.000";
    final String operator = "mei";

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
        final LinearLayout listingsContainer = (LinearLayout) findViewById(R.id.listingsContainer);

        param_1.setMargins(10, 10, 10,10);
        param_2.setMargins(10, 10, 10,10);
        param_3.setMargins(10, 10, 10,10);
        layoutParams_img.setMargins(10, 10, 10,10);

        //HTTP GET request to get itineraries near destination
        getAdsListings_sync();

        //loop for each element from database
        for(int i=0; i<arrayList.size(); i++){
            //Ads listings row
            final LinearLayout listingsRow = new LinearLayout(this);
            listingsRow.setOrientation(LinearLayout.HORIZONTAL);
            listingsRow.setBackgroundColor(Color.WHITE);
            listingsRow.setPadding(10, 10, 10, 10);

            LinearLayout textviewBlock = new LinearLayout(this);
            textviewBlock.setOrientation(LinearLayout.VERTICAL);

            //Ads info box
            TextView textView1 = new TextView(this);
            TextView textView2 = new TextView(this);
            TextView textView3 = new TextView(this);
            TextView textView4 = new TextView(this);
            TextView textView5 = new TextView(this);

            //Icon
            ImageView driver_icon_view = new ImageView(this);
            driver_icon_view.setLayoutParams(layoutParams_img);
            driver_icon_view.setImageResource(R.mipmap.driver_logo);

            listingsRow.setLayoutParams(param_2);
            textviewBlock.setLayoutParams(param_3);
            textView1.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
            textView2.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
            textView3.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
            textView4.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
            textView5.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);

            try{
                ReverseGeocoding reverseGeocoder = new ReverseGeocoding();
                Address addressStart = reverseGeocoder.reverseLookup(this,Double.parseDouble(arrayList.get(i).getString("startingLatitude")),Double.parseDouble(arrayList.get(i).getString("startingLongitude")));
                Address addressEnd = reverseGeocoder.reverseLookup(this,Double.parseDouble(arrayList.get(i).getString("endingLatitude")),Double.parseDouble(arrayList.get(i).getString("endingLongitude")));
//TESTING METHODS
//                Address addressStart = reverseGeocoder.reverseLookup(getApplicationContext(),45.4724431,-73.5928623);
//                Address addressEnd = reverseGeocoder.reverseLookup(getApplicationContext(),45.5087437,-73.6908567);

                textView1.setText(reverseGeocoder.safeAddressToString(addressStart));
                textView2.setText(reverseGeocoder.safeAddressToString(addressEnd));
                textView3.setText(arrayList.get(i).getString("endingTime"));
                textView4.setText(arrayList.get(i).getString("startingLatitude"));
                textView5.setText(arrayList.get(i).getString("startingLongitude"));
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
            textviewBlock.addView(textView5);

            //clicking on the rows
            listingsRow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    editTxt.setText(( (TextView)((LinearLayout) listingsRow.getChildAt(1)).getChildAt(0)).getText());
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
                    selectAd.setText("Pick a driver");
                    selectAd.setBackgroundColor(Color.parseColor("#d6d7d7"));
                    for(int i=0; i< listingsContainer.getChildCount(); i++){
                        listingsContainer.getChildAt(i).setBackgroundColor(Color.WHITE);
                    }
                }
            });
        }
    }

    public void getAdsListings_sync() {
        if(arrayList != null && arrayList.size()>0){
            arrayList.clear();
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
                client.setTimeout(1500);
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
                                        arrayList.add(jb);
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

}
