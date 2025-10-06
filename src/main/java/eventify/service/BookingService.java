package eventify.service;

import eventify.model.Booking;
import eventify.model.Event;
import eventify.model.User;
import eventify.repository.BookingRepository;
import eventify.repository.EventRepository;
import eventify.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
public class BookingService {

    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final EventRepository eventRepository;

    @Autowired
    public BookingService(BookingRepository bookingRepository, UserRepository userRepository,
                          EventRepository eventRepository) {
        this.bookingRepository = bookingRepository;
        this.userRepository = userRepository;
        this.eventRepository = eventRepository;
    }

    public Booking save(Booking booking) {
        return bookingRepository.save(booking);
    }

    public Optional<Booking> getBookingById(long id) {
        return bookingRepository.findById(id);
    }

    public List<Booking> getAllByUserId(Long userId) {
        return bookingRepository.findByUserId(userId);
    }

    public List<Booking> getAllBookings() {
        List<Booking> bookings = new ArrayList<>();
        bookingRepository.findAll().forEach(bookings::add);
        return bookings;
    }

    @Transactional
    public Booking updateBookingStatus(Long id, Booking.BookingStatus status) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Booking not found"));
        booking.setBookingStatus(status);
        return bookingRepository.save(booking);
    }

    @Transactional
    public void deleteById(long id) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Booking not found"));
        bookingRepository.delete(booking);
    }

    @Transactional
    public Booking createBooking(Long userId, Long eventId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new RuntimeException("Event not found"));

        Booking booking = new Booking();
        booking.setUser(user);
        booking.setEvent(event);
        booking.setBookingStatus(Booking.BookingStatus.PENDING);
        booking.setBookingDate(LocalDateTime.now());

        return bookingRepository.save(booking);
    }

    @Transactional
    public void updateBooking(Long id, Booking bookingDetails) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Booking not found"));

        booking.setBookingStatus(bookingDetails.getBookingStatus());
        booking.setBookingDate(bookingDetails.getBookingDate());
        bookingRepository.save(booking);
    }

    public List<Booking> getAllBookingsSortedByEvent() {
        return ((List<Booking>) bookingRepository.findAll())
                .stream()
                .sorted(Comparator.comparing(b -> b.getEvent().getName()))
                .toList();
    }

    public boolean existsByUserAndEvent(User user, Event event) {
        return bookingRepository.existsByUserAndEvent(user, event);
    }
}
