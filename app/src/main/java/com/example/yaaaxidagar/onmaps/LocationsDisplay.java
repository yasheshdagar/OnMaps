package com.example.yaaaxidagar.onmaps;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class LocationsDisplay extends AppCompatActivity {

    TextView latlng,location;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_locations_display);

        latlng=(TextView)findViewById(R.id.latlng);
        latlng.setText(MainActivity.latitude+", "+MainActivity.longitude);

        location=(TextView)findViewById(R.id.location);
        location.setText(MainActivity.locality);
    }
}
