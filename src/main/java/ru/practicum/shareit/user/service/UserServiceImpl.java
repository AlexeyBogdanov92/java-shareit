package ru.practicum.shareit.user.service;

import io.vavr.control.Try;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.storage.UserStorage;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Service
public class UserServiceImpl implements UserService {
    private final UserStorage userStorage;

    @Autowired
    public UserServiceImpl(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    @Transactional
    public UserDto createUser(UserDto userDto) {
        User createdUser = userStorage.save(UserMapper.toUser(userDto));
        log.info("Создан пользователь {}", createdUser);
        return UserMapper.toUserDto(createdUser);
    }

    @Transactional
    public UserDto updateUserById(long userId, UserDto user) {
        User expectedUser = checkUserId(userId);

        updateUserFields(user, expectedUser);

        return Try.of(() -> userStorage.save(expectedUser))
                .onSuccess(savedUser -> log.info("Пользователь с id {} успешно обновлен", userId))
                .onFailure(e -> log.error("Ошибка при обновлении пользователя с id {}: {}", userId, e.getMessage()))
                .map(savedUser -> UserMapper.toUserDto(expectedUser))
                .getOrElseThrow(e -> new RuntimeException("Не удалось обновить пользователя", e));
    }

    @Transactional(readOnly = true)
    public UserDto getUserById(long id) {
        User findedUser = checkUserId(id);
        log.info("Получен пользователь с id {}", id);
        return UserMapper.toUserDto(findedUser);
    }

    @Transactional(readOnly = true)
    public List<UserDto> getAllUsers() {
        List<UserDto> users = userStorage.findAll().stream()
                .map(UserMapper::toUserDto).collect(Collectors.toList());
        log.info("Получен список из {} пользователей", users.size());
        return users;
    }

    @Transactional
    public void deleteUserById(long id) {
        checkUserId(id);
        userStorage.deleteById(id);
        log.info("Удален пользователь с id {}", id);
    }

    private User checkUserId(long userId) {
        return userStorage.findById(userId).orElseThrow(() ->
                new NotFoundException(String.format("Пользователь с id %d не существует", userId)));
    }

    private void updateUserFields(UserDto source, User target) {
        if (source.getName() != null && !source.getName().isBlank()) {
            target.setName(source.getName());
        }
        if (source.getEmail() != null && !source.getEmail().isBlank()) {
            target.setEmail(source.getEmail());
        }
    }
}