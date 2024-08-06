/*
 * TAAMSFragment.java     1.0     2024/08/07
 */

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

import com.example.b07demosummer2024.firebase.FirebaseReferences;
import com.google.firebase.auth.FirebaseAuth;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.StorageReference;

/**
 * TAAMSFragment is the abstract parent class for almost all Fragment classes in the
 * <code>com.example.TAAM_collection_management</code> package that require displaying
 * an xml file.
 * <p>
 * TAAMSFragment allows for subclasses to load other fragments as needed and
 * standardizes text and buttons generated in certain views.
 * <p>
 * Includes references to Firebase Database and storage reference.
 */
public class TAAMSFragment extends Fragment {
    protected FirebaseDatabase database = FirebaseReferences.DATABASE;
    protected DatabaseReference itemsRef;
    protected StorageReference storageReference = FirebaseReferences.STORAGE_ROOT;
    protected StorageReference storageRef;
    protected static FirebaseUser user;

    /**
     * This method transitions screen from the current fragment to <code>fragment</code>.
     * Using the <code>FragmentTransaction</code> class, it replaces the current displayed
     * fragment with parameter <code>fragment</code>.
     * <p>
     * <code>fragment</code> should not be null. Generally, <code>fragment</code> is defined
     * in the method call.
     *
     * @param fragment      fragment is the new instantiated class of the corresponding
     *                      xml file. fragment is always instantiated in the method call.
     */
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

        textView.setPadding(4,5,4,5);
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    protected void setButtonStyle(Button button){

        Typeface typeface = ResourcesCompat.getFont(requireActivity(), R.font.lato);
        button.setAllCaps(false);
        button.setBackground(getResources().getDrawable(R.drawable.button_view));

        button.setTextColor(getResources().getColor(R.color.shaded_white, null));

        button.setTextSize(15);

        button.setPadding(4,5,4,5);

        TableRow.LayoutParams params = new TableRow.LayoutParams(150, 80);
        button.setLayoutParams(params);
    }
}