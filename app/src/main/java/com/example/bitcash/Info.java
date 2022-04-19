package com.example.bitcash;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import android.annotation.SuppressLint;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;
import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import java.util.ArrayList;
import java.util.List;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.charts.Pie;
import com.example.bitcash.UnitContract.*;
import maes.tech.intentanim.CustomIntent;

public class Info extends AppCompatActivity {
    //variables declaration
    AnyChartView stat;
    ArrayList<String> values = new ArrayList<>();
    ArrayList<Integer> quantities = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //making the activity go full screen
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //attaching layout to java class
        setContentView(R.layout.activity_info);
        //referring to views
        stat = findViewById(R.id.statistics);
        stat.setZoomEnabled(true);
        try{
            databaseToArray();
        } catch (Exception e){
            Toast.makeText(this, "No info to show", Toast.LENGTH_SHORT).show();
            finish();
        }
        setupChart();
    }
    //this method is called to initialise the chart with the data entries
    private void setupChart() {
        Pie pie = AnyChart.pie();
        List<DataEntry> dataEntries = new ArrayList<>();
        for(int i = 0; i < values.size(); i++){
            dataEntries.add(new ValueDataEntry(values.get(i), quantities.get(i)));
        }
        pie.data(dataEntries);
        pie.title("Cash Register - Composition");
        stat.setChart(pie);
    }
    //this method is called to query the database and save results in arrays to feed the chart
    @SuppressLint("Range")
    private void databaseToArray(){
        String valuesQuery = "SELECT " + UnitEntry.COLUMN_VALUE + " AS VL FROM " + UnitEntry.TABLE_NAME + ";";
        String qntsQuery = "SELECT " + UnitEntry.COLUMN_QUANTITY + " AS QT FROM " + UnitEntry.TABLE_NAME + ";";
        Cursor cv = MainActivity.cashDatabase.rawQuery(valuesQuery, null);
        cv.moveToFirst();
        Cursor cq = MainActivity.cashDatabase.rawQuery(qntsQuery, null);
        cq.moveToFirst();
        do {
            values.add(cv.getInt(cv.getColumnIndex("VL"))+" DA");
            quantities.add(cq.getInt(cq.getColumnIndex("QT")));
        }
        while (cv.moveToNext() && cq.moveToNext());
        cv.close();
        cq.close();
    }
    @Override
    public void finish() {
        super.finish();
        CustomIntent.customType(Info.this, "bottom-to-up");
    }
}