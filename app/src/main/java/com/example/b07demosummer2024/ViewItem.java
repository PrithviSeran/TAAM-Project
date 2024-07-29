package com.example.b07demosummer2024;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.ListResult;
import com.google.firebase.storage.StorageReference;

import org.json.JSONArray;

import java.util.ArrayList;

public class ViewItem extends TAAMSFragment {

    private String item;
    private TextView itemName;
    private TextView itemCategory;
    private TextView itemPeriod;
    private TextView itemDescription;
    private TextView itemLotNum;
    private RecyclerView viewImage;
    private ArrayList<String> images = new ArrayList<String>();
    private ViewImageAdapter adapter = new ViewImageAdapter(images, getContext());

    public ViewItem(String item){
        this.item = item;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_view_popup, container, false);

        itemLotNum =view.findViewById(R.id.LotNum);
        itemName = view.findViewById(R.id.artTitle);
        itemCategory = view.findViewById(R.id.category);
        itemPeriod = view.findViewById(R.id.period);
        itemDescription = view.findViewById(R.id.descriptionText);
        viewImage = view.findViewById(R.id.recycleViewImage);
        viewImage.setLayoutManager(new LinearLayoutManager(getContext()));


        itemsRef = database.getReference("Items/" + item);

        itemsRef.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {

             public void onComplete(@NonNull Task<DataSnapshot> task) {
                 if (!task.isSuccessful()) {
                     Log.e("firebase", "Error getting data", task.getException());
                 }
                 else {
                     itemName.setText(String.valueOf(task.getResult().child("name").getValue()));
                     itemCategory.setText(String.valueOf(task.getResult().child("category").getValue()));
                     itemPeriod.setText(String.valueOf(task.getResult().child("period").getValue()));
                     itemDescription.setText(String.valueOf(task.getResult().child("description").getValue()));
                     itemLotNum.setText(String.valueOf(task.getResult().child("lotNum").getValue()));
                 }
                 storageRef = FirebaseStorage.getInstance("gs://login-taam-bo7.appspot.com").getReference().child(item);

                 storageReference.listAll().addOnSuccessListener(new OnSuccessListener<ListResult>() {
                     @Override
                     public void onSuccess(ListResult listResult) {
                         for (StorageReference fileRef : listResult.getItems()) {
                             fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                 @Override
                                 public void onSuccess(Uri uri) {
                                     images.add(uri.toString());
                                     Log.d("item", uri.toString());
                                 }
                             }).addOnSuccessListener(new OnSuccessListener<Uri>() {
                                 @Override
                                 public void onSuccess(Uri uri) {
                                     viewImage.setAdapter(adapter);
                                 }
                             });
                         }
                     }
                 });
             }
        });

        return view;

    }
}
