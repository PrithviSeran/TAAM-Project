package com.example.b07demosummer2024;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.CheckBox;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.database.FirebaseDatabase;

public class SearchFragment extends Fragment {
    private EditText editTextLotNum, editTextName;
    private Spinner spinnerCategory, spinnerPeriod;
    private ImageButton imageButton;
    private Button submitButton;
    private CheckBox reportCheckBox;
    FirebaseDatabase database = FirebaseDatabase.getInstance("https://login-taam-bo7-default-rtdb.firebaseio.com/");

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_search, container, false);

        // EditTexts
        editTextLotNum = view.findViewById(R.id.editTextText);
        editTextName = view.findViewById(R.id.editTextText2);

        // Buttons
        imageButton = view.findViewById(R.id.imageButton);
        submitButton = view.findViewById(R.id.button);

        // Spinners
        spinnerCategory = view.findViewById(R.id.spinner);
        spinnerPeriod = view.findViewById(R.id.spinner2);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),
                R.array.categories_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategory.setAdapter(adapter);

        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(getContext(),
                R.array.periods_array, android.R.layout.simple_spinner_item);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerPeriod.setAdapter(adapter2);

        // CheckBox
        reportCheckBox = view.findViewById(R.id.checkBox);

        return view;
    }

    private void searchItem(){
        String lotNum = editTextLotNum.getText().toString().trim();
        String name = editTextName.getText().toString().trim();
        String category = spinnerCategory.getSelectedItem().toString().trim();
        String period = spinnerPeriod.getSelectedItem().toString().trim();

    }
}