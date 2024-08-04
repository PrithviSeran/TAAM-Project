package com.example.b07demosummer2024;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;

import java.util.HashMap;
import java.util.Map;

/**
 * ViewItemsTable is the interface used by all classes that are
 * which allows them to display items in a table format.
 *
 */
public interface ViewItemsTable {
    /**
     * Called to display items from database in a table view.
     * Upon successful task completion, the desired table view is implement.
     * Upon a failed task, a popup is displayed and exception is logged.
     *
     */
    void displayItems();

}
