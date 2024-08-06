/*
 * ILoginPresenter.java     1.0     2024/08/07
 */

package com.example.b07demosummer2024;

/**
 * Interface with methods used to authenticate user
 * information and implement actions based on successful or failed
 * attempts.
 */
public interface ILoginPresenter {

    /**
     * Method should be called to check if given <code>email</code> and
     * <code>password</code> matches user information stored in Firebase database.
     *
     * @param email         Email from login attempt.
     * @param password      Password from login attempt.
     */
    void authenticateUser(String email, String password);

    /**
     * Method should be called after <code>authenticateUser</code> is called
     * if a successful login attempt has been made.
     *
     * @param name      Name of successfully logged in user.
     */
    void userAuthenticated(String name);

    /**
     * Method should be called after <code>authenticateUser</code> is called
     * if an unsuccessful login attempt has been made.
     */
    void showLoginNotSuccesful();

}
