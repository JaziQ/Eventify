package eventify.controller;

import eventify.dto.UserDTO;
import eventify.model.User;
import eventify.service.UserService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/users")
public class UserPageController {

    private final UserService userService;

    public UserPageController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/signup")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new UserDTO());
        return "users/signup";
    }

    @PostMapping("/signup")
    public String registerUser(@Valid @ModelAttribute("user") UserDTO userDTO){
        userDTO.setRole(User.Role.USER);
        userService.save(userDTO);
        return "redirect:/users/login";
    }

    @GetMapping("/login")
    public String showLoginForm() {
        return "users/login";
    }

    @GetMapping("/{username}")
    public String userDetailsByUsername(@PathVariable String username, Model model) {
        try {
            UserDTO user = userService.getUserByUsername(username);
            model.addAttribute("user", user);
            return "users/details";
        } catch (EntityNotFoundException e) {
            return "redirect:/users?error=notfound";
        }
    }

    @GetMapping("/new")
    public String createForm(Model model) {
        model.addAttribute("user", new UserDTO());
        return "users/form";
    }

    @PostMapping
    public String createUser(@Valid @ModelAttribute("user") UserDTO userDTO) {
        userService.save(userDTO);
        return "redirect:/users";
    }

    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id, Model model) {
        try {
            UserDTO user = userService.getUserById(id);
            model.addAttribute("user", user);
            return "users/form";
        } catch (EntityNotFoundException e) {
            return "redirect:/users?error=notfound";
        }
    }

    @PostMapping("/{id}")
    public String updateUser(@PathVariable Long id, @Valid @ModelAttribute("user") UserDTO userDTO) {
        try {
            userService.updateUser(id, userDTO);
            return "redirect:/users/" + id;
        } catch (EntityNotFoundException e) {
            return "redirect:/users?error=notfound";
        }
    }

    @PostMapping("/{id}/delete")
    public String deleteUser(@PathVariable Long id) {
        try {
            userService.deleteUser(id);
            return "redirect:/users";
        } catch (EntityNotFoundException e) {
            return "redirect:/users?error=notfound";
        }
    }
}
