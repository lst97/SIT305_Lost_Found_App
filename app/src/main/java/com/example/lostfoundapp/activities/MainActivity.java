package com.example.lostfoundapp.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import com.example.lostfoundapp.R;
import com.example.lostfoundapp.helpers.DatabaseHelper;
import com.example.lostfoundapp.repositories.ItemRepository;

public class MainActivity extends AppCompatActivity {

    private Button newAdvertButton;
    private Button showItemsButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // drop the table
        DatabaseHelper databaseHelper = new DatabaseHelper(this);

        newAdvertButton = findViewById(R.id.main_new_advert);
        showItemsButton = findViewById(R.id.main_show_all);

        newAdvertButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, NewAdvertActivity.class);
            startActivity(intent);
        });

        showItemsButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, ShowItemsActivity.class);
            startActivity(intent);
        });

    }
}