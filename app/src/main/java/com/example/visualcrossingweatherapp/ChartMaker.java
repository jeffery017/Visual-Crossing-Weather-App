package com.example.visualcrossingweatherapp;

import android.content.res.Configuration;
import android.graphics.Color;
import android.util.Log;
import android.view.View;

import com.example.visualcrossingweatherapp.databinding.ActivityMainBinding;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class ChartMaker {
    private static final String TAG = "ChartMaker";

    private static final SimpleDateFormat sdf =
            new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);

    private static final SimpleDateFormat sdf2 =
            new SimpleDateFormat("yyyy-MM-dd ", Locale.US);

    private static final SimpleDateFormat sdf3 =
            new SimpleDateFormat("MMMM dd, yyyy h:mm a", Locale.US);

    //private TreeMap<String, Double> temperatureData;
    private static MainActivity mainActivity;
    private final ActivityMainBinding binding;

    public ChartMaker(MainActivity mainActivity, ActivityMainBinding binding) {
        ChartMaker.mainActivity = mainActivity;
        this.binding = binding;
    }

    public void makeChart(ArrayList<Weather.HourlyWeather> hours) {

        setupChart(binding.mainChart);
        setupXAxis(binding.mainChart);
        setupYAxis(binding.mainChart);
        setData(binding.mainChart, hours);

        binding.mainChart.setVisibility(View.VISIBLE);
    }


    private void setData(LineChart mChart, ArrayList<Weather.HourlyWeather> hours) {

        boolean isCelsius = mainActivity.getIsCelsius();
        ArrayList<Entry> values = new ArrayList<>();
        Float isFirstValue = null;

        for (int i = 0; i < 12; i++) {
            try {
                long time = hours.get(i).getDateTimeEpoch();
                float temp = isCelsius ?  (float) hours.get(i).getTempC() : (float) hours.get(i).getTempF();
                if (isFirstValue == null)
                    isFirstValue = temp;
                values.add(new Entry(time, temp));
            } catch (Exception e) {
                Log.e(TAG, "setData: ", e);
            }
        }

        Log.d(TAG, "setData: " );
        LineDataSet lineDataSet;
        lineDataSet = new LineDataSet(values, "DataSet 1");
        lineDataSet.setDrawIcons(false);
        lineDataSet.setColor(Color.WHITE);
        lineDataSet.setCircleColor(Color.BLACK);
        lineDataSet.setLineWidth(3f);
        lineDataSet.setDrawCircles(false);
        lineDataSet.setDrawValues(false);
        lineDataSet.setValueTextSize(12f);
        lineDataSet.setDrawFilled(false);
        lineDataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);

        ArrayList<ILineDataSet> dataSets = new ArrayList<>();
        dataSets.add(lineDataSet); // add the datasets

        LineData data = new LineData(dataSets);
        mChart.setVisibility(View.VISIBLE);
        mChart.clear();
        mChart.setData(data);
        mChart.invalidate();


        // Add vertical line to show current time
        LimitLine llXAxis = new LimitLine(System.currentTimeMillis());// + (30 * 60 * 1000));
        llXAxis.setLineWidth(1f);
        llXAxis.setLineColor(Color.WHITE);
        mChart.getXAxis().removeAllLimitLines(); // reset all limit lines to avoid overlapping lines
        mChart.getXAxis().addLimitLine(llXAxis);
    }

    private void setupChart(LineChart mChart) {
        mChart.setDrawGridBackground(false);
        mChart.getDescription().setEnabled(false);
        mChart.setTouchEnabled(true);
        mChart.setDragEnabled(true);
        mChart.setScaleEnabled(true);
        mChart.setPinchZoom(true);
        mChart.setAutoScaleMinMaxEnabled(true);
        mChart.getAxisRight().setEnabled(false);
        mChart.animateX(500);
        mChart.setExtraBottomOffset(4f);
        mChart.setExtraRightOffset(20f);
        Legend l = mChart.getLegend();
        l.setEnabled(false);

        mChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener()
        {
            @Override
            public void onValueSelected(Entry e, Highlight h)
            {
                //mainActivity.displayChartTemp(e.getX(), e.getY());
            }

            @Override
            public void onNothingSelected()
            {

            }
        });
    }


    private void setupXAxis(LineChart mChart) {

        XAxis xAxis = mChart.getXAxis();
        xAxis.enableGridDashedLine(10f, 10f, 0f);
        xAxis.setGridColor(Color.parseColor("#DDFFFFFF"));
        xAxis.setValueFormatter(new MyCustomXAxisValueFormatter());
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setTextColor(Color.WHITE);
        xAxis.setTextSize(15);
        xAxis.setLabelRotationAngle(90);
        xAxis.setLabelCount(8, true);
        xAxis.setSpaceMax(0.0f);
        xAxis.setSpaceMin(0.0f);

    }


    private void setupYAxis(LineChart mChart) {

        YAxis leftAxis = mChart.getAxisLeft();
        leftAxis.removeAllLimitLines(); // reset all limit lines to avoid overlapping lines
        leftAxis.enableGridDashedLine(10f, 10f, 0f);
        leftAxis.setValueFormatter(new MyCustomYAxisValueFormatter());
        leftAxis.setGridColor(Color.parseColor("#DDFFFFFF"));

        int orientation = mainActivity.getResources().getConfiguration().orientation;
        int labelCount = (orientation == Configuration.ORIENTATION_LANDSCAPE) ? 4 : 6;
        leftAxis.setLabelCount(labelCount, true);


        leftAxis.setDrawZeroLine(false);
        leftAxis.setTextColor(Color.WHITE);
        leftAxis.setTextSize(16);
        leftAxis.setDrawLimitLinesBehindData(true);
    }



    public static class MyCustomXAxisValueFormatter extends ValueFormatter {
        private final SimpleDateFormat simpleDateFormat;

        MyCustomXAxisValueFormatter() {
            simpleDateFormat = new SimpleDateFormat("h a", Locale.US);
        }

        @Override
        public String getFormattedValue(float value) {
            //value += 5 * 60 * 1000;
            Date d = new Date((long) value * 1000);
            return simpleDateFormat.format(d).toLowerCase();
        }

    }

    public static class MyCustomYAxisValueFormatter extends ValueFormatter {

        @Override
        public String getFormattedValue(float value) {
            String unit = mainActivity.getIsCelsius() ? "°C" : "°F";
            return String.format(Locale.getDefault(), "%.0f%s°", value, unit);
        }

    }
}
