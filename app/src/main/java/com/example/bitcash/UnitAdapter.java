package com.example.bitcash;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

public class UnitAdapter extends RecyclerView.Adapter<UnitAdapter.UnitViewHolder>{
    private ArrayList<Unit> unites;
    public static class UnitViewHolder extends RecyclerView.ViewHolder{
        public TextView unitValue, unitQnt;
        public Button plusBtn, minusBtn;
        public UnitViewHolder(@NonNull View itemView) {
            super(itemView);
            unitValue = itemView.findViewById(R.id.unit_value);
            unitQnt = itemView.findViewById(R.id.unit_qnt);
            plusBtn = itemView.findViewById(R.id.control_btn_plus);
            minusBtn = itemView.findViewById(R.id.control_btn_minus);
        }
    }
    public UnitAdapter(ArrayList<Unit> unites){this.unites = unites;}
    @NonNull
    @Override
    public UnitViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.unit_exmpl, parent, false);
        return new UnitViewHolder(v);
    }
    @Override
    public void onBindViewHolder(@NonNull UnitViewHolder holder, int position) {
        Unit currentUnit = unites.get(position);
        holder.unitValue.setText(String.valueOf(currentUnit.getUnitValue()));
        holder.unitQnt.setText(String.valueOf(currentUnit.getUnitQnt()));
        holder.plusBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currentUnit.setUnitQnt(currentUnit.getUnitQnt()+1);
                MainActivity.adapter.notifyItemChanged(holder.getAdapterPosition());
            }
        });
        holder.plusBtn.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                currentUnit.setUnitQnt(currentUnit.getUnitQnt()+10);
                MainActivity.adapter.notifyItemChanged(holder.getAdapterPosition());
                return true;
            }
        });
        holder.minusBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(currentUnit.getUnitQnt() != 0){
                    currentUnit.setUnitQnt(currentUnit.getUnitQnt()-1);
                    MainActivity.adapter.notifyItemChanged(holder.getAdapterPosition());
                }
            }
        });
        holder.minusBtn.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                if(currentUnit.getUnitQnt() >= 10){
                currentUnit.setUnitQnt(currentUnit.getUnitQnt()-10);
                MainActivity.adapter.notifyItemChanged(holder.getAdapterPosition());
            }
                return true;
            }
        });
    }
    @Override
    public int getItemCount() {
        return unites.size();
    }
}

