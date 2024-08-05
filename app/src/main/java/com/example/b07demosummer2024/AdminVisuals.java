package com.example.b07demosummer2024;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.b07demosummer2024.firebase.FirebaseCallback;
import com.example.b07demosummer2024.firebase.ItemFetcher;
import com.example.b07demosummer2024.report.ReportFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AdminVisuals extends user_table_view implements ViewItemsTable{

    private TableRow newRow;

    private TextView lotText, nameText;
    private CheckBox selectItemCheckbox;
    private Button viewItem;
    private Button deleteButton;    //make local
    private Button addItem;         //make local
    private TableLayout mainTable;
    private Button searchItem;
    private Button reportButton;

    private HashMap<CheckBox, String> leftOfCheckBoxes = new HashMap<CheckBox, String>();
    private HashMap<String, String> nameToLotNum = new HashMap<String, String>();
    private ArrayList<String> nameofItemsToDelete = new ArrayList<String>();
    private ArrayList<String> lotNumsofItemsToDelete = new ArrayList<String>();

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
                    TableRow newRow = AdminVisuals.super.getItemTableRow(item);
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



