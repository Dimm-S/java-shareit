package ru.practicum.shareit.booking;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingInfoDto;

import java.time.LocalDateTime;
import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BookingController.class)
@AutoConfigureMockMvc
public class BookingControllerTest {
    @MockBean
    private BookingService bookingService;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper mapper;

    private final BookingDto bookingDto = new BookingDto(
            1L,
            1L,
            LocalDateTime.of(2023, 1, 1, 0, 0),
            LocalDateTime.of(2023, 2, 1, 0, 0)
    );

    private final BookingInfoDto bookingInfoDto = new BookingInfoDto(
            1L,
            LocalDateTime.of(2023, 1, 1, 0, 0),
            LocalDateTime.of(2023, 2, 1, 0, 0),
            Status.WAITING,
            1L,
            1L,
            "item",
            new BookingInfoDto.BookerDto(1L, "booker"),
            new BookingInfoDto.ItemDto(1L, "name", 1L)
    );

    @Test
    void addBookingTest() throws Exception {
        when(bookingService.saveBooking(any(BookingDto.class), anyLong()))
                .thenReturn(bookingInfoDto);

        mockMvc.perform(post("/bookings")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(bookingDto))
                .header("X-Sharer-User-Id", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(bookingInfoDto.getId()));

        verify(bookingService, times(1))
                .saveBooking(bookingDto, 1L);
    }

    @Test
    void getBookingTest() throws Exception {
        when(bookingService.getBookingById(anyLong(), anyLong()))
                .thenReturn(bookingInfoDto);

        mockMvc.perform(get("/bookings/" + 1)
                .header("X-Sharer-User-Id", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(bookingInfoDto.getId()));

        verify(bookingService, times(1))
                .getBookingById(1L, 1L);
    }

    @Test
    void approveBookingByIdTest() throws Exception {
        bookingInfoDto.setStatus(Status.APPROVED);
        when(bookingService.approveBookingById(anyLong(), anyLong(), anyBoolean()))
                .thenReturn(bookingInfoDto);

        mockMvc.perform(patch("/bookings/" + 1)
                .header("X-Sharer-User-Id", 1L)
                .param("approved", String.valueOf(true)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(bookingInfoDto.getStatus().toString()));

        verify(bookingService, times(1))
                .approveBookingById(1L, 1L, true);
    }

    @Test
    void getAllBookingsByUserIdTest() throws Exception {
        when(bookingService.getAllBookingsByOwnerId(anyLong(), anyString(), any(PageRequest.class)))
                .thenReturn(Collections.emptyList());

        mockMvc.perform(get("/bookings")
                .header("X-Sharer-User-Id", 1L)
                .param("state", "ALL")
                .param("from", String.valueOf(1))
                .param("size", String.valueOf(10)))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));

        verify(bookingService, times(1)).getAllBookingsByUserId(
                1L, "ALL", PageRequest.of(0, 10, Sort.by("id").descending()));
    }

    @Test
    void getAllBookingsByOwnerIdTest() throws Exception {
        when(bookingService.getAllBookingsByOwnerId(anyLong(), anyString(), any(PageRequest.class)))
                .thenReturn(Collections.emptyList());

        mockMvc.perform(get("/bookings")
                        .header("X-Sharer-User-Id", 1L)
                        .param("state", "ALL")
                        .param("from", String.valueOf(1))
                        .param("size", String.valueOf(10)))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));

        verify(bookingService, times(1)).getAllBookingsByUserId(
                1L, "ALL", PageRequest.of(0, 10, Sort.by("id").descending()));
    }
}
