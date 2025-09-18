package eventify.controller;

import eventify.dto.BookingDTO;
import eventify.mapper.Mapper;
import eventify.model.Booking;
import eventify.service.BookingService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/bookings")
public class BookingController {

    private final BookingService bookingService;

    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @GetMapping
    public List<BookingDTO> getAllBookings() {
        return bookingService.getAllBookings().stream()
                .map(Mapper::toBookingDTO)
                .toList();
    }

    @GetMapping("/{id}")
    public BookingDTO getBookingById(@PathVariable Long id) {
        return bookingService.getBookingById(id)
                .map(Mapper::toBookingDTO)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Booking not found"));
    }

    @GetMapping("/user/{userId}")
    public List<BookingDTO> getBookingsByUser(@PathVariable Long userId) {
        return bookingService.getAllByUserId(userId).stream()
                .map(Mapper::toBookingDTO)
                .toList();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BookingDTO createBooking(@Valid @RequestBody Booking booking) {
        return Mapper.toBookingDTO(bookingService.save(booking));
    }

    @PutMapping("/{id}/status")
    public BookingDTO updateBookingStatus(@PathVariable Long id, @Valid @RequestParam Booking.BookingStatus status) {
        try {
            return Mapper.toBookingDTO(bookingService.updateBookingStatus(id, status));
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Booking not found");
        }
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteBooking(@PathVariable Long id) {
        try {
            bookingService.deleteById(id);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Booking not found");
        }
    }
}
