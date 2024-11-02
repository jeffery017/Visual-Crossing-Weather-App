package com.example.visualcrossingweatherapp;

import android.annotation.SuppressLint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.visualcrossingweatherapp.databinding.HourlyWeatherItemViewBinding;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class HourlyWeatherAdapter extends RecyclerView.Adapter<HourlyWeatherAdapter.ViewHolder> {
    public static class ViewHolder extends RecyclerView.ViewHolder {
        public HourlyWeatherItemViewBinding binding;
        public ViewHolder(HourlyWeatherItemViewBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    private static final String TAG = "WeatherAdapter";
    private final MainActivity mainActivity;
    private final ArrayList<Weather.HourlyWeather> hourlyWeatherList;

    public HourlyWeatherAdapter(MainActivity mainActivity, ArrayList<Weather.HourlyWeather> hourlyWeatherList) {
        Log.d(TAG, "HourlyWeatherAdaptor: ");
        this.mainActivity = mainActivity;
        this.hourlyWeatherList = hourlyWeatherList;
    }

    @NonNull
    @Override
    public HourlyWeatherAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.d(TAG, "onCreateViewHolder: ");
        HourlyWeatherItemViewBinding binding =
                HourlyWeatherItemViewBinding.inflate(
                        LayoutInflater.from(parent.getContext()), parent, false
                );
        return new ViewHolder(binding);
    }

    @SuppressLint("DefaultLocale")
    @Override
    public void onBindViewHolder(@NonNull HourlyWeatherAdapter.ViewHolder holder, int position) {
        Log.d(TAG, "onBindViewHolder: ");
        // get weather data by position
        Weather.HourlyWeather weather = hourlyWeatherList.get(position);
        // bind data to view holder
        long datetimeEpoch = weather.getDateTimeEpoch();
        String dayText = isToday(datetimeEpoch) ? "Today" : getDayText(datetimeEpoch);
        holder.binding.dateTextHourlyItemView.setText(String.format("%s", dayText));
        holder.binding.timeTextHourlyItemView.setText(String.format("%s", getHourlyTimeText(datetimeEpoch)));
        boolean isCelsius = mainActivity.getIsCelsius();

        String unit = isCelsius ? "°C" : "°F";
        Double temp = isCelsius ? weather.getTempC() : weather.getTempF();
        holder.binding.tempTextHourlyItemView.setText(String.format("%.0f%s", temp, unit));

        holder.binding.contitionsTextHourlyItemView.setText(weather.getConditions());
        String iconName = weather.getIcon();
        iconName = iconName.replace("-", "_"); // Replace all dashes with underscores
        int iconID = Utils.getId(iconName);
        if (iconID == 0) {
            iconID = R.mipmap.ic_launcher;
        }
        holder.binding.imageHourlyItemView.setImageResource(iconID);
        //holder.binding.imageHourlyItemView.setImageResource();
    }

    @Override
    public int getItemCount() {
        return hourlyWeatherList.size();
    }

    private String getHourlyTimeText(long timeEpoch) {
        LocalDateTime dateTime = LocalDateTime.ofInstant(Instant.ofEpochSecond(timeEpoch), ZoneId.systemDefault());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm a");
        return dateTime.format(formatter);
    }

    private boolean isToday(long timeEpoch) {
        LocalDateTime dateTime = LocalDateTime.ofInstant(Instant.ofEpochSecond(timeEpoch), ZoneId.systemDefault());
        LocalDateTime now = LocalDateTime.now();
        return dateTime.getDayOfYear() == now.getDayOfYear();
    }

    private String getDayText(long timeEpoch) {
        LocalDateTime dateTime = LocalDateTime.ofInstant(Instant.ofEpochSecond(timeEpoch), ZoneId.systemDefault());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEE");
        return dateTime.format(formatter);
    }



}
