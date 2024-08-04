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
import androidx.fragment.app.FragmentTransaction;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * This class is used to create the view for the <code>activity_search.xml</code> file, and
 * allow user to search for items in Firebase Database.
 * <p>
 * Extends <code>TAAMSFragment</code> to use Firebase database and several
 * override methods of <code>Fragment</code> class.
 */
public class SearchFragment extends TAAMSFragment {

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
    private Button keywordSearch;

    private final FirebaseDatabase database = FirebaseDatabase.getInstance(
            "https://login-taam-bo7-default-rtdb.firebaseio.com/");

    private final String activityTitle;
    private final View.OnClickListener submissionListener;

    private final String blankOption = "Not selected";

    /**
     * Default constructor of SearchFragment. Runs the <code>setSubmissionListener</code> method.
     * <p>
     * Contains the <code>setSubmissionListener</code> method, which loads <code>SearchResult</code>
     * fragment on success and displays an error popup on failure.
     *
     */
    public SearchFragment() {
        this.activityTitle = "Search Collection";

        this.submissionListener = setSubmissionListener(new SearchResultCallback() {
            @Override
            public void onSuccess(List<Item> results) {
                // search result here
                loadFragment(new SearchResult(results));
            }

            @Override
            public void onFailure(String message) {
                Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * Constructor of SearchFragment. Assigns <code>activityTitle</code> and
     * <code>submissionListener</code> based on parameters.
     * @param title             String representing activity title.
     * @param searchResponse    An instance of the <code>SearchResultCallback</code> interface.
     */
    protected SearchFragment(String title, SearchResultCallback searchResponse) {
        this.activityTitle = title;
        this.submissionListener = setSubmissionListener(searchResponse);
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
    public final View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
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
     * OnClickListener that checks if at least one search variable has been filled.
     * <p>
     * When corresponding button is clicked in view, <code>setSubmissionListener</code> checks
     * all <code>EditText</code>'s and <code>Spinner</code>'s to see if any are empty.
     * If at least one is filled, <code>searchItems</code> is called, else a popup is displayed.
     * @param callback      Instance of <code>SearchResultCallback</code>.
     * @return              Returns view generated by <code>searchItems</code>, or null.
     */
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

    /**
     * Creates a list of all items in Firebase database that match the specifications
     * given by <code>lotNum</code>, <code>name</code>, <code>category</code>, and <code>period</code>.
     * <p>
     * If accessing items from database fails, <code>onFailure</code> is called by <code>callback</code>.
     * If no items match the specifications, a popup is displayed.
     * <p>
     * If a nonempty list of Items is successfully generated, <code>onSuccess</code> is called by
     * <code>callback</code> which loads the search results into the <code>SearchResult</code> fragment.
     * <p>
     * <b>Note:</b> any of <code>lotNum</code>, <code>name</code>, <code>category</code>, or <code>period</code>
     * can be empty but at least one should have a value.
     *
     * @param callback      Instance of <code>SearchResultCallback</code> interface.
     * @param lotNum        Lot number of item.
     * @param name          Name of item.
     * @param category      Category of item.
     * @param period        Period of item.
     */
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
                if(searchResults.isEmpty()){
                    Toast.makeText(getContext(), "No items match this description", Toast.LENGTH_SHORT).show();
                } else {
                    callback.onSuccess(searchResults);
                }
            } else {
                callback.onFailure(Objects.toString(task.getException(), "No message available"));
            }
        });
    }

    /**
     * Initializes spinners for use in <code>SearchFragment</code>'s view.
     *
     * @param arrayId       Id of input array, the content's of which will replace the items in <code>spinner</code>.
     * @param spinner       The spinner being replaced by <code>arrayId</code> inputs.
     */
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
            if (!isBlankInput(s)) {
                return false;
            }
        }
        return true;
    }
}