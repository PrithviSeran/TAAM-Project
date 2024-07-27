package com.example.b07demosummer2024;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link user_table_view#newInstance} factory method to
 * create an instance of this fragment.
 */
public class user_table_view extends Fragment {

    private TableRow tableRow1;

    private TextView textView1, textView2, textView3, textView4;
    private CheckBox checkBox;
    private Button viewItem;
    private Button backButton;

    FirebaseDatabase database = FirebaseDatabase.getInstance("https://login-taam-bo7-default-rtdb.firebaseio.com/");
    private DatabaseReference itemsRef;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_user_table_view, container, false);

        TableLayout tableLayout1 = view.findViewById(R.id.tableLayout);


        itemsRef = database.getReference("Items");

        itemsRef.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {

            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e("firebase", "Error getting data", task.getException());
                }
                else {

                    for (Object entry1 : ((HashMap)task.getResult().getValue()).entrySet()) {
                        tableRow1 = new TableRow(getActivity());
                        checkBox = new CheckBox(getActivity());

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
                }
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