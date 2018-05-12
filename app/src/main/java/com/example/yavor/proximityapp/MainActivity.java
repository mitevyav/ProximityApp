package com.example.yavor.proximityapp;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.example.yavor.proximityapp.location.LocationManager;
import com.example.yavor.proximityapp.location.LocationManagerImpl;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    public static final String SOURCE_JSON = "{\n" +
                                             "   \"html_attributions\" : [],\n" +
                                             "   \"results\" : [\n" +
                                             "      {\n" +
                                             "         \"geometry\" : {\n" +
                                             "            \"location\" : {\n" +
                                             "               \"lat\" : -33.8609472,\n" +
                                             "               \"lng\" : 151.209872\n" +
                                             "            },\n" +
                                             "            \"viewport\" : {\n" +
                                             "               \"northeast\" : {\n" +
                                             "                  \"lat\" : -33.85959737010728,\n" +
                                             "                  \"lng\" : 151.2112218298927\n" +
                                             "               },\n" +
                                             "               \"southwest\" : {\n" +
                                             "                  \"lat\" : -33.86229702989272,\n" +
                                             "                  \"lng\" : 151.2085221701072\n" +
                                             "               }\n" +
                                             "            }\n" +
                                             "         },\n" +
                                             "         \"icon\" : \"https://maps.gstatic.com/mapfiles/place_api/icons/generic_business-71.png\",\n" +
                                             "         \"id\" : \"9ea7c77cb181b1f33d19c9d76121fcc6d5246ad8\",\n" +
                                             "         \"name\" : \"Australian Cruise Group Circular Quay\",\n" +
                                             "         \"opening_hours\" : {\n" +
                                             "            \"open_now\" : false,\n" +
                                             "            \"weekday_text\" : []\n" +
                                             "         },\n" +
                                             "         \"photos\" : [\n" +
                                             "            {\n" +
                                             "               \"height\" : 1152,\n" +
                                             "               \"html_attributions\" : [\n" +
                                             "                  \"\\u003ca href=\\\"https://maps.google.com/maps/contrib/112378780393544273770/photos\\\"\\u003eAustralian Cruise Group Circular Quay\\u003c/a\\u003e\"\n" +
                                             "               ],\n" +
                                             "               \"photo_reference\" : \"CmRaAAAAFimeOecAR2A84AZhMeoyaNP0cFb50pWRo8vBumMNbXfIQ1ymBsaEp0tgEyCaBoEIBJRmjr1AWIsSwsER84mLUbhqwvs0_04C31vEv0M5l3PwN8XWH8dARtoQgB92gWI7EhDNyt-ZA1ODNb2wWQD0C7LEGhQM5ix-LLaPDvwj0c7DUEZWKcph7w\",\n" +
                                             "               \"width\" : 2048\n" +
                                             "            }\n" +
                                             "         ],\n" +
                                             "         \"place_id\" : \"ChIJpU8KgUKuEmsRKErVGEaa11w\",\n" +
                                             "         \"rating\" : 3,\n" +
                                             "         \"reference\" : \"CmRbAAAAwEnMVO_mj8t4eWJeZ_AorZoeZmwH2--DRkdKzXH3d8E2eqjZ4N1H7m-OsYwFseKaHu8t7QksT0rofxu4GikdfITtVbemB__VKYGPnG4zTdcXNFsCCpDz9danvQ-9Pxg-EhDUC4wutuUjJXtLirfBEhsTGhQY_NsB4S-DA8jGTLuZv8d0KxlwIw\",\n" +
                                             "         \"scope\" : \"GOOGLE\",\n" +
                                             "         \"types\" : [\n" +
                                             "            \"travel_agency\",\n" +
                                             "            \"restaurant\",\n" +
                                             "            \"food\",\n" +
                                             "            \"point_of_interest\",\n" +
                                             "            \"establishment\"\n" +
                                             "         ],\n" +
                                             "         \"vicinity\" : \"6 Cirular Quay, Sydney\"\n" +
                                             "      }\n" +
                                             "   ],\n" +
                                             "   \"status\" : \"OK\"\n" +
                                             "}";

    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 505;

    private LocationManager locationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        locationManager = new LocationManagerImpl(this, this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume");
        checkPermissions();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[],
                                           int[] grantResults) {
        switch (requestCode) {
            case PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
                if (grantResults.length > 0 &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    locationManager.startLocationUpdates();
                } else {

                }
                return;
            }
        }
    }

    private void checkPermissions() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) !=
            PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                                              new String[] {Manifest.permission.ACCESS_FINE_LOCATION},
                                              PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
    }
}
