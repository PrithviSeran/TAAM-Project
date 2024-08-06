/*
 * LoginPresenter.java     1.0     2024/08/07
 */

package com.example.b07demosummer2024;

/**
 * Class used to authenticate user information and implement actions
 * based on successful or failed attempts.
 * <p>
 * Implements <code>ILoginPresenter</code> to authenticate user and
 * act on successful and failed attempts.
 */
public class LoginPresenter implements ILoginPresenter{

    ILoginView loginView;
    LoginModel loginModel;

    /**
     * Constructor for <code>LoginPresenter</code>. assigns values
     * to <code>loginView</code> and <code>loginModel</code>.
     *
     * @param loginView     Interface for displaying outcomes of login attempts.
     * @param loginModel    Class used to authenticate login information.
     */
    public LoginPresenter(ILoginView loginView, LoginModel loginModel){
        this.loginView = loginView;
        this.loginModel = loginModel;
    }

    @Override
    public void authenticateUser(String name, String password){
        loginModel.authenticateUser(this, name, password);
    }

    @Override
    public void userAuthenticated(String name){
        loginView.showLoginSuccesful(name);
    }


    @Override
    public void showLoginNotSuccesful(){
        loginView.showLoginNotSuccesful();
    }
}
