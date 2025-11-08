package pl.fishingwear.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import pl.fishingwear.dto.UserRegistrationDto;
import pl.fishingwear.service.UserService;

@Controller
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;

    @GetMapping("/logowanie")
    public String login() {
        return "auth/login";
    }

    @GetMapping("/rejestracja")
    public String registration(Model model) {
        UserRegistrationDto user = new UserRegistrationDto();
        model.addAttribute("user", user);
        return "auth/register";
    }

    @PostMapping("/register")
    public String register(UserRegistrationDto userRegistrationDto) {
        userService.register(userRegistrationDto);
        return "redirect:/";
    }
}
