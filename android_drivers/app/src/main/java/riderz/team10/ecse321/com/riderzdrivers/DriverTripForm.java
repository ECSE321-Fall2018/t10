package riderz.team10.ecse321.com.riderzdrivers;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.SyncHttpClient;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;
import riderz.team10.ecse321.com.riderzdrivers.constants.HTTP;
import riderz.team10.ecse321.com.riderzdrivers.constants.TAG;
import riderz.team10.ecse321.com.riderzdrivers.constants.URL;
import riderz.team10.ecse321.com.riderzdrivers.http.HttpRequestClient;

public class DriverTripForm extends AppCompatActivity implements HttpRequestClient {


    private JSONObject jsonCreateRoute;
    private JSONObject jsonRouteLast;
    private JSONObject jsonTrip;
    private String username;

    //to do link the main page to this page
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_trip_form);
        mapButtons();
    }

    /*//intent, pair of values
    //intent.put extra
    //start address and end address
    //keys are "startAddress" and "endAddress"
    public void newTrip(View view) {
        final TextView startLoc = (TextView) findViewById(R.id.startlocation_form);
        final TextView endLoc   = (TextView) findViewById(R.id.endlocation_form);
        final TextView startTime   = (TextView) findViewById(R.id.endlocation_form);
        final TextView startDate   = (TextView) findViewById(R.id.endlocation_form);
        final TextView numberSeats   = (TextView) findViewById(R.id.endlocation_form);
        //add check if the fields are empty, return a warning

        *//*TO DO: link to Ryan's code
        Intent intent = new Intent(DriverTripForm.this,
                DriverTripForm.class);
        intent.putExtra("startLocation", startLoc.toString());
        intent.putExtra("endLocation", endLoc.toString());
        intent.putExtra("startTime", startTime.toString());
        intent.putExtra("startDate", startDate.toString());
        intent.putExtra("numberSeats", numberSeats.toString());
*//*

//        Intent intent = new Intent();
//        intent.putExtra();
    }*/
    //values are what we get from form

    public void mapButtons() {

        Log.e("TAGGGGG", "ABC");
        Button newTrip = findViewById(R.id.new_trip_button);

        // Add event listener for register button
        newTrip.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
//                final TextView startLoc = (TextView) findViewById(R.id.startlocation_form);
//                final TextView endLoc   = (TextView) findViewById(R.id.endlocation_form);
                final TextView startTime   = (TextView) findViewById(R.id.endlocation_form);
                final TextView startDate   = (TextView) findViewById(R.id.endlocation_form);
                final TextView numberSeats   = (TextView) findViewById(R.id.endlocation_form);
                //add check if the fields are empty, return a warning



                syncHttpRequestCreate();
                syncHttpRequestLastTrip();
                String tripID = null;
                try {
                    tripID = jsonRouteLast.getString("tripID");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                //i receive startLong, startLat, endLong, endLat, time difference(ms)
                //the start time is taken from driver

//                syncHttpRequestCreateRoute(tripID, startLongitude, startLatitude, startTime,
//                        endLongitude, endLatitude, endTime);

                /*TO DO: link to Ryan's code
                Intent intent = new Intent(DriverTripForm.this,
                    DriverTripForm.class);
                intent.putExtra("startLocation", startLoc.toString());
                intent.putExtra("endLocation", endLoc.toString());
                intent.putExtra("startTime", startTime.toString());
                intent.putExtra("startDate", startDate.toString());
                intent.putExtra("numberSeats", numberSeats.toString());
                DriverTripForm.this.startActivity(intent);
                */
            }
        });
    }

    @Override
    public void syncHttpRequest(){

    }
    //create the trip
    public void syncHttpRequestCreate(){
        final RequestParams params = new RequestParams();
        params.add("operator", username);

        // Instantiate a new Runnable object which will handle http requests asynchronously
        // Then await until thread is finished to make the request synchronous.
        Thread t = new Thread(new Runnable() {
            // URL to target
            final String tripUrl = URL.baseUrl + "trip/insertTrip";

            @Override
            public void run() {
                // Instantiate new synchronous http client, set timeout and perform get request
                // Uses extended timeout
                SyncHttpClient client = new SyncHttpClient();
                client.setTimeout(HTTP.maxTimeoutExtended);
                client.put(tripUrl, params, new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                        try {
                            jsonCreateRoute = new JSONObject(new String(responseBody));
                        } catch (JSONException e) {
                            Log.e(riderz.team10.ecse321.com.riderzdrivers.constants.TAG.driverTripFormTag, "Failed to parse JSON from HTTP response");
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers,
                                          byte[] responseBody, Throwable error) {
                        Log.e(TAG.driverTripFormTag, "Server error - could not contact server");
                    }
                });
            }
        });
        t.start();

        // Await until thread t has finished its execution before proceeding
        try {
            t.join();
        } catch (InterruptedException e) {
            Log.e(TAG.driverTripFormTag, "Thread exception in login thread");
        }
    }

    public void syncHttpRequestLastTrip(){
        final RequestParams params = new RequestParams();
        params.add("operator", username);

        // Instantiate a new Runnable object which will handle http requests asynchronously
        // Then await until thread is finished to make the request synchronous.
        Thread t = new Thread(new Runnable() {
            // URL to target
            final String tripUrl = URL.baseUrl + "trip/last";

            @Override
            public void run() {
                // Instantiate new synchronous http client, set timeout and perform get request
                // Uses extended timeout
                SyncHttpClient client = new SyncHttpClient();
                client.setTimeout(HTTP.maxTimeoutExtended);
                client.put(tripUrl, params, new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                        try {
                            jsonRouteLast = new JSONObject(new String(responseBody));
                        } catch (JSONException e) {
                            Log.e(riderz.team10.ecse321.com.riderzdrivers.constants.TAG.driverTripFormTag, "Failed to parse JSON from HTTP response");
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers,
                                          byte[] responseBody, Throwable error) {
                        Log.e(TAG.driverTripFormTag, "Server error - could not contact server");
                    }
                });
            }
        });
        t.start();

        // Await until thread t has finished its execution before proceeding
        try {
            t.join();
        } catch (InterruptedException e) {
            Log.e(TAG.driverTripFormTag, "Thread exception in login thread");
        }

    }

    public void syncHttpRequestCreateRoute(String tripID, String startLongitude, String startLatitude, String startTime,
                                String endLongitude, String endLatitude, String endTime) {

        final RequestParams params = new RequestParams();
        params.add("tripID", tripID);
        params.add("startLongitude", startLongitude);
        params.add("startLatitude", startLatitude);
        params.add("startTime", startTime);
        params.add("endLongitude", endLongitude);
        params.add("endLatitude", endLatitude);
        params.add("endTime", endTime);
        params.add("operator", username);


        // Instantiate a new Runnable object which will handle http requests asynchronously
        // Then await until thread is finished to make the request synchronous.
        Thread t = new Thread(new Runnable() {
            // URL to target
            final String routeUrl = URL.baseUrl + "/insertItinerary/";

            @Override
            public void run() {
                // Instantiate new synchronous http client, set timeout and perform get request
                // Uses extended timeout
                SyncHttpClient client = new SyncHttpClient();
                client.setTimeout(HTTP.maxTimeoutExtended);
                client.put(routeUrl, params, new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                        try {
                            jsonTrip = new JSONObject(new String(responseBody));
                        } catch (JSONException e) {
                            Log.e(riderz.team10.ecse321.com.riderzdrivers.constants.TAG.driverTripFormTag, "Failed to parse JSON from HTTP response");
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers,
                                          byte[] responseBody, Throwable error) {
                        Log.e(TAG.driverTripFormTag, "Server error - could not contact server");
                    }
                });
            }
        });
        t.start();

        // Await until thread t has finished its execution before proceeding
        try {
            t.join();
        } catch (InterruptedException e) {
            Log.e(TAG.driverTripFormTag, "Thread exception in login thread");
        }
    }
}
