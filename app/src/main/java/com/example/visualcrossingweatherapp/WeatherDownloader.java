package com.example.visualcrossingweatherapp;

import android.net.Uri;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class WeatherDownloader {
    private static final String TAG = "WeatherDownloader";
    private static MainActivity mainActivity;
    private static RequestQueue queue;
    private static final ArrayList<Weather> weatherList = new ArrayList<>();
    private static final String API_KEY = "BRGDG9L6T8BY7MZDNDEZKUP5M";
    private static final Gson gson = new Gson();
    private static double latitude;
    private static double longitude;
    private static String resolvedAddress;

    public static void downloadWeathersByLocation(MainActivity mainActivity, String location) {
        WeatherDownloader.mainActivity = mainActivity;
        queue = Volley.newRequestQueue(mainActivity);
        // create URL
        String baseUrl = "https://weather.visualcrossing.com/VisualCrossingWebServices/rest/services/timeline/" + location;
        Uri.Builder uri = Uri.parse(baseUrl).buildUpon();
//        uri.appendQueryParameter("", location);
        uri.appendQueryParameter("key", API_KEY);

        Log.d(TAG, "downloadWeathersByLocation: " + uri);
        // create request
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, uri.toString(), null,
                (res) -> {
                    weatherList.clear();
                    parseWeatherJsonData(res.toString());
                    mainActivity.updateLocation(latitude, longitude, resolvedAddress);
                    mainActivity.updateWeatherData(weatherList);
                    mainActivity.updateHoursData(weatherList);
                    mainActivity.updateDataView();
                },
                (err) ->  Log.d(TAG, "downloadWeatherByLocation: ")
        );
        Log.d(TAG, "downloadWeathersByLocation: add request to queue");
        queue.add(jsonObjectRequest);
    }

    private static void parseWeatherJsonData(String jsonString) {

        try {
            JSONObject jsonObject = new JSONObject(jsonString);
            latitude = jsonObject.getDouble("latitude");
            longitude = jsonObject.getDouble("longitude");
            resolvedAddress = jsonObject.getString("resolvedAddress");
            JSONArray jsonDays = jsonObject.getJSONArray("days");
            for (int i = 0; i < jsonDays.length(); i++) {
                String dayData = jsonDays.getJSONObject(i).toString();
                Weather weather = gson.fromJson(dayData, Weather.class);

                weatherList.add(weather);
            }

        }catch (Exception e ) {
            Log.e(TAG, "parseWeatherJsonData: ", e );
        }
    }


}
