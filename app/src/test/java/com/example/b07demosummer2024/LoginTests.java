package com.example.b07demosummer2024;//package com.example.b07demosummer2024;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;

import android.content.Context;

import com.example.b07demosummer2024.ILoginPresenter;
import com.example.b07demosummer2024.ILoginView;
import com.example.b07demosummer2024.LoginModel;
import com.example.b07demosummer2024.LoginPresenter;
import com.google.firebase.auth.FirebaseAuth;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class LoginTests {

    @Mock
    public ILoginView loginView;

    @Mock
    public LoginModel loginModel;

    @Test
    public void testOnLoginAuthenticate() {

        ILoginPresenter presenter = new LoginPresenter(loginView, loginModel);
        //presenter.isUsername = true;
        String username = "testuser";
        String password = "testpass";

        presenter.authenticateUser(username, password);

        verify(loginModel).authenticateUser(presenter, username, password);
    }

    @Test
    public void testOnLoginAuthenticatedUser() {

        ILoginPresenter presenter = new LoginPresenter(loginView, loginModel);
        //presenter.isUsername = true;
        String name = "John Doe";

        presenter.userAuthenticated(name);

        verify(loginView).showLoginSuccesful(name);
    }

    @Test
    public void testOnLoginNotSuccesful() {

        ILoginPresenter presenter = new LoginPresenter(loginView, loginModel);

        presenter.showLoginNotSuccesful();

        verify(loginView).showLoginNotSuccesful();
    }

}
