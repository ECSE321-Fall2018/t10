package riderz.team10.ecse321.com.riderzdrivers.http;

import android.os.Bundle;
import android.app.Activity;
import android.util.Log;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.SyncHttpClient;

import org.json.JSONArray;
import org.json.JSONException;

import cz.msebera.android.httpclient.Header;
import riderz.team10.ecse321.com.riderzdrivers.R;
import riderz.team10.ecse321.com.riderzdrivers.constants.HTTP;
import riderz.team10.ecse321.com.riderzdrivers.constants.TAG;
import riderz.team10.ecse321.com.riderzdrivers.constants.URL;

public class DriverProfile extends Activity implements HttpRequestClient {

    // Provides access
    private String username;

    // Current trips
    private JSONArray json;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_profile);
        username = getIntent().getStringExtra("username");
//      http request to get info
    }

    @Override
    public void mapButtons() {
        //no buttons required
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
                // Uses extended timeout
                SyncHttpClient client = new SyncHttpClient();
                client.setTimeout(HTTP.maxTimeoutExtended);
                client.get(tripUrl, params, new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                        try {
                            json = new JSONArray(new String(responseBody));
                        } catch (JSONException e) {
                            Log.e(TAG.driverProfileTag, "Failed to parse JSON from HTTP response");
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers,
                                          byte[] responseBody, Throwable error) {
                        Log.e(TAG.driverProfileTag, "Server error - could not contact server");
                    }
                });
            }
        });
        t.start();

        // Await until thread t has finished its execution before proceeding
        try {
            t.join();
        } catch (InterruptedException e) {
            Log.e(TAG.driverProfileTag, "Thread exception in login thread");
        }

    }
}
