package ru.practicum.shareit.user.storage;

import ru.practicum.shareit.user.model.User;

import java.util.List;
import java.util.Optional;

public interface UserStorage {
    User addUser(User user);

    Optional<User> getUserById(long id);

    List<User> getAllUsers();

    void deleteUserById(long id);
}
