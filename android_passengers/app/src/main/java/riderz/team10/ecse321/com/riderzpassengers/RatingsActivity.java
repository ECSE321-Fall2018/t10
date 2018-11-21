package riderz.team10.ecse321.com.riderzpassengers;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class RatingsActivity extends AppCompatActivity {

    private String msg;
    private ArrayList<String> driverNames;
    private String operator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ratings);

        ListView ratingsList = (ListView) findViewById(R.id.ratings_list);
        operator = getIntent().getStringExtra("username");

        driverNames = new ArrayList<>();

        asyncHttpRequestTrips();
    }

    private void asyncHttpRequestTrips() {
        // Add parameters
        final RequestParams params = new RequestParams();
        params.add("operator", operator);

        Log.e("debug", operator);

        // Execute asynchronous http request on another thread other than main
        new Thread(new Runnable() {
            @Override
            public void run() {
                final String getTrips = "https://riderz-t10.herokuapp.com/trip/username";
                AsyncHttpClient client = new AsyncHttpClient();
                client.setTimeout(10000);
                client.get(getTrips, params, new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                        // Check that we obtained a valid response from the server
                        if (responseBody.length == 0) {
                            // Response is invalid
                            Log.e("debug", "Failed to get trips");
                            msg = "Failed to get trips";
                        } else {
                            // Response is valid
                            msg = "";
                            try {
                                // Parse JSON object and set EditText where valid
                                JSONArray trips = new JSONArray(new String(responseBody));

                                int length = trips.length();
                                for(int i = 0; i < length; i++){
                                    Log.e("hello1", String.valueOf(trips.getJSONObject(i).getInt("tripID")));
                                    asyncHttpRequestDriverName(String.valueOf(trips.getJSONObject(i).getInt("tripID")));
                                }

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

    private void asyncHttpRequestDriverName(final String tripID) {
        // Add parameters
        final RequestParams params = new RequestParams();
        params.add("tripID", tripID);

        Log.e("debug", tripID);

        // Execute asynchronous http request on another thread other than main
        new Thread(new Runnable() {
            @Override
            public void run() {
                final String getTrips = "https://riderz-t10.herokuapp.com/trip/getDriverName";
                AsyncHttpClient client = new AsyncHttpClient();
                client.setTimeout(7500);
                client.get(getTrips, params, new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                        // Check that we obtained a valid response from the server
                        if (responseBody.length == 0) {
                            // Response is invalid
                            Log.e("debug", "Failed to get driver name " + tripID);
                            msg = "Failed to get driver name";
                            asyncHttpRequestDriverName(tripID);
                        } else {
                            // Response is valid
                            msg = "";

                            String driver = new String(responseBody);
                            Log.e("hello2", driver + " " + tripID);
                            if (driver != null){
                                driverNames.add(driver);
                                Log.e("drivers", driverNames.toString());
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