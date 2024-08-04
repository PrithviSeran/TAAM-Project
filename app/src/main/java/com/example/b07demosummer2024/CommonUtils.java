package com.example.b07demosummer2024;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class CommonUtils {
    public static void logError(@NonNull String type, @Nullable String message) {
        Log.e(type, message == null ? "No message" : message);
    }
}
