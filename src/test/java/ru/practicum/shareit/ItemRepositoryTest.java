package ru.practicum.shareit;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.request.ItemRequestRepository;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
@DataJpaTest
public class ItemRepositoryTest {
    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ItemRequestRepository itemRequestRepository;

    private User user1;
    private Item item1;
    private User user2;
    private Item item2;
    private ItemRequest itemRequest1;
    private ItemRequest itemRequest2;


    @BeforeEach
    void setup() {
        user1 = userRepository.save(new User(1L, "user1", "user1@email"));
        user2 = userRepository.save(new User(2L, "user2", "user2@email"));

        itemRequest1 = itemRequestRepository.save(new ItemRequest(1L, "desc1", 1L, null));
        itemRequest2 = itemRequestRepository.save(new ItemRequest(2L, "desc2", 1L, null));


        item1 = itemRepository.save(new Item(
                1L, "item1", "descItem1", true, 1L, 1L));
        item2 = itemRepository.save(new Item(
                2L, "item2", "descItem2", true, 2L, 1L));

    }

    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    @Test
    void getAllItemsByUserIdTest() {
        final List<Item> itemsList1 = itemRepository.getAllItemsByUserId(1L);
        assertNotNull(itemsList1);
    }

    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    @Test
    void getAllItemsByRequestIdTest() {
        final List<Item> itemsList2 = itemRepository.getAllItemsByRequestId(1L);
        assertNotNull(itemsList2);
        assertEquals(1, itemsList2.get(0).getRequestId());
    }

    @AfterEach
    void afterEach() {
        itemRepository.deleteAll();
        itemRequestRepository.deleteAll();
        userRepository.deleteAll();
    }
}
