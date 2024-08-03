package com.example.b07demosummer2024;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;

import java.util.ArrayList;
import java.util.List;


public class KeywordSearchFragment extends TAAMSFragment implements KeywordSearchAdapter.OnItemClickListener{

    private RecyclerView recyclerView;
    private KeywordSearchAdapter adapter;
    private List<Item> items;
    private SearchView searchView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_keyword_search, container, false);

        initializeViews(view);
        setAdapter(view);
        populateSearch();
        setUpSearchListener();

        return view;
    }

    private void initializeViews(View view){
        recyclerView = view.findViewById(R.id.rvItems);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        searchView = view.findViewById(R.id.search_view);
    }

    private void setAdapter(View view) {
        items = new ArrayList<>();
        adapter = new KeywordSearchAdapter(view.getContext(), items, this::onItemClick);
        recyclerView.setAdapter(adapter);
    }

    private void populateSearch() {
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
    }

    private void setUpSearchListener(){
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                String searchStr = newText;
                adapter.getFilter().filter(newText);
                return true;
            }
        });
    }

    @Override
    public void onItemClick(Item item) {
        loadFragment(new ViewItem(item.getLotNum()));
    }
}