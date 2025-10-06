package eventify.repository;

import eventify.model.Booking;
import eventify.model.Event;
import eventify.model.User;
import org.springframework.data.repository.CrudRepository;
import java.util.List;

public interface BookingRepository extends CrudRepository<Booking, Long> {
    List<Booking> findByUserId(Long userId);

    boolean existsByUserAndEvent(User user, Event event);
}