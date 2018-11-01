package riderz.team10.ecse321.com.riderzdrivers;

import android.os.Bundle;
import android.app.Activity;

public class DriverProfile extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
//        when create, just make a get http request for driver info and go from there
//        back button is only way to go back and nothing else is done here
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_profile);
    }

}
