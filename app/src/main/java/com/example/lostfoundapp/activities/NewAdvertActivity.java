package com.example.lostfoundapp.activities;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;

import androidx.appcompat.app.AppCompatActivity;

import com.example.lostfoundapp.R;
import com.example.lostfoundapp.helpers.DatabaseHelper;
import com.example.lostfoundapp.models.Item;
import com.example.lostfoundapp.repositories.ItemRepository;

import java.text.SimpleDateFormat;
import java.util.Date;

public class NewAdvertActivity extends AppCompatActivity {
    private RadioGroup radioGroup;
    private EditText nameEditText;
    private EditText descriptionEditText;
    private EditText locationEditText;
    private EditText dateEditText;
    private EditText phoneEditText;
    private Button saveButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_advert);

        radioGroup = findViewById(R.id.post_type_radio_group);
        nameEditText = findViewById(R.id.name_input);
        descriptionEditText = findViewById(R.id.description_input);
        locationEditText = findViewById(R.id.location_input);
        dateEditText = findViewById(R.id.date_input);
        phoneEditText = findViewById(R.id.phone_input);
        saveButton = findViewById(R.id.save_btn);

        saveButton.setOnClickListener(v -> {

            // data validation
            if (nameEditText.getText().toString().isEmpty()) {
                nameEditText.setError("Name is required");
                return;
            }
            if (descriptionEditText.getText().toString().isEmpty()) {
                descriptionEditText.setError("Description is required");
                return;
            }
            if (locationEditText.getText().toString().isEmpty()) {
                locationEditText.setError("Location is required");
                return;
            }
            if (dateEditText.getText().toString().isEmpty()) {
                dateEditText.setError("Date is required");
                return;
            }
            if (phoneEditText.getText().toString().isEmpty()) {
                phoneEditText.setError("Phone is required");
                return;
            }

            String name = nameEditText.getText().toString();
            String description = descriptionEditText.getText().toString();
            String location = locationEditText.getText().toString();
            String dateString = dateEditText.getText().toString();
            String phone = phoneEditText.getText().toString();
            String type = radioGroup.getCheckedRadioButtonId() == R.id.lost_radio ? "lost" : "found";

            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            Date date = null;
            try {
                date = dateFormat.parse(dateString);
            } catch (Exception e) {
                e.printStackTrace();
            }
            Item item = new Item(0, name, description, location, date, phone, type);

            ItemRepository itemRepository = ItemRepository.getInstance(new DatabaseHelper(this));
            itemRepository.create(item);

            finish();
        });
    }
}
