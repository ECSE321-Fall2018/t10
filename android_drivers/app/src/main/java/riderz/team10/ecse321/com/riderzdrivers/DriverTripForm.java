package riderz.team10.ecse321.com.riderzdrivers;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class DriverTripForm extends AppCompatActivity {

    //to do link the main page to this page
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_trip_form);
        mapButtons();
    }

    /*//intent, pair of values
    //intent.put extra
    //start address and end address
    //keys are "startAddress" and "endAddress"
    public void newTrip(View view) {
        final TextView startLoc = (TextView) findViewById(R.id.startlocation_form);
        final TextView endLoc   = (TextView) findViewById(R.id.endlocation_form);
        final TextView startTime   = (TextView) findViewById(R.id.endlocation_form);
        final TextView startDate   = (TextView) findViewById(R.id.endlocation_form);
        final TextView numberSeats   = (TextView) findViewById(R.id.endlocation_form);
        //add check if the fields are empty, return a warning

        *//*TO DO: link to Ryan's code
        Intent intent = new Intent(DriverTripForm.this,
                DriverTripForm.class);
        intent.putExtra("startLocation", startLoc.toString());
        intent.putExtra("endLocation", endLoc.toString());
        intent.putExtra("startTime", startTime.toString());
        intent.putExtra("startDate", startDate.toString());
        intent.putExtra("numberSeats", numberSeats.toString());
*//*

//        Intent intent = new Intent();
//        intent.putExtra();
    }*/
    //values are what we get from form

    public void mapButtons() {

        Log.e("TAGGGGG", "ABC");
        Button newTrip = findViewById(R.id.new_trip_button);

        // Add event listener for register button
        newTrip.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                final TextView startLoc = (TextView) findViewById(R.id.startlocation_form);
                final TextView endLoc   = (TextView) findViewById(R.id.endlocation_form);
                final TextView startTime   = (TextView) findViewById(R.id.endlocation_form);
                final TextView startDate   = (TextView) findViewById(R.id.endlocation_form);
                final TextView numberSeats   = (TextView) findViewById(R.id.endlocation_form);
                //add check if the fields are empty, return a warning

                /*TO DO: link to Ryan's code
                Intent intent = new Intent(DriverTripForm.this,
                    DriverTripForm.class);
                intent.putExtra("startLocation", startLoc.toString());
                intent.putExtra("endLocation", endLoc.toString());
                intent.putExtra("startTime", startTime.toString());
                intent.putExtra("startDate", startDate.toString());
                intent.putExtra("numberSeats", numberSeats.toString());
                DriverTripForm.this.startActivity(intent);
                */
            }
        });
    }
}
