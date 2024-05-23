package ru.practicum.shareit.item.storage;

import ru.practicum.shareit.item.model.Item;

import java.util.List;
import java.util.Optional;

public interface ItemStorage {
    Item addItem(long userId, Item item);

    Optional<Item> getItemById(long id);

    List<Item> getItemsByUserId(long userId);

    List<Item> searchItems(String query);
}
