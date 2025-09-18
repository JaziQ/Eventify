package eventify.repository;

import eventify.model.Event;
import org.springframework.data.repository.CrudRepository;
import java.util.List;

public interface EventRepository extends CrudRepository<Event, Long> {

    List<Event> findAllByLocation(String location);

}