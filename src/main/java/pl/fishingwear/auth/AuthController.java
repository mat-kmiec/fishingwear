package pl.fishingwear.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import pl.fishingwear.dto.UserRegistrationDto;
import pl.fishingwear.exception.EmailAlreadyExistException;
import pl.fishingwear.exception.PasswordNotMatchException;
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
        if (!model.containsAttribute("user")) {
            model.addAttribute("user", new UserRegistrationDto());
        }
        return "auth/register";
    }

    @PostMapping("/register")
    public String register(@ModelAttribute("user") UserRegistrationDto userRegistrationDto, RedirectAttributes redirectAttributes) {
        try{
            userService.register(userRegistrationDto);
            return "redirect:/";
        }catch (EmailAlreadyExistException | PasswordNotMatchException e){
            redirectAttributes.addFlashAttribute("user", userRegistrationDto);
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/rejestracja";
        }

    }
}
