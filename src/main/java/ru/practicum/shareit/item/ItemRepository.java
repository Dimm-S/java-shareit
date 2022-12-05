package ru.practicum.shareit.item;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Long> {

    @Query(nativeQuery = true,
            value = "SELECT * " +
                    "FROM items " +
                    "WHERE owner_id = ?")
    List<Item> getAllItemsByUserId(Long userId);

    @Query(nativeQuery = true,
            value = "SELECT * " +
                    "FROM items " +
                    "WHERE request_id = ?")
    List<Item> getAllItemsByRequestId(Long requestId);
}
