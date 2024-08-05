/*
 * SpinnerFragment.java     1.0     2024/08/04
 */

package com.example.TAAM_collection_management.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.b07demosummer2024.R;

/**
 * Class used to create display for <code>fragment_spinner.xml</code> file.
 * Creates two spinners, one with all item categories and one with all item periods.
 * <p>
 * A Spinner is an interactable drop down display allowing user to select between
 * different predetermined options.
 */
public class SpinnerFragment extends Fragment {

    /**
     * Called to instantiate SpinnerFragment view.
     * This view is created from the <code>fragment_spinner.xml</code> file.
     *
     * @param inflater The LayoutInflater object that can be used to inflate
     * any views in SpinnerFragment,
     * @param container This is the parent view that the fragment's
     * UI should be attached to. SpinnerFragment should not add the view itself,
     * but this can be used to generate the LayoutParams of the view.
     * @param savedInstanceState If non-null, SpinnerFragment is being re-constructed
     * from a previous saved state as given here. savedInstanceState is not used in this instance of
     * <code>onCreateView</code>
     *
     * @return Return the View for <code>fragment_spinner.xml</code>'s UI, or null.
     */
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_spinner, container, false);
        Spinner categorySpinner = view.findViewById(R.id.spinner);
        Spinner periodSpinner = view.findViewById(R.id.spinner2);

        //Initializing array adapters
        ArrayAdapter<CharSequence> categoryAdapter = ArrayAdapter.createFromResource(getContext(),
                R.array.categories_array, android.R.layout.simple_spinner_item);
        ArrayAdapter<CharSequence> periodAdapter = ArrayAdapter.createFromResource(getContext(),
                R.array.periods_array, android.R.layout.simple_spinner_item);

        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categorySpinner.setAdapter(categoryAdapter);

        periodAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        periodSpinner.setAdapter(periodAdapter);

        return view;
    }
}
