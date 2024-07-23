package com.example.b07demosummer2024;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class SearchFragment extends Fragment {

    protected interface SearchResultCallback {
        void onSuccess(List<Item> results);
        void onFailure(String message);
    }

    private EditText editTextLotNum;
    private EditText editTextName;
    private Spinner spinnerCategory;
    private Spinner spinnerPeriod;
    private Button submitButton;
    private TextView title;

    private final FirebaseDatabase database = FirebaseDatabase.getInstance(
            "https://login-taam-bo7-default-rtdb.firebaseio.com/");

    private final String activityTitle;
    private final View.OnClickListener submissionListener;

    private final String blankOption = "Not selected";

    public SearchFragment() {
        this.activityTitle = "Search for items";
        this.submissionListener = setSubmissionListener(new SearchResultCallback() {
            @Override
            public void onSuccess(List<Item> results) {
                for (Item i : results) {
                    Log.i("Results", i.toString());
                }
            }

            @Override
            public void onFailure(String message) {

            }
        });
    }

    protected SearchFragment(String title, SearchResultCallback searchResponse) {
        this.activityTitle = title;
        this.submissionListener = setSubmissionListener(searchResponse);
    }

    @Nullable
    @Override
    public final View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_search, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        // Title
        title = view.findViewById(R.id.searchActivityTitle);
        title.setText(activityTitle);

        // EditTexts
        editTextLotNum = view.findViewById(R.id.editTextLotNumber);
        editTextName = view.findViewById(R.id.editTextName);

        // Buttons
        submitButton = view.findViewById(R.id.submitConfirm);
        submitButton.setOnClickListener(submissionListener);

        // Spinners
        spinnerCategory = view.findViewById(R.id.categorySpinner);
        spinnerPeriod = view.findViewById(R.id.periodSpinner);

        initializeSpinner(R.array.categories_array, spinnerCategory);
        initializeSpinner(R.array.periods_array, spinnerPeriod);
    }

    private View.OnClickListener setSubmissionListener(SearchResultCallback callback) {
        return (v -> {
            String lotNum = normalize(editTextLotNum.getText().toString());
            String name = normalize(editTextName.getText().toString());
            String category = normalize(spinnerCategory.getSelectedItem().toString());
            String period = normalize(spinnerPeriod.getSelectedItem().toString());

            if (isAllBlankInput(lotNum, name, category, period)) {
                Toast.makeText(getContext(), "Please fill in at least one field!", Toast.LENGTH_SHORT).show();
            } else {
                searchItems(callback, lotNum, name, category, period);
            }
        });
    }

    protected void searchItems(SearchResultCallback callback, String lotNum, String name,
                               String category, String period) {
        List<Item> searchResults = new ArrayList<>();
        DatabaseReference itemsRef = database.getReference();
        itemsRef.child("Items").get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DataSnapshot itemsList = task.getResult();
                for (DataSnapshot snapshot : itemsList.getChildren()) {
                    Item item = snapshot.getValue(Item.class);

                    // if the fields are blank, compare() should return true anyways
                    if (item != null && compare(lotNum, item.getLotNum()) && compare(name, item.getName())
                            && compare(category, item.getCategory()) && compare(period, item.getPeriod())) {
                        searchResults.add(item);
                    }
                }
                callback.onSuccess(searchResults);
            } else {
                callback.onFailure(Objects.toString(task.getException(), "No message available"));
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
        if (!isBlankInput(input) && compareTo != null) {
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