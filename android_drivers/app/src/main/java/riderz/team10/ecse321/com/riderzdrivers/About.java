package riderz.team10.ecse321.com.riderzdrivers;

import android.os.Bundle;
import android.widget.TextView;

import riderz.team10.ecse321.com.riderzdrivers.assets.template.activity.AppCompatActivityBack;

public class About extends AppCompatActivityBack {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        ((TextView) findViewById(R.id.about)).setText(
                "Riderz Drivers application version " + BuildConfig.VERSION_NAME);
    }
}
