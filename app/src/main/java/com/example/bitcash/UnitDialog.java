package com.example.bitcash;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;

public class UnitDialog extends AppCompatDialogFragment {
    String unitValue, unitQnt;
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.new_input, null);
        EditText valueInput = view.findViewById(R.id.new_unit_value);
        EditText qntInput = view.findViewById(R.id.new_unit_qnt);
        builder.setView(view)
                .setTitle("add new unit")
                .setPositiveButton("add", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        unitValue = valueInput.getText().toString().trim();
                        unitQnt = qntInput.getText().toString().trim();
                        if(unitValue.equals("") || unitQnt.equals("")){
                            Toast.makeText(getActivity(), "Enter value and quantity", Toast.LENGTH_SHORT).show();
                        }else{
                            MainActivity.units.add(new Unit(Integer.parseInt(unitValue), Integer.parseInt(unitQnt)));
                            MainActivity.adapter.notifyDataSetChanged();
                        }
                    }
                })
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dismiss();
                    }
                });
        return builder.create();
    }
}
