package ru.practicum.shareit.item.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.storage.BookingStorage;
import ru.practicum.shareit.exception.BookingStatusException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.storage.ItemStorage;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.comment.*;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoShort;
import ru.practicum.shareit.pagination.Paginator;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.storage.ItemRequestStorage;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storager.UserStorage;


import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ItemServiceImpl implements ItemService {
    private final ItemStorage itemStorage;
    private final UserStorage userStorage;
    private final BookingStorage bookingStorage;
    private final CommentStorage commentStorage;
    private final ItemRequestStorage requestStorage;

    @Autowired
    public ItemServiceImpl(ItemStorage itemStorage, UserStorage userStorage,
                           BookingStorage bookingStorage, CommentStorage commentStorage, ItemRequestStorage itemRequestStorage) {
        this.itemStorage = itemStorage;
        this.userStorage = userStorage;
        this.bookingStorage = bookingStorage;
        this.commentStorage = commentStorage;
        this.requestStorage = itemRequestStorage;
    }

    @Transactional
    public ItemDtoShort createItem(long userId, ItemDtoShort item) {
        User user = checkUserId(userId);
        if (item.getRequestId() != null) {
            checkRequestId(item.getRequestId());
        }
        Item createdItem = itemStorage.save(ItemMapper.toItem(item, user));
        log.info("Пользователь с id {} создал вещь {}", userId, createdItem);
        return ItemMapper.toItemDtoShort(createdItem);
    }

    @Transactional
    public ItemDtoShort updateItem(long userId, long itemId, ItemDtoShort item) {
        Item expectedItem = checkItemId(itemId);
        if (expectedItem.getOwner().getId() != userId) {
            throw new NotFoundException(
                    String.format("Пользователь с id %d не является владельцем вещи с id %d", userId, itemId));
        }
        if (item.getName() != null && !item.getName().isBlank()) {
            expectedItem.setName(item.getName());
        }
        if (item.getDescription() != null && !item.getDescription().isBlank()) {
            expectedItem.setDescription(item.getDescription());
        }
        if (item.getAvailable() != null) {
            expectedItem.setAvailable(item.getAvailable());
        }
        itemStorage.save(expectedItem);
        log.info("Пользователь с id {} обновил вещь с id {}", userId, itemId);
        return ItemMapper.toItemDtoShort(expectedItem);
    }

    @Transactional(readOnly = true)
    public ItemDto getItem(long itemId, long userId) {
        Item item = checkItemId(itemId);
        List<CommentDtoResponse> comments = commentStorage.findAllByItemId(itemId)
                .stream().map(CommentMapper::toCommentDtoResponse).collect(Collectors.toList());
        if (item.getOwner().getId() == userId) {
            log.info("Получена вещь с id {}", itemId);
            return ItemMapper.toItemDto(item, comments);
        }
        item.setLastBooking(null);
        item.setNextBooking(null);
        log.info("Получена вещь с id {}", itemId);
        return ItemMapper.toItemDto(item, comments);
    }

    @Transactional(readOnly = true)
    public List<ItemDto> getItemsByUser(long userId, Integer from, Integer size) {
        Map<Long, Item> itemMap = itemStorage.findAllItemsByOwnerId(userId, Paginator.simplePage(from, size))
                .stream()
                .collect(Collectors.toMap(Item::getId, Function.identity()));

        Map<Long, List<Comment>> comments = commentStorage.findAllByItemIdIn(itemMap.keySet())
                .stream()
                .collect(Collectors.groupingBy(Comment::getItemId));

        List<ItemDto> itemsByUser = itemMap.values()
                .stream()
                .map(i -> addComments(i, comments.getOrDefault(i.getId(), List.of())))
                .collect(Collectors.toList());

        log.info("Получен список из {} вещей для пользователя с id {}", itemsByUser.size(), userId);
        return itemsByUser;
    }

    @Transactional(readOnly = true)
    public List<ItemDtoShort> searchItems(String query, Integer from, Integer size) {
        if (query.isBlank()) {
            log.info("Получен список из 0 вещей по запросу '{}'", query);
            return Collections.emptyList();
        }
        List<ItemDtoShort> foundItems = itemStorage.search(query, Paginator.simplePage(from, size)).stream()
                .map(ItemMapper::toItemDtoShort).collect(Collectors.toList());
        log.info("Получен список из {} вещей по запросу '{}'", foundItems.size(), query);
        return foundItems;
    }

    @Transactional
    public CommentDtoResponse createComment(long itemId, long userId, CommentDtoRequest commentDto) {
        User user = checkUserId(userId);
        if (!bookingStorage.existsByItemIdAndBookerIdAndEndBefore(itemId, userId, LocalDateTime.now())) {
            throw new BookingStatusException("Нельзя оставить комментарий");
        }
        Comment comment = commentStorage.save(CommentMapper.toComment(commentDto, user, itemId));
        log.info("Получен комментарий от пользователя {}", userId);
        return CommentMapper.toCommentDtoResponse(comment);
    }

    private User checkUserId(long userId) {
        return userStorage.findById(userId).orElseThrow(() ->
                new NotFoundException(String.format("Пользователь с id %d не существует", userId)));
    }

    private Item checkItemId(long itemId) {
        return itemStorage.findById(itemId).orElseThrow(() ->
                new NotFoundException(String.format("Вещь с id %d не существует", itemId)));
    }

    private ItemDto addComments(Item item, List<Comment> comments) {
        List<CommentDtoResponse> commentList = comments
                .stream().map(CommentMapper::toCommentDtoResponse).collect(Collectors.toList());
        return ItemMapper.toItemDto(item, commentList);
    }

    private ItemRequest checkRequestId(long reqId) {
        return requestStorage.findById(reqId).orElseThrow(() ->
                new NotFoundException(String.format("Запрос с id %d не существует", reqId)));
    }
}