package com.example.b07demosummer2024.firebase;

import android.graphics.Bitmap;
import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.io.IOException;
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

    public static class FetchBitmapFromUri implements OnCompleteListener<Uri> {
        private Bitmap result;

        @Override
        public void onComplete(@NonNull Task<Uri> task) {
            if (task.isSuccessful()) {
                try {
                    result = Picasso.get().load(task.getResult()).get();
                    System.out.println("hello");
                } catch (IOException e) {
                    result = null;
                }
            } else {
                result = null;
            }
        }

        public Bitmap getResult() {
            return result;
        }
    }

}
