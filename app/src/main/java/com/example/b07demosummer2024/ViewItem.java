package com.example.b07demosummer2024;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;

import org.json.JSONArray;

public class ViewItem extends TAAMSFragment {

    private String item;
    private TextView itemName;
    private TextView itemCategory;
    private TextView itemPeriod;
    private TextView itemDescription;


    public ViewItem(String item){
        this.item = item;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_view_popup, container, false);

        itemName = view.findViewById(R.id.artTitle);
        itemCategory = view.findViewById(R.id.category);
        itemPeriod = view.findViewById(R.id.period);
        itemDescription = view.findViewById(R.id.descriptionText);
        itemName.setText(item);

        itemsRef = database.getReference("Items/" + item);

        itemsRef.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {

             public void onComplete(@NonNull Task<DataSnapshot> task) {
                 if (!task.isSuccessful()) {
                     Log.e("firebase", "Error getting data", task.getException());
                 }
                 else {

                     System.out.println(String.valueOf(task.getResult()));

                     itemCategory.setText(String.valueOf(task.getResult().child("category").getValue()));
                     itemPeriod.setText(String.valueOf(task.getResult().child("period").getValue()));
                     itemDescription.setText(String.valueOf(task.getResult().child("description").getValue()));

                 }
             }
        });

        return view;

    }
}
