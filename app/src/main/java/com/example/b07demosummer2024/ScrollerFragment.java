package com.example.b07demosummer2024;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

/**
 * Class used to create display for <code>fragment_scroller.xml</code> package.
 * <p>
 * A Scroller is an object that displays visuals for scrolling.
 */
public class ScrollerFragment extends Fragment {

    /**
     * Called to instantiate ScrollerFragment view.
     * This view is created from the <code>fragment_spinner.xml</code> file.
     *
     * @param inflater The LayoutInflater object that can be used to inflate
     * any views in ScrollerFragment,
     * @param container This is the parent view that the fragment's
     * UI should be attached to. ScrollerFragment should not add the view itself,
     * but this can be used to generate the LayoutParams of the view.
     * @param savedInstanceState If non-null, ScrollerFragment is being re-constructed
     * from a previous saved state as given here. savedInstanceState is not used in this instance of
     * <code>onCreateView</code>
     *
     * @return Return the View for <code>fragment_spinner.xml</code>'s UI, or null.
     */
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_scroller, container, false);

        TextView textView = view.findViewById(R.id.textView);
        StringBuilder text = new StringBuilder();
        for (int i = 0; i < 20; i++) {
            text.append("Line ").append(i + 1).append("\n");
        }
        textView.setText(text.toString());

        return view;
    }
}
