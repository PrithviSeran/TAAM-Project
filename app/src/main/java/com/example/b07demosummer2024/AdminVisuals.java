package com.example.b07demosummer2024;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

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
public class AdminVisuals extends Fragment {


    FirebaseDatabase database = FirebaseDatabase.getInstance("https://login-taam-bo7-default-rtdb.firebaseio.com/");
    private DatabaseReference itemsRef;

    private TableRow tableRow1;

    private TextView textView1;
    private CheckBox checkBox;
    private Button viewItem;
    private Button deleteButton;
    private Button addItem;

    private HashMap<CheckBox, String> leftOfCheckBoxes = new HashMap<CheckBox, String>();
    private ArrayList<String> itemsToDelete = new ArrayList<String>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_admin_visuals, container, false);

        TableLayout tableLayout1 = view.findViewById(R.id.tableLayout);

        itemsRef = database.getReference("Items");

        deleteButton = view.findViewById(R.id.button10);
        addItem = view.findViewById(R.id.addItemButton);

        itemsRef.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {

            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e("firebase", "Error getting data", task.getException());
                }
                else {

                    Log.d("Class", String.valueOf(task.getResult().getValue().getClass()));

                    for (Object entry1 : ((HashMap)task.getResult().getValue()).entrySet()) {
                        tableRow1 = new TableRow(getActivity());
                        checkBox = new CheckBox(getActivity());

                        leftOfCheckBoxes.put(checkBox, String.valueOf(((Map.Entry)entry1).getKey()));

                        tableRow1.addView(checkBox);

                        textView1 = new TextView(getActivity());
                        textView1.setText(String.valueOf(((Map.Entry)entry1).getKey()));

                        tableRow1.addView(textView1);

                        viewItem = new Button(getActivity());
                        viewItem.setText("View Item");

                        viewItem.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                // Handle the button click
                                loadFragment(new ViewItem(String.valueOf(((Map.Entry)entry1).getKey())));
                            }
                        });

                        tableRow1.addView(viewItem);

                        tableLayout1.addView(tableRow1);
                    }

                    Log.d("CheckBoxes", String.valueOf(leftOfCheckBoxes));
                }
            }
        });

        deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v){
                    for (Object box : leftOfCheckBoxes.entrySet()) {

                        if (((CheckBox)((Map.Entry) box).getKey()).isChecked()){
                            System.out.println(String.valueOf(((Map.Entry) box).getValue()));
                            itemsToDelete.add(String.valueOf(((Map.Entry) box).getValue()));
                        }
                    }
                    loadFragment(new DeleteItemFragment(itemsToDelete));
                }
            }
        );

        addItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadFragment(new AddItemFragment());
            }
        });

        // Inflate the layout for this fragment
        return view;

    }
    private void loadFragment(Fragment fragment) {
        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}