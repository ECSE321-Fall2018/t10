package riderz.team10.ecse321.com.riderzpassengers;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class MainMenuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        String[] activities = {"Map", "Activity 2", "Activity 3", "Activity 4"};
        ListAdapter adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, activities);

        final ListView activitiesListView = (ListView) findViewById(R.id.main_menu);
        activitiesListView.setAdapter(adapter);

        activitiesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == 0){
                    Intent intent = new Intent(MainMenuActivity.this, MapsPassengerActivity.class);
                    startActivity(intent);
                }
            }
        });
    }
}
