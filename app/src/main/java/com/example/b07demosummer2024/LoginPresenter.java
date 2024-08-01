package com.example.b07demosummer2024;

public class LoginPresenter implements ILoginPresenter{

    ILoginView loginView;
    LoginModel loginModel;

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
