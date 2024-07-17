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
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SearchFragment extends Fragment {
    private EditText editTextLotNum, editTextName;
    private Spinner spinnerCategory, spinnerPeriod;
    private ImageButton imageButton;
    private Button submitButton;
    private CheckBox reportCheckBox;
    private TextView errorMsg;
    FirebaseDatabase database = FirebaseDatabase.getInstance("https://login-taam-bo7-default-rtdb.firebaseio.com/");
    private DataSnapshot itemsSnap;
    private DatabaseReference itemsRef;

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

        // Error Msg
        errorMsg = view.findViewById(R.id.errorMsg);

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Do not generate report
                if (!reportCheckBox.isChecked())
                    searchItem();
                // generate report
                else{
                    // report code here
                }
            }
        });

        return view;
    }

    private void searchItem(){
        String lotNum = (editTextLotNum.getText().toString().trim()).toLowerCase();
        String name = (editTextName.getText().toString().trim()).toLowerCase();
        String category = (spinnerCategory.getSelectedItem().toString().trim()).toLowerCase();
        String period = (spinnerPeriod.getSelectedItem().toString().trim()).toLowerCase();

        if (lotNum.isEmpty() || name.isEmpty() || category.isEmpty() || period.isEmpty()) {
            Toast.makeText(getContext(), "Please fill out all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        itemsRef = database.getReference();
        itemsRef.child("Items").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot: dataSnapshot.getChildren()) {
                    Item item = snapshot.getValue(Item.class);

                    if (lotNum.equals(item.getLotNum().toLowerCase()) && name.equals(item.getName().toLowerCase()) && category.equals(item.getCategory().toLowerCase()) && period.equals(item.getPeriod().toLowerCase())) {
                        // perform result of search
                        // ...
                        // ...
                        errorMsg.setText("Successful Search");
                        return;
                    }

                }
                errorMsg.setText("Cannot find item");

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        }
}