package com.example.lostfoundapp.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Button;

import com.example.lostfoundapp.R;
import com.example.lostfoundapp.helpers.DatabaseHelper;
import com.example.lostfoundapp.repositories.ItemRepository;

public class MainActivity extends AppCompatActivity {

    private Button newAdvertButton;
    private Button showItemsButton;
    private Button showOnMapButton;

    private void initViews(){
        newAdvertButton = findViewById(R.id.main_new_advert);
        showItemsButton = findViewById(R.id.main_show_all);
        showOnMapButton = findViewById(R.id.main_show_on_map);
    }

    private void initListeners(){
        newAdvertButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, NewAdvertActivity.class);
            startActivity(intent);
        });

        showItemsButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, ShowItemsActivity.class);
            startActivity(intent);
        });

        showOnMapButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, ShowOnMapActivity.class);
            startActivity(intent);
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // drop the table
        DatabaseHelper databaseHelper = new DatabaseHelper(this);
//        SQLiteDatabase db = databaseHelper.getWritableDatabase();
//        db.execSQL("DROP TABLE IF EXISTS items");
//        db.close();

        initViews();
        initListeners();

    }
}