package eventify.repository;

import eventify.model.Ticket;
import org.springframework.data.repository.CrudRepository;
import java.util.List;

public interface TicketRepository extends CrudRepository<Ticket, Long> {
    List<Ticket> findAllByEventId(Long eventId);

    List<Ticket> findAllTicketsByUserId(Long id);
}