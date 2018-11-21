package riderz.team10.ecse321.com.riderzpassengers;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class PreviousRidesActivity extends AppCompatActivity {

    private String msg;
    private ArrayList<String> driverNames;
    private String operator;
    private int lengthOfNames;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_previous_rides);

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

                                lengthOfNames = trips.length();
                                for(int i = 0; i < lengthOfNames; i++){
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

                            final String driver = new String(responseBody);
                            Log.e("hello2", driver + " " + tripID);
                            if (driver != null){
                                driverNames.add(driver);
                                Log.e("drivers", driverNames.toString());
                            }

                            if (driverNames.size() == lengthOfNames){

                                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, driverNames);

                                Log.e("size", String.valueOf(driverNames.size()));

                                ListView listView = (ListView) findViewById(R.id.previous_rides_list);
                                listView.setBackgroundColor(Color.DKGRAY);
                                listView.setAdapter(adapter);

                                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                        Intent intent = new Intent(PreviousRidesActivity.this, UserProfile.class);
                                        intent.putExtra("driver", driverNames.get(i));
                                        PreviousRidesActivity.this.startActivity(intent);
                                    }
                                });
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