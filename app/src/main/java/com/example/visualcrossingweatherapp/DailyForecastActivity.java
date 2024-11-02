package com.example.visualcrossingweatherapp;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.visualcrossingweatherapp.databinding.ActivityDailyForecastBinding;

import java.lang.reflect.Field;
import java.util.ArrayList;

public class DailyForecastActivity extends AppCompatActivity {
    private static final String TAG = "DailyForecastActivity";
    public ActivityDailyForecastBinding binding;
    private ArrayList<Weather> weathers = new ArrayList<>();
    private Location location = new Location(0, 0, "");
    private boolean isCelsius = false;
    private DailyWeatherAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityDailyForecastBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        Intent intent = getIntent();
        weathers = (ArrayList<Weather>) intent.getSerializableExtra("weathers");
        location = (Location) intent.getSerializableExtra("location");
        isCelsius = intent.getBooleanExtra("isCelsius", false);

        // recycler view
        adapter = new DailyWeatherAdapter(this, weathers);
        binding.recyclerDailyForcast.setAdapter(adapter);
        binding.recyclerDailyForcast.setLayoutManager(new LinearLayoutManager(this));

        String titleText = String.format("%s 15-Day Forecast", location.resolvedAddress);
        binding.titleDailyForecast.setText(titleText);
    }


    public static int getId(String resourceName, Class<?> c) {
        try {
            Field idField = c.getDeclaredField(resourceName);
            return idField.getInt(idField);
        } catch (Exception e) {
            return 0;
        }
    }

    public boolean isCelsius() {
        return isCelsius;
    }
}