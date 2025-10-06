package eventify.restControllers;

import eventify.dto.TicketDTO;
import eventify.mapper.Mapper;
import eventify.model.Ticket;
import eventify.service.TicketService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/tickets")
public class TicketController {

    private final TicketService ticketService;

    public TicketController(TicketService ticketService) {
        this.ticketService = ticketService;
    }

    @GetMapping("/user/{userId}")
    public List<TicketDTO> getTicketsByUser(@PathVariable Long userId) {
        return ticketService.getTicketsByUserId(userId).stream()
                .map(Mapper::toTicketDTO)
                .toList();
    }

    @GetMapping("/event/{eventId}")
    public List<TicketDTO> getTicketsByEvent(@PathVariable Long eventId) {
        return ticketService.getTicketsByEventId(eventId).stream()
                .map(Mapper::toTicketDTO)
                .toList();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TicketDTO createTicket(@Valid @RequestBody Ticket ticket) {
        return Mapper.toTicketDTO(ticketService.save(ticket));
    }

    @PutMapping("/{id}")
    public TicketDTO updateTicket(@PathVariable Long id, @Valid @RequestBody Ticket updatedTicket) {
        try {
            return Mapper.toTicketDTO(ticketService.updateTicket(id, updatedTicket));
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Ticket not found");
        }
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteTicket(@PathVariable Long id) {
        try {
            ticketService.deleteTicket(id);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Ticket not found");
        }
    }
}
