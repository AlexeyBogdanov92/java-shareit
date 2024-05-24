package ru.practicum.shareit.item.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.service.ItemServiceImpl;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/items")
public class ItemController {
    private static final String OWNER_ID = "X-Sharer-User-Id";
    private final ItemServiceImpl itemService;

    @Autowired
    public ItemController(ItemServiceImpl itemService) {
        this.itemService = itemService;
    }

    @PostMapping
    public ItemDto createItem(@RequestHeader(OWNER_ID) long userId, @Valid @RequestBody ItemDto item) {
        log.info("Получен запрос от пользователя с id {} на создание вещи", userId);
        return itemService.createItem(userId, item);
    }

    @PatchMapping("/{itemId}")
    public ItemDto updateItem(@RequestHeader(OWNER_ID) long userId,
                              @PathVariable long itemId, @RequestBody ItemDto item) {
        log.info("Получен запрос от пользователя с id {} на обновление вещи", userId);
        return itemService.updateItem(userId, itemId, item);
    }

    @GetMapping("/{itemId}")
    public ItemDto getItem(@PathVariable long itemId) {
        log.info("Получен запрос на получение вещи с id {}", itemId);
        return itemService.getItem(itemId);
    }

    @GetMapping
    public List<ItemDto> getItemsByUser(@RequestHeader(OWNER_ID) long userId) {
        log.info("Получен запрос от пользователя с id {} на получение списка его вещей", userId);
        return itemService.getItemsByUser(userId);
    }

    @GetMapping("/search")
    public List<ItemDto> searchItems(@RequestParam String text) {
        log.info("Получен запрос на поиск '{}' среди вещей", text);
        return itemService.searchItems(text);
    }
}