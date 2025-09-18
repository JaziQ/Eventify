package eventify.service;

import eventify.model.Booking;
import eventify.repository.BookingRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class BookingService {

    private final BookingRepository bookingRepository;

    @Autowired
    public BookingService(BookingRepository bookingRepository) {
        this.bookingRepository = bookingRepository;
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
        bookingRepository.deleteById(id);
    }
}
