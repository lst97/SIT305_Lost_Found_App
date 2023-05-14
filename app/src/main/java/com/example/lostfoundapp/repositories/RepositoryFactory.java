package com.example.lostfoundapp.repositories;

import com.example.lostfoundapp.models.Item;

import java.util.List;

public interface RepositoryFactory {
    void create(Item item);
    void update(Item item);
    void delete(int id);
    List<Item> read();
    Item read(int id);
    Item read(String itemName);
}
