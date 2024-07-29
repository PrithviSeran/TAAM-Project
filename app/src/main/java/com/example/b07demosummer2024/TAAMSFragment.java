package com.example.b07demosummer2024;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public abstract class TAAMSFragment extends Fragment {
    protected FirebaseDatabase database = FirebaseDatabase.getInstance("https://login-taam-bo7-default-rtdb.firebaseio.com/");
    protected DatabaseReference itemsRef;
    protected static FirebaseUser user;
    protected StorageReference storageReference = FirebaseStorage.getInstance("gs://login-taam-bo7.appspot.com").getReference();
    protected StorageReference storageRef;
    protected FirebaseAuth mAuth = FirebaseAuth.getInstance();

    protected void loadFragment(Fragment fragment) {
        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}