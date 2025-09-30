package eventify.controller;

import eventify.mapper.Mapper;
import eventify.model.Event;
import eventify.service.EventService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/events")
public class EventPageController {

    private final EventService eventService;

    @Autowired
    public EventPageController(EventService eventService) {
        this.eventService = eventService;
    }

    @GetMapping
    public String eventPage(Model model) {
        model.addAttribute("events", eventService.getAllEvents()
                .stream()
                .map(Mapper::toEventDTO)
                .toList());
        return "events/list";
    }

    @GetMapping("/{id}")
    public String eventById(@PathVariable Long id, Model model) {
        try {
            model.addAttribute("event", Mapper.toEventDTO(eventService.getEventById(id)));
            return "events/details";
        } catch (EntityNotFoundException e) {
            return "redirect:/events?error=notfound";
        }
    }

    @GetMapping("/new")
    public String createForm(Model model) {
        model.addAttribute("event", new Event());
        return "events/form";
    }

    @PostMapping
    public String createEvent(@Valid @ModelAttribute("event") Event event) {
        eventService.save(event);
        return "redirect:/events";
    }

    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id, Model model) {
        try {
            Event event = eventService.getEventById(id);
            model.addAttribute("event", event);
            return "events/form";
        } catch (EntityNotFoundException e) {
            return "redirect:/events?error=notfound";
        }
    }

    @PostMapping("/{id}")
    public String updateEvent(@PathVariable Long id, @Valid @ModelAttribute("event") Event updatedEvent) {
        try {
            eventService.updateEvent(id, updatedEvent);
            return "redirect:/events/" + id;
        } catch (EntityNotFoundException e) {
            return "redirect:/events?error=notfound";
        }
    }

    @PostMapping("/{id}/delete")
    public String deleteEvent(@PathVariable Long id) {
        try {
            eventService.deleteEvent(id);
            return "redirect:/events";
        } catch (EntityNotFoundException e) {
            return "redirect:/events?error=notfound";
        }
    }
}
