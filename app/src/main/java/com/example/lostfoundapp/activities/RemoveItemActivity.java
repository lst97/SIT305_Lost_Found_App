package com.example.lostfoundapp.activities;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.lostfoundapp.R;
import com.example.lostfoundapp.helpers.DatabaseHelper;
import com.example.lostfoundapp.models.Item;
import com.example.lostfoundapp.repositories.ItemRepository;
import com.example.lostfoundapp.repositories.RepositoryFactory;

import java.text.SimpleDateFormat;
import java.util.Date;

public class RemoveItemActivity extends AppCompatActivity {
    private TextView nameTextView;
    private TextView dateTextView;
    private TextView locationTextView;
    private Button removeButton;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remove_item);

        nameTextView = findViewById(R.id.remove_name);
        dateTextView = findViewById(R.id.remove_date);
        locationTextView = findViewById(R.id.remove_location);
        removeButton = findViewById(R.id.remove_remove_btn);

        // get value from intent
        String id = getIntent().getStringExtra("itemId");
        String name = getIntent().getStringExtra("itemName");
        String description = getIntent().getStringExtra("itemDescription");
        String location = getIntent().getStringExtra("itemLocation");
        String dateString = getIntent().getStringExtra("itemDate");
        String phone = getIntent().getStringExtra("itemPhone");
        String type = getIntent().getStringExtra("itemPostType");

        // set value to edit text
        nameTextView.setText(name);
        // convert String to Date
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Date date = null;
        try {
            date = dateFormat.parse(dateString);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // calculate date difference
        long diff = new Date().getTime() - date.getTime();
        long diffDays = diff / (24 * 60 * 60 * 1000);
        String newDateString = diffDays + " days ago";

        dateTextView.setText(newDateString);
        locationTextView.setText("At" + location);

        removeButton.setOnClickListener(v -> {
            RepositoryFactory itemRepository = ItemRepository.getInstance(new DatabaseHelper(this));
            Item item = itemRepository.read(name);

            itemRepository.delete(item.getId());
            finish();

        });
    }
}
