package com.example.b07demosummer2024;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.b07demosummer2024.firebase.FirebaseCallback;
import com.example.b07demosummer2024.firebase.ImageFetcher;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.squareup.picasso.Picasso;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.ListResult;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;
import org.json.JSONArray;

import java.util.ArrayList;

public class ViewItem extends TAAMSFragment {

    private String itemID;
    private Item item = null;
    private String identifier;
    private TextView itemName;
    private TextView itemCategory;
    private TextView itemPeriod;
    private TextView itemDescription;
    private TextView itemLotNum;
    private ImageView viewItemPic;
    private VideoView viewItemVid;
    private Uri imageURI;
  

    private ImageView viewImage;

    public ViewItem(String itemID){
        this.itemID = itemID;
    }

    // CHANGE: Added item constructor because SearchResult calls this class, and it already
    // has the item info, we do not need to query Firebase again
    public ViewItem(Item item) {
        this.item = item;
        this.itemID = item.getLotNum();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_view_popup, container, false);

        itemLotNum = view.findViewById(R.id.LotNum);
        itemName = view.findViewById(R.id.artTitle);
        itemCategory = view.findViewById(R.id.category);
        itemPeriod = view.findViewById(R.id.period);
        itemDescription = view.findViewById(R.id.descriptionText);
        viewItemPic = view.findViewById(R.id.itemViewImage);
        viewItemVid = view.findViewById(R.id.videoView2);
        popUp();

        return view;

    }

    private void popUp() {
        itemsRef = database.getReference("Items/" + itemID);

        itemsRef.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {

            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e("firebase", "Error getting data", task.getException());
                    Toast.makeText(getContext(), "Error getting data", Toast.LENGTH_SHORT).show();
                } else {
                    itemName.setText(String.valueOf(task.getResult().child("name").getValue()));
                    itemCategory.setText(String.valueOf(task.getResult().child("category").getValue()));
                    itemPeriod.setText(String.valueOf(task.getResult().child("period").getValue()));
                    itemDescription.setText(String.valueOf(task.getResult().child("description").getValue()));
                    itemLotNum.setText(String.valueOf(task.getResult().child("lotNum").getValue()));
                }

                retrieveFromStorage();
            }
        });

    }

    private void retrieveFromStorage() {

        StorageReference fileRef = storageReference.child(identifier);
        fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {

            @Override
            public void onSuccess(Uri uri) {

                imageURI = uri;
                fileRef.getMetadata().addOnSuccessListener(new OnSuccessListener<StorageMetadata>() {

                    @Override
                    public void onSuccess(StorageMetadata storageMetadata) {
                        String mimeType = storageMetadata.getContentType();

                        if (mimeType != null) {
                            if (mimeType.startsWith("image/")) {
                                viewItemVid.setVisibility(View.INVISIBLE);
                                viewItemPic.setVisibility(View.VISIBLE);
                                Picasso.get().load(uri).into(viewItemPic);
                            }
                            else if (mimeType.startsWith("video/")) {
                                viewItemPic.setVisibility(View.INVISIBLE);
                                viewItemVid.setVisibility(View.VISIBLE);
                                viewItemVid.setVideoURI(uri);
                                viewItemVid.setOnPreparedListener(mp -> {
                                    mp.setLooping(true);
                                    viewItemVid.start();
                                });
                            }
                            else {
                                Toast.makeText(getContext(), "Unsupported file type", Toast.LENGTH_SHORT).show();
                            }
                        }
                        else {
                            Toast.makeText(getContext(), "Unable to determine file type", Toast.LENGTH_SHORT).show();

                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getContext(), "No Content Associated", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e("firebase", "Error retrieving data from storage", e);
                Toast.makeText(getContext(), "No Content Associated", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
