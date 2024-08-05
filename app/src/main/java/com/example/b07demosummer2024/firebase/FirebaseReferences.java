package com.example.b07demosummer2024.firebase;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public final class FirebaseReferences {
    public static final FirebaseDatabase DATABASE = FirebaseDatabase.getInstance(
            "https://login-taam-bo7-default-rtdb.firebaseio.com/");
    public static final DatabaseReference DATABASE_ROOT = DATABASE.getReference();
    public static final FirebaseStorage STORAGE = FirebaseStorage.getInstance(
            "gs://login-taam-bo7.appspot.com");
    public static final StorageReference STORAGE_ROOT = STORAGE.getReference();
}
