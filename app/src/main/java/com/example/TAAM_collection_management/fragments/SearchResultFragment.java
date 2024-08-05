/*
 * SearchResultFragment.java     1.0     2024/08/04
 */

package com.example.TAAM_collection_management.fragments;

import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.example.TAAM_collection_management.strategy.Item;
import com.example.TAAM_collection_management.interfaces.ViewItemsTable;
import com.example.b07demosummer2024.R;

import java.util.List;

/**
 * Class used to create display for the <code>fragment_user_table_view.xml</code>
 * file after a call for <code>SearchFragment</code> has been made and a successful
 * search has been completed.
 * <p>
 * Extends <code>TAAMSFragment</code> to use <code>loadFragment</code> and standardized
 * button and text. Implements <code>ViewItemsTable</code> to use interface
 * <code>displayItems</code> method.
 *
 */
public class SearchResultFragment extends TAAMSFragment implements ViewItemsTable {

    private List<Item> items;
    private TableLayout tableLayout;

    /**
     * Constructor for <code>SearchResult</code>.
     * Requires list of <code>Items</code> to be called.
     *
     * @param items     List of items created from searching.
     */
    public SearchResultFragment(List<Item> items) {
        this.items = items;
    }

    /**
     * Called to have SearchResult instantiate its user interface view.
     * This view is created from the <code>fragment_user_table_view.xml</code> file.
     *
     * @param inflater The LayoutInflater object that can be used to inflate
     * any views in SearchResult,
     * @param container If non-null, this is the parent view that the fragment's
     * UI should be attached to.  SearchResult should not add the view itself,
     * but this can be used to generate the LayoutParams of the view.
     * @param savedInstanceState If non-null, SearchResult is being re-constructed
     * from a previous saved state as given here. savedInstanceState is not used in this
     * onCreateView method.
     *
     * @return Return the View for SearchResult's UI, or null.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_user_table_view, container, false);
        tableLayout = view.findViewById(R.id.tableLayout);
        displayItems();
        return view;
    }

    /**
     * Called to display items from database in a table view.
     * Since <code>items</code> is always non empty and contains correct data
     * from database, when called, displayItems will always return immediately
     * and successfully.
     *<p>
     * method has an <code>onClick</code> method, which will call
     * <code>loadFragment</code> on <code>ViewItem</code> when the <code>viewItem</code>
     * button is clicked.
     */
    @Override
    public void displayItems(){

        TableRow tableRow;
        TextView lotNumTextView;
        TextView nameTextView;
        CheckBox checkBox;
        Button viewItemButton;


        for (Item item: items) {
            tableRow = new TableRow(getActivity());
            checkBox = new CheckBox(getActivity());

            tableRow.addView(checkBox);

            lotNumTextView = new TextView(getActivity());
            lotNumTextView.setText(item.getLotNum());
            setTextViewStyle(lotNumTextView);
            lotNumTextView.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1.5f));
            tableRow.addView(lotNumTextView);

            nameTextView = new TextView(getActivity());
            nameTextView.setText(item.getName());
            nameTextView.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 3f));
            setTextViewStyle(nameTextView);
            tableRow.addView(nameTextView);

            viewItemButton = new Button(getActivity());
            viewItemButton.setText("View");
            setButtonStyle(viewItemButton);

            viewItemButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Handle the button click
                    loadFragment(new ViewItemFragment(item.getLotNum()));
                }
            });

            tableRow.addView(viewItemButton);
            tableLayout.addView(tableRow);
        }

    }

}


