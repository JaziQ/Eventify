package eventify.pageControllers;

import eventify.mapper.Mapper;
import eventify.model.Booking;
import eventify.model.Event;
import eventify.model.Ticket;
import eventify.model.User;
import eventify.service.BookingService;
import eventify.service.EventService;
import eventify.service.TicketService;
import eventify.service.UserService;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.Optional;

@Slf4j
@Controller
@RequestMapping("/bookings")
public class BookingPageController {

    private final BookingService bookingService;
    private final EventService eventService;
    private final UserService userService;
    private final TicketService ticketService;

    @Autowired
    public BookingPageController(BookingService bookingService, EventService eventService,
                                 UserService userService, TicketService ticketService) {
        this.bookingService = bookingService;
        this.eventService = eventService;
        this.userService = userService;
        this.ticketService = ticketService;
    }

    @GetMapping
    public String listBookings(Model model, Principal principal) {
        User user = userService.getUserEntityByUsername(principal.getName());
        model.addAttribute("bookings", bookingService.getAllByUserId(user.getId())
                .stream()
                .map(Mapper::toBookingDTO)
                .toList());
        return "bookings/list";
    }

    @GetMapping("/{id}")
    public String viewBooking(@PathVariable Long id, Model model, Principal principal) {
        try {
            Optional<Booking> bookingOptional = bookingService.getBookingById(id);
            if (bookingOptional.isEmpty()) {
                model.addAttribute("error", "Booking not found.");
                return "error";
            }

            Booking booking = bookingOptional.get();

            if (!booking.getUser().getUsername().equals(principal.getName())) {
                model.addAttribute("error", "You are not authorized to view this booking.");
                return "error";
            }

            model.addAttribute("booking", Mapper.toBookingDTO(booking));
            return "bookings/details";
        } catch (EntityNotFoundException e) {
            model.addAttribute("error", e.getMessage());
            return "error";
        }
    }

    @GetMapping("/confirm/{eventId}")
    public String showConfirmBooking(@PathVariable Long eventId, Model model, Principal principal) {
        try {
            Event event = eventService.getEventById(eventId);
            model.addAttribute("event", Mapper.toEventDTO(event));

            if (principal != null) {
                User user = userService.getUserEntityByUsername(principal.getName());
                boolean alreadyBooked = bookingService.existsByUserAndEvent(user, event);
                if (alreadyBooked) {
                    model.addAttribute("error", "You have already booked this event!");
                    return "bookings/alreadyBooked";
                }
            }
            return "bookings/confirm";
        } catch (EntityNotFoundException e) {
            model.addAttribute("error", e.getMessage());
            return "/error";
        }
    }

    @PostMapping("/confirm/{eventId}/submit")
    public String confirmBooking(@PathVariable Long eventId, Principal principal,
                                 RedirectAttributes redirectAttributes) {
        try {
            User user = userService.getUserEntityByUsername(principal.getName());
            Event event = eventService.getEventById(eventId);

            if (bookingService.existsByUserAndEvent(user, event)) {
                redirectAttributes.addFlashAttribute("error", "You have already booked this event!");
                return "redirect:/events";
            }

            Ticket ticket = ticketService.save(event, user);

            Booking booking = new Booking();
            booking.setUser(user);
            booking.setEvent(event);
            booking.setTicket(ticket);
            booking.setBookingStatus(Booking.BookingStatus.PENDING);
            booking.setBookingDate(LocalDateTime.now());

            bookingService.save(booking);

            return "redirect:/bookings/success";
        } catch (EntityNotFoundException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/events";
        }
    }

    @GetMapping("/success")
    public String bookingSuccess() {
        return "bookings/success";
    }

    @PostMapping("/{id}/delete")
    public String deleteBooking(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            bookingService.deleteById(id);
            redirectAttributes.addFlashAttribute("success", "Booking deleted successfully!");
        } catch (EntityNotFoundException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/bookings";
    }

}
