package ru.practicum.shareit.request;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestInfoDto;
import ru.practicum.shareit.user.UserService;

import java.util.ArrayList;
import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ItemRequestController.class)
@AutoConfigureMockMvc
public class ItemRequestControllerTest {
    @MockBean
    private ItemRequestService itemRequestService;
    @MockBean
    private UserService userService;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper mapper;

    private final ItemRequestDto itemRequestDto = new ItemRequestDto(
            1L,
            "description",
            null
    );

    private final ItemRequestInfoDto itemRequestInfoDto = new ItemRequestInfoDto(
            1L,
            "description",
            null,
            new ArrayList<>()
    );

    @Test
    void addRequestTest() throws Exception {
        when(itemRequestService.saveRequest(any(ItemRequestDto.class), anyLong()))
                .thenReturn(itemRequestDto);

        mockMvc.perform(post("/requests")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(itemRequestDto))
                        .header("X-Sharer-User-Id", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(itemRequestDto.getId()));

        verify(itemRequestService, times(1))
                .saveRequest(itemRequestDto, 1L);
    }

    @Test
    void getRequestByIdTest() throws Exception {
        when(itemRequestService.getRequestById(anyLong(), anyLong()))
                .thenReturn(itemRequestInfoDto);

        mockMvc.perform(get("/requests/" + 1)
                        .header("X-Sharer-User-Id", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(itemRequestInfoDto.getId()));

        verify(itemRequestService, times(1))
                .getRequestById(1L, 1L);
    }

    @Test
    void getAllRequestsByUser() throws Exception {
        when(itemRequestService.getAllRequestsByUser(anyLong()))
                .thenReturn(new ArrayList<>(Collections.singleton(itemRequestInfoDto)));

        mockMvc.perform(get("/requests")
                        .header("X-Sharer-User-Id", 1L))
                .andExpect(status().isOk());

        verify(itemRequestService, times(1))
                .getAllRequestsByUser(1L);
    }
}
