package com.example.b07demosummer2024;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;

import com.example.TAAM_collection_management.interfaces.ILoginPresenter;
import com.example.TAAM_collection_management.interfaces.ILoginView;
import com.example.TAAM_collection_management.strategy.LoginModel;
import com.example.TAAM_collection_management.strategy.LoginPresenter;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
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

        verify(loginView).showLoginSuccessful(name);
    }

    @Test
    public void testOnLoginNotSuccesful() {

        ILoginPresenter presenter = new LoginPresenter(loginView, loginModel);

        presenter.showLoginNotSuccessful();

        verify(loginView).showLoginNotSuccessful();
    }

}
