package eventify.mapper;

import eventify.dto.*;
import eventify.model.*;

public class Mapper {

    public static UserDTO toUserDTO(User user) {
        UserDTO dto = new UserDTO();
        dto.setId(user.getId());
        dto.setFirstName(user.getFirstName());
        dto.setLastName(user.getLastName());
        dto.setEmail(user.getEmail());
        dto.setBirthDate(user.getBirthDate());
        return dto;
    }

    public static BookingDTO toBookingDTO(Booking booking) {
        BookingDTO dto = new BookingDTO();
        dto.setId(booking.getId());
        dto.setUserId(booking.getUser().getId());
        dto.setEventId(booking.getEvent().getId());
        dto.setBookingStatus(booking.getBookingStatus());
        dto.setBooking_date(booking.getBooking_date());
        return dto;
    }

    public static EventDTO toEventDTO(Event event) {
        EventDTO dto = new EventDTO();
        dto.setId(event.getId());
        dto.setName(event.getName());
        dto.setDescription(event.getDescription());
        dto.setLocation(event.getLocation());
        dto.setPrice(event.getPrice());
        dto.setCapacity(event.getCapacity());
        dto.setStartTime(event.getStartTime());
        dto.setEndTime(event.getEndTime());
        return dto;
    }

    public static ReviewDTO toReviewDTO(Review review) {
        ReviewDTO dto = new ReviewDTO();
        dto.setId(review.getId());
        dto.setEventId(review.getEvent().getId());
        dto.setUserId(review.getUser().getId());
        dto.setReviewText(review.getReviewText());
        dto.setReviewRating(review.getReviewRating());
        return dto;
    }

    public static TicketDTO toTicketDTO(Ticket ticket) {
        TicketDTO dto = new TicketDTO();
        dto.setId(ticket.getId());
        dto.setUserId(ticket.getUser().getId());
        dto.setEventId(ticket.getEvent().getId());
        dto.setPrice(ticket.getPrice());
        dto.setStatus(ticket.getStatus());
        return dto;
    }
}
