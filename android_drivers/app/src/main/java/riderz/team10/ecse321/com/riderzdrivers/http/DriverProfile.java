package riderz.team10.ecse321.com.riderzdrivers.http;

import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.widget.TextView;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.SyncHttpClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;
import riderz.team10.ecse321.com.riderzdrivers.R;
import riderz.team10.ecse321.com.riderzdrivers.constants.HTTP;
import riderz.team10.ecse321.com.riderzdrivers.constants.TAG;
import riderz.team10.ecse321.com.riderzdrivers.constants.URL;

public class DriverProfile extends Activity implements HttpRequestClient {

    // Provides access in the url
    private String username;

    // Information concerning the driver
    private JSONObject jsonDriver;
    private JSONObject jsonCar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_profile);
        username = getIntent().getStringExtra("username");
        syncHttpRequest();
        syncHttpRequestCar();
        TextView driverName = (TextView)findViewById(R.id.driverNameView);
        TextView driverRating = (TextView)findViewById(R.id.driverRatingView);
        TextView driverCarMake = (TextView)findViewById(R.id.driverCarMakeView);
        TextView driverCarModel = (TextView)findViewById(R.id.driverCarModelView);
        TextView driverCarYear = (TextView)findViewById(R.id.driverCarYearView);
        TextView driverCarSeats = (TextView)findViewById(R.id.driverCarYearView);
        TextView driverCarLicense = (TextView)findViewById(R.id.driverCarLicensePlateView);
        try {
            driverName.setText(jsonDriver.getString("operator"));
            driverRating.setText(jsonDriver.getString("rating"));

        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            driverCarMake.setText(jsonCar.getString("make"));
            driverCarModel.setText(jsonCar.getString("model"));
            driverCarYear.setText(jsonCar.getString("year"));
            driverCarSeats.setText(jsonCar.getString("numOfSeats"));
            driverCarLicense.setText(jsonCar.getString("licensePlate"));

        } catch (JSONException e) {
            e.printStackTrace();
        }
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
            final String driverUrl = URL.baseUrl + URL.driverUrl + "getDriverByUsername";

            @Override
            public void run() {
                // Instantiate new synchronous http client, set timeout and perform get request
                // Uses extended timeout
                SyncHttpClient client = new SyncHttpClient();
                client.setTimeout(HTTP.maxTimeoutExtended);
                client.get(driverUrl, params, new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                        try {
                            jsonDriver = new JSONObject(new String(responseBody));
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

    public void syncHttpRequestCar() {
        final RequestParams params = new RequestParams();
        params.add("operator", username);

        // Instantiate a new Runnable object which will handle http requests asynchronously
        // Then await until thread is finished to make the request synchronous.
        Thread t = new Thread(new Runnable() {
            // URL to target
            final String carUrl = URL.baseUrl + URL.carUrl + "getCar";

            @Override
            public void run() {
                // Instantiate new synchronous http client, set timeout and perform get request
                // Uses extended timeout
                SyncHttpClient client = new SyncHttpClient();
                client.setTimeout(HTTP.maxTimeoutExtended);
                client.get(carUrl, params, new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                        try {
                            jsonCar = new JSONObject(new String(responseBody));
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
