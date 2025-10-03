package eventify.restControllers;

import eventify.dto.EventDTO;
import eventify.mapper.Mapper;
import eventify.model.Event;
import eventify.service.EventService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/events")
public class EventController {

    private final EventService eventService;

    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

    @GetMapping
    public List<EventDTO> getAllEvents() {
        return eventService.getAllEvents().stream()
                .map(Mapper::toEventDTO)
                .toList();
    }

    @GetMapping("/{id}")
    public EventDTO getEventById(@PathVariable Long id) {
        try {
            return Mapper.toEventDTO(eventService.getEventById(id));
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Event not found");
        }
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EventDTO createEvent(@Valid @RequestBody Event event) {
        return Mapper.toEventDTO(eventService.save(event));
    }

    @PutMapping("/{id}")
    public EventDTO updateEvent(@PathVariable Long id, @Valid @RequestBody Event updatedEvent) {
        try {
            return Mapper.toEventDTO(eventService.updateEvent(id, updatedEvent));
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Event not found");
        }
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteEvent(@PathVariable Long id) {
        try {
            eventService.deleteEvent(id);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Event not found");
        }
    }
}
