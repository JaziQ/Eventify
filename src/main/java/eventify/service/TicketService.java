package eventify.service;

import eventify.model.Ticket;
import eventify.model.Event;
import eventify.model.User;
import eventify.repository.TicketRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;


@Service
public class TicketService {

    private final TicketRepository ticketRepository;

    @Autowired
    public TicketService(TicketRepository ticketRepository) {
        this.ticketRepository = ticketRepository;
    }

    public Ticket save(Ticket ticket) {
        return ticketRepository.save(ticket);
    }

    public List<Ticket> getTicketsByUserId(Long id) {
        return ticketRepository.findAllTicketsByUserId(id);
    }

    public List<Ticket> getTicketsByEventId(Long eventId) {
        return ticketRepository.findAllByEventId(eventId);
    }

    public List<Ticket> getTicketsByUser(User user) {
        return ticketRepository.findAllByUser(user);
    }

    public Ticket save(Event event, User user) {
        int sold = getSoldTickets(event);
        if (sold >= event.getCapacity()) {
            throw new RuntimeException("No tickets available");
        }

        Ticket ticket = new Ticket();
        ticket.setEvent(event);
        ticket.setUser(user);
        ticket.setPrice(event.getPrice() != null ? event.getPrice() : BigDecimal.ZERO);
        ticket.setStatus(Ticket.TicketStatus.SOLD);

        ticket.setUniqueCode(UUID.randomUUID().toString());

        return ticketRepository.save(ticket);
    }

    @Transactional
    public void updateTicketStatus(Long ticketId, Ticket.TicketStatus status) {
        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new EntityNotFoundException("Ticket not found"));
        ticket.setStatus(status);
        ticketRepository.save(ticket);
    }

    public int getSoldTickets(Event event) {
        return ticketRepository.countByEventAndStatus(event, Ticket.TicketStatus.SOLD);
    }

    public long getRemainingTickets(Event event) {
        return event.getCapacity() - getSoldTickets(event);
    }

    @Transactional
    public Ticket updateTicket(Long id, Ticket updatedTicket) {
        Ticket existingTicket = ticketRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Ticket not found"));
        existingTicket.setPrice(updatedTicket.getPrice());
        existingTicket.setStatus(updatedTicket.getStatus());
        return ticketRepository.save(existingTicket);
    }

    @Transactional
    public void deleteTicket(Long id) {
        Ticket ticket = ticketRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Ticket not found"));
        ticketRepository.delete(ticket);
    }

    public Ticket getTicketById(Long ticketId) {
        return ticketRepository.findTicketById(ticketId)
                .orElseThrow(() -> new EntityNotFoundException("Ticket not found"));
    }
}
