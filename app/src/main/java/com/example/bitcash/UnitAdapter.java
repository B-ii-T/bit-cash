package com.example.bitcash;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import com.example.bitcash.UnitContract.*;

public class UnitAdapter extends RecyclerView.Adapter<UnitAdapter.UnitViewHolder>{
    //variables declaration
    private Context context;
    private Cursor cursor;
    //inner class
    public static class UnitViewHolder extends RecyclerView.ViewHolder{
        //variables declaration
        public TextView unitValue, unitQnt;
        public Button plusBtn, minusBtn;
        //constructor
        public UnitViewHolder(@NonNull View itemView) {
            super(itemView);
            //referring to views
            unitValue = itemView.findViewById(R.id.unit_value);
            unitQnt = itemView.findViewById(R.id.unit_qnt);
            plusBtn = itemView.findViewById(R.id.control_btn_plus);
            minusBtn = itemView.findViewById(R.id.control_btn_minus);
        }
    }
    //constructor
    public UnitAdapter(Context context, Cursor cursor){
        this.context = context;
        this.cursor = cursor;
    }
    //this method is called when the view holder is created
    @NonNull
    @Override
    public UnitViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.unit_exmpl, parent, false);
        return new UnitViewHolder(v);
    }
    //this method is called to bind data to the view holder to make sure it is displayed
    @Override
    public void onBindViewHolder(@NonNull UnitViewHolder holder, int position) {
        //checking that there is a positing to move to
        if(!cursor.moveToPosition(position)){return;}
        //using the cursor to read from the database
        @SuppressLint("Range") int vl = cursor.getInt(cursor.getColumnIndex(UnitEntry.COLUMN_VALUE));
        @SuppressLint("Range") int qnt = cursor.getInt(cursor.getColumnIndex(UnitEntry.COLUMN_QUANTITY));
        @SuppressLint("Range") long id = cursor.getLong(cursor.getColumnIndex(UnitEntry._ID));
        //setting text views to display the values from the database
        holder.unitValue.setText(String.valueOf(vl));
        holder.unitQnt.setText(String.valueOf(qnt));
        //using the tag to identify the item with the assigned id in the database
        holder.itemView.setTag(id);
        //method to increase the quantity for a value by 1
        holder.plusBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });
        //method to increase the quantity for a value by 10
        holder.plusBtn.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                return true;
            }
        });
        //method to decrease the quantity for a value by 1
        holder.minusBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });
        //method to decrease the quantity for a value by 10
        holder.minusBtn.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                return true;
            }
        });
    }
    //this method is called to get the count of the elements in the unit adapter
    @Override
    public int getItemCount() {
        return cursor.getCount();
    }
    //this method is called to pass a new cursor in order to update the database
    @SuppressLint("NotifyDataSetChanged")
    public void swapCursor(Cursor newCursor){
        if(cursor != null){
            cursor.close();
        }
        cursor = newCursor;
        if(newCursor != null){
            notifyDataSetChanged();
        }
    }
}

