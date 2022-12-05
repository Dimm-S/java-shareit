package ru.practicum.shareit.item;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.comment.dto.CommentDto;
import ru.practicum.shareit.comment.dto.CommentInfoDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemInfoDto;

import java.util.ArrayList;
import java.util.Collections;

import static org.mockito.Mockito.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(ItemController.class)
@AutoConfigureMockMvc
public class ItemControllerTest {
    @MockBean
    private ItemService itemService;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper mapper;

    private final ItemDto itemDto = new ItemDto(1L,"name","desc",
            true,1L
    );
    private final ItemInfoDto itemInfoDto = new ItemInfoDto(1L,"name","desc",
            1L,true,
            new ItemInfoDto.BookingDto(),
            new ItemInfoDto.BookingDto(),
            new ArrayList<>()
    );
    private final CommentDto commentDto = new CommentDto(1L, "text");
    private final CommentInfoDto commentInfoDto = new CommentInfoDto(1L, "text",
            1L, "authorName", null);

    @Test
    void addItemTest() throws Exception {
        when(itemService.saveItem(any(ItemDto.class), anyLong()))
                .thenReturn(itemDto);

        mockMvc.perform(post("/items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(itemDto))
                        .header("X-Sharer-User-Id", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(itemDto.getId()));

        verify(itemService, times(1))
                .saveItem(itemDto, 1L);
    }

    @Test
    void getItemTest() throws Exception {
        when(itemService.getItemById(anyLong(),anyLong()))
                .thenReturn(itemInfoDto);

        mockMvc.perform(get("/items/" + 1)
                        .header("X-Sharer-User-Id", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(itemInfoDto.getId()));

        verify(itemService, times(1))
                .getItemById(1L, 1L);
    }

    @Test
    void updateItem() throws Exception {
        when(itemService.updateItem(anyLong(), any(ItemDto.class), anyLong()))
                .thenReturn(itemDto);

        mockMvc.perform(patch("/items/" + 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(itemDto))
                        .header("X-Sharer-User-Id", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(itemDto.getId()));

        verify(itemService, times(1))
                .updateItem(1L, itemDto, 1L);
    }

    @Test
    void addCommentTest() throws Exception {
        when(itemService.saveComment(anyLong(), any(CommentDto.class), anyLong()))
                .thenReturn(commentInfoDto);

        mockMvc.perform(post("/items/" + 1 + "/comment")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(commentDto))
                .header("X-Sharer-User-Id", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(commentInfoDto.getId()));

        verify(itemService, times(1))
                .saveComment(1L, commentDto, 1L);
    }

    @Test
    void getAllItemsByUserIdTest() throws Exception {
        when(itemService.getAllItems(anyLong()))
                .thenReturn(new ArrayList<>(Collections.singleton(itemInfoDto)));

        mockMvc.perform(get("/items/")
                        .header("X-Sharer-User-Id", 1L))
                .andExpect(status().isOk());

        verify(itemService, times(1))
                .getAllItems(1L);
    }

    @Test
    void deleteItemTest() throws Exception {
        doNothing().when(itemService).deleteItem(anyLong(), anyLong());

        mockMvc.perform(delete("/items/" + 1)
                .header("X-Sharer-User-Id", 1L))
                .andExpect(status().isOk());

        verify(itemService, times(1))
                .deleteItem(1L, 1L);
    }

    @Test
    void getItemsByQueryTest() throws Exception {
        when(itemService.getItemsByQuery(anyString()))
                .thenReturn(new ArrayList<>(Collections.singleton(itemDto)));

        mockMvc.perform(get("/items/search")
                        .param("text", "string")
                        .header("X-Sharer-User-Id", 1L))
                .andExpect(status().isOk());

        verify(itemService, times(1))
                .getItemsByQuery("string");
    }
}
