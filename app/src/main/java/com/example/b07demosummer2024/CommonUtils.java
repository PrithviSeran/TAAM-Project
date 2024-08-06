/*
 *
 */

package com.example.b07demosummer2024;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * Class dedicated to error logging and popup display.
 */
public class CommonUtils {

    /**
     * Logs error with a specific type and message.
     *
     * @param type      Type of error being logged.
     * @param message   Message to write to <code>Log</code>.
     */
    public static void logError(@NonNull String type, @Nullable String message) {
        Log.e(type, message == null ? "No message" : message);
    }

    /**
     * Displays a popup on screen. Displays the <code>text</code> to a
     * <code>Context</code>.
     *
     * @param text          <code>String</code> to be displayed in popup.
     * @param context       <code>Context</code> to which popup is being displayed.
     */
    public static void makeAndShowShortToast(String text, Context context) {
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
    }
}
