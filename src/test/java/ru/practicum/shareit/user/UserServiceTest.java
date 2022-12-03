package ru.practicum.shareit.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.WARN)
public class UserServiceTest {
    private UserServiceImpl userService;
    @Mock
    private UserRepository userRepository;
    @Mock
    private UserMapping userMapping;

    @BeforeEach
    void setup() { //инициализация
        userService = new UserServiceImpl(userRepository, userMapping);
    }

    @Test
    void getAllUsersTest() {
        List<User> user = new ArrayList<>(Collections.singleton(new User(1L, "name", "name@email")));
        final PageImpl<User> userPage = new PageImpl<>(user);
         user.add(new User(1L, "name", "name@email"));
        when(userRepository.findAll(PageRequest.ofSize(10)))
                .thenReturn(userPage);

        final List<UserDto> userDtos = userService.getAllUsers(1, 10);

        assertNotNull(userDtos);
        assertEquals(1, userDtos.size());
        assertEquals(userMapping.mapToUserDto(user.get(0)), userDtos.get(0));
    }

    @Test
    void updateUserTest() {
        User userForUpdate = new User(1L, "name", "name@email");
        UserDto userDto1 = new UserDto(1L, "newName", "newEmail@email");

        when(userRepository.existsById(any()))
                .thenReturn(true);
        when(userRepository.getReferenceById(anyLong()))
                .thenReturn(userForUpdate);
        when(userRepository.save(any())).thenReturn(userForUpdate);
        doCallRealMethod().when(userMapping).mapToUserDto(any());

        final UserDto updatedUser1 = userService.updateUser(1, userDto1);
        assertEquals(userDto1, updatedUser1);

    }

    @Test
    void updateUserOnlyNameTest() {
        User userForUpdate = new User(1L, "name", "name@email");
        UserDto userDto2 = new UserDto(1L, "newName", null);

        when(userRepository.existsById(any()))
                .thenReturn(true);
        when(userRepository.getReferenceById(anyLong()))
                .thenReturn(userForUpdate);
        when(userRepository.save(any())).thenReturn(userForUpdate);
        doCallRealMethod().when(userMapping).mapToUserDto(any());

        final UserDto updatedUser2 = userService.updateUser(1, userDto2);
        assertEquals(new UserDto(1L, "newName", "name@email"), updatedUser2);
    }

    @Test
    void updateUserWrongEmailTest() {
        UserDto userDto3 = new UserDto(1L, "newName", "wrongemail");
        when(userRepository.findAll())
                .thenReturn(new ArrayList<>(Collections.singleton(
                        new User(1L, "newName", "wrongemail"))));

        assertThrows(ValidationException.class, () -> {
            userService.updateUser(1L, userDto3);
        }, "User not found");
    }

    @Test
    void saveUserTest() {
        UserDto userDto = new UserDto(1L, "name", "email@email");
        User user = new User(1L, "name", "email@email");

        doCallRealMethod().when(userMapping).mapToUser(any(UserDto.class));
        when(userRepository.save(any(User.class)))
                .thenReturn(user);
        doCallRealMethod().when(userMapping).mapToUserDto(any(User.class));

        final UserDto savedUser = userService.saveUser(userDto);

        assertEquals(userDto, savedUser);
    }

    @Test
    void getUserByIdTest() {
        UserDto userDto = new UserDto(1L, "name", "email@email");
        User user = new User(1L, "name", "email@email");

        when(userRepository.existsById(any()))
                .thenReturn(true);
        when(userRepository.getReferenceById(anyLong()))
                .thenReturn(user);
        doCallRealMethod().when(userMapping).mapToUserDto(any(User.class));

        final UserDto gettedUser = userService.getUserById(1);

        assertEquals(userDto, gettedUser);
    }

    @Test
    void getUserByWrongIdTest() {
        UserDto userDto = new UserDto(1L, "name", "email@email");
        User user = new User(1L, "name", "email@email");

        when(userRepository.existsById(any()))
                .thenReturn(false);

        assertThrows(NotFoundException.class, () -> {
            userService.getUserById(1);
        }, "User not found");
    }

    @Test
    void deleteUserTest() {
        doNothing().when(userRepository).deleteById(any());
        userService.deleteUser(1);
    }
}
