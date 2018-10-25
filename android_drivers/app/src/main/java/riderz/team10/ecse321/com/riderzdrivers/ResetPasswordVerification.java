package riderz.team10.ecse321.com.riderzdrivers;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

/**
 * Reset password verification screen. Verifies that user's email address and username match
 * whatever is provided within the database.
 */
public class ResetPasswordVerification extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password_verification);

        // Enable back button on action bar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        // Allows navigation back to login screen
        if (id == android.R.id.home) {
            onBackPressed();
            return true;
        }

        return false;
    }
}

