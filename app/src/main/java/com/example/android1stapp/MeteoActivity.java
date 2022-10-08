package com.example.android1stapp;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;

import java.text.DecimalFormat;


public class MeteoActivity extends AppCompatActivity {

    private FusedLocationProviderClient fusedLocationProviderClient;

    private Location currentLocation;

    private static final String TAG = "MeteoActivity";
    private int LOCATION_REQUEST_CODE = 10001;
    DecimalFormat df = new DecimalFormat("#.##");

    //DecimalFormat df = new DecimalFormat("#.##");

    private String cityString;
    private String urlGetCity;
    private String urlGetWeather;
    private final String appid = "1e2d1217e9f0dc3db773c2f36f768a14";

    //API for coordinates to city name : http://api.openweathermap.org/geo/1.0/reverse?lat={lat}&lon={lon}&limit={limit}&appid={API key}
    //API for city name to weather :https://api.openweathermap.org/data/2.5/weather" + "?q=" + city + "," + country + "&appid=" + appid
    //https://api.openweathermap.org/data/2.5/weather?q=Nancy&appid=1e2d1217e9f0dc3db773c2f36f768a14
    //=============================================================================


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meteo);

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);


        Button localisation = findViewById(R.id.buttonLocalisation);

        currentLocation = new Location((String) null);

        localisation.setOnClickListener((v) -> {


            Toast.makeText(MeteoActivity.this, "Localisation: " + "Latitude: " + String.valueOf(currentLocation.getLatitude()) + " Longitude: " + String.valueOf(currentLocation.getLongitude()), Toast.LENGTH_SHORT).show();
            System.out.println("Localisation: " + "Latitude: " + String.valueOf(currentLocation.getLatitude()) + " Longitude: " + String.valueOf(currentLocation.getLongitude()));
            try {
                this.getCityName(currentLocation.getLatitude(), currentLocation.getLongitude());
                //this.getWeatherDetails();
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

        });
    }

    protected void getCityName(double lat, double lon) throws JSONException, IOException {

        StringBuilder urlGetCityBuilder = new StringBuilder("http://api.openweathermap.org/geo/1.0/reverse?lat=" + lat + "&lon=" + lon + "&limit=1&appid=" + appid);

        this.urlGetCity = urlGetCityBuilder.toString();

        Log.d("successful got url city", this.urlGetCity);

        Toast.makeText(MeteoActivity.this, "URL CITY: " + this.urlGetCity, Toast.LENGTH_SHORT).show();


        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, this.urlGetCity, null, new Response.Listener<JSONArray>() {
            //private String city;

            @Override
            public void onResponse(JSONArray response) {
                try {
                    JSONObject jsonObjectCity = response.getJSONObject(0);
                    cityString = jsonObjectCity.getString("name");
                    Log.d("successful search city", cityString);
                    Toast.makeText(MeteoActivity.this, "Name city: " + cityString, Toast.LENGTH_SHORT).show();
                }
                catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(MeteoActivity.this, "Fail onResponse City", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener(){

            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), error.toString().trim(), Toast.LENGTH_SHORT).show();
            }
        });
        Volley.newRequestQueue(this).add(jsonArrayRequest);

        this.getWeatherDetails();



    }

    public void getWeatherDetails() throws IOException, JSONException {

        if(this.cityString==null){
            this.cityString = "Nancy";
        }
        this.urlGetWeather = new String("http://api.openweathermap.org/data/2.5/weather" + "?q=" + this.cityString + "&appid=" + appid);


        Log.d("weather", this.urlGetWeather);
        Toast.makeText(MeteoActivity.this, "URL WEATHER: " + this.urlGetWeather, Toast.LENGTH_SHORT).show();

        

        StringRequest stringRequest = new StringRequest(Request.Method.GET, this.urlGetWeather, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {

                    JSONObject jsonResponse = new JSONObject(response);
                    JSONArray jsonArray = jsonResponse.getJSONArray("weather");
                    JSONObject jsonObjectWeather = jsonArray.getJSONObject(0);
                    String description = jsonObjectWeather.getString("description");
                    JSONObject jsonObjectMain = jsonResponse.getJSONObject("main");
                    double temp = jsonObjectMain.getDouble("temp") - 273.15;
                    double feelsLike = jsonObjectMain.getDouble("feels_like") - 273.15;
                    float pressure = jsonObjectMain.getInt("pressure");
                    int humidity = jsonObjectMain.getInt("humidity");
                    JSONObject jsonObjectWind = jsonResponse.getJSONObject("wind");
                    String wind = jsonObjectWind.getString("speed");
                    JSONObject jsonObjectClouds = jsonResponse.getJSONObject("clouds");
                    String clouds = jsonObjectClouds.getString("all");
                    JSONObject jsonObjectSys = jsonResponse.getJSONObject("sys");
                    String countryName = jsonObjectSys.getString("country");
                    String cityName = jsonResponse.getString("name");

                    String weatherString = "Current weather of " + cityName + " (" + countryName + ")"
                            + "\n Temp: " + df.format(temp) + " °C"
                            + "\n Feels Like: " + df.format(feelsLike) + " °C"
                            + "\n Humidity: " + humidity + "%"
                            + "\n Description: " + description
                            + "\n Wind Speed: " + wind + "m/s (meters per second)"
                            + "\n Cloudiness: " + clouds + "%"
                            + "\n Pressure: " + pressure + " hPa";


                    Log.d("successful search weather", weatherString);
                    Toast.makeText(MeteoActivity.this, weatherString, Toast.LENGTH_SHORT).show();
                }
                catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(MeteoActivity.this, "Fail onResponse Weather", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener(){

            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), error.toString().trim(), Toast.LENGTH_SHORT).show();
            }
        });
        Volley.newRequestQueue(this).add(stringRequest);

    }

    @Override
    protected void onStart() {
        super.onStart();
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            getLastLocations();
        } else {
            askLocationPermission();
        }
    }

    private void getLastLocations() {
        @SuppressLint("MissingPermission") Task<Location> locationTask = fusedLocationProviderClient.getLastLocation();
        locationTask.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {
                    //We have a location
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(MeteoActivity.this, "Succes: Acces a la localisation", Toast.LENGTH_SHORT).show();
                            currentLocation.setLatitude(location.getLatitude());
                            currentLocation.setLongitude(location.getLongitude());
                        }
                    });


                } else {
                    Log.d(TAG, "onSuccess: Location was null...");
                }
            }
        });

        locationTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e(TAG, "onFailure: " + e.getLocalizedMessage());
            }
        });

    }

    private void askLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                Log.d(TAG, "askLocationPermission: you should show an alert dialog...");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_REQUEST_CODE);
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_REQUEST_CODE);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == LOCATION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //Permission granted
                getLastLocations();
            } else {
                //Permission denied

            }
        }
    }
}