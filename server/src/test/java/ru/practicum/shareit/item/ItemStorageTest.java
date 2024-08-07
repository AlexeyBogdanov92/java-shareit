package ru.practicum.shareit.item;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.ItemStorage;
import ru.practicum.shareit.pagination.Paginator;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storager.UserStorage;


import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class ItemStorageTest {
    @Autowired
    private ItemStorage itemStorage;
    @Autowired
    private UserStorage userStorage;

    @BeforeEach
    public void init() {
        User user = User.builder()
                .id(1L)
                .name("UserName")
                .email("email@mail.ru")
                .build();
        userStorage.save(user);

        itemStorage.save(Item.builder()
                .id(1L)
                .name("Brain")
                .description("Amazing brain")
                .owner(user)
                .available(true)
                .requestId(null)
                .lastBooking(null)
                .nextBooking(null)
                .build()
        );
    }

    @AfterEach
    public void clear() {
        itemStorage.deleteAll();
        userStorage.deleteAll();
    }

    @Test
    public void search() {
        List<Item> actualItems = itemStorage.search("brain", Paginator.simplePage(0, 5));

        assertEquals(1, actualItems.size());
        assertEquals("Amazing brain", actualItems.get(0).getDescription());
    }
}