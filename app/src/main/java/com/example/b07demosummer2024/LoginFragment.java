package com.example.b07demosummer2024;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

public class LoginFragment extends TAAMSFragment {

    private Button loginButton;     // make local
    private TextView email;
    private TextView password;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_login, container, false);

        loginButton = view.findViewById(R.id.button);

        email = view.findViewById(R.id.usernameEditText);

        password = view.findViewById(R.id.passwordEditText);

        loginButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){

                if (email.getText().toString().trim().isEmpty() || password.getText().toString().trim().isEmpty()){
                    Toast.makeText(getContext(), "Please fill our all fields!", Toast.LENGTH_SHORT).show();
                    return;
                }

                LoginAdmin(email.getText().toString(), password.getText().toString());
            }
        });

        return view;
    }

    private void RegisterAdmin(String name, String email, String password ){
        mAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                        user = mAuth.getCurrentUser();
                        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                            .setDisplayName(name)
                            .build();

                        user.updateProfile(profileUpdates)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (!task.isSuccessful()) {
                                        Toast.makeText(getContext(), "Unexpected Error", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(getContext(), "Account Created", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });

                        loadFragment(new HomeFragment());

                    } else {
                        Toast.makeText(getContext(), "Unexpected Error", Toast.LENGTH_SHORT).show();
                    }
                }
            });
    }

    private void LoginAdmin(String email, String password){

        mAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                        user = mAuth.getCurrentUser();

                        Toast.makeText(getContext(), "Login Successful", Toast.LENGTH_SHORT).show();
                        // Read that having success messages isn't necessary,
                        // or good, discuss if needed later - Burhanuddin

                        loadFragment(new HomeFragment());

                    } else {
                        // If sign in fails, display a message to the user.
                        Toast.makeText(getContext(), "Email or Password incorrect", Toast.LENGTH_SHORT).show();
                    }
                }
            });
    }
}