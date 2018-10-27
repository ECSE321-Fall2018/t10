package riderz.team10.ecse321.com.riderzdrivers;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

public class DriverTripForm extends AppCompatActivity {

    //to do link the main page to this page
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_trip_form);
    }

    //intent, pair of values
    //intent.put extra
    //start address and end address
    //keys are "startAddress" and "endAddress"
    //values are what we get from form
    public void newTrip(View view) {
        final TextView startLoc = (TextView) findViewById(R.id.startlocation_form);
        final TextView endLoc   = (TextView) findViewById(R.id.endlocation_form);


        //add check if the fields are empty, return a warning


//        Intent intent = new Intent();
//        intent.putExtra();
    }
}
