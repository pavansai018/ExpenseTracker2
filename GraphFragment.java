package com.krishnachaitanya.expensetracker;


import android.graphics.Color;
import android.os.Bundle;
import android.app.Fragment;
import android.os.DropBoxManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 */
public class GraphFragment extends android.support.v4.app.Fragment {


    PieChart pieChart;
    ArrayList<Entry> entries;
    ArrayList<String> PieEntryLabels;
    PieDataSet pieDataSet;
    PieData pieData;

    private DatabaseReference ExpenseList;

    public GraphFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        // Inflate the layout for this fragment

        View rootView = inflater.inflate(R.layout.fragment_fragment_graphs, container, false);
        PieChart pieChart = (PieChart) rootView.findViewById(R.id.piechart);
        pieChart.setUsePercentValues(true);
        ArrayList<Entry> yvalues = new ArrayList<Entry>();

        ExpenseList = FirebaseDatabase.getInstance().getReference();


        yvalues.add(new Entry(8f, 0));
        yvalues.add(new Entry(15f, 1));
        yvalues.add(new Entry(12f, 2));
        yvalues.add(new Entry(25f, 3));
        yvalues.add(new Entry(23f, 4));
        yvalues.add(new Entry(17f, 5));

        PieDataSet dataSet = new PieDataSet(yvalues, "Election Results");
        ArrayList<String> xVals = new ArrayList<String>();

        xVals.add("Clothing");
        xVals.add("Entertainment");
        xVals.add("Food");
        xVals.add("Health");
        xVals.add("Fuel");
        xVals.add("Stationary");

        PieData data = new PieData(xVals, dataSet);
        data.setValueFormatter(new PercentFormatter());
        pieChart.setData(data);
        dataSet.setColors(ColorTemplate.VORDIPLOM_COLORS);
        pieChart.setDescription("This is Pie Chart");
        pieChart.setDrawHoleEnabled(true);
        pieChart.setTransparentCircleRadius(30f);
        pieChart.setHoleRadius(30f);
        data.setValueTextSize(13f);
        data.setValueTextColor(Color.DKGRAY);


        return rootView;
    }

}
