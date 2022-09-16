package ru.practicum.shareit.user;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.exception.NotFoundException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class UserRepositoryImplInMemory implements UserRepository {
    private final Map<Long, User> users = new HashMap<>();
    long id = 0;

    @Override
    public User saveUser(User user) {
        if (user.getId() == null) {
            user.setId(++id);
        }
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public User getUserById(long id) {
        return users.get(id);
    }

    @Override
    public List<User> getAllUsers() {
        return new ArrayList<>(users.values());
    }

    @Override
    public User updateUser(long id, User user) {
        if (user.getName() != null) {
            users.get(id).setName(user.getName());
        }
        if (user.getEmail() != null) {
            users.get(id).setEmail(user.getEmail());
        }
        return users.get(id);
    }

    @Override
    public void deleteUser(long id) {
        users.remove(id);
    }

    @Override
    public void checkUserExistence(long id) {
        if (!users.containsKey(id)) {
            throw new NotFoundException("User with id=" + id + " not found");
        }
    }
}
