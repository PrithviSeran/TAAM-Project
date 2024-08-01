package com.example.b07demosummer2024;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class HomeFragment extends TAAMSFragment {

    private TextView title;
    private String name;

    public HomeFragment(){
    }

    public HomeFragment(String name){
        this.name = name;
    }

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

        buttonSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadFragment(new SearchFragment());
            }
        });

        buttonFeature.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadFragment(new AdminVisuals());
            }
        });



        buttonTable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (user != null){
                    loadFragment(new AdminVisuals());
                }
                else{
                    loadFragment(new user_table_view());
                }
            }
        });

        return view;
    }
}


