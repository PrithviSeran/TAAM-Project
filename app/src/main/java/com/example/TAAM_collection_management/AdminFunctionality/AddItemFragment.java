/*
 * AddItemFragment.java     1.0     2024/08/04
 */

package com.example.TAAM_collection_management.AdminFunctionality;

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
import android.widget.VideoView;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.TAAM_collection_management.Objects.Item;
import com.example.TAAM_collection_management.Objects.TAAMSFragment;
import com.example.b07demosummer2024.R;
import com.google.firebase.database.DatabaseReference;

/**
 * Class used to display <code>fragment_add_item.xml</code>, and
 * compiling proper information for the view.
 * <p>
 * AddItemFragment creates a view for the xml file and allows for
 * adding items to Firebase Database, through input of information and
 * images/videos.
 * <p>
 * Extends <code>TAAMSFragment</code> to use Firebase database. Implements
 * <code>ViewItemsTable</code> to use interface <code>displayItems</code> method.
 */
public class AddItemFragment extends TAAMSFragment {

    private EditText editTextLotNum, editTextName, editTextDescription;
    private Spinner spinnerCategory, spinnerPeriod;
    private ImageButton addImageButton;
    private DatabaseReference itemsRef;
    private Intent intent;
    private ImageView imageView;
    private VideoView videoView;
    private Uri contentDisplay;
    private String lotNum, name, category, period, description;

    private final ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult result) {
            if (result.getResultCode() == RESULT_OK) {
                if (result.getData() != null) {
                    addImageButton.setEnabled(true);
                    contentDisplay = result.getData().getData();
                    String mimeType = requireContext().getContentResolver().getType(contentDisplay);

                    if (mimeType != null) {
                        if (mimeType.startsWith("image/")) {
                            videoView.setVisibility(View.GONE);
                            imageView.setVisibility(View.VISIBLE);
                            imageView.setImageURI(contentDisplay);

                        }
                        else if (mimeType.startsWith("video/")) {
                            imageView.setVisibility(View.GONE);
                            videoView.setVisibility(View.VISIBLE);
                            videoView.setVideoURI(contentDisplay);
                            videoView.setOnPreparedListener(mp -> {
                                mp.setLooping(true);
                                videoView.start();
                            });
                        }
                    }
                    else {
                        Toast.makeText(getContext(), "Incompatible file type", Toast.LENGTH_SHORT).show();
                    }
                }
            }
            else {
                Toast.makeText(getContext(), "Please select an image", Toast.LENGTH_SHORT).show();
            }
        }
    });

    /**
     * Called to instantiate AddItemFragment view.
     * This view is created from the <code>fragment_add_item.xml</code> file.
     * <p>
     * Calls <code>onClick</code> for <code>addItem</code>.
     *
     * @param inflater The LayoutInflater object that can be used to inflate
     * any views in AddItemFragment,
     * @param container This is the parent view that AddItemFragment's
     * UI should be attached to. AddItemFragment should not add the view itself,
     * but this can be used to generate the LayoutParams of the view.
     * @param savedInstanceState If non-null, AddItemFragment is being re-constructed
     * from a previous saved state as given here. savedInstanceState is not used in this instance of
     * <code>onCreateView</code>.
     *
     * @return Return the View for AddItemFragment's UI, or null.
     */
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_item, container, false);
        Button buttonAdd = view.findViewById(R.id.addItemButton);

        editTextLotNum = view.findViewById(R.id.editTextText1);
        editTextName = view.findViewById(R.id.editTextText2);
        spinnerCategory = view.findViewById(R.id.spinner);
        spinnerPeriod= view.findViewById(R.id.spinner2);
        editTextDescription = view.findViewById(R.id.editTextText3);
        addImageButton = view.findViewById(R.id.imagesearch);
        imageView = view.findViewById(R.id.imageView2);
        videoView = view.findViewById(R.id.videoView);


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


        addImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(Intent.ACTION_PICK);
                intent.setType("*/*");
                String[] mimeTypes = {"image/*", "video/*"};
                intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);
                activityResultLauncher.launch(intent);
            }
        });

        return view;
    }

    /**
     * This method adds an item to Firebase Database with the provided information
     * in the Fragment view's <code>EditText</code>.
     * <p>
     * If any field is left blank, a popup is displayed and method ends. If Firebase
     * task fails or the item already exist, popup is displayed.
     * <p>
     * If task is successful, an item with the field information is added to Firebase
     * database.
     *
     */
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

        itemsRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {

                if (!(task.getResult().child(lotNum).exists())) {
                    Item item = new Item(lotNum, name, category, period, description);
                    itemsRef.child(lotNum).setValue(item).addOnCompleteListener(addTask -> {

                        if (addTask.isSuccessful()) {
                            Toast.makeText(getContext(), "Item added", Toast.LENGTH_SHORT).show();

                            if (contentDisplay != null) {
                                uploadImage(contentDisplay);
                            }
                            getParentFragmentManager().popBackStackImmediate();

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
        storageRef = storageReference.child(lotNum);
        storageRef.putFile(image);
    }

}