package eventify.pageControllers;

import eventify.mapper.Mapper;
import eventify.model.Ticket;
import eventify.model.User;
import eventify.service.TicketService;
import eventify.service.UserService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@Controller
@RequestMapping("/tickets")
public class TicketPageController {

    private final TicketService ticketService;
    private final UserService userService;

    @Autowired
    public TicketPageController(TicketService ticketService, UserService userService) {
        this.ticketService = ticketService;
        this.userService = userService;
    }

    @GetMapping
    public String ticketsPage(Model model, Principal principal) {
        User user = Mapper.toUserEntity(userService.getUserByUsername(principal.getName()));
        model.addAttribute("tickets", ticketService.getTicketsByUser(user)
                .stream()
                .map(Mapper::toTicketDTO)
                .toList());
        return "tickets/list";
    }

    @GetMapping("/{id}")
    public String getTicketDetails(@PathVariable Long id, Model model) {
        Ticket ticket = ticketService.getTicketById(id);

        model.addAttribute("ticket", Mapper.toTicketDTO(ticket));
        return "tickets/details";
    }

    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id, Model model) {
        try {
            Ticket ticket = ticketService.getTicketById(id);
            model.addAttribute("ticket", Mapper.toTicketDTO(ticket));
            return "ticket/form";
        } catch (EntityNotFoundException e) {
            return "redirect:/events?error=notfound";
        }
    }

    @PostMapping("/{id}")
    public String updateTicket(@PathVariable Long id, @Valid @ModelAttribute("ticket") Ticket updatedTicket) {
        try {
            ticketService.updateTicket(id, updatedTicket);
            return "redirect:/tickets/" + id;
        } catch (EntityNotFoundException e) {
            return "redirect:/events?error=notfound";
        }
    }

    @PostMapping("/{id}/delete")
    public String deleteTicket(@PathVariable Long id) {
        try {
            ticketService.deleteTicket(id);
            return "redirect:/tickets/" + id;
        } catch (EntityNotFoundException e) {
            return "redirect:/events?error=notfound";
        }
    }
}
