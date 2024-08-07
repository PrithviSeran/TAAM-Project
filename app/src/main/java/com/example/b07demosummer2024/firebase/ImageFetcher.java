/*
 * ImageFetcher.java     1.0     2024/08/07
 */

package com.example.b07demosummer2024.firebase;

import android.graphics.drawable.Drawable;
import android.net.Uri;

import com.example.b07demosummer2024.CommonUtils;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.Objects;

/**
 * Class dedicated to fetching images from Firebase database.
 *
 */
public class ImageFetcher {
    private static final StorageReference storageRoot = FirebaseStorage.getInstance(
            "gs://login-taam-bo7.appspot.com").getReference();

    public static void getImageUriFromId(String imageId, FirebaseCallback<Uri> callback) {
        requestImageUriFromId(imageId).addOnCompleteListener(
                uriTask -> {
                    if (uriTask.isSuccessful()) {
                        callback.onFirebaseSuccess(uriTask.getResult());
                    } else {
                        callback.onFirebaseFailure(Objects.toString(uriTask.getException(),
                                "No message available"));
                    }
                }
        );
    }

    /**
     * Returns <code>Task</code> of attempting to retrieve image from
     * Firebase.
     *
     * @param imageId       Id of image being queried on Firebase.
     * @return              Task of retrieving image by id from Firebase.
     */
    public static Task<Uri> requestImageUriFromId(String imageId) {
        return getStorageReferenceAtId(imageId).getDownloadUrl();
    }

    /**
     * Requests an image from Firebase database to be downloaded and
     * used by <code>picasso</code> and <code>Bitmap</code>.
     *
     * @param imageId           Id of image being queried on Firebase
     * @param picasso           instance of <code>Picasso</code> class being used to
     *                          download image.
     * @param listener          Target location of image to be loaded onto.
     * @param errorDrawable     Error image to be drawn if any exceptions occur.
     */
    public static void requestImage(String imageId,
                                    Picasso picasso,
                                    Target listener,
                                    Drawable errorDrawable) {

        getStorageReferenceAtId(imageId)
                .getMetadata()
                .addOnSuccessListener((metadata) -> {
                    String contentType = metadata.getContentType();
                    String errorMessage;

                    if (contentType == null) {
                        errorMessage = String.format("Content type for image id %s is null", imageId);
                        listener.onBitmapFailed(new NullPointerException(errorMessage), errorDrawable);
                    } else if (!contentType.contains("image")) {
                        errorMessage = String.format("Content type for image id %s is %s, not image", imageId, contentType);
                        listener.onBitmapFailed(new RuntimeException(errorMessage), errorDrawable);
                    } else {
                        requestImageUriFromId(imageId)
                                .addOnSuccessListener((uri) -> {
                                    picasso.load(uri).noFade().error(errorDrawable).into(listener);
                                }).addOnFailureListener(e -> {
                                        CommonUtils.logError("FirebaseError", e.getMessage());
                                        listener.onBitmapFailed(e, errorDrawable);
                                    }
                                );
                    }
                }).addOnFailureListener(e -> {
                    CommonUtils.logError("FirebaseError", e.getMessage());
                    listener.onBitmapFailed(e, errorDrawable);
                });
    }

    private static StorageReference getStorageReferenceAtId(String imageId) {
        return storageRoot.child(imageId);
    }

}