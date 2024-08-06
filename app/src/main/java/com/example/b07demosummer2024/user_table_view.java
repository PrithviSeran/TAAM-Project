/*
 * UserTableViewFragment.java     1.0     2024/08/07
 */

package com.example.b07demosummer2024;

import android.os.Bundle;

import androidx.annotation.NonNull;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.b07demosummer2024.firebase.FirebaseCallback;
import com.example.b07demosummer2024.firebase.ItemFetcher;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;

import java.util.List;

/**
 * Class used to display <code>fragment_user_table_view.xml</code>, and
 * compiling proper information for the view.
 * <p>
 * user_table_view creates a view for the xml file and display the list
 * of all items under the "Items" reference stored in Firebase Database.
 * It also adds functionality to a button which allows for searching
 * through displayed items, through <code>SearchFragment</code>.
 * <p>
 * Extends <code>TAAMSFragment</code> to use Firebase database. Implements
 * <code>ViewItemsTable</code> to use interface <code>displayItems</code> method.
 */
public class user_table_view extends TAAMSFragment implements ViewItemsTable{

    private TableRow newRow;
    private TextView lotText, nameText;
    private Button viewItem;
    private TableLayout mainTable;
    private Button searchItem;

    /**
     * Called to instantiate user_table_view view.
     * This view is created from the <code>fragment_user_table_view.xml</code> file.
     * <p>
     * Calls <code>onClick</code> for <code>searchItem</code> and is used
     * to call <code>loadFragment</code> for <code>SearchFragment</code>.
     *
     * @param inflater The LayoutInflater object that can be used to inflate
     * any views in user_table_view,
     * @param container This is the parent view that user_table_view's
     * UI should be attached to. user_table_view should not add the view itself,
     * but this can be used to generate the LayoutParams of the view.
     * @param savedInstanceState If non-null, user_table_view is being re-constructed
     * from a previous saved state as given here. savedInstanceState is not used in this instance of
     * <code>onCreateView</code>.
     *
     * @return Return the View for user_table_view's UI, or null.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_user_table_view, container, false);

        mainTable = view.findViewById(R.id.tableLayout);

        searchItem = view.findViewById(R.id.searchButton);

        searchItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadFragment(new SearchFragment());
            }
        });

        displayItems();

        // Inflate the layout for this fragment
        return view;
    }


    @Override
    public void displayItems(){
        ItemFetcher.getAllItems(new FirebaseCallback<List<Item>>() {
            @Override
            public void onFirebaseSuccess(List<Item> results) {
                for (Item item: results) {
                    mainTable.addView(getItemTableRow(item));
                }
            }

            @Override
            public void onFirebaseFailure(String message) {
                CommonUtils.logError("FirebaseError", message);
                CommonUtils.makeAndShowShortToast("Display Error", getContext());
            }
        });
    }

    private void initializeTextView(TextView textView, String initialText, float initialWeight) {
        textView.setText(initialText);
        textView.setSingleLine(true);
        textView.setEllipsize(TextUtils.TruncateAt.END);
        setTextViewStyle(textView);
        textView.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, initialWeight));
    }

    /**
     * Reads the information associated with a given item and converts it
     * into a <code>TableRow</code>.
     * <p>
     * Contains an <code>onClickListener</code> to call the <code>ViewItem</code>
     * class to display item on screen.
     *
     * @param item      Information of this parameter is used in the <code>TableRow</code>
     * @return          Returns <code>TableRow</code> the lot number and name of the item.
     */
    protected TableRow getItemTableRow(Item item) {
        newRow = new TableRow(getActivity());
        newRow.setPadding(0,10,0,10);

        lotText = new TextView(getActivity());
        initializeTextView(lotText, item.getLotNum(), 1.5f);

        newRow.addView(lotText);

        nameText = new TextView(getActivity());
        initializeTextView(nameText, item.getName(), 3f);

        newRow.addView(nameText);

        viewItem = new Button(getActivity());
        viewItem.setText("View");
        setButtonStyle(viewItem);

        viewItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle the button click
                loadFragment(new ViewItem(item));
            }
        });

        newRow.addView(viewItem);

        return newRow;
    }
}


