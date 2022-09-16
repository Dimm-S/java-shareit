package ru.practicum.shareit.user;

import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;

public interface UserService {
    UserDto saveUser(UserDto userDto);

    UserDto getUserById(long id);

    List<UserDto> getAllUsers();

    UserDto updateUser(long id, UserDto user);

    void deleteUser(long id);
}
