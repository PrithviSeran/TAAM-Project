/*
 * HomeFragment.java     1.0     2024/08/07
 */

package com.example.b07demosummer2024;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * Class used to display <code>activity_home_fragment.xml</code>, and
 * compiling proper information for the view.
 * <p>
 * HomeFragment creates a view for the xml file and adds functionality to
 * the different buttons in the xml file.
 * <p>
 * Extends <code>TAAMSFragment</code> to use Firebase user and the
 * <code>loadFragment</code> method.
 */
public class HomeFragment extends TAAMSFragment {

    private TextView title;
    private String name;

    /**
     * Default constructor of <code>HomeFragment</code>.
     */
    public HomeFragment(){
    }

    /**
     * Constructor of <code>HomeFragment</code>. Sets name of logged
     * in user. <code>name</code> is only implemented if user is not null.
     *
     * @param name      Name of logged in user.
     */
    public HomeFragment(String name){
        this.name = name;
    }

    /**
     * Called to instantiate HomeFragment view.
     * This view is created from the <code>activity_home_fragment.xml</code> file.
     * <p>
     * This method has multiple <code>onClick</code> methods for the 4 different
     * buttons being used. When clicked, each one loads its corresponding Fragment.
     *
     *
     * @param inflater The LayoutInflater object that can be used to inflate
     * any views in HomeFragment,
     * @param container This is the parent view that HomeFragment's
     * UI should be attached to. HomeFragment should not add the view itself,
     * but this can be used to generate the LayoutParams of the view.
     * @param savedInstanceState If non-null, HomeFragment is being re-constructed
     * from a previous saved state as given here. savedInstanceState is not used in this instance of
     * <code>onCreateView</code>.
     *
     * @return Return the View for HomeFragment's UI, or null.
     */
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_home_fragment, container, false);

        ImageButton buttonSearch = view.findViewById(R.id.imageButton1);
        ImageButton buttonFeature = view.findViewById(R.id.imageButton2);
        ImageButton buttonAdmin = view.findViewById(R.id.imageButton3);
        ImageButton buttonTable = view.findViewById(R.id.imageButton4);
        TextView loginlogout = view.findViewById(R.id.textView6);


        title = view.findViewById(R.id.homeTittle);
        buttonAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginlogout.setText("Admin Log-in");
                loadFragment(new LoginFragment());
            }
        });

        if (user != null){
            name = user.getDisplayName();

            title.setText("Hello, " + name);
            loginlogout.setText("Admin Log-out");

            // Create a new Button programmatically

            // Set OnClickListener for the Button
            buttonAdmin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    user = null;
                    loadFragment(new HomeFragment());
                }
            });

        }
        else{
            buttonAdmin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    loadFragment(new LoginFragment());

                }
            });
        }


        buttonSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadFragment(new SearchFragment());
            }
        });

        buttonFeature.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.addCategory(Intent.CATEGORY_BROWSABLE);
                intent.setData(Uri.parse("https://taam.ca/index.php/en/"));
                startActivity(intent);
//                loadFragment(new ReportFragment());
            }
        });

        buttonTable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (user != null){
                    loadFragment(new AdminTableFragment());
                }
                else{
                    loadFragment(new UserTableFragment());
                }
            }
        });

        return view;
    }
}


