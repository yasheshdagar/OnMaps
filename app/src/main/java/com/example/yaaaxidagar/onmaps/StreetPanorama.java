package com.example.yaaaxidagar.onmaps;

import android.app.Dialog;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.maps.OnStreetViewPanoramaReadyCallback;
import com.google.android.gms.maps.StreetViewPanorama;
import com.google.android.gms.maps.StreetViewPanoramaFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.StreetViewPanoramaCamera;

public class StreetPanorama extends AppCompatActivity implements OnStreetViewPanoramaReadyCallback{

    StreetViewPanorama panorama;
    EditText editText1,editText2;
    Button proceed,current,clear;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_street_panorama);

        editText1=(EditText)findViewById(R.id.edittext1);
        editText2=(EditText)findViewById(R.id.edittext2);
        proceed=(Button)findViewById(R.id.proceed);
        current=(Button)findViewById(R.id.current);
        clear=(Button)findViewById(R.id.clear);

        proceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                String lat=editText1.getText().toString();
                String lng=editText2.getText().toString();

                if(lat.equals("") || lng.equals("")){
                    Toast.makeText(StreetPanorama.this,"Fill the fields",Toast.LENGTH_SHORT).show();
                }

                else{
                    panorama.setPosition(new LatLng(Double.parseDouble(lat),Double.parseDouble(lng)));
                    panorama.setUserNavigationEnabled(true);
                    panorama.setStreetNamesEnabled(true);
                    panorama.setZoomGesturesEnabled(true);

                    Toast.makeText(StreetPanorama.this,"Panorama ready..",Toast.LENGTH_SHORT).show();
                }

            }
        });

        current.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editText1.setText((Double.toString(MainActivity.latitude)));
                editText2.setText((Double.toString(MainActivity.longitude)));
            }
        });

        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cancel();
            }
        });

        StreetViewPanoramaFragment streetViewPanorama= (StreetViewPanoramaFragment) getFragmentManager().findFragmentById(R.id.streetfragment);
        streetViewPanorama.getStreetViewPanoramaAsync(this);

    }

    private void cancel() {

        final AlertDialog.Builder alertdialog= new AlertDialog.Builder(this);

        alertdialog.setTitle("Want to erase the values?").setMessage("The latitude and longitude values will be erased !")
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
            }).setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                editText1.setText("");
                editText2.setText("");
            }});

        alertdialog.show();

    }

    @Override
    public void onStreetViewPanoramaReady(StreetViewPanorama streetViewPanorama) {
        panorama=streetViewPanorama;
        editText1.setText((Double.toString(MainActivity.latitude)));
        editText2.setText((Double.toString(MainActivity.longitude)));
    }
}
