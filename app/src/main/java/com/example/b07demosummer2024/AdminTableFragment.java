/*
 * AdminTableFragment.java     1.0     2024/08/07
 */

package com.example.b07demosummer2024;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TableLayout;
import android.widget.TableRow;

import com.example.b07demosummer2024.firebase.FirebaseCallback;
import com.example.b07demosummer2024.firebase.ItemFetcher;
import com.example.b07demosummer2024.report.ReportFragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Class used to display <code>fragment_admin_visuals.xml</code>, and
 * compiling proper information for the view.
 * <p>
 * AdminTableFragment creates a view for the xml file and display the list
 * of all items under the "Items" reference stored in Firebase Database.
 * It also gives functionality to multiple buttons allowing for adding, deleting,
 * reporting, and searching items displayed on screen, through other classes.
 * <p>
 * Extends <code>UserTableFragment</code> due to similar view layouts. Implements
 * <code>ViewItemsTable</code> to use interface <code>displayItems</code> method.
 */
public class AdminTableFragment extends UserTableFragment implements ViewItemsTable{

    private CheckBox selectItemCheckbox;
    private Button deleteButton;    //make local
    private Button addItem;         //make local
    private TableLayout mainTable;
    private Button searchItem;
    private Button reportButton;

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

        mainTable = view.findViewById(R.id.tableLayout);

        itemsRef = database.getReference("Items");

        searchItem = view.findViewById(R.id.button12);

        deleteButton = view.findViewById(R.id.button10);
        addItem = view.findViewById(R.id.addItemButton);
        reportButton = view.findViewById(R.id.createReportButton);

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

                    if (!nameofItemsToDelete.isEmpty() && !lotNumsofItemsToDelete.isEmpty()) {
                        loadFragment(new DeleteItemFragment(nameofItemsToDelete, lotNumsofItemsToDelete));
                    } else {
                        CommonUtils.makeAndShowShortToast("No items selected", getContext());
                    }
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

        reportButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadFragment(new ReportFragment());
            }
        });

        // Inflate the layout for this fragment
        return view;

    }

    @Override
    public void displayItems(){

        leftOfCheckBoxes.clear();
        nameToLotNum.clear();

        ItemFetcher.getAllItems(new FirebaseCallback<List<Item>>() {
            @Override
            public void onFirebaseSuccess(List<Item> results) {
                for (Item item : results) {
                    selectItemCheckbox = new CheckBox(getActivity());
                    TableRow newRow = AdminTableFragment.super.getItemTableRow(item);
                    newRow.setPadding(0,10,0,10);
                    newRow.addView(selectItemCheckbox, 0);

                    leftOfCheckBoxes.put(selectItemCheckbox, item.getName());
                    nameToLotNum.put(item.getName(), item.getLotNum());
                    mainTable.addView(newRow);
                }
            }
            @Override
            public void onFirebaseFailure(String message) {
                CommonUtils.logError("FirebaseError", message);
                CommonUtils.makeAndShowShortToast("Display Error", getContext());
            }
        });

    }

}



