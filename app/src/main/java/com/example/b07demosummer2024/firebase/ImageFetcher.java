package com.example.b07demosummer2024.firebase;

import android.net.Uri;
import android.util.Log;

import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.Objects;

public class ImageFetcher {
    private static final StorageReference storageRoot = FirebaseStorage.getInstance(
            "gs://login-taam-bo7.appspot.com").getReference();

    public static void getImageUriFromId(String imageId, FirebaseCallback<Uri> callback) {
        requestImageUriFromId(imageId).addOnCompleteListener(
                uriTask -> {
                    if (uriTask.isSuccessful()) {
                        callback.onSuccess(uriTask.getResult());
                    } else {
                        callback.onFailure(Objects.toString(uriTask.getException(),
                                "No message available"));
                    }
                }
        );
    }

    public static Task<Uri> requestImageUriFromId(String imageId) {
        return storageRoot.child(imageId).getDownloadUrl();
    }

    public static void requestImage(String imageId,
                                    Picasso picasso,
                                    Target listener,
                                    int errorId) {
        requestImageUriFromId(imageId)
                .addOnSuccessListener((uri) -> picasso.load(uri).noFade().error(errorId).into(listener))
                .addOnFailureListener(e -> {
                            Log.e("FirebaseError", Objects.requireNonNull(e.getMessage(),
                                    "There was an Firebase exception but message was null"));
                            picasso.load(errorId).into(listener);
                    }
                );
    }
}