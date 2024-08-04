package com.example.b07demosummer2024;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

/**
 * Class used to create display for <code>fragment_spinner.xml</code> package.
 * <p>
 * A Spinner is an interactable drop down display allowing user to select between
 * different predetermined options.
 *
 * Gonna have to spend some time figuring out what the fuck this thing does :(
 *
 */
public class SpinnerFragment extends Fragment { //Should not extend TAAMSFragment???????

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

        Spinner spinner = view.findViewById(R.id.spinner);
        Spinner spinner2 = view.findViewById(R.id.spinner2);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),
                R.array.categories_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(getContext(),
                R.array.periods_array, android.R.layout.simple_spinner_item);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner2.setAdapter(adapter2);

        return view;
    }
}
