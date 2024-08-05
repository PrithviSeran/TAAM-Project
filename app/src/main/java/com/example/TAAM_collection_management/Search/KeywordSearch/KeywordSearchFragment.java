/*
 * KeywordSearchFragment.java     1.0     2024/08/04
 */

package com.example.TAAM_collection_management.Search.KeywordSearch;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.TAAM_collection_management.Objects.TAAMSFragment;
import com.example.TAAM_collection_management.ItemView.ViewItemFragment;
import com.example.TAAM_collection_management.Objects.Item;
import com.example.b07demosummer2024.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

/**
 * Class used to display <code>fragment_keyword_search.xml</code>, and
 * compiling proper information for the view.
 * <p>
 * KeywordSearchFragment creates a view for the xml file and display the list
 * of all items filtered by a keyword input by user, and moves to <code>ViewItem</code>
 * if a displayed item is clicked.
 * <p>
 * Extends <code>TAAMSFragment</code> to use Firebase database. Implements
 * <code>KeywordSearchAdapter.OnItemClickListener</code> to use interface
 * <code>onItemClick</code> method.
 */
public class KeywordSearchFragment extends TAAMSFragment implements KeywordSearchAdapter.OnItemClickListener{

    private KeywordSearchAdapter adapter;
    private ArrayList<Item> items;
    private final FirebaseDatabase database = FirebaseDatabase.getInstance(
            "https://login-taam-bo7-default-rtdb.firebaseio.com/");


    /**
     * Called to instantiate KeywordSearchFragment view.
     * This view is created from the <code>fragment_keyword_search.xml</code> file.
     * <p>
     * Method searches for items in database using <code>KeywordSearchAdapter</code>.
     *
     * @param inflater The LayoutInflater object that can be used to inflate
     * any views in KeywordSearchFragment,
     * @param container This is the parent view that KeywordSearchFragment's
     * UI should be attached to. KeywordSearchFragment should not add the view itself,
     * but this can be used to generate the LayoutParams of the view.
     * @param savedInstanceState If non-null, KeywordSearchFragment is being re-constructed
     * from a previous saved state as given here. savedInstanceState is not used in this instance of
     * <code>onCreateView</code>.
     *
     * @return Return the View for KeywordSearchFragment's UI, or null.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_keyword_search, container, false);
        RecyclerView recyclerView = view.findViewById(R.id.rvItems);
        SearchView searchView = view.findViewById(R.id.search_view);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        items = new ArrayList<>();
        adapter = new KeywordSearchAdapter(view.getContext(), items, this::onItemClick);

        // prevent soft keyboard from moving up layout
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        recyclerView.setAdapter(adapter);

        itemsRef = database.getReference("Items");
        itemsRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DataSnapshot itemsList = task.getResult();
                for (DataSnapshot snapshot : itemsList.getChildren()) {
                    items.add(snapshot.getValue(Item.class));
                }
                adapter.notifyDataSetChanged();
            } else {
                Toast.makeText(getContext(), "Failed to access database", Toast.LENGTH_SHORT).show();
            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                return true;
            }
        });
        return view;
    }

    @Override
    public void onItemClick(Item item) {
        loadFragment(new ViewItemFragment(item.getLotNum()));
    }
}