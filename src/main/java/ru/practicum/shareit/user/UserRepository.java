package ru.practicum.shareit.user;

import java.util.List;

public interface UserRepository {
    User saveUser(User user);
    User getUserById(long id);
    List<User> getAllUsers();
    User updateUser(long id, User user);
    void deleteUser(long id);
    void checkUserExistence(long id);
}
