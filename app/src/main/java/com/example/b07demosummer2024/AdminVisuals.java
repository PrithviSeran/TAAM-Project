package com.example.b07demosummer2024;

import android.annotation.SuppressLint;
import android.graphics.Typeface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


/**
 * CLass used to display <code>fragment_admin_visuals.xml</code>, and
 * compiling proper information for the view.
 * <p>
 * AdminVisuals creates a view for the xml file and display the list
 * of all items under the "Items" reference stored in Firebase Database.
 * It also gives functionality to multiple buttons allowing for adding, deleting,
 * reporting, and searching items displayed on screen, through other classes.
 * <p>
 * Extends <code>TAAMSFragment</code> to use Firebase database. Implements
 * <code>ViewItemsTable</code> to use interface <code>displayItems</code> method.
 */
public class AdminVisuals extends TAAMSFragment implements ViewItemsTable{

    private TableRow tableRow1;

    private TextView textView1, textView2;
    private CheckBox checkBox;
    private Button viewItem;
    private Button deleteButton;    //make local
    private Button addItem;         //make local
    private TableLayout tableLayout1;
    private Button searchItem;

    private HashMap<CheckBox, String> leftOfCheckBoxes = new HashMap<CheckBox, String>();
    private HashMap<String, String> nameToLotNum = new HashMap<String, String>();
    private ArrayList<String> nameofItemsToDelete = new ArrayList<String>();
    private ArrayList<String> lotNumsofItemsToDelete = new ArrayList<String>();

    /**
     * Called to instantiate <code>fragment_admin_visuals.xml</code> view.
     * <p>
     * Calls <code>onClick</code> for <code>deleteButton</code> and is used
     * to call <code>loadFragment</code> for <code>DeleteItemFragment</code>.
     * <p>
     * Calls <code>onClick</code> for <code>AddItemFragment</code> and <code>SearchFragment</code>
     * with their corresponding buttons.
     *
     * @param inflater The LayoutInflater object that can be used to inflate
     * any views in <code>fragment_admin_visuals.xml</code>,
     * @param container This is the parent view that the fragment's
     * UI should be attached to. <code>fragment_admin_visuals.xml</code> should not add the view itself,
     * but this can be used to generate the LayoutParams of the view.
     * @param savedInstanceState If non-null, <code>fragment_admin_visuals.xml</code> is being re-constructed
     * from a previous saved state as given here. savedInstanceState is note used in this instance of
     * <code>onCreateView</code>
     *
     * @return Return the View for <code>fragment_admin_visuals.xml</code>'s UI, or null.
     */
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_admin_visuals, container, false);

        tableLayout1 = view.findViewById(R.id.tableLayout);

        itemsRef = database.getReference("Items");

        searchItem = view.findViewById(R.id.button12);

        deleteButton = view.findViewById(R.id.button10);
        addItem = view.findViewById(R.id.addItemButton);

        displayItems();

        deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v){

                    nameofItemsToDelete.clear();
                    lotNumsofItemsToDelete.clear();

                    for (Object box : leftOfCheckBoxes.entrySet()) {

                        if (((CheckBox)((Map.Entry) box).getKey()).isChecked()){
                            nameofItemsToDelete.add(String.valueOf(((Map.Entry) box).getValue()));
                        }
                    }

                    for (String name : nameofItemsToDelete) {
                        lotNumsofItemsToDelete.add(nameToLotNum.get(name));
                    }

                    loadFragment(new DeleteItemFragment(nameofItemsToDelete, lotNumsofItemsToDelete));
                }
            }
        );

        searchItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadFragment(new SearchFragment());
            }
        });

        addItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadFragment(new AddItemFragment());
            }
        });

        // Inflate the layout for this fragment
        return view;

    }

    @Override
    public void displayItems(){

        leftOfCheckBoxes.clear();
        nameToLotNum.clear();

        itemsRef.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {

            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e("firebase", "Error getting data", task.getException());
                    Toast.makeText(getContext(), "Unexpected Error", Toast.LENGTH_SHORT).show();
                }
                else {

                    Log.d("Class", String.valueOf(task.getResult().getValue().getClass()));

                    for (DataSnapshot entry1 : (task.getResult().getChildren())) {
                        tableRow1 = new TableRow(getActivity());
                        checkBox = new CheckBox(getActivity());

                        tableRow1.addView(checkBox);

                        textView1 = new TextView(getActivity());
                        textView1.setText(String.valueOf(entry1.child("lotNum").getValue()));
                        setTextViewStyle(textView1);
                        textView1.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1.5f));
                        tableRow1.addView(textView1);

                        textView2 = new TextView(getActivity());
                        textView2.setText(String.valueOf(entry1.child("name").getValue()));// Change to get name
                        leftOfCheckBoxes.put(checkBox, String.valueOf(entry1.child("name").getValue()));
                        nameToLotNum.put(String.valueOf(entry1.child("name").getValue()), String.valueOf(entry1.child("lotNum").getValue()));
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



