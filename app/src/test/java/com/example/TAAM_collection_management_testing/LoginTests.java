package com.example.TAAM_collection_management_testing;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;

import com.example.TAAM_collection_management.Login.ILoginPresenter;
import com.example.TAAM_collection_management.Login.ILoginView;
import com.example.TAAM_collection_management.Login.LoginModel;
import com.example.TAAM_collection_management.Login.LoginPresenter;

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
