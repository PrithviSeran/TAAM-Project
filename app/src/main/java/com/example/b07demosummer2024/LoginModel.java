package com.example.b07demosummer2024;

import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginModel extends TAAMSFragment{

    private FirebaseAuth mAuth = FirebaseAuth.getInstance();

    //public LoginModel(FirebaseAuth mAuth){
      //  super.mAuth = mAuth;
    //}

    public void authenticateUser(ILoginPresenter loginPresenter, String email, String password){
        mAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                        user = mAuth.getCurrentUser();
                        loginPresenter.userAuthenticated(user.getDisplayName());

                    } else {
                        // If sign in fails, display a message to the user.
                        loginPresenter.showLoginNotSuccesful();
                    }
                }
            });
    }

}
