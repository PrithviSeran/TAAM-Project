package com.example.b07demosummer2024;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class CommonUtils {
    public static void logError(@NonNull String type, @Nullable String message) {
        Log.e(type, message == null ? "No message" : message);
    }

    public static void makeAndShowShortToast(String text, Context context) {
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
    }
}
