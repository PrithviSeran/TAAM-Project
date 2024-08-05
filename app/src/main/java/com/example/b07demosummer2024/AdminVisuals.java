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

import com.example.b07demosummer2024.report.ReportFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AdminVisuals#newInstance} factory method to
 * create an instance of this fragment.
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
    private Button reportButton;

    private HashMap<CheckBox, String> leftOfCheckBoxes = new HashMap<CheckBox, String>();
    private HashMap<String, String> nameToLotNum = new HashMap<String, String>();
    private ArrayList<String> nameofItemsToDelete = new ArrayList<String>();
    private ArrayList<String> lotNumsofItemsToDelete = new ArrayList<String>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_admin_visuals, container, false);

        tableLayout1 = view.findViewById(R.id.tableLayout);

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



