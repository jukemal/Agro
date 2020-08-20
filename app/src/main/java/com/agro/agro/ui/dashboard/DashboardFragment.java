package com.agro.agro.ui.dashboard;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.agro.agro.R;
import com.agro.agro.models.ApiResponse;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.Collections;
import java.util.List;

import static android.graphics.Color.RED;
import static android.graphics.Color.WHITE;
import static android.graphics.Color.rgb;

public class DashboardFragment extends Fragment {

    private LineChart humidityChart;
    private LineChart temperatureChart;
    private LineChart soilMoistureChart;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_dashboard, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        DashboardViewModel dashboardViewModel = new ViewModelProvider(requireActivity()).get(DashboardViewModel.class);

        humidityChart = view.findViewById(R.id.humidityChart);
        temperatureChart = view.findViewById(R.id.temperatureChart);
        soilMoistureChart = view.findViewById(R.id.soilMoistureChart);

        dashboardViewModel.getHumidityDataList().observe(getViewLifecycleOwner(), new Observer<List<ApiResponse>>() {
            @Override
            public void onChanged(List<ApiResponse> apiResponses) {
                Collections.reverse(apiResponses);

                initChart(humidityChart);

                for (ApiResponse entry : apiResponses) {
                    addEntry(humidityChart, entry);
                }
            }
        });

        dashboardViewModel.getHumidityDataLast().observe(getViewLifecycleOwner(), new Observer<ApiResponse>() {
            @Override
            public void onChanged(ApiResponse apiResponse) {
                addEntry(humidityChart, apiResponse);
            }
        });

        dashboardViewModel.getTemperatureDataList().observe(getViewLifecycleOwner(), new Observer<List<ApiResponse>>() {
            @Override
            public void onChanged(List<ApiResponse> apiResponses) {
                Collections.reverse(apiResponses);

                initChart(temperatureChart);

                for (ApiResponse entry : apiResponses) {
                    addEntry(temperatureChart, entry);
                }
            }
        });

        dashboardViewModel.getTemperatureDataLast().observe(getViewLifecycleOwner(), new Observer<ApiResponse>() {
            @Override
            public void onChanged(ApiResponse apiResponse) {
                addEntry(temperatureChart, apiResponse);
            }
        });

        dashboardViewModel.getSoilMoistureDataList().observe(getViewLifecycleOwner(), new Observer<List<ApiResponse>>() {
            @Override
            public void onChanged(List<ApiResponse> apiResponses) {
                Collections.reverse(apiResponses);

                initChart(soilMoistureChart);

                for (ApiResponse entry : apiResponses) {
                    addEntry(soilMoistureChart, entry);
                }
            }
        });

        dashboardViewModel.getSoilMoistureDataLast().observe(getViewLifecycleOwner(), new Observer<ApiResponse>() {
            @Override
            public void onChanged(ApiResponse apiResponse) {
                addEntry(soilMoistureChart, apiResponse);
            }
        });

        dashboardViewModel.getShowError().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                Toast.makeText(getContext(), s, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initChart(LineChart chart) {
        chart.setTouchEnabled(true);

        chart.getDescription().setEnabled(false);

        // enable scaling and dragging
        chart.setDragEnabled(true);
        chart.setScaleEnabled(true);
        chart.setDrawGridBackground(false);

        // if disabled, scaling can be done on x- and y-axis separately
        chart.setPinchZoom(true);

        // set an alternative background color
        chart.setBackgroundColor(WHITE);

//        chart.animateX(500);

        LineData data = new LineData();
        data.setValueTextColor(RED);

        // add empty data
        chart.setData(data);


        XAxis xl = chart.getXAxis();
        xl.setPosition(XAxis.XAxisPosition.BOTTOM);
        xl.setDrawGridLines(false);
        xl.setAvoidFirstLastClipping(true);
        xl.setEnabled(true);
        xl.setValueFormatter(new ValueFormatter() {

            @Override
            public String getFormattedValue(float value) {
                return Integer.toString((int) value * 5);
            }

        });

        YAxis leftAxis = chart.getAxisLeft();
        leftAxis.setDrawGridLines(true);

        YAxis rightAxis = chart.getAxisRight();
        rightAxis.setEnabled(false);
    }

    private void addEntry(LineChart chart, ApiResponse entry) {

        LineData data = chart.getData();

        if (data != null) {

            ILineDataSet set = data.getDataSetByIndex(0);

            if (set == null) {
                set = createSet();
                data.addDataSet(set);
            }

            data.addEntry(new Entry(set.getEntryCount(), Float.parseFloat(entry.getValue())), 0);
            data.notifyDataChanged();

            // let the chart know it's data has changed
            chart.notifyDataSetChanged();

            // limit the number of visible entries
            chart.setVisibleXRangeMaximum(10);
            // chart.setVisibleYRange(30, AxisDependency.LEFT);

            // move to the latest entry
            chart.moveViewToX(data.getEntryCount());
        }
    }

    private LineDataSet createSet() {

        LineDataSet set = new LineDataSet(null, "");
        set.setAxisDependency(YAxis.AxisDependency.LEFT);
        set.setColor(ColorTemplate.getHoloBlue());
        set.setCircleColor(RED);
        set.setLineWidth(2f);
        set.setCircleRadius(4f);
        set.setFillAlpha(65);
        set.setFillColor(ColorTemplate.getHoloBlue());
        set.setHighLightColor(rgb(244, 117, 117));
        set.setValueTextColor(WHITE);
        set.setValueTextSize(9f);
        set.setDrawValues(false);
        return set;
    }
}
