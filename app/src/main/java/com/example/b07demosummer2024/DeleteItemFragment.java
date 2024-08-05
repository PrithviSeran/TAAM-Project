package com.example.b07demosummer2024;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TextView;
import android.graphics.Typeface;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Map;
import java.util.Objects;

public class DeleteItemFragment extends TAAMSFragment implements ViewItemsTable {

    private ArrayList<String> itemsToDelete;
    private ArrayList<String> itemsToDeleteLotNum;
    private TextView individualItems;
    private Button confirmDelete;
    private Button goBack;
    private LinearLayout tableLayout1;

    public DeleteItemFragment(ArrayList<String> itemsToDelete, ArrayList<String> itemsToDeleteLotNum){
        this.itemsToDelete = itemsToDelete;
        this.itemsToDeleteLotNum = itemsToDeleteLotNum;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_delete_item, container, false);

        confirmDelete = view.findViewById(R.id.proceedWithDelete);
        goBack = view.findViewById(R.id.goBackItems);

        tableLayout1 = view.findViewById(R.id.linearLayoutDelete);

        displayItems();

        confirmDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v){ deleteItems(); }
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
        if (fragmentManager != null) {
            fragmentManager.popBackStack();
        }
    }

    @Override
    public void displayItems(){
        for (String item : itemsToDelete){
            System.out.println(item);
            individualItems = new TextView(getActivity());
            individualItems.setText(item);
            setTextViewStyle(individualItems);
            tableLayout1.addView(individualItems);
        }
    }

    private void deleteItems(){
        System.out.println(itemsToDeleteLotNum);
        for (String lotNum : itemsToDeleteLotNum) {
            itemsRef = database.getReference("Items/" + lotNum);
            itemsRef.removeValue();

            storageRef = storageReference.child(lotNum);
            storageRef.delete();
        }
        goPreviousFragment();
    }
}
