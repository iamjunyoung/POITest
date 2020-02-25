package com.bbeaggoo.poitest;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    private POIManager poiManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /*
        poiManager = new POIManager(this, "3F");
        poiManager.generatePoi();

        poiManager.showAllPOIDatasInDB();
        */
    }
}
