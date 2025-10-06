package eventify.pageControllers;

import eventify.mapper.Mapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ui.Model;
import eventify.model.Booking;
import eventify.model.Ticket;
import eventify.service.BookingService;
import eventify.service.TicketService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.security.Principal;
import java.util.Optional;

@Slf4j
@Controller
@RequestMapping("/admin/bookings")
public class AdminBookingPageController {

    private final BookingService bookingService;
    private final TicketService ticketService;

    @Autowired
    public AdminBookingPageController(BookingService bookingService, TicketService ticketService) {
        this.bookingService = bookingService;
        this.ticketService = ticketService;
    }

    @GetMapping
    public String listAllBookings(Model model) {
        var bookings = bookingService.getAllBookingsSortedByEvent();
        model.addAttribute("bookings", bookings);
        return "admin/bookings/list";
    }

    @GetMapping("/{id}")
    public String viewBooking(@PathVariable Long id, Model model, Principal principal) {
        try {
            Optional<Booking> bookingOptional = bookingService.getBookingById(id);
            if(bookingOptional.isEmpty()) {
                model.addAttribute("error", "Booking not found");
                return "error";
            }

            Booking booking = bookingOptional.get();
            model.addAttribute("booking", Mapper.toBookingDTO(booking));
            return "admin/bookings/details";
        }catch (EntityNotFoundException e) {
            model.addAttribute("error", e.getMessage());
            return "error";
        }
    }

    @GetMapping("/{id}/edit")
    public String editBookingForm(@PathVariable Long id, Model model) {
        Booking booking = bookingService.getBookingById(id)
                .orElseThrow(() -> new EntityNotFoundException("Booking not found"));
        model.addAttribute("booking", Mapper.toBookingDTO(booking));
        return "admin/bookings/form";
    }

    @PostMapping("/{id}/edit")
    public String updateBooking(@PathVariable Long id,
                                @ModelAttribute Booking bookingDetails,
                                RedirectAttributes redirectAttributes) {
        try {
            bookingService.updateBooking(id, bookingDetails);
            redirectAttributes.addFlashAttribute("success", "Booking updated successfully!");
        } catch (EntityNotFoundException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/admin/bookings";
    }

    @PostMapping("/{id}/delete")
    public String deleteBooking(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            bookingService.deleteById(id);
            redirectAttributes.addFlashAttribute("success", "Booking deleted successfully!");
        } catch (EntityNotFoundException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/admin/bookings";
    }

    @PostMapping("/tickets/{id}/toggle-status")
    public String toggleTicketStatus(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            Ticket ticket = ticketService.getTicketById(id)
                    .orElseThrow(() -> new EntityNotFoundException("Ticket not found"));

            if (ticket.getStatus() == Ticket.TicketStatus.SOLD) {
                ticketService.updateTicketStatus(id, Ticket.TicketStatus.CANCELLED);
            } else {
                ticketService.updateTicketStatus(id, Ticket.TicketStatus.SOLD);
            }

            redirectAttributes.addFlashAttribute("success", "Ticket status updated!");
        } catch (EntityNotFoundException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }

        return "redirect:/admin/bookings";
    }


}
