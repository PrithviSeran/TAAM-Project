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

public class user_table_view extends TAAMSFragment implements ViewItemsTable{

    private TableRow newRow;
    private TextView lotText, nameText;
    private Button viewItem;
    private TableLayout mainTable;
    private Button searchItem;


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


