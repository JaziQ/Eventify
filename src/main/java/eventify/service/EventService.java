package eventify.service;

import eventify.model.Event;
import eventify.repository.EventRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class EventService {

    private final EventRepository eventRepository;

    @Autowired
    public EventService(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    public Event save(Event event) {
        return eventRepository.save(event);
    }

    public Event getEventById(Long id) {
        return eventRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Event not found"));    }

    public List<Event> getAllEvents() {
        List<Event> events = new ArrayList<>();
        eventRepository.findAll().forEach(events::add);
        return events;
    }

    public void deleteEvent(Long id) {
        eventRepository.deleteById(id);
    }

    public List<Event> findAllByLocation(String location){
        return eventRepository.findAllByLocation(location);
    }

    @Transactional
    public Event updateEvent(Long id, Event updatedEvent) {
        Event existingEvent = eventRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Event not found"));
        existingEvent.setName(updatedEvent.getName());
        existingEvent.setDescription(updatedEvent.getDescription());
        existingEvent.setStartTime(updatedEvent.getStartTime());
        existingEvent.setEndTime(updatedEvent.getEndTime());
        existingEvent.setLocation(updatedEvent.getLocation());
        existingEvent.setPrice(updatedEvent.getPrice());
        existingEvent.setCapacity(updatedEvent.getCapacity());
        existingEvent.setUpdatedAt(LocalDateTime.now());
        return eventRepository.save(existingEvent);
    }
}
