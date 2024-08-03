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

public class LoginFragment extends TAAMSFragment implements ILoginView {

    private Button loginButton;     // make local
    private TextView email;
    private TextView password;
    private LoginPresenter loginPresenter;
    private LoginModel loginModel;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_login, container, false);

        loginButton = view.findViewById(R.id.button);

        email = view.findViewById(R.id.usernameEditText);

        password = view.findViewById(R.id.passwordEditText);

        loginModel = new LoginModel();

        loginPresenter = new LoginPresenter(this, loginModel) ;

        loginButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){

                if (email.getText().toString().trim().isEmpty() || password.getText().toString().trim().isEmpty()){
                    showLoginEmptyFields();
                    return;
                }

                loginPresenter.authenticateUser(email.getText().toString(), password.getText().toString());
            }
        });

        return view;
    }

    @Override
    public void showLoginSuccesful(String name){

        loadFragment(new HomeFragment(name));

        Toast.makeText(getContext(), "Login Successful", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showLoginNotSuccesful(){
        Toast.makeText(getContext(), "Email or Password incorrect", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showLoginEmptyFields(){
        Toast.makeText(getContext(), "Please fill our all fields!", Toast.LENGTH_SHORT).show();

    }
}