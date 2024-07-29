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
public class AdminVisuals extends TAAMSFragment {


    FirebaseDatabase database = FirebaseDatabase.getInstance("https://login-taam-bo7-default-rtdb.firebaseio.com/");
    private DatabaseReference itemsRef;

    private TableRow tableRow1;

    private TextView textView1, textView2;
    private CheckBox checkBox;
    private Button viewItem;
    private Button deleteButton;
    private Button addItem;
    private Button searchItem;

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
        searchItem = view.findViewById(R.id.button12);
        itemsRef.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {

            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e("firebase", "Error getting data", task.getException());
                }
                else {

                    leftOfCheckBoxes.clear();

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

        deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v){
                    for (Object box : leftOfCheckBoxes.entrySet()) {

                        if (((CheckBox)((Map.Entry) box).getKey()).isChecked()){
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

        searchItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadFragment(new SearchFragment());
            }
        });

        return view;
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private void setTextViewStyle(TextView textView) {
        Typeface typeface = ResourcesCompat.getFont(requireActivity(), R.font.lato);
        textView.setTypeface(typeface);

        textView.setTextSize(15);

        textView.setGravity(Gravity.CENTER);

        textView.setTextColor(getResources().getColor(R.color.black, null));

        textView.setBackground(getResources().getDrawable(R.drawable.border_square));

        textView.setPadding(0,5,0,5);
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private void setButtonStyle(Button button){

        Typeface typeface = ResourcesCompat.getFont(requireActivity(), R.font.lato);
        button.setAllCaps(false);
        button.setBackground(getResources().getDrawable(R.drawable.button_view));

        button.setTextColor(getResources().getColor(R.color.shaded_white, null));

        button.setTextSize(15);

        button.setPadding(0,5,0,5);

        TableRow.LayoutParams params = new TableRow.LayoutParams(150, 80);
        button.setLayoutParams(params);
    }

}