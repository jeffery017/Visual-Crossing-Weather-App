package com.example.visualcrossingweatherapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.visualcrossingweatherapp.databinding.ActivityMainBinding;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    public ActivityMainBinding binding;
    private final ArrayList<Weather> weathers = new ArrayList<>();
    private final ArrayList<Weather.HourlyWeather> hours = new ArrayList<>();
    private final Location location = new Location(0, 0, "");
    private boolean isCelsius = false;
    private HourlyWeatherAdapter adapter;

    @SuppressLint("DefaultLocale")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // recycler view
        adapter = new HourlyWeatherAdapter(this, hours);
        binding.hourlyWeatherRecyclerMain.setAdapter(adapter);
        binding.hourlyWeatherRecyclerMain.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));


        // unit icon
        binding.iconToggleUnitMain.setImageResource(isCelsius ? R.drawable.units_c : R.drawable.units_f);

        // Download data
        WeatherDownloader.downloadWeathersByLocation(this, "Chicago");

        // Set up chart
    }

    // ========================= Icon Handlers ==========================
    public void onShowInMapClick(View view) {
        Log.d(TAG, "onShowInMapClick: ");
        WeatherDownloader.downloadWeathersByLocation(this, "Chicago");
    }

    public void onShowCurrentLocationClick(View view) {
        Log.d(TAG, "onShowCurrentLocationClick: ");
    }

    public void onShareClick(View view) {
        Log.d(TAG, "onShareClick: ");
    }

    public void onToggleUnitClick(View view) {
        Log.d(TAG, "onToggleUnitClick: ");
        toggleUnits();
    }
    public void onDailyForecastClick(View view) {
        Log.d(TAG, "onDailyForecastClick: ");
        // create intent
        Intent intent = new Intent(this, DailyForecastActivity.class);
        intent.putExtra("weathers", this.weathers);
        intent.putExtra("location", this.location);
        intent.putExtra("isCelsius", this.isCelsius);
        startActivity(intent);
    }


    public void onSetLocationClick(View view) {
        Log.d(TAG, "onSetLocationClick: ");
    }

    // ========================= Helper Functions ==========================
    private String getTitleTimeText(long timeEpoch) {
        return DateTimeFormatter.ofPattern("EEE MMM-dd hh:mm a")
                .format(Instant.ofEpochMilli(timeEpoch).atZone(ZoneId.systemDefault()));
    }

    private String getHourlyTimeText(long timeEpoch) {
        return DateTimeFormatter.ofPattern("hh:mm a")
                .format(Instant.ofEpochMilli(timeEpoch).atZone(ZoneId.systemDefault()));
    }
    @SuppressLint("DefaultLocale")
    private String getFormattedWindText(double windspeed, double windgust, double winddir) {
        final String[] directions = {"N", "NE", "E", "SE", "S", "SW", "W", "NW"};
        int dir = (int) winddir / 45;
        return String.format("Winds: %s %.0f mph gusting to %.0f mph", directions[dir], windspeed, windgust);
    }

    public boolean getIsCelsius() {
        return isCelsius;
    }

    private void toggleUnits() {
        isCelsius = !isCelsius;
        binding.iconToggleUnitMain.setImageResource(isCelsius ? R.drawable.units_c : R.drawable.units_f);
        updateDataView();
        adapter.notifyDataSetChanged();
    }

    public void updateWeatherData(ArrayList<Weather> days) {
        this.weathers.clear();
        this.weathers.addAll(days);
        //this.hours.addAll(weathers.get(0).getHours());

    }

    public void updateHoursData(ArrayList<Weather> days) {
        this.hours.clear();
        //this.hours.addAll(days.get(0).getHours());
        final int SIZE = 24;
        int dayId = 0;
        int hourId = 0;
        // get now timestamp
        long nowEpoch = System.currentTimeMillis() / 1000;

        Weather today = days.get(dayId);
        ArrayList<Weather.HourlyWeather> hours = today.getHours();
        while (this.hours.size() < SIZE) {
            if (hourId == 24) {
                hours = days.get( ++dayId ).getHours();
                hourId = 0;
            }
            if (hours.get(hourId).getDateTimeEpoch() < nowEpoch) {
                hourId++;
                continue;
            }
            this.hours.add(hours.get(hourId));
            hourId++;
        }


    }

    public void updateLocation(double latitude, double longitude, String resolvedAddress) {
        this.location.latitude = latitude;
        this.location.longitude = longitude;
        int i = resolvedAddress.indexOf(',');
        // Check if a comma exists in the string
        if (i != -1) {
            resolvedAddress = resolvedAddress.substring(0, i);
        }
        this.location.resolvedAddress = resolvedAddress;
    }

    // store data in bundle
    @Override
    public void onSaveInstanceState(@NonNull Bundle outState, @NonNull PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }


    @SuppressLint("DefaultLocale")
    public void updateDataView() {
        Weather todayWeather = weathers.get(0);
        String unit = isCelsius ? "°C" : "°F";
        double temp = isCelsius ?  hours.get(0).getTempC() : hours.get(0).getTempF();
        double feelsLike = isCelsius ?  hours.get(0).getFeelslikeC() : hours.get(0).getFeelslikeF();

        // bind data to view
        String titleTime = getTitleTimeText(todayWeather.getDatetimeEpoch());


        binding.addrTimeTextMain.setText(String.format("%s, %s", location.resolvedAddress, titleTime));
        binding.tempTitleMain.setText( String.format("%.0f%s", temp, unit) );
        binding.feelsLikeTextMain.setText( String.format("Feels Like %.0f%s", feelsLike, unit) );
        binding.weatherDescriptionTextMain.setText( todayWeather.getDescription());
        binding.windTextMain.setText( getFormattedWindText(todayWeather.getWindspeed(), todayWeather.getWindgust(), todayWeather.getWinddir()) );
        binding.humidityTextMain.setText( String.format("Humidity: %.0f", todayWeather.getHumidity()) );
        binding.uvTextMain.setText( String.format("UV Index: %.0f", todayWeather.getUvindex()) );
        binding.visibilityTextMain.setText( String.format("%s: %.2f", "Visibility", todayWeather.getVisibility()) );
        binding.sunriseTextMain.setText( String.format("Sunrise: %s", getHourlyTimeText(todayWeather.getSunriseEpoch())) );
        binding.sunsetTextMain.setText( String.format("Sunset: %s", getHourlyTimeText(todayWeather.getSunsetEpoch())) );

        // weather main image
        String iconName = hours.get(0).getIcon();
        iconName = iconName.replace("-", "_"); // Replace all dashes with underscores
        int iconID = Utils.getId(iconName);
        binding.weatherImageMain.setImageResource(iconID);
//        int iconResourceId = this.getResources().getIdentifier(iconName, "drawable", this.getPackageName());
//        binding.weatherImageMain.setImageResource(iconResourceId);

        // update chart data
        ChartMaker chartMaker = new ChartMaker(this, binding);
        chartMaker.makeChart(hours);

        adapter.notifyDataSetChanged();

    }


}