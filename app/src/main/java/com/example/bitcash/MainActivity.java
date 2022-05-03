package com.example.bitcash;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.Spannable;
import android.util.AttributeSet;
import android.view.ContextThemeWrapper;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.example.bitcash.UnitContract.UnitEntry;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import maes.tech.intentanim.CustomIntent;

public class MainActivity extends AppCompatActivity implements UnitDialog.UnitDialogListener, PopupMenu.OnMenuItemClickListener {
    //variables declaration
    static SQLiteDatabase cashDatabase;
    private UnitAdapter adapter;
    public static String orderType = " DESC";
    public static String orderString = UnitEntry.COLUMN_TIMESTAMP;
    @SuppressLint("StaticFieldLeak")
    public static TextView totalValue;
    LottieAnimationView emptyText;
    @SuppressLint("StaticFieldLeak")
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
        totalValue = findViewById(R.id.total_value);
        emptyText = findViewById(R.id.empty_text);
        calculateTotal();
        //informing that the recycle view has a fixed size of elements
        recyclerView.setHasFixedSize(true);
        //attaching adapter to recycler view
        recyclerView.setAdapter(adapter);
        //attaching a linear layout manager to recycler view
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        handleEmptyAdapter();
        //creating an item touch helper to implement swipe to delete item
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT | ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }
            //this method is called when we swipe the item
            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                //alert dialog to confirm the delete and avoid accidents
                AlertDialog.Builder deleteUnit = new AlertDialog.Builder(MainActivity.this, R.style.new_unit_dialog);
                deleteUnit.setTitle("This unit will be deleted");
                deleteUnit.setIcon(R.drawable.ic_delete_gold);
                deleteUnit.setCancelable(false);
                deleteUnit.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //calling the method to delete a unit
                        removeUnit((long) viewHolder.itemView.getTag());
                    }
                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //refreshing the adapter
                        adapter.swapCursor(getAllUnits());
                    }
                });
                AlertDialog deleteUnitDialog = deleteUnit.create();
                deleteUnitDialog.show();
                deleteUnitDialog.getButton(deleteUnitDialog.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.light_gray));
            }
        }).attachToRecyclerView(recyclerView);
        //add a new unit
        addUnitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //showing the add unit dialog
                UnitDialog dialog = new UnitDialog();
                dialog.show(getSupportFragmentManager(), "add dialog");
            }
        });
        //deleting all units from the database
        ImageView deleteBtn = findViewById(R.id.delete_btn);
        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(getAllUnits().moveToFirst()){
                    //alert dialog to confirm the delete and avoid accidents
                    AlertDialog.Builder deleteUnits = new AlertDialog.Builder(MainActivity.this, R.style.new_unit_dialog);
                    deleteUnits.setTitle("All units will be deleted");
                    deleteUnits.setIcon(R.drawable.ic_delete_gold);
                    deleteUnits.setCancelable(true);
                    deleteUnits.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //calling the method to delete all units
                            deleteAllUnits();
                            calculateTotal();
                        }
                    });
                    AlertDialog deleteUnitsDialog = deleteUnits.create();
                    deleteUnitsDialog.getWindow().getAttributes().windowAnimations = R.style.new_unit_dialog; //style id
                    deleteUnitsDialog.show();
                }else{
                    Toast.makeText(MainActivity.this, "No units to delete", Toast.LENGTH_SHORT).show();
                }
            }
        });
        //go to settings
        ImageView settingsBtn = findViewById(R.id.settings_btn);
        settingsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent settingsIntent = new Intent(MainActivity.this, Settings.class);
