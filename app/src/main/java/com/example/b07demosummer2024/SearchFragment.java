package com.example.b07demosummer2024;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.database.DataSnapshot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SearchFragment extends TAAMSFragment {

    private EditText editTextLotNum, editTextName;
    private Spinner spinnerCategory, spinnerPeriod;
    private Button submitButton;
    private CheckBox reportCheckBox;
    private TextView errorMsg;

    private final String blankOption = "Not selected";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_search, container, false);

        // EditTexts
        editTextLotNum = view.findViewById(R.id.editTextLotNumber);
        editTextName = view.findViewById(R.id.editTextName);

        // Buttons
        submitButton = view.findViewById(R.id.submitConfirm);

        // Spinners
        spinnerCategory = view.findViewById(R.id.categorySpinner);
        spinnerPeriod = view.findViewById(R.id.periodSpinner);

        initializeSpinner(R.array.categories_array, spinnerCategory);
        initializeSpinner(R.array.periods_array, spinnerPeriod);

        // CheckBox
        reportCheckBox = view.findViewById(R.id.reportConfirm);

        // Error Msg
        errorMsg = view.findViewById(R.id.errorMsg);

        submitButton.setOnClickListener(v -> {
            // Do not generate report
            if (!reportCheckBox.isChecked())
                searchItem();
            // generate report
            else {

            }
        });

        return view;
    }

    private void searchItem(){
        String lotNum = normalize(editTextLotNum.getText().toString());
        String name = normalize(editTextName.getText().toString());
        String category = normalize(spinnerCategory.getSelectedItem().toString());
        String period = normalize(spinnerPeriod.getSelectedItem().toString());

        if (isAllBlankInput(lotNum, name, category, period)) {
            Toast.makeText(getContext(), "Please fill in at least one field!", Toast.LENGTH_SHORT).show();
            return;
        }

        itemsRef = database.getReference();
        itemsRef.child("Items").get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DataSnapshot itemsList = task.getResult();
                for (DataSnapshot snapshot : itemsList.getChildren()) {
                    Item item = snapshot.getValue(Item.class);

                    // if the fields are blank, compare() should return true anyways
                    if (compare(lotNum, item.getLotNum()) && compare(name, item.getName()) && compare(category, item.getCategory()) && compare(period, item.getPeriod())) {
                        // perform result of search
                        // ...
                        // ...
                        errorMsg.setText("Successful Search");
                        return;
                    }
                }
                errorMsg.setText("Cannot find item");
            } else {
                // error handling code
            }
        });
    }

    private void initializeSpinner(int arrayId, Spinner spinner) {
        List<CharSequence> resList = new ArrayList<>(Arrays.asList(getResources().getStringArray(arrayId)));
        ArrayAdapter<CharSequence> adapter = new ArrayAdapter<>(getContext(),
                android.R.layout.simple_spinner_item, resList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapter.insert(blankOption, 0);
        spinner.setAdapter(adapter);
        spinner.setSelection(0);
    }

    private String normalize(String s) {
        return s.trim().toLowerCase();
    }

    private boolean compare(String input, String compareTo) {
        if (!isBlankInput(input)) {
            return input.equals(compareTo.toLowerCase());
        }
        return true; // if isBlankInput is true, then there is basically no input to check
    }

    private boolean isBlankInput(String s) {
        return s == null || s.isEmpty() || s.equals(blankOption.toLowerCase());
    }

    private boolean isAllBlankInput(String ... a) {
        for (String s : a) {
            if (!isBlankInput(s)) {return false;}
        }
        return true;
    }
}