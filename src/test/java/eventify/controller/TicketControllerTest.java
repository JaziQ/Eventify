package eventify.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import eventify.dto.TicketDTO;
import eventify.mapper.Mapper;
import eventify.model.Event;
import eventify.model.Ticket;
import eventify.model.User;
import eventify.service.TicketService;
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

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = TicketController.class, excludeAutoConfiguration = SecurityAutoConfiguration.class)
class TicketControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private TicketService ticketService;

    private Ticket ticket;
    private TicketDTO ticketDTO;

    @BeforeEach
    void setUp() {
        User user = new User();
        user.setId(1L);

        Event event = new Event();
        event.setId(1L);

        ticket = new Ticket();
        ticket.setId(1L);
        ticket.setUser(user);
        ticket.setEvent(event);
        ticket.setPrice(BigDecimal.valueOf(100));
        ticket.setStatus(Ticket.TicketStatus.AVAILABLE);

        ticketDTO = Mapper.toTicketDTO(ticket);
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    void getTicketsByUser() throws Exception {
        Mockito.when(ticketService.getTicketsByUserId(1L)).thenReturn(List.of(ticket));

        mockMvc.perform(get("/api/tickets/user/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(ticketDTO.getId()))
                .andExpect(jsonPath("$[0].userId").value(ticketDTO.getUserId()))
                .andExpect(jsonPath("$[0].eventId").value(ticketDTO.getEventId()))
                .andExpect(jsonPath("$[0].price").value(ticketDTO.getPrice()))
                .andExpect(jsonPath("$[0].status").value(ticketDTO.getStatus().name()));
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    void getTicketsByEvent() throws Exception {
        Mockito.when(ticketService.getTicketsByEventId(1L)).thenReturn(List.of(ticket));

        mockMvc.perform(get("/api/tickets/event/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(ticketDTO.getId()))
                .andExpect(jsonPath("$[0].userId").value(ticketDTO.getUserId()))
                .andExpect(jsonPath("$[0].eventId").value(ticketDTO.getEventId()))
                .andExpect(jsonPath("$[0].price").value(ticketDTO.getPrice()))
                .andExpect(jsonPath("$[0].status").value(ticketDTO.getStatus().name()));
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    void createTicket() throws Exception {
        Mockito.when(ticketService.save(any())).thenReturn(ticket);

        mockMvc.perform(post("/api/tickets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(ticketDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(ticketDTO.getId()))
                .andExpect(jsonPath("$.userId").value(ticketDTO.getUserId()))
                .andExpect(jsonPath("$.eventId").value(ticketDTO.getEventId()))
                .andExpect(jsonPath("$.price").value(ticketDTO.getPrice()))
                .andExpect(jsonPath("$.status").value(ticketDTO.getStatus().name()));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void updateTicket() throws Exception {
        ticket.setStatus(Ticket.TicketStatus.SOLD);
        Mockito.when(ticketService.updateTicket(anyLong(), any())).thenReturn(ticket);

        mockMvc.perform(put("/api/tickets/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(ticketDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(ticketDTO.getId()))
                .andExpect(jsonPath("$.status").value("SOLD"));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void deleteTicket() throws Exception {
        Mockito.doNothing().when(ticketService).deleteTicket(anyLong());

        mockMvc.perform(delete("/api/tickets/1"))
                .andExpect(status().isNoContent());
    }
}
