package riderz.team10.ecse321.com.riderzdrivers;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

import riderz.team10.ecse321.com.riderzdrivers.http.HttpRequestClient;

public class ResetPassword extends AppCompatActivity implements HttpRequestClient {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        // Enable back button on action bar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Hide error message for now

        mapButtons();
    }

    @Override
    public void mapButtons() {

    }

    @Override
    public void syncHttpRequest() {

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        // Allows navigation back to previous page
        if (id == android.R.id.home) {
            onBackPressed();
            return true;
        }

        return false;
    }
}
