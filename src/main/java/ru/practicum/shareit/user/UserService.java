package ru.practicum.shareit.user;

import org.springframework.data.domain.PageRequest;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;

public interface UserService {
    UserDto saveUser(UserDto userDto);

    UserDto getUserById(long id);

    List<UserDto> getAllUsers(PageRequest pageRequest);

    UserDto updateUser(long id, UserDto user);

    void deleteUser(long id);

    void checkUserExistence(long id);
}
