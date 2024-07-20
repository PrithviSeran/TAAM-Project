package com.example.b07demosummer2024;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Map;

public class DeleteItemFragment extends Fragment {

    FirebaseDatabase database = FirebaseDatabase.getInstance("https://login-taam-bo7-default-rtdb.firebaseio.com/");
    private DatabaseReference itemsRef;
    private ArrayList<String> itemsToDelete;
    private TextView individualItems;

    private Button confirmDelete;
    private Button goBack;

    public DeleteItemFragment(ArrayList<String> itemsToDelete){
        this.itemsToDelete = itemsToDelete;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_delete_item, container, false);

        confirmDelete = view.findViewById(R.id.proceedWithDelete);
        goBack = view.findViewById(R.id.goBackItems);

        LinearLayout tableLayout1 = view.findViewById(R.id.linearLayoutDelete);

        for (String item : itemsToDelete){
            individualItems = new TextView(getActivity());
            individualItems.setText(item);

            tableLayout1.addView(individualItems);
        }

        confirmDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v){
                    for (String item : itemsToDelete) {
                        itemsRef = database.getReference("TestBranch/" + item);
                        //itemsRef.removeValue();
                    }
                    goPreviousFragment();
                }
            }
        );

        goBack.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                goPreviousFragment();
            }
        });

        return view;
    }

    public void goPreviousFragment(){
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        System.out.println(fragmentManager);
        if (fragmentManager != null) {
            fragmentManager.popBackStack();
        }
    }
}