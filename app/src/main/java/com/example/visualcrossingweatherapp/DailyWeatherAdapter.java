package com.example.visualcrossingweatherapp;

import android.annotation.SuppressLint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.visualcrossingweatherapp.databinding.DailyWeatherItemViewBinding;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

public class DailyWeatherAdapter extends RecyclerView.Adapter<DailyWeatherAdapter.ViewHolder> {
    private static final SimpleDateFormat sdf =
            new SimpleDateFormat("EEEE MM/dd", Locale.US);

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private static final String TAG = "DailyWeatherAdapter";
        public DailyWeatherItemViewBinding binding;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = DailyWeatherItemViewBinding.bind(itemView);
        }
    }
    private static final String TAG = "DailyWeatherAdapter";
    private final DailyForecastActivity activity;
    private final ArrayList<Weather> days;

    DailyWeatherAdapter(DailyForecastActivity activity, ArrayList<Weather> days) {
        this.activity = activity;
        this.days = days;
    }


    @NonNull
    @Override
    public DailyWeatherAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.d(TAG, "onCreateViewHolder: ");
        DailyWeatherItemViewBinding binding = DailyWeatherItemViewBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false
        );
        return new ViewHolder(binding.getRoot());
    }

    @SuppressLint("DefaultLocale")
    @Override
    public void onBindViewHolder(@NonNull DailyWeatherAdapter.ViewHolder holder, int position) {
        Log.d(TAG, "onBindViewHolder: ");
        Weather day = days.get(position);
        String dateText = sdf.format(day.getDatetimeEpoch() * 1000);
        holder.binding.dateTextDailyItem.setText(dateText);

        String tempText = (activity.isCelsius()) ?
                String.format("%.0f°C/%.0f°C", day.getTempmaxC(), day.getTempminC()):
                String.format("%.0f°F/%.0f°F", day.getTempmaxF(), day.getTempminF());
        holder.binding.tempTextDailyItem.setText(tempText);
        holder.binding.descriptionDailyItem.setText(day.getDescription());
        holder.binding.precipDailyItem.setText( String.format("(%.0f%% precip.)", day.getPrecipprob()) );
        holder.binding.uvIndexDailyItem.setText( String.format("UV Index: %.0f", day.getUvindex()));

        ArrayList<Weather.HourlyWeather> hours = day.getHours();
        Weather.HourlyWeather morning = hours.get(8);
        Weather.HourlyWeather afternoon = hours.get(13);
        Weather.HourlyWeather evening = hours.get(17);
        Weather.HourlyWeather night = hours.get(23);

        if (activity.isCelsius()) {
            holder.binding.morningTempDailyItem.setText( String.format("%.0f°C", morning.getTempC()) );
            holder.binding.afternoonTempDailyItem.setText(String.format("%.0f°C", afternoon.getTempC()));
            holder.binding.eveningTempDailyItem.setText( String.format("%.0f°C", evening.getTempC()) );
            holder.binding.nightTempDailyItem.setText( String.format("%.0f°C", night.getTempC()) );
        }
        else {
            holder.binding.morningTempDailyItem.setText( String.format("%.0f°F", morning.getTempF()) );
            holder.binding.afternoonTempDailyItem.setText(String.format("%.0f°F", afternoon.getTempF()));
            holder.binding.eveningTempDailyItem.setText( String.format("%.0f°F", evening.getTempF()) );
            holder.binding.nightTempDailyItem.setText( String.format("%.0f°F", night.getTempF()) );
        }

        String iconName = day.getIcon();
        iconName = iconName.replace("-", "_"); // Replace all dashes with underscores
        int iconID = Utils.getId(iconName);
        holder.binding.imageView.setImageResource(iconID);

    }

    @Override
    public int getItemCount() {
        return days.size();
    }


}
