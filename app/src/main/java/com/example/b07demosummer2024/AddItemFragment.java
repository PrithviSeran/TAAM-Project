package com.example.b07demosummer2024;

import static android.app.Activity.RESULT_OK;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.net.URI;

public class AddItemFragment extends TAAMSFragment {

    private EditText editTextLotNum, editTextName, editTextDescription;
    private Spinner spinnerCategory, spinnerPeriod;
    private Button buttonAdd;
    private ImageButton addImageButton;
    private DatabaseReference itemsRef;
    private Intent intent;
    private ImageView imageView;
    private Uri image;
    private String lotNum, name, category, period, description;



    private final ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult result) {
            if (result.getResultCode() == RESULT_OK) {
                if (result.getData() != null) {
                    addImageButton.setEnabled(true);
                    image = result.getData().getData();
                    imageView.setImageURI(image);

                }
            } else {
                Toast.makeText(getContext(), "Please select an image", Toast.LENGTH_SHORT).show();
            }
        }
    });

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_add_item, container, false);

        editTextLotNum = view.findViewById(R.id.editTextText1);
        editTextName = view.findViewById(R.id.editTextText2);
        spinnerCategory = view.findViewById(R.id.spinner);
        spinnerPeriod= view.findViewById(R.id.spinner2);
        editTextDescription = view.findViewById(R.id.editTextText3);
        addImageButton = view.findViewById(R.id.imagesearch);
        buttonAdd = view.findViewById(R.id.addItemButton);
        imageView = view.findViewById(R.id.imageView2);


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
                uploadImage(image);
            }
        });


        addImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                activityResultLauncher.launch(intent);

            }
        });

        return view;
    }

    private void addItem() {
        lotNum = editTextLotNum.getText().toString().trim();
        name = editTextName.getText().toString().trim();
        category = spinnerCategory.getSelectedItem().toString().trim();
        period = spinnerPeriod.getSelectedItem().toString().trim();
        description = editTextDescription.getText().toString().trim();

        if (lotNum.isEmpty() || name.isEmpty() || category.isEmpty() || period.isEmpty() || description.isEmpty()) {
            Toast.makeText(getContext(), "Please fill out all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        itemsRef = database.getReference("Items");

        Item item = new Item(lotNum, name, category, period, description);


        itemsRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                if (!(task.getResult().child(lotNum).exists())) {
                    Item item = new Item(lotNum, name, category, period, description, pic);

                    itemsRef.child(lotNum).setValue(item).addOnCompleteListener(addTask -> {
                        if (addTask.isSuccessful()) {
                            Toast.makeText(getContext(), "Item added", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getContext(), "Failed to add item", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                else {
                    Toast.makeText(getContext(), "Item with Lot# already exists!", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(getContext(), "Failed to access database", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void uploadImage(Uri image){
        storageRef = storageReference.child(name);
        storageRef.putFile(image);
    }

}