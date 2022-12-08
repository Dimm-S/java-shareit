package ru.practicum.shareit.request;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ItemRequestRepository extends JpaRepository<ItemRequest, Long> {
    List<ItemRequest> findAllByRequesterId(Long requesterId);

    @Query(nativeQuery = true,
            value = "SELECT *" +
                    "FROM requests " +
                    "WHERE requester_id != ?")
    List<ItemRequest> getAllOtherUsersRequests(PageRequest pageRequest, Long userId);
}
