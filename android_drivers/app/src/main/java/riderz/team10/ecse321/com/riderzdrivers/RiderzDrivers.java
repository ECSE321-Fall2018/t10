package riderz.team10.ecse321.com.riderzdrivers;

import cz.msebera.android.httpclient.Header;
import riderz.team10.ecse321.com.riderzdrivers.constants.TAG;
import riderz.team10.ecse321.com.riderzdrivers.constants.URL;
import riderz.team10.ecse321.com.riderzdrivers.http.HttpRequestClient;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.loopj.android.http.*;

/**
 * Main activity. Performs login functionalities.
 */
public class RiderzDrivers extends AppCompatActivity implements HttpRequestClient {
    // loginError is to be accessed within nested functions, required to give global access
    protected TextView errorMsg;

    // Used for transmit information from synchronous HTTP requests to the login REST API
    protected boolean success;
    protected String msg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_riderz_drivers);

        // Hide error message for now
        errorMsg = findViewById(R.id.loginError);
        errorMsg.setVisibility(View.INVISIBLE);

        // Obtain mapping to each of the buttons
        mapButtons();
    }


    @Override
    public void mapButtons() {
        Button resetButton = findViewById(R.id.reset);
        Button loginButton = findViewById(R.id.login);
        Button registerButton = findViewById(R.id.register);

        /*
        // ****************************** DO NOT COMMIT ******************************************

        Button button = findViewById(R.id.button);

        button.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RiderzDrivers.this, MapsDriversActivity.class);
                intent.putExtra("username", "test-ty2");
                RiderzDrivers.this.startActivity(intent);
            }
        });

        // ************************************************************************************
        */

        // Add event listener for reset button
        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Launch reset password verification activity when on click event gets triggered
                Intent intent = new Intent(RiderzDrivers.this,
                        ResetPasswordVerification.class);
                RiderzDrivers.this.startActivity(intent);
            }
        });

        // Add event listener for login button
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RiderzDrivers.this,
                        DriverProfile.class);
                intent.putExtra("username", "username");
                RiderzDrivers.this.startActivity(intent);
            }
        });

        // Add event listener for register button
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RiderzDrivers.this,
                        DriverTripForm.class);
                RiderzDrivers.this.startActivity(intent);
            }
        });
    }

    @Override
    public void syncHttpRequest() {
        // URL to target
        final String loginUrl = URL.baseUrl + "login";

        // Obtains values from EditText elements
        final EditText username = findViewById(R.id.loginUsername);
        final EditText password = findViewById(R.id.loginPassword);
        final String usernameText = username.getText().toString();
        final String passwordText = password.getText().toString();

        // Instantiate RequestParams object
        // Add username and password strings
        final RequestParams params = new RequestParams();
        params.add("username", usernameText);
        params.add("password", passwordText);

        // Instantiate a new Runnable object which will handle http requests asynchronously
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                // Instantiate new synchronous http client, set timeout to 5 seconds and
                // perform post request
                SyncHttpClient client = new SyncHttpClient();
                client.setTimeout(1500);
                client.post(loginUrl, params, new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                        String responseString = new String(responseBody);
                        switch (responseString) {
                            case "User session has been established!":
                                Log.d(TAG.loginTag, "User: " + usernameText + " successfully authenticated");
                                success = true;
                                break;
                            default:
                                Log.d(TAG.loginTag, "User: " + usernameText + " failed to authenticate");
                                success = false;
                                msg = "This combination does not exist!";
                                break;
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers,
                        byte[] responseBody, Throwable error) {
                            success = false;
                            msg = "Could not contact server";
                            Log.e(TAG.loginTag, "Server error - could not contact server");
                        }
                    });
            }
        });
        t.start();

        // Await until thread t has finished its execution before proceeding
        try {
            t.join();
        } catch (InterruptedException e) {
            Log.e(TAG.loginTag, "Thread exception in login thread");
        }
    }
}
