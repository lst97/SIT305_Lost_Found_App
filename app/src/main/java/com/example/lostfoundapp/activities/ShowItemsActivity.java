package com.example.lostfoundapp.activities;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lostfoundapp.R;
import com.example.lostfoundapp.adapters.ItemRecyclerViewAdapter;
import com.example.lostfoundapp.helpers.DatabaseHelper;
import com.example.lostfoundapp.models.Item;
import com.example.lostfoundapp.repositories.ItemRepository;
import com.example.lostfoundapp.repositories.RepositoryFactory;

import java.util.List;

public class ShowItemsActivity extends AppCompatActivity {
    private List<Item> items;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_items);

        setupItemModel();

        RecyclerView recyclerView = findViewById(R.id.items_recycler_view);
        ItemRecyclerViewAdapter adapter = new ItemRecyclerViewAdapter(this, items);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


    }

    private void setupItemModel() {
        RepositoryFactory itemRepository = ItemRepository.getInstance(new DatabaseHelper(this));
        items = itemRepository.read();
    }
}
