package eventify.service;

import eventify.model.Ticket;
import eventify.repository.TicketRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


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

}
