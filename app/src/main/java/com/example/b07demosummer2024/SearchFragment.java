/*
 * SearchFragment.java     1.0     2024/08/07
 */

package com.example.b07demosummer2024;

import android.os.Bundle;
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
import androidx.fragment.app.FragmentTransaction;

import com.example.b07demosummer2024.firebase.FirebaseCallback;
import com.example.b07demosummer2024.firebase.ItemFetcher;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * This class is used to create the view for the <code>activity_search.xml</code> file, and
 * allow user to search for items in Firebase Database.
 */
public class SearchFragment extends Fragment {


    protected EditText editTextLotNum;
    protected EditText editTextName;
    protected Spinner spinnerCategory;
    protected Spinner spinnerPeriod;
    protected Button submitButton;
    protected Button keywordSearch;
    protected TextView title;


    private final String activityTitle;
    private final String submitText;
    private View.OnClickListener submissionListener = null;

    private final String blankOption = "Not selected";

    /**
     * Constructor of SearchFragment. Assigns <code>activityTitle</code> and
     * <code>String</code> based on parameters.
     * @param title             String representing activity title.
     * @param submitText        An instance of the <code>SearchResultCallback</code> interface.
     */
    protected SearchFragment(String title, String submitText) {
        this.activityTitle = title;
        this.submitText = submitText;
    }

    /**
     * Default constructor of SearchFragment. Creates new <code>SearchFragment</code>
     * with <code>activityTitle</code> as "Search Collection".
     * Runs the <code>setSubmissionListener</code> method.
     * <p>
     * Contains the <code>setSubmissionListener</code> method, which loads <code>SearchResult</code>
     * fragment on success and displays an error popup on failure.
     */
    public SearchFragment() {
        this("Search collection", "Search");
        setSubmissionListener(new FirebaseCallback<List<Item>>() {

            @Override
            public void onFirebaseSuccess(List<Item> results) {
                // search result here
                loadFragment(new SearchResultFragment(results));
            }

            @Override
            public void onFirebaseFailure(String message) {
                Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * Called to instantiate SearchFragment view.
     * This view is created from the <code>activity_search.xml</code> file.
     *
     * @param inflater The LayoutInflater object that can be used to inflate
     * any views in SearchFragment,
     * @param container This is the parent view that SearchFragment's
     * UI should be attached to. SearchFragment should not add the view itself,
     * but this can be used to generate the LayoutParams of the view.
     * @param savedInstanceState If non-null, SearchFragment is being re-constructed
     * from a previous saved state as given here. savedInstanceState is not used in this instance of
     * <code>onCreateView</code>.
     *
     * @return Return the View for activity_search's UI, or null.
     */
    @Nullable
    @Override
    public final View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                                   @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_search, container, false);
    }

    /**
     * Called immediately after {@link #onCreateView(LayoutInflater, ViewGroup, Bundle)}
     * has returned, but before any saved state has been restored in to the view.
     * This allows SearchFragment to instantiate all necessary variables using the
     * successfully generated <code>view</code>.
     *
     * @param view The View returned by {@link #onCreateView(LayoutInflater, ViewGroup, Bundle)}.
     * @param savedInstanceState If non-null, this fragment is being re-constructed
     * from a previous saved state as given here. savedInstanceState is not used in this
     * method.
     */
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
        keywordSearch = view.findViewById(R.id.keywordSearch);
        keywordSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //user = null;
                loadFragment(new KeywordSearchFragment());
            }
        });

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
     * of the search results is done with the provided FirebaseCallback.
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