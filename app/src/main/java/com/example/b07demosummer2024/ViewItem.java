package com.example.b07demosummer2024;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.b07demosummer2024.firebase.FirebaseCallback;
import com.example.b07demosummer2024.firebase.ImageFetcher;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.squareup.picasso.Picasso;

public class ViewItem extends TAAMSFragment {

    private String itemID;
    private Item item = null;
    private TextView itemName;
    private TextView itemCategory;
    private TextView itemPeriod;
    private TextView itemDescription;
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
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_view_popup, container, false);

        itemName = view.findViewById(R.id.artTitle);
        itemCategory = view.findViewById(R.id.category);
        itemPeriod = view.findViewById(R.id.period);
        itemDescription = view.findViewById(R.id.descriptionText);
        viewImage = view.findViewById(R.id.itemImage);
        itemName.setText(itemID);

        if (item == null) {
            itemsRef = database.getReference("Items/" + itemID);

            itemsRef.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {

                public void onComplete(@NonNull Task<DataSnapshot> task) {
                    if (!task.isSuccessful()) {
                        Log.e("firebase", "Error getting data", task.getException());
                    } else {
                        itemCategory.setText(String.valueOf(task.getResult().child("category").getValue()));
                        itemPeriod.setText(String.valueOf(task.getResult().child("period").getValue()));
                        itemDescription.setText(String.valueOf(task.getResult().child("description").getValue()));
                    }
                }
            });
        } else {
            itemCategory.setText(item.getCategory());
            itemPeriod.setText(item.getPeriod());
            itemDescription.setText(item.getDescription());
        }
        Picasso picasso = Picasso.get();
        ImageFetcher.getImageUriFromId(itemID, new FirebaseCallback<Uri>() {
            @Override
            public void onSuccess(Uri results) {
                picasso.load(results)
                        .fit() // if we have this, image fits the imageView, but borders cannot be seen
                        .error(R.drawable.image_not_found)
                        .into(viewImage);
            }

            @Override
            public void onFailure(String message) {
                picasso.load(R.drawable.image_not_found).into(viewImage);
            }
        });
        return view;
    }
}
