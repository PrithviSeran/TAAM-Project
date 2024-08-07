/*
 * SearchResultFragment.java     1.0     2024/08/07
 */

package com.example.b07demosummer2024;

import android.os.Bundle;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.List;

/**
 * Class used to create display for the <code>fragment_user_table_view.xml</code>
 * file after a call for <code>SearchFragment</code> has been made and a successful
 * search has been completed.
 * <p>
 * Extends <code>TAAMSFragment</code> to use <code>loadFragment</code> and standardized
 * button and text. Implements <code>ViewItemsTable</code> to use interface
 * <code>displayItems</code> method.
 */
public class SearchResultFragment extends TAAMSFragment implements ViewItemsTable {
    private TableRow tableRow1;

    private TextView textView1, textView2;
    private Button viewItem;
    private List<Item> items;
    private TableLayout tableLayout1;

    /**
     * Constructor for <code>SearchResultFragment</code>.
     * Requires list of <code>Items</code> to be called.
     *
     * @param items     List of items created from searching.
     */
    public SearchResultFragment(List<Item> items) {
        this.items = items;
    }

    /**
     * Called to have SearchResultFragment instantiate its user interface view.
     * This view is created from the <code>fragment_user_table_view.xml</code> file.
     *
     * @param inflater The LayoutInflater object that can be used to inflate
     * any views in SearchResultFragment,
     * @param container If non-null, this is the parent view that the fragment's
     * UI should be attached to.  SearchResultFragment should not add the view itself,
     * but this can be used to generate the LayoutParams of the view.
     * @param savedInstanceState If non-null, SearchResultFragment is being re-constructed
     * from a previous saved state as given here. savedInstanceState is not used in this
     * onCreateView method.
     *
     * @return Return the View for SearchResultFragment's UI, or null.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_user_table_view, container, false);
        tableLayout1 = view.findViewById(R.id.tableLayout);
        Button searchButton = view.findViewById(R.id.searchButton);
        searchButton.setVisibility(View.GONE);

        TextView title = view.findViewById(R.id.userTableTitle);
        title.setText("Search Results");

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

        for (Item item: items) {
            tableRow1 = new TableRow(getActivity());

            tableRow1.setPadding(0,10,0,10);

            textView1 = new TextView(getActivity());
            initializeTextView(textView1, item.getLotNum(), 1.5f);
            tableRow1.addView(textView1);

            textView2 = new TextView(getActivity());
            initializeTextView(textView2, item.getName(), 3f);
            tableRow1.addView(textView2);

            viewItem = new Button(getActivity());
            viewItem.setText("View");
            setButtonStyle(viewItem);

            viewItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Handle the button click
                    loadFragment(new ViewItemFragment(item));
                }
            });

            tableRow1.addView(viewItem);
            tableLayout1.addView(tableRow1);
        }

    }
    private void initializeTextView(TextView textView, String initialText, float initialWeight) {
        textView.setText(initialText);
        textView.setSingleLine(true);
        textView.setEllipsize(TextUtils.TruncateAt.END);
        setTextViewStyle(textView);
        textView.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, initialWeight));
    }


}


