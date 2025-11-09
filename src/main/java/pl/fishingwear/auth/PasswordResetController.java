package pl.fishingwear.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import pl.fishingwear.model.AuthProvider;
import pl.fishingwear.model.User;
import pl.fishingwear.repository.UserRepository;
import pl.fishingwear.service.MailService;
import pl.fishingwear.service.PasswordResetService;

import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class PasswordResetController {

    private final UserRepository userRepository;
    private final PasswordResetService passwordResetService;
    private final MailService mailService;

    @GetMapping("/resetowanie-hasla")
    public String showForgotPasswordPage() {
        return "auth/forgot-password";
    }

    @PostMapping("/forgot-password")
    public String processForgotPassword(@RequestParam("email") String email, Model model) {
        Optional<User> userOpt = userRepository.findByEmail(email.toLowerCase().trim());

        if (userOpt.isEmpty()) {
            model.addAttribute("error", "Nie znaleziono użytkownika o podanym adresie e-mail.");
            return "auth/forgot-password";
        }

        User user = userOpt.get();
        var token = passwordResetService.createTokenForUser(user);

        String resetLink = "http://localhost:8080/reset-password?token=" + token.getToken();
        mailService.sendPasswordResetEmail(user.getEmail(), resetLink);

        model.addAttribute("message", "Link do resetu hasła został wysłany na e-mail.");
        return "auth/forgot-password";
    }

    @GetMapping("/reset-password")
    public String showResetPasswordForm(@RequestParam("token") String token, Model model) {
        if (!passwordResetService.isTokenValid(token)) {
            model.addAttribute("error", "Token jest nieprawidłowy lub wygasł.");
            return "auth/reset-password";
        }

        model.addAttribute("token", token);
        return "auth/reset-password";
    }

    @PostMapping("/reset-password")
    public String processResetPassword(@RequestParam("token") String token,
                                       @RequestParam("password") String password,
                                       @RequestParam("confirmPassword") String confirmPassword,
                                       Model model) {
        if (!password.equals(confirmPassword)) {
            model.addAttribute("error", "Hasła nie są takie same.");
            model.addAttribute("token", token);
            return "auth/reset-password";
        }

        var resetOpt = passwordResetService.getValidToken(token);
        if (resetOpt.isEmpty()) {
            model.addAttribute("error", "Token jest nieprawidłowy lub wygasł.");
            return "auth/reset-password";
        }

        var resetToken = resetOpt.get();
        var user = resetToken.getUser();

        user.setPassword(passwordResetService.encodePassword(password));
        user.setAuthProvider(AuthProvider.LOCAL);
        userRepository.save(user);
        passwordResetService.deleteToken(resetToken);

        model.addAttribute("message", "Hasło zostało zmienione pomyślnie.");
        return "auth/reset-password";
    }
}
