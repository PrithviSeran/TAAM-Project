package com.example.b07demosummer2024;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AdminVisuals#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AdminVisuals extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    FirebaseDatabase database = FirebaseDatabase.getInstance("https://login-taam-bo7-default-rtdb.firebaseio.com/");
    private DatabaseReference itemsRef;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private TextView lotNum, itemName, itemCategory, itemPeriod;
    private RadioButton firstRadioButtonTest;

    public AdminVisuals() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AdminVisuals.
     */
    // TODO: Rename and change types and number of parameters
    public static AdminVisuals newInstance(String param1, String param2) {
        AdminVisuals fragment = new AdminVisuals();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_admin_visuals, container, false);

        lotNum = view.findViewById(R.id.lot1);
        itemName = view.findViewById(R.id.name1);
        itemCategory = view.findViewById(R.id.category1);
        itemPeriod= view.findViewById(R.id.description1);

        //firstRadioButtonTest = view.findViewById(R.id.radioButton1);
        //firstRadioButtonTest.setChecked(true);



        itemsRef = database.getReference("categories");

        System.out.println("Items Ref " + itemsRef);
        //String id = itemsRef.push().getKey();
        //Item item = new Item(lotNum, name, category, period, description, pic);


        itemsRef.child("Test").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {

            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e("firebase", "Error getting data", task.getException());
                }
                else {
                    lotNum.setText(String.valueOf(task.getResult().child("lotNum").getValue())); //task.getResult().child("lotNum").getValue())
                    itemName.setText(String.valueOf(task.getResult().child("name").getValue())); //task.getResult().child("name").getValue())
                    itemCategory.setText(String.valueOf(task.getResult().child("category").getValue())); //task.getResult().child("category").getValue())
                    itemPeriod.setText(String.valueOf(task.getResult().child("period").getValue())); //task.getResult().child("period").getValue())
                    Log.d("firebase", String.valueOf(task.getResult().getValue()));
                }
            }
        });


        // Inflate the layout for this fragment
        return view;


    }
}