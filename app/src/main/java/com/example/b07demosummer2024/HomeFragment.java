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

import com.example.b07demosummer2024.report.ReportFragment;

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
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.addCategory(Intent.CATEGORY_BROWSABLE);
                intent.setData(Uri.parse("https://taam.ca/index.php/en/"));
                startActivity(intent);
            }
        });

        buttonAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadFragment(new LoginFragment());

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


