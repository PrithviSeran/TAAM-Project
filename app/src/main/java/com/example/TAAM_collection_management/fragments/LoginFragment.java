/*
 * LoginFragment.java     1.0     2024/08/04
 */

package com.example.TAAM_collection_management.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.TAAM_collection_management.strategy.LoginModel;
import com.example.TAAM_collection_management.strategy.LoginPresenter;
import com.example.TAAM_collection_management.interfaces.ILoginView;
import com.example.b07demosummer2024.R;

/**
 * Class used to display <code>activity_login.xml</code>, and
 * compiling proper information for the view.
 * <p>
 * LoginFragment creates a view for the xml file and checks for
 * admin login.
 * <p>
 * Extends <code>TAAMSFragment</code> to use Firebase authentication,
 * and <code>loadFragment</code> method. Implements <code>ILoginView</code>
 * to display all possible login attempt outcomes.
 */
public class LoginFragment extends TAAMSFragment implements ILoginView {

    private TextView email;
    private TextView password;
    private LoginPresenter loginPresenter;

    /**
     * Called to instantiate LoginFragment view.
     * This view is created from the <code>activity_login.xml</code> file.
     * <p>
     * Calls <code>onClick</code> to check for login information filled
     * out in <code>TextVIew</code>'s.
     * <p>
     * If any <code>TextView</code> is empty, returns nothing. Otherwise, calls
     * to authenticate user with <code>LoginPresenter</code>.
     *
     * @param inflater The LayoutInflater object that can be used to inflate
     * any views in LoginFragment,
     * @param container This is the parent view that user_table_view's
     * UI should be attached to. LoginFragment should not add the view itself,
     * but this can be used to generate the LayoutParams of the view.
     * @param savedInstanceState If non-null, LoginFragment is being re-constructed
     * from a previous saved state as given here. savedInstanceState is not used in this instance of
     * <code>onCreateView</code>.
     *
     * @return Return the View for LayoutFragment's UI, or null.
     */
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_login, container, false);
        Button loginButton = view.findViewById(R.id.button);
        LoginModel loginModel = new LoginModel();

        email = view.findViewById(R.id.usernameEditText);
        password = view.findViewById(R.id.passwordEditText);
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
    public void showLoginSuccessful(String name){
        loadFragment(new HomeFragment(name));
        Toast.makeText(getContext(), "Login Successful", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showLoginNotSuccessful(){
        Toast.makeText(getContext(), "Email or Password incorrect", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showLoginEmptyFields(){
        Toast.makeText(getContext(), "Please fill our all fields!", Toast.LENGTH_SHORT).show();
    }
}