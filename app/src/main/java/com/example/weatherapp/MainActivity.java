package com.example.weatherapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.DownloadManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.weatherapp.Models.weather;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class MainActivity extends AppCompatActivity{

    private FusedLocationProviderClient fusedLocationClient;
    Location localCation;
    TextView txtLocation, txtTemp;
    private final int MY_PERMISSIONS_REQUEST_LOCATION = 123;
    private boolean locationPermission;
    private weather currWeather = new weather();
    private List<weather> forecast = new ArrayList<>();
    private String api_key = "7e1bc3a2447407b3b5bc7dfd5f68b1c8";
    private String url;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        txtLocation = findViewById(R.id.txtLocation);
        txtTemp = findViewById(R.id.txtTemp);


        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_CALENDAR)
                != PackageManager.PERMISSION_GRANTED) {
            permissionHandler(this);
        }
        System.out.println(locationPermission);
        if(locationPermission) {
            System.out.println("Attempting to retrieve location");
            fusedLocationClient.getLastLocation()
                    .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            txtLocation.setText("Location gotten");
                            System.out.println(location);
                            localCation = location;
                            if (location != null) {
                                double lat = localCation.getLatitude();
                                double log = localCation.getLongitude();
                                txtLocation.setText(String.format("Longitude: %.5f | Latitude: %.5f",log,lat));
                                getWeather(lat,log);
                                txtTemp.setText(currWeather.getTemperature());
                            } else {
                                Toast toast = Toast.makeText(getApplicationContext(), "No Location Found", Toast.LENGTH_LONG);
                                toast.show();
                            }
                        }
                    });
        } else {
            txtLocation.setText("Location Services Disabled");
        }










    }


    public void getLocation(FusedLocationProviderClient client, final Context context) {

    }

    private void permissionHandler(Context activityContext) {
        if (ContextCompat.checkSelfPermission(activityContext,
                Manifest.permission.READ_CONTACTS)
                != PackageManager.PERMISSION_GRANTED) {

            // Permission is not granted
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,
                    Manifest.permission.ACCESS_COARSE_LOCATION)) {
                Toast toast = Toast.makeText(getApplicationContext(), "Weather App will not work without location", Toast.LENGTH_LONG);
                toast.show();
                locationPermission = false;
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
            } else {
                // No explanation needed; request the permission
                ActivityCompat.requestPermissions(MainActivity.this,
                        new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
                locationPermission = true;

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        } else {
            // Permission has already been granted
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                } else {

                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request.
        }
    }

    public weather getWeather(double longitude, double latitude) {
        if(locationPermission) {
            url  = String.format("https://api.darksky.net/forecast/%s/%f,%f",api_key,latitude,longitude);
        }
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);
        // Request a string response from the provided URL.
        JsonObjectRequest jos = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                currWeather = toWeather1(response,"currently");
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        // Add the request to the RequestQueue.
        queue.add(jos);

        return currWeather;

    }

    public weather toWeather1(JSONObject jsonObject, String key) {
        weather weatherObj = new weather();
        try {
            if (jsonObject.get(key) instanceof JSONObject) {
                System.out.println("creating a new weather object: "+key);
                JSONObject obj = ((JSONObject) jsonObject.get("currently"));
                weatherObj.setIcon(obj.getString("icon"));
                weatherObj.setMoonPhase(obj.getInt("moonphase"));
                weatherObj.setName(key);
                weatherObj.setTemperature(obj.getInt("temperature"));
            }
        } catch (JSONException e) {

        }


    /*
     ArrayList<String> list = new ArrayList<>();
                    JSONArray jsonArray = (JSONArray)obj.get("Type");
                    if (jsonArray != null) {
                        int len = jsonArray.length();
                        for (int i=0;i<len;i++){
                            list.add(jsonArray.get(i).toString());
                        }
                    }
     */
    return weatherObj;
    }


}
