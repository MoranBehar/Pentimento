package com.example.pentimento;

import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class AnalyticsFragment extends Fragment {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_analytics, container, false);

        initAnalytics(view);

        return view;
    }

    private void initAnalytics(View view) {
        Map<String, Integer> photoViews = getPhotoViews();
        setupBarChart(view, photoViews);
    }

    private Map<String, Integer> getPhotoViews() {

        // Temp data
        // TODO - Read from Database
        Map<String, Integer> photoViews = new HashMap<>();
        photoViews.put("photo_1", 160);
        photoViews.put("photo_2", 696);
        photoViews.put("photo_3", 106);
        photoViews.put("photo_4", 25);
        photoViews.put("photo_5", 206);
        photoViews.put("photo_6", 423);
        photoViews.put("photo_7", 265);
        photoViews.put("photo_8", 655);
        photoViews.put("photo_9", 333);
        photoViews.put("photo_10", 429);

        return photoViews;
    }

    public void setupBarChart(View view, Map<String, Integer> photoViews) {
        BarChart chart = view.findViewById(R.id.barChart);
        List<BarEntry> entries = new ArrayList<>();
        List<String> id1s = new ArrayList<>();

        List<Integer> colors = new ArrayList<>();
        int color1 = ContextCompat.getColor(view.getContext(), R.color.primary_foreground);
        colors.add(color1);

        int index = 0;
        for (Map.Entry<String, Integer> entry : photoViews.entrySet()) {
            entries.add(new BarEntry(index, entry.getValue()));
            id1s.add(entry.getKey());
            index++;
        }

        // Set colors
        BarDataSet dataSet = new BarDataSet(entries, "Views");
        dataSet.setColors(colors);

        // Set the bar chart
        BarData barData = new BarData(dataSet);
        chart.setData(barData);
        chart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(id1s));
        chart.getDescription().setEnabled(false);
        chart.invalidate(); // refresh
    }

}