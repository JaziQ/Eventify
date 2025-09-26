package eventify.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import eventify.dto.BookingDTO;
import eventify.mapper.Mapper;
import eventify.model.Booking;
import eventify.model.Booking.BookingStatus;
import eventify.model.Event;
import eventify.model.Ticket;
import eventify.model.User;
import eventify.service.BookingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = BookingController.class, excludeAutoConfiguration = SecurityAutoConfiguration.class)

class BookingControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private BookingService bookingService;

    private BookingDTO bookingDTO;

    private Booking booking;

    @BeforeEach
    void setUp() {
        booking = new Booking();
        booking.setId(1L);
        booking.setUser(new User());
        booking.setEvent(new Event());
        booking.setTicket(new Ticket());
        booking.setBookingStatus(BookingStatus.PENDING);
        booking.setBookingDate(LocalDateTime.now());

        bookingDTO = Mapper.toBookingDTO(booking);
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    void getAllBookings() throws Exception {
        Mockito.when(bookingService.getAllBookings()).thenReturn(List.of(booking));

        mockMvc.perform(get("/api/bookings"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(bookingDTO.getId()))
                .andExpect(jsonPath("$[0].bookingStatus").value(bookingDTO.getBookingStatus().name()))
                .andExpect(jsonPath("$[0].bookingDate").exists());
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    void getBookingById() throws Exception {
        Mockito.when(bookingService.getBookingById(1L)).thenReturn(Optional.of(booking));

        mockMvc.perform(get("/api/bookings/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(bookingDTO.getId()))
                .andExpect(jsonPath("$.bookingStatus").value(bookingDTO.getBookingStatus().name()))
                .andExpect(jsonPath("$.bookingDate").exists());
    }


    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    void createBooking() throws Exception {
        Mockito.when(bookingService.save(any())).thenReturn(booking);

        mockMvc.perform(post("/api/bookings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(bookingDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(bookingDTO.getId()))
                .andExpect(jsonPath("$.bookingStatus").value(bookingDTO.getBookingStatus().name()))
                .andExpect(jsonPath("$.bookingDate").exists());
    }

    @Test
    @WithMockUser(username = "user", roles = {"ADMIN"})
    void updateBookingStatus() throws Exception {
        booking.setBookingStatus(BookingStatus.CONFIRMED);
        Mockito.when(bookingService.updateBookingStatus(anyLong(), any())).thenReturn(booking);

        mockMvc.perform(put("/api/bookings/1/status")
                        .param("status", "CONFIRMED"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.bookingStatus").value("CONFIRMED"));
    }


    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void deleteBooking() throws Exception {
        Mockito.doNothing().when(bookingService).deleteById(anyLong());

        mockMvc.perform(delete("/api/bookings/1")
                        .with(csrf()))
                .andExpect(status().isNoContent());
    }

}
