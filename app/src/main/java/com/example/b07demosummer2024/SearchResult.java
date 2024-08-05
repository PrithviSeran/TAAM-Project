package com.example.b07demosummer2024;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

public class SearchResult extends TAAMSFragment implements ViewItemsTable {
    private TableRow tableRow1;

    private TextView textView1, textView2;
    private Button viewItem;
    private List<Item> items;
    private TableLayout tableLayout1;

    public SearchResult(List<Item> items) {
        this.items = items;
    }

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
                    loadFragment(new ViewItem(item));
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


