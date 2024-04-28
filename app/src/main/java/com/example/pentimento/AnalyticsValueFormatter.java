package com.example.pentimento;

import com.github.mikephil.charting.formatter.ValueFormatter;

import java.text.DecimalFormat;

class AnalyticsValueFormatter extends ValueFormatter {
    private DecimalFormat mFormat;
    private float total;


    public AnalyticsValueFormatter(float total) {
        this.total = total;

        // Use one decimal
        mFormat = new DecimalFormat("###,###,##0.0");
    }


    @Override
    // Format how data is displayed in the graph
    public String getFormattedValue(float value) {
        float percent = (value/total) * 100;
        return mFormat.format(value) + "\n (" + mFormat.format(percent) + "%)";
    }
}
