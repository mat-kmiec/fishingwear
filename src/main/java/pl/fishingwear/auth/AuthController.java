package pl.fishingwear.auth;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AuthController {

    @GetMapping("/logowanie")
    public String login() {
        return "auth/login";
    }

    @GetMapping("/rejestracja")
    public String registration() {
        return "auth/register";
    }
}
