package com.example.b07demosummer2024;

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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;

public class user_table_view extends TAAMSFragment implements ViewItemsTable{

    private TableRow newRow;
    private TextView lotText, nameText;
    private Button viewItem;
    private TableLayout mainTable;
    private Button searchItem;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_user_table_view, container, false);

        mainTable = view.findViewById(R.id.tableLayout);

        searchItem = view.findViewById(R.id.searchButton);

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
                        newRow = new TableRow(getActivity());
                        newRow.setPadding(0,10,0,10);

                        lotText = new TextView(getActivity());
                        lotText.setText(String.valueOf(entry1.child("lotNum").getValue()));
                        setTextViewStyle(lotText);
                        lotText.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1.5f));
                        newRow.addView(lotText);

                        nameText = new TextView(getActivity());
                        nameText.setText(String.valueOf(entry1.child("name").getValue()));// Change to get name
                        nameText.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 3f));
                        setTextViewStyle(nameText);
                        newRow.addView(nameText);

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

                        newRow.addView(viewItem);

                        mainTable.addView(newRow);
                    }
                }
            }
        });
    }
}


