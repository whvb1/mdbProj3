package com.example.weatherapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.DownloadManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.weatherapp.Adapters.ForecastAdapter;
import com.example.weatherapp.Models.weather;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class MainActivity extends AppCompatActivity{

    private FusedLocationProviderClient fusedLocationClient;
    Location localCation;
    TextView txtLocation, txtTemp;
    Button btnRefresh;
    RecyclerView recycleWeather;
    ForecastAdapter forecastAdapter;
    private final int MY_PERMISSIONS_REQUEST_LOCATION = 123;
    private boolean locationPermission;
    private weather currWeather = new weather();
    private List<weather> forecastDummy = new ArrayList<>();
    private List<weather> forecast = new ArrayList<>();
    private String api_key = "7e1bc3a2447407b3b5bc7dfd5f68b1c8";
    private String url;
    private String forecast_description;
    private String forecast_icon;
    weather day1 = new weather();
    weather day2 = new weather();
    weather day3 = new weather();
    private int base;
    private HashMap<Integer, String> dayMapper = new HashMap<>();
    private HashMap<String, Integer> inverseMapper = new HashMap<>();
    SimpleDateFormat sdf = new SimpleDateFormat("EEEE");
    Date d = new Date();
    String dayOfTheWeek = sdf.format(d);









    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        txtLocation = findViewById(R.id.txtLocation);
        txtTemp = findViewById(R.id.txtTemp);
        btnRefresh = findViewById(R.id.btnRefresh);
        btnRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Save the current text in the txtNote field into the note.txt file.
                setCurrWeather(localCation.getLatitude(),localCation.getLongitude());
                txtTemp.setText(Integer.toString(currWeather.getTemperature()));
                forecastAdapter.setData((ArrayList)forecast);
                Log.d("refresh",forecast.toString());
                // Show a toast that the file was saved.
                Toast refreshed = Toast.makeText(getApplicationContext(), "Refreshed", Toast.LENGTH_SHORT);
                refreshed.show();
            }
        });

        dayMapper.put(0,"Monday");
        dayMapper.put(1,"Tuesday");
        dayMapper.put(2,"Wednesday");
        dayMapper.put(3,"Thursday");
        dayMapper.put(4,"Friday");
        dayMapper.put(5,"Saturday");
        dayMapper.put(6,"Sunday");



        for(int i = 0; i < dayMapper.size(); i ++) {
            inverseMapper.put(dayMapper.get(i), i);
        }
        base = inverseMapper.get(dayOfTheWeek);


        // Initialize the recycler view.
        recycleWeather = findViewById(R.id.recycleWeather);
        forecastAdapter = new ForecastAdapter((ArrayList)forecast);
        recycleWeather.setAdapter(forecastAdapter);
        recycleWeather.setLayoutManager(new LinearLayoutManager(this));


        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            permissionHandler(this);
        }
        System.out.println(locationPermission);
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
                                setCurrWeather(lat,log);
                                System.out.println(currWeather.getTemperature());
                                txtTemp.setText(Integer.toString(currWeather.getTemperature()));
                            } else {
                                Toast toast = Toast.makeText(getApplicationContext(), "No Location Found", Toast.LENGTH_LONG);
                                toast.show();
                                txtLocation.setText("Location Services Disabled");
                            }
                        }
                    });

    }


    public void getLocation(FusedLocationProviderClient client, final Context context) {

    }

    private void permissionHandler(Context activityContext) {
        System.out.println("calling permission handler");
        if (ContextCompat.checkSelfPermission(activityContext,
                Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Permission is not granted
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,
                    Manifest.permission.ACCESS_COARSE_LOCATION)) {
                Toast toast = Toast.makeText(getApplicationContext(), "Weather App will not work without location", Toast.LENGTH_LONG);
                toast.show();
                Log.d("permission",String.format("requesting the permission"));
                locationPermission = true;
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

    public void setCurrWeather(double longitude, double latitude) {
        System.out.println("calling setCurrWeather");
        if(locationPermission) {
            url  = String.format("https://api.darksky.net/forecast/%s/%f,%f",api_key,latitude,longitude);
        }
        url = "https://api.darksky.net/forecast/7e1bc3a2447407b3b5bc7dfd5f68b1c8/37.8267,-122.4233";
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(MainActivity.this);
        // Request a string response from the provided URL.
        JsonObjectRequest jos = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                currWeather = toWeather1(response,"currently");
                toForecast(response,"daily");
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        // Add the request to the RequestQueue.
        queue.add(jos);
    }

    public weather toWeather1(JSONObject jsonObject, String key) {
        System.out.println("calling toWeather1");
        weather weatherObj = new weather();
        try {
            Log.d("weatherAPI",String.format("creating a new weather object: %s",key));
            System.out.println("creating a new weather object: "+key);
            JSONObject obj = ((JSONObject) jsonObject.get("currently"));
            weatherObj.setIcon(obj.getString("icon"));
            //weatherObj.setMoonPhase(obj.getInt("moonphase"));
            weatherObj.setName(key);
            weatherObj.setTemperature(obj.getInt("temperature"));
            weatherObj.setSummary(obj.getString("summary"));
            System.out.println("weather object: "+weatherObj.toString());
    } catch (JSONException e) {
            e.printStackTrace();

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

    public void toForecast(JSONObject jsonObject, String key) {
        System.out.println("calling toForecast");
        //can work for daily, minutely, etc.
        try {
            forecast_description = jsonObject.getJSONObject(key).getString("summary");
            forecast_icon = jsonObject.getJSONObject(key).getString("icon");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JSONArray days = null;
        try {
            days = jsonObject.getJSONObject(key).getJSONArray("data");
            Log.d("forecast: success days",days.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.d("forecast: days",days.toString());
           for(int i = 1; i < days.length(); i++) {
               JSONObject day = null;
               try {
                   day = (JSONObject)days.get(i);
                   if (day instanceof JSONObject) {
                       Log.d("forecast: day",day.toString());
                       JSONObject obj = day;
                       weather weath = new weather();
                       weath.setName(dayMapper.get(wrap(base+i)));
                       weath.setTemperature(obj.getInt("temperatureHigh"));
                       weath.setTemperatureHigh(obj.getInt("temperatureHigh"));
                       weath.setTemperatureLow(obj.getInt("temperatureLow"));
                       weath.setSummary(obj.getString("summary"));
                       //weath.setMoonPhase(obj.getDouble("moonphase"));
                       forecast.add(weath);
                       Log.d("forecast: FORECAST",forecast.toString());
                   }
               } catch (JSONException ex) {
                   ex.printStackTrace();
               }

           }
    }


    public int wrap(int i) {
        i = i%dayMapper.size();
        return i;

    }
}
