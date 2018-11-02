package riderz.team10.ecse321.com.riderzdrivers.settings;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.SyncHttpClient;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;
import riderz.team10.ecse321.com.riderzdrivers.R;
import riderz.team10.ecse321.com.riderzdrivers.assets.template.activity.AppCompatActivityBack;
import riderz.team10.ecse321.com.riderzdrivers.assets.verificator.RegexVerification;
import riderz.team10.ecse321.com.riderzdrivers.constants.HTTP;
import riderz.team10.ecse321.com.riderzdrivers.constants.TAG;
import riderz.team10.ecse321.com.riderzdrivers.constants.URL;
import riderz.team10.ecse321.com.riderzdrivers.http.HttpRequestClient;

public class ModifyCar extends AppCompatActivityBack implements HttpRequestClient {
    // Used to get and set values in EditText
    protected EditText make;
    protected EditText model;
    protected EditText year;
    protected EditText seats;
    protected EditText efficiency;
    protected EditText plate;

    // Used to display error messages
    protected TextView errorMsg;

    // User credentials to uniquely identify an user
    protected String username;

    // Used to transmit information from synchronous HTTP requests
    protected boolean success;
    protected String msg;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_car);

        // Map Java to XML
        make = findViewById(R.id.updateCarMake);
        model = findViewById(R.id.updateCarModel);
        year = findViewById(R.id.updateCarYear);
        seats = findViewById(R.id.updateCarSeats);
        efficiency = findViewById(R.id.updateCarFuelEfficiency);
        plate = findViewById(R.id.updateCarLicense);

        // Hide error message for now
        errorMsg = findViewById(R.id.updateCarError);
        errorMsg.setVisibility(View.GONE);

        // Obtain username from intent
        username = getIntent().getStringExtra("username");

        // Perform Asynchronous request to obtain information about car
        asyncHttpRequest();
        mapButtons();
    }

    /**
     * Asynchronously obtains the driver's car registration information and inserts it into the EditText.
     */
    private void asyncHttpRequest() {
        // Add parameters
        final RequestParams params = new RequestParams();
        params.add("operator", username);

        Log.e(TAG.changeCarTag, username);

        // Execute asynchronous http request on another thread other than main
        new Thread(new Runnable() {
            @Override
            public void run() {
                final String getCarUrl = URL.baseUrl + "car";
                AsyncHttpClient client = new AsyncHttpClient();
                client.setTimeout(HTTP.maxTimeout);
                client.get(getCarUrl, params, new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                        // Check that we obtained a valid response from the server
                        if (responseBody.length == 0) {
                            // Response is invalid
                            Log.e(TAG.changeCarTag, "Failed to fetch car information");
                            msg = "Failed to fetch car information";
                        } else {
                            // Response is valid
                            msg = "";
                            try {
                                // Parse JSON object and set EditText where valid
                                JSONObject object = new JSONObject(new String(responseBody));
                                make.setText(object.getString("make"));
                                model.setText(object.getString("model"));
                                year.setText("" + object.getInt("year"));
                                seats.setText("" + object.getInt("numOfSeats"));
                                efficiency.setText("" + object.getDouble("fuelEfficiency"));
                                plate.setText(object.getString("licensePlate"));
                            } catch (JSONException e) {
                                Log.e(TAG.changeCarTag, "Failed to parse JSON object");
                            }
                        }
                        // Display error message
                        errorMsg.setText(msg);
                        errorMsg.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                        // Most possible error is statusCode 500
                        Log.e(TAG.changeCarTag, "Server error - Failed to contact server");
                        errorMsg.setText("Failed to contact server");
                        errorMsg.setVisibility(View.VISIBLE);
                    }
                });
            }
        }).run();
    }

    @Override
    public void mapButtons() {
        findViewById(R.id.updateCarButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Make is not alphabets
                if (!RegexVerification.isAlphabetical(make.getText().toString())) {
                    errorMsg.setText("Invalid input for make");
                    errorMsg.setVisibility(View.VISIBLE);
                    return;
                }

                // Model is not alphanumeric
                if (!RegexVerification.isAlphanumerical(model.getText().toString())) {
                    errorMsg.setText("Invalid input for model");
                    errorMsg.setVisibility(View.VISIBLE);
                    return;
                }

                // License plate is not alphanumeric
                if (!RegexVerification.isAlphanumerical(plate.getText().toString())) {
                    errorMsg.setText("Invalid input for license plate");
                    errorMsg.setVisibility(View.VISIBLE);
                    return;
                }

                syncHttpRequest();
                // If the request was successful, terminate current activity
                if (success) {
                    success = false;
                    msg = "";
                    finish();
                }
                // Display error message
                errorMsg.setText(msg);
                errorMsg.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    public void syncHttpRequest() {
        // Add parameters
        final RequestParams params = new RequestParams();
        params.add("operator", username);
        params.add("make", make.getText().toString());
        params.add("model", model.getText().toString());
        params.add("year", year.getText().toString());
        params.add("numberOfSeats", seats.getText().toString());
        params.add("fuelEfficiency", efficiency.getText().toString());
        params.add("licensePlate", plate.getText().toString());

        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                final String setCarUrl = URL.baseUrl + "car";
                SyncHttpClient client = new SyncHttpClient();
                client.setTimeout(HTTP.maxTimeout);
                client.put(setCarUrl, params, new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                        // Check that we obtained a valid response from the server
                        if (responseBody.length != 0) {
                            try {
                                new JSONObject(new String(responseBody));
                            } catch(JSONException e) {
                                // Invalid response, display error
                                Log.e(TAG.changeCarTag, "Could not parse response");
                                success = false;
                                msg = "Failed to update information";
                                return;
                            }
                            // JSONObject properly parsed
                            success = true;
                            msg = "";
                        } else {
                            Log.e(TAG.changeCarTag, "Response from server: " + new String(responseBody));
                            Log.e(TAG.changeCarTag, new String(responseBody));
                            success = false;
                            msg = "Failed to update information";
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                        Log.e(TAG.changeCarTag, "Server error - Failed to contact server");
                        success = false;
                        msg = "Could not contact server";
                    }
                });
            }
        });
        t.start();

        // Await until thread has finished execution before proceeding
        try {
            t.join();
        } catch (InterruptedException e) {
            Log.e(TAG.changePhoneTag, "Thread error in changing phone thread");
        }
    }
}
