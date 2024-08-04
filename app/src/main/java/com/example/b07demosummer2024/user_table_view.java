package com.example.b07demosummer2024;

import android.annotation.SuppressLint;
import android.graphics.Typeface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

/**
 * CLass used to display <code>fragment_user_table_view.xml</code>, and
 * compiling proper information for the view.
 * <p>
 * user_table_view creates a view for the xml file and display the list
 * of all items under the "Items" reference stored in Firebase Database.
 * <p>
 * Extends <code>TAAMSFragment</code> to use Firebase database. Implements
 * <code>ViewItemsTable</code> to use interface <code>displayItems</code> method.
 *
 *
 */
public class user_table_view extends TAAMSFragment implements ViewItemsTable{

    private TableRow tableRow1;
    private TextView textView1, textView2;
    private Button viewItem;
    private TableLayout tableLayout1;
    private Button searchItem;

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

        tableLayout1 = view.findViewById(R.id.tableLayout);

        searchItem = view.findViewById(R.id.button12);

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

                    for (DataSnapshot entry1 : (task.getResult().getChildren())) {
                        tableRow1 = new TableRow(getActivity());

                        textView1 = new TextView(getActivity());
                        textView1.setText(String.valueOf(entry1.child("lotNum").getValue()));
                        setTextViewStyle(textView1);
                        textView1.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1.5f));
                        tableRow1.addView(textView1);

                        textView2 = new TextView(getActivity());
                        textView2.setText(String.valueOf(entry1.child("name").getValue()));// Change to get name
                        textView2.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 3f));
                        setTextViewStyle(textView2);
                        tableRow1.addView(textView2);

                        viewItem = new Button(getActivity());
                        viewItem.setText("View");
                        setButtonStyle(viewItem);

                        viewItem.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                // Handle the button click
                                loadFragment(new ViewItem(String.valueOf(entry1.getKey())));
                            }
                        });

                        tableRow1.addView(viewItem);

                        tableLayout1.addView(tableRow1);
                    }
                }
            }
        });
    }
}


