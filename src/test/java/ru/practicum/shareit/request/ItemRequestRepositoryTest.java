package ru.practicum.shareit.request;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTest
public class ItemRequestRepositoryTest {
    @Autowired
    private ItemRequestRepository requestRepository;
    @Autowired
    private UserRepository userRepository;

    private User user;
    private ItemRequest itemRequest;

    @Test
    void findAllByRequesterId() {
        user = userRepository.save(new User(1L, "user1", "user1@email"));
        itemRequest = requestRepository.save(new ItemRequest(1L, "desc1", 1L, null));

        final List<ItemRequest> requests = requestRepository.findAllByRequesterId(1L);

        assertNotNull(requests);
    }
}
