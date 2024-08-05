/*
 * UserTableViewFragment.java     1.0     2024/08/04
 */

package com.example.TAAM_collection_management.CatalogueView;

import android.os.Bundle;

import androidx.annotation.NonNull;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.TAAM_collection_management.Search.SearchFragment;
import com.example.TAAM_collection_management.ItemView.ViewItemFragment;
import com.example.TAAM_collection_management.Objects.TAAMSFragment;
import com.example.b07demosummer2024.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;

/**
 * Class used to display <code>fragment_user_table_view.xml</code>, and
 * compiling proper information for the view.
 * <p>
 * user_table_view creates a view for the xml file and display the list
 * of all items under the "Items" reference stored in Firebase Database.
 * It also adds functionality to a button which allows for searching
 * through displayed items, through <code>SearchFragment</code>.
 * <p>
 * Extends <code>TAAMSFragment</code> to use Firebase database. Implements
 * <code>ViewItemsTable</code> to use interface <code>displayItems</code> method.
 */
public class UserTableViewFragment extends TAAMSFragment implements ViewItemsTable {

    private TableLayout tableLayout;

    /**
     * Called to instantiate user_table_view view.
     * This view is created from the <code>fragment_user_table_view.xml</code> file.
     * <p>
     * Calls <code>onClick</code> for <code>searchItem</code> and is used
     * to call <code>loadFragment</code> for <code>SearchFragment</code>.
     *
     * @param inflater The LayoutInflater object that can be used to inflate
     * any views in user_table_view,
     * @param container This is the parent view that user_table_view's
     * UI should be attached to. user_table_view should not add the view itself,
     * but this can be used to generate the LayoutParams of the view.
     * @param savedInstanceState If non-null, user_table_view is being re-constructed
     * from a previous saved state as given here. savedInstanceState is not used in this instance of
     * <code>onCreateView</code>.
     *
     * @return Return the View for user_table_view's UI, or null.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_user_table_view, container, false);
        Button searchItem = view.findViewById(R.id.button12);

        tableLayout = view.findViewById(R.id.tableLayout);

        searchItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadFragment(new SearchFragment());
            }
        });

        displayItems();

        // Inflate the layout for this fragment
        return view;
    }

    //Doc comments in overridden class
    @Override
    public void displayItems(){

        itemsRef = database.getReference("Items");

        itemsRef.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {

            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e("firebase", "Error getting data", task.getException());
                    Toast.makeText(getContext(), "Display Error", Toast.LENGTH_SHORT).show();
                }
                else {

                    TableRow tableRow;
                    TextView logNumTextView;
                    TextView nameTextView;
                    Button viewItemButton;

                    for (DataSnapshot entry : (task.getResult().getChildren())) {
                        tableRow = new TableRow(getActivity());

                        logNumTextView = new TextView(getActivity());
                        logNumTextView.setText(String.valueOf(entry.child("lotNum").getValue()));
                        setTextViewStyle(logNumTextView);
                        logNumTextView.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1.5f));
                        tableRow.addView(logNumTextView);

                        nameTextView = new TextView(getActivity());
                        nameTextView.setText(String.valueOf(entry.child("name").getValue()));// Change to get name
                        nameTextView.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 3f));
                        setTextViewStyle(nameTextView);
                        tableRow.addView(nameTextView);

                        viewItemButton = new Button(getActivity());
                        viewItemButton.setText("View");
                        setButtonStyle(viewItemButton);

                        viewItemButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                // Handle the button click
                                loadFragment(new ViewItemFragment(String.valueOf(entry.getKey())));
                            }
                        });

                        tableRow.addView(viewItemButton);
                        tableLayout.addView(tableRow);
                    }
                }
            }
        });
    }
}


