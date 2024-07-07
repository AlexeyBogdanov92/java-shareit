package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;

public interface UserService {
    UserDto createUser(UserDto userDto);

    UserDto updateUserById(long userId, UserDto user);

    UserDto getUserById(long id);

    List<UserDto> getAllUsers();

    void deleteUserById(long id);

}