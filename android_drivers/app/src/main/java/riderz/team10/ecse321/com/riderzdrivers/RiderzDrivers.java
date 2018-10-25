package riderz.team10.ecse321.com.riderzdrivers;

import cz.msebera.android.httpclient.Header;
import riderz.team10.ecse321.com.riderzdrivers.constants.URL;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.loopj.android.http.*;

public class RiderzDrivers extends AppCompatActivity {
    // loginError is to be accessed within nested functions, required global access
    protected TextView errorMsg;

    // TAG for Logcat, only valid for this class
    private static final String TAG = "LoginScreen";

    // Used for synchronous calls to REST APIs
    protected boolean success;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_riderz_drivers);

        // Hide error message for now
        errorMsg = findViewById(R.id.loginError);
        errorMsg.setVisibility(View.INVISIBLE);

        // Obtain mapping to each of the buttons
        Button resetButton = findViewById(R.id.reset);
        Button loginButton = findViewById(R.id.login);
        Button registerButton = findViewById(R.id.register);

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
                verifyLoginCredentials();
                if (success) {
                    Intent intent = new Intent(RiderzDrivers.this,
                            ResetPasswordVerification.class);
                    RiderzDrivers.this.startActivity(intent);
                    errorMsg.setText("Successful login");
                } else {
                    errorMsg.setText("Combination does not exist");
                }
                errorMsg.setVisibility(View.VISIBLE);
            }
        });

        // Add event listener for register button
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO: Complete
            }
        });
    }

    /**
     * This method instantiates a new synchronous HTTP client and post requests the login API.
     * @return True if the user credentials are valid
     */
    private boolean verifyLoginCredentials() {
        // URL to target
        final String loginUrl = URL.baseUrl + "login";

        // Obtains values from EditText elements
        final EditText username = findViewById(R.id.username);
        final EditText password = findViewById(R.id.password);
        final String usernameText = username.getText().toString();
        final String passwordText = password.getText().toString();

        // Instantiate http request parameters and fill with appropriate parameters
        final RequestParams params = new RequestParams();
        params.add("username", usernameText);
        params.add("password", passwordText);

        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                // Instantiate new synchronous http client, set timeout to 5 seconds and post request
                SyncHttpClient client = new SyncHttpClient();
                client.setTimeout(5000);
                client.post(loginUrl, params, new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                        String responseString = new String(responseBody);
                        switch (responseString) {
                            case "User session has been established!":
                                Log.d(TAG, "User: " + usernameText + " successfully authenticated");
                                success = true;
                                break;
                            default:
                                Log.d(TAG, "User: " + usernameText + " failed to authenticate");
                                success = false;
                                break;
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers,
                        byte[] responseBody, Throwable error) {
                            success = false;
                            Log.e(TAG, "Server error");
                        }
                    });
            }
        });
        t.start();

        // Await until thread t has finished its execution
        try {
            t.join();
        } catch (InterruptedException e) {
            Log.e(TAG, "Thread exception in login thread");
        }
        return success;
    }
}
