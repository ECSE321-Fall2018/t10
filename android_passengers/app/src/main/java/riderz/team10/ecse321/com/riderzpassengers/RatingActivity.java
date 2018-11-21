package riderz.team10.ecse321.com.riderzpassengers;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import cz.msebera.android.httpclient.Header;
import riderz.team10.ecse321.com.riderzpassengers.constants.HTTP;
import riderz.team10.ecse321.com.riderzpassengers.navigation.MainNavigation;

public class RatingActivity extends AppCompatActivity {

    private String operator;
    private String rating;

    private RatingBar ratingBar;
    private String msg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rating);

        operator = getIntent().getStringExtra("operator");

        TextView ratedDriver = (TextView) findViewById(R.id.rated_driver);
        ratingBar = (RatingBar) findViewById(R.id.rating);
        Button ratingConfirmation = (Button) findViewById(R.id.confirm_rating_button);

        ratedDriver.setText("What rating would you like to give " + operator + " ?");

        ratingConfirmation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                asyncHttpUpdateRating();
            }
        });
    }

    private void asyncHttpUpdateRating() {

        rating = String.valueOf(ratingBar.getRating());

        // Add parameters
        final RequestParams params = new RequestParams();
        params.add("operator", operator);
        params.add("rating", rating);

        Log.e("debug", operator);
        Log.e("debug", rating);


        // Execute asynchronous http request on another thread other than main
        new Thread(new Runnable() {
            @Override
            public void run() {
                final String updateRating = "https://riderz-t10.herokuapp.com/driver/rating";
                AsyncHttpClient client = new AsyncHttpClient();
                client.setTimeout(HTTP.maxTimeout);
                client.put(updateRating, params, new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                        // Check that we obtained a valid response from the server
                        if (responseBody.length == 0) {
                            // Response is invalid
                            Log.e("debug", "Failed to update the driver's rating");
                            msg = "Failed to update the driver's rating";
                        } else {
                            // Response is valid
                            msg = "";
                            Log.e("debug", new String(responseBody));
                            Toast.makeText(getApplicationContext(), "The operator " + operator + " had a rating of " + rating,
                                    Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(RatingActivity.this, MainNavigation.class);
                            finish();
                            startActivity(intent);
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                        // Most possible error is statusCode 500
                        Log.e("debug", new String(responseBody));
                        Log.e("debug", "Server error - Failed to contact server");
                    }
                });

            }
        }).run();
    }

}
