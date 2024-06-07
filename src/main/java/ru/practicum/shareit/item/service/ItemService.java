package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.comment.CommentDtoRequest;
import ru.practicum.shareit.item.comment.CommentDtoResponse;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoShort;

import java.util.List;

public interface ItemService {
    ItemDtoShort createItem(long userId, ItemDtoShort item);

    ItemDtoShort updateItem(long userId, long itemId, ItemDtoShort item);

    ItemDto getItem(long itemId, long userId);

    List<ItemDto> getItemsByUser(long userId);

    List<ItemDtoShort> searchItems(String query);

    CommentDtoResponse createComment(long itemId, long userId, CommentDtoRequest commentDto);
}
