package com.example.b07demosummer2024;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.ArrayList;

public class KeywordSearchAdapter extends RecyclerView.Adapter<KeywordSearchAdapter.MyViewHolder> implements Filterable {
    private Context context;
    private List<Item> items;
    private OnItemClickListener listener;
    private List<Item> getItemsFilter;

    @Override
    public Filter getFilter() {
        Filter filter = new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                FilterResults filterResults = new FilterResults();
                if (charSequence == null || charSequence.length() == 0){
                    filterResults.values = getItemsFilter;
                    filterResults.count = getItemsFilter.size();
                }else{
                    String searchStr = charSequence.toString().toLowerCase();
                    List<Item> newItems = new ArrayList<>();
                    for (Item item: getItemsFilter){
                        if (findMatch(item, searchStr)) {
                            newItems.add(item);
                        }
                    }
                    filterResults.values = newItems;
                    filterResults.count = newItems.size();
                }

                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                items = (List<Item>) filterResults.values;
                // update user adapter
                notifyDataSetChanged();
            }
        };
        return filter;
    }

    public interface OnItemClickListener {
        void onItemClick(Item item);
    }
    public KeywordSearchAdapter(Context context, List<Item> items, OnItemClickListener listener) {
        this.context = context;
        this.items = items;
        this.getItemsFilter = items;
        this.listener = listener;
    }
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_card, parent, false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        Item item = items.get(position);
        holder.itemNameText.setText(items.get(position).getName());
        holder.lotNumText.setText(items.get(position).getLotNum());
        holder.periodText.setText(items.get(position).getPeriod());
        holder.categoryText.setText(items.get(position).getCategory());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onItemClick(item);
            }
        });
    }

    @Override
    public int getItemCount() {
        if (items == null) return 0;

        return items.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView itemNameText, lotNumText, periodText, categoryText;
        CardView itemCard;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            this.itemNameText = itemView.findViewById(R.id.itemNameText);
            this.lotNumText = itemView.findViewById(R.id.lotNumText);
            this.periodText = itemView.findViewById(R.id.periodText);
            this.categoryText = itemView.findViewById(R.id.categoryText);
            this.itemCard = itemView.findViewById(R.id.itemCard);

        }
    }

    private boolean findMatch(Item item, String searchStr) {
        return item.getName().toLowerCase().contains(searchStr) ||
                item.getLotNum().contains(searchStr) ||
                item.getPeriod().toLowerCase().contains(searchStr) ||
                item.getCategory().toLowerCase().contains(searchStr);
    }
}