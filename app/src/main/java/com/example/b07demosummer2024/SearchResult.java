package com.example.b07demosummer2024;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

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

public class SearchResult extends user_table_view {
    private TableRow tableRow1;

    private TextView textView1, textView2, textView3, textView4;
    private CheckBox checkBox;
    private Button viewItem;
    private Button backButton;
    private List<Item> items;

    public SearchResult(List<Item> items) {
        this.items = items;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_user_table_view, container, false);

        TableLayout tableLayout1 = view.findViewById(R.id.tableLayout);

        for (Item item: items) {
            tableRow1 = new TableRow(getActivity());
            checkBox = new CheckBox(getActivity());

            tableRow1.addView(checkBox);

            textView1 = new TextView(getActivity());
            textView1.setText(item.getLotNum());
            setTextViewStyle(textView1);
            textView1.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1.5f));
            tableRow1.addView(textView1);

            textView2 = new TextView(getActivity());
            textView2.setText(item.getName());
            textView2.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 3f));
            setTextViewStyle(textView2);
            tableRow1.addView(textView2);

            viewItem = new Button(getActivity());
            viewItem.setText("View");
            setButtonStyle(viewItem);

            viewItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Handle the button click
                    loadFragment(new ViewItem(item.getName()));
                }
            });

            tableRow1.addView(viewItem);
            tableLayout1.addView(tableRow1);
        }

        return view;
    }

    private void loadFragment(Fragment fragment) {
        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

}


