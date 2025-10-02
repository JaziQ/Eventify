package eventify.repository;

import eventify.model.Event;
import eventify.model.Ticket;
import eventify.model.User;
import org.springframework.data.repository.CrudRepository;
import java.util.List;
import java.util.Optional;

public interface TicketRepository extends CrudRepository<Ticket, Long> {
    List<Ticket> findAllByEventId(Long eventId);

    List<Ticket> findAllTicketsByUserId(Long id);

    int countByEventAndStatus(Event event, Ticket.TicketStatus status);

    Optional<Ticket> findTicketById(Long ticketId);
}