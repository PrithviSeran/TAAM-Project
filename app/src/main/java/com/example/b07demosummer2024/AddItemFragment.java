package com.example.b07demosummer2024;

import static android.content.ContentValues.TAG;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import java.util.HashMap;
import java.util.Map;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class AddItemFragment extends Fragment {

    private EditText editTextLotNum, editTextName, editTextDescription, editTextPic;
    private Spinner spinnerCategory, spinnerPeriod;
    private Button buttonAdd;
    //FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseDatabase database = FirebaseDatabase.getInstance("https://login-taam-bo7-default-rtdb.firebaseio.com/");
    private DatabaseReference itemsRef;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_add_item, container, false);


        editTextLotNum = view.findViewById(R.id.editTextText1);
        editTextName = view.findViewById(R.id.editTextText2);
        spinnerCategory = view.findViewById(R.id.spinner);
        spinnerPeriod= view.findViewById(R.id.spinner2);
        editTextDescription = view.findViewById(R.id.editTextText3);
        editTextPic = view.findViewById(R.id.editTextText4);
        buttonAdd = view.findViewById(R.id.addItemButton);


        // Set up the spinner with categories
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),
                R.array.categories_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategory.setAdapter(adapter);

        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(getContext(),
                R.array.periods_array, android.R.layout.simple_spinner_item);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerPeriod.setAdapter(adapter2);


        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addItem();
            }
        });

        return view;
    }

    private void addItem() {
        String lotNum = editTextLotNum.getText().toString().trim();
        String name = editTextName.getText().toString().trim();
        String category = spinnerCategory.getSelectedItem().toString().trim();
        String period = spinnerCategory.getSelectedItem().toString().trim();
        String description = editTextDescription.getText().toString().trim();
        String pic = editTextPic.getText().toString().trim();

        if (lotNum.isEmpty() || name.isEmpty() || category.isEmpty() || period.isEmpty() || description.isEmpty() || pic.isEmpty()) {
            Toast.makeText(getContext(), "Please fill out all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        itemsRef = database.getReference("categories/" + category);
        String id = itemsRef.push().getKey();
        Item item = new Item(lotNum, name, category, period, description, pic);

        itemsRef.child(id).setValue(item).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(getContext(), "Item added", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getContext(), "Failed to add item", Toast.LENGTH_SHORT).show();
            }

        });



    }
}
