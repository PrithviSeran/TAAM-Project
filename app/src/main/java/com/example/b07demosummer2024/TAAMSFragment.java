package com.example.b07demosummer2024;

import android.annotation.SuppressLint;
import android.graphics.Typeface;
import android.view.Gravity;
import android.widget.Button;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.core.content.res.ResourcesCompat;
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

    protected void loadFragment(Fragment fragment) {
        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    protected void setTextViewStyle(TextView textView) {
        Typeface typeface = ResourcesCompat.getFont(requireActivity(), R.font.lato);
        textView.setTypeface(typeface);

        textView.setTextSize(15);

        textView.setGravity(Gravity.CENTER);

        textView.setTextColor(getResources().getColor(R.color.black, null));

        textView.setBackground(getResources().getDrawable(R.drawable.border_square));

        textView.setPadding(0,5,0,5);
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    protected void setButtonStyle(Button button){

        Typeface typeface = ResourcesCompat.getFont(requireActivity(), R.font.lato);
        button.setAllCaps(false);
        button.setBackground(getResources().getDrawable(R.drawable.button_view));

        button.setTextColor(getResources().getColor(R.color.shaded_white, null));

        button.setTextSize(15);

        button.setPadding(0,5,0,5);

        TableRow.LayoutParams params = new TableRow.LayoutParams(150, 80);
        button.setLayoutParams(params);
    }
}