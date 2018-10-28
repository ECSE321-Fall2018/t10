package riderz.team10.ecse321.com.riderzdrivers.trip;

import android.location.Address;
import android.location.Geocoder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.SyncHttpClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import cz.msebera.android.httpclient.Header;
import riderz.team10.ecse321.com.riderzdrivers.R;
import riderz.team10.ecse321.com.riderzdrivers.constants.HTTP;
import riderz.team10.ecse321.com.riderzdrivers.constants.TAG;
import riderz.team10.ecse321.com.riderzdrivers.constants.URL;
import riderz.team10.ecse321.com.riderzdrivers.http.HttpRequestClient;
import riderz.team10.ecse321.com.riderzdrivers.konvertor.SQLCompliance;

public class EditTrip extends AppCompatActivity implements HttpRequestClient {
    // Contains a list of generated IDs for TextViews
    final private ArrayList<Integer> generatedId = new ArrayList<>();

    // Provides access
    private String username;

    // Current trips
    private JSONArray json;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_trip);

        // Obtains the username from the previous intent
        username = getIntent().getStringExtra("username");

        // Enable back button on action bar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        populateLinearLayout();
        mapButtons();
    }

    /**
     * Populate linear layout with programmatically generated TextViews
     */
    private void populateLinearLayout() {
        syncHttpRequest();
        LinearLayout layout = findViewById(R.id.editTripLayout);
        createTextViews(layout);
        Log.e(TAG.editTripTag, Arrays.toString(generatedId.toArray()));
    }

    /**
     * Creates new TextView components and inserts them into a specific layout.
     * @param layout LinearLayout which will contain the TextView
     */
    private void createTextViews(LinearLayout layout) {
        try {
            for (int i = 0; i < json.length(); i++) {
                JSONObject object = json.getJSONObject(i);
                String text = "";
                double latitude = object.getDouble("startingLongitude");
                double longitude = object.getDouble("startingLatitude");

                // Modify Timestamp to make it SQL compliant
                text += "Starting Time: " +
                        Timestamp.valueOf(SQLCompliance.convertToSQLTimestamp(object.getString("startingTime"))) + "\n";

                try {
                    Geocoder geoCoder = new Geocoder(getApplicationContext());
                    List<Address> matches = geoCoder.getFromLocation(latitude, longitude, 1);
                    Address bestMatch = (matches.isEmpty() ? null : matches.get(0));
                    Log.e(TAG.editTripTag, bestMatch.toString());
                } catch (IOException e) {
                    Log.e(TAG.editTripTag, e.getLocalizedMessage());
                }

                // Generate a new unique View ID
                int id = View.generateViewId();
                generatedId.add(id);

                // Instantiate a new TextView, set text and id
                TextView tv = new TextView(this);
                tv.setText(text);
                tv.setId(id);

                // Convert padding from dp to px and set
                int padding_in_dp = 16;
                final float scale = getResources().getDisplayMetrics().density;
                int padding_in_px = (int) (padding_in_dp * scale + 0.5f);
                tv.setPadding(padding_in_px, padding_in_px, padding_in_px, padding_in_px);

                // Set font size to 14dp
                tv.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 16);

                // Add TextView to layout
                layout.addView(tv);
            }
        } catch (JSONException e) {
            Log.e(TAG.editTripTag, "Failed to parse JSON array here");
            Log.e(TAG.editTripTag, e.getLocalizedMessage());
        }

    }

    @Override
    public void mapButtons() {

    }

    @Override
    public void syncHttpRequest() {
        final RequestParams params = new RequestParams();
        params.add("operator", username);

        // Instantiate a new Runnable object which will handle http requests asynchronously
        // Then await until thread is finished to make the request synchronous.
        Thread t = new Thread(new Runnable() {
            // URL to target
            final String tripUrl = URL.baseUrl + "getItineraryByUsername";

            @Override
            public void run() {
                // Instantiate new synchronous http client, set timeout and perform get request
                // Timeout has been
                SyncHttpClient client = new SyncHttpClient();
                client.setTimeout(HTTP.maxTimeoutExtended);
                client.get(tripUrl, params, new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                        try {
                            json = new JSONArray(new String(responseBody));
                        } catch (JSONException e) {
                            Log.e(TAG.editTripTag, "Failed to parse JSON array");
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers,
                                          byte[] responseBody, Throwable error) {
                        // Go back to login screen
                        Log.e(TAG.editTripTag, "Server error - could not contact server");
                    }
                });
            }
        });
        t.start();

        // Await until thread t has finished its execution before proceeding
        try {
            t.join();
        } catch (InterruptedException e) {
            Log.e(TAG.editTripTag, "Thread exception in login thread");
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        // Allows navigation back to previous screen
        if (id == android.R.id.home) {
            onBackPressed();
            return true;
        }

        return false;
    }
}
