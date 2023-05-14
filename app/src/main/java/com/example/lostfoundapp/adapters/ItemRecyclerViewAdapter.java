package com.example.lostfoundapp.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lostfoundapp.R;
import com.example.lostfoundapp.activities.RemoveItemActivity;
import com.example.lostfoundapp.activities.ShowItemsActivity;
import com.example.lostfoundapp.models.Item;
import com.example.lostfoundapp.repositories.RepositoryFactory;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.List;

public class ItemRecyclerViewAdapter extends RecyclerView.Adapter<ItemRecyclerViewAdapter.ViewHolder> {
    Context context;
    List<Item> items;
    RepositoryFactory itemRepository;

    public ItemRecyclerViewAdapter(Context context, List<Item> items) {
        this.context = context;
        this.items = items;
    }

    @NonNull
    @Override
    public ItemRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.recycler_item, parent, false);
        return new ItemRecyclerViewAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemRecyclerViewAdapter.ViewHolder holder, int position) {
        holder.titleView.setText(items.get(position).getName());

        // set card on click listener
        holder.itemView.setOnClickListener(v -> {
            // get current position
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

            Item item = items.get(position);
            Intent intent = new Intent(context, RemoveItemActivity.class);

            intent.putExtra("itemId", item.getId());
            intent.putExtra("itemName", item.getName());
            intent.putExtra("itemDescription", item.getDescription());
            intent.putExtra("itemLocation", item.getLocation());
            intent.putExtra("itemDate", dateFormat.format(item.getDate()));
            intent.putExtra("itemPostType", item.getPostType());
            intent.putExtra("itemPhone", item.getPhone());

            context.startActivity(intent);
            ((Activity)context).finish();
        });

    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView titleView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            titleView = itemView.findViewById(R.id.title_recycler_label);
        }
    }
}
