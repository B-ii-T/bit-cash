package com.example.bitcash;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.ArrayList;
import com.example.bitcash.UnitContract.*;

public class MainActivity extends AppCompatActivity implements UnitDialog.UnitDialogListener {
    //variables declaration
    static SQLiteDatabase cashDatabase;
    private UnitAdapter adapter;
    public static final String order = " DESC";
    @Override
    //this method is called to create the activity
    protected void onCreate(Bundle savedInstanceState) {
        //making the activity go full screen
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //attaching layout to java class
        setContentView(R.layout.activity_main);
        //initiating variables
        UnitDBH dbh = new UnitDBH(this);
        cashDatabase = dbh.getWritableDatabase();
        adapter = new UnitAdapter(this, getAllUnits());
        //referring to views
        RecyclerView recyclerView = findViewById(R.id.unit_recycler_view);
        FloatingActionButton addUnitBtn = findViewById(R.id.add_unit_btn);
        TextView totalValue = findViewById(R.id.total_value);
        //informing that the recycle view has a fixed size of elements
        recyclerView.setHasFixedSize(true);
        //attaching adapter to recycler view
        recyclerView.setAdapter(adapter);
        //attaching a linear layout manager to recycler view
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        //creating an item touch helper to implement swipe to delete item
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT | ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }
            //this method is called when we swipe the item
            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                removeUnit((long) viewHolder.itemView.getTag());
            }
        }).attachToRecyclerView(recyclerView);
        //calculating total
        int numRows = (int) DatabaseUtils.queryNumEntries(cashDatabase, UnitEntry.TABLE_NAME);
        //setting the total text view
        totalValue.setText(String.valueOf(numRows));
        //add a new unit
        addUnitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //showing the add unit dialog
                UnitDialog dialog = new UnitDialog();
                dialog.show(getSupportFragmentManager(), "add dialog");
            }
        });
    }
    //this method is called to get data from the dialog and save it to the database units table
    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void passValues(int value, int quantity) {
        //creating a content values variable to hold the entries
        ContentValues cv = new ContentValues();
        cv.put(UnitEntry.COLUMN_VALUE, value);
        cv.put(UnitEntry.COLUMN_QUANTITY, quantity);
        //checking if the database contains the entry to prevent duplication
        Cursor c = cashDatabase.rawQuery("SELECT * FROM " + UnitEntry.TABLE_NAME + " WHERE " + UnitEntry.COLUMN_VALUE + " = "+ value, null);
        if(c.moveToFirst()){
            Toast.makeText(this, "duplicated - already exists", Toast.LENGTH_SHORT).show();
            c.close();
        }else{
            //inserting into the database unites table
            cashDatabase.insert(UnitEntry.TABLE_NAME, null, cv);
            //passing the new cursor
            adapter.swapCursor(getAllUnits());
            //updating the recycler view to display the new element
            adapter.notifyDataSetChanged();
        }
    }
    //this method is called to get all units from the database
    public static Cursor getAllUnits(){
        return cashDatabase.query(
                UnitEntry.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                UnitEntry.COLUMN_TIMESTAMP + order
        );
    }
    //this method is called to delete a unit from the database
    private void removeUnit(long id) {
        cashDatabase.delete(UnitEntry.TABLE_NAME,
                UnitEntry._ID + " = " + id, null);
        adapter.swapCursor(getAllUnits());
    }
}