package com.example.lostfoundapp.repositories;

import static android.database.sqlite.SQLiteDatabase.openOrCreateDatabase;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.lostfoundapp.helpers.DatabaseHelper;
import com.example.lostfoundapp.models.Item;

import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ItemRepository implements RepositoryFactory{
    private DatabaseHelper databaseHelper;
    private SQLiteDatabase database;
    private static ItemRepository instance;

    private ItemRepository(DatabaseHelper dbHelper){
        databaseHelper = dbHelper;
        database = databaseHelper.getWritableDatabase();
    }

    public static ItemRepository getInstance(DatabaseHelper dbHelper){
        if (instance == null){
            instance = new ItemRepository(dbHelper);
        }
        return instance;
    }

    public static ItemRepository getInstance(){
        if (instance == null){
            throw new NullPointerException("ItemRepository is not initialized");
        }
        return instance;
    }


    @Override
    public void create(Item item) {
        // security issue?
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        String dateString = dateFormat.format(item.getDate());

        database.execSQL("INSERT INTO items (name, phone, description, date, location, postType, latitude, longitude) VALUES ('" + item.getName() + "', '" + item.getPhone() + "', '" + item.getDescription() + "', '" + dateString + "', '" + item.getLocation() + "', '" + item.getPostType() + "' , '" + item.getLatitude() + "' , '" + item.getLongitude() + "')");
    }

    @Override
    public void update(Item item) {
        // current not used
        throw new UnsupportedOperationException();
    }

    @Override
    public void delete(int id) {
        database.execSQL("DELETE FROM items WHERE id = '" + id + "'");
    }

    @Override
    public List<Item> read() {
        List<Item> items = new ArrayList<>();
        // read all items from database
        String[] columns = {
                "id",
                "name",
                "phone",
                "description",
                "date",
                "location",
                "postType",
                "latitude",
                "longitude"
        };
        Cursor cursor = database.query("items", columns, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                // Retrieve the values from the cursor and create a new Item object
                int id = cursor.getInt(0);
                String name = cursor.getString(1);
                String phone = cursor.getString(2);
                String description = cursor.getString(3);
                String dateString = cursor.getString(4);
                String location = cursor.getString(5);
                String postType = cursor.getString(6);
                double latitude = cursor.getDouble(7);
                double longitude = cursor.getDouble(8);

                SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                Date date = null;
                try {
                    date = dateFormat.parse(dateString);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                Item item = new Item(id, name, phone, description, date, location, postType, latitude, longitude);
                // Add the item to the list
                items.add(item);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return items;
    }

    public void delete(String name) {
        database.execSQL("DELETE FROM items WHERE name = '" + name + "'");
    }


    @Override
    public Item read(int id) {
        String[] columns = {
                "name",
                "phone",
                "description",
                "date",
                "location",
                "postType",
                "latitude",
                "longitude"
        };
        String selection = "id = ?";
        String[] selectionArgs = { String.valueOf(id) };

        Cursor cursor = database.query("items", columns, selection, selectionArgs, null, null, null);
        // Check if the cursor has any rows
        if (cursor.moveToFirst()) {
            // Retrieve the values from the cursor and create a new Item object
            String name = cursor.getString(1);
            String phone = cursor.getString(2);
            String description = cursor.getString(3);
            String dateString = cursor.getString(4);
            String location = cursor.getString(5);
            String postType = cursor.getString(6);
            double latitude = cursor.getDouble(7);
            double longitude = cursor.getDouble(8);

            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            Date date = null;
            try {
                date = dateFormat.parse(dateString);
            } catch (Exception e) {
                e.printStackTrace();
            }

            Item item = new Item(id, name, phone, description, date, location, postType, latitude, longitude);
            cursor.close();

            return item;
        }

        cursor.close();
        return null;
    }

    @Override
    public Item read(String itemName) {
        String[] columns = {
                "id",
                "phone",
                "description",
                "date",
                "location",
                "postType",
                "latitude",
                "longitude"
        };
        String selection = "name = ?";
        String[] selectionArgs = { itemName };

        Cursor cursor = database.query("items", columns, selection, selectionArgs, null, null, null);
        // Check if the cursor has any rows
        if (cursor.moveToFirst()) {
            // Retrieve the values from the cursor and create a new Item object
            int id = cursor.getInt(0);
            String phone = cursor.getString(1);
            String description = cursor.getString(2);
            String dateString = cursor.getString(3);
            String location = cursor.getString(4);
            String postType = cursor.getString(5);
            double latitude = cursor.getDouble(6);
            double longitude = cursor.getDouble(7);

            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            Date date = null;
            try {
                date = dateFormat.parse(dateString);
            } catch (Exception e) {
                e.printStackTrace();
            }

            Item item = new Item(id, itemName, phone, description, date, location, postType, latitude, longitude);
            cursor.close();

            return item;
        }

        cursor.close();
        return null;
    }

    public void close(){
        database.close();
    }
}
