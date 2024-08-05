package com.example.b07demosummer2024.firebase;

public interface FirebaseCallback<T> {
    void onSuccess(T results);
    void onFailure(String message);
}
