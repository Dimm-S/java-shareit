package ru.practicum.shareit.item;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.shareit.client.BaseClient;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;

@Service
public class ItemClient extends BaseClient {
    private static final String API_PREFIX = "/items";

    @Autowired
    public ItemClient(@Value("${shareit-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                        .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                        .build()
        );
    }

    public ResponseEntity<Object> saveItem(ItemDto itemDto, Long ownerId) {
        return post("", ownerId, itemDto);
    }

    public ResponseEntity<Object> saveComment(Long itemId, CommentDto commentDto, Long userId) {
        return post("/" + itemId + "/comment", userId, commentDto);
    }

    public ResponseEntity<Object> updateItem(Long id, ItemDto itemDto, Long ownerId) {
        return patch("/" + id, ownerId, itemDto);
    }

    public ResponseEntity<Object> getItemById(Long id, Long userId) {
        return get("/" + id, userId);
    }

    public ResponseEntity<Object> getAllItems(Long userId) {
        return get("", userId);
    }

    public ResponseEntity<Object> getItemsByQuery(String query) {
        return get("/search?text=" + query);
    }

    public void deleteItem(Long id, Long ownerId) {
        delete("/" + id, ownerId);
    }
}
