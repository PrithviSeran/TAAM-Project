package com.example.b07demosummer2024;

import android.os.Bundle;
import android.os.StrictMode;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.b07demosummer2024.firebase.FirebaseCallback;
import com.example.b07demosummer2024.firebase.FirebaseReferences;
import com.example.b07demosummer2024.firebase.ItemFetcher;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class SearchFragment extends Fragment {

    protected EditText editTextLotNum;
    protected EditText editTextName;
    protected Spinner spinnerCategory;
    protected Spinner spinnerPeriod;
    protected Button submitButton;
    protected TextView title;

    private final String activityTitle;
    private final String submitText;
    private View.OnClickListener submissionListener = null;

    private final String blankOption = "Not selected";

    /**
     * Creates a new SearchFragment with title "title".
     * @param title the title for this fragment
     */
    protected SearchFragment(String title, String submitText) {
        this.activityTitle = title;
        this.submitText = submitText;
    }

    /**
     * Creates a new SearchFragment with default title "title". Sets the submission
     * listener to by default graphically display the search results.
     */
    public SearchFragment() {
        this("Search collection", "Search");
        setSubmissionListener(new FirebaseCallback<List<Item>>() {
            @Override
            public void onSuccess(List<Item> results) {
                // search result here
                loadFragment(new SearchResult(results));
            }

            @Override
            public void onFailure(String message) {

            }
        });
    }

    @Nullable
    @Override
    public final View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                                   @Nullable Bundle savedInstanceState) {
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
        submitButton.setText(submitText);
        submitButton.setOnClickListener(submissionListener);

        // Spinners
        spinnerCategory = view.findViewById(R.id.categorySpinner);
        spinnerPeriod = view.findViewById(R.id.periodSpinner);

        initializeSpinner(R.array.categories_array, spinnerCategory);
        initializeSpinner(R.array.periods_array, spinnerPeriod);
    }

    /**
     * Sets this SearchFragment's submissionListener with callback "onSearchCompleted".
     * @param onSearchCompleted the callback that handles the search results
     */
    protected void setSubmissionListener(FirebaseCallback<List<Item>> onSearchCompleted) {
        this.submissionListener = (v -> performSearch(onSearchCompleted));
    }

    /**
     * Performs a search query based on the input from this SearchFragment. The handling
     * of the search results is done with the provided SearchResultCallback.
     * @see FirebaseCallback
     * @see SearchFragment#setSubmissionListener
     * @param onSearchCompleted the callback that handles the search results
     */
    protected void performSearch(FirebaseCallback<List<Item>> onSearchCompleted) {
        if (onSearchCompleted == null) {
            throw new NullPointerException("Cannot have null callback");
        }

        String lotNum = normalize(editTextLotNum.getText().toString());
        String name = normalize(editTextName.getText().toString());
        String category = normalize(spinnerCategory.getSelectedItem().toString());
        String period = normalize(spinnerPeriod.getSelectedItem().toString());

        ItemFetcher.searchItems(onSearchCompleted, this::compare, lotNum, name, category, period);
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

    private void loadFragment(Fragment fragment) {
        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}