package eventify.restControllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import eventify.dto.EventDTO;
import eventify.mapper.Mapper;
import eventify.model.Event;
import eventify.service.EventService;
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
import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = EventController.class, excludeAutoConfiguration = SecurityAutoConfiguration.class)
class EventControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private EventService eventService;

    private Event event;
    private EventDTO eventDTO;

    @BeforeEach
    void setUp() {
        event = new Event();
        event.setId(1L);
        event.setName("Test Event");
        event.setDescription("Description");
        event.setLocation("NY");
        event.setStartTime(LocalDateTime.now().plusDays(1));
        event.setEndTime(LocalDateTime.now().plusDays(2));
        event.setPrice(BigDecimal.valueOf(100));
        event.setCapacity(50L);

        eventDTO = Mapper.toEventDTO(event);
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    void getAllEvents() throws Exception {
        Mockito.when(eventService.getAllEvents()).thenReturn(List.of(event));

        mockMvc.perform(get("/api/events"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(eventDTO.getId()))
                .andExpect(jsonPath("$[0].name").value(eventDTO.getName()))
                .andExpect(jsonPath("$[0].location").value(eventDTO.getLocation()));
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    void getEventById() throws Exception {
        Mockito.when(eventService.getEventById(1L)).thenReturn(event);

        mockMvc.perform(get("/api/events/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(eventDTO.getId()))
                .andExpect(jsonPath("$.name").value(eventDTO.getName()))
                .andExpect(jsonPath("$.location").value(eventDTO.getLocation()));
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    void createEvent() throws Exception {
        Mockito.when(eventService.save(any(Event.class))).thenReturn(event);

        mockMvc.perform(post("/api/events")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(eventDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(eventDTO.getId()))
                .andExpect(jsonPath("$.name").value(eventDTO.getName()))
                .andExpect(jsonPath("$.location").value(eventDTO.getLocation()));
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    void updateEvent() throws Exception {
        Event updatedEvent = new Event();
        updatedEvent.setId(1L);
        updatedEvent.setName("Updated Event");
        updatedEvent.setDescription("Updated Description");
        updatedEvent.setLocation("LA");

        EventDTO updatedDTO = Mapper.toEventDTO(updatedEvent);

        Mockito.when(eventService.updateEvent(anyLong(), any(Event.class))).thenReturn(updatedEvent);

        mockMvc.perform(put("/api/events/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(updatedDTO.getId()))
                .andExpect(jsonPath("$.name").value(updatedDTO.getName()))
                .andExpect(jsonPath("$.location").value(updatedDTO.getLocation()));
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    void deleteEvent() throws Exception {
        Mockito.doNothing().when(eventService).deleteEvent(1L);

        mockMvc.perform(delete("/api/events/1"))
                .andExpect(status().isNoContent());
    }
}
