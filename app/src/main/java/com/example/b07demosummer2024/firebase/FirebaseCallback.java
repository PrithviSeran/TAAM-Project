/*
 * FirebaseCallback.java     1.0     2024/08/07
 */

package com.example.b07demosummer2024.firebase;

public interface FirebaseCallback<T> {
    void onFirebaseSuccess(T results);
    void onFirebaseFailure(String message);
}
