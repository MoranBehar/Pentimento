package com.example.pentimento;

import android.graphics.Color;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

public class AnalyticsFragment extends Fragment {

    private View view;
    private DBManager dbManager;
    private CountDownLatch latchLoadData;
    private Long totalPhotoViews, totalAlbumViews, totalSecretViews, totalSecretAdded;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_analytics, container, false);
        initAnalytics(view);

        return view;
    }

    private void initAnalytics(View view) {
        dbManager = DBManager.getInstance();

        // setup latch to count 4 actions
        latchLoadData = new CountDownLatch(4);

        // load the data for the pie chart
        loadPhotoViews();
        loadAlbumViews();
        loadSecretViews();
        loadSecretAdded();

        // Wait till all the load actions finises before showing the chart
        // we run this on a dedicated thread so the UI thread will not be blocked
        new Thread(() -> {
            try {
                latchLoadData.await();
                // switch back to UI thread to update UI
                getActivity().runOnUiThread(() -> setupPieChart());
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }).start();


    }

    // Load the album views data
    private void loadAlbumViews() {
        dbManager.getLogCount(7, 2, new DBManager.DBActionResult<Long>() {
            @Override
            public void onSuccess(Long data) {
                totalAlbumViews = data;
                latchLoadData.countDown();  // Decrease latch count
            }

            @Override
            public void onError(Exception e) {
                // If error occurs we also need to decrease the latch
                latchLoadData.countDown();
            }
        });
    }

    private void loadPhotoViews() {
        dbManager.getLogCount(7, 1, new DBManager.DBActionResult<Long>() {
            @Override
            public void onSuccess(Long data) {
                totalPhotoViews = data;
                latchLoadData.countDown();  // Decrease count
            }

            @Override
            public void onError(Exception e) {
                latchLoadData.countDown();
            }
        });
    }

    private void loadSecretViews() {
        dbManager.getLogCount(7, 3, new DBManager.DBActionResult<Long>() {
            @Override
            public void onSuccess(Long data) {
                totalSecretViews = data;
                latchLoadData.countDown();  // Decrease count
            }

            @Override
            public void onError(Exception e) {
                latchLoadData.countDown();
            }
        });
    }

    private void loadSecretAdded() {
        dbManager.getLogCount(7, 4, new DBManager.DBActionResult<Long>() {
            @Override
            public void onSuccess(Long data) {
                totalSecretAdded = data;
                latchLoadData.countDown();  // Decrease count
            }

            @Override
            public void onError(Exception e) {
                latchLoadData.countDown();
            }
        });
    }

//    private void loadTopViews() {
//
//        List<Map.Entry<String, Integer>> topViews = new ArrayList<>();
//
//        dbManager.getTopPhotoViewed(5, 7, new DBManager.DBActionResult<List<Map.Entry<String, Integer>>>() {
//            @Override
//            public void onSuccess(List data) {
//                topViews.addAll(data);
//                setupBarChart(view, topViews);
//            }
//
//            @Override
//            public void onError(Exception e) {
//
//            }
//        });
//
//    }

//    public void setupBarChart(View view, List<Map.Entry<String, Integer>> photoViews) {
//        BarChart chart = view.findViewById(R.id.barChart);
//        List<BarEntry> entries = new ArrayList<>();
//        List<String> id1s = new ArrayList<>();
//
//        List<Integer> colors = new ArrayList<>();
//        int color1 = ContextCompat.getColor(view.getContext(), R.color.primary_foreground);
//        colors.add(color1);
//
//        int index = 0;
//        for (Map.Entry<String, Integer> entry : photoViews) {
//            entries.add(new BarEntry(index, entry.getValue()));
//            id1s.add(entry.getKey());
//            index++;
//        }
//
//        // Set colors
//        BarDataSet dataSet = new BarDataSet(entries, "Views");
//        dataSet.setColors(colors);
//
//        // Set the bar chart
//        BarData barData = new BarData(dataSet);
//        chart.setData(barData);
//        chart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(id1s));
//        chart.getDescription().setEnabled(false);
//        chart.invalidate(); // refresh
//    }

    public void setupPieChart() {

        PieChart pieChart = view.findViewById(R.id.pieChart);

        List<Integer> colors = new ArrayList<>();
        int color1 = ContextCompat.getColor(view.getContext(), R.color.pie_1);
        int color2 = ContextCompat.getColor(view.getContext(), R.color.pie_2);
        int color3 = ContextCompat.getColor(view.getContext(), R.color.pie_3);
        int color4 = ContextCompat.getColor(view.getContext(), R.color.pie_4);

        colors.add(color1);
        colors.add(color2);
        colors.add(color3);
        colors.add(color4);

        // Creating a list of PieEntries
        ArrayList<PieEntry> entries = new ArrayList<>();
        entries.add(new PieEntry(totalPhotoViews.floatValue(), "Photo Views"));
        entries.add(new PieEntry(totalAlbumViews.floatValue(), "Album Views"));
        entries.add(new PieEntry(totalSecretAdded.floatValue(), "Secrets Added"));
        entries.add(new PieEntry(totalSecretViews.floatValue(), "Secret Views"));

        float total =
                totalPhotoViews.floatValue() +
                totalAlbumViews.floatValue() +
                totalSecretAdded.floatValue() +
                totalSecretViews.floatValue();

        // Creating a DataSet
        PieDataSet dataSet = new PieDataSet(entries, "Categories");

        // Set chart colors
        dataSet.setColors(colors);

        // Set format of data displayed ( value + percentage)
        dataSet.setValueFormatter(new AnalyticsValueFormatter(total));
        dataSet.setValueTextSize(14f); // Set the text size for values
        dataSet.setValueTextColor(Color.WHITE); // Set the text color for values

        // Creating a PieData object with the DataSet
        PieData pieData = new PieData(dataSet);

        customizeLegend(pieChart);

        // Setting the PieData UI
        pieChart.setData(pieData);
        pieChart.setEntryLabelTextSize(12f);
        pieChart.setCenterText("Category");
        pieChart.setCenterTextSize(14f);
        pieChart.invalidate(); // Refresh the chart


    }

    public void customizeLegend(PieChart pieChart) {
        Legend legend = pieChart.getLegend();
        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
        legend.setOrientation(Legend.LegendOrientation.VERTICAL);
        legend.setDrawInside(false);
        legend.setTextSize(16f);
        legend.setEnabled(true);
    }

}