//                startActivity(settingsIntent);
//                CustomIntent.customType(MainActivity.this, "up-to-bottom");
                Toast.makeText(MainActivity.this, "No settings yet", Toast.LENGTH_SHORT).show();
            }
        });
        //go to info
        ImageView infoBtn = findViewById(R.id.info_btn);
        infoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent infoIntent = new Intent(MainActivity.this, Info.class);
                startActivity(infoIntent);
                CustomIntent.customType(MainActivity.this, "up-to-bottom");
            }
        });
        //change sort
        ImageView sortBtn = findViewById(R.id.sort_btn);
        sortBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPopup(sortBtn);
            }
        });
    }
    //this method is called to get data from the dialog and save it to the database units table
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
            //passing the new cursor to display all elements
            adapter.swapCursor(getAllUnits());
            handleEmptyAdapter();
            //creating and playing sound effect
            final MediaPlayer mp = MediaPlayer.create(this, R.raw.cash_register);
            mp.start();
            //calculating total value
            calculateTotal();
        }
    }
    //this method is called to get all units from the database
    public static Cursor getAllUnits(){
        Cursor c = cashDatabase.query(
                UnitEntry.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                orderString + orderType
        );
        return c;
    }
    //this method is called to delete a unit from the database
    private void removeUnit(long id) {
        cashDatabase.delete(UnitEntry.TABLE_NAME,
                UnitEntry._ID + " = " + id, null);
        adapter.swapCursor(getAllUnits());
        handleEmptyAdapter();
        calculateTotal();
    }
    //this method is called to calculate the total value
    @SuppressLint("Range")
    public static void calculateTotal(){
        //calculating total
        String total_query = "SELECT SUM( " + UnitEntry.COLUMN_VALUE + " * " + UnitEntry.COLUMN_QUANTITY + " ) AS Total FROM " + UnitEntry.TABLE_NAME + ";";
        Cursor c = cashDatabase.rawQuery(total_query ,null);
        //setting the total text view
        if(c.moveToFirst()){
            int total = c.getInt(c.getColumnIndex("Total"));
            totalValue.setText(String.valueOf(total));
        }
        c.close();
    }
    public void showPopup(View v){
        ContextThemeWrapper ctw = new ContextThemeWrapper(this, R.style.popUpMenu);
        PopupMenu popupMenu = new PopupMenu(ctw, v);
        popupMenu.setOnMenuItemClickListener(this);
        popupMenu.inflate(R.menu.sort_menu);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            popupMenu.setForceShowIcon(true);
        }
        popupMenu.show();
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onMenuItemClick(MenuItem menuItem) {
        switch (menuItem.getItemId()){
            case R.id.time_desc : orderString = UnitEntry.COLUMN_TIMESTAMP; orderType = " DESC"; adapter.swapCursor(getAllUnits());
                return true;
            case R.id.time_asc : orderString = UnitEntry.COLUMN_TIMESTAMP; orderType = " ASC"; adapter.swapCursor(getAllUnits());
                return true;
            case R.id.value_desc : orderString = UnitEntry.COLUMN_VALUE; orderType = " DESC"; adapter.swapCursor(getAllUnits());
                return true;
            case R.id.value_asc : orderString = UnitEntry.COLUMN_VALUE; orderType = " ASC"; adapter.swapCursor(getAllUnits());
                return true;
            case R.id.qnt_desc : orderString = UnitEntry.COLUMN_QUANTITY; orderType = " DESC"; adapter.swapCursor(getAllUnits());
                return true;
            case R.id.qnt_asc : orderString = UnitEntry.COLUMN_QUANTITY; orderType = " ASC"; adapter.swapCursor(getAllUnits());
                return true;
            default : return false;
        }
    }
    public void deleteAllUnits(){
        cashDatabase.execSQL("DELETE FROM " + UnitEntry.TABLE_NAME);
        adapter.swapCursor(getAllUnits());
        handleEmptyAdapter();
    }

        public void handleEmptyAdapter(){
        //handling empty recycler view
        Cursor c = cashDatabase.rawQuery("SELECT * FROM " + UnitEntry.TABLE_NAME, null);
        if(c.moveToFirst()){
            emptyText.setVisibility(View.GONE);
        }else{
            emptyText.setVisibility(View.VISIBLE);
        }
        c.close();
    }
}