package eventify.mapper;

import eventify.dto.*;
import eventify.model.*;

public class Mapper {

    public static UserDTO toUserDTO(User user) {
        if (user == null) return null;
        UserDTO dto = new UserDTO();
        dto.setId(user.getId());
        dto.setFirstName(user.getFirstName());
        dto.setLastName(user.getLastName());
        dto.setUsername(user.getUsername());
        dto.setEmail(user.getEmail());
        dto.setBirthDate(user.getBirthDate());
        dto.setRole(user.getRole());
        return dto;
    }

    public static User toUserEntity(UserDTO dto) {
        if (dto == null) return null;
        User user = new User();
        user.setId(dto.getId());
        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        user.setUsername(dto.getUsername());
        user.setEmail(dto.getEmail());
        user.setBirthDate(dto.getBirthDate());
        user.setRole(dto.getRole());
        return user;
    }

    public static BookingDTO toBookingDTO(Booking booking) {
        if (booking == null) return null;
        BookingDTO dto = new BookingDTO();
        dto.setId(booking.getId());
        dto.setUserId(booking.getUser().getId());
        dto.setEventId(booking.getEvent().getId());
        dto.setBookingStatus(booking.getBookingStatus());
        dto.setBookingDate(booking.getBookingDate());
        return dto;
    }

    public static Booking toBookingEntity(BookingDTO dto, User user, Event event) {
        if (dto == null) return null;
        Booking booking = new Booking();
        booking.setId(dto.getId());
        booking.setUser(user);
        booking.setEvent(event);
        booking.setBookingStatus(dto.getBookingStatus());
        booking.setBookingDate(dto.getBookingDate());
        return booking;
    }

    public static EventDTO toEventDTO(Event event) {
        if (event == null) return null;
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

    public static Event toEventEntity(EventDTO dto) {
        if (dto == null) return null;
        Event event = new Event();
        event.setId(dto.getId());
        event.setName(dto.getName());
        event.setDescription(dto.getDescription());
        event.setLocation(dto.getLocation());
        event.setPrice(dto.getPrice());
        event.setCapacity(dto.getCapacity());
        event.setStartTime(dto.getStartTime());
        event.setEndTime(dto.getEndTime());
        return event;
    }

    public static ReviewDTO toReviewDTO(Review review) {
        if (review == null) return null;
        ReviewDTO dto = new ReviewDTO();
        dto.setId(review.getId());
        dto.setEventId(review.getEvent().getId());
        dto.setUserId(review.getUser().getId());
        dto.setReviewText(review.getReviewText());
        dto.setReviewRating(review.getReviewRating());
        return dto;
    }

    public static Review toReviewEntity(ReviewDTO dto, User user, Event event) {
        if (dto == null) return null;
        Review review = new Review();
        review.setId(dto.getId());
        review.setUser(user);
        review.setEvent(event);
        review.setReviewText(dto.getReviewText());
        review.setReviewRating(dto.getReviewRating());
        return review;
    }

    public static TicketDTO toTicketDTO(Ticket ticket) {
        if (ticket == null) return null;
        TicketDTO dto = new TicketDTO();
        dto.setId(ticket.getId());
        dto.setUserId(ticket.getUser().getId());
        dto.setEventId(ticket.getEvent().getId());
        dto.setPrice(ticket.getPrice());
        dto.setStatus(ticket.getStatus());
        return dto;
    }

    public static Ticket toTicketEntity(TicketDTO dto, User user, Event event) {
        if (dto == null) return null;
        Ticket ticket = new Ticket();
        ticket.setId(dto.getId());
        ticket.setUser(user);
        ticket.setEvent(event);
        ticket.setPrice(dto.getPrice());
        ticket.setStatus(dto.getStatus());
        return ticket;
    }
}
