package com.example.bitcash;

import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    public static UnitAdapter adapter;
    public TextView totalValue;
    public static ArrayList<Unit> units;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        RecyclerView recyclerView = findViewById(R.id.unit_recycler_view);
        FloatingActionButton addUnitBtn = findViewById(R.id.add_unit_btn);
        EditText valueInput = findViewById(R.id.new_unit_value);
        EditText qntInput = findViewById(R.id.new_unit_qnt);
        totalValue = findViewById(R.id.total_value);
        recyclerView.setHasFixedSize(true);
        units = new ArrayList<>();
        units.add(new Unit(5, 10));
        units.add(new Unit(10, 20));
        units.add(new Unit(20, 0));
        units.add(new Unit(50, 0));
        units.add(new Unit(100, 0));
        units.add(new Unit(200, 0));
        units.add(new Unit(500, 0));
        units.add(new Unit(1000, 10));
        units.add(new Unit(2000, 0));
        adapter = new UnitAdapter(units);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        long totalCash = 0;
        for (Unit u : units) {
            totalCash += ((long) u.getUnitValue() * u.getUnitQnt());
        }
        totalValue.setText(new StringBuilder().append(" ").append(totalCash).append(" "));
        addUnitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UnitDialog dialog = new UnitDialog();
                dialog.show(getSupportFragmentManager(), "add dialog");
            }
        });
    }
}