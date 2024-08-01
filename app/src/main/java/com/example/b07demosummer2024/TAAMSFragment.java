package com.example.b07demosummer2024;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.b07demosummer2024.firebase.FirebaseReferences;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.StorageReference;

public class TAAMSFragment extends Fragment {
    protected FirebaseDatabase database = FirebaseReferences.DATABASE;
    protected DatabaseReference itemsRef;
    protected StorageReference storageReference = FirebaseReferences.STORAGE_ROOT;
    protected StorageReference storageRef;
    protected static FirebaseUser user;

    protected void loadFragment(Fragment fragment) {
        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}