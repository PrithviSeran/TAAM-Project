package com.example.b07demosummer2024;

public interface ILoginPresenter {

    void authenticateUser(String email, String password);
    void userAuthenticated(String name);
    void showLoginNotSuccesful();

}
