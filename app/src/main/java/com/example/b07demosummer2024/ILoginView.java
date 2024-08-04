package com.example.b07demosummer2024;

/**
 * Interface with methods used to display different
 * login outcomes.
 */
public interface ILoginView {

    /**
     * Method should be called when a successful login attempt
     * has been made. Resulting action uses the name of the user.
     *
     * @param name      Name of user who successfully logged in.
     */
    void showLoginSuccesful(String name);

    /**
     * Method should be called when an unsuccessful login attempt
     * has been made where all login parameters have been filled.
     */
    void showLoginNotSuccesful();

    /**
     * Method should be called when an unsuccessful login attempt
     * has been made where one or more login parameters are empty.
     */
    void showLoginEmptyFields();

}
