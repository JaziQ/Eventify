package eventify.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class HomePageController {

    @GetMapping("/")
    public String homePage() {
        return "home";
    }

    @GetMapping("/login")
    public String showLoginForm() {
        return "users/login";
    }

}
