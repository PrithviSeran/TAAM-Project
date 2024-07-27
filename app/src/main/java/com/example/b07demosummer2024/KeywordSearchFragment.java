package com.example.b07demosummer2024;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;


public class KeywordSearchFragment extends TAAMSFragment implements KeywordSearchAdapter.OnItemClickListener{

    private RecyclerView recyclerView;
    private KeywordSearchAdapter adapter;
    private ArrayList<Item> items;

    private SearchView searchView;

    private final FirebaseDatabase database = FirebaseDatabase.getInstance(
            "https://login-taam-bo7-default-rtdb.firebaseio.com/");

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_keyword_search, container, false);

        recyclerView = view.findViewById(R.id.rvItems);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        searchView = view.findViewById(R.id.search_view);

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

        // search
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                String searchStr = newText;
                adapter.getFilter().filter(newText);
                return true;
            }
        });



        return view;
    }

    @Override
    public void onItemClick(Item item) {
        loadFragment(new ViewItem(item.getName()));

    }


}