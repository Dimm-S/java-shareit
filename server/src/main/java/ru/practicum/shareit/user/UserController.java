package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping(path = "/users")
public class UserController {
    private final UserService userService;

    @PostMapping
    public UserDto addUser(@RequestBody UserDto userDto) {
        log.info("Request endpoint: 'POST /users' (добавление нового пользователя {})", userDto);
        return userService.saveUser(userDto);
    }

    @GetMapping("/{id}")
    public UserDto getUser(@PathVariable long id) {
        log.info("Request endpoint: 'GET /users/{}' (Получение пользователя по id)", id);
        return userService.getUserById(id);
    }

    @GetMapping
    public List<UserDto> getAllUsers(@RequestParam(name = "from", defaultValue = "0") Integer from,
                                     @RequestParam(name = "size", defaultValue = "10") Integer size) {
        log.info("Request endpoint: 'GET /users' (Получение списка всех пользователей)");
        return userService.getAllUsers(from, size);
    }

    @PatchMapping("/{id}")
    public UserDto updateUser(@PathVariable long id,
                              @RequestBody UserDto userDto) {
        log.info("Request endpoint: 'PATCH /users/{}' (Обновление пользователя по id {})", id, userDto);
        return userService.updateUser(id, userDto);
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable long id) {
        log.info("Request endpoint: 'DELETE /users/{}' (Удаление пользователя по id)", id);
        userService.deleteUser(id);
    }
}
