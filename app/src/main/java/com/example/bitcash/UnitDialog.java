package com.example.bitcash;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentValues;
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
import com.example.bitcash.UnitContract.*;

public class UnitDialog extends AppCompatDialogFragment {
    //variables declaration
    private String unitValue, unitQnt;
    private UnitDialogListener listener;
    //this method is called ti create an alertDialog
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        //inflating view into alertDialog
        View view = inflater.inflate(R.layout.new_input, null);
        //referring to inputs
        EditText valueInput = view.findViewById(R.id.new_unit_value);
        EditText qntInput = view.findViewById(R.id.new_unit_qnt);
        //customising dialog
        builder.setView(view)
                .setTitle("add new unit")
                .setPositiveButton("add", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //surrounding by try catch to avoid a run time error from a long number input
                        try {
                            //checking if inputs are empty
                            unitValue = valueInput.getText().toString().trim();
                            unitQnt = qntInput.getText().toString().trim();
                            if(unitValue.equals("") || unitQnt.equals("")){
                                Toast.makeText(getActivity(), "Enter value and quantity", Toast.LENGTH_SHORT).show();
                            }else{
                                if(Integer.parseInt(unitValue) == 0 || Integer.parseInt(unitQnt) == 0){
                                    Toast.makeText(getActivity(), "value and quantity can't be 0", Toast.LENGTH_SHORT).show();
                                }else{
                                    //passing data to the main activity
                                    int value = Integer.parseInt(unitValue);
                                    int qnt = Integer.parseInt(unitQnt);
                                    listener.passValues(value, qnt);
                                }
                            }
                        } catch (Exception e) {
                            Toast.makeText(getActivity(), "something went wrong :(", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
        return builder.create();
    }
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            listener = (UnitDialogListener) context;
        } catch (ClassCastException e) {
            e.printStackTrace();
        }
    }
    //creating an interface to help pass data to main activity
    public interface UnitDialogListener{
        void passValues(int value, int quantity);
    }
}
