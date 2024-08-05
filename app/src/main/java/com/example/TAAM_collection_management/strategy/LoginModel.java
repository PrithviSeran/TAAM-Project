/*
 * LoginModel.java     1.0     2024/08/04
 */

package com.example.TAAM_collection_management.strategy;

import androidx.annotation.NonNull;

import com.example.TAAM_collection_management.fragments.TAAMSFragment;
import com.example.TAAM_collection_management.interfaces.ILoginPresenter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

/**
 * Class used to authenticate user using <code>FirebaseAuth</code>.
 * <p>
 * Extends <code>TAAMSFragment</code> for storing and manipulation
 * of <code>static</code> user information.
 */
public class LoginModel extends TAAMSFragment {

    private FirebaseAuth mAuth = FirebaseAuth.getInstance();

    /**
     * Method used to authenticate and store user information.
     * <p>
     *
     *
     * @param loginPresenter    <code>ILoginPresenter</code> interface used to authenticate user
     *                          and display failed authentication message.
     * @param email             Email from login attempt.
     * @param password          Password from login attempt.
     */
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
                        loginPresenter.showLoginNotSuccessful();
                    }
                }
            });
    }

}